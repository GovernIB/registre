package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.AsientoRegistralSir;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
public class AsientoRegistralSirBusquedaForm {

    private AsientoRegistralSir asientoRegistralSir;
    private Integer pageNumber;
    private Integer anyo;
    private String estado;


    public AsientoRegistralSirBusquedaForm() {}

    public AsientoRegistralSirBusquedaForm(AsientoRegistralSir asientoRegistralSir, Integer pageNumber) {
        this.asientoRegistralSir = asientoRegistralSir;
        this.pageNumber = pageNumber;
    }

    public AsientoRegistralSir getAsientoRegistralSir() {
        return asientoRegistralSir;
    }

    public void setAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) {
        this.asientoRegistralSir = asientoRegistralSir;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getAnyo() {
        return anyo;
    }

    public void setAnyo(Integer anyo) {
        this.anyo = anyo;
    }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }
}
