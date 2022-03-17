package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 19/03/14
 */
@Entity
@Table(name = "RWE_TIPODOCUMENTAL")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "tipoDocumental")
@XmlAccessorType(XmlAccessType.FIELD)
public class TipoDocumental extends Traducible {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String codigoNTI;
    @XmlTransient
    private Entidad entidad;

    public TipoDocumental() {
    }

    public TipoDocumental(String id) {
      this.id= Long.valueOf(id);
    }

    public TipoDocumental(String codigoNTI, Long idEntidad) {
        this.codigoNTI = codigoNTI;
        this.entidad = new Entidad(idEntidad);
    }

    /**
     *
     * @param td
     */
    public TipoDocumental(TipoDocumental td) {
      super();
      this.id = td.id;
      this.codigoNTI = td.codigoNTI;
      this.traducciones = new HashMap<String, Traduccion>(td.getTraducciones());
      //this.entidad = td.entidad == null? null : new Entidad(td.entidad);
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CODIGONTI")
    public String getCodigoNTI() {
      return codigoNTI;
    }

    public void setCodigoNTI(String codigoNTI) {
      this.codigoNTI = codigoNTI;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_TIPODOCUMENTAL_ENTIDAD_FK")
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }



    @CollectionOfElements(fetch=FetchType.LAZY,targetElement = TraduccionTipoDocumental.class)
    @Cascade(value=org.hibernate.annotations.CascadeType.ALL)
    @LazyCollection(value= LazyCollectionOption.FALSE)
    @JoinTable(name="RWE_TRA_TDOCUMENTAL",joinColumns={@JoinColumn(name="IDTDOCUMENTAL")})
    @org.hibernate.annotations.MapKey(columns={@Column(name="LANG",length=2)})
    @ForeignKey(name="RWE_TIPODOC_TRATIPODOC_FK", inverseName = "RWE_TRATIPODOC_TIPODOC_FK")
    @Override
    public Map<String,Traduccion> getTraducciones() {
        return traducciones;
    }
}
