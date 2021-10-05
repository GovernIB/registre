package es.caib.regweb3.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author anadal
 *
 */
public interface IRegistro extends Serializable {

  Long getId();

  void setId(Long id);

  UsuarioEntidad getUsuario();

  void setUsuario(UsuarioEntidad usuario);

  Oficina getOficina();

  void setOficina(Oficina oficina);

  Date getFecha();

  void setFecha(Date fecha);

  Libro getLibro();

  void setLibro(Libro libro);

  String getNumeroRegistro();

  void setNumeroRegistro(String numeroRegistro);

  String getNumeroRegistroFormateado();

  void setNumeroRegistroFormateado(String numeroRegistroFormateado);

  Long getEstado();

  void setEstado(Long estado);

  RegistroDetalle getRegistroDetalle();

  void setRegistroDetalle(RegistroDetalle registroDetalle);

}
