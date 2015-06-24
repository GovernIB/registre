package es.caib.regweb3.webapp.utils;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 21/05/14
 */
public class PersonaJson {

    String id;
    String nombre;
    private Boolean isRepresentante = false;
    private PersonaJson representado;

    public PersonaJson() {
    }

    public PersonaJson(String id) {
        this.id = id;
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

    public Boolean getIsRepresentante() {
        return isRepresentante;
    }

    public void setIsRepresentante(Boolean isRepresentante) {
        this.isRepresentante = isRepresentante;
    }

    public PersonaJson getRepresentado() {
        return representado;
    }

    public void setRepresentado(PersonaJson representado) {
        this.representado = representado;
    }
}
