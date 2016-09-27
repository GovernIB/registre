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
    private String organismoExternoCodigo;
    private String organismoExternoDenominacion;
    private Long idLibro;
    private String oficinaSIRCodigo;

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

    public String getOrganismoExternoCodigo() {
        return organismoExternoCodigo;
    }

    public void setOrganismoExternoCodigo(String organismoExternoCodigo) {
        this.organismoExternoCodigo = organismoExternoCodigo;
    }

    public String getOficinaSIRCodigo() {
        return oficinaSIRCodigo;
    }

    public void setOficinaSIRCodigo(String oficinaSIRCodigo) {
        this.oficinaSIRCodigo = oficinaSIRCodigo;
    }

    public String getOrganismoExternoDenominacion() {
      return organismoExternoDenominacion;
    }

    public void setOrganismoExternoDenominacion(String organismoExternoDenominacion) {
      this.organismoExternoDenominacion = organismoExternoDenominacion;
    }

}
