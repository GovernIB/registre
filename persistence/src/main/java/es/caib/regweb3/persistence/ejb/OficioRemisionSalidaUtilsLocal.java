package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Set;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (EJB)
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface OficioRemisionSalidaUtilsLocal {

    /**
     * Obtiene los Organimos destino de los Registros de Salida que están considerados Oficios de Remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    public List<Organismo> organismosSalidaPendientesRemision(Long idOficina, List<Libro> libros, Set<String> organismos) throws Exception;

    /**
     * Obtiene el total de Registros de Salida que están considerados Oficios de Remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    public Long oficiosSalidaPendientesRemisionCount(Long idOficina, List<Libro> libros, Set<String> organismos) throws Exception;

    /**
     * Obtiene todos los Registros de Salida que están considerados Oficios de Remisión de un Organismo destinatario en concreto
     * @param pageNumber
     * @param any
     * @param idOficina Oficina activa
     * @param idLibro Libro seleccionado
     * @param codigoOrganismo Organismo destinatario seleccionado
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    public OficiosRemisionOrganismo oficiosSalidaPendientesRemision(Integer pageNumber, Integer any, Long idOficina, Long idLibro, String codigoOrganismo, Entidad entidadActiva) throws Exception;

    /**
     * @param registrosSalida
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param idOrganismo
     * @param idLibro
     * @return
     */
    public OficioRemision crearOficioRemisionInterno(List<RegistroSalida> registrosSalida, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro) throws Exception, I18NException, I18NValidationException;

    /**
     * @param registrosSalida
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param organismoExterno
     * @param organismoExternoDenominacion
     * @param idLibro
     * @return
     */
    public OficioRemision crearOficioRemisionExterno(List<RegistroSalida> registrosSalida, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno, String organismoExternoDenominacion, Long idLibro) throws Exception, I18NException, I18NValidationException;

    /**
     * @param oficioRemision
     * @param usuario
     * @param oficinaActiva
     * @param oficios
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public List<RegistroEntrada> procesarOficioRemision(OficioRemision oficioRemision,
                                                        UsuarioEntidad usuario, Oficina oficinaActiva,
                                                        List<OficioPendienteLlegada> oficios) throws Exception, I18NException, I18NValidationException;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision o no
     *
     * @param idRegistro
     * @param organismos Lista con los Destinatarios que no se consideran Oficio de Remisión
     * @return
     * @throws Exception
     */
    public Boolean isOficioRemision(Long idRegistro, Set<String> organismos) throws Exception;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision interno o no
     * @param registroSalida
     * @param organismos
     * @return
     * @throws Exception
     */
    public Boolean isOficioRemisionInterno(RegistroSalida registroSalida, Set<String> organismos) throws Exception;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision externo o no
     * @param registroSalida
     * @param organismos
     * @return
     * @throws Exception
     */
    public Boolean isOficioRemisionExterno(RegistroSalida registroSalida, Set<String> organismos) throws Exception;
}
