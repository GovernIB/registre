package es.caib.regweb3.webapp.form;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fundació Bit
 * @author earrivi
 */
@XmlRootElement(name = "reproForm")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantillaForm {

    @XmlElement
    private Long idRegistro;
    @XmlElement
    private Integer tipoRegistro;
    @XmlElement
    private String nombreRepro;



    public PlantillaForm() {
    }

    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Integer getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Integer tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getNombreRepro() {
        return nombreRepro;
    }

    public void setNombreRepro(String nombreRepro) {
        this.nombreRepro = nombreRepro;
    }
}
