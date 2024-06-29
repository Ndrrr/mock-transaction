package org.untitled.common.util;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Objects;

public final class ExceptionUtil {

    public static boolean isReadTimeoutError(Exception ex) {
        return isTypeOf(ex, SocketTimeoutException.class, ConnectException.class);
    }

    @SafeVarargs
    public static boolean isTypeOf(Throwable e, Class<? extends Throwable>... types) {
        for (Class<? extends Throwable> t : types) {
            if (t.isInstance(e)) {
                return true;
            }
        }

        if (Objects.isNull(e.getCause()) || e.equals(e.getCause())) {
            return false;
        }

        return isTypeOf(e.getCause(), types);
    }

    private ExceptionUtil() {
    }

}
