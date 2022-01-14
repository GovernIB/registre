package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
@XmlRootElement
@Deprecated
public class RegistroEntradaResponseWs extends RegistroResponseWs  {

    private String destinoCodigo;
    private String destinoDenominacion;

    public String getDestinoCodigo() {
        return destinoCodigo;
    }

    public void setDestinoCodigo(String destinoCodigo) {
        this.destinoCodigo = destinoCodigo;
    }

    public String getDestinoDenominacion() {
        return destinoDenominacion;
    }

    public void setDestinoDenominacion(String destinoDenominacion) {
        this.destinoDenominacion = destinoDenominacion;
    }

    @Override
    public String toString() {
        return "RegistroEntradaResponseWs{" +
                "destinoCodigo='" + destinoCodigo + '\'' +
                ", destinoDenominacion='" + destinoDenominacion + '\'' +
                "} " + super.toString();
    }
}
