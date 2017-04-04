package websource;

import com.google.gson.Gson;
import control.GestorePersonale;
import model.OperatoreTelefonico;
import org.restlet.Server;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Created by dn on 02/04/17.
 */
public class DatabaseAttachOperatoreTelefonicoResourceJson extends ServerResource {

    @Get
    public String getOperatori(){
        Gson gson = new Gson();
        return gson.toJson(GestorePersonale.getInstance().getAllOperatoriTelefonici(), OperatoreTelefonico[].class);
    }

    @Put
    public String createOperatoreTelefonico(String body){
        Gson gson = new Gson();
        OperatoreTelefonico toAdd = gson.fromJson(body, OperatoreTelefonico.class);
        GestorePersonale.getInstance().inserisciOperatoreTelefonico(toAdd);
        return gson.toJson(toAdd,OperatoreTelefonico.class);
    }
}
