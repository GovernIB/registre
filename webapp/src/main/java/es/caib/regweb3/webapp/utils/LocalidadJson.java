package es.caib.regweb3.webapp.utils;

/**
 * Created by mgonzalez on 16/03/2016.
 */
public class LocalidadJson {

    private String id;
    private String nombre;
    private String codigoEntidadGeografica;

    public LocalidadJson(String id, String nombre, String codigoEntidadGeografica) {
        this.id = id;
        this.nombre = nombre;
        this.codigoEntidadGeografica = codigoEntidadGeografica;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoEntidadGeografica() {
        return codigoEntidadGeografica;
    }

    public void setCodigoEntidadGeografica(String codigoEntidadGeografica) {
        this.codigoEntidadGeografica = codigoEntidadGeografica;
    }
}
