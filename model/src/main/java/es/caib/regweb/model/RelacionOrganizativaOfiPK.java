package es.caib.regweb.model;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 *
 * @author mgonzalez
 */
public class RelacionOrganizativaOfiPK implements Serializable {


    private Oficina oficina;
    private Organismo organismo;

    public RelacionOrganizativaOfiPK() {}


    @Id
    @ManyToOne(cascade= CascadeType.PERSIST)
    @JoinColumn(name="IDOFICINA")
    @ForeignKey(name="RWE_RELORGOFI_OFICINA_FK")
    public Oficina getOficina() {
        return oficina;
    }


    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }


    @Id
    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn (name="IDORGANISMO")
    @ForeignKey(name="RWE_RELORGOFI_ORGANISMO_FK")
    public Organismo getOrganismo() {
        return organismo;
    }


    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelacionOrganizativaOfiPK that = (RelacionOrganizativaOfiPK) o;

        if (oficina != null ? !oficina.equals(that.oficina) : that.oficina != null) return false;
        if (organismo != null ? !organismo.equals(that.organismo) : that.organismo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = oficina != null ? oficina.hashCode() : 0;
        result = 31 * result + (organismo != null ? organismo.hashCode() : 0);
        return result;
    }
}
