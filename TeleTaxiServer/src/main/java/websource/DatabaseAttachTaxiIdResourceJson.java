package websource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import control.GestoreFlottaTaxi;
import control.GestoreStatistica;
import model.Manager;
import model.Taxi;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import resources.exception.*;

/**
 * Created by dn on 22/04/17.
 */
public class DatabaseAttachTaxiIdResourceJson extends ServerResource {
    @Get("json")
    public String getTaxiByID() {
        Status toReturn;
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
        try {
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            Manager m = Manager.getInstance();
            if (m.getPassword().equalsIgnoreCase(series.getFirstValue("Authorization")))
                return gson.toJson(GestoreStatistica.getInstance().findTaxiByCodice(Integer.parseInt(getAttribute("id"))).getValues().get(0), Taxi.class);
            else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return new Gson().toJson(toReturn, Status.class);
            }
        } catch (FindTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
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
        } catch (FindOperatoreFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindPrenotazioneFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (InserisciManagerFailException e) {
            toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindManagerFailException e) {
            toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (Exception e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }

    @Delete("json")
    public String deleteTaxi() {
        Status toReturn;
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            Manager m = Manager.getInstance();
            if (m.getPassword().equalsIgnoreCase(series.getFirstValue("Authorization"))) {
                Taxi toDelete = (Taxi) GestoreStatistica.getInstance().findTaxiByCodice(Integer.parseInt(getAttribute("id"))).getValues().get(0);
                return gson.toJson(GestoreFlottaTaxi.getInstance().eliminaTaxi(toDelete), Taxi.class);
            } else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return new Gson().toJson(toReturn, Status.class);
            }
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindManagerFailException e) {
            toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (InserisciManagerFailException e) {
            toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (GetTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindPrenotazioneFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_PRENOTAZIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindOperatoreFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindClienteFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CLIENTE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (EliminaTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (Exception e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }

    }
}
