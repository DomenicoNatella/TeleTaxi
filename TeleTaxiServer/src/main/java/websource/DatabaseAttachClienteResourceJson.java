package websource;

import com.google.gson.Gson;
import model.Cliente;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Created by dn on 03/04/17.
 */
public class DatabaseAttachClienteResourceJson extends ServerResource {
    @Put
    public String inserisciCliente(String body){
        Gson gson = new Gson();
        Cliente toAdd = gson.fromJson(body, Cliente.class);
        toAdd.inserisciCliente();
        return gson.toJson(toAdd,Cliente.class);
    }
}
