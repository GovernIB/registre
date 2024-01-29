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
    private Long entidad;
    private Integer pageNumber;
    private Boolean sir;
    private Boolean exportarOficinas = false;

    public OficinaBusquedaForm() {
    }

    public OficinaBusquedaForm(Oficina oficina, Long entidad, Integer pageNumber) {
        this.oficina = oficina;
        this.entidad = entidad;
        this.pageNumber = pageNumber;
        this.sir = null;
        oficina.setOamr(null);
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Long getEntidad() {
        return entidad;
    }

    public void setEntidad(Long entidad) {
        this.entidad = entidad;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Boolean getSir() {
        return sir;
    }

    public void setSir(Boolean sir) {
        this.sir = sir;
    }

    public Boolean getExportarOficinas() {
        return exportarOficinas;
    }

    public void setExportarOficinas(Boolean exportarOficinas) {
        this.exportarOficinas = exportarOficinas;
    }
}
