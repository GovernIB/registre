package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by mgonzalez on 21/03/2018.
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA"})
public interface ColaLocal extends BaseEjb<Cola, Long> {

    /**
     * @param inicio
     * @return
     * @throws Exception
     */
    List<Cola> getPagination(int inicio) throws Exception;

    /**
     * Busca todos los elementos de la cola del mismo tipo
     * @param tipo tipo de elemento
     * @return
     * @throws Exception
     */
    List<Cola> findByTipoEntidad(Long tipo, Long idEntidad,Integer total) throws Exception;

    /**
     * Busca un elemento de la cola por IdObjeto y por entidad
     * @param idObjeto
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Cola findByIdObjeto(Long idObjeto, Long idEntidad) throws Exception;

    /**
     * Busca un elemento de la cola por IdObjeto, Entidad y Estado
     * @param idObjeto
     * @param idEntidad
     * @param idEstado
     * @return
     * @throws Exception
     */
    Cola findByIdObjetoEstado(Long idObjeto,Long idEntidad, Long idEstado) throws Exception;

    /**
     * Obtiene los elementos de la cola por tipo y por entidad que han alcanzado el máximo de reintentos
     * @param tipo tipo de elemento
     * @param idEntidad entidad a la que pertenece
     * @return
     * @throws Exception
     */
    List<Cola> findByTipoMaxReintentos(Long tipo, Long idEntidad, int maxReintentos) throws Exception;

    /**
     * Realiza la busqueda de los elementos de la cola por entidad
     * @param cola
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Cola cola, Long idEntidad) throws Exception;

    /**
     *
     * @param re
     * @param usuarioEntidad
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    boolean enviarAColaDistribucion(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * añade a la cola de cusodia un nuevo anexo Justificante
     * @param registro
     * @param usuarioEntidad
     * @return
     */
    boolean enviarAColaCustodia(IRegistro registro, Long tipoRegistro, UsuarioEntidad usuarioEntidad);

    /**
     * Vuelve a activar los elementos en la cola poniendo el contador a 0 para que se puedan volver a enviar
     * @param idEntidad
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
     void reiniciarColabyEntidadTipo(Long idEntidad, Long tipo) throws Exception, I18NException;

    /**
     *
     * @param elemento
     * @param error
     * @param entidadId
     * @throws Exception
     */
     void actualizarElementoCola(Cola elemento,Long entidadId, String error) throws Exception;

    /**
     * Elimina los elementos de la cola de distribución de la entidad indicada
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Reiniciamos un elemento de la cola para que vuelva a relanzarse
     * @param idCola
     * @throws Exception
     */
    void reiniciarElementoCola(Long idCola) throws Exception;

    /**
     * MArca como procesado un elemento de la Cola
     * @param elemento
     */
    void procesarElemento(Cola elemento) throws Exception;

    /**
     * Marca como procesado un elemento de tipo Distribución de la Cola y cambia el estado del RegistroEntrada
     * @param elemento
     */
    void procesarElementoDistribucion(Cola elemento) throws Exception;

    /**
     * Método que elimina los elementos que fueron procesados hace x meses
     * @param idEntidad
     * @param meses
     * @throws Exception
     */
    void purgarElementosProcesados(Long idEntidad, Integer meses) throws Exception;

    /**
     * Obtiene los elementos de cualquier cola que estén en estado Error
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Cola> getElementosError( Long idEntidad) throws Exception;
}
