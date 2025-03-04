package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.utils.RegistroBasico;

import java.util.List;

/**
 * Created by Fundació Bit
 * @author earrivi
 */
public class OficioRemisionForm {

    private Long tipoOficioRemision;
    private List<RegistroBasico> registros;
    private Long idOrganismo;
    private String organismoExternoCodigo;
    private String organismoExternoDenominacion;
    private String oficinaSIRCodigo;
    private String oficinaSIRDenominacion;
    private String oficinaUoResponsable;


    public OficioRemisionForm() {
    }

    public OficioRemisionForm(Long tipoOficioRemision) {
        this.tipoOficioRemision = tipoOficioRemision;
    }

    public Long getTipoOficioRemision() {
        return tipoOficioRemision;
    }

    public void setTipoOficioRemision(Long tipoOficioRemision) {
        this.tipoOficioRemision = tipoOficioRemision;
    }

    public List<RegistroBasico> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroBasico> registros) {
        this.registros = registros;
    }

    public Long getIdOrganismo() {
        return idOrganismo;
    }

    public void setIdOrganismo(Long idOrganismo) {
        this.idOrganismo = idOrganismo;
    }

    public String getOrganismoExternoCodigo() {
        return organismoExternoCodigo;
    }

    public void setOrganismoExternoCodigo(String organismoExternoCodigo) {
        this.organismoExternoCodigo = organismoExternoCodigo;
    }

    public String getOrganismoExternoDenominacion() {
        return organismoExternoDenominacion;
    }

    public void setOrganismoExternoDenominacion(String organismoExternoDenominacion) {
        this.organismoExternoDenominacion = organismoExternoDenominacion;
    }

    public String getOficinaSIRCodigo() {
        return oficinaSIRCodigo;
    }

    public void setOficinaSIRCodigo(String oficinaSIRCodigo) {
        this.oficinaSIRCodigo = oficinaSIRCodigo;
    }

    public String getOficinaSIRDenominacion() {
        return oficinaSIRDenominacion;
    }

    public void setOficinaSIRDenominacion(String oficinaSIRDenominacion) {
        this.oficinaSIRDenominacion = oficinaSIRDenominacion;
    }

    public String getOficinaUoResponsable() {
        return oficinaUoResponsable;
    }

    public void setOficinaUoResponsable(String oficinaUoResponsable) {
        this.oficinaUoResponsable = oficinaUoResponsable;
    }
}
