package resources.exception;

/**
 * Created by dn on 05/04/17.
 */
public class FindManagerFailException extends Exception {
    public FindManagerFailException(String usernameManager) {
        super("Il manager " + usernameManager + " non e' stato trovato");
    }
}
