package es.caib.regweb3.model;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 30/10/14
 */
@Entity
@Table(name = "RWE_HISTORICO_REGISTRO_SALIDA")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class HistoricoRegistroSalida implements Serializable {

    private Long id;
    private RegistroSalida registroSalida;
    private String registroSalidaOriginal;
    private Long estado;
    private Date fecha;
    private String modificacion;
    private UsuarioEntidad usuario;


    public HistoricoRegistroSalida() {
    }

    public HistoricoRegistroSalida(Long id, String registroSalidaOriginal, Long estado, Date fecha, String modificacion, Long usuarioEntidad, Usuario usuario) {
        this.id = id;
        this.registroSalidaOriginal = registroSalidaOriginal;
        this.estado = estado;
        this.fecha = fecha;
        this.modificacion = modificacion;
        this.usuario = new UsuarioEntidad(usuarioEntidad, usuario, null);
    }

    public HistoricoRegistroSalida(Long idRegistro, Integer numeroRegistro, String nombreLibro, String denominacionOficina, String denominacionOrganismo, Date fechaRegistro, Date fecha, String modificacion) {
        this.registroSalida = new RegistroSalida(idRegistro, numeroRegistro, fechaRegistro, null, nombreLibro, denominacionOficina, denominacionOrganismo);
        this.fecha = fecha;
        this.modificacion = modificacion;
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


    @ManyToOne()
    @JoinColumn(name = "REGISTRO_SALIDA")
    @ForeignKey(name = "RWE_HITORICO_RS_FK")
    public RegistroSalida getRegistroSalida() {
        return registroSalida;
    }

    public void setRegistroSalida(RegistroSalida registroSalida) {
        this.registroSalida = registroSalida;
    }

    @Lob
    @Column(name = "RS_ORIGINAL")
    public String getRegistroSalidaOriginal() {
        return registroSalidaOriginal;
    }

    public void setRegistroSalidaOriginal(String registroSalidaOriginal) {
        this.registroSalidaOriginal = registroSalidaOriginal;
    }

    
    @Column(name = "ESTADO")    
    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @Column(name = "FECHA", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    @Column(name = "MODIFICACION", nullable = false)
    public String getModificacion() {
        return modificacion;
    }

    public void setModificacion(String modificacion) {
        this.modificacion = modificacion;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO")
    @ForeignKey(name = "RWE_HISTORICO_USUARIO_RS_FK")
    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = usuario;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoricoRegistroSalida historicoRegistroSalida = (HistoricoRegistroSalida) o;

        if (id != null ? !id.equals(historicoRegistroSalida.id) : historicoRegistroSalida.id != null) return false;

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
}

