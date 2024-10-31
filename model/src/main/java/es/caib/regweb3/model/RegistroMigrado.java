package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by jpernia on 06/11/2014.
 */

@Entity
@Table(name = "RWE_REGISTRO_MIGRADO",
        indexes = {
                @Index(name = "RWE_REGMIG_ANO_I", columnList = "ANO"),
                @Index(name = "RWE_REGMIG_NUM_I", columnList = "NUMERO"),
                @Index(name = "RWE_REGMIG_TREG_I", columnList = "TREGISTRO"),
                @Index(name = "RWE_REGMIG_CODOF_I", columnList = "CODOFICINA"),
                @Index(name = "RWE_REGMIG_EXTR_I", columnList = "EXTRACTO"),
                @Index(name = "RWE_REGMIG_FECREG_I", columnList = "FECHAREG"),
                @Index(name = "RWE_REGMIG_REMDES_I", columnList = "DESREMDES"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name="RWE_REGISTRO_MIGRADO_UK", columnNames = {"ANO", "NUMERO", "CODOFICINA", "TREGISTRO", "IDENTIDAD"})
        })
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class RegistroMigrado implements Serializable {

    public static final boolean TIPOREGISTRO_ENTRADA = true;
    public static final boolean TIPOREGISTRO_SALIDA = false;

    private Long id;
    private int ano;
    private int numero;
    private boolean tipoRegistro;
    private Entidad entidad;
    private int codigoOficina;
    private String denominacionOficina;
    private int codigoOficinaFisica;
    private String denominacionOficinaFisica;
    private String extracto;
    private Date fechaDocumento;
    private Date fechaRegistro;
    private String descripcionRemitenteDestinatario;
    private int codigoOrganismoDestinatarioEmisor;
    private String descripcionOrganismoDestinatarioEmisor;
    private String tipoDocumento;
    private String descripcionDocumento;
    private int codigoIdiomaDocumento;
    private String descripcionIdiomaDocumento;
    private Date fechaVisado;
    private boolean anulado;
    private String otros;
    private int procedenciaDestinoGeograficoBaleares;
    private String procedenciaDestinoGeograficoFuera;
    private String procedenciaDestinoGeografico;
    private int numeroEntradaSalida;
    private int anoEntradaSalida;
    private int codigoIdiomaExtracto;
    private String nombreIdiomaExtracto;
    private Integer numeroDisquet;
    private String numeroCorreo;
    private String emailRemitente;
    private Set<RegistroLopdMigrado> registroLopdMigrados;
    private Set<ModificacionLopdMigrado> modificacionLopdMigrados;
    private String infoAdicional;

    /**
     *
     */
    public RegistroMigrado() {
    }

    public RegistroMigrado(int numero, int ano, String denominacionOficina, boolean tipoRegistro) {
        this.numero = numero;
        this.ano = ano;
        this.denominacionOficina = denominacionOficina;
        this.tipoRegistro = tipoRegistro;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    //@Index(name="RWE_REGISTRO_MIGRADO_PK_I")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ANO", nullable = false, length = 4)
    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Column(name = "NUMERO", nullable = false, length = 5)
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Column(name = "TREGISTRO")
    public boolean isTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(boolean tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDENTIDAD", referencedColumnName = "ID", nullable = true, foreignKey = @ForeignKey(name = "RWE_REGMIG_ENTIDAD_FK"))
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "CODOFICINA", nullable = false)
    public int getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(int codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    @Column(name = "DENOFICINA", nullable = false)
    public String getDenominacionOficina() {
        return denominacionOficina;
    }

    public void setDenominacionOficina(String denominacionOficina) {
        this.denominacionOficina = denominacionOficina;
    }

    @Column(name = "CODOFIFIS", nullable = false)
    public int getCodigoOficinaFisica() {
        return codigoOficinaFisica;
    }

    public void setCodigoOficinaFisica(int codigoOficinaFisica) {
        this.codigoOficinaFisica = codigoOficinaFisica;
    }

    @Column(name = "DENOFIFIS", nullable = false, length = 60)
    public String getDenominacionOficinaFisica() {
        return denominacionOficinaFisica;
    }

    public void setDenominacionOficinaFisica(String denominacionOficinaFisica) {
        this.denominacionOficinaFisica = denominacionOficinaFisica;
    }

    @Column(name = "EXTRACTO", nullable = false, length = 2000)
    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    @Column(name = "FECHADOC", nullable = false)
    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    @Column(name = "FECHAREG", nullable = false)
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Column(name = "DESREMDES", nullable = false, length = 160)
    public String getDescripcionRemitenteDestinatario() {
        return descripcionRemitenteDestinatario;
    }

    public void setDescripcionRemitenteDestinatario(String descripcionRemitenteDestinatario) {
        this.descripcionRemitenteDestinatario = descripcionRemitenteDestinatario;
    }

    @Column(name = "CODORGDESEMI", nullable = false, length = 4)
    public int getCodigoOrganismoDestinatarioEmisor() {
        return codigoOrganismoDestinatarioEmisor;
    }

    public void setCodigoOrganismoDestinatarioEmisor(int codigoOrganismoDestinatarioEmisor) {
        this.codigoOrganismoDestinatarioEmisor = codigoOrganismoDestinatarioEmisor;
    }

    @Column(name = "DESORGDESEMI", length = 60)
    public String getDescripcionOrganismoDestinatarioEmisor() {
        return descripcionOrganismoDestinatarioEmisor;
    }

    public void setDescripcionOrganismoDestinatarioEmisor(String descripcionOrganismoDestinatarioEmisor) {
        this.descripcionOrganismoDestinatarioEmisor = descripcionOrganismoDestinatarioEmisor;
    }

    @Column(name = "TIPODOC", nullable = false, length = 2)
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Column(name = "DESCDOC", nullable = false, length = 60)
    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }

    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }

    @Column(name = "CODIDIDOC", nullable = false, length = 1)
    public int getCodigoIdiomaDocumento() {
        return codigoIdiomaDocumento;
    }

    public void setCodigoIdiomaDocumento(int codigoIdiomaDocumento) {
        this.codigoIdiomaDocumento = codigoIdiomaDocumento;
    }

    @Column(name = "DESIDIDOC", nullable = false, length = 15)
    public String getDescripcionIdiomaDocumento() {
        return descripcionIdiomaDocumento;
    }

    public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento) {
        this.descripcionIdiomaDocumento = descripcionIdiomaDocumento;
    }

    @Column(name = "FECHAVIS")
    public Date getFechaVisado() {
        return fechaVisado;
    }

    public void setFechaVisado(Date fechaVisado) {
        this.fechaVisado = fechaVisado;
    }

    @Column(name = "ANULADO")
    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }

    @Column(name = "OTROS", length = 255)
    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    @Column(name = "PRODESGEOBAL", nullable = false, length = 3)
    public int getProcedenciaDestinoGeograficoBaleares() {
        return procedenciaDestinoGeograficoBaleares;
    }

    public void setProcedenciaDestinoGeograficoBaleares(int procedenciaDestinoGeograficoBaleares) {
        this.procedenciaDestinoGeograficoBaleares = procedenciaDestinoGeograficoBaleares;
    }

    @Column(name = "PRODESGEOFUE", length = 50)
    public String getProcedenciaDestinoGeograficoFuera() {
        return procedenciaDestinoGeograficoFuera;
    }

    public void setProcedenciaDestinoGeograficoFuera(String procedenciaDestinoGeograficoFuera) {
        this.procedenciaDestinoGeograficoFuera = procedenciaDestinoGeograficoFuera;
    }

    @Column(name = "PRODESGEO", nullable = false, length = 50)
    public String getProcedenciaDestinoGeografico() {
        return procedenciaDestinoGeografico;
    }

    public void setProcedenciaDestinoGeografico(String procedenciaDestinoGeografico) {
        this.procedenciaDestinoGeografico = procedenciaDestinoGeografico;
    }

    @Column(name = "NUMENTSAL", nullable = false, length = 6)
    public int getNumeroEntradaSalida() {
        return numeroEntradaSalida;
    }

    public void setNumeroEntradaSalida(int numeroEntradaSalida) {
        this.numeroEntradaSalida = numeroEntradaSalida;
    }

    @Column(name = "ANOENTSAL", nullable = false, length = 4)
    public int getAnoEntradaSalida() {
        return anoEntradaSalida;
    }

    public void setAnoEntradaSalida(int anoEntradaSalida) {
        this.anoEntradaSalida = anoEntradaSalida;
    }

    @Column(name = "CODIDIEXT", nullable = false)
    public int getCodigoIdiomaExtracto() {
        return codigoIdiomaExtracto;
    }

    public void setCodigoIdiomaExtracto(int codigoIdiomaExtracto) {
        this.codigoIdiomaExtracto = codigoIdiomaExtracto;
    }

    @Column(name = "DESIDIEXT", nullable = false, length = 15)
    public String getNombreIdiomaExtracto() {
        return nombreIdiomaExtracto;
    }

    public void setNombreIdiomaExtracto(String nombreIdiomaExtracto) {
        this.nombreIdiomaExtracto = nombreIdiomaExtracto;
    }

    @Column(name = "NUMDISQUET", length = 5)
    public Integer getNumeroDisquet() {
        return numeroDisquet;
    }

    public void setNumeroDisquet(Integer numeroDisquet) {
        this.numeroDisquet = numeroDisquet;
    }

    @Column(name = "NUMCORREO", length = 8)
    public String getNumeroCorreo() {
        return numeroCorreo;
    }

    public void setNumeroCorreo(String numeroCorreo) {
        this.numeroCorreo = numeroCorreo;
    }

    @Column(name = "MAILREMITENTE", length = 50)
    public String getEmailRemitente() {
        return emailRemitente;
    }

    public void setEmailRemitente(String emailRemitente) {
        this.emailRemitente = emailRemitente;
    }

    @OneToMany(cascade = CascadeType.REMOVE, targetEntity = RegistroLopdMigrado.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "REGMIG", foreignKey = @ForeignKey(name = "RWE_REGLOPDMIG_REGMIG_FK"))
    public Set<RegistroLopdMigrado> getRegistroLopdMigrados() {
        return registroLopdMigrados;
    }

    public void setRegistroLopdMigrados(Set<RegistroLopdMigrado> registroLopdMigrados) {
        this.registroLopdMigrados = registroLopdMigrados;
    }

    @OneToMany(cascade = CascadeType.REMOVE, targetEntity = ModificacionLopdMigrado.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "REGMIG", foreignKey = @ForeignKey(name = "RWE_MODLOPDMIG_REGMIG_FK"))
    public Set<ModificacionLopdMigrado> getModificacionLopdMigrados() {
        return modificacionLopdMigrados;
    }

    public void setModificacionLopdMigrados(Set<ModificacionLopdMigrado> modificacionLopdMigrados) {
        this.modificacionLopdMigrados = modificacionLopdMigrados;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    @Column(name = "INFOADICIONAL", length = 4000)
    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }
}
