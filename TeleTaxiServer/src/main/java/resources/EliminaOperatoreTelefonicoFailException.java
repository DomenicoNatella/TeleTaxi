package resources;

/**
 * Created by dn on 09/04/17.
 */
public class EliminaOperatoreTelefonicoFailException extends Exception {
    public EliminaOperatoreTelefonicoFailException(String s) {
        super("Errore nell'eliminazione dell'operatore telefonico: "+s);
    }
}
