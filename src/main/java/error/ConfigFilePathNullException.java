package error;

/**
 * @author Chris.Ge
 */
public class ConfigFilePathNullException extends AppBaseException {

    public ConfigFilePathNullException() {
        super();
    }

    /**
     * Message+Cause constructor.
     *
     * @param message The message of this Exception.
     * @param cause   The cause of this Exception.
     */
    public ConfigFilePathNullException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Message constructor.
     *
     * @param message Default constructor.
     */
    public ConfigFilePathNullException(String message) {
        super(message);
    }

    /**
     * Cause constructor.
     *
     * @param cause The cause of this Exception.
     */
    public ConfigFilePathNullException(Throwable cause) {
        super(cause);
    }

}
