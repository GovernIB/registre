package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (EJB)
 * Date: 16/01/14
 */
@Local
public interface OficioRemisionSalidaUtilsLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/OficioRemisionSalidaUtilsEJB";


    /**
     * @param idEntidad
     * @param idOficina
     * @param tipoEvento
     * @param total
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Organismo> organismosSalidaPendientesRemisionTipo(Long idEntidad, Long idOficina, Long tipoEvento, Integer total, Boolean fecha) throws I18NException;

    /**
     * Obtiene el total de Registros de Salida que están considerados Oficios de Remisión
     *
     * @param idOficina
     * @param tipoEvento
     * @return
     * @throws I18NException
     */
    Long oficiosSalidaPendientesRemisionCount(Long idOficina, Long tipoEvento) throws I18NException;

    /**
     * Obtiene todos los Registros de Salida que están considerados Oficios de Remisión de un Organismo destinatario en concreto
     *
     * @param pageNumber
     * @param any
     * @param oficinaActiva   Oficina activa
     * @param idOrganismo
     * @param codigoOrganismo Organismo destinatario seleccionado
     * @param entidadActiva
     * @return
     * @throws I18NException
     */
    OficiosRemisionOrganismo oficiosSalidaPendientesRemision(Integer pageNumber, Integer any, Oficina oficinaActiva, Long idOrganismo, String codigoOrganismo, Entidad entidadActiva, Long tipoEvento) throws I18NException;

    /**
     * @param registrosSalida
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param idOrganismo
     * @param idLibro
     * @return
     */
    OficioRemision crearOficioRemisionInterno(List<RegistroSalida> registrosSalida, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro) throws I18NException, I18NValidationException;

    /**
     * @param registrosSalida
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param organismoExterno
     * @param organismoExternoDenominacion
     * @param idLibro
     * @return
     */
    OficioRemision crearOficioRemisionExterno(List<RegistroSalida> registrosSalida, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno, String organismoExternoDenominacion, Long idLibro) throws I18NException, I18NValidationException;

    /**
     *
     * @param registroSalida
     * @param oficinaActiva
     * @param usuarioEntidad
     * @param oficinaSirDestino
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    OficioRemision crearOficioRemisionSIR(RegistroSalida registroSalida,Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, OficinaTF oficinaSirDestino)
            throws I18NException, I18NValidationException;

    /**
     * Genera los Justificantes de todos los registros de un Oficio de Remisión
     * @param registros
     * @param usuario
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    List<RegistroSalida> crearJustificantesRegistros(Entidad entidad, List<RegistroSalida> registros, UsuarioEntidad usuario) throws I18NException, I18NValidationException;

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
