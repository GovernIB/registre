package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.DestinatarioWrapper;
import es.caib.regweb3.persistence.utils.OficiosRemisionInternoOrganismo;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.distribucion.Destinatarios;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RegistroEntradaLocal extends RegistroEntradaCambiarEstadoLocal {


    /**
     * Obtiene los Registros de Entrada de un Usuario.
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> getByUsuario(Long idUsuarioEntidad) throws Exception;
    
    
    /**
     * Guarda un Registro de Entrada y le asocia un número de registro (sin anexos)
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada)
        throws Exception, I18NException, I18NValidationException;
    

    /**
     * Guarda un Registro de Entrada y le asocia un número de registro (con anexos)
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                            UsuarioEntidad usuarioEntidad, List<AnexoFull> anexosFull)
            throws Exception, I18NException, I18NValidationException;

   /**
     * Busca los Registros de Entrada en función de los parámetros
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroEntrada
     * @return
     * @throws Exception
     */
   public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, String interesadoNom, String interesadoDoc, String interesadoLli1, String interesadoLli2, String organoDest, Boolean anexos, String observaciones, String usuario) throws Exception;

    /**
     * Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<String> oficiosPendientesRemisionInterna(Long idLibro, Set<Long> organismos) throws Exception;

    /**
     * Busca los RegistroEntrada pendientes de tramitar mediante un OficioRemision INTERNI
     * y los agrupa según su Organismo destinatario.
     * @param any
     * @param libro
     * @return
     * @throws Exception
     */
    public List<OficiosRemisionInternoOrganismo> oficiosPendientesRemisionInterna(Integer any, Libro libro, Set<Long> organismos) throws Exception;

    /**
     * Cuenta los Oficios pendientes de Remisión Interna de un conjunto de Libros
     * @param libros
     * @return
     * @throws Exception
     */
    public Long oficiosPendientesRemisionInternaCount(List<Libro> libros, Set<Long> organismos) throws Exception;


    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision o no
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public Boolean isOficioRemisionInterno(Long idRegistro, Set<Long> organismos) throws Exception;

    /**
     * Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<String> oficiosPendientesRemisionExterna(Long idLibro) throws Exception;

    /**
     * Busca los RegistroEntrada pendientes de tramitar mediante un OficioRemision EXTERNO, es decir, cuyo Organismo destino
     * no pertenece a la Entidad Activa y los agrupa según su Organismo destinatario.
     * @param any
     * @param libro
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    public List<OficiosRemisionOrganismo> oficiosPendientesRemisionExterna(Integer any, Libro libro, Entidad entidadActiva) throws Exception;

    /**
     * Cuenta los Oficios pendientes de Remisión Externa de un conjunto de Libros
     * @param libros
     * @return
     * @throws Exception
     */
    public Long oficiosPendientesRemisionExternaCount(List<Libro> libros) throws Exception;

    /**
     * Busca Oficios de Remisión de un Organismo propio, según los parámetros.
     * @param idOrganismo
     * @param any
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> oficiosRemisionByOrganismoPropio(Long idOrganismo, Integer any, Long idLibro) throws Exception;

    /**
     * Busca Oficios de Remisión de un Organismo externo, según los parámetros.
     * @param codigoOrganismo
     * @param any
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> oficiosRemisionByOrganismoExterno(String codigoOrganismo, Integer any, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Entrada en función de varios parámetros
     * @param fechaInicio
     * @param fechaFin
     * @param numRegistro
     * @param interesadoNom
     * @param interesadoLli1
     * @param interesadoLli2
     * @param interesadoDoc
     * @param anexos
     * @param observaciones
     * @param usuario
     * @param extracto
     * @param libros
     * @param estado
     * @param idOficina
     * @param idTipoAsunto
     * @param organoDest
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaLibroRegistro(Date fechaInicio, Date fechaFin, String numRegistro, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String usuario, String extracto, List<Libro> libros, Long estado, Long idOficina, Long idTipoAsunto, String organoDest) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long buscaIndicadoresTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de una Oficina
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    public Long buscaIndicadoresOficinaTotal(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Conselleria
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Tipos Asunto, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param tipoAsunto
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idOficina
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Libros
     * @param fechaInicio
     * @param fechaFin
     * @param libro
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Oficinas
     * @param fechaInicio
     * @param fechaFin
     * @param oficina
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception;


    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    public List<RegistroBasico> getByOficinaEstado(Long idOficinaActiva, Long idEstado, Integer total) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> getByOficinaEstado(Long idOficinaActiva, Long idEstado) throws Exception;

 /**
  * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
  * @param idOficinaActiva
  * @param idEstado
  * @return
  * @throws Exception
  */
 public Long getByOficinaEstadoCount(Long idOficinaActiva, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de un listado de Libros en función de su estado.
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> getByLibrosEstado(List<Libro> libros, Long idEstado) throws Exception;

 /**
  * Busca los Registros de Entrada de un listado de Libros en función de su estado.
  * @param libros
  * @param idEstado
  * @return
  * @throws Exception
  */
 public Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Cambia el estado de un RegistroEntrada
     * @param idRegistro
     * @param idEstado
     * @throws Exception
     */
    public void cambiarEstado(Long idRegistro, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin, por Usuario en los Libros gestionados
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaEntradaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén pendientes en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaEntradaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin, Libro y Número de registro
     * @param fechaInicio
     * @param fechaFin
     * @param idLibro
     * @param numeroRegistro
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaPorLibroTipoNumero(Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception;

    /**
     * Busca los últimos RegistroEntrada de una Oficina
     * @param idOficina
     * @param total
     * @return
     * @throws Exception
     */
    public List<RegistroBasico> getUltimosRegistros(Long idOficina, Integer total) throws Exception;

    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro formateado
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     */
    public RegistroEntrada findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception;

    /**
     * Obtiene el numero de registro formateado de un RegistroEntrada
     * a partir de su numero de registro, año y libro.
     * @param numero
     * @param anyo
     * @param libro
     * @return
     * @throws Exception
     */
    public RegistroEntrada findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception;
    
    
    /**
     * Anula un RegistroEntrada, cambiandole el estado a anulado.
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void anularRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Activa un RegistroEntrada, cambiandole el estado a anulado.
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void activarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Visa un RegistroEntrada, cambiandole el estado a anulado.
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void visarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Tramita un RegistroEntrada, cambiandole el estado a tramitado.
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void tramitarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Retorna el identificador del Libro al que pertenece el RegistroEntrada
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    public Long getLibro(Long idRegistroEntrada) throws Exception;

    /**
     * Elimina los RegistroEntrada de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada de un Libro.
     * @param idLibro
     * @return
     * @throws Exception
     */
    public Long getTotalByLibro(Long idLibro) throws Exception;

    /**
     * Comprueba si un usuario tiene RegistroEntrada
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Comprueba si un RegistroEntrada tiene un Estado en concreto
     *
     * @param idRegistroEntrada
     * @param idEstado
     * @return
     * @throws Exception
     */
    public Boolean tieneEstado(Long idRegistroEntrada, Long idEstado) throws Exception;

    /**
     * Método que obtiene los destinatarios a los que distribuir el registro
     *
     * @param re registro de entrada a distribuir
     * @return lista de destinatarios a los que se debe distribuir el registro
     * @throws Exception
     * @throws I18NException
     */
    public Destinatarios distribuir(RegistroEntrada re) throws Exception, I18NException;

    /**
     * Método que envia un registro de entrada a un conjunto de destinatarios
     *
     * @param re      registro de entrada
     * @param wrapper contiene los destinatarios a los que enviar el registro de entrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public Boolean enviar(RegistroEntrada re, DestinatarioWrapper wrapper) throws Exception, I18NException;

}
