/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.regweb3.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author mgonzalez
 * @author anadal (index)
 */
@Entity
@Table(name = "RWE_DESCARGA", indexes =
@Index(name = "RWE_DESCAR_ENTIDA_FK_I", columnList = "ENTIDAD"))
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Descarga implements Serializable {

    private Long id;
    private Date fechaImportacion;
    private Integer tipo;
    private Entidad entidad;
    private String elementos;

    public Descarga() {
    }

    public Descarga(Date fechaImportacion, Integer tipo, Entidad entidad, String elementos) {
        this.fechaImportacion = fechaImportacion;
        this.tipo = tipo;
        this.entidad = entidad;
        this.elementos = elementos;
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

    @Column(name = "FECHAIMPORTACION")
    public Date getFechaImportacion() {
        return fechaImportacion;
    }

    public void setFechaImportacion(Date fechaImportacion) {
        this.fechaImportacion = fechaImportacion;
    }

    @Column(name = "TIPO")
    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD", foreignKey =@ForeignKey(name = "RWE_DESCARGA_ENTIDAD_FK"))
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ELEMENTOS", length = 2147483647)
    public String getElementos() {
        return elementos;
    }

    public void setElementos(String elementos) {
        this.elementos = elementos;
    }

    @Transient
    public String getFechaFormateada() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaImportacion);
    }
}
