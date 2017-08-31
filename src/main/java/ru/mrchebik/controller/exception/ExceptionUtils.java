package ru.mrchebik.controller.exception;

import ru.mrchebik.controller.exception.throwing.ThrowingConsumer;

import java.util.function.Consumer;

/**
 * Created by mrchebik on 8/31/17.
 */
public class ExceptionUtils {
    public static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
            ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    System.err.println("Exception occurred : " + exceptionClass.cast(ex).getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

}
