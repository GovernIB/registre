package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.utils.CamposNTI;

import java.util.List;

/**
 * Created 5/06/14 16:09
 * @author jpernia
 */
public class RegistrarForm {

    private Long idLibro;
    private Long idIdioma;
    private Long idTipoAsunto;

    private List<CamposNTI> camposNTIs;

    public RegistrarForm(Long idLibro, Long idIdioma, Long idTipoAsunto) {
        this.idLibro = idLibro;
        this.idIdioma = idIdioma;
        this.idTipoAsunto = idTipoAsunto;
    }

    public RegistrarForm() {

    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public Long getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(Long idIdioma) {
        this.idIdioma = idIdioma;
    }

    public Long getIdTipoAsunto() {
        return idTipoAsunto;
    }

    public void setIdTipoAsunto(Long idTipoAsunto) {
        this.idTipoAsunto = idTipoAsunto;
    }

    public List<CamposNTI> getCamposNTIs() {
        return camposNTIs;
    }

    public void setCamposNTIs(List<CamposNTI> camposNTIs) {
        this.camposNTIs = camposNTIs;
    }

}
