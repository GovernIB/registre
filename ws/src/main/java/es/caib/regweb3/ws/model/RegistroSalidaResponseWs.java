package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
@XmlRootElement
@Deprecated
public class RegistroSalidaResponseWs extends RegistroResponseWs {

    private String origenCodigo;
    private String origenDenominacion;

    public String getOrigenCodigo() {
        return origenCodigo;
    }

    public void setOrigenCodigo(String origenCodigo) {
        this.origenCodigo = origenCodigo;
    }

    public String getOrigenDenominacion() {
        return origenDenominacion;
    }

    public void setOrigenDenominacion(String origenDenominacion) {
        this.origenDenominacion = origenDenominacion;
    }

    @Override
    public String toString() {
        return "RegistroSalidaResponseWs{" +
                "origenCodigo='" + origenCodigo + '\'' +
                ", origenDenominacion='" + origenDenominacion + '\'' +
                "} " + super.toString();
    }
}
