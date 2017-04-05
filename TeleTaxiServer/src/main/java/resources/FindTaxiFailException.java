package resources;

/**
 * Created by dn on 05/04/17.
 */
public class FindTaxiFailException extends Throwable {
    public FindTaxiFailException(String s) {
        super("Il taxi non e' stato trovato: "+s);
    }
}
