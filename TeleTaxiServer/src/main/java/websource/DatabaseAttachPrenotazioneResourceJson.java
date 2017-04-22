package websource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import control.GestorePrenotazione;
import control.GestoreStatistica;
import model.Prenotazione;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import resources.exception.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dn on 02/04/17.
 */
public class DatabaseAttachPrenotazioneResourceJson extends ServerResource {

    @Get("json")
    public String getPrenotazioni(){
        Status toReturn;
        try {
            GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss");
            Gson gson = gsonBuilder.create();
            String idOperatore = getQueryValue("operatore");
            if (idOperatore != null) {
                String dataTmp = getQueryValue("data");
                if (dataTmp != null) {
                    String hour = getQueryValue("hour");
                    Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataTmp + " " + hour);
                    List<Prenotazione> prenotazioneByOperatoreEData = (List<Prenotazione>) GestoreStatistica.getInstance().findPrenotazioneByOperatoreEData(idOperatore, date).getValues();
                    return gson.toJson(prenotazioneByOperatoreEData.toArray(new Prenotazione[prenotazioneByOperatoreEData.size()]), Prenotazione[].class);
                } else return gson.toJson(GestorePrenotazione.getInstance().getAllPrenotazioni(), Prenotazione[].class);
            } else return gson.toJson(GestorePrenotazione.getInstance().getAllPrenotazioni(), Prenotazione[].class);
        } catch (GetPrenotazioniFailException getPrenotazioniFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL,"FatalError", getPrenotazioniFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException connectionSQLFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", connectionSQLFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindOperatoreFailException findOperatoreFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL,"FatalError", findOperatoreFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindTaxiFailException findTaxiFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL,"FatalError", findTaxiFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindClienteFailException findClienteFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CLIENTE_FAIL,"FatalError", findClienteFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }

    @Put("json")
    public String createPrenotazione(String body){
        Status toReturn;
        try {
            Gson gson = new Gson();
            Prenotazione toAdd = gson.fromJson(body, Prenotazione.class);
            GestorePrenotazione.getInstance().inserisciPrenotazione(toAdd);
            return gson.toJson(toAdd,Prenotazione.class);
        } catch (InserisciPrenotazioneFailException inserisciPrenotazioneFail) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL,"FatalError",inserisciPrenotazioneFail.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException connectionSQLFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", connectionSQLFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }

    }

    @Post("json")
    public String updatePrenotazione(String body){
        Status toReturn;
        try{
            Gson gson = new Gson();
            Prenotazione toUpdate = gson.fromJson(body, Prenotazione.class);
            GestorePrenotazione.getInstance().updatePrenotazione(toUpdate);
            return gson.toJson(toUpdate, Prenotazione.class);
        } catch (UpdatePrenotazioneFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (Exception e) {
            toReturn = new Status(Status.CLIENT_ERROR_BAD_REQUEST, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }

}
