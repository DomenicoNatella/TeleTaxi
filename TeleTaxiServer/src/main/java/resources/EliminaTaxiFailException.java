package resources;

/**
 * Created by dn on 05/04/17.
 */
public class EliminaTaxiFailException extends Exception {
    public EliminaTaxiFailException(String message) {
        super("Errore nell'eliminazione del taxi: "+message);
    }
}
