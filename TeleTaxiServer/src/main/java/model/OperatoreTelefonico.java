package model;

import java.util.Date;

/**
 * Created by dn on 21/03/17.
 */
public class OperatoreTelefonico extends Persona {

    private String identificativo, password;

    public OperatoreTelefonico(String identificativo, String nome, String cognome, Date dataDiNascita, String password) {
        super(nome, cognome, dataDiNascita);
        this.identificativo = identificativo;
        this.password = password;
    }

    public String getIdentificativo() {
        return identificativo;
    }

    public OperatoreTelefonico setIdentificativo(String identificativo) {
        this.identificativo = identificativo;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public OperatoreTelefonico setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "OperatoreTelefonico: " +
                "id:'" + identificativo + '\'';
    }
}
