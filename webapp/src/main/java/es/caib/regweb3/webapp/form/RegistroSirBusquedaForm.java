package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.RegistroSir;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
public class RegistroSirBusquedaForm {

    private RegistroSir registroSir;
    private Integer pageNumber;
    private Integer anyo;
    private String estado;


    public RegistroSirBusquedaForm() {}

    public RegistroSirBusquedaForm(RegistroSir registroSir, Integer pageNumber) {
        this.registroSir = registroSir;
        this.pageNumber = pageNumber;
    }

    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
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
