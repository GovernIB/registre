package es.caib.regweb3.webapp.form;

/**
 * Created 5/06/14 16:09
 * @author jpernia
 */
public class RegistrarForm {

    private Long idLibro;
    private Long idIdioma;
    private Long idTipoAsunto;

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
}
