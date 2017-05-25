package es.caib.regweb3.persistence.utils;

/**
 * Created by earrivi on 21/03/2017.
 */
public class Oficio {

    private Boolean isOficioRemision = false;
    private Boolean isInterno = false;
    private Boolean isExterno = false;
    private Boolean isSir = false;

    public Oficio() {
    }

    public Oficio(Boolean isOficioRemision, Boolean isInterno, Boolean isExterno, Boolean isSir) {
        this.isOficioRemision = isOficioRemision;
        this.isInterno = isInterno;
        this.isExterno = isExterno;
        this.isSir = isSir;
    }

    public Boolean getOficioRemision() {
        return isOficioRemision;
    }

    public void setOficioRemision(Boolean oficioRemision) {
        isOficioRemision = oficioRemision;
    }

    public Boolean getInterno() {
        return isInterno;
    }

    public void setInterno(Boolean interno) {
        isInterno = interno;
    }

    public Boolean getExterno() {
        return isExterno;
    }

    public void setExterno(Boolean externo) {
        isExterno = externo;
    }

    public Boolean getSir() {
        return isSir;
    }

    public void setSir(Boolean sir) {
        isSir = sir;
    }
}
