package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
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
    public List<Cola> findByTipoEntidad(Long tipo, Long idEntidad,Integer total, int maxReintentos) throws Exception;



    /**
     * Obtiene los elementos de la cola por tipo y por entidad que han alcanzado el máximo de reintentos
     * @param tipo tipo de elemento
     * @param idEntidad entidad a la que pertenece
     * @param total numero total de resultados que se quiere devolver
     * @return
     * @throws Exception
     */
    public List<Cola> findByTipoEntidadMaxReintentos(Long tipo, Long idEntidad,Integer total, int maxReintentos) throws Exception;

    /**
     * Realiza la busqueda de los elementos de la cola por entidad
     * @param cola
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Cola cola, Long idEntidad) throws Exception;

    /**
     * Vuelve a activar los elementos en la cola poniendo el contador a 0 para que se puedan volver a enviar
     * @param idEntidad
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
     Paginacion reiniciarColabyEntidadTipo(Long idEntidad, Long tipo, Cola cola) throws Exception, I18NException, I18NValidationException;


    /**
     *  Actualiza los datos de un elemento de la cola durante el proceso de reintentos.
     * @param elemento elemento que se actualiza
     * @param idioma idioma del mail que se va a enviar
     * @param administradores
     * parametros necesarios para crear la entrada en la tabla de integraciones
     * @param descripcion
     * @param peticion
     * @param tiempo
     * @param entidadId
     * @param hora
     * @param th
     * @throws Exception
     */
    void actualizarElementoCola(Cola elemento,String descripcion, StringBuilder peticion,long tiempo,Long entidadId, String hora, String idioma, Throwable th, List<UsuarioEntidad> administradores, int maxReintentos) throws Exception;

    /**
     * Elimina los elementos de la cola de distribución de la entidad indicada
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Reiniciamos un elemento de la cola para que vuelva a relanzarse
     * @param cola
     * @throws Exception
     */
    void reiniciarElementoCola(Cola cola) throws Exception;

}
