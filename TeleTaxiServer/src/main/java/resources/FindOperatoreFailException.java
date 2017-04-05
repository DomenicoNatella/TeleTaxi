package resources;

/**
 * Created by dn on 05/04/17.
 */
public class FindOperatoreFailException extends Exception {
    public FindOperatoreFailException(String s) {
        super("L'operatore non e' stato trovato: "+s);
    }
}
