package resources.exception;

/**
 * Created by dn on 09/04/17.
 */
public class UpdateOperatoreTelefonicoFailException extends Exception {
    public UpdateOperatoreTelefonicoFailException(String s) {
        super("Errore durante l'update dell'operatore telefonico:"+s);

    }
}
