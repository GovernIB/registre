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
public interface OficioRemisionEntradaUtilsLocal {


    List<Organismo> organismosEntradaPendientesRemisionInternos(Long idOficina, List<Libro> libros, Integer total) throws Exception;

    List<Organismo> organismosEntradaPendientesRemisionExternosTipo(Long idOficina, List<Libro> libros, Long tipoEvento, Integer total) throws Exception;



    /**
     * Obtiene los Organimos destino de los Registros de Entrada que están considerados Oficios de Remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    List<Organismo> organismosEntradaPendientesRemision(Long idOficina, List<Libro> libros, Set<Long> organismos, Integer total) throws Exception;

    /**
     * Obtiene el total de Registros de Entrada que están considerados Oficios de Remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    Long oficiosEntradaPendientesRemisionCount(Long idOficina, List<Libro> libros, Set<Long> organismos) throws Exception;

    /**
     * Obtiene todos los Registros de Entrada que están considerados Oficios de Remisión de un Organismo destinatario en concreto
     * @param tipoEvento
     * @param pageNumber
     * @param any
     * @param oficinaActiva Oficina activa
     * @param idLibro Libro seleccionado
     * @param codigoOrganismo Organismo destinatario seleccionado
     * @param organismos
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    OficiosRemisionOrganismo oficiosEntradaPendientesRemision(Long tipoEvento, Integer pageNumber, final Integer resultsPerPage, Integer any, Oficina oficinaActiva, Long idLibro, String codigoOrganismo, Set<Long> organismos, Entidad entidadActiva) throws Exception;


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
