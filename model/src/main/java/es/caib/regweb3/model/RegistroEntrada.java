package es.caib.regweb3.model;

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
@Table(name = "RWE_REGISTRO_ENTRADA")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "registroEntrada")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistroEntrada implements IRegistro {

    @XmlAttribute
    private Long id;
    @XmlTransient
    private Entidad entidad;
    private Long evento;
    @XmlElement
    private UsuarioEntidad usuario;
    @XmlElement
    private Oficina oficina;
    @XmlElement
    private Organismo destino;
    @XmlElement
    private String destinoExternoCodigo;
    @XmlElement
    private String destinoExternoDenominacion;
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
    public RegistroEntrada() {
      this.registroDetalle = new RegistroDetalle();
    }

    public RegistroEntrada(Long id) {
        this.id=id;
    }

    /**
     * @param re
     */
    public RegistroEntrada(RegistroEntrada re) {
      
      this.id = re.id;
      this.usuario = re.usuario;
      this.oficina = re.oficina;
      this.destino = re.destino;
      this.destinoExternoCodigo = re.destinoExternoCodigo;
      this.destinoExternoDenominacion = re.destinoExternoDenominacion;
      this.fecha = re.fecha;
      this.libro = re.libro;
      this.numeroRegistro = re.numeroRegistro;
      this.numeroRegistroFormateado = re.numeroRegistroFormateado;
      this.estado = re.estado;
      this.registroDetalle = re.registroDetalle == null? null : new RegistroDetalle(re.registroDetalle);
    }

    public RegistroEntrada(Long id, Integer numeroRegistro, Date fecha, Long idLibro, String nombreLibro, String denominacionOficina, String denominacionOrganismo) {
        this.id = id;
        this.numeroRegistro = numeroRegistro;
        this.fecha = fecha;
        this.libro = new Libro(idLibro, nombreLibro, null, null, denominacionOrganismo);
        this.oficina = new Oficina(null, null, denominacionOficina);
    }

    /**
     * Constructor para Informe LibroRegistro
     */
    public RegistroEntrada(Long idRegistro, Long idLibro, String nombreLibro, Long idOficina, String denominacionOficina, Date fecha,
                           Integer numeroRegistro, String numeroRegistroFormateado, String extracto, Long idOficinaOrigen, String denominacionOficinaOrigen,
                           String numeroRegistroOrigen, Date fechaOrigen, String destinoExternoDenominacion, Long idDestino,
                           String denominacionDestino, Long tipoDocumentacionFisica, Long idioma, String observaciones, Long estado,
                           String expediente, Long idCodigoAsunto, String referenciaExterna, Long transporte, String numeroTransporte,
                           Long idRegistroDetalle, String destinoExternoCodigo, List<Interesado> interesados) {

        this.id = idRegistro;
        this.numeroRegistro = numeroRegistro;
        this.numeroRegistroFormateado = numeroRegistroFormateado;
        this.fecha = fecha;
        this.estado = estado;
        this.destinoExternoCodigo = destinoExternoCodigo;
        this.destinoExternoDenominacion = destinoExternoDenominacion;
        this.destino = new Organismo(idDestino, denominacionDestino);
        this.registroDetalle = new RegistroDetalle(idRegistroDetalle, extracto, idOficinaOrigen, denominacionOficinaOrigen,
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_REGENT_ENTIDAD_FK")
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name="EVENTO")
    public Long getEvento() {
        return evento;
    }

    public void setEvento(Long evento) {
        this.evento = evento;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="USUARIO")
    @ForeignKey(name="RWE_REGENT_USUENT_FK")
    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = usuario;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="OFICINA")
    @ForeignKey(name="RWE_REGENT_OFICINA_FK")
    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    @ManyToOne()
    @JoinColumn(name="DESTINO")
    @ForeignKey(name="RWE_REGENT_DESTINO_FK")
    public Organismo getDestino() {
        return destino;
    }

    public void setDestino(Organismo destino) {
        this.destino = destino;
    }

    @Column(name = "DESTEXTCOD", length = 9)
    public String getDestinoExternoCodigo() {
        return destinoExternoCodigo;
    }

    public void setDestinoExternoCodigo(String destinoExternoCodigo) {
        this.destinoExternoCodigo = destinoExternoCodigo;
    }

    @Column(name = "DESTEXTDEN", length = 300)
    public String getDestinoExternoDenominacion() {
        return destinoExternoDenominacion;
    }

    public void setDestinoExternoDenominacion(String destinoExternoDenominacion) {
        this.destinoExternoDenominacion = destinoExternoDenominacion;
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
    @ForeignKey(name="RWE_REGENT_LIBRO_FK")
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
    @ForeignKey(name = "RWE_REGENT_REGDET_FK")
    public RegistroDetalle getRegistroDetalle() {
        return registroDetalle;
    }

    public void setRegistroDetalle(RegistroDetalle registroDetalle) {
        this.registroDetalle = registroDetalle;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistroEntrada that = (RegistroEntrada) o;

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
