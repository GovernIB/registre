package es.caib.regweb3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.caib.regweb3.model.utils.DocumentoVisor;

/**
 * Created by Limit Tecnologies S.L.
 * @author Jamal
 */
@Entity
@Table(name = "RWE_REMESA")
@org.hibernate.annotations.Table(appliesTo = "RWE_REMESA", indexes = {
    @Index(name="RWE_REMESA_CONCE_I", columnNames = {"CONCEPTO"}),
    @Index(name="RWE_REMESA_IDENT_I", columnNames = {"IDENTIFICADOR"}),
    @Index(name="RWE_REMESA_TIPO_I", columnNames = {"TIPO"}),
    @Index(name="RWE_REMESA_EMISOR_CODI_I", columnNames = {"ORGANO_EMISOR_CODIGO"}),
    @Index(name="RWE_REMESA_EMISOR_NOM_I", columnNames = {"ORGANO_EMISOR_NOMBRE"}),
    @Index(name="RWE_REMESA_TITULAR_NIF_I", columnNames = {"TITULAR_NIF"}),
    @Index(name="RWE_REMESA_TITULAR_NOM_I", columnNames = {"TITULAR_NOMBRE"}),
    @Index(name="RWE_REMESA_ESTADO_I", columnNames = {"ESTADO"}),
    @Index(name="RWE_REMESA_ESTADO_NOTIF_I", columnNames = {"ESTADO_NOTIFICA"}),
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Remesa implements Serializable {

	private static final long serialVersionUID = 3077948894205536490L;
	
	private Long id;
    private String concepto;
    private String descripcion;
    private String identificador;
    private String identificadorIntern;
    private String referencia;
    private Integer codigoOrigen;
    private Integer tipo;
    private String organoEmisorCodigo;
    private String organoEmisorNif;
    private String organoEmisorNombre;
    private Date fechaPuestaDisposicion;
    private String titularNif;
    private String titularNombre;
    private String titularDir3codi;
    private String entidadCodigo;
    private String entidadNombre;
    private String estado;
    private String estadoNotifica;
    private String codigoProcedimiento;
    private Date fechaEstado;
    private Date fechaCreacion;
    private Date fechaEnvio;
    private Date fechaFinalizacion;
    
    // Reintentos lectura notificación
    private Integer reintentosLectura;
    
    private RegistroEntrada registro;
    
    private Entidad entidad;

    private UsuarioEntidad usuario;
    
    private List<DocumentoVisor> documentosRecibidos = new ArrayList<DocumentoVisor>();
    
    private TipoRemesa tipoRemesa;
    
    private String mensajeError;
    
    private Boolean documentoDescomprimido = false; 
    
    public enum TipoRemesa {
    	ENVIADA,
    	RECIBIDA
    }
    
    public Remesa() { }


	public Remesa(String concepto, String descripcion, String identificador, String identificadorIntern, Integer codigoOrigen, Integer tipo, String organoEmisorCodigo, String organoEmisorNif,
			String organoEmisorNombre, Date fechaPuestaDisposicion, String titularNif, String titularNombre, String titularDir3codi, String entidadCodigo, String entidadNombre, 
			String estado, String estadoNotifica, String codigoProcedimiento, Integer reintentosLectura, Entidad entidad, UsuarioEntidad usuario, TipoRemesa tipoRemesa) {
		super();
		this.concepto = concepto;
		this.descripcion = descripcion;
		this.identificador = identificador;
		this.identificadorIntern = identificadorIntern;
		this.codigoOrigen = codigoOrigen;
		this.tipo = tipo;
		this.organoEmisorCodigo = organoEmisorCodigo;
		this.organoEmisorNif = organoEmisorNif;
		this.organoEmisorNombre = organoEmisorNombre;
		this.fechaPuestaDisposicion = fechaPuestaDisposicion;
		this.titularNif = titularNif;
		this.titularNombre = titularNombre;
		this.titularDir3codi = titularDir3codi;
		this.entidadCodigo = entidadCodigo;
		this.entidadNombre = entidadNombre;
		this.estado = estado;
		this.estadoNotifica = estadoNotifica;
		this.codigoProcedimiento = codigoProcedimiento;
		this.reintentosLectura = reintentosLectura;
		this.entidad = entidad;
		this.usuario = usuario;
		this.tipoRemesa = tipoRemesa;
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

    @Column(name = "CONCEPTO", nullable = false)
	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	@Column(name = "DESCRIPCION")
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "IDENTIFICADOR")
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	@Column(name = "IDENTIFICADOR_INTERN")
	public String getIdentificadorIntern() {
		return identificadorIntern;
	}

	public void setIdentificadorIntern(String identificadorIntern) {
		this.identificadorIntern = identificadorIntern;
	}
	
	@Column(name = "REFERENCIA")
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@Column(name = "CODIGO_ORIGEN")
	public Integer getCodigoOrigen() {
		return codigoOrigen;
	}


	public void setCodigoOrigen(Integer codigoOrigen) {
		this.codigoOrigen = codigoOrigen;
	}


	@Column(name = "TIPO", nullable = false)
	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	@Column(name = "ORGANO_EMISOR_CODIGO", nullable = false)
	public String getOrganoEmisorCodigo() {
		return organoEmisorCodigo;
	}

	public void setOrganoEmisorNif(String organoEmisorNif) {
		this.organoEmisorNif = organoEmisorNif;
	}

	@Column(name = "ORGANO_EMISOR_NIF", nullable = true)
	public String getOrganoEmisorNif() {
		return organoEmisorNif;
	}

	public void setOrganoEmisorCodigo(String organoEmisorCodigo) {
		this.organoEmisorCodigo = organoEmisorCodigo;
	}
	
	@Column(name = "ORGANO_EMISOR_NOMBRE", nullable = false)
	public String getOrganoEmisorNombre() {
		return organoEmisorNombre;
	}

	public void setOrganoEmisorNombre(String organoEmisorNombre) {
		this.organoEmisorNombre = organoEmisorNombre;
	}

	@Column(name = "FECHA_PUESTA_DISPOSICION")
	public Date getFechaPuestaDisposicion() {
		return fechaPuestaDisposicion;
	}

	public void setFechaPuestaDisposicion(Date fechaPuestaDisposicion) {
		this.fechaPuestaDisposicion = fechaPuestaDisposicion;
	}

	@Column(name = "TITULAR_NIF")
	public String getTitularNif() {
		return titularNif;
	}

	public void setTitularNif(String titularNif) {
		this.titularNif = titularNif;
	}

	@Column(name = "TITULAR_NOMBRE")
	public String getTitularNombre() {
		return titularNombre;
	}

	public void setTitularNombre(String titularNombre) {
		this.titularNombre = titularNombre;
	}
	
	@Column(name = "TITULAR_DIR3CODI")
	public String getTitularDir3codi() {
		return titularDir3codi;
	}

	public void setTitularDir3codi(String titularDir3codi) {
		this.titularDir3codi = titularDir3codi;
	}


	@Column(name = "ENTIDAD_CODIGO")
	public String getEntidadCodigo() {
		return entidadCodigo;
	}


	public void setEntidadCodigo(String entidadCodigo) {
		this.entidadCodigo = entidadCodigo;
	}

	@Column(name = "ENTIDAD_NOMBRE")
	public String getEntidadNombre() {
		return entidadNombre;
	}


	public void setEntidadNombre(String entidadNombre) {
		this.entidadNombre = entidadNombre;
	}


	@Column(name = "ESTADO", nullable = false)
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "ESTADO_NOTIFICA")
	public String getEstadoNotifica() {
		return estadoNotifica;
	}

	public void setEstadoNotifica(String estadoNotifica) {
		this.estadoNotifica = estadoNotifica;
	}
	
	@Column(name = "CODIGO_PROCEDIMIENTO")
    public String getCodigoProcedimiento() {
		return codigoProcedimiento;
	}


	public void setCodigoProcedimiento(String codigoProcedimiento) {
		this.codigoProcedimiento = codigoProcedimiento;
	}

	@Column(name = "REINTENTOS_LECTURA")
	public Integer getReintentosLectura() {
		return reintentosLectura;
	}

	public void setReintentosLectura(Integer reintentosLectura) {
		this.reintentosLectura = reintentosLectura;
	}
	
	@ManyToOne(optional = true)
    @JoinColumn(name = "REGISTRO")
    @ForeignKey(name = "RWE_REMESA_REGISTRO_FK")
    @JsonIgnore
	public RegistroEntrada getRegistro() {
		return registro;
	}

	public void setRegistro(RegistroEntrada registro) {
		this.registro = registro;
	}

	@ManyToOne(optional = false)
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_REMESA_ENTIDAD_FK")
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

	@ManyToOne(optional = true)
    @JoinColumn(name = "USUARIO")
    @ForeignKey(name = "RWE_REMESA_USUARIO_ENTIDAD_FK")
    @JsonIgnore
    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = usuario;
    }

	@Column(name = "TIPO_REMESA", nullable = true)
	@Enumerated(EnumType.STRING)
	public TipoRemesa getTipoRemesa() {
		return tipoRemesa;
	}

	public void setTipoRemesa(TipoRemesa tipoRemesa) {
		this.tipoRemesa = tipoRemesa;
	}

	@Column(name = "FECHA_ESTADO")
	public Date getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	@Column(name = "FECHA_CREACION")
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "FECHA_ENVIO")
	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	@Column(name = "FECHA_FINALIZACION")
	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	@Column(name = "MENSAJE_ERROR")
	public String getMensajeError() {
		return mensajeError;
	}


	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}


	@Column(name = "DOCUMENTO_DESCOMPRIMIDO")
	public Boolean getDocumentoDescomprimido() {
		return documentoDescomprimido;
	}
	
	public void setDocumentoDescomprimido(Boolean documentoDescomprimido) {
		this.documentoDescomprimido = documentoDescomprimido;
	}


	@Transient
	public List<DocumentoVisor> getDocumentosRecibidos() {
		return documentosRecibidos;
	}

	public void setDocumentosRecibidos(List<DocumentoVisor> documentosRecibidos) {
		this.documentosRecibidos = documentosRecibidos;
	}

	@Transient
    private Integer pageNumber = 1;

    @Transient
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Transient
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    
}
