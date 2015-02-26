package es.caib.regweb.model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Entity
@Table(name = "RWE_IDIOMA_REGISTRO")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "idiomaRegistro")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdiomaRegistro implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String nombre;
    @XmlTransient
    private String codigo;
    @XmlTransient
    private int orden;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NOMBRE", nullable = false, unique = true)
    public String getNombre() {
        return nombre;
    }

    @Column(name = "CODIGO", nullable = false, unique = true)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "orden", nullable=false, unique = true)
    public int getOrden() {
        return orden;
    }
    public void setOrden(int orden) {
        this.orden = orden;
    }
}
