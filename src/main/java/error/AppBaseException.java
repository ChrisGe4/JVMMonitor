package error;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Chris.Ge
 */
public class AppBaseException extends RuntimeException {
    public static final String ERROR = "error";
    public static final String REASON = "reason";
    public static final String HTTP_STATUS = "httpStatus";
    private static final long serialVersionUID = 1L;
    private final Map<String, String> attributes = Maps.newHashMap();

    public AppBaseException() {
        super();
    }

    /**
     * Message+Cause constructor.
     *
     * @param message The message of this Exception.
     * @param cause   The cause of this Exception.
     */
    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Message constructor.
     *
     * @param message Default constructor.
     */
    public AppBaseException(String message) {
        super(message);
    }

    /**
     * Cause constructor.
     *
     * @param cause The cause of this Exception.
     */
    public AppBaseException(Throwable cause) {
        super(cause);
    }

    public static <T extends AppBaseException> T getException(Class<T> clazz, String message,
        Throwable t, String httpStatus) {
        return getException(clazz, message, getError(t), httpStatus);
    }

    public static <T extends AppBaseException> T getException(Class<T> clazz, String message,
        Throwable t) {
        return getException(clazz, message, getError(t));
    }

    public static <T extends AppBaseException> T getException(Class<T> clazz, String message,
        String error, String httpStatus) {
        T exception = getException(clazz, message, error);

        if (!Strings.isNullOrEmpty(httpStatus))
            exception.setProperty(AppBaseException.HTTP_STATUS, httpStatus);
        return exception;
    }

    public static <T extends AppBaseException> T getException(Class<T> clazz, String message,
        String error) {
        T exception = null;
        try {
            exception = clazz.getConstructor(String.class).newInstance(message);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            exception = (T) new AppBaseException(message);
        }
        exception.setProperty(AppBaseException.ERROR, error);

        return exception;
    }

    public static String getError(Throwable t) {
        return t.getMessage() + "-Trace: " + getTrace(t);
    }

    private static String getTrace(Throwable t) {
        return Arrays.stream(t.getStackTrace()).limit(3).map(StackTraceElement::toString)
            .collect(Collectors.joining(" << "));
    }

    public void setProperty(String key, String value) {
        this.attributes.put(key, value);
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @Override
    public String toString() {
        return "{\"message:\":\"" + super.getMessage() + "\",\"" + ERROR + "\":\"" + attributes
            .get(ERROR) + "\"}";
    }
}
