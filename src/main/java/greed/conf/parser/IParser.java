package greed.conf.parser;

/**
 * Greed is good! Cheers!
 */
public interface IParser<T> {
    T apply(Object obj) throws ReflectiveOperationException;
}
