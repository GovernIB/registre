package es.caib.regweb3.persistence.dto;

import es.caib.regweb3.model.Entidad;

import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IntegracionDTO {

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

    public IntegracionDTO(Long id, Entidad entidad, Long tipo, Long estado, Long tiempo, Date fecha, String descripcion, String peticion, String error, String excepcion, String numRegFormat) {
        this.id = id;
        this.entidad = entidad;
        this.tipo = tipo;
        this.estado = estado;
        this.tiempo = tiempo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.peticion = peticion;
        this.error = error;
        this.excepcion = excepcion;
        this.numRegFormat = numRegFormat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Long getTiempo() {
        return tiempo;
    }

    public void setTiempo(Long tiempo) {
        this.tiempo = tiempo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPeticion() {
        return peticion;
    }

    public void setPeticion(String peticion) {
        this.peticion = peticion;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getExcepcion() {
        return excepcion;
    }

    public void setExcepcion(String excepcion) {
        this.excepcion = excepcion;
    }

    public String getNumRegFormat() {
        return numRegFormat;
    }

    public void setNumRegFormat(String numRegFormat) {
        this.numRegFormat = numRegFormat;
    }

    public String getFechaFormateada() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fecha);
    }


    public String getTiempoFormateado() {

        Date date = new Date(tiempo);
        return new SimpleDateFormat("mm:ss:SSS").format(date);
    }

    public String getErrorCorto(){

        String errorCorto = getError();

        if (errorCorto != null && errorCorto.length() > 50) {
            errorCorto = errorCorto.substring(0, 50) + "...";
        }

        return errorCorto;
    }

    private Integer pageNumber = 1;


    public Integer getPageNumber() {
        return pageNumber;
    }


    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
