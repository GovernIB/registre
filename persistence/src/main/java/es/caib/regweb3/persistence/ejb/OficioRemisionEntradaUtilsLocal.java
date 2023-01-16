package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (EJB)
 * Date: 16/01/14
 */
@Local
public interface OficioRemisionEntradaUtilsLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/OficioRemisionEntradaUtilsEJB";


    /**
     * @param idOficina
     * @param libros
     * @param total
     * @return
     * @throws I18NException
     */
    List<Organismo> organismosEntradaPendientesRemisionInternos(Long idOficina, List<Libro> libros, Integer total) throws I18NException;

    /**
     * @param idOficina
     * @param tipoEvento
     * @param total
     * @return
     * @throws I18NException
     */
    List<Organismo> organismosEntradaPendientesRemisionExternosTipo(Long idEntidad, Long idOficina, Long tipoEvento, Integer total) throws I18NException;


    /**
     * Obtiene el total de Registros de Entrada que están considerados Oficios de Remisión internos
     *
     * @param idOficina
     * @param libros
     * @return
     * @throws I18NException
     */
    Long oficiosEntradaInternosPendientesRemisionCount(Long idOficina, List<Libro> libros) throws I18NException;

    /**
     * Obtiene el total de Registros de Entrada que están considerados Oficios de Remisión externos
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Long oficiosEntradaExternosPendientesRemisionCount(Long idOficina) throws I18NException;

    /**
     * Obtiene todos los Registros de Entrada que están considerados Oficios de Remisión de un Organismo destinatario en concreto
     *
     * @param tipoEvento
     * @param pageNumber
     * @param any
     * @param oficinaActiva   Oficina activa
     * @param codigoOrganismo Organismo destinatario seleccionado
     * @param entidadActiva
     * @return
     * @throws I18NException
     */
    OficiosRemisionOrganismo oficiosEntradaPendientesRemision(Long tipoEvento, Integer pageNumber, final Integer resultsPerPage, Integer any, Oficina oficinaActiva, String codigoOrganismo, Entidad entidadActiva) throws I18NException;


    /**
     * @param registrosEntrada
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param idOrganismo
     * @param idLibro
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    OficioRemision crearOficioRemisionInterno(List<RegistroEntrada> registrosEntrada, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad,
                                              Long idOrganismo, Long idLibro) throws I18NException, I18NValidationException;


    /**
     * @param registrosEntrada
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param organismoExternoCodigo
     * @param organismoExternoDenominacion
     * @param idLibro
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada, Entidad entidad,
                                              Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExternoCodigo,
                                              String organismoExternoDenominacion, Long idLibro) throws I18NException, I18NValidationException;

    /**
     *
     * @param registroEntrada
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param oficinaSirDestino
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    OficioRemision crearOficioRemisionSIR(RegistroEntrada registroEntrada, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, OficinaTF oficinaSirDestino )
            throws I18NException, I18NValidationException;

    /**
     * Genera los Justificantes de todos los registros de un Oficio de Remisión
     *
     * @param registros
     * @param usuario
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    List<RegistroEntrada> crearJustificantesRegistros(Entidad entidad, List<RegistroEntrada> registros, UsuarioEntidad usuario) throws I18NException, I18NValidationException;

    /**
     * @param oficioRemision
     * @param usuario
     * @param oficinaActiva
     * @param oficios
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    List<RegistroEntrada> aceptarOficioRemision(OficioRemision oficioRemision, Entidad entidad, UsuarioEntidad usuario, Oficina oficinaActiva,
                                                List<OficioPendienteLlegada> oficios) throws I18NException, I18NValidationException;

}
