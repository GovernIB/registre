package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (index)
 * Date: 6/02/14
 */
@Entity
@Table(name = "RWE_INTERESADO", indexes = {
        @Index(name = "RWE_INTERES_CATPAI_FK_I", columnList = "PAIS"),
        @Index(name = "RWE_INTERES_CATLOC_FK_I", columnList = "LOCALIDAD"),
        @Index(name = "RWE_INTERES_REPADO_FK_I", columnList = "REPRESENTADO"),
        @Index(name = "RWE_INTERES_REPANT_FK_I", columnList = "REPRESENTANTE")
})
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "interesado")
@XmlAccessorType(XmlAccessType.FIELD)
public class Interesado implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private Long tipo;
    @XmlElement
    private String nombre;
    @XmlElement
    private String apellido1;
    @XmlElement
    private String apellido2;
    @XmlElement
    private String razonSocial;
    @XmlElement
    private String codigoDir3;
    @XmlElement
    private Long tipoDocumentoIdentificacion;
    @XmlElement
    private String documento;
    @XmlElement
    private CatPais pais;
    @XmlElement
    private CatProvincia provincia;
    @XmlElement
    private CatLocalidad localidad;
    @XmlElement
    private String direccion;
    @XmlElement
    private String cp;
    @XmlElement
    private String email;
    @XmlElement
    private String telefono;
    @XmlElement
    private String direccionElectronica;
    @XmlElement
    private Long canal;

    @XmlTransient
    private Interesado representado;
    @XmlTransient
    private Interesado representante;
    @XmlElement
    private Boolean isRepresentante = false;

    @XmlElement
    private String observaciones;
    @XmlTransient
    private RegistroDetalle registroDetalle;
    @XmlTransient
    private boolean guardarInteresado;
    @XmlTransient
    private Long entidad;

    //Metadatos nueva arquitectura SIR
    @XmlTransient
    private String codDirectoriosUnificados;

    //SICRES4
    private Boolean receptorNotificaciones = false;
    private String telefonoMovil;
    private Boolean avisoNotificacionSMS = false;
    private Boolean avisoCorreoElectronico = false;


    public Interesado() {
    }

    public Interesado(Long id) {
        this.id = id;
    }

    public Interesado(String id) {

        if (id != null) {
            this.id = Long.valueOf(id);
        } else {
            this.id = null;
        }
    }

    /**
     * Constructor para Informe LibroRegistro
     */
    public Interesado(Long id, String nombre, String apellido1, String apellido2, Boolean isRepresentante, String razonSocial,
                      String documento, Long tipo, String email) {

        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.isRepresentante = isRepresentante;
        this.documento = documento;
        this.tipo = tipo;
        this.razonSocial = razonSocial;
        this.email = email;

    }


    /**
     * Constructor para un Interesado de Tipo Persona
     *
     * @param persona
     */
    public Interesado(Persona persona) {

        if (persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_FISICA)) {
            this.tipo = RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA;

        } else if (persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_JURIDICA)) {
            this.tipo = RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA;
        }

      /*  if(persona.getRepresentado() != null){
            this.representado = new Interesado(persona.getRepresentado().getId());
        }
        if(persona.getRepresentante() != null){
            this.representante = new Interesado(persona.getRepresentante());
        }*/

        this.id = persona.getId();
        //   this.isRepresentante = persona.getIsRepresentante();
        this.razonSocial = persona.getRazonSocial();
        this.nombre = persona.getNombre();
        this.apellido1 = persona.getApellido1();
        this.apellido2 = persona.getApellido2();
        this.tipoDocumentoIdentificacion = persona.getTipoDocumentoIdentificacion();
        this.documento = persona.getDocumento();
        this.pais = persona.getPais();
        this.provincia = persona.getProvincia();
        this.localidad = persona.getLocalidad();
        this.direccion = persona.getDireccion();
        this.cp = persona.getCp();
        this.email = persona.getEmail();
        this.telefono = persona.getTelefono();
        this.direccionElectronica = persona.getDireccionElectronica();
        this.canal = persona.getCanal();
        this.observaciones = persona.getObservaciones();
    }

    /**
     * Constructor para un Interesado de Tipo Administración
     *
     * @param codigoDir3
     * @param organismo
     */
    public Interesado(String codigoDir3, String organismo) {
        this.tipo = RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION;
        this.razonSocial = organismo;
        this.tipoDocumentoIdentificacion = RegwebConstantes.TIPODOCUMENTOID_CODIGO_ORIGEN_ID;
        this.documento = codigoDir3;
        this.codigoDir3 = codigoDir3;
    }


    /**
     * @param i
     */
    public Interesado(Interesado i) {
        super();
        this.id = i.id;
        this.tipo = i.tipo;
        this.nombre = i.nombre;
        this.apellido1 = i.apellido1;
        this.apellido2 = i.apellido2;
        this.razonSocial = i.razonSocial;
        this.codigoDir3 = i.codigoDir3;
        this.tipoDocumentoIdentificacion = i.tipoDocumentoIdentificacion;
        this.documento = i.documento;
        this.pais = i.pais == null ? null : new CatPais(i.pais);
        this.provincia = i.provincia == null ? null : new CatProvincia(i.provincia);
        this.localidad = i.localidad == null ? null : new CatLocalidad(i.localidad);
        this.direccion = i.direccion;
        this.cp = i.cp;
        this.email = i.email;
        this.telefono = i.telefono;
        this.direccionElectronica = i.direccionElectronica;
        this.canal = i.canal;
        // Comentada la linea porque provocaba una referencia cíclica
        // this.representado = i.representado == null? null : new Interesado(i.representado);
        this.representante = i.representante == null ? null : new Interesado(i.representante);
        this.isRepresentante = i.isRepresentante;
        this.observaciones = i.observaciones;
        //this.registroDetalle = i.registroDetalle;
        this.guardarInteresado = i.guardarInteresado;
        this.entidad = i.entidad;
        this.codDirectoriosUnificados = i.codDirectoriosUnificados;
    }


    public static List<Interesado> clone(List<Interesado> list) {
        if (list == null) {
            return null;
        }

        List<Interesado> clone = new ArrayList<Interesado>(list.size());
        for (Interesado interesado : list) {
            clone.add(interesado == null ? null : new Interesado(interesado));
        }

        return clone;
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

    @Column(name = "NOMBRE")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "APELLIDO1")
    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    @Column(name = "APELLIDO2")
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

    @Column(name = "TIPOINTERESADO")
    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    @Column(name = "RAZONSOCIAL", length = 2000) // Modificado a 2000 para permitir las Administraciones
    public String getRazonSocial() {
        return razonSocial;
    }

    @Column(name = "CODIGODIR3")
    public String getCodigoDir3() {
        return codigoDir3;
    }

    public void setCodigoDir3(String codigoDir3) {
        this.codigoDir3 = codigoDir3;
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

    //SICRES4  aumento de tamaño
    @Column(name = "DOCUMENTO", length = 256)
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @ManyToOne()
    @JoinColumn(name = "PAIS", foreignKey = @ForeignKey(name = "RWE_INTERESADO_PAIS_FK"))
    public CatPais getPais() {
        return pais;
    }

    public void setPais(CatPais pais) {
        this.pais = pais;
    }

    @ManyToOne()
    @JoinColumn(name = "PROVINCIA", foreignKey = @ForeignKey(name = "RWE_INTERESADO_PROVINCIA_FK"))
    public CatProvincia getProvincia() {
        return provincia;
    }

    public void setProvincia(CatProvincia provincia) {
        this.provincia = provincia;
    }

    @ManyToOne()
    @JoinColumn(name = "LOCALIDAD", foreignKey = @ForeignKey(name = "RWE_INTERESADO_LOCALIDAD_FK"))
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

    @ManyToOne()
    @JoinColumn(name = "REPRESENTADO", foreignKey = @ForeignKey(name = "RWE_INTERESADO_REPRESENT_FK"))
   // @JsonIgnore
    public Interesado getRepresentado() {
        return representado;
    }

    public void setRepresentado(Interesado representado) {
        this.representado = representado;
    }

    @ManyToOne()
    @JoinColumn(name = "REPRESENTANTE", foreignKey = @ForeignKey(name = "RWE_INTERESADO_REPREANTE_FK"))
    @JsonIgnore
    public Interesado getRepresentante() {
        return representante;
    }

    public void setRepresentante(Interesado representante) {
        this.representante = representante;
    }

    @Column(name = "ISREPRESENTANTE")
    public Boolean getIsRepresentante() {
        return isRepresentante;
    }

    public void setIsRepresentante(Boolean isRepresentante) {
        this.isRepresentante = isRepresentante;
    }

    @ManyToOne()
    @JoinColumn(name = "REGISTRODETALLE", foreignKey = @ForeignKey(name = "RWE_INTERESADO_REGDET_FK"))
    @JsonIgnore
    public RegistroDetalle getRegistroDetalle() {
        return registroDetalle;
    }

    public void setRegistroDetalle(RegistroDetalle registroDetalle) {
        this.registroDetalle = registroDetalle;
    }


    @Column(name = "CODDIRUNIF", length = 15)
    public String getCodDirectoriosUnificados() {
        return codDirectoriosUnificados;
    }

    public void setCodDirectoriosUnificados(String codDirectoriosUnificados) {
        this.codDirectoriosUnificados = codDirectoriosUnificados;
    }


    //SICRES4
    //Indica quien es el interesado que recibirá las notificaciones en caso de muchos interesados.
    @Column(name = "RECEPNOTIF")
    public Boolean getReceptorNotificaciones() {
        return receptorNotificaciones;
    }

    public void setReceptorNotificaciones(Boolean receptorNotificaciones) {
        this.receptorNotificaciones = receptorNotificaciones;
    }

    @Column(name = "TLFMOVIL", length = 20)
    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    @Column(name = "AVISONOTIFSMS")
    public Boolean getAvisoNotificacionSMS() {
        return avisoNotificacionSMS;
    }

    public void setAvisoNotificacionSMS(Boolean avisoNotificacionSMS) {
        this.avisoNotificacionSMS = avisoNotificacionSMS;
    }

    @Column(name = "AVISONOTIFEMAIL")
    public Boolean getAvisoCorreoElectronico() {
        return avisoCorreoElectronico;
    }

    public void setAvisoCorreoElectronico(Boolean avisoCorreoElectronico) {
        this.avisoCorreoElectronico = avisoCorreoElectronico;
    }

    @Transient
    public String getInformacionHtml() {
        String info = "";

        if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {

            info = (getRazonSocial() != null) ? getRazonSocial() : getNombre();
            info = info + " <br/>";
            if (StringUtils.isNotEmpty(getCodigoDir3())) {
                info = info + "DIR3: " + getCodigoDir3() + " <br/>";
            }

        } else if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {

            info = getNombrePersonaFisica() + " <br/>";
            if (StringUtils.isNotEmpty(getDireccion())) {
                info = info + getDireccion() + " <br/>";
            }
            if (getProvincia() != null) {
                info = info + getProvincia().getDescripcionProvincia() + " <br/>";
            }
            if (getLocalidad() != null) {
                info = info + getLocalidad().getNombre() + " <br/>";
            }
            if (StringUtils.isNotEmpty(getCp())) {
                info = info + "Cp: " + getCp() + " <br/>";
            }
            if (StringUtils.isNotEmpty(getTelefono())) {
                info = info + "Tlf: " + getTelefono() + " <br/>";
            }
            if (StringUtils.isNotEmpty(getEmail())) {
                info = info + "Email: " + getEmail() + " <br/>";
            }
            if (StringUtils.isNotEmpty(getDireccionElectronica())) {
                info = info + "Dir. elect: " + getEmail() + " <br/>";
            }

        } else if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
            info = getNombrePersonaJuridica() + " <br/>";
            if (StringUtils.isNotEmpty(getDireccion())) {
                info = info + getDireccion() + " <br/>";
            }
            if (getProvincia() != null) {
                info = info + getProvincia().getDescripcionProvincia() + " <br/>";
            }
            if (getLocalidad() != null) {
                info = info + getLocalidad().getNombre() + " <br/>";
            }
            if (StringUtils.isNotEmpty(getCp())) {
                info = info + "Cp: " + getCp() + " <br/>";
            }
            if (StringUtils.isNotEmpty(getTelefono())) {
                info = info + "Tlf: " + getTelefono() + " <br/>";
            }
            if (StringUtils.isNotEmpty(getEmail())) {
                info = info + "Email: " + getEmail() + " <br/>";
            }
            if (StringUtils.isNotEmpty(getDireccionElectronica())) {
                info = info + "Dir. elect: " + getEmail() + " <br/>";
            }

        }

        return info;
    }

    @Transient
    public String getNombreCompleto() {

        if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
            return getNombreOrganismo();
        } else if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
            return getNombrePersonaFisicaCorto();
        } else if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
            return getNombrePersonaJuridica();
        }

        return "";

    }

    @Transient
    public String getNombreCompletoInforme() {

        if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
            return getNombreOrganismo();
        } else if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
            return getNombrePersonaFisica();
        } else if (tipo.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
            return getNombrePersonaJuridica();
        }

        return "";

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

    @Transient
    public String getNombreOrganismo() {
        String nombre = (getRazonSocial() != null) ? getRazonSocial() : getNombre();

        if (getCodigoDir3() != null) {
            nombre += " - " + getCodigoDir3();
        }

        return nombre;

    }

    @Transient
    public String getDocumentoNTI() {

        if (StringUtils.isNotEmpty(getDocumento())) {
            return getDocumento();
        } else {
            return getCodigoDir3();
        }
    }

    public void setGuardarInteresado(boolean guardarInteresado) {
        this.guardarInteresado = guardarInteresado;
    }

    @Transient
    public Long getEntidad() {
        return entidad;
    }

    public void setEntidad(Long entidad) {
        this.entidad = entidad;
    }

    @Transient
    public boolean isGuardarInteresado() {
        return guardarInteresado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interesado that = (Interesado) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

}
