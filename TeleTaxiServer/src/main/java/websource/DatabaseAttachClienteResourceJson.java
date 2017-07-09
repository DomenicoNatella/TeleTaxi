package websource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Cliente;
import org.restlet.data.Status;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import resources.exception.ErrorCodes;
import resources.exception.InserisciClienteFailException;

/**
 * Created by dn on 03/04/17.
 */
public class DatabaseAttachClienteResourceJson extends ServerResource {

    @Put("json")
    public String inserisciCliente(String body){
        Status toReturn;
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
            Cliente toAdd = gson.fromJson(body, Cliente.class);
            toAdd.inserisciCliente();
            Cliente returned = new Cliente(toAdd.getCodiceCliente(), toAdd.getNome(), toAdd.getCognome(), toAdd.getDataDiNascita(), toAdd.getTelefono());
            return gson.toJson(returned, Cliente.class);
        } catch (InserisciClienteFailException inserisciClienteFail) {
            toReturn = new Status(ErrorCodes.ECCEZIONE_CLIENTE_FAIL,"FatalError",inserisciClienteFail.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }catch (Exception e){
            toReturn = new Status(ErrorCodes.ECCEZIONE_CLIENTE_FAIL, "FatalError", e.getMessage());
            setStatus(toReturn);
            return new Gson().toJson(toReturn, Status.class);
        }
    }
}
