package net.daneau.assnat.utils;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Supplier;

@Component
public class ErrorHandler {

    public <X extends Throwable> void assertSize(int expectedSize, Collection<?> collection, Supplier<? extends X> exceptionSupplier) throws X {
        if (collection.size() != expectedSize) {
            throw exceptionSupplier.get();
        }
    }

    public <X extends Throwable> void assertNotNull(Object object, Supplier<? extends X> exceptionSupplier) throws X {
        if (object == null) {
            throw exceptionSupplier.get();
        }
    }

    public <X extends Throwable> void assertLessThan(int lessThan, Collection<?> collection, Supplier<? extends X> exceptionSupplier) throws X {
        if (collection.size() >= lessThan) {
            throw exceptionSupplier.get();
        }
    }
}
