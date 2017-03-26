package websource;

import control.GestorePrenotazione;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.*;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by dn on 22/03/17.
 */
public class DatabaseAttachDefaultResource extends ServerResource{

    @Get
    public void getHomePage(){
        GestorePrenotazione gp = GestorePrenotazione.getInstance();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<head><title>Page not found - " +
                "TeleTaxi</title></head>");
        stringBuilder.append("<body bgcolor=white>");
        stringBuilder.append("<table border=\"0\">");
        stringBuilder.append("<tr>");
        stringBuilder.append("<td>");
        stringBuilder.append("<h1>Pagina non trovata</h1>");
        stringBuilder.append("</td>");
        stringBuilder.append("</tr>");
        stringBuilder.append("</table>");
        stringBuilder.append("</body>");
        stringBuilder.append("</html>");
        getResponse().setEntity(new StringRepresentation( stringBuilder.toString(), MediaType.TEXT_HTML));
    }
}
