package es.caib.regweb3.webapp.form;


import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;

/**
 * Created by Fundació Bit
 * @author earrivi
 */
public class ReenviarForm {

    private String codigoOficina;
    private String denominacionOficina;
    private String codigoOrganismoResponsable;
    private String denominacionOrganismoResponsable;



    public ReenviarForm() {
    }

    public String getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(String codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public String getDenominacionOficina() {
        return denominacionOficina;
    }

    public void setDenominacionOficina(String denominacionOficina) {
        this.denominacionOficina = denominacionOficina;
    }

    public String getCodigoOrganismoResponsable() {
        return codigoOrganismoResponsable;
    }

    public void setCodigoOrganismoResponsable(String codigoOrganismoResponsable) {
        this.codigoOrganismoResponsable = codigoOrganismoResponsable;
    }

    public String getDenominacionOrganismoResponsable() {
        return denominacionOrganismoResponsable;
    }

    public void setDenominacionOrganismoResponsable(String denominacionOrganismoResponsable) {
        this.denominacionOrganismoResponsable = denominacionOrganismoResponsable;
    }

    public Oficina oficinaReenvio(){
        Oficina oficina = new Oficina(null, getCodigoOficina(), getDenominacionOficina());
        oficina.setOrganismoResponsable(new Organismo(null, codigoOrganismoResponsable, denominacionOrganismoResponsable));

        return oficina;
    }
}
