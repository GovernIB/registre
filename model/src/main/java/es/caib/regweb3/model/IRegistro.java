package es.caib.regweb3.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author anadal
 *
 */
public interface IRegistro extends Serializable {

  public Long getId();

  public void setId(Long id);

  public UsuarioEntidad getUsuario();

  public void setUsuario(UsuarioEntidad usuario);

  public Oficina getOficina();

  public void setOficina(Oficina oficina);

  public Date getFecha();

  public void setFecha(Date fecha);

  public Libro getLibro();

  public void setLibro(Libro libro);

  public Integer getNumeroRegistro();

  public void setNumeroRegistro(Integer numeroRegistro);

  public String getNumeroRegistroFormateado();

  public void setNumeroRegistroFormateado(String numeroRegistroFormateado);

  public Long getEstado();

  public void setEstado(Long estado);

  public RegistroDetalle getRegistroDetalle();

  public void setRegistroDetalle(RegistroDetalle registroDetalle);

}
