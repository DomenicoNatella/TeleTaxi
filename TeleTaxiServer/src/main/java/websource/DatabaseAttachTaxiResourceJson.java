package websource;

import com.google.gson.Gson;
import control.GestoreFlottaTaxi;
import model.Taxi;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Created by dn on 02/04/17.
 */
public class DatabaseAttachTaxiResourceJson extends ServerResource {
    @Get
    public String getTaxi(){
        Gson gson = new Gson();
        return gson.toJson(GestoreFlottaTaxi.getInstance().getAllTaxi(), Taxi[].class);
    }

    @Put
    public String createTaxi(String body){
        Gson gson = new Gson();
        Taxi toAdd = gson.fromJson(body, Taxi.class);
        GestoreFlottaTaxi.getInstance().inserisciTaxi(toAdd);
        return gson.toJson(toAdd,Taxi.class);
    }
}
