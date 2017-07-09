package websource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import control.GestorePersonale;
import model.Manager;
import model.OperatoreTelefonico;
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
public class DatabaseAttachOperatoreTelefonicoIdResourceJson extends ServerResource {

    @Get("json")
    public String getOperatoreTelefonicoByID() {
        Status toReturn;
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            Manager m = Manager.getInstance();
            if (m.getPassword().equalsIgnoreCase(series.getFirstValue("Authorization")))
                return gson.toJson(GestorePersonale.getInstance().findOperatore(getAttribute("id")), OperatoreTelefonico.class);
            else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return new Gson().toJson(toReturn, Status.class);
            }
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindManagerFailException e) {
            toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (InserisciManagerFailException e) {
            toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindOperatoreFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }

    @Delete("json")
    public String deleteOperatore() {
        Status toReturn;
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            Series<Header> series = (Series<Header>) getRequest().getHeaders();
            if (Manager.getInstance().getPassword().equalsIgnoreCase(series.getFirstValue("Authorization"))) {
                return gson.toJson(GestorePersonale.getInstance().eliminaOperatoreTelefonico(getAttribute("id")), OperatoreTelefonico.class);
            } else {
                toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", "Errore di autenticazione");
                setStatus(toReturn);
                return gson.toJson(toReturn, Status.class);
            }
        } catch (ConnectionSQLFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CONNESSIONE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindManagerFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (InserisciManagerFailException e) {
            toReturn = new Status(Status.CLIENT_ERROR_UNAUTHORIZED, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (FindOperatoreFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (EliminaOperatoreTelefonicoFailException e) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_OPERATORE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        } catch (Exception e) {
            toReturn = new Status(Status.CLIENT_ERROR_BAD_REQUEST, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }
}
