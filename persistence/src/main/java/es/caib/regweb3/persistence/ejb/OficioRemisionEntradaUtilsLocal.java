package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (EJB)
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface OficioRemisionEntradaUtilsLocal {

    /**
     *
     * @param idOficina
     * @param libros
     * @param total
     * @return
     * @throws Exception
     */
    List<Organismo> organismosEntradaPendientesRemisionInternos(Long idOficina, List<Libro> libros, Integer total) throws Exception;

    /**
     *
     * @param idOficina
     * @param tipoEvento
     * @param total
     * @return
     * @throws Exception
     */
    List<Organismo> organismosEntradaPendientesRemisionExternosTipo(Long idOficina, Long tipoEvento, Integer total) throws Exception;


    /**
     * Obtiene el total de Registros de Entrada que están considerados Oficios de Remisión internos
     * @param idOficina
     * @param libros
     * @return
     * @throws Exception
     */
    Long oficiosEntradaInternosPendientesRemisionCount(Long idOficina, List<Libro> libros) throws Exception;

    /**
     * Obtiene el total de Registros de Entrada que están considerados Oficios de Remisión externos
     * @param idOficina
     * @param libros
     * @return
     * @throws Exception
     */
    Long oficiosEntradaExternosPendientesRemisionCount(Long idOficina) throws Exception;

    /**
     * Obtiene todos los Registros de Entrada que están considerados Oficios de Remisión de un Organismo destinatario en concreto
     * @param tipoEvento
     * @param pageNumber
     * @param any
     * @param oficinaActiva Oficina activa
     * @param codigoOrganismo Organismo destinatario seleccionado
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    OficiosRemisionOrganismo oficiosEntradaPendientesRemision(Long tipoEvento, Integer pageNumber, final Integer resultsPerPage, Integer any, Oficina oficinaActiva, String codigoOrganismo, Entidad entidadActiva) throws Exception;


    /**
     * @param registrosEntrada
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param idOrganismo
     * @param idLibro
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    OficioRemision crearOficioRemisionInterno(List<RegistroEntrada> registrosEntrada,
                                              Oficina oficinaActiva, UsuarioEntidad usuarioEntidad,
                                              Long idOrganismo, Long idLibro) throws Exception, I18NException, I18NValidationException;


    /**
     * @param registrosEntrada
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param organismoExternoCodigo
     * @param organismoExternoDenominacion
     * @param idLibro
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada,
                                              Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExternoCodigo,
                                              String organismoExternoDenominacion, Long idLibro) throws Exception, I18NException, I18NValidationException;

    /**
     *
     * @param registroEntrada
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param oficinaSirDestino
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    OficioRemision crearOficioRemisionSIR(RegistroEntrada registroEntrada, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, OficinaTF oficinaSirDestino )
            throws Exception, I18NException, I18NValidationException;

    /**
     * Genera los Justificantes de todos los registros de un Oficio de Remisión
     * @param registros
     * @param usuario
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    List<RegistroEntrada> crearJustificantesRegistros(List<RegistroEntrada> registros, UsuarioEntidad usuario) throws Exception, I18NException, I18NValidationException;


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
