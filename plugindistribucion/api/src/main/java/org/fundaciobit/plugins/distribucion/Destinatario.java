package org.fundaciobit.plugins.distribucion;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 27/10/2015
 */
public class Destinatario {

    String id;
    String name;

    public Destinatario() {
    }

    /**
     * @param id
     * @param name
     */
    public Destinatario(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Destinatario [name=" + name + ", id=" + id + "]";
    }
}
