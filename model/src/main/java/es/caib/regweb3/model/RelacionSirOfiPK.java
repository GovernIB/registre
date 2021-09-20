package es.caib.regweb3.model;

import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created 19/02/14 12:06
 *
 * @author mgonzalez
 */
public class RelacionSirOfiPK implements Serializable {

    private Oficina oficina;
    private Organismo organismo;

    public RelacionSirOfiPK() {}


    @Id
    @ManyToOne()
    @JoinColumn(name="IDOFICINA", foreignKey = @ForeignKey(name="RWE_RELSIROFI_OFICINA_FK"))
    public Oficina getOficina() {
        return oficina;
    }


    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }


    @Id
    @ManyToOne()
    @JoinColumn (name="IDORGANISMO", foreignKey = @ForeignKey(name="RWE_RELSIROFI_ORGANISMO_FK"))
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

        RelacionSirOfiPK that = (RelacionSirOfiPK) o;

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
