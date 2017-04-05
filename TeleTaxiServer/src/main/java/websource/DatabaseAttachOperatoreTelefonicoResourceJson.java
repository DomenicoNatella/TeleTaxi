package websource;

import com.google.gson.Gson;
import control.GestorePersonale;
import model.OperatoreTelefonico;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import resources.ConnectionSQLFailException;
import resources.ErrorCodes;
import resources.GetOperatoriTelefoniciFailException;
import resources.InserisciOperatoreFailException;

/**
 * Created by dn on 02/04/17.
 */
public class DatabaseAttachOperatoreTelefonicoResourceJson extends ServerResource {

    @Get
    public String getOperatori(){
        Gson gson = new Gson();
        Status toReturn;
        try {
            return gson.toJson(GestorePersonale.getInstance().getAllOperatoriTelefonici(), OperatoreTelefonico[].class);
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

    @Put
    public String createOperatoreTelefonico(String body) {
        Gson gson = new Gson();
        Status toReturn;
        try {
            OperatoreTelefonico toAdd = gson.fromJson(body, OperatoreTelefonico.class);
            GestorePersonale.getInstance().inserisciOperatoreTelefonico(toAdd);
            return gson.toJson(toAdd,OperatoreTelefonico.class);
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
}
