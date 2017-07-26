package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *
 */

@XmlRootElement
public class JustificanteWs implements Serializable {

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
