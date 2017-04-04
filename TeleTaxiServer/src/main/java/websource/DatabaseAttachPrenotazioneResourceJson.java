package websource;

import com.google.gson.Gson;
import control.GestorePrenotazione;
import model.Prenotazione;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 * Created by dn on 02/04/17.
 */
public class DatabaseAttachPrenotazioneResourceJson extends ServerResource {

    @Get
    public String getPrenotazioni(){
        Gson gson = new Gson();
        return gson.toJson(GestorePrenotazione.getInstance().getAllPrenotazioni(),Prenotazione[].class);
    }

    @Put
    public String createPrenotazione(String body){
        Gson gson = new Gson();
        Prenotazione toAdd = gson.fromJson(body, Prenotazione.class);
        GestorePrenotazione.getInstance().inserisciPrenotazione(toAdd);
        return gson.toJson(toAdd,Prenotazione.class);
    }

}
