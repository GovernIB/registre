package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.Oficio;
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

    /**
     * Obtiene los Organimos destino de los Registros de Entrada que están considerados Oficios de Remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    public List<Organismo> organismosEntradaPendientesRemision(Long idOficina, List<Libro> libros, Set<Long> organismos) throws Exception;

    /**
     * Obtiene el total de Registros de Entrada que están considerados Oficios de Remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    public Long oficiosEntradaPendientesRemisionCount(Long idOficina, List<Libro> libros, Set<Long> organismos) throws Exception;

    /**
     * Obtiene todos los Registros de Entrada que están considerados Oficios de Remisión de un Organismo destinatario en concreto
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
    public OficiosRemisionOrganismo oficiosEntradaPendientesRemision(Integer pageNumber, final Integer resultsPerPage, Integer any, Oficina oficinaActiva, Long idLibro, String codigoOrganismo, Set<Long> organismos, Entidad entidadActiva) throws Exception;


    /**
     * Comprueba si el Registro es considerado como un OficioRemision y de que tipo
     * @param idRegistro
     * @param organismos
     * @return
     * @throws Exception
     */
    public Oficio isOficio(Long idRegistro, Set<Long> organismos, Entidad entidadActiva) throws Exception;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision interno o no
     *
     * @param idRegistro
     * @param organismos Lista con los Destinatarios que no se consideran Oficio de Remisión
     * @return
     * @throws Exception
     */
    public Boolean isOficioRemisionInterno(Long idRegistro, Set<Long> organismos) throws Exception;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision externo o no
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public Boolean isOficioRemisionExterno(Long idRegistro) throws Exception;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision SIR o no
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public List<OficinaTF> isOficioRemisionSir(Long idRegistro) throws Exception;

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
    public OficioRemision crearOficioRemisionInterno(List<RegistroEntrada> registrosEntrada,
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
    public OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada,
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
    public void crearJustificantesRegistros(List<RegistroEntrada> registros, UsuarioEntidad usuario) throws Exception, I18NException, I18NValidationException;


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
    public List<RegistroEntrada> aceptarOficioRemision(OficioRemision oficioRemision,
                                                        UsuarioEntidad usuario, Oficina oficinaActiva,
                                                        List<OficioPendienteLlegada> oficios) throws Exception, I18NException, I18NValidationException;

}
