/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import java.util.List;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VNumber;
import org.epics.vtype.ValueFactory;


/**
 *
 * @author carcassi
 */
abstract class OneArgNumericFormulaFunction implements FormulaFunction {

    private final String name;
    private final String description;
    private final List<Class<?>> argumentTypes;
    private final List<String> argumentNames;
    
    public OneArgNumericFormulaFunction(String name, String description, String argName) {
        this.name = name;
        this.description = description;
        this.argumentTypes = Arrays.<Class<?>>asList(VNumber.class);
        this.argumentNames = Arrays.asList(argName);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return argumentTypes;
    }

    @Override
    public List<String> getArgumentNames() {
        return argumentNames;
    }

    @Override
    public Class<?> getReturnType() {
        return VNumber.class;
    }

    @Override
    public Object calculate(List<Object> args) {
        return ValueFactory.newVDouble(calculate(((VNumber) args.get(0)).getValue().doubleValue()), ValueFactory.newTime(Timestamp.now()));
    }
    
    abstract double calculate(double arg);
    
}
