package es.caib.regweb3.webapp.utils;

/**
 * Created 15/07/14 14:24
 *
 * @author mgonzalez
 */
public class AnexoJson {

    private String id;
    private String nombre;
    private Boolean isPrimerAnexo = false;

    public AnexoJson() {
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

    public Boolean getPrimerAnexo() {
        return isPrimerAnexo;
    }

    public void setPrimerAnexo(Boolean primerAnexo) {
        isPrimerAnexo = primerAnexo;
    }
}
