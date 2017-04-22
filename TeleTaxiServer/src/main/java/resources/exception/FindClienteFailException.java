package resources.exception;

/**
 * Created by dn on 05/04/17.
 */
public class FindClienteFailException extends Exception {
    public FindClienteFailException(String s) {
        super("Il cliente non e' stato trovato: "+s);
    }
}
