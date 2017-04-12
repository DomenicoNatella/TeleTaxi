package websource;

import com.google.gson.Gson;
import control.GestoreFlottaTaxi;
import model.Taxi;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import resources.ConnectionSQLFailException;
import resources.ErrorCodes;
import resources.GetTaxiFailException;
import resources.InserisciTaxiFailException;

/**
 * Created by dn on 02/04/17.
 */
public class DatabaseAttachTaxiResourceJson extends ServerResource {
    @Get("json")
    public String getTaxi(){
        Gson gson = new Gson();
        Status toReturn;
        try {
            return gson.toJson(GestoreFlottaTaxi.getInstance().getAllTaxi(), Taxi[].class);
        } catch (GetTaxiFailException getTaxiFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL,"FatalError", getTaxiFailException.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn,Status.class);
        } catch (ConnectionSQLFailException connectionSQLFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", connectionSQLFailException.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        }catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        }
    }

    @Put("json")
    public String createTaxi(String body){
        Gson gson = new Gson();
        Status toReturn;
        Taxi toAdd = gson.fromJson(body, Taxi.class);
        try {
            GestoreFlottaTaxi.getInstance().inserisciTaxi(toAdd);
            return gson.toJson(toAdd,Taxi.class);
        } catch (InserisciTaxiFailException inserisciTaxiFail) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL,"FatalError",inserisciTaxiFail.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn,Status.class);
        } catch (ConnectionSQLFailException connectionSQLFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", connectionSQLFailException.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        }catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_TAXI_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        }
    }
}
