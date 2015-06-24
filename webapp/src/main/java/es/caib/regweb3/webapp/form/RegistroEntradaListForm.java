package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.RegistroEntrada;

import java.util.List;

/**
 * Created by Fundació Bit
 * @author earrivi
 */
public class RegistroEntradaListForm {

    private List<RegistroEntrada> registros;
    private Long idOrganismo;
    private String organismoExterno;
    private Long idLibro;

    public RegistroEntradaListForm() {
    }

    public List<RegistroEntrada> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroEntrada> registros) {
        this.registros = registros;
    }

    public Long getIdOrganismo() {
        return idOrganismo;
    }

    public void setIdOrganismo(Long idOrganismo) {
        this.idOrganismo = idOrganismo;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public String getOrganismoExterno() {
        return organismoExterno;
    }

    public void setOrganismoExterno(String organismoExterno) {
        this.organismoExterno = organismoExterno;
    }
}
