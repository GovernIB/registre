package es.caib.regweb3.ws.model;

import es.caib.regweb3.ws.utils.CharAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 * 
 * @author anadal
 * 
 */
@XmlRootElement
public class PersonaWs implements Serializable {

  private Long id;
 // private Long tipoInteresadoID;
  private String nombre;
  private String apellido1;
  private String apellido2;
  private String razonSocial;

  private Character tipoDocumentoIdentificacionNTI;
  private String documento;
  private Long paisDir3ID;
  private Long provinciaDir3ID;
  private Long localidadDir3ID;
  private String codigoEntidadGeograficaDir3ID;
  private String direccion;
  private String cp;
  private String email;
  private String telefono;
  private String direccionElectronica;
  private Long canal;

 /* private PersonaWs representado;
  private PersonaWs representante;
  private Boolean isRepresentante = false; */

  private String observaciones;
  
  
  private String entidadDir3ID;

  private boolean guardarInteresado;

  private Long tipoPersonaID;

  public PersonaWs() {
  }

  public PersonaWs(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido1() {
    return apellido1;
  }

  public void setApellido1(String apellido1) {
    this.apellido1 = apellido1;
  }

  public String getApellido2() {
    return apellido2;
  }

  public void setApellido2(String apellido2) {
    this.apellido2 = apellido2;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public String getDocumento() {
    return documento;
  }

  public void setDocumento(String documento) {
    this.documento = documento;
  }

  public Long getPaisDir3ID() {
    return paisDir3ID;
  }

  public void setPaisDir3ID(Long paisDir3ID) {
    this.paisDir3ID = paisDir3ID;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getCp() {
    return cp;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getDireccionElectronica() {
    return direccionElectronica;
  }

  public void setDireccionElectronica(String direccionElectronica) {
    this.direccionElectronica = direccionElectronica;
  }

  /*public PersonaWs getRepresentado() {
    return representado;
  }

  public void setRepresentado(PersonaWs representado) {
    this.representado = representado;
  }

  public PersonaWs getRepresentante() {
    return representante;
  }

  public void setRepresentante(PersonaWs representante) {
    this.representante = representante;
  }

  public Boolean getIsRepresentante() {
    return isRepresentante;
  }

  public void setIsRepresentante(Boolean isRepresentante) {
    this.isRepresentante = isRepresentante;
  } */

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Long getCanal() {
    return canal;
  }

  public void setCanal(Long canal) {
    this.canal = canal;
  }

  @XmlJavaTypeAdapter(CharAdapter.class)
  public Character getTipoDocumentoIdentificacionNTI() {
    return tipoDocumentoIdentificacionNTI;
  }

  public void setTipoDocumentoIdentificacionNTI(Character tipoDocumentoIdentificacionNTI) {
    this.tipoDocumentoIdentificacionNTI = tipoDocumentoIdentificacionNTI;
  }
/*
  public Long getTipoInteresadoID() {
    return tipoInteresadoID;
  }

  public void setTipoInteresadoID(Long tipoInteresadoID) {
    this.tipoInteresadoID = tipoInteresadoID;
  }*/

  public Long getProvinciaDir3ID() {
    return provinciaDir3ID;
  }

  public void setProvinciaDir3ID(Long provinciaDir3ID) {
    this.provinciaDir3ID = provinciaDir3ID;
  }

  public Long getLocalidadDir3ID() {
    return localidadDir3ID;
  }

  public void setLocalidadDir3ID(Long localidadDir3ID) {
    this.localidadDir3ID = localidadDir3ID;
  }



  @XmlElement(required=true)
  public String getEntidadDir3ID() {
    return entidadDir3ID;
  }

  public void setEntidadDir3ID(String entidadDir3ID) {
    this.entidadDir3ID = entidadDir3ID;
  }

  public boolean isGuardarInteresado() {
    return guardarInteresado;
  }

  public void setGuardarInteresado(boolean guardarInteresado) {
    this.guardarInteresado = guardarInteresado;
  }

  public Long getTipoPersonaID() {
    return tipoPersonaID;
  }

  public void setTipoPersonaID(Long tipoPersonaID) {
    this.tipoPersonaID = tipoPersonaID;
  }

  public String getCodigoEntidadGeograficaDir3ID() {
    return codigoEntidadGeograficaDir3ID;
  }

  public void setCodigoEntidadGeograficaDir3ID(String codigoEntidadGeograficaDir3ID) {
    this.codigoEntidadGeograficaDir3ID = codigoEntidadGeograficaDir3ID;
  }



  /*
   * public InteresadoWs(Persona persona) {
   * 
   * if(persona.getTipoPersona().getId().equals(RegwebConstantes.PERSONA_FISICA))
   * { this.tipoInteresado = new
   * TipoInteresado(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);
   * 
   * }else
   * if(persona.getTipoPersona().getId().equals(RegwebConstantes.PERSONA_JURIDICA
   * )){ this.tipoInteresado = new
   * TipoInteresado(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA); }
   * 
   * 
   * 
   * }
   * 
   * 
   * public InteresadoWs(String organismo, String codigoDir3){
   * this.tipoInteresado = new
   * TipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
   * this.nombre = organismo; this.codigoDir3 = codigoDir3; }
   */

}
