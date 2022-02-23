package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created 19/02/14 12:01
 *
 * @author mgonzalez
 */

@Entity
@IdClass(es.caib.regweb3.model.RelacionSirOfiPK.class)
@Table(name = "RWE_RELSIROFI")
public class RelacionSirOfi implements Serializable {

    private Oficina oficina;
    private Organismo organismo;
    private CatEstadoEntidad estado;

    public RelacionSirOfi() {
    }

    public RelacionSirOfi(Long idOficina, String codOficina, String denOficina, Long idOrganismo, Long idOrgResponsable) {
        this.oficina = new Oficina(idOficina, codOficina, denOficina, idOrgResponsable);
        this.organismo = new Organismo(idOrganismo);
    }


    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "IDOFICINA", foreignKey = @ForeignKey(name = "RWE_RELSIROFI_OFICINA_FK"))
    public Oficina getOficina() {
        return oficina;
    }


    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }


    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "IDORGANISMO", foreignKey = @ForeignKey(name = "RWE_RELSIROFI_ORGANISMO_FK"))
    public Organismo getOrganismo() {
        return organismo;
    }


    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }


    @ManyToOne(optional = false)
    @JoinColumn(name = "ESTADO", foreignKey = @ForeignKey(name = "RWE_RELSIROFI_CATESTENTI_FK"))
    public CatEstadoEntidad getEstado() {
        return estado;
    }


    public void setEstado(CatEstadoEntidad estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelacionSirOfi that = (RelacionSirOfi) o;

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
