/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.regweb3.model;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @author mgonzalez
 * @author anadal (index)
 */
@Table(name = "RWE_DESCARGA")
@org.hibernate.annotations.Table(appliesTo = "RWE_DESCARGA", indexes = {
        @Index(name = "RWE_DESCAR_ENTIDA_FK_I", columnNames = {"ENTIDAD"})
})
@Entity
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Descarga implements Serializable {

    private Long id;
    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaImportacion;
    private String tipo;
    private Entidad entidad;

    public Descarga() {
    }

    public Descarga(Date fechaImportacion, String tipo, Entidad entidad) {
        this.fechaImportacion = fechaImportacion;
        this.tipo = tipo;
        this.entidad = entidad;
    }

    @Column(name = "ID", nullable = false, length = 3)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FECHAINICIO")
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @Column(name = "FECHAFIN")
    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Column(name = "FECHAIMPORTACION")
    public Date getFechaImportacion() {
        return fechaImportacion;
    }

    public void setFechaImportacion(Date fechaImportacion) {
        this.fechaImportacion = fechaImportacion;
    }

    @Column(name = "TIPO")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_DESCARGA_ENTIDAD_FK")
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

}
