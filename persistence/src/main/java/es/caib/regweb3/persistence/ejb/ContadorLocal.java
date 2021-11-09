package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Contador;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.persistence.utils.NumeroRegistro;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface ContadorLocal extends BaseEjb<Contador, Long> {

    /**
     * Aumenta el 1 el contador pasado por parámetro
     * @param idContador
     * @return
     * @throws Exception
     */
    NumeroRegistro incrementarContador(Long idContador) throws Exception;

    /**
     * Pone a 0 el Contador
     * @param idContador
     * @throws Exception
     */
    void reiniciarContador(Long idContador) throws Exception;

    /**
     * Reinicia todos los contadores de un Libro
     * @param libro
     * @throws Exception
     */
    void reiniciarContadoresLibro(Libro libro) throws Exception;

    /**
     * Obtiene una secuenta de 8 dígitos a partir del Contador Sir de la Entidad
     * @param idContador
     * @return
     * @throws Exception
     */
    String secuenciaSir(Long idContador) throws Exception;

}
