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
    private List<CamposNTI> camposNTIs;
    private Long idOrganismoDestino;
    private String codigoSia;
    private String emails;
    private String motivo;
    private String extracto;


    public RegistrarForm() {

    }

    public RegistrarForm(String extracto) {
        this.extracto = extracto;
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

    public List<CamposNTI> getCamposNTIs() {
        return camposNTIs;
    }

    public void setCamposNTIs(List<CamposNTI> camposNTIs) {
        this.camposNTIs = camposNTIs;
    }

    public Long getIdOrganismoDestino() {
        return idOrganismoDestino;
    }

    public void setIdOrganismoDestino(Long idOrganismoDestino) {
        this.idOrganismoDestino = idOrganismoDestino;
    }

    public String getCodigoSia() {
        return codigoSia;
    }

    public void setCodigoSia(String codigoSia) {
        this.codigoSia = codigoSia;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }
}
