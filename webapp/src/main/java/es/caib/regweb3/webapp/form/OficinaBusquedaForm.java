package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Oficina;

import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 7/05/14
 */
public class OficinaBusquedaForm implements Serializable {

    private Oficina oficina;
    private Integer pageNumber;

    public OficinaBusquedaForm() {
    }

    public OficinaBusquedaForm(Oficina oficina, Integer pageNumber) {
        this.oficina = oficina;
        this.pageNumber = pageNumber;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
