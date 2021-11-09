package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by earrivi on 26/11/2015.
 */
@Entity
@Table(name = "RWE_INTERESADO_SIR")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class InteresadoSir implements Serializable {

    private Long id;
//    @JsonIgnore
    private RegistroSir registroSir;
    private String tipoDocumentoIdentificacionInteresado;
    private String documentoIdentificacionInteresado;
    private String razonSocialInteresado;
    private String nombreInteresado;
    private String primerApellidoInteresado;
    private String segundoApellidoInteresado;
    private String codigoPaisInteresado;
    private String codigoProvinciaInteresado;
    private String codigoMunicipioInteresado;
    private String direccionInteresado;
    private String codigoPostalInteresado;
    private String correoElectronicoInteresado;
    private String telefonoInteresado;
    private String direccionElectronicaHabilitadaInteresado;
    private String canalPreferenteComunicacionInteresado;
    private String tipoDocumentoIdentificacionRepresentante;
    private String documentoIdentificacionRepresentante;
    private String razonSocialRepresentante;
    private String nombreRepresentante;
    private String primerApellidoRepresentante;
    private String segundoApellidoRepresentante;
    private String codigoPaisRepresentante;
    private String codigoProvinciaRepresentante;
    private String codigoMunicipioRepresentante;
    private String direccionRepresentante;
    private String codigoPostalRepresentante;
    private String correoElectronicoRepresentante;
    private String telefonoRepresentante;
    private String direccionElectronicaHabilitadaRepresentante;
    private String canalPreferenteComunicacionRepresentante;
    private String observaciones;


    public InteresadoSir() {
    }


    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne()
    @JoinColumn(name = "REGISTRO_SIR")
    @ForeignKey(name = "RWE_INTERESADOSIR_REGSIR_FK")
    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
    }

    @Column(name = "T_DOCUMENTO_INTERESADO", length = 1, nullable = true)
    public String getTipoDocumentoIdentificacionInteresado() {
        return tipoDocumentoIdentificacionInteresado;
    }

    public void setTipoDocumentoIdentificacionInteresado(String tipoDocumentoIdentificacionInteresado) {
        this.tipoDocumentoIdentificacionInteresado = tipoDocumentoIdentificacionInteresado;
    }

    @Column(name = "DOCUMENTO_INTERESADO", length = 17, nullable = true)
    public String getDocumentoIdentificacionInteresado() {
        return documentoIdentificacionInteresado;
    }

    public void setDocumentoIdentificacionInteresado(String documentoIdentificacionInteresado) {
        this.documentoIdentificacionInteresado = documentoIdentificacionInteresado;
    }

    @Column(name = "RAZON_SOCIAL_INTERESADO", length = 80, nullable = true)
    public String getRazonSocialInteresado() {
        return razonSocialInteresado;
    }

    public void setRazonSocialInteresado(String razonSocialInteresado) {
        this.razonSocialInteresado = razonSocialInteresado;
    }

    @Column(name = "NOMBRE_INTERESADO", length = 30, nullable = true)
    public String getNombreInteresado() {
        return nombreInteresado;
    }

    public void setNombreInteresado(String nombreInteresado) {
        this.nombreInteresado = nombreInteresado;
    }

    @Column(name = "APELLIDO1_INTERESADO", length = 30, nullable = true)
    public String getPrimerApellidoInteresado() {
        return primerApellidoInteresado;
    }

    public void setPrimerApellidoInteresado(String primerApellidoInteresado) {
        this.primerApellidoInteresado = primerApellidoInteresado;
    }

    @Column(name = "APELLIDO2_INTERESADO", length = 30, nullable = true)
    public String getSegundoApellidoInteresado() {
        return segundoApellidoInteresado;
    }

    public void setSegundoApellidoInteresado(String segundoApellidoInteresado) {
        this.segundoApellidoInteresado = segundoApellidoInteresado;
    }

    @Column(name = "COD_PAIS_INTERESADO", length = 4, nullable = true)
    public String getCodigoPaisInteresado() {
        return codigoPaisInteresado;
    }

    public void setCodigoPaisInteresado(String codigoPaisInteresado) {
        this.codigoPaisInteresado = codigoPaisInteresado;
    }

    @Column(name = "COD_PROVINCIA_INTERESADO", length = 2, nullable = true)
    public String getCodigoProvinciaInteresado() {
        return codigoProvinciaInteresado;
    }

    public void setCodigoProvinciaInteresado(String codigoProvinciaInteresado) {
        this.codigoProvinciaInteresado = codigoProvinciaInteresado;
    }

    @Column(name = "COD_MUNICIPIO_INTERESADO", length = 5, nullable = true)
    public String getCodigoMunicipioInteresado() {
        return codigoMunicipioInteresado;
    }

    public void setCodigoMunicipioInteresado(String codigoMunicipioInteresado) {
        this.codigoMunicipioInteresado = codigoMunicipioInteresado;
    }

    @Column(name = "DIRECCION_INTERESADO", length = 160, nullable = true)
    public String getDireccionInteresado() {
        return direccionInteresado;
    }

    public void setDireccionInteresado(String direccionInteresado) {
        this.direccionInteresado = direccionInteresado;
    }

    @Column(name = "CP_INTERESADO", length = 5, nullable = true)
    public String getCodigoPostalInteresado() {
        return codigoPostalInteresado;
    }

    public void setCodigoPostalInteresado(String codigoPostalInteresado) {
        this.codigoPostalInteresado = codigoPostalInteresado;
    }

    @Column(name = "EMAIL_INTERESADO", length = 160, nullable = true)
    public String getCorreoElectronicoInteresado() {
        return correoElectronicoInteresado;
    }

    public void setCorreoElectronicoInteresado(String correoElectronicoInteresado) {
        this.correoElectronicoInteresado = correoElectronicoInteresado;
    }

    @Column(name = "TELEFONO_INTERESADO", length = 20, nullable = true)
    public String getTelefonoInteresado() {
        return telefonoInteresado;
    }

    public void setTelefonoInteresado(String telefonoInteresado) {
        this.telefonoInteresado = telefonoInteresado;
    }

    @Column(name = "DIR_ELECTRONICA_INTERESADO", length = 160, nullable = true)
    public String getDireccionElectronicaHabilitadaInteresado() {
        return direccionElectronicaHabilitadaInteresado;
    }

    public void setDireccionElectronicaHabilitadaInteresado(String direccionElectronicaHabilitadaInteresado) {
        this.direccionElectronicaHabilitadaInteresado = direccionElectronicaHabilitadaInteresado;
    }

    @Column(name = "CANAL_NOTIF_INTERESADO", length = 2, nullable = true)
    public String getCanalPreferenteComunicacionInteresado() {
        return canalPreferenteComunicacionInteresado;
    }

    public void setCanalPreferenteComunicacionInteresado(String canalPreferenteComunicacionInteresado) {
        this.canalPreferenteComunicacionInteresado = canalPreferenteComunicacionInteresado;
    }

    @Column(name = "T_DOCUMENTO_REPRESENTANTE", length = 1, nullable = true)
    public String getTipoDocumentoIdentificacionRepresentante() {
        return tipoDocumentoIdentificacionRepresentante;
    }

    public void setTipoDocumentoIdentificacionRepresentante(String tipoDocumentoIdentificacionRepresentante) {
        this.tipoDocumentoIdentificacionRepresentante = tipoDocumentoIdentificacionRepresentante;
    }

    @Column(name = "DOCUMENTO_REPRESENTANTE", length = 17, nullable = true)
    public String getDocumentoIdentificacionRepresentante() {
        return documentoIdentificacionRepresentante;
    }

    public void setDocumentoIdentificacionRepresentante(String documentoIdentificacionRepresentante) {
        this.documentoIdentificacionRepresentante = documentoIdentificacionRepresentante;
    }

    @Column(name = "RAZON_SOCIAL_REPRESENTANTE", length = 80, nullable = true)
    public String getRazonSocialRepresentante() {
        return razonSocialRepresentante;
    }

    public void setRazonSocialRepresentante(String razonSocialRepresentante) {
        this.razonSocialRepresentante = razonSocialRepresentante;
    }

    @Column(name = "NOMBRE_REPRESENTANTE", length = 30, nullable = true)
    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    @Column(name = "APELLIDO1_REPRESENTANTE", length = 30, nullable = true)
    public String getPrimerApellidoRepresentante() {
        return primerApellidoRepresentante;
    }

    public void setPrimerApellidoRepresentante(String primerApellidoRepresentante) {
        this.primerApellidoRepresentante = primerApellidoRepresentante;
    }

    @Column(name = "APELLIDO2_REPRESENTANTE", length = 30, nullable = true)
    public String getSegundoApellidoRepresentante() {
        return segundoApellidoRepresentante;
    }

    public void setSegundoApellidoRepresentante(String segundoApellidoRepresentante) {
        this.segundoApellidoRepresentante = segundoApellidoRepresentante;
    }

    @Column(name = "COD_PAIS_REPRESENTANTE", length = 4, nullable = true)
    public String getCodigoPaisRepresentante() {
        return codigoPaisRepresentante;
    }

    public void setCodigoPaisRepresentante(String codigoPaisRepresentante) {
        this.codigoPaisRepresentante = codigoPaisRepresentante;
    }

    @Column(name = "COD_PROVINCIA_REPRESENTANTE", length = 2, nullable = true)
    public String getCodigoProvinciaRepresentante() {
        return codigoProvinciaRepresentante;
    }

    public void setCodigoProvinciaRepresentante(String codigoProvinciaRepresentante) {
        this.codigoProvinciaRepresentante = codigoProvinciaRepresentante;
    }

    @Column(name = "COD_MUNICIPIO_REPRESENTANTE", length = 5, nullable = true)
    public String getCodigoMunicipioRepresentante() {
        return codigoMunicipioRepresentante;
    }

    public void setCodigoMunicipioRepresentante(String codigoMunicipioRepresentante) {
        this.codigoMunicipioRepresentante = codigoMunicipioRepresentante;
    }

    @Column(name = "DIRECCION_REPRESENTANTE", length = 160, nullable = true)
    public String getDireccionRepresentante() {
        return direccionRepresentante;
    }

    public void setDireccionRepresentante(String direccionRepresentante) {
        this.direccionRepresentante = direccionRepresentante;
    }

    @Column(name = "CP_REPRESENTANTE", length = 5, nullable = true)
    public String getCodigoPostalRepresentante() {
        return codigoPostalRepresentante;
    }

    public void setCodigoPostalRepresentante(String codigoPostalRepresentante) {
        this.codigoPostalRepresentante = codigoPostalRepresentante;
    }

    @Column(name = "EMAIL_REPRESENTANTE", length = 160, nullable = true)
    public String getCorreoElectronicoRepresentante() {
        return correoElectronicoRepresentante;
    }

    public void setCorreoElectronicoRepresentante(String correoElectronicoRepresentante) {
        this.correoElectronicoRepresentante = correoElectronicoRepresentante;
    }

    @Column(name = "TELEFONO_REPRESENTANTE", length = 20, nullable = true)
    public String getTelefonoRepresentante() {
        return telefonoRepresentante;
    }

    public void setTelefonoRepresentante(String telefonoRepresentante) {
        this.telefonoRepresentante = telefonoRepresentante;
    }

    @Column(name = "DIR_ELECTRONICA_REPRESENTANTE", length = 160, nullable = true)
    public String getDireccionElectronicaHabilitadaRepresentante() {
        return direccionElectronicaHabilitadaRepresentante;
    }

    public void setDireccionElectronicaHabilitadaRepresentante(String direccionElectronicaHabilitadaRepresentante) {
        this.direccionElectronicaHabilitadaRepresentante = direccionElectronicaHabilitadaRepresentante;
    }

    @Column(name = "CANAL_NOTIF_REPRESENTANTE", length = 2, nullable = true)
    public String getCanalPreferenteComunicacionRepresentante() {
        return canalPreferenteComunicacionRepresentante;
    }

    public void setCanalPreferenteComunicacionRepresentante(String canalPreferenteComunicacionRepresentante) {
        this.canalPreferenteComunicacionRepresentante = canalPreferenteComunicacionRepresentante;
    }

    @Column(name = "OBSERVACIONES", length = 160, nullable = true)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Transient
    public Long getTipoInteresado(){

        if(StringUtils.isNotEmpty(getNombreInteresado()) && StringUtils.isNotEmpty(getPrimerApellidoInteresado())){
            return RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA;

        }else if(StringUtils.isNotEmpty(getRazonSocialInteresado())){
            return RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA;

        }else if(StringUtils.isNotEmpty(getTipoDocumentoIdentificacionInteresado())){ // Caso en que es una Administración, pero no han puesto la denominación en Razon social
            if(getTipoDocumentoIdentificacionInteresado().equals(String.valueOf(RegwebConstantes.TIPODOCUMENTOID_CODIGO_ORIGEN))){
                return RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA;
            }
        }

        return null;
    }

    @Transient
    public Long getTipoRepresentante(){

        if(StringUtils.isNotEmpty(getNombreRepresentante()) && StringUtils.isNotEmpty(getPrimerApellidoRepresentante())){
            return RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA;

        }else if(StringUtils.isNotEmpty(getRazonSocialRepresentante())){
            return RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA;

        }else if(StringUtils.isNotEmpty(getTipoDocumentoIdentificacionRepresentante())){ // Caso en que es una Administración, pero no han puesto la denominación en Razon social
            if(getTipoDocumentoIdentificacionRepresentante().equals(String.valueOf(RegwebConstantes.TIPODOCUMENTOID_CODIGO_ORIGEN))){
                return RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA;
            }
        }

        return null;
    }


    @Transient
    public Boolean getRepresentante(){

        return StringUtils.isNotEmpty(getNombreRepresentante()) || StringUtils.isNotEmpty(getRazonSocialRepresentante());
    }

    @Transient
    public String getNombreCompleto(){

        if(getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
            return getNombrePersonaFisica();
        }else if(getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
            return getNombrePersonaJuridica();
        }

        return "";
    }

    @Transient
    public String getNombreCompletoRepresentante(){

        if(getRepresentante()){
            if(getTipoRepresentante().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
                return getNombrePersonaFisicaRepresentante();
            }else if(getTipoRepresentante().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
                return getNombrePersonaJuridicaRepresentante();
            }
        }

        return "";
    }

    @Transient
    public String getNombrePersonaFisica(){

        String personaFisica = "" ;

        if(StringUtils.isNotEmpty(getNombreInteresado())){

            personaFisica = getNombreInteresado()+ " " + getPrimerApellidoInteresado();

            if(StringUtils.isNotEmpty(getSegundoApellidoInteresado())){
                personaFisica = personaFisica.concat(" " + getSegundoApellidoInteresado());
            }

            if(StringUtils.isNotEmpty(getDocumentoIdentificacionInteresado())){
                personaFisica = personaFisica.concat(" - " + getDocumentoIdentificacionInteresado());
            }
        }

        return personaFisica;
    }

    @Transient
    public String getNombrePersonaJuridica(){

        String personaJuridica = "";

        if(StringUtils.isNotEmpty(getRazonSocialInteresado())){

            personaJuridica = getRazonSocialInteresado();

            if(StringUtils.isNotEmpty(getDocumentoIdentificacionInteresado())){
                personaJuridica = personaJuridica.concat(" - " + getDocumentoIdentificacionInteresado());
            }
        }else if(StringUtils.isNotEmpty(getDocumentoIdentificacionInteresado())){
            personaJuridica = getDocumentoIdentificacionInteresado();
        }

        return  personaJuridica;
    }

    @Transient
    public String getNombrePersonaFisicaRepresentante(){

        String personaFisica = "" ;

        if(StringUtils.isNotEmpty(getNombreRepresentante())){

            personaFisica = getNombreRepresentante()+ " " + getPrimerApellidoRepresentante();

            if(StringUtils.isNotEmpty(getSegundoApellidoRepresentante())){
                personaFisica = personaFisica.concat(" " + getSegundoApellidoRepresentante());
            }

            if(StringUtils.isNotEmpty(getDocumentoIdentificacionRepresentante())){
                personaFisica = personaFisica.concat(" - " + getDocumentoIdentificacionRepresentante());
            }
        }

        return personaFisica;
    }

    @Transient
    public String getNombrePersonaJuridicaRepresentante(){

        String personaJuridica = "";

        if(StringUtils.isNotEmpty(getRazonSocialRepresentante())){

            personaJuridica = getRazonSocialRepresentante();

            if(StringUtils.isNotEmpty(getDocumentoIdentificacionRepresentante())){
                personaJuridica = personaJuridica.concat(" - " + getDocumentoIdentificacionRepresentante());
            }
        }else if(StringUtils.isNotEmpty(getDocumentoIdentificacionRepresentante())){
            personaJuridica = getDocumentoIdentificacionRepresentante();
        }

        return  personaJuridica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InteresadoSir interesado = (InteresadoSir) o;

        if (!id.equals(interesado.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
