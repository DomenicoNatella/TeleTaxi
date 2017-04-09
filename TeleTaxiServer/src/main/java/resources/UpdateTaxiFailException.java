package resources;

/**
 * Created by dn on 09/04/17.
 */
public class UpdateTaxiFailException extends Exception {
    public UpdateTaxiFailException(String s) {
        super("Errore durante l'update del taxi:"+s);
    }
}
