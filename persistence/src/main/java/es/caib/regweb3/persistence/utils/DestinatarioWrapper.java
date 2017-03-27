package es.caib.regweb3.persistence.utils;


import es.caib.regweb3.plugins.distribucion.Destinatario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

/**
 * Created by mgonzalez on 14/01/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DestinatarioWrapper {

    @XmlAttribute
    private List<Destinatario> destinatarios;
    @XmlAttribute
    private String observaciones;

    /**
     * @return the destinatarios
     */
    public List<Destinatario> getDestinatarios() {
        return destinatarios;
    }

    /**
     * @param destinatarios the destinatarios to set
     */
    public void setDestinatarios(List<Destinatario> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
