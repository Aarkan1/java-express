package express.database.exceptions;

/**
 * @author Johan Wirén
 */
public class DatabaseNotEnabledException extends Exception{
    public DatabaseNotEnabledException(String message) {
        super(message);
    }
}
