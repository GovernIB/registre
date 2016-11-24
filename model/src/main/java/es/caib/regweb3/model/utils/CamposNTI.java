package es.caib.regweb3.model.utils;

/**
 * Created by mgonzalez on 15/11/2016.
 */
public class CamposNTI {

    private Long id;
    private String idValidezDocumento;
    private Long idOrigen;
    private String idTipoDocumental;

    public CamposNTI() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdValidezDocumento() {
        return idValidezDocumento;
    }

    public void setIdValidezDocumento(String idValidezDocumento) {
        this.idValidezDocumento = idValidezDocumento;
    }

    public Long getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(Long idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getIdTipoDocumental() {
        return idTipoDocumental;
    }

    public void setIdTipoDocumental(String idTipoDocumental) {
        this.idTipoDocumental = idTipoDocumental;
    }
}
