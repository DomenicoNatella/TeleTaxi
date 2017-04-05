package websource;

import com.google.gson.Gson;
import model.Cliente;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.data.Status;
import resources.ErrorCodes;
import resources.InserisciClienteFailException;

/**
 * Created by dn on 03/04/17.
 */
public class DatabaseAttachClienteResourceJson extends ServerResource {
    @Put
    public String inserisciCliente(String body){
        Gson gson = new Gson();
        Status toReturn;
        Cliente toAdd = gson.fromJson(body, Cliente.class);
        try {
            toAdd.inserisciCliente();
            return gson.toJson(toAdd,Cliente.class);
        } catch (InserisciClienteFailException inserisciClienteFail) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CLIENTE_FAIL,"FatalError",inserisciClienteFail.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn,Status.class);
        }catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_CLIENTE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return gson.toJson(toReturn, Status.class);
        }
    }
}
