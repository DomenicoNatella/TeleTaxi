package websource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import control.GestoreFlottaTaxi;
import model.Manager;
import model.Taxi;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import resources.exception.*;


/**
 * Created by dn on 02/04/17.
 */
public class DatabaseAttachTaxiResourceJson extends ServerResource {
    @Get("json")
    public String getTaxi(){
        Status toReturn;
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            Manager m = Manager.getInstance();
            if (m.getPassword().equalsIgnoreCase(series.getFirstValue("Authorization")))
                return gson.toJson(GestoreFlottaTaxi.getInstance().getAllTaxi(), Taxi[].class);
            else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return new Gson().toJson(toReturn, Status.class);
            }
        } catch (GetTaxiFailException getTaxiFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL,"FatalError", getTaxiFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException connectionSQLFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", connectionSQLFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }

    @Put("json")
    public String createTaxi(String body){
        Status toReturn;
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            Manager m = Manager.getInstance();
            if (m.getPassword().equalsIgnoreCase(series.getFirstValue("Authorization"))) {
                Taxi toAdd = gson.fromJson(body, Taxi.class);
                GestoreFlottaTaxi.getInstance().inserisciTaxi(toAdd);
                return new Gson().toJson(toAdd, Taxi.class);
            } else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return new Gson().toJson(toReturn, Status.class);
            }
        } catch (InserisciTaxiFailException inserisciTaxiFail) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL,"FatalError",inserisciTaxiFail.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException connectionSQLFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", connectionSQLFailException.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }

    @Post("json")
    public String updateTaxi(String body) {
        Status toReturn;
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            Taxi toUpdate = gson.fromJson(body, Taxi.class);
            return gson.toJson(GestoreFlottaTaxi.getInstance().updateTaxi(toUpdate), Taxi.class);
        } catch (UpdateTaxiFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (Exception e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }

}
