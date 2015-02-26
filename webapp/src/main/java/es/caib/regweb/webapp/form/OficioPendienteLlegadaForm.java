package es.caib.regweb.webapp.form;

import es.caib.regweb.model.utils.OficioPendienteLlegada;

import java.util.List;

/**
 * Created on 18/09/14.
 * @author earrivi
 */
public class OficioPendienteLlegadaForm {

    private List<OficioPendienteLlegada> oficios;

    public OficioPendienteLlegadaForm() {
    }

    public OficioPendienteLlegadaForm(List<OficioPendienteLlegada> oficios) {
        this.oficios = oficios;
    }

    public List<OficioPendienteLlegada> getOficios() {
        return oficios;
    }

    public void setOficios(List<OficioPendienteLlegada> oficios) {
        this.oficios = oficios;
    }
}
