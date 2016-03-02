package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RegistroEntrada;

import java.util.List;

/**
 * Bean para almacenar RegistroEntrada, de un mismo Organismo,
 * pendientes de realizar un Oficio de Remmision Interno.
 * Created by earrivi on 5/09/14.
 */
public class OficiosRemisionInternoOrganismo {

    Organismo organismo;
    List<RegistroEntrada> oficiosRemision;
    Boolean oficinas = false;
    Boolean vigente = false;

    public OficiosRemisionInternoOrganismo() {
    }

    public List<RegistroEntrada> getOficiosRemision() {
        return oficiosRemision;
    }

    public void setOficiosRemision(List<RegistroEntrada> oficiosRemision) {
        this.oficiosRemision = oficiosRemision;
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
