package org.tohasan.dlms.obisrecognizer.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * author LehaSan
 * date 25.02.2018
 */
public class CalculationUtils {
    private final static ScriptEngine engine;

    static {
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
    }

    public static String evaluate(String expression) throws ScriptException {
        return String.format("%s", engine.eval(expression));
    }
}
