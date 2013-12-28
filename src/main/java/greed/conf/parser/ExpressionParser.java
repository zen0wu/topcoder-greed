package greed.conf.parser;

import greed.util.reflect.FullGeneric1;
import greed.util.reflect.ReflectionUtil;

import java.lang.reflect.Method;

/**
 * Greed is good! Cheers!
 */
public abstract class ExpressionParser<T> implements IParser<T>, FullGeneric1<T> {
    @Override
    public final T apply(Object obj) throws ReflectiveOperationException {
        return apply(obj.toString());
    }

    public T apply(String expression) throws ReflectiveOperationException {
        return parseExp(expression, typeOf1());
    }

    public abstract T raw(String expression);

    protected final <R> R parseExp(String expression, Class<R> returnType) throws ReflectiveOperationException {
        boolean isFuncCall = expression.endsWith(")");
        int p = expression.indexOf('(');
        if (p == -1) isFuncCall = false;
        if (isFuncCall) {
            String funcName = expression.substring(0, p);
            String[] paramExps = expression.substring(p + 1, expression.length() - 1).split(" ");
            for (int i = 0; i < paramExps.length; ++i)
                paramExps[i] = paramExps[i].trim();

            Method method = ReflectionUtil.findMethod(this.getClass(), funcName, returnType, paramExps.length);
            if (method != null) {
                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] params = new Object[paramExps.length];
                for (int i = 0; i < paramExps.length; ++i)
                    params[i] = parseExp(paramExps[i], paramTypes[i]);

                return returnType.cast(method.invoke(this, params));
            }
        }
        // Issue: Although not needed right now, but it's possible T = String,
        // in this situation, the parser would get confused, and an extra option is needed
        if (String.class.equals(returnType))
            return returnType.cast(expression);
        if (typeOf1().equals(returnType)) {
            return returnType.cast(raw(expression));
        }
        throw new ReflectiveOperationException("Cannot convert expression to type " + typeOf1().getName());
    }
}
