package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by mgonzalez on 21/03/2018.
 */
@Local
public interface ColaLocal extends BaseEjb<Cola, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/ColaEJB";

    /**
     * @param inicio
     * @return
     * @throws I18NException
     */
    List<Cola> getPagination(int inicio) throws I18NException;

    /**
     * Busca todos los elementos de la cola del mismo tipo
     *
     * @param tipo tipo de elemento
     * @return
     * @throws I18NException
     */
    List<Cola> findByTipoEntidad(Long tipo, Long idEntidad,Integer inicio, Integer total) throws I18NException;

    /**
     * Retorna el total de elementos de la Cola en estado PENDIENTE
     * @param tipo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long findPendientesByTipo(Long tipo, Long idEntidad) throws I18NException;

    /**
     * Busca un elemento de la cola por IdObjeto y por entidad
     *
     * @param idObjeto
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Cola findByIdObjeto(Long idObjeto, Long idEntidad) throws I18NException;

    /**
     * Busca un elemento de la cola por IdObjeto, Entidad y Estado
     *
     * @param idObjeto
     * @param idEntidad
     * @param idEstado
     * @return
     * @throws I18NException
     */
    Cola findByIdObjetoEstado(Long idObjeto, Long idEntidad, Long idEstado) throws I18NException;

    /**
     * Obtiene los elementos de la cola por tipo y por entidad que han alcanzado el máximo de reintentos
     *
     * @param tipo      tipo de elemento
     * @param idEntidad entidad a la que pertenece
     * @return
     * @throws I18NException
     */
    List<Cola> findByTipoMaxReintentos(Long tipo, Long idEntidad, int maxReintentos) throws I18NException;

    /**
     * Realiza la busqueda de los elementos de la cola por entidad
     *
     * @param cola
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Cola cola, Long idEntidad) throws I18NException;

    /**
     * @param re
     * @param usuarioEntidad
     * @param descripcion
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    boolean enviarAColaDistribucion(RegistroEntrada re, UsuarioEntidad usuarioEntidad, String descripcion) throws I18NException;

    /**
     * añade a la cola de cusodia un nuevo anexo Justificante
     *
     * @param registro
     * @param usuarioEntidad
     * @return
     */
    boolean enviarAColaCustodia(IRegistro registro, Long tipoRegistro, UsuarioEntidad usuarioEntidad);

    /**
     * Vuelve a activar los elementos en la cola poniendo el contador a 0 para que se puedan volver a enviar
     *
     * @param idEntidad
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    void reiniciarColabyEntidadTipo(Long idEntidad, Long tipo) throws I18NException;

    /**
     * @param elemento
     * @param error
     * @param entidadId
     * @throws I18NException
     */
    void actualizarElementoCola(Cola elemento, Long entidadId, String error) throws I18NException;

    /**
     * Elimina los elementos de la cola de distribución de la entidad indicada
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Reiniciamos un elemento de la cola para que vuelva a relanzarse
     *
     * @param idCola
     * @throws I18NException
     */
    void reiniciarElementoCola(Long idCola) throws I18NException;

    /**
     * Marca como procesado un elemento de la Cola
     *
     * @param elemento
     */
    void procesarElemento(Cola elemento) throws I18NException;

    /**
     * Método que elimina los elementos que fueron procesados hace x meses
     *
     * @param idEntidad
     * @throws I18NException
     */
    Integer purgarElementosProcesados(Long idEntidad) throws I18NException;

    /**
     * Obtiene los elementos de cualquier cola que estén en estado Error
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Cola> getElementosError(Long idEntidad) throws I18NException;


    /**
     * Elimina un elemento de la Cola
     * @param idCola
     * @throws I18NException
     */
    void eliminarElemento(Long idCola) throws I18NException;
}
