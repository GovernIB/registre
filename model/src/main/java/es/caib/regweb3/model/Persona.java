package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 6/02/14
 */
@Entity
@Table(name = "RWE_PERSONA", indexes = {
        @Index(name = "RWE_PERSONA_ENTIDAD_FK_I", columnList = "ENTIDAD")
})
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Persona implements Serializable {

    private Long id;
    private Long tipo;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String razonSocial;
    private Long tipoDocumentoIdentificacion;
    private String documento;
    private CatPais pais;
    private CatProvincia provincia;
    private CatLocalidad localidad;
    private String direccion;
    private String cp;
    private String email;
    private String telefono;
    private String direccionElectronica;
    private Long canal;
    private String observaciones;

    private Entidad entidad;

   /* private Persona representante;
    private Persona representado;
    private Boolean isRepresentante = false;*/

    private boolean guardarInteresado;

    public Persona() {
    }

    public Persona(Long id) {
        this.id = id;
    }

    public Persona(String id) {
        this.id = Long.valueOf(id);
    }

    public Persona(Long id, String razonSocial, String documento, Long tipo) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.documento = documento;
        this.tipo = tipo;
    }

    public Persona(Long id, String nombre, String apellido1, String apellido2, String documento, Long tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.documento = documento;
        this.tipo = tipo;
    }

    public Persona(Long id, String nombre, String apellido1, String apellido2, String razonSocial, String documento, Long tipo,
                   String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.razonSocial = razonSocial;
        this.documento = documento;
        this.tipo = tipo;
        this.email = email;
        this.telefono = telefono;
    }


    public Persona(Interesado interesado) {

        if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
            this.tipo = RegwebConstantes.TIPO_PERSONA_FISICA;

        } else if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
            this.tipo = RegwebConstantes.TIPO_PERSONA_JURIDICA;
        }

        this.razonSocial = interesado.getRazonSocial();
        this.nombre = interesado.getNombre();
        this.apellido1 = interesado.getApellido1();
        this.apellido2 = interesado.getApellido2();
        this.tipoDocumentoIdentificacion = interesado.getTipoDocumentoIdentificacion();
        this.documento = interesado.getDocumento();
        this.pais = interesado.getPais();
        this.provincia = interesado.getProvincia();
        this.localidad = interesado.getLocalidad();
        this.direccion = interesado.getDireccion();
        this.cp = interesado.getCp();
        this.email = interesado.getEmail();
        this.telefono = interesado.getTelefono();
        this.canal = interesado.getCanal();
        this.observaciones = interesado.getObservaciones();
        this.guardarInteresado = interesado.isGuardarInteresado();
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

    @Column(name = "NOMBRE", length = 30)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "APELLIDO1", length = 30)
    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    @Column(name = "APELLIDO2", length = 30)
    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    @Column(name = "DIRECCION", length = 160)
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Column(name = "CP", length = 5)
    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    @Column(name = "EMAIL", length = 160)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "TELEFONO", length = 20)
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Column(name = "DIRELECTRONICA", length = 160)
    public String getDireccionElectronica() {
        return direccionElectronica;
    }

    public void setDireccionElectronica(String direccionElectronica) {
        this.direccionElectronica = direccionElectronica;
    }

    @Column(name = "RAZONSOCIAL", length = 80)
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }


    @Column(name = "TIPODOCIDENT")
    public Long getTipoDocumentoIdentificacion() {
        return tipoDocumentoIdentificacion;
    }

    public void setTipoDocumentoIdentificacion(Long tipoDocumentoIdentificacion) {
        this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion;
    }

    @Column(name = "DOCUMENTO", length = 17)
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }


    @Column(name = "TIPOPERSONA", nullable = false)
    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }


    @ManyToOne(optional = true)
    @JoinColumn(name = "PAIS", foreignKey = @ForeignKey(name = "RWE_PERSONA_PAIS_FK"))
    public CatPais getPais() {
        return pais;
    }

    public void setPais(CatPais pais) {
        this.pais = pais;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "PROVINCIA", foreignKey = @ForeignKey(name = "RWE_PERSONA_PROVINCIA_FK"))
    public CatProvincia getProvincia() {
        return provincia;
    }

    public void setProvincia(CatProvincia provincia) {
        this.provincia = provincia;
    }

    @ManyToOne()
    @JoinColumn(name = "LOCALIDAD", foreignKey = @ForeignKey(name = "RWE_PERSONA_LOCALIDAD_FK"))
    public CatLocalidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(CatLocalidad localidad) {
        this.localidad = localidad;
    }

    @Column(name = "CANALNOTIF")
    public Long getCanal() {
        return canal;
    }

    public void setCanal(Long canal) {
        this.canal = canal;
    }


    @Column(name = "OBSERVACIONES", length = 160)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "ENTIDAD", foreignKey = @ForeignKey(name = "RWE_PERSONA_ENTIDAD_FK"))
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }


    @Transient
    public boolean isGuardarInteresado() {
        return guardarInteresado;
    }

    public void setGuardarInteresado(boolean guardarInteresado) {
        this.guardarInteresado = guardarInteresado;
    }


    @Transient
    public String getNombrePersonaFisica() {

        String personaFisica = "";

        if (StringUtils.isNotEmpty(getNombre())) {

            personaFisica = getNombre() + " " + getApellido1();

            if (getApellido2() != null && getApellido2().length() > 0) {
                personaFisica = personaFisica.concat(" " + getApellido2());
            }

            if (getDocumento() != null && getDocumento().length() > 0) {
                personaFisica = personaFisica.concat(" - " + getDocumento());
            }
        }

        return personaFisica;
    }

    @Transient
    public String getNombrePersonaFisicaCorto() {

        String personaFisica = "";

        if (StringUtils.isNotEmpty(getNombre())) {
            personaFisica = getNombre() + " " + getApellido1();

            if (getApellido2() != null && getApellido2().length() > 0) {
                personaFisica = personaFisica.concat(" " + getApellido2());
            }
        }

        return personaFisica;
    }

    @Transient
    public String getNombrePersonaJuridica() {

        String personaJuridica = "";

        if (StringUtils.isNotEmpty(getRazonSocial())) {

            personaJuridica = getRazonSocial();

            if (getDocumento() != null && getDocumento().length() > 0) {
                personaJuridica = personaJuridica.concat(" - " + getDocumento());
            }
        }

        return personaJuridica;
    }

    @Override
    public String toString() {
        if (id != null) {
            return id.toString();
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Persona usuario = (Persona) o;

        if (id != null ? !id.equals(usuario.id) : usuario.id != null) return false;

        return true;
    }

}
