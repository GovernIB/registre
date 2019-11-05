package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 31/10/19
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AsientoRegistralSesionWs {

    private Long estado;
    private AsientoRegistralWs asientoRegistralWs;

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public AsientoRegistralWs getAsientoRegistralWs() {
        return asientoRegistralWs;
    }

    public void setAsientoRegistralWs(AsientoRegistralWs asientoRegistralWs) {
        this.asientoRegistralWs = asientoRegistralWs;
    }
}
