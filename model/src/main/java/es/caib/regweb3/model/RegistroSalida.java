package es.caib.regweb3.model;

import es.caib.regweb3.utils.RegwebConstantes;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 27/03/14
 */
@Entity
@Table(name = "RWE_REGISTRO_SALIDA")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "registroSalida")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistroSalida implements IRegistro {

    @XmlAttribute
    private Long id;
    @XmlElement
    private UsuarioEntidad usuario;
    @XmlElement
    private Oficina oficina;
    @XmlElement
    private Organismo origen;
    @XmlElement
    private String origenExternoCodigo;
    @XmlElement
    private String origenExternoDenominacion;
    @XmlElement
    private Date fecha;
    @XmlElement
    private Libro libro;
    @XmlElement
    private Integer numeroRegistro;
    @XmlElement
    private String numeroRegistroFormateado;
    @XmlElement
    private Long estado;
    @XmlElement
    private RegistroDetalle registroDetalle;



    /**
     * 
     */
    public RegistroSalida() {
      super();
    }

    public RegistroSalida(Long id) {
        this.id = id;
    }

    /**
     * @param rs
     */
    public RegistroSalida(RegistroSalida rs) {
      super();
      this.id = rs.id;
      this.usuario = rs.usuario;
      this.oficina = rs.oficina;
      this.origen = rs.origen;
      this.origenExternoCodigo = rs.origenExternoCodigo;
      this.origenExternoDenominacion = rs.origenExternoDenominacion;
      this.fecha = rs.fecha;
      this.libro = rs.libro;
      this.numeroRegistro = rs.numeroRegistro;
      this.numeroRegistroFormateado = rs.numeroRegistroFormateado;
      this.estado = rs.estado;
      this.registroDetalle =  rs.registroDetalle == null? null : new RegistroDetalle(rs.registroDetalle);
    }

    public RegistroSalida(Long id, Integer numeroRegistro, Date fecha, Long idLibro, String nombreLibro, String denominacionOficina, String denominacionOrganismo) {
        this.id = id;
        this.numeroRegistro = numeroRegistro;
        this.fecha = fecha;
        this.libro = new Libro(idLibro, nombreLibro, null, null, denominacionOrganismo);
        this.oficina = new Oficina(null, null, denominacionOficina);
    }

    /**
     * Constructor para Informe LibroRegistro
     */
    public RegistroSalida(Long idRegistro, Long idLibro, String nombreLibro, Long idOficina, String denominacionOficina, Date fecha,
                          Integer numeroRegistro, String extracto, Long idTipoAsunto, Long idOficinaOrigen, String denominacionOficinaOrigen,
                          String numeroRegistroOrigen, Date fechaOrigen, String origenExternoDenominacion, Long idOrigen,
                          String denominacionOrigen, Long tipoDocumentacionFisica, Long idioma, String observaciones, Long estado,
                          String expediente, Long idCodigoAsunto, String referenciaExterna, Long transporte, String numeroTransporte,
                          Long idRegistroDetalle, String origenExternoCodigo, List<Interesado> interesados) {

        this.id = idRegistro;
        this.numeroRegistro = numeroRegistro;
        this.fecha = fecha;
        this.estado = estado;
        this.origenExternoCodigo = origenExternoCodigo;
        this.origenExternoDenominacion = origenExternoDenominacion;
        this.origen = new Organismo(idOrigen, denominacionOrigen);
        this.registroDetalle = new RegistroDetalle(idRegistroDetalle, extracto, idTipoAsunto, idOficinaOrigen, denominacionOficinaOrigen,
                numeroRegistroOrigen, fechaOrigen, tipoDocumentacionFisica, idioma, observaciones, expediente, idCodigoAsunto,
                referenciaExterna, transporte, numeroTransporte, interesados);
        this.libro = new Libro(idLibro, nombreLibro, null, null, null);
        this.oficina = new Oficina(idOficina, null, denominacionOficina);
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

    @ManyToOne(optional = false)
    @JoinColumn(name="USUARIO")
    @ForeignKey(name="RWE_REGSAL_USUSAL_FK")
    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = usuario;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="OFICINA")
    @ForeignKey(name="RWE_REGSAL_OFICINA_FK")
    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    @ManyToOne()
    @JoinColumn(name="ORIGEN")
    @ForeignKey(name="RWE_REGSAL_ORIGEN_FK")
    public Organismo getOrigen() {
        return origen;
    }

    public void setOrigen(Organismo origen) {
        this.origen = origen;
    }

    @Column(name = "DESTEXTCOD", length = 9)
    public String getOrigenExternoCodigo() {
        return origenExternoCodigo;
    }

    public void setOrigenExternoCodigo(String origenExternoCodigo) {
        this.origenExternoCodigo = origenExternoCodigo;
    }

    @Column(name = "DESTEXTDEN", length = 300)
    public String getOrigenExternoDenominacion() {
        return origenExternoDenominacion;
    }

    public void setOrigenExternoDenominacion(String origenExternoDenominacion) {
        this.origenExternoDenominacion = origenExternoDenominacion;
    }

    @Column(name = "FECHA", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="LIBRO")
    @ForeignKey(name="RWE_REGSAL_LIBRO_FK")
    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Column(name = "NUMREGISTRO", nullable = false)
    public Integer getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(Integer numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    @Column(name = "NUMREGFORMAT", nullable = false)
    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

    
    @Column(name="ESTADO", nullable=false)    
    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "REGISTRO_DETALLE")
    @ForeignKey(name = "RWE_REGSAL_REGDET_FK")
    public RegistroDetalle getRegistroDetalle() {
        return registroDetalle;
    }

    public void setRegistroDetalle(RegistroDetalle registroDetalle) {
        this.registroDetalle = registroDetalle;
    }


    @Transient
    public String interesadoDestinoCodigo() throws Exception{

        List<Interesado> interesados = this.getRegistroDetalle().getInteresados();

        for (Interesado interesado : interesados) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){

                return interesado.getCodigoDir3();
            }
        }

        return null;
    }

    @Transient
    public String getInteresadoDestinoDenominacion() throws Exception{

        List<Interesado> interesados = this.getRegistroDetalle().getInteresados();

        for (Interesado interesado : interesados) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){

                return interesado.getRazonSocial();
            }
        }

        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistroSalida that = (RegistroSalida) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public String toString() {
        if(id != null){
            return id.toString();
        }else{
            return null;
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
