package control;

import com.google.gson.Gson;
import model.*;
import resources.*;
import resources.distanceMatrix.DistanceMatrix;
import resources.distanceMatrix.Element;
import resources.distanceMatrix.Row;
import resources.exception.*;
import websource.DatabaseManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dn on 26/03/17.
 */
public class GestorePrenotazione {
    private Gson gs;
    private static GestorePrenotazione instance = null;
    private Connection connection;
    private Statement statement;
    private CopyOnWriteArrayList<Prenotazione> prenotazioni;


    public GestorePrenotazione() throws ConnectionSQLFailException {
        gs = new Gson();
        prenotazioni = new CopyOnWriteArrayList<Prenotazione>();
        DatabaseManager db = DatabaseManager.getInstance();
        connection = db.getConnection();
        try {
            if(connection!=null) statement = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Errore nella creazione della connessione in GestorePrenotazione");
            throw new ConnectionSQLFailException(Integer.toString(e.getErrorCode()));
        }catch (NullPointerException e){
            System.err.println("Nessuna connessione");
        }
    }

    //singleton
    public static synchronized GestorePrenotazione getInstance() throws ConnectionSQLFailException {
        if(instance==null) instance=new GestorePrenotazione();
        return instance;
    }

    public Prenotazione[] getAllPrenotazioni()
            throws GetPrenotazioniFailException, FindOperatoreFailException, FindTaxiFailException, FindClienteFailException, ConnectionSQLFailException {
        ArrayList<Prenotazione> prenotazionesTmp = new ArrayList<Prenotazione>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_PRENOTAZIONI);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String progressivoPrenotazione = rs.getString(BaseColumns.PROGRESSIVO_PRENOTAZIONE);
                String identificativoOperatore = rs.getString(BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO);
                OperatoreTelefonico operatoreTelefonico =(OperatoreTelefonico) GestoreStatistica.getInstance().findOperatoreById(identificativoOperatore).getValues().get(0);
                int identificativoTaxi = rs.getInt(BaseColumns.IDENTIFICATIVO_TAXI);
                Taxi taxi = (Taxi) GestoreStatistica.getInstance().findTaxiByCodice(identificativoTaxi).getValues().get(0);
                String identificativoCliente = rs.getString(BaseColumns.IDENTIFICATIVO_CLIENTE);
                Cliente cliente =(Cliente) GestoreStatistica.getInstance().findClienteByID(identificativoCliente).getValues().get(0);
                String posizioneCorrente = rs.getString(BaseColumns.POSIZIONE_CLIENTE);
                String destinazione = rs.getString(BaseColumns.DESTINAZIONE);
                String[] serviziSpeciali = gs.fromJson(rs.getString(BaseColumns.SERVIZI_SPECIALI), String[].class);
                java.util.Date dataPrenotazione = new java.util.Date(rs.getTimestamp(BaseColumns.DATA_PRENOTAZIONE).getTime());
                boolean assegnata = Boolean.getBoolean(rs.getString(BaseColumns.PRENOTAZIONE_ASSEGNATA));
                prenotazionesTmp.add(new Prenotazione(progressivoPrenotazione,cliente,operatoreTelefonico,taxi,destinazione,serviziSpeciali,posizioneCorrente,
                        0.0,dataPrenotazione,assegnata));
            }
            return (Prenotazione[]) prenotazionesTmp.toArray(new Prenotazione[prenotazionesTmp.size()]);
        }catch (SQLException e){
            throw new GetPrenotazioniFailException(Integer.toString(e.getErrorCode()));
        }catch(NullPointerException e){
            throw new GetPrenotazioniFailException(e.getMessage());
        }
    }

    public synchronized Prenotazione inserisciPrenotazione(final Prenotazione pr)
            throws InserisciPrenotazioneFailException, ConnectionSQLFailException, GetTaxiFailException, FindPrenotazioneFailException, UpdatePrenotazioneFailException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        prenotazioni.add(pr);
        final PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO " + BaseColumns.TAB_PRENOTAZIONI +
                    "(" + BaseColumns.PROGRESSIVO_PRENOTAZIONE + "," + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + "," + BaseColumns.IDENTIFICATIVO_TAXI + "," +
                    BaseColumns.IDENTIFICATIVO_CLIENTE + "," + BaseColumns.POSIZIONE_CLIENTE + "," + BaseColumns.DESTINAZIONE + "," + BaseColumns.SERVIZI_SPECIALI + ","
                    + BaseColumns.DATA_PRENOTAZIONE + "," + BaseColumns.PRENOTAZIONE_ASSEGNATA + ")" + " VALUES(?,?,?,?,?,?,?,?,?)");
            statement.setString(1, pr.getProgressivo());
            if (pr.getOperatoreTelefonico() != null) statement.setString(2, pr.getOperatoreTelefonico().getIdentificativo());
            statement.setInt(3, ((Taxi) (GestoreStatistica.getInstance().findTaxiBetterWaiting(pr).getValues().get(0))).getCodice());
            statement.setString(4, pr.getCliente().getCodiceCliente());
            statement.setString(5, pr.getPosizioneCliente());
            statement.setString(6, pr.getDestinazione());
            statement.setString(7, gs.toJson(pr.getServiziSpeciali()));
            statement.setTimestamp(8, new Timestamp(pr.getData().getTime()));
            statement.setString(9, Boolean.toString(pr.isAssegnata()));
            statement.executeUpdate();
            Thread t = new Thread() {
                public void run() {
                    try {
                        sleep(40000);
                        while (!prenotazioni.get(prenotazioni.indexOf(pr)).isAssegnata()) {
                            try {
                                pr.setTaxi((Taxi) (GestoreStatistica.getInstance().findTaxiBetterWaiting(pr).getValues().get(0)));
                                updatePrenotazione(pr);
                                sleep(40000);
                            } catch (ConnectionSQLFailException e) {e.printStackTrace();}
                              catch (GetTaxiFailException e) {e.printStackTrace();}
                              catch (FindPrenotazioneFailException e) {e.printStackTrace();}
                              catch (UpdatePrenotazioneFailException e) {e.printStackTrace();}
                        }
                        interrupt();
                    } catch (InterruptedException e) {e.printStackTrace();}
                }
            };
            t.start();
            return pr;
        } catch (Exception e) {throw new InserisciPrenotazioneFailException(e.getMessage());}
    }

    public synchronized void eliminaPrenotazione(Prenotazione pr) throws EliminaPrenotazioneFailException {
        try {
            prenotazioni.remove(pr);
            String sql = "DELETE FROM "+BaseColumns.TAB_PRENOTAZIONI+" WHERE "+BaseColumns.PROGRESSIVO_PRENOTAZIONE+" = '"+pr.getProgressivo()+"' ;";
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            throw new EliminaPrenotazioneFailException(Integer.toString(e.getErrorCode()));
        }
    }

    public synchronized  Prenotazione updatePrenotazione(Prenotazione p) throws UpdatePrenotazioneFailException {
        try {
            Gson gson = new Gson();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE " + BaseColumns.TAB_PRENOTAZIONI + " SET " + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " = ?,"
                            + BaseColumns.IDENTIFICATIVO_TAXI + " = ?," + BaseColumns.IDENTIFICATIVO_CLIENTE + " = ?,"
                            + BaseColumns.POSIZIONE_CLIENTE + " = ?,"+BaseColumns.DESTINAZIONE + " = ?,"
                            + BaseColumns.SERVIZI_SPECIALI + " = ?," +BaseColumns.PRENOTAZIONE_ASSEGNATA + " = ?,"
                            +" WHERE " + BaseColumns.PROGRESSIVO_PRENOTAZIONE + " = ?");
            ps.setString(1, p.getOperatoreTelefonico().getIdentificativo());
            ps.setInt(2, p.getTaxi().getCodice());
            ps.setString(3, p.getCliente().getCodiceCliente());
            ps.setString(4, p.getPosizioneCliente());
            ps.setString(5, p.getDestinazione());
            ps.setString(6, gson.toJson(p.getServiziSpeciali(), String[].class));
            ps.setString(7, Boolean.toString(p.isAssegnata()));
            ps.setString(8, p.getProgressivo());
            for(Prenotazione pr: prenotazioni) if(pr.getProgressivo().equalsIgnoreCase(p.getProgressivo())) prenotazioni.set(prenotazioni.indexOf(pr), p);
            return p;
        } catch (SQLException e) {
            throw new UpdatePrenotazioneFailException(Integer.toString(e.getErrorCode()));
        }
    }

    public double richiediTempiDiAttesa(String indirizzoOrigine, String indirizzoDestinazione) throws ConnectionSQLFailException, GetTaxiFailException, FindPrenotazioneFailException {
        try {
            Gson gs = new Gson();
            String origine = URLEncoder.encode(indirizzoOrigine, "UTF-8").replace("+", "%20");
            String destinazione = URLEncoder.encode(indirizzoDestinazione, "UTF-8").replace("+", "%20");
            URL google = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origine
                    + "&destinations=" + destinazione + "&mode=driving&language=fr-FR&key=AIzaSyAIgJBbZ8l8K_7LCOcAsoxBZkwiEB3j_dU");
            URLConnection yc = google.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            DistanceMatrix dc = gs.fromJson(sb.toString(), DistanceMatrix.class);
            br.close();
            String[] parts = null;
            for (Row r : dc.getRows())
                for (Element e : r.getElements()) parts = e.getDuration().getText().split(" ");
            return Double.parseDouble(parts[0]);
        }catch (IOException e){
            throw new FindPrenotazioneFailException(e.getMessage());
        }
    }



}
