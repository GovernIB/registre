package es.caib.regweb3.persistence.utils;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.RegistroEntrada;

import java.util.List;

/**
 * Bean para almacenar RegistroEntrada, de un mismo Organismo, 
 * pendientes de realizar un Oficio de Remmision.
 * Created by earrivi on 5/09/14.
 */
public class OficiosRemisionOrganismo {

    List<RegistroEntrada> oficiosRemision;
    List<OficinaTF> oficinasSIR;
    Boolean sir = false;

    public OficiosRemisionOrganismo() {
    }

    public List<RegistroEntrada> getOficiosRemision() {
        return oficiosRemision;
    }

    public void setOficiosRemision(List<RegistroEntrada> oficiosRemision) {
        this.oficiosRemision = oficiosRemision;
    }

    public List<OficinaTF> getOficinasSIR() {
        return oficinasSIR;
    }

    public void setOficinasSIR(List<OficinaTF> oficinasSIR) {
        this.oficinasSIR = oficinasSIR;
    }

    public Boolean getSir() {
        return sir;
    }

    public void setSir(Boolean sir) {
        this.sir = sir;
    }
}
