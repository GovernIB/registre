package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
@XmlRootElement
public class InteresadoWs {

    private DatosInteresadoWs interesado;
    private DatosInteresadoWs representante;

    public DatosInteresadoWs getInteresado() {
        return interesado;
    }

    public void setInteresado(DatosInteresadoWs interesado) {
        this.interesado = interesado;
    }

    public DatosInteresadoWs getRepresentante() {
        return representante;
    }

    public void setRepresentante(DatosInteresadoWs representante) {
        this.representante = representante;
    }
}
