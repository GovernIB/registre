package es.caib.regweb3.model;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (index)
 * Date: 16/01/14
 */
@Entity
@Table(name = "RWE_HISTORICO_REGISTRO_ENTRADA", indexes = {
        @Index(name = "RWE_HRE_REGENT_FK_I", columnList = "REGISTRO_ENTRADA"),
        @Index(name = "RWE_HRE_USUENT_FK_I", columnList = "USUARIO")})
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class HistoricoRegistroEntrada implements Serializable {

    private Long id;
    private RegistroEntrada registroEntrada;
    private String registroEntradaOriginal;
    private Long estado;
    private Date fecha;
    private String modificacion;
    private UsuarioEntidad usuario;

    public HistoricoRegistroEntrada() {
    }

    public HistoricoRegistroEntrada(Long id, String registroEntradaOriginal, Long estado, Date fecha, String modificacion, Long usuarioEntidad, Usuario usuario) {
        this.id = id;
        this.registroEntradaOriginal = registroEntradaOriginal;
        this.estado = estado;
        this.fecha = fecha;
        this.modificacion = modificacion;
        this.usuario = new UsuarioEntidad(usuarioEntidad, usuario, null);
    }

    public HistoricoRegistroEntrada(Long idRegistro, Integer numeroRegistro, String nombreLibro, String denominacionOficina, String denominacionOrganismo, Date fechaRegistro, Date fecha, String modificacion) {
        this.registroEntrada = new RegistroEntrada(idRegistro, numeroRegistro, fechaRegistro, null, nombreLibro, denominacionOficina, denominacionOrganismo);
        this.fecha = fecha;
        this.modificacion = modificacion;
    }

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
    @JoinColumn(name = "REGISTRO_ENTRADA", foreignKey =@ForeignKey(name = "RWE_HITORICO_RE_FK"))
    public RegistroEntrada getRegistroEntrada() {
        return registroEntrada;
    }

    public void setRegistroEntrada(RegistroEntrada registroEntrada) {
        this.registroEntrada = registroEntrada;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "RE_ORIGINAL")
    public String getRegistroEntradaOriginal() {
        return registroEntradaOriginal;
    }

    public void setRegistroEntradaOriginal(String registroEntradaOriginal) {
        this.registroEntradaOriginal = registroEntradaOriginal;
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
    @JoinColumn(name = "USUARIO", foreignKey =@ForeignKey(name = "RWE_HISTORICO_USUARIO_FK"))
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

        HistoricoRegistroEntrada historicoRegistroEntrada = (HistoricoRegistroEntrada) o;

        if (id != null ? !id.equals(historicoRegistroEntrada.id) : historicoRegistroEntrada.id != null) return false;

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
