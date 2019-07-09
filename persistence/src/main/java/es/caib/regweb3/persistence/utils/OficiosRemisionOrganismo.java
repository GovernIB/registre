package es.caib.regweb3.persistence.utils;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.Organismo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Bean para almacenar Registros, de un mismo Organismo,
 * pendientes de realizar un Oficio de Remmision.
 * Created by earrivi on 5/09/14.
 */
public class OficiosRemisionOrganismo {

    private Organismo organismo;
    private Boolean externo = false;
    private Boolean oficinas = false;
    private Boolean vigente = false;
    private Paginacion paginacion;
    private Set<Organismo> sustitutos = new HashSet<Organismo>();
    private Boolean sir = false;
    private List<OficinaTF> oficinasSIR;

    public OficiosRemisionOrganismo() {
    }

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    public Boolean getExterno() {
        return externo;
    }

    public void setExterno(Boolean externo) {
        this.externo = externo;
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

    public Boolean getOficinas() {
        return oficinas;
    }

    public void setOficinas(Boolean oficinas) {
        this.oficinas = oficinas;
    }

    public Set<Organismo> getSustitutos() {
        return sustitutos;
    }

    public void setSustitutos(Set<Organismo> sustitutos) {
        this.sustitutos = sustitutos;
    }
}
