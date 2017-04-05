package resources;

/**
 * Created by dn on 05/04/17.
 */
public class EliminaPrenotazioneFailException extends Throwable {
    public EliminaPrenotazioneFailException(String s) {
        super("Errore nell'eliminazione della prenotazione: "+s);
    }
}
