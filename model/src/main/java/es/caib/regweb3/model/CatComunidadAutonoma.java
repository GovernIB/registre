package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author anadal (index)
 * @version 1.0
 * @created 28-oct-2013 14:41:38
 */
@Entity
@Table(name = "RWE_CATCOMUNIDADAUTONOMA",
        indexes = @Index(name = "RWE_CATCOM_CATPAI_FK_I", columnList = "PAIS"))

@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class CatComunidadAutonoma implements Serializable {

    private Long id;
    private Long codigoComunidad;
    private String descripcionComunidad;
    private CatPais pais;
    private String c_comunidad_rpc;
    private Long c_codigo_dir2;

    public CatComunidadAutonoma() {

    }

    public CatComunidadAutonoma(Long id) {
        this.id = id;
    }


    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the codigoComunidad
     */
    @Column(name = "CODIGOCOMUNIDAD", nullable = false, length = 2)
    public Long getCodigoComunidad() {
        return codigoComunidad;
    }

    /**
     * @param codigoComunidad the codigoComunidad to set
     */
    public void setCodigoComunidad(Long codigoComunidad) {
        this.codigoComunidad = codigoComunidad;
    }

    /**
     * @return the descripcionComunidad
     */
    @Column(name = "DESCRIPCIONCOMUNIDAD", nullable = false, length = 50)
    public String getDescripcionComunidad() {
        return descripcionComunidad;
    }

    /**
     * @param descripcionComunidad the descripcionComunidad to set
     */
    public void setDescripcionComunidad(String descripcionComunidad) {
        this.descripcionComunidad = descripcionComunidad;
    }

    /**
     * @return the codigoPais
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAIS", foreignKey = @ForeignKey(name = "RWE_CATCOMUNAUT_CATPAIS_FK"))
    public CatPais getPais() {
        return pais;
    }

    /**
     * @param pais the codigoPais to set
     */
    public void setPais(CatPais pais) {
        this.pais = pais;
    }

    /**
     * @return the c_comunidad_rpc
     */
    @Column(name = "C_COMUNIDAD_RPC", length = 2)
    public String getC_comunidad_rpc() {
        return c_comunidad_rpc;
    }

    /**
     * @param c_comunidad_rpc the c_comunidad_rpc to set
     */
    public void setC_comunidad_rpc(String c_comunidad_rpc) {
        this.c_comunidad_rpc = c_comunidad_rpc;
    }

    /**
     * @return the c_codigo_dir2
     */
    @Column(name = "C_CODIGO_DIR2", length = 2)
    public Long getC_codigo_dir2() {
        return c_codigo_dir2;
    }

    /**
     * @param c_codigo_dir2 the c_codigo_dir2 to set
     */
    public void setC_codigo_dir2(Long c_codigo_dir2) {
        this.c_codigo_dir2 = c_codigo_dir2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.codigoComunidad != null ? this.codigoComunidad.hashCode() : 0);
        hash = 37 * hash + (this.descripcionComunidad != null ? this.descripcionComunidad.hashCode() : 0);
        hash = 37 * hash + (this.pais != null ? this.pais.hashCode() : 0);
        hash = 37 * hash + (this.c_comunidad_rpc != null ? this.c_comunidad_rpc.hashCode() : 0);
        hash = 37 * hash + (this.c_codigo_dir2 != null ? this.c_codigo_dir2.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatComunidadAutonoma other = (CatComunidadAutonoma) obj;
        if (this.codigoComunidad != other.codigoComunidad && (this.codigoComunidad == null || !this.codigoComunidad.equals(other.codigoComunidad))) {
            return false;
        }
        if ((this.descripcionComunidad == null) ? (other.descripcionComunidad != null) : !this.descripcionComunidad.equals(other.descripcionComunidad)) {
            return false;
        }
        if (this.pais != other.pais && (this.pais == null || !this.pais.equals(other.pais))) {
            return false;
        }
        if ((this.c_comunidad_rpc == null) ? (other.c_comunidad_rpc != null) : !this.c_comunidad_rpc.equals(other.c_comunidad_rpc)) {
            return false;
        }
        if (this.c_codigo_dir2 != other.c_codigo_dir2 && (this.c_codigo_dir2 == null || !this.c_codigo_dir2.equals(other.c_codigo_dir2))) {
            return false;
        }
        return true;
    }


}