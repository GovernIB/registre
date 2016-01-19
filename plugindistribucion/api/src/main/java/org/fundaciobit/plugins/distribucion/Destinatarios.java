package org.fundaciobit.plugins.distribucion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 27/10/2015
 */
@XmlRootElement(name = "destinatarios")
@XmlAccessorType(XmlAccessType.FIELD)
public class Destinatarios {

    @XmlAttribute
    public List<Destinatario> propuestos;
    @XmlAttribute
    public List<Destinatario> posibles;
    @XmlAttribute
    Boolean modificable;

    public Destinatarios() {
    }

    /**
     * @param propuestos
     * @param posibles
     * @param modificable
     */
    public Destinatarios(List<Destinatario> propuestos, List<Destinatario> posibles, Boolean modificable) {
        this.propuestos = propuestos;
        this.posibles = posibles;
        this.modificable = modificable;
    }

    public List<Destinatario> getPropuestos() {
        return propuestos;
    }

    public void setPropuestos(List<Destinatario> propuestos) {
        this.propuestos = propuestos;
    }

    public List<Destinatario> getPosibles() {
        return posibles;
    }

    public void setPosibles(List<Destinatario> posibles) {
        this.posibles = posibles;
    }

    public Boolean getModificable() {
        return modificable;
    }

    public void setModificable(Boolean modificable) {
        this.modificable = modificable;
    }
}
