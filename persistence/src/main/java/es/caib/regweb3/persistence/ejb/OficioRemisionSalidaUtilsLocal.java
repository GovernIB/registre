package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.LinkedHashSet;
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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface OficioRemisionSalidaUtilsLocal {



    LinkedHashSet<Organismo> organismosSalidaPendientesRemisionTipo(Long idOficina, List<Libro> libros, Long tipoEvento, Integer total) throws Exception;


    /**
     * Obtiene los Organimos destino de los Registros de Salida que están considerados Oficios de Remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    LinkedHashSet<Organismo> organismosSalidaPendientesRemision(Long idOficina, List<Libro> libros, Set<String> organismos, Long entidadActiva, Integer total) throws Exception;

    /**
     * Obtiene el total de Registros de Salida que están considerados Oficios de Remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    Long oficiosSalidaPendientesRemisionCount(Long idOficina, List<Libro> libros, Set<String> organismos, Long entidadActiva) throws Exception;

    /**
     * Obtiene todos los Registros de Salida que están considerados Oficios de Remisión de un Organismo destinatario en concreto
     * @param pageNumber
     * @param any
     * @param oficinaActiva Oficina activa
     * @param idLibro Libro seleccionado
     * @param codigoOrganismo Organismo destinatario seleccionado
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    OficiosRemisionOrganismo oficiosSalidaPendientesRemision(Integer pageNumber, Integer any, Oficina oficinaActiva, Long idLibro, String codigoOrganismo, Entidad entidadActiva, Long tipoEvento) throws Exception;

    /**
     * @param registrosSalida
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param idOrganismo
     * @param idLibro
     * @return
     */
    OficioRemision crearOficioRemisionInterno(List<RegistroSalida> registrosSalida, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro) throws Exception, I18NException, I18NValidationException;

    /**
     * @param registrosSalida
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param organismoExterno
     * @param organismoExternoDenominacion
     * @param idLibro
     * @return
     */
    OficioRemision crearOficioRemisionExterno(List<RegistroSalida> registrosSalida, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno, String organismoExternoDenominacion, Long idLibro) throws Exception, I18NException, I18NValidationException;

    /**
     * Genera los Justificantes de todos los registros de un Oficio de Remisión
     * @param registros
     * @param usuario
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public List<RegistroSalida> crearJustificantesRegistros(List<RegistroSalida> registros, UsuarioEntidad usuario) throws Exception, I18NException, I18NValidationException;

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
    List<RegistroEntrada> aceptarOficioRemision(OficioRemision oficioRemision,
                                                UsuarioEntidad usuario, Oficina oficinaActiva,
                                                List<OficioPendienteLlegada> oficios) throws Exception, I18NException, I18NValidationException;


}
