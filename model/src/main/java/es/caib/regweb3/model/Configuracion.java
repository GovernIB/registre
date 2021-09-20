package es.caib.regweb3.model;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 06/07/2015
 */

@Entity
@Table(name = "RWE_CONFIGURACION")
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Configuracion implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlTransient
    private String colorMenu = "#ff9523";
    @XmlTransient
    private String textoPie;
    @XmlTransient
    private Archivo logoMenu;
    @XmlTransient
    private Archivo logoPie;

    public Configuracion() {

    }

    public Configuracion(Configuracion c) {
        super();
        this.id = c.id;
        this.colorMenu = c.colorMenu;
        this.textoPie = c.textoPie;
        this.logoMenu = c.logoMenu;
        this.logoPie = c.logoPie;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "COLORMENU")
    public String getColorMenu() {
        return colorMenu;
    }

    public void setColorMenu(String colorMenu) {
        this.colorMenu = colorMenu;
    }

    @Column(name = "TEXTOPIE", length = 4000)
    public String getTextoPie() {
        return textoPie;
    }

    public void setTextoPie(String textoPie) {
        this.textoPie = textoPie;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOGOMENU", foreignKey =@ForeignKey(name = "RWE_CONFIGURACION_LOGOMENU_FK"))
    public Archivo getLogoMenu() {
        return logoMenu;
    }

    public void setLogoMenu(Archivo logoMenu) {
        this.logoMenu = logoMenu;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOGOPIE", foreignKey =@ForeignKey(name = "RWE_CONFIGURACION_LOGOPIE_FK"))
    public Archivo getLogoPie() {
        return logoPie;
    }

    public void setLogoPie(Archivo logoPie) {
        this.logoPie = logoPie;
    }

}
