package es.caib.regweb3.model.utils;

/**
 * Created on 18/09/14.
 * @author earrivi
 * Bean para representar un OficioPendiente de Llegada y el Libro donde se creará su entrada
 */
public class OficioPendienteLlegada {

    private Long idRegistro;
    private Long idLibro;
    private Long idOrganismoDestinatario;

    public OficioPendienteLlegada() {
    }

    public OficioPendienteLlegada(Long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public Long getIdOrganismoDestinatario() {
        return idOrganismoDestinatario;
    }

    public void setIdOrganismoDestinatario(Long idOrganismoDestinatario) {
        this.idOrganismoDestinatario = idOrganismoDestinatario;
    }
}
