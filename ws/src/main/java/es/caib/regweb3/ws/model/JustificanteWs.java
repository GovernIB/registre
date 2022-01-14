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
public class JustificanteWs implements Serializable {

    @XmlInlineBinaryData
    private byte[] justificante;

    public JustificanteWs() {
    }

    public JustificanteWs(byte[] justificante) {
        this.justificante = justificante;
    }

    public byte[] getJustificante() {
        return justificante;
    }

    public void setJustificante(byte[] justificante) {
        this.justificante = justificante;
    }
}
