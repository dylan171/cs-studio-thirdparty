/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.expression;

import org.epics.pvmanager.ReadRecipe;
import org.epics.pvmanager.ReadRecipeBuilder;
import org.epics.pvmanager.ReadFunction;
import org.epics.pvmanager.PVReaderDirector;

/**
 * An expression to read at the desired rate.
 * <p>
 * Don't implement objects with this interface, use {@link DesiredRateExpressionImpl}.
 *
 * @param <R> type of the read payload
 * @author carcassi
 */
public interface DesiredRateExpression<R> extends DesiredRateExpressionList<R> {
    
    /**
     * Changes the name for this expression
     * 
     * @param name new name
     * @return this
     */
    public DesiredRateExpression<R> as(String name);
    
    /**
     * Name of this expression.
     *
     * @return the expression name
     */
    public String getName();
    
    /**
     * The recipe for connect the channels for this expression.
     *
     * @return a data recipe
     */
    public void fillReadRecipe(PVReaderDirector director, ReadRecipeBuilder builder);
    
    /**
     * The function that calculates this expression.
     *
     * @return the expression function
     */
    public ReadFunction<R> getFunction();
    
    /**
     * The implementation of this expression.
     * 
     * @return the implementation
     */
    public DesiredRateExpressionImpl<R> getDesiredRateExpressionImpl();
}
