package websource;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import java.io.FileNotFoundException;

/**
 * Created by dn on 21/03/17.
 */
public class TeleTaxiServerWeb extends Application {

    @Override
    public Restlet createInboundRoot(){
        Router router = new Router(getContext());
        router.attach("/teletaxi/operatoretelefonico", DatabaseAttachOperatoreTelefonicoResourceJson.class);
        router.attach("/teletaxi/prenotazione", DatabaseAttachPrenotazioneResourceJson.class);
        router.attach("/teletaxi/taxi", DatabaseAttachTaxiResourceJson.class);
        router.attach("/teletaxi/cliente", DatabaseAttachClienteResourceJson.class);
        router.attach("/teletaxi/taxi/{id}", DatabaseAttachTaxiIdResourceJson.class);
        router.attach("/teletaxi/prenotazione/{id}", DatabaseAttachPrenotazioneIdResourceJson.class);
        router.attach("/teletaxi/operatoretelefonico/{id}", DatabaseAttachOperatoreTelefonicoIdResourceJson.class);

        router.attachDefault(DatabaseAttachDefaultResource.class);
        return router;
    }

    public static void main(String[] args) {
        //JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setCurrentDirectory(new File("."));
        //fileChooser.showOpenDialog(new JPanel());
        //File f =fileChooser.getSelectedFile();
        //System.setProperty("javax.net.ssl.trustStore", f.getAbsolutePath());
        Component component = null;
        try {
            component = new Component();
            //Server server = component.getServers().add(Protocol.HTTPS, 443);
            component.getServers().add(Protocol.HTTP, 80);
            //String keystorePwd = "t3l3t4x1";
            //String keyPwd = "t3l3t4x1";
            //Series parameters = server.getContext().getParameters();
            //parameters.add("sslContextFactory",
              //      "org.restlet.engine.ssl.DefaultSslContextFactory");
            //System.out.println(f.getAbsolutePath());
            //parameters.add("keyStorePath", f.getPath());
            //parameters.add("keyStorePassword", keystorePwd);
            //parameters.add("keyPassword", keyPwd);
            //parameters.add("keyStoreType", "JKS");

            component.getDefaultHost().attach(new TeleTaxiServerWeb());
            component.start();
        } catch (FileNotFoundException e) {
            System.err.println("Errore nell'inizializzazione del WebServer. " + e);
        } catch (Exception e) {
            System.err.println("Errore nell'inizializzazione del WebServer. " + e);
        }
    }
}
