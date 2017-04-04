package websource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/**
 * Created by dn on 21/03/17.
 */
public class TeleTaxiServerWeb extends Application {

    @Override
    public Restlet createInboundRoot(){
        Router router = new Router(getContext());
        router.attach("/teleTaxi/operatoretelefonico",DatabaseAttachOperatoreTelefonicoResourceJson.class);
        router.attach("/teleTaxi/prenotazione", DatabaseAttachPrenotazioneResourceJson.class);
        router.attach("/teleTaxi/taxi", DatabaseAttachTaxiResourceJson.class);
        router.attach("/teleTaxi/cliente", DatabaseAttachClienteResourceJson.class);
        router.attachDefault(DatabaseAttachDefaultResource.class);
        return router;
    }

    public static void main(String[] args){
        Component component = null;
        try {
            component = new Component();
            component.getServers().add(Protocol.HTTP, 8080);
            component.getDefaultHost().attach(new TeleTaxiServerWeb());
            component.start();
        } catch (Exception e) {
            System.err.println("Errore nell'inizializzazione del WebServer. "+e.getMessage());
        }
    }

}
