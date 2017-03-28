package es.caib.regweb3.webapp.form;


import es.caib.regweb3.model.Oficina;

/**
 * Created by Fundació Bit
 * @author earrivi
 */
public class ReenviarForm {

    private String codigoOficina;
    private String denominacionOficina;



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


    public Oficina oficinaReenvio(){
        return new Oficina(null, getCodigoOficina(), getDenominacionOficina());
    }
}
