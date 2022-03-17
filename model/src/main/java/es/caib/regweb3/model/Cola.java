package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mgonzalez on 21/03/2018.
 * Clase que representa un elemento de la cola de elementos a procesar en diferido.
 * Por ejemplo la distribución de registros.
 */
@Entity
@Table(name = "RWE_COLA")
@SequenceGenerator(name="generator",sequenceName = "RWE_COLA_SEQ", allocationSize = 1)
public class Cola implements Serializable{


    private Long id;
    private Long tipo; //de que tipo es el elemento de la cola(por el momento solo seran distribuciones)
    private Integer numeroMaximoReintentos;
    private int numeroReintentos = 0;
    private Long idObjeto; // Identificador del objeto en cola(Ex: registro pendiente de distribuir)
    private String descripcionObjeto; // Descripción del objeto en cola(Ex: registro pendiente de distribuir)
    private String denominacionOficina; //Denominacion Oficina en la que se ha creado el objeto
    private String error; // Campo que nos servirá para guardar información tipo (excepcion producida, etc).
    private Date fecha; // fecha entrada en cola
    private UsuarioEntidad usuarioEntidad;
    private Long estado;
    private Long tipoRegistro;
    private Date fechaProcesado;

    public static final int RESULTADOS_PAGINACION = 20;

    public Cola() {
        this.fecha= new Date();
        this.error= "&nbsp;";
    }

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="TIPO")
    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    @Column(name="NUMMAXREINTENTOS")
    public Integer getNumeroMaximoReintentos() {
        return numeroMaximoReintentos;
    }

    public void setNumeroMaximoReintentos(Integer numeroMaximoReintentos) {
        this.numeroMaximoReintentos = numeroMaximoReintentos;
    }

    @Column(name="NUMREINTENTOS")
    public int getNumeroReintentos() {
        return numeroReintentos;
    }

    public void setNumeroReintentos(int numeroReintentos) {
        this.numeroReintentos = numeroReintentos;
    }

    @Column(name="IDOBJETO")
    public Long getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Long idObjeto) {
        this.idObjeto = idObjeto;
    }

    @Column(name="DESCRIPCIONOBJETO")
    public String getDescripcionObjeto() {
        return descripcionObjeto;
    }

    public void setDescripcionObjeto(String descripcionObjeto) {
        this.descripcionObjeto = descripcionObjeto;
    }


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="USUARIOENTIDAD")
    @ForeignKey(name="RWE_COLA_USUENTI_FK")
    @JsonIgnore
    public UsuarioEntidad getUsuarioEntidad() {
        return usuarioEntidad;
    }

    public void setUsuarioEntidad(UsuarioEntidad usuarioEntidad) {
        this.usuarioEntidad = usuarioEntidad;
    }


    @Column(name="DENOMINACIONOFICINA")
    public String getDenominacionOficina() {
        return denominacionOficina;
    }

    public void setDenominacionOficina(String denominacionOficina) {
        this.denominacionOficina = denominacionOficina;
    }

    @Lob
    @Column(name="ERROR",  length=2147483647)
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Column(name = "FECHA")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Column(name = "ESTADO")
    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @Column(name = "TIPOREGISTRO")
    public Long getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    @Column(name = "FECHAPROCESADO")
    public Date getFechaProcesado() {
        return fechaProcesado;
    }

    public void setFechaProcesado(Date fechaProcesado) {
        this.fechaProcesado = fechaProcesado;
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

    @XmlElement(name="fechaFormateada")
    public String fechaFormateada(){
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatDate.format(fecha);
    }

    @Transient
    private String fechaFormateada;

    @Transient
    public String getFechaFormateada() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fecha);
    }

    @Transient
    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }
}
