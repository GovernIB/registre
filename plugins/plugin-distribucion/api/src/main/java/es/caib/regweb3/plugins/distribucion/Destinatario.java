package es.caib.regweb3.plugins.distribucion;

/**
 * Created by Fundació BIT.
 * Representa un Destinatario al que se distribuirá el asiento registral.
 *
 * @author earrivi
 *         Date: 27/10/2015
 */
public class Destinatario {

    private String id;
    private String name;

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
