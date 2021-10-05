package es.caib.regweb3.model;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created 01/10/14 12:25
 *
 * @author jpernia
 * @author anadal (index)
 */
@Entity
@Table(name = "RWE_LOPD")
@org.hibernate.annotations.Table(appliesTo = "RWE_LOPD", indexes = {
    @Index(name="RWE_LOPD_LIBRO_FK_I", columnNames = {"LIBRO"}),
    @Index(name="RWE_LOPD_USUENT_FK_I", columnNames = {"USUARIO"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Lopd implements Serializable{

    private Long id;
    private String numeroRegistro;
    private Long tipoRegistro;
    private String anyoRegistro;
    private Libro libro;
    private Date fecha;
    private UsuarioEntidad usuario;
    private Long accion;


    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "NUMREGISTRO", nullable = true)
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }


    @Column(name = "TIPOREGISTRO", nullable=false)
    public Long getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }


    @Column(name = "ANYOREGISTRO", nullable=true)
    public String getAnyoRegistro() {
        return anyoRegistro;
    }

    public void setAnyoRegistro(String anyoRegistro) {
        this.anyoRegistro = anyoRegistro;
    }


    @ManyToOne(optional = false)
    @JoinColumn(name="LIBRO")
    @ForeignKey(name="RWE_LOPD_LIBRO_FK")
    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }


    @Column(name = "FECHA", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    @ManyToOne(optional = false)
    @JoinColumn(name="USUARIO")
    @ForeignKey(name="RWE_LOPD_USUENT_FK")
    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = usuario;
    }


    @Column(name = "ACCION", nullable=false)
    public Long getAccion() {
        return accion;
    }

    public void setAccion(Long accion) {
        this.accion = accion;
    }

}
