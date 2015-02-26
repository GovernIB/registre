package es.caib.regweb.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created 19/03/14 12:55
 *
 * @author earrivi
 */
@Embeddable
public class TraduccionTipoDocumental implements Traduccion {

    private String nombre;


    @Column(name = "NOMBRE", nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
