package websource;

import com.google.gson.Gson;
import control.GestorePersonale;
import model.Manager;
import model.OperatoreTelefonico;
import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import resources.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by dn on 02/04/17.
 */
public class DatabaseAttachOperatoreTelefonicoResourceJson extends ServerResource {

    @Get("json")
    public String getOperatori(){
        Gson gson = new Gson();
        Status toReturn;
        try {
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            Manager m = Manager.getInstance();
            if(m.getPassword().equalsIgnoreCase(series.getFirstValue("Authorization")))
                return gson.toJson(GestorePersonale.getInstance().getAllOperatoriTelefonici(), OperatoreTelefonico[].class);
            else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return gson.toJson(toReturn, Status.class);
            }
        } catch (GetOperatoriTelefoniciFailException getOperatoriTelefoniciFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", getOperatoriTelefoniciFailException.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        } catch (ConnectionSQLFailException connectionSQLFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", connectionSQLFailException.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        } catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        }
    }

    @Put("json")
    public String createOperatoreTelefonico(String body) {
        Gson gson = new Gson();
        Status toReturn;
        try {
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            if(Manager.getInstance().getPassword().equalsIgnoreCase(series.getFirstValue("Authorization"))) {
                OperatoreTelefonico toAdd = gson.fromJson(body, OperatoreTelefonico.class);
                GestorePersonale.getInstance().inserisciOperatoreTelefonico(toAdd);
                return gson.toJson(toAdd, OperatoreTelefonico.class);
            }else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return gson.toJson(toReturn, Status.class);
            }
        } catch (ConnectionSQLFailException connectionSQLFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", connectionSQLFailException.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        } catch (InserisciOperatoreFailException inserisciOperatoreFailException) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", inserisciOperatoreFailException.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        } catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        }
    }

    @Post("json")
    public String updateOperatoreTelefonico(String body){
        Gson gson = new Gson();
        Status toReturn;
        try{
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            if(Manager.getInstance().getPassword().equalsIgnoreCase(series.getFirstValue("Authorization"))) {
                OperatoreTelefonico toUpdate = gson.fromJson(body, OperatoreTelefonico.class);
                GestorePersonale.getInstance().updateOperatoreTelefonico(toUpdate);
                return gson.toJson(toUpdate, OperatoreTelefonico.class);
            }else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return gson.toJson(toReturn, Status.class);
            }
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        } catch (FindManagerFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        } catch (InserisciManagerFailException e) {
            toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        } catch (UpdateOperatoreTelefonicoFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        }
    }

}
