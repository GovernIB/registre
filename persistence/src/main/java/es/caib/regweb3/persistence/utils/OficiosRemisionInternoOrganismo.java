package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.Organismo;

/**
 * Bean para almacenar RegistroEntrada, de un mismo Organismo,
 * pendientes de realizar un Oficio de Remmision Interno.
 * Created by earrivi on 5/09/14.
 */
public class OficiosRemisionInternoOrganismo {

    Organismo organismo;
    Paginacion paginacion;
    Boolean oficinas = false;
    Boolean vigente = false;

    public OficiosRemisionInternoOrganismo() {
    }

    public Paginacion getPaginacion() {
        return paginacion;
    }

    public void setPaginacion(Paginacion paginacion) {
        this.paginacion = paginacion;
    }

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    public Boolean getOficinas() {
        return oficinas;
    }

    public void setOficinas(Boolean oficinas) {
        this.oficinas = oficinas;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }
}
