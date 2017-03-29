package es.caib.regweb3.model;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created 3/09/14 9:47
 * 
 * @author mgonzalez
 * @author anadal (index)
 */
@Entity
@Table(name = "RWE_OFICIO_REMISION")
@org.hibernate.annotations.Table(appliesTo = "RWE_OFICIO_REMISION", indexes = {
    @Index(name = "RWE_OFIREM_ORGANI_FK_I", columnNames = { "ORGANISMODEST" }),
    @Index(name = "RWE_OFIREM_LIBRO_FK_I", columnNames = { "LIBRO" }),
    @Index(name = "RWE_OFIREM_OFICIN_FK_I", columnNames = { "OFICINA" }),
    @Index(name = "RWE_OFIREM_USUARI_FK_I", columnNames = { "USUARIO" }) })
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class OficioRemision implements Serializable {

  private Long id;
  private Organismo organismoDestinatario;
  private String destinoExternoCodigo;
  private String destinoExternoDenominacion;
  private Long tipoOficioRemision; // entrada o salida
  private UsuarioEntidad usuarioResponsable;
  private Integer numeroOficio;
  private Oficina oficina;
  private Libro libro;
  private Date fecha;
  private List<RegistroEntrada> registrosEntrada;
  private List<RegistroSalida> registrosSalida;
  private int estado;
  private Date fechaEstado;

  private Boolean sir = false;
  private AsientoRegistralSir asientoRegistralSir;
  private Date fechaRecepcion;
  private Date fechaEnvio;
  private String codigoError;
  private String descripcionError;
  private Integer numeroReintentos;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @ManyToOne()
  @JoinColumn(name = "ORGANISMODEST")
  @ForeignKey(name = "RWE_OFIREM_ORGANISMODEST_FK")
  public Organismo getOrganismoDestinatario() {
    return organismoDestinatario;
  }

  public void setOrganismoDestinatario(Organismo organismoDestinatario) {
    this.organismoDestinatario = organismoDestinatario;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "LIBRO")
  @ForeignKey(name = "RWE_OFIREM_LIBRO_FK")
  public Libro getLibro() {
    return libro;
  }

  public void setLibro(Libro libro) {
    this.libro = libro;
  }

  @Column(name = "NUMREGISTRO", nullable = false)
  public Integer getNumeroOficio() {
    return numeroOficio;
  }

  public void setNumeroOficio(Integer numeroOficio) {
    this.numeroOficio = numeroOficio;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "OFICINA")
  @ForeignKey(name = "RWE_OFIREM_OFICINA_FK")
  public Oficina getOficina() {
    return oficina;
  }

  public void setOficina(Oficina oficina) {
    this.oficina = oficina;
  }

  @ManyToMany(targetEntity = RegistroEntrada.class, fetch = FetchType.LAZY)
  @JoinTable(name = "RWE_OFIREM_REGENT", joinColumns = { @JoinColumn(name = "IDOFIREM") }, inverseJoinColumns = { @JoinColumn(name = "IDREGENT") })
  @ForeignKey(name = "RWE_REGENT_OFIREM_FK", inverseName = "RWE_OFIREM_REGENT_FK")
  @OrderBy("id")
  public List<RegistroEntrada> getRegistrosEntrada() {
    return registrosEntrada;
  }

  public void setRegistrosEntrada(List<RegistroEntrada> registrosEntrada) {
    this.registrosEntrada = registrosEntrada;
  }

  @ManyToMany(targetEntity = RegistroSalida.class, fetch = FetchType.LAZY)
  @JoinTable(name = "RWE_OFIREM_REGSAL", joinColumns = { @JoinColumn(name = "IDOFIREM") }, inverseJoinColumns = { @JoinColumn(name = "IDREGSAL") })
  @ForeignKey(name = "RWE_REGSAL_OFIREM_FK", inverseName = "RWE_OFIREM_REGSAL_FK")
  @OrderBy("id")
  public List<RegistroSalida> getRegistrosSalida() {
    return registrosSalida;
  }

  public void setRegistrosSalida(List<RegistroSalida> registrosSalida) {
    this.registrosSalida = registrosSalida;
  }

  @Column(name = "FECHA", nullable = false)
  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  @Column(name = "TIPO_OFICIO", nullable = false)
  public Long getTipoOficioRemision() {
    return tipoOficioRemision;
  }

  public void setTipoOficioRemision(Long tipoOficioRemision) {
    this.tipoOficioRemision = tipoOficioRemision;
  }


  @ManyToOne(optional = false)
  @JoinColumn(name = "USUARIO")
  @ForeignKey(name = "RWE_OFIREM_USUORM_FK")
  public UsuarioEntidad getUsuarioResponsable() {
    return usuarioResponsable;
  }

  public void setUsuarioResponsable(UsuarioEntidad usuarioResponsable) {
    this.usuarioResponsable = usuarioResponsable;
  }

  /**
   * @return Indica el estado del oficio de remision:
   *         -RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO(0): (Interno)
   *         -RegwebConstantes.OFICIO_REMISION_EXTERNO_ENVIADO(1): (Externo)
   *         -RegwebConstantes.OFICIO_REMISION_ACEPTADO(2): (Interno y Externo)
   *         -RegwebConstantes.OFICIO_REMISION_RECHAZADO(3): (Solo Externo)
   *         -RegwebConstantes.OFICIO_REMISION_REENVIADO(4): (Solo Externo)
   *         -RegwebConstantes.OFICIO_REMISION_ANULADO(4): (Interno y Externo)
   */
  @Column(name = "ESTADO", nullable = false)
  public int getEstado() {
    return estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }

  @Column(name = "FECHA_ESTADO")
  public Date getFechaEstado() {
    return fechaEstado;
  }

  public void setFechaEstado(Date fechaEstado) {
    this.fechaEstado = fechaEstado;
  }

  @Column(name = "DESTINOEXTERNOCODIGO", length = 9)
  public String getDestinoExternoCodigo() {
    return destinoExternoCodigo;
  }

  public void setDestinoExternoCodigo(String destinoExternoCodigo) {
    this.destinoExternoCodigo = destinoExternoCodigo;
  }

  @Column(name = "DESTINOEXTERNODENOMINA", length = 300)
  public String getDestinoExternoDenominacion() {
    return destinoExternoDenominacion;
  }

  public void setDestinoExternoDenominacion(String destinoExternoDenominacion) {
    this.destinoExternoDenominacion = destinoExternoDenominacion;
  }

  @Column(name = "SIR", nullable = false)
  public Boolean getSir() {
    return sir;
  }

  public void setSir(Boolean sir) {
    this.sir = sir;
  }

  @ManyToOne(optional = true)
  @JoinColumn(name = "ASIENTO_REGISTRAL_SIR")
  @ForeignKey(name = "RWE_OFIREM_ASR_FK")
  public AsientoRegistralSir getAsientoRegistralSir() {
    return asientoRegistralSir;
  }

  public void setAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) {
    this.asientoRegistralSir = asientoRegistralSir;
  }

  @Column(name = "FECHA_RECEPCION", nullable = true)
  public Date getFechaRecepcion() {
    return fechaRecepcion;
  }

  public void setFechaRecepcion(Date fechaRecepcion) {
    this.fechaRecepcion = fechaRecepcion;
  }

  @Column(name = "FECHA_ENVIO", nullable = true)
  public Date getFechaEnvio() {
    return fechaEnvio;
  }

  public void setFechaEnvio(Date fechaEnvio) {
    this.fechaEnvio = fechaEnvio;
  }

  @Column(name = "COD_ERROR", nullable = true)
  public String getCodigoError() {
    return codigoError;
  }

  public void setCodigoError(String codigoError) {
    this.codigoError = codigoError;
  }

  @Column(name = "DESC_ERROR", length = 2000 ,nullable = true)
  public String getDescripcionError() {
    return descripcionError;
  }

  public void setDescripcionError(String descripcionError) {
    this.descripcionError = descripcionError;
  }

  @Column(name = "REINTENTOS", nullable = true)
  public Integer getNumeroReintentos() {
    return numeroReintentos;
  }

  public void setNumeroReintentos(Integer numeroReintentos) {
    this.numeroReintentos = numeroReintentos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    OficioRemision oficioRemision = (OficioRemision) o;

    if (id != null ? !id.equals(oficioRemision.id) : oficioRemision.id != null)
      return false;

    return true;
  }

  @Override
  public String toString() {
    if (id != null) {
      return id.toString();
    } else {
      return null;
    }
  }
}
