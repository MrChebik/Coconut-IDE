package ru.mrchebik.exception;

import ru.mrchebik.exception.service.ThrowingConsumer;

import java.util.function.Consumer;

public class ExceptionUtils {
    protected static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
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
