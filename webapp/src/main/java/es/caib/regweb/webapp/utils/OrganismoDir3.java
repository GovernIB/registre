package es.caib.regweb.webapp.utils;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 14/05/14
 */
public class OrganismoDir3 {

    private String codigoDir3;
    private String denominacion;


    public OrganismoDir3() {
    }

    public OrganismoDir3(String codigoDir3, String denominacion) {
        this.codigoDir3 = codigoDir3;
        this.denominacion = denominacion;
    }

    public OrganismoDir3(String codigoDir3) {
        this.codigoDir3 = codigoDir3;
    }

    public String getCodigoDir3() {
        return codigoDir3;
    }

    public void setCodigoDir3(String codigoDir3) {
        this.codigoDir3 = codigoDir3;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganismoDir3 that = (OrganismoDir3) o;

        if (codigoDir3 != null ? !codigoDir3.equals(that.codigoDir3) : that.codigoDir3 != null) return false;

        return true;
    }


}
