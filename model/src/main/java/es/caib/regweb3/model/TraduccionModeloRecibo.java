package es.caib.regweb3.model;

import javax.persistence.*;

/**
 * Created 8/04/14 13:39
 *
 * @author mgonzalez
 */
@Embeddable
public class TraduccionModeloRecibo implements Traduccion {

    private String nombre;

    private Archivo modelo;

    @Column(name = "NOMBRE", nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ARCHIVO", foreignKey = @ForeignKey(name = "RWE_MODELRECIBO_ARCHIVO_FK"))
    public Archivo getModelo() {
        return modelo;
    }

    public void setModelo(Archivo modelo) {
        this.modelo = modelo;
    }

}
