package es.caib.regweb3.persistence.utils;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.Organismo;

import java.util.List;

/**
 * Bean para almacenar RegistroEntrada, de un mismo Organismo, 
 * pendientes de realizar un Oficio de Remmision.
 * Created by earrivi on 5/09/14.
 */
public class OficiosRemisionExternoOrganismo {

    Organismo organismo;
    Boolean vigente = false;
    Paginacion paginacion;
    List<OficinaTF> oficinasSIR;
    Boolean sir = false;

    public OficiosRemisionExternoOrganismo() {
    }

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }
    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public Paginacion getPaginacion() {
        return paginacion;
    }

    public void setPaginacion(Paginacion paginacion) {
        this.paginacion = paginacion;
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
