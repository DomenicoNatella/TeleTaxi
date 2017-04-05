package resources;

/**
 * Created by dn on 05/04/17.
 */
public class GetOperatoriTelefoniciFailException extends Throwable {
    public GetOperatoriTelefoniciFailException(String s) {
        super("Si e' verificato un errore durante l'esecuzione della richiesta: "+s);
    }
}
