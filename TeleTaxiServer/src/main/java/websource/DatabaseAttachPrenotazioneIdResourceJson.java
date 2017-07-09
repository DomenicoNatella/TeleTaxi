package websource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import control.GestorePrenotazione;
import control.GestoreStatistica;
import model.Prenotazione;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import resources.exception.*;

/**
 * Created by dn on 22/04/17.
 */
public class DatabaseAttachPrenotazioneIdResourceJson extends ServerResource {

    @Get("json")
    public String getPrenotazioneByID() {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
        Status toReturn;
        try {
            gson = new Gson();
            return gson.toJson(GestoreStatistica.getInstance().findPrenotazioneByProgressivo(getAttribute("id")).getValues().get(0));
        } catch (FindPrenotazioneFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindOperatoreFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (GetTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindClienteFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CLIENTE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (Exception e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }

    @Delete("json")
    public String deletePrenotazione() {
        Status toReturn;
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
        try {
            Prenotazione toDelete = (Prenotazione) GestoreStatistica.getInstance().findPrenotazioneByProgressivo(getAttribute("id")).getValues().get(0);
            return gson.toJson(GestorePrenotazione.getInstance().eliminaPrenotazione(toDelete), Prenotazione.class);
        } catch (FindPrenotazioneFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindOperatoreFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (GetTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindClienteFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CLIENTE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (EliminaPrenotazioneFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (Exception e) {
            toReturn = new Status(Status.CLIENT_ERROR_BAD_REQUEST, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }
}
