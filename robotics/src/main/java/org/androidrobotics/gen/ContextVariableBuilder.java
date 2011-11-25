package org.androidrobotics.gen;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;

/**
 * @author John Ericksen
 */
public class ContextVariableBuilder implements VariableBuilder {
    @Override
    public JExpression buildVariable() {
        return JExpr._this();
    }
}