package model;

import java.util.List;

/**
 * Created by dn on 29/03/17.
 */
public class Statistica {
    private Class key;
    private List<?> values;

    public Statistica(Class key, List<?> values) {
        this.key = key;
        this.values = values;
    }

    public Class getKey() {return key;}

    public Statistica setKey(Class key) {this.key = key; return this;}

    public List<?> getValues() {return values;}

    public Statistica setValues(List<Object> values) {this.values = values; return this;}
}
