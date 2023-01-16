package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Contador;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.persistence.utils.NumeroRegistro;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface ContadorLocal extends BaseEjb<Contador, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/ContadorEJB";

    /**
     * Aumenta el 1 el contador pasado por parámetro
     *
     * @param idContador
     * @return
     * @throws I18NException
     */
    NumeroRegistro incrementarContador(Long idContador) throws I18NException;

    /**
     * Pone a 0 el Contador
     *
     * @param idContador
     * @throws I18NException
     */
    void reiniciarContador(Long idContador) throws I18NException;

    /**
     * Reinicia todos los contadores de un Libro
     *
     * @param libro
     * @throws I18NException
     */
    void reiniciarContadoresLibro(Libro libro) throws I18NException;

    /**
     * Obtiene una secuenta de 8 dígitos a partir del Contador Sir de la Entidad
     *
     * @param idContador
     * @return
     * @throws I18NException
     */
    String secuenciaSir(Long idContador) throws I18NException;

}
