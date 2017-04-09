package resources;

/**
 * Created by dn on 08/04/17.
 */
public class UpdateManagerFailException extends Exception {
    public UpdateManagerFailException(String s) {
        super("Errore durante l'update del manager:"+s);
    }
}
