package es.caib.regweb.ws.model;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (herència)
 */
@XmlRootElement
public class RegistroEntradaWs extends RegistroWs  {

    private String destino;

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

}
