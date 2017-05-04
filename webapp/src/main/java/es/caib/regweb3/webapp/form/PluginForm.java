package es.caib.regweb3.webapp.form;


/**
 * Created by Fundació Bit
 * @author earrivi
 */
public class PluginForm {

    private Long tipo;
    private Integer pageNumber;


    public PluginForm() {
    }

    public PluginForm(Long tipo, Integer pageNumber) {
        this.tipo = tipo;
        this.pageNumber = pageNumber;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
