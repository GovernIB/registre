package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fundació BIT.
 * @author earrivi
 * Date: 06/03/18
 */
@Entity
@Table(name = "RWE_INTEGRACION", indexes = @Index(name="RWE_INT_ENTIDAD_FK_I", columnList = "ENTIDAD"))
@SequenceGenerator(name="generator",sequenceName = "RWE_INT_SEQ", allocationSize = 1)
public class Integracion implements Serializable {

    private Long id;
    private Entidad entidad;
    private Long tipo;
    private Long estado;
    private Long tiempo;
    private Date fecha;
    private String descripcion;
    private String peticion;
    private String error;
    private String excepcion;
    private String numRegFormat;

    public static final int RESULTADOS_PAGINACION = 50;

    public Integracion() { }

    public Integracion(Long tipo) {
        this.tipo = tipo;
    }

    public Integracion(Date fecha, Long tipo, String numRegFormat, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.numRegFormat = numRegFormat;
    }

    public Integracion(Date inicio, Long tipo, Long estado, String descripcion, String peticion, Long tiempo,Long idEntidad, String numRegFormat) {
        this.tipo = tipo;
        this.estado = estado;
        this.descripcion = descripcion;
        this.entidad = new Entidad(idEntidad);
        this.peticion = peticion;
        this.tiempo = tiempo;
        this.fecha = inicio;
        this.numRegFormat = numRegFormat;
    }

    public Integracion(Long tipo, Long estado, String descripcion, String peticion, String error, String excepcion, Long tiempo, Long idEntidad, String numRegFormat) {
        this.tipo = tipo;
        this.estado = estado;
        this.descripcion = descripcion;
        this.entidad = new Entidad(idEntidad);
        this.peticion = peticion;
        this.tiempo = tiempo;
        this.error = error;
        this.excepcion = excepcion;
        this.fecha = new Date();
        this.numRegFormat = numRegFormat;
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
    @JoinColumn(name = "ENTIDAD", foreignKey =@ForeignKey(name = "RWE_INT_ENTIDAD_FK"))
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "TIPO", nullable = false)
    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    @Column(name = "ESTADO", nullable = false)
    public Long getEstado() {
        return estado;
    }

    @Column(name = "TIEMPO", nullable = false)
    public Long getTiempo() {
        return tiempo;
    }

    public void setTiempo(Long tiempo) {
        this.tiempo = tiempo;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @Column(name = "FECHA")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Column(name = "DESCRIPCION", length = 400)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "PETICION", length = 2000)
    public String getPeticion() {
        return peticion;
    }

    public void setPeticion(String peticion) {
        this.peticion = peticion;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ERROR", length = 2147483647)
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "EXCEPCION", length = 2147483647)
    public String getExcepcion() {
        return excepcion;
    }

    public void setExcepcion(String excepcion) {
        this.excepcion = excepcion;
    }

    @Column(name = "NUMREGFORMAT", length = 255)
    public String getNumRegFormat() {
        return numRegFormat;
    }

    public void setNumRegFormat(String numRegFormat) {
        this.numRegFormat = numRegFormat;
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

    @Transient
    private String tiempoFormateado;

    @Transient
    public String getTiempoFormateado() {

        Date date = new Date(tiempo);
        return new SimpleDateFormat("mm:ss:SSS").format(date);
    }

    @Transient
    public void setTiempoFormateado(String tiempoFormateado) {
        this.tiempoFormateado = tiempoFormateado;
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

    @Transient
    public String getErrorCorto(){

        String errorCorto = getError();

        if (errorCorto != null && errorCorto.length() > 50) {
            errorCorto = errorCorto.substring(0, 50) + "...";
        }

        return errorCorto;
    }
}
