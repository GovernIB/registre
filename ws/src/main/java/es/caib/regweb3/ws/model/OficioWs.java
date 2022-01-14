package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OficioWs implements Serializable {

    @XmlInlineBinaryData
    private byte[] oficio;

    public OficioWs() {
    }

    public OficioWs(byte[] oficio) {
        this.oficio = oficio;
    }

    public byte[] getOficio() {
        return oficio;
    }

    public void setOficio(byte[] oficio) {
        this.oficio = oficio;
    }
}
