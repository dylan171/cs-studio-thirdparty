/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.archive.engine2;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.csstudio.apputil.args.ArgParser;
import org.csstudio.apputil.args.BooleanOption;
import org.csstudio.apputil.args.IntegerOption;
import org.csstudio.apputil.args.StringOption;
import org.csstudio.apputil.time.BenchmarkTimer;
import org.csstudio.archive.engine2.model.EngineModel;
import org.csstudio.archive.engine2.server.EngineServer;
import org.csstudio.archive.rdb.RDBArchivePreferences;
import org.csstudio.platform.logging.CentralLogger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/** Eclipse Application for CSS archive engine
 *  @author Kay Kasemir
 */
public class Application implements IApplication
{
    /** Database URL, user, password */
    private String url = RDBArchivePreferences.getURL(),
                   user = RDBArchivePreferences.getUser(),
                   password = RDBArchivePreferences.getPassword();

    /** HTTP Server port */
    private int port;

    /** Request file */
    private String engine_name;

    /** Application model */
    private EngineModel model;

    /** Obtain settings from preferences and command-line arguments
     *  @param args Command-line arguments
     *  @return <code>true</code> if OK.
     */
    @SuppressWarnings("nls")
    private boolean getSettings(final String args[])
    {
        // Create the parser and run it.
        final ArgParser parser = new ArgParser();
        final BooleanOption help_opt = new BooleanOption(parser, "-help",
                    "Display Help");
        final IntegerOption port_opt = new IntegerOption(parser, "-port", "4812",
                    "HTTP server port", 4812);
        final StringOption url_opt = new StringOption(parser, "-rdb_url", "jdbc:...",
                    "Database URL, overrides preference setting", this.url);
        final StringOption user_opt = new StringOption(parser, "-rdb_user", "arch_user",
                "Database user, overrides preference setting", this.user);
        final StringOption pass_opt = new StringOption(parser, "-rdb_password", "secret",
                "Database password, overrides preference setting", this.password);
        final StringOption engine_name_opt = new StringOption(parser,
                    "-engine", "demo_engine", "Engine config name", null);
        // Options handled by Eclipse,
        // but this way they show up in the help message
        new StringOption(parser, "-pluginCustomization", "/path/to/mysettings.ini",
                        "Eclipse plugin defaults", null);
        new StringOption(parser, "-data", "/home/fred/Workspace", "Eclipse workspace location", null);
        try
        {
            parser.parse(args);
        }
        catch (final Exception ex)
        {   // Bad options
            System.out.println(ex.getMessage());
            System.out.println(parser.getHelp());
            return false;
        }
        if (help_opt.get())
        {   // Help requested
            System.out.println(parser.getHelp());
            return false;
        }

        // Check arguments
        if (engine_name_opt.get() == null)
        {
            System.out.println("Missing option " + engine_name_opt.getOption());
            System.out.println(parser.getHelp());
            return false;
        }

        // Copy stuff from options into member vars.
        url = url_opt.get();
        user = user_opt.get();
        password = pass_opt.get();
        port = port_opt.get();
        engine_name = engine_name_opt.get();
        return true;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("nls")
    public Object start(final IApplicationContext context) throws Exception
    {
        final String args[] =
            (String []) context.getArguments().get("application.args");
        if (!getSettings(args)) {
            return EXIT_OK;
        }

        if (url == null)
        {
            System.out.println(
                    "No Database URL. Set via preferences or command-line");
            return EXIT_OK;
        }

        // Setup groups, channels, writer
        // This is all single-threaded!
        final Logger logger = CentralLogger.getInstance().getLogger(this);
        logger.info("Archive Engine " + EngineModel.VERSION);
        try
        {
            // connect the service with the given prefs
            final Map<String, Object> prefs = createConnectionPrefsMap();
            Activator.getDefault().getArchiveWriterService().connect(prefs);

            model = new EngineModel();
            // Setup takes some time, but engine server should already respond.
            EngineServer server;
            try
            {
                server = new EngineServer(model, port);
            }
            catch (final Exception ex)
            {
                logger.fatal("Cannot start server on port "
                                + port + ": " + ex.getMessage(), ex);
                return EXIT_OK;
            }

            boolean run = true;
            while (run)
            {
                logger.info("Reading configuration '" + engine_name + "'");
                final BenchmarkTimer timer = new BenchmarkTimer();
                try
                {
                    Activator.getDefault().getArchiveEngineConfigService().connect(prefs);
                    model.readConfig(engine_name, port);
                    Activator.getDefault().getArchiveEngineConfigService().disconnect();
                }
                catch (final Exception ex)
                {
                    logger.fatal(ex.getMessage());
                    return EXIT_OK;
                }
                timer.stop();
                logger.info("Read configuration: " + model.getChannelCount() +
                            " channels in " + timer.toString());

                // Run until model gets stopped via HTTPD or #stop()
                logger.info("Running, CA addr list: "
                    + System.getProperty("com.cosylab.epics.caj.CAJContext.addr_list"));
                model.start();
                while (true)
                {
                    Thread.sleep(1000);
                    if (model.getState() == EngineModel.State.SHUTDOWN_REQUESTED)
                    {
                        run = false;
                        break;
                    }
                    if (model.getState() == EngineModel.State.RESTART_REQUESTED) {
                        break;
                    }
                }
                // Stop sampling
                logger.info("ArchiveEngine ending");
                model.stop();
                model.clearConfig();
            }
            Activator.getDefault().getArchiveWriterService().disconnect();

            logger.info("ArchiveEngine stopped");
            server.stop();
        }
        catch (final Exception ex)
        {
            logger.fatal("Unhandled Main Loop Error", ex);
            ex.printStackTrace();
        }

        return EXIT_OK;
    }

    private Map<String, Object> createConnectionPrefsMap() {
        final Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put(RDBArchivePreferences.URL, url);
        prefs.put(RDBArchivePreferences.USER, user);
        prefs.put(RDBArchivePreferences.PASSWORD, password);
        return prefs;
    }

    /** {@inheritDoc} */
    public void stop()
    {
        if (model != null) {
            model.requestStop();
        }
    }
}
