/*
 * Copyright (c) 2011 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.domain.desy.epics.typesupport;

import javax.annotation.Nonnull;

import org.csstudio.data.values.IMetaData;
import org.csstudio.data.values.INumericMetaData;
import org.csstudio.domain.desy.epics.types.EpicsGraphicsData;
import org.csstudio.domain.desy.epics.types.EpicsMetaData;
import org.csstudio.domain.desy.types.Limits;
import org.csstudio.domain.desy.typesupport.TypeSupportException;

/**
 * Conversion support for {@link Long}.
 *
 * @author bknerr
 * @since 11.05.2011
 */
final class LongConversionSupport extends EpicsIMetaDataTypeSupport<Long> {

    /**
     * Constructor.
     */
    public LongConversionSupport() {
        super(Long.class);
    }

    @Nonnull
    private Long toLong(final double d) {
        return Long.valueOf(Double.valueOf(d).longValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    protected EpicsMetaData convertToMetaData(@Nonnull final IMetaData data) throws TypeSupportException {
        final INumericMetaData numData = checkAndConvertToNumeric(data, Long.class);
        final EpicsGraphicsData<Long> gr =
            new EpicsGraphicsData<Long>(Limits.<Long>create(toLong(numData.getAlarmLow()),
                                                            toLong(numData.getAlarmHigh())),
                                        Limits.<Long>create(toLong(numData.getWarnLow()),
                                                            toLong(numData.getWarnHigh())),
                                        Limits.<Long>create(toLong(numData.getDisplayLow()),
                                                            toLong(numData.getDisplayHigh())));
        return EpicsMetaData.create(null, gr, null, null);
    }
}
