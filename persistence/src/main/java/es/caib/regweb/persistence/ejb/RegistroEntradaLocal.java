package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.Libro;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.model.utils.RegistroBasico;
import es.caib.regweb.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.Date;
import java.util.List;

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
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada)throws Exception, I18NException;
    

    /**
     * Guarda un Registro de Entrada y le asocia un número de registro (con anexos)
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada, 
        UsuarioEntidad usuarioEntidad, List<AnexoFull> anexosFull) throws Exception, I18NException;

   /**
     * Busca los Registros de Entrada en función de los parámetros
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, List<Libro> libros, String interesadoNom, String interesadoDoc, String organoDest, Boolean anexos) throws Exception;

    /**
     * Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar
     * @param libro
     * @return
     * @throws Exception
     */
    public List<String> oficiosPendientesRemisionInterna(Libro libro) throws Exception;

    /**
     * Busca los RegistroEntrada pendientes de tramitar mediante un OficioRemision INTERNI
     * y los agrupa según su Organismo destinatario.
     * @param any
     * @param libro
     * @return
     * @throws Exception
     */
    public List<OficiosRemisionOrganismo> oficiosPendientesRemisionInterna(Integer any, Libro libro) throws Exception;


    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision o no
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public Boolean isOficioRemisionInterno(Long idRegistro) throws Exception;

    /**
     * Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar
     * @param libro
     * @return
     * @throws Exception
     */
    public List<String> oficiosPendientesRemisionExterna(Libro libro) throws Exception;

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
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin y el libro
     * @param fechaInicio
     * @param fechaFin
     * @param libros
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaLibroRegistro(Date fechaInicio, Date fechaFin, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaIndicadores(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;

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
     * Busca los Registros de Entrada de un listado de Libros en función de su estado.
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    public List<RegistroBasico> getByLibrosEstado(List<Libro> libros, Long idEstado) throws Exception;

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
     * Tramita un RegistroEntrada, cambiandole el estado a tramitado.
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void tramitarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

}
