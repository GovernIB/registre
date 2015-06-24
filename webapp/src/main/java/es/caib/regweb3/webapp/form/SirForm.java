package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.RegistroEntrada;

import java.util.List;

/**
 * Created by Fundació Bit
 * @author jpernia
 * @author anadal
 */
public class SirForm {

    private List<RegistroEntrada> registros;
    private Long idOrganismo;
    private String organismoExterno;
    private String organismoExternoDenominacion;
    private Long idLibro;
    private String oficinaSIR;

    public SirForm() {
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

    public String getOficinaSIR() {
        return oficinaSIR;
    }

    public void setOficinaSIR(String oficinaSIR) {
        this.oficinaSIR = oficinaSIR;
    }

    public String getOrganismoExternoDenominacion() {
      return organismoExternoDenominacion;
    }

    public void setOrganismoExternoDenominacion(String organismoExternoDenominacion) {
      this.organismoExternoDenominacion = organismoExternoDenominacion;
    }

}
