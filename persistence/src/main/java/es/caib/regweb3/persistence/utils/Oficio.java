package es.caib.regweb3.persistence.utils;

/**
 * Created by earrivi on 21/03/2017.
 */
public class Oficio {

    Boolean isOficioRemision;
    Boolean isInterno;
    Boolean isExterno;
    Boolean isSir;

    public Oficio() {
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
