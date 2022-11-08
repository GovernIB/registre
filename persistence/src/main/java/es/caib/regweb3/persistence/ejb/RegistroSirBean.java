package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.*;
import es.caib.regweb3.model.utils.*;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.persistence.utils.ProgresoActualitzacion.TipoInfo;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.schema.*;
import es.caib.regweb3.sir.core.schema.types.Documentacion_FisicaType;
import es.caib.regweb3.sir.core.schema.types.Indicador_PruebaType;
import es.caib.regweb3.sir.core.schema.types.Tipo_RegistroType;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.plugin.geiser.api.ApunteRegistro;
import org.plugin.geiser.api.RespuestaBusquedaGeiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.util.DefaultPropertiesPersister;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.caib.regweb3.utils.RegwebConstantes.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */

@Stateless(name = "RegistroSirEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RegistroSirBean extends BaseEjbJPA<RegistroSir, Long> implements RegistroSirLocal {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdf_view = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RegistroSirHelperLocal registroSirHelperEjb; 
    @EJB private InteresadoSirLocal interesadoSirEjb;
    @EJB private AnexoSirLocal anexoSirEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private ArchivoLocal archivoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private CatProvinciaLocal catProvinciaEjb;
    @EJB private CatLocalidadLocal catLocalidadEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private CatPaisLocal catPaisEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private SignatureServerLocal signatureServerEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;
    @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
    @EJB private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;
    @EJB private UsuarioEntidadLocal usuarioEntidadEjb;
    
    public static Map<Long, ProgresoActualitzacion> progresoActualitzacion = new HashMap<Long, ProgresoActualitzacion>();
    
    @Autowired 
    private GeiserPluginHelper pluginHelper;

    @Override
    public List<Long> getUltimosPendientesProcesarERTE(EstadoRegistroSir estado, String oficinaSir, Date fechaInicio, Date fechaFin, String aplicacion, Integer total) throws Exception{

        Query q = em.createQuery("Select r.id from RegistroSir as r " +
                "where r.codigoEntidadRegistral = :oficinaSir and r.estado = :idEstado " +
                "and  (r.fechaRecepcion >= :fechaInicio and r.fechaRecepcion <= :fechaFin) " +
                "and r.aplicacion LIKE :aplicacion " +
                "order by r.fechaRecepcion");

        q.setMaxResults(total);
        q.setParameter("oficinaSir", oficinaSir);
        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("aplicacion", "%"+aplicacion+"%");
        q.setParameter("idEstado", estado);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    public RegistroSir getReference(Long id) throws Exception {

        return em.getReference(RegistroSir.class, id);
    }

    @Override
    public RegistroSir findById(Long id) throws Exception {

        RegistroSir registroSir = em.find(RegistroSir.class, id);
        Hibernate.initialize(registroSir.getAnexos());
        Hibernate.initialize(registroSir.getInteresados());

        return registroSir;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getAll() throws Exception {

        return em.createQuery("Select registroSir from RegistroSir as registroSir order by registroSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroSir.id) from RegistroSir as registroSir");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroSir from RegistroSir as registroSir order by registroSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroSir getRegistroSir(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception {

        Query q = em.createQuery("Select registroSir from RegistroSir as registroSir where " +
                "registroSir.identificadorIntercambio = :identificadorIntercambio and registroSir.codigoEntidadRegistral = :codigoEntidadRegistralDestino " +
                "and registroSir.estado != :eliminado");

        q.setParameter("identificadorIntercambio",identificadorIntercambio);
        q.setParameter("codigoEntidadRegistralDestino",codigoEntidadRegistralDestino);
        q.setParameter("eliminado",EstadoRegistroSir.ELIMINADO);

        List<RegistroSir> registroSir = q.getResultList();
        if(registroSir.size() >= 1){
            return registroSir.get(0);
        }else{
            return null;
        }
    }
    
    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getRegistroSirByNumeroRegistro(String numeroRegistro, String codigoEntidadRegistralDestino) throws Exception {

        Query q = em.createQuery("Select registroSir from RegistroSir as registroSir where " +
                "registroSir.numeroRegistro = :numeroRegistro and registroSir.codigoEntidadRegistral = :codigoEntidadRegistralDestino " +
                "and registroSir.estado != :eliminado");

        q.setParameter("numeroRegistro",numeroRegistro);
        q.setParameter("codigoEntidadRegistralDestino",codigoEntidadRegistralDestino);
        q.setParameter("eliminado",EstadoRegistroSir.ELIMINADO);

        List<RegistroSir> registroSir = q.getResultList();
        if(registroSir.size() >= 1){
            return registroSir.get(0).getId();
        }else{
            return null;
        }
    }

    @Override
    public RegistroSir getRegistroSirConAnexos(Long idRegistroSir) throws Exception{

        RegistroSir registroSir = findById(idRegistroSir);

        List<AnexoSir> anexosFull = new ArrayList<AnexoSir>();
        for (AnexoSir anexoSir : registroSir.getAnexos()) {

            anexoSir.setAnexoData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
            anexosFull.add(anexoSir);
        }

        registroSir.setAnexos(anexosFull);

        return registroSir;

    }

    @Override
    public ProgresoActualitzacion getProgresoRecuperacionRegistrosSir(Long entidadId) {
    	ProgresoActualitzacion progreso = progresoActualitzacion.get(entidadId);
		if (progreso != null && progreso.getProgreso() != null &&  progreso.getProgreso() >= 100) {
			progresoActualitzacion.remove(entidadId);
		}
		return progreso;
	}
    
    @Override
    public boolean isRecoveringRegistrosSIR(Long entidadId) {
		ProgresoActualitzacion progreso = progresoActualitzacion.get(entidadId);
		return progreso != null && (progreso.getProgreso() > 0 && progreso.getProgreso() < 100) && !progreso.isError();
	}
    
    @Override
    public Integer recuperarRegistrosSirGEISER(Entidad entidad, Date inicio, Date fin) throws Exception, I18NException {
    	String fechaInicio = null;
		String fechaFin = null;
		Integer total = 0;
		Long entidadId = entidad.getId();
		ProgresoActualitzacion progreso = progresoActualitzacion.get(entidadId);
    	try {
    		fechaInicio = (inicio != null) ? sdf.format(inicio) : getFechaInicioProximaBusqueda(entidadId);
    		fechaFin = (fin != null) ? sdf.format(fin) : sdf.format(new Date());
    		
    		if (progreso != null && (progreso.getProgreso() > 0 && progreso.getProgreso() < 100) && !progreso.isError()) {
    			progreso.addInfo(TipoInfo.WARNING, "Ja existeix un altre procés que està executant l'actualització");
				log.error("Ja existeix un altre procés que està executant l'actualització");
				return total;	// Ja existeix un altre procés que està executant l'actualització.
			}
    		
    		// inicialitza el seguiment del prgrés d'actualització
			progreso = new ProgresoActualitzacion();
			progresoActualitzacion.put(entidadId, progreso);
			progreso.addInfo(TipoInfo.TITOL, "Recuperando registros SIR [fechaInicio=" + sdf_view.format(sdf.parse(fechaInicio)) + ", fechaFin=" + sdf_view.format(sdf.parse(fechaFin)) + "]");
			log.info("Recuperando registros SIR [fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + "]");
    	    if (fechaInicio != null) {
    	    	// Busca en todas las oficinas definidas en el plugin
    	    	List<RespuestaBusquedaGeiser> resultadosBusqueda = pluginHelper.postProcesoBusquedaRegistroSirGeiser(fechaInicio, fechaFin, entidadId);
    	    	if (resultadosBusqueda != null) {
    	    		Integer totalRegistros = 0;
					List<ApunteRegistro> apuntesBusquedaFiltrados = new ArrayList<ApunteRegistro>();
	    	    	for (RespuestaBusquedaGeiser respuestaBusquedaGeiser : resultadosBusqueda) {
	    	    		progreso.addInfo(TipoInfo.INFO, "Analizando registros oficina " + respuestaBusquedaGeiser.getOficinaDestino());
						if (respuestaBusquedaGeiser != null && respuestaBusquedaGeiser.getNuTotalApuntes() != 0) {
							List<ApunteRegistro> apuntesBusqueda = respuestaBusquedaGeiser.getApuntes();
							List<ApunteRegistro> apuntesBusquedaFiltradosOficina = new ArrayList<ApunteRegistro>();
							for (ApunteRegistro apunteBusqueda : apuntesBusqueda) {
								if (!apunteBusqueda.getNuRegistroOrigen().equals(apunteBusqueda.getNuRegistro())) {
									List<Oficina> oficinas = oficinaEjb.findByEntidad(entidadId);
									for (Oficina oficina: oficinas) {
										if (oficina.getCodigo() != null && oficina.getCodigo().equals(apunteBusqueda.getCdAmbitoOrigen())) {
											apuntesBusquedaFiltradosOficina.add(apunteBusqueda);
										}
									}
								}
							}
							if (apuntesBusquedaFiltradosOficina.size() > 0)
								progreso.addInfo(TipoInfo.INFO, "[OFICINA=" + respuestaBusquedaGeiser.getOficinaDestino() + "] - " + apuntesBusquedaFiltradosOficina.size() + " registros filtrados con estado final");
							else
								progreso.addInfo(TipoInfo.WARNING, "[OFICINA=" + respuestaBusquedaGeiser.getOficinaDestino() + "] - No se han encontrado registros para las fechas introducidas");
							
							totalRegistros+=apuntesBusquedaFiltradosOficina.size();
							apuntesBusquedaFiltrados.addAll(apuntesBusquedaFiltradosOficina);
						}
	    	    	}
					progreso.setNumRegistrosSIR(totalRegistros);
					progreso.addInfo(TipoInfo.SUBTITOL, "Procesando un total de " + progreso.getNumRegistrosSIR() +" registros recibidos via SIR");
					
					if (apuntesBusquedaFiltrados.size() > 0) {
						for (ApunteRegistro apunteRegistroBusquedaFiltrado : apuntesBusquedaFiltrados) {
							String nuRegistro = apunteRegistroBusquedaFiltrado.getNuRegistro();
							progreso.addInfo(TipoInfo.INFO, "Realizando proceso registro recibido [numeroRegistro=" + nuRegistro + "]");
							Oficina oficinaOrigen = oficinaEjb.findByCodigoEntidad(apunteRegistroBusquedaFiltrado.getOrganoOrigen(), entidadId);
							RegistroEntrada registroEntradaExistente = registroEntradaConsultaEjb.findByNumeroRegistro(apunteRegistroBusquedaFiltrado.getOrganoDestino(), nuRegistro);
							
							// Solo crear asiento si la oficina no es de la entidad actual y no existe en Regweb
							if (oficinaOrigen == null && registroEntradaExistente == null) { 
								try {
									total = registroSirHelperEjb.crearRegistroSirRecibido(
											apunteRegistroBusquedaFiltrado, 
											entidadId, 
											total, 
											progreso);
									log.info("Registro de entrada " + nuRegistro + " creado correctamente");
									progreso.addInfo(TipoInfo.INFO, "Registro de entrada creado correctamente");
									progreso.addInfo(TipoInfo.INFO, "-------------------------------------------");
								} catch (Exception e) {
									log.error("Hi ha hagut un error creant el registre d'entrada del registre rebut: " + nuRegistro, e.getCause());
									progreso.addInfo(TipoInfo.ERROR, "Hi ha hagut un error creant el registre d'entrada del registre rebut: " + nuRegistro);
									progreso.incrementRegistrosRecuperados();
									enviarEmailErrorConsultaSirRecibido(nuRegistro, entidad);
									throw new RecuperacionRegistroException("Hi ha hagut un error creant el registre d'entrada del registre rebut: " + nuRegistro);
								}
							} else {
								log.info("Registro duplicado [numeroRegistro=" + nuRegistro + "]");
								progreso.addInfo(TipoInfo.WARNING, "Registro duplicado [numeroRegistro=" + nuRegistro + "]");
								progreso.addInfo(TipoInfo.INFO, "Se procede con la creación del siguiente registro");
								progreso.incrementRegistrosRecuperados();
							}
						}
    	    		} else {
						log.info("No se han encontrado registros para las fechas introducidas");
						progreso.addInfo(TipoInfo.WARNING, "No se han encontrado registros para las fechas introducidas");
					}
    	    	}
				updateFechaInicioProximaBusqueda(entidadId, fechaFin);
    	    }
    	    progreso.setProgreso(100);
    	} catch (RecuperacionRegistroException ex) {
			progreso.setProgreso(100);
			if (!progresoActualitzacion.isEmpty()) {
				progresoActualitzacion.get(entidadId).setError(true);
    			progresoActualitzacion.get(entidadId).setErrorMsg(ex.getMessage());
    			progresoActualitzacion.get(entidadId).setProgreso(100);
			}
		} catch (Exception | I18NException ex) {
    		progresoActualitzacion.get(entidadId).setError(true);
    		progresoActualitzacion.get(entidadId).setErrorMsg(ex.getMessage());
    		progresoActualitzacion.get(entidadId).setProgreso(100);
    		
    		log.error("Error en la búsqueda de registros SIR [fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", entidadId=" + entidadId + " ]");
    		ex.printStackTrace();
    		enviarEmailErrorSirRecibidoBusqueda(entidad);
			throw ex;
		}
    	
    	return total;
    }
    
    @Override
    public RegistroSir crearRegistroSir(FicheroIntercambio ficheroIntercambio) throws Exception{

        RegistroSir registroSir = transformarFicheroIntercambio(ficheroIntercambio);
        registroSir.setEstado(EstadoRegistroSir.ENVIADO_PENDIENTE_CONFIRMACION);

        try{

            // En caso de recepción, le asignamos la entidad a la que va dirigida
            if(registroSir.getEntidad() == null){

                Entidad entidad;
                if(multiEntidadEjb.isMultiEntidad()) {
                    entidad = new Entidad(oficinaEjb.obtenerEntidadMultiEntidad(registroSir.getCodigoEntidadRegistral()));
                }else{
                    entidad = new Entidad(oficinaEjb.obtenerEntidad(registroSir.getCodigoEntidadRegistral()));
                }
                registroSir.setEntidad(entidad);
            }

            registroSir = persist(registroSir);

            // Guardamos los Interesados
            if(registroSir.getInteresados() != null && registroSir.getInteresados().size() > 0){
                for(InteresadoSir interesadoSir: registroSir.getInteresados()){
                    interesadoSir.setRegistroSir(registroSir);

                    interesadoSirEjb.guardarInteresadoSir(interesadoSir);
                }
            }

            // Guardamos los Anexos
            if(registroSir.getAnexos() != null && registroSir.getAnexos().size() > 0){
                for(AnexoSir anexoSir: registroSir.getAnexos()){
                    anexoSir.setRegistroSir(registroSir);

                    anexoSirEjb.persist(anexoSir);
                }
            }
            em.flush();

            // Creamos la TrazabilidadSir
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_RECEPCION);
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
            trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
            trazabilidadSir.setAplicacion(registroSir.getAplicacion());
            trazabilidadSir.setNombreUsuario(registroSir.getNombreUsuario());
            trazabilidadSir.setContactoUsuario(registroSir.getContactoUsuario());
            trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
            trazabilidadSirEjb.persist(trazabilidadSir);

        }catch (Exception e){
            log.info("Error al crear el RegistroSir:");
            e.printStackTrace();
            for(AnexoSir anexoSir: registroSir.getAnexos()){
                ArchivoManager am = new ArchivoManager(anexoSir.getAnexo(),archivoEjb);
                am.processErrorArchivosWithoutThrowException();
                log.info("Eliminamos los posibles archivos creados: " + anexoSir.getAnexo().getId());
            }
            throw e;
        }

        return registroSir;
    }

    @Override
    public void eliminarRegistroSir(RegistroSir registroSir) throws Exception{

        List<TrazabilidadSir> trazabilidades = trazabilidadSirEjb.getByRegistroSir(registroSir.getId());

        // Eliminamos sus trazabilidades
        for (TrazabilidadSir trazabilidadSir :trazabilidades) {
            trazabilidadSirEjb.remove(trazabilidadSir);
        }

        // Copiamos los id de los anexos sir a eliminar
        List<Long> idsAnexosSir = new ArrayList<>();
        for(AnexoSir anexoSir: registroSir.getAnexos()){
            idsAnexosSir.add(anexoSir.getAnexo().getId());
        }

        // Eliminamos el Anexo Sir
        remove(registroSir);

        // Eliminamos los archivos
        for(Long idAnexoSir: idsAnexosSir){
            FileSystemManager.eliminarArchivo(idAnexoSir);
        }

    }

    @Override
    public void marcarEliminado(RegistroSir registroSir, UsuarioEntidad usuario, String observaciones) throws Exception{

        // Creamos la TrazabilidadSir
        TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_ELIMINAR);
        trazabilidadSir.setRegistroSir(registroSir);
        trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
        trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
        trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
        trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
        trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
        trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
        trazabilidadSir.setContactoUsuario(usuario.getUsuario().getEmail());
        trazabilidadSir.setObservaciones(observaciones);
        trazabilidadSirEjb.persist(trazabilidadSir);

//        modificarEstado(registroSir.getId(),EstadoRegistroSir.ELIMINADO);
    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSir registroSir, String oficinaSir, String estado, String entidad) throws Exception{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select registroSir from RegistroSir as registroSir ");

        if(es.caib.regweb3.utils.StringUtils.isNotEmpty(oficinaSir)){
            where.add(" (registroSir.codigoEntidadRegistral = :oficinaSir) "); parametros.put("oficinaSir",oficinaSir);
        }

        if(es.caib.regweb3.utils.StringUtils.isNotEmpty(entidad)){
            where.add(" (registroSir.entidad.codigoDir3 = :entidad) "); parametros.put("entidad",entidad);
        }

        if (registroSir.getResumen() != null && registroSir.getResumen().length() > 0) {
            where.add(DataBaseUtils.like("registroSir.resumen", "resumen", parametros, registroSir.getResumen()));
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getIdentificadorIntercambio())) {
            where.add(DataBaseUtils.like("registroSir.identificadorIntercambio", "identificadorIntercambio", parametros, registroSir.getIdentificadorIntercambio()));
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getNumeroRegistro())) {
            where.add(DataBaseUtils.like("registroSir.numeroRegistro", "numeroRegistro", parametros, registroSir.getNumeroRegistro()));
        }

        if (StringUtils.isNotEmpty(estado)) {
            where.add(" registroSir.estado = :estado "); parametros.put("estado", EstadoRegistroSir.getEstadoRegistroSir(estado));
        }

        if (registroSir.getTipoRegistro() != null) {
            where.add(" registroSir.tipoRegistro = :tipoRegistro "); parametros.put("tipoRegistro", registroSir.getTipoRegistro());
        }

        if (es.caib.regweb3.utils.StringUtils.isNotEmpty(registroSir.getAplicacion())) {
            where.add(DataBaseUtils.like("registroSir.aplicacion", "aplicacion", parametros, registroSir.getAplicacion()));
        }

        // Intervalo fechas
        where.add(" (registroSir.fechaRecepcion >= :fechaInicio  "); parametros.put("fechaInicio", fechaInicio);
        where.add(" registroSir.fechaRecepcion <= :fechaFin) "); parametros.put("fechaFin", fechaFin);

        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            q2 = em.createQuery(query.toString().replaceAll("Select registroSir from RegistroSir as registroSir ", "Select count(registroSir.id) from RegistroSir as registroSir "));
            query.append(" order by registroSir.fechaRecepcion desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select registroSir from RegistroSir as registroSir ", "Select count(registroSir.id) from RegistroSir as registroSir "));
            query.append("order by registroSir.fechaRecepcion desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public Paginacion getRegistrosEstado(Integer pageNumber, String oficinaSir, String estado) throws Exception{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select registroSir from RegistroSir as registroSir ");

        where.add(" (registroSir.codigoEntidadRegistral = :oficinaSir) "); parametros.put("oficinaSir",oficinaSir);

        if (StringUtils.isNotEmpty(estado)) {
            where.add(" registroSir.estado = :estado "); parametros.put("estado", EstadoRegistroSir.getEstadoRegistroSir(estado));
        }

        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            q2 = em.createQuery(query.toString().replaceAll("Select registroSir from RegistroSir as registroSir ", "Select count(registroSir.id) from RegistroSir as registroSir "));
            query.append(" order by registroSir.fechaRecepcion");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select registroSir from RegistroSir as registroSir ", "Select count(registroSir.id) from RegistroSir as registroSir "));
            query.append("order by registroSir.fechaRecepcion");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSir> getUltimosPendientesProcesar(String oficinaSir, Integer total) throws Exception{

        Query q = em.createQuery("Select r.id, r.decodificacionEntidadRegistralOrigen, r.fechaRecepcion, r.resumen, r.documentacionFisica from RegistroSir as r " +
                "where r.codigoEntidadRegistral = :oficinaSir and r.estado = :idEstado " +
                "order by r.fechaRecepcion");

        q.setMaxResults(total);
        q.setParameter("oficinaSir", oficinaSir);
        q.setParameter("idEstado", EstadoRegistroSir.RECIBIDO_PENDIENTE_CONFIRMACION);
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroSir> registros = new ArrayList<RegistroSir>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroSir registroSir = new RegistroSir();
            registroSir.setId((Long) object[0]);
            registroSir.setDecodificacionEntidadRegistralOrigen((String) object[1]);
            registroSir.setFechaRecepcion((Date) object[2]);
            registroSir.setResumen((String) object[3]);
            registroSir.setDocumentacionFisica((String) object[4]);

            registros.add(registroSir);
        }

        return registros;
    }



    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getPendientesProcesarCount(String oficinaSir) throws Exception{

        Query q = em.createQuery("Select count(registroSir.id) from RegistroSir as registroSir " +
                "where registroSir.codigoEntidadRegistral = :oficinaSir and registroSir.estado = :idEstado " +
                "and registroSir.documentacionFisica = :no_acompanya");

        q.setParameter("oficinaSir", oficinaSir);
        q.setParameter("idEstado", EstadoRegistroSir.RECIBIDO_PENDIENTE_CONFIRMACION);
        q.setParameter("no_acompanya", RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC.toString());
        q.setHint("org.hibernate.readOnly", true);

        return  (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void modificarEstado(Long idRegistroSir, EstadoRegistroSir estado) throws Exception {

        Query q = em.createQuery("update RegistroSir set estado=:estado where id = :idRegistroSir");
        q.setParameter("estado", estado);
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<RegistroSir> registros = em.createQuery("Select a from RegistroSir as a where a.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (RegistroSir registroSir : registros) {
            List<AnexoSir> anexos = registroSir.getAnexos();
            remove(registroSir);

            for (AnexoSir anexoSir : anexos) {
                FileSystemManager.eliminarArchivo(anexoSir.getAnexo().getId());
            }

        }
        em.flush();

        return registros.size();

    }

    /**
     * Crea un RegistroSir, a partir del FicheroIntercambio.
     *
     * @return Información del registroSir registral.
     */
    @Override
    public RegistroSir transformarFicheroIntercambio(FicheroIntercambio ficheroIntercambio)throws Exception{

        final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

        RegistroSir registroSir = null;

        if (ficheroIntercambio.getFicheroIntercambio() != null) {

            Fichero_Intercambio_SICRES_3 fichero_intercambio_sicres_3 = ficheroIntercambio.getFicheroIntercambio();

            registroSir = new RegistroSir();
            registroSir.setFechaRecepcion(new Date());

            // Segmento De_Origen_o_Remitente
            De_Origen_o_Remitente de_Origen_o_Remitente = fichero_intercambio_sicres_3.getDe_Origen_o_Remitente();
            if (de_Origen_o_Remitente != null) {

                registroSir.setCodigoEntidadRegistralOrigen(de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen());

                if (StringUtils.isNotEmpty(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen())) {
                    registroSir.setDecodificacionEntidadRegistralOrigen(de_Origen_o_Remitente.getDecodificacion_Entidad_Registral_Origen());
                } else {
                    registroSir.setDecodificacionEntidadRegistralOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(),de_Origen_o_Remitente.getCodigo_Entidad_Registral_Origen(), RegwebConstantes.OFICINA));
                }

                registroSir.setCodigoUnidadTramitacionOrigen(de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen());

                if (StringUtils.isNotEmpty(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen())) {
                    registroSir.setDecodificacionUnidadTramitacionOrigen(de_Origen_o_Remitente.getDecodificacion_Unidad_Tramitacion_Origen());
                } else {
                    registroSir.setDecodificacionUnidadTramitacionOrigen(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(),de_Origen_o_Remitente.getCodigo_Unidad_Tramitacion_Origen(), RegwebConstantes.UNIDAD));
                }


                registroSir.setNumeroRegistro(de_Origen_o_Remitente.getNumero_Registro_Entrada());
                registroSir.setTimestampRegistro(Base64.encodeBase64String(de_Origen_o_Remitente.getTimestamp_Entrada()));

                String fechaRegistro = de_Origen_o_Remitente.getFecha_Hora_Entrada();
                if (StringUtils.isNotBlank(fechaRegistro)) {
                    try {
                        registroSir.setFechaRegistro(SDF.parse(fechaRegistro));
                    } catch (ParseException e) {
                        log.error("Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
                        throw new ValidacionException(Errores.ERROR_0037,"Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
                    }
                }
            }

            // Segmento De_Destino
            De_Destino de_Destino = fichero_intercambio_sicres_3.getDe_Destino();
            if (de_Destino != null) {

                registroSir.setCodigoEntidadRegistral(de_Destino.getCodigo_Entidad_Registral_Destino());

                registroSir.setCodigoEntidadRegistralDestino(de_Destino.getCodigo_Entidad_Registral_Destino());
                if (StringUtils.isNotEmpty(de_Destino.getDecodificacion_Entidad_Registral_Destino())) {
                    registroSir.setDecodificacionEntidadRegistralDestino(de_Destino.getDecodificacion_Entidad_Registral_Destino());
                } else {
                    registroSir.setDecodificacionEntidadRegistralDestino(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(),de_Destino.getCodigo_Entidad_Registral_Destino(), RegwebConstantes.OFICINA));
                }

                if (StringUtils.isNotEmpty(de_Destino.getCodigo_Unidad_Tramitacion_Destino())) {
                    registroSir.setCodigoUnidadTramitacionDestino(de_Destino.getCodigo_Unidad_Tramitacion_Destino());
                    if (StringUtils.isNotEmpty(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino())) {
                        registroSir.setDecodificacionUnidadTramitacionDestino(de_Destino.getDecodificacion_Unidad_Tramitacion_Destino());
                    } else {
                        registroSir.setDecodificacionUnidadTramitacionDestino(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(),de_Destino.getCodigo_Unidad_Tramitacion_Destino(), RegwebConstantes.UNIDAD));
                    }
                }

            }

            // Segmento De_Asunto de_Asunto
            De_Asunto de_Asunto = fichero_intercambio_sicres_3.getDe_Asunto();
            if (de_Asunto != null) {
                registroSir.setResumen(de_Asunto.getResumen());
                registroSir.setCodigoAsunto(de_Asunto.getCodigo_Asunto_Segun_Destino());
                registroSir.setReferenciaExterna(de_Asunto.getReferencia_Externa());
                registroSir.setNumeroExpediente(de_Asunto.getNumero_Expediente());
            }

            // Segmento De_Internos_Control
            De_Internos_Control de_Internos_Control = fichero_intercambio_sicres_3.getDe_Internos_Control();
            if (de_Internos_Control != null) {

                registroSir.setIdentificadorIntercambio(de_Internos_Control.getIdentificador_Intercambio());
                registroSir.setAplicacion(de_Internos_Control.getAplicacion_Version_Emisora());
                registroSir.setTipoAnotacion(ficheroIntercambio.getTipoAnotacion());
                registroSir.setDecodificacionTipoAnotacion(de_Internos_Control.getDescripcion_Tipo_Anotacion());
                registroSir.setNumeroTransporte(de_Internos_Control.getNumero_Transporte_Entrada());
                registroSir.setNombreUsuario(de_Internos_Control.getNombre_Usuario());
                registroSir.setContactoUsuario(de_Internos_Control.getContacto_Usuario());
                registroSir.setObservacionesApunte(de_Internos_Control.getObservaciones_Apunte());

                registroSir.setCodigoEntidadRegistralInicio(de_Internos_Control.getCodigo_Entidad_Registral_Inicio());
                if (StringUtils.isNotEmpty(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio())) {
                    registroSir.setDecodificacionEntidadRegistralInicio(de_Internos_Control.getDecodificacion_Entidad_Registral_Inicio());
                } else {
                    registroSir.setDecodificacionEntidadRegistralInicio(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(),de_Internos_Control.getCodigo_Entidad_Registral_Inicio(), RegwebConstantes.OFICINA));
                }


                // Tipo de transporte
                String tipoTransporte = de_Internos_Control.getTipo_Transporte_Entrada();
                if (StringUtils.isNotBlank(tipoTransporte)) {
                    registroSir.setTipoTransporte(TipoTransporte.getTipoTransporteValue(tipoTransporte));
                }

                // Tipo de registro
                Tipo_RegistroType tipo_Registro = de_Internos_Control.getTipo_Registro();
                if ((tipo_Registro != null) && StringUtils.isNotBlank(tipo_Registro.value())) {
                    registroSir.setTipoRegistro(TipoRegistro.getTipoRegistro(tipo_Registro.value()));
                }

                // Documentación física
                Documentacion_FisicaType documentacion_Fisica = de_Internos_Control.getDocumentacion_Fisica();
                if ((documentacion_Fisica != null) && StringUtils.isNotBlank(documentacion_Fisica.value())) {
                    registroSir.setDocumentacionFisica(DocumentacionFisica.getDocumentacionFisicaValue(documentacion_Fisica.value()));
                }

                // Indicador de prueba
                Indicador_PruebaType indicadorPrueba = de_Internos_Control.getIndicador_Prueba();
                if ((indicadorPrueba != null) && StringUtils.isNotBlank(indicadorPrueba.value())){
                    registroSir.setIndicadorPrueba(IndicadorPrueba.getIndicadorPrueba(indicadorPrueba.value()));
                }

            }

            // Segmento De_Formulario_Generico
            De_Formulario_Generico de_Formulario_Generico = fichero_intercambio_sicres_3.getDe_Formulario_Generico();
            if (de_Formulario_Generico != null) {
                registroSir.setExpone(de_Formulario_Generico.getExpone());
                registroSir.setSolicita(de_Formulario_Generico.getSolicita());
            }

            // Segmento De_Interesado
            De_Interesado[] de_Interesados = fichero_intercambio_sicres_3.getDe_Interesado();
            if (ArrayUtils.isNotEmpty(de_Interesados)) {
                for (De_Interesado de_Interesado : de_Interesados) {
                    if (de_Interesado != null) {

                        // Si se trata de una Salida y no tiene Interesados
                        if(ficheroIntercambio.getTipoRegistro().equals(TipoRegistro.SALIDA) &&
                                StringUtils.isBlank(de_Interesado.getRazon_Social_Interesado())
                                && (StringUtils.isBlank(de_Interesado.getNombre_Interesado()) && StringUtils.isBlank(de_Interesado.getPrimer_Apellido_Interesado()))){

                            // Creamos uno a partir de la Entidad destino
                            registroSir.getInteresados().add(crearInteresadoJuridico(ficheroIntercambio));

                        }else{

                            InteresadoSir interesado = new InteresadoSir();

                            // Información del interesado
                            interesado.setDocumentoIdentificacionInteresado(de_Interesado.getDocumento_Identificacion_Interesado());
                            interesado.setRazonSocialInteresado(de_Interesado.getRazon_Social_Interesado());
                            interesado.setNombreInteresado(de_Interesado.getNombre_Interesado());
                            interesado.setPrimerApellidoInteresado(de_Interesado.getPrimer_Apellido_Interesado());
                            interesado.setSegundoApellidoInteresado(de_Interesado.getSegundo_Apellido_Interesado());
                            interesado.setCodigoPaisInteresado(de_Interesado.getPais_Interesado());
                            interesado.setCodigoProvinciaInteresado(de_Interesado.getProvincia_Interesado());
                            interesado.setCodigoMunicipioInteresado(de_Interesado.getMunicipio_Interesado());
                            interesado.setDireccionInteresado(de_Interesado.getDireccion_Interesado());
                            interesado.setCodigoPostalInteresado(de_Interesado.getCodigo_Postal_Interesado());
                            interesado.setCorreoElectronicoInteresado(de_Interesado.getCorreo_Electronico_Interesado());
                            interesado.setTelefonoInteresado(de_Interesado.getTelefono_Contacto_Interesado());
                            interesado.setDireccionElectronicaHabilitadaInteresado(de_Interesado.getDireccion_Electronica_Habilitada_Interesado());

                            String tipoDocumento = de_Interesado.getTipo_Documento_Identificacion_Interesado();
                            if (StringUtils.isNotBlank(tipoDocumento)) {
                                interesado.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacionValue(tipoDocumento));
                            }

                            String canalPreferente = de_Interesado.getCanal_Preferente_Comunicacion_Interesado();
                            if (StringUtils.isNotBlank(canalPreferente)) {
                                interesado.setCanalPreferenteComunicacionInteresado(CanalNotificacion.getCanalNotificacionValue(canalPreferente));
                            }

                            // Información del representante
                            interesado.setDocumentoIdentificacionRepresentante(de_Interesado.getDocumento_Identificacion_Representante());
                            interesado.setRazonSocialRepresentante(de_Interesado.getRazon_Social_Representante());
                            interesado.setNombreRepresentante(de_Interesado.getNombre_Representante());
                            interesado.setPrimerApellidoRepresentante(de_Interesado.getPrimer_Apellido_Representante());
                            interesado.setSegundoApellidoRepresentante(de_Interesado.getSegundo_Apellido_Representante());
                            interesado.setCodigoPaisRepresentante(de_Interesado.getPais_Representante());
                            interesado.setCodigoProvinciaRepresentante(de_Interesado.getProvincia_Representante());
                            interesado.setCodigoMunicipioRepresentante(de_Interesado.getMunicipio_Representante());
                            interesado.setDireccionRepresentante(de_Interesado.getDireccion_Representante());
                            interesado.setCodigoPostalRepresentante(de_Interesado.getCodigo_Postal_Representante());
                            interesado.setCorreoElectronicoRepresentante(de_Interesado.getCorreo_Electronico_Representante());
                            interesado.setTelefonoRepresentante(de_Interesado.getTelefono_Contacto_Representante());
                            interesado.setDireccionElectronicaHabilitadaRepresentante(de_Interesado.getDireccion_Electronica_Habilitada_Representante());

                            tipoDocumento = de_Interesado.getTipo_Documento_Identificacion_Representante();
                            if (StringUtils.isNotBlank(tipoDocumento)) {
                                interesado.setTipoDocumentoIdentificacionRepresentante(TipoDocumentoIdentificacion.getTipoDocumentoIdentificacionValue(tipoDocumento));
                            }

                            canalPreferente = de_Interesado.getCanal_Preferente_Comunicacion_Representante();
                            if (StringUtils.isNotBlank(canalPreferente)) {
                                interesado.setCanalPreferenteComunicacionRepresentante(CanalNotificacion.getCanalNotificacionValue(canalPreferente));
                            }

                            interesado.setObservaciones(de_Interesado.getObservaciones());

                            registroSir.getInteresados().add(interesado);
                        }

                    }
                }
            }

            // Segmento De_Anexos
            De_Anexo[] de_Anexos = fichero_intercambio_sicres_3.getDe_Anexo();
            if (ArrayUtils.isNotEmpty(de_Anexos)) {
                for (De_Anexo de_Anexo : de_Anexos) {
                    if (de_Anexo != null) {
                        AnexoSir anexo = new AnexoSir();

                        anexo.setNombreFichero(es.caib.regweb3.utils.StringUtils.sustituirCaracteresProhibidosArxiu(de_Anexo.getNombre_Fichero_Anexado(),'_'));
                        anexo.setIdentificadorFichero(de_Anexo.getIdentificador_Fichero());
                        anexo.setIdentificadorDocumentoFirmado(de_Anexo.getIdentificador_Documento_Firmado());
                        anexo.setCertificado(Base64.encodeBase64String(de_Anexo.getCertificado()));
                        anexo.setFirma(Base64.encodeBase64String(de_Anexo.getFirma_Documento()));
                        anexo.setTimestamp(Base64.encodeBase64String(de_Anexo.getTimeStamp()));
                        anexo.setValidacionOCSPCertificado(Base64.encodeBase64String(de_Anexo.getValidacion_OCSP_Certificado()));
                        anexo.setHash(Base64.encodeBase64String(de_Anexo.getHash()));
                        //Si el tipo mime es null, se obtiene de la extensión del fichero
//                        if (de_Anexo.getTipo_MIME() == null || de_Anexo.getTipo_MIME().isEmpty()) {
//                            String mime = MimeTypeUtils.getMimeTypeFileName(de_Anexo.getNombre_Fichero_Anexado());
//                            if(mime.length() <= 20){
//                                anexo.setTipoMIME(mime);
//                            }
//                        } else {
//                            anexo.setTipoMIME(de_Anexo.getTipo_MIME());
//                        }

                        ArchivoManager am = null;
                        try {
                            am = new ArchivoManager(archivoEjb, de_Anexo.getNombre_Fichero_Anexado(), anexo.getTipoMIME(), de_Anexo.getAnexo());
                            anexo.setAnexo(am.prePersist());
                        } catch (Exception e) {
                            log.info("Error al crear el Anexo en el sistema de archivos", e);
                            am.processError();
                            throw new ServiceException(Errores.ERROR_0065,e);
                        }

                        anexo.setObservaciones(de_Anexo.getObservaciones());

                        String validezDocumento = de_Anexo.getValidez_Documento();
                        if (StringUtils.isNotBlank(validezDocumento)) {
                            anexo.setValidezDocumento(ValidezDocumento.getValidezDocumentoValue(validezDocumento));
                        }

                        String tipoDocumento = de_Anexo.getTipo_Documento();
                        if (StringUtils.isNotBlank(tipoDocumento)) {
                            anexo.setTipoDocumento(TipoDocumento.getTipoDocumentoValue(tipoDocumento));
                        }

                        registroSir.getAnexos().add(anexo);
                    }
                }
            }
        }

        return registroSir;

    }

    /**
     * Transforma un {@link RegistroEntrada} en un {@link RegistroSir}
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroSir transformarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException {

        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        RegistroSir registroSir = new RegistroSir();

        registroSir.setIndicadorPrueba(IndicadorPrueba.NORMAL);
        registroSir.setEntidad(registroEntrada.getOficina().getOrganismoResponsable().getEntidad());

        // Segmento De_Origen_O_Remitente
        registroSir.setCodigoEntidadRegistralOrigen(registroEntrada.getOficina().getCodigo());
        registroSir.setDecodificacionEntidadRegistralOrigen(registroEntrada.getOficina().getDenominacion());
        registroSir.setNumeroRegistro(registroEntrada.getNumeroRegistroFormateado());
        registroSir.setFechaRegistro(registroEntrada.getFecha());
        registroSir.setCodigoUnidadTramitacionOrigen(null);
        registroSir.setDecodificacionUnidadTramitacionOrigen(null);
//        registroSir.setCodigoUnidadTramitacionOrigen(registroEntrada.getOficina().getOrganismoResponsable().getCodigo());
//        registroSir.setDecodificacionUnidadTramitacionOrigen(registroEntrada.getOficina().getOrganismoResponsable().getDenominacion());

        // Segmento De_Destino
        registroSir.setCodigoEntidadRegistral(registroDetalle.getCodigoEntidadRegistralDestino());
        registroSir.setCodigoEntidadRegistralDestino(registroDetalle.getCodigoEntidadRegistralDestino());
        registroSir.setDecodificacionEntidadRegistralDestino(registroDetalle.getDecodificacionEntidadRegistralDestino());
        registroSir.setCodigoUnidadTramitacionDestino(registroEntrada.getDestinoExternoCodigo());
        registroSir.setDecodificacionUnidadTramitacionDestino(registroEntrada.getDestinoExternoDenominacion());

        // Segmento De_Asunto
        registroSir.setResumen(registroDetalle.getExtracto());
        if(registroEntrada.getDestino() != null){
            TraduccionCodigoAsunto tra = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO);
            registroSir.setCodigoAsunto(tra.getNombre());
        }
        registroSir.setReferenciaExterna(registroDetalle.getReferenciaExterna());
        registroSir.setNumeroExpediente(registroDetalle.getExpediente());

        // Segmento De_Internos_Control
        registroSir.setTipoTransporte(CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        registroSir.setNumeroTransporte(registroDetalle.getNumeroTransporte());
        registroSir.setNombreUsuario(registroEntrada.getUsuario().getNombreCompleto());
        registroSir.setContactoUsuario(registroEntrada.getUsuario().getUsuario().getEmail());
        registroSir.setIdentificadorIntercambio(registroDetalle.getIdentificadorIntercambio());
        registroSir.setAplicacion(registroDetalle.getAplicacion());
        registroSir.setTipoAnotacion(registroDetalle.getTipoAnotacion());
        registroSir.setDecodificacionTipoAnotacion(registroDetalle.getDecodificacionTipoAnotacion());
        registroSir.setTipoRegistro(TipoRegistro.ENTRADA);
        registroSir.setDocumentacionFisica(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        registroSir.setObservacionesApunte(registroDetalle.getObservaciones());
        registroSir.setCodigoEntidadRegistralInicio(obtenerCodigoOficinaOrigen(registroDetalle,registroEntrada.getOficina().getCodigo()));
        registroSir.setDecodificacionEntidadRegistralInicio(obtenerDenominacionOficinaOrigen(registroDetalle, registroEntrada.getOficina().getDenominacion()));

        // Segmento De_Formulario_Genérico
        registroSir.setExpone(registroDetalle.getExpone());
        registroSir.setSolicita(registroDetalle.getSolicita());

        // Segmento De_Interesados
        registroSir.setInteresados(procesarInteresadosSir(registroDetalle.getInteresados()));

        // Segmento De_Anexos

        // Firmar anexos
        Locale locale = new Locale(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(registroEntrada.getUsuario().getUsuario().getIdioma()));

        List<AnexoFull> anexosfirmados = signatureServerEjb.firmarAnexosEnvioSir(registroDetalle.getAnexosFull(),registroEntrada.getUsuario().getEntidad().getId(),locale,true, registroEntrada.getNumeroRegistroFormateado());

        registroSir.setAnexos(transformarAnexosSir(anexosfirmados, registroSir.getIdentificadorIntercambio()));

        return registroSir;
    }

    /**
     * Transforma un {@link RegistroSalida} en un {@link RegistroSir}
     * @param registroSalida
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroSir transformarRegistroSalida(RegistroSalida registroSalida) throws Exception, I18NException{

        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        RegistroSir registroSir = new RegistroSir();

        registroSir.setIndicadorPrueba(IndicadorPrueba.NORMAL); // todo Modificar cuando entremos en Producción
        registroSir.setEntidad(registroSalida.getOficina().getOrganismoResponsable().getEntidad());

        // Segmento De_Origen_O_Remitente
        registroSir.setCodigoEntidadRegistralOrigen(registroSalida.getOficina().getCodigo());
        registroSir.setDecodificacionEntidadRegistralOrigen(registroSalida.getOficina().getDenominacion());
        registroSir.setNumeroRegistro(registroSalida.getNumeroRegistroFormateado());
        registroSir.setFechaRegistro(registroSalida.getFecha());
        registroSir.setCodigoUnidadTramitacionOrigen(registroSalida.getOficina().getOrganismoResponsable().getCodigo());
        registroSir.setDecodificacionUnidadTramitacionOrigen(registroSalida.getOficina().getOrganismoResponsable().getDenominacion());

        // Segmento De_Destino
        registroSir.setCodigoEntidadRegistral(registroDetalle.getCodigoEntidadRegistralDestino());
        registroSir.setCodigoEntidadRegistralDestino(registroDetalle.getCodigoEntidadRegistralDestino());
        registroSir.setDecodificacionEntidadRegistralDestino(registroDetalle.getDecodificacionEntidadRegistralDestino());
        registroSir.setCodigoUnidadTramitacionDestino(obtenerCodigoUnidadTramitacionDestino(registroDetalle));
        String destinoExternoDecodificacion = obtenerDenominacionUnidadTramitacionDestino(registroDetalle);
        if(es.caib.regweb3.utils.StringUtils.isNotEmpty(destinoExternoDecodificacion)) {
            registroSir.setDecodificacionUnidadTramitacionDestino(destinoExternoDecodificacion);
        }else{
            registroSir.setDecodificacionUnidadTramitacionDestino(null);
        }

        // Segmento De_Asunto
        registroSir.setResumen(registroDetalle.getExtracto());
        /*if(registroSalida.getDestino() != null){ //todo Revisar
            TraduccionCodigoAsunto tra = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO);
            registroSir.setCodigoAsunto(tra.getNombre());
        }*/
        registroSir.setReferenciaExterna(registroDetalle.getReferenciaExterna());
        registroSir.setNumeroExpediente(registroDetalle.getExpediente());

        // Segmento De_Internos_Control
        registroSir.setTipoTransporte(CODIGO_SICRES_BY_TRANSPORTE.get(registroDetalle.getTransporte()));
        registroSir.setNumeroTransporte(registroDetalle.getNumeroTransporte());
        registroSir.setNombreUsuario(registroSalida.getUsuario().getNombreCompleto());
        registroSir.setContactoUsuario(registroSalida.getUsuario().getUsuario().getEmail());
        registroSir.setIdentificadorIntercambio(registroDetalle.getIdentificadorIntercambio());
        registroSir.setAplicacion(registroDetalle.getAplicacion());
        registroSir.setTipoAnotacion(registroDetalle.getTipoAnotacion());
        registroSir.setDecodificacionTipoAnotacion(registroDetalle.getDecodificacionTipoAnotacion());
        registroSir.setTipoRegistro(TipoRegistro.SALIDA);
        registroSir.setDocumentacionFisica(String.valueOf(registroDetalle.getTipoDocumentacionFisica()));
        registroSir.setObservacionesApunte(registroDetalle.getObservaciones());
        registroSir.setCodigoEntidadRegistralInicio(obtenerCodigoOficinaOrigen(registroDetalle, registroSalida.getOficina().getCodigo()));
        registroSir.setDecodificacionEntidadRegistralInicio(obtenerDenominacionOficinaOrigen(registroDetalle, registroSalida.getOficina().getDenominacion()));

        // Segmento De_Formulario_Genérico
        registroSir.setExpone(registroDetalle.getExpone());
        registroSir.setSolicita(registroDetalle.getSolicita());

        // Segmento De_Interesados: Irá siempre vacio, porque el destinatario va informado en el segmento DeDestino
        // Necesario para consultar el id intercambio de GEISER
        registroSir.setInteresados(procesarInteresadosSir(registroDetalle.getInteresados()));

        // Segmento De_Anexos

        // Firmar anexos
        Locale locale = new Locale(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(registroSalida.getUsuario().getUsuario().getIdioma()));

        List<AnexoFull> anexosfirmados = signatureServerEjb.firmarAnexosEnvioSir(registroDetalle.getAnexosFull(),registroSalida.getUsuario().getEntidad().getId(),locale,true, registroSalida.getNumeroRegistroFormateado());

        registroSir.setAnexos(transformarAnexosSir(anexosfirmados, registroSir.getIdentificadorIntercambio()));

        return registroSir;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Long> getEnviadosSinAck(Long idEntidad) throws Exception{

        Query q = em.createQuery("Select registroSir.id from RegistroSir as registroSir " +
                "where registroSir.entidad.id = :idEntidad and (registroSir.estado = :reenviado or registroSir.estado = :rechazado) " +
                "and registroSir.numeroReintentos < :maxReintentos order by id");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("reenviado", EstadoRegistroSir.REENVIADO);
//        q.setParameter("rechazado", EstadoRegistroSir.RECHAZADO);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));
        q.setHint("org.hibernate.readOnly", true);
        q.setMaxResults(20);

        return  q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Long> getEnviadosConError(Long idEntidad) throws Exception{

        Query q = em.createQuery("Select registroSir.id from RegistroSir as registroSir " +
                "where registroSir.entidad.id = :idEntidad and registroSir.estado = :reenviado or registroSir.estado = :rechazado " +
                "and (registroSir.codigoError = '0039' or registroSir.codigoError = '0046' or registroSir.codigoError = '0057' or  registroSir.codigoError = '0058') " +
                "and registroSir.numeroReintentos < :maxReintentos order by id");

        q.setParameter("idEntidad", idEntidad);
//        q.setParameter("reenviado", EstadoRegistroSir.REENVIADO_Y_ERROR);
//        q.setParameter("rechazado", EstadoRegistroSir.RECHAZADO_Y_ERROR);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));
        q.setHint("org.hibernate.readOnly", true);
        q.setMaxResults(20);

        return  q.getResultList();

    }

    @Override
    public void reiniciarIntentos(Long idRegistroSir) throws Exception {

        Query q = em.createQuery("update RegistroSir set numeroReintentos=0 where id = :idRegistroSir");
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();

    }
    
	@Override
	@SuppressWarnings(value = "unchecked")
	public List<Long> getRegistrosSirPendientes(Long idEntidad, int maxReintentos) throws Exception {
		Query q = em.createQuery("Select registroSir.id from RegistroSir as registroSir "
				+ "where registroSir.entidad.id = :idEntidad "
				+ "and (registroSir.estado = :pendienteEnvio "
				+ "		or registroSir.estado = :pendienteConfirmacion "
				+ "		or registroSir.estado = :pendienteConfirmacionManual "
				+ "		or registroSir.estado = :tramite or registroSir.estado = :proceso "
				+ "		or registroSir.estado = :reenviado) "
				+ "and (registroSir.numeroReintentos is null or registroSir.numeroReintentos < :reintentos) order by id");
		q.setParameter("idEntidad", idEntidad);
		q.setParameter("pendienteEnvio", EstadoRegistroSir.PENDIENTE_ENVIO);
		q.setParameter("pendienteConfirmacion", EstadoRegistroSir.ENVIADO_PENDIENTE_CONFIRMACION);
		q.setParameter("pendienteConfirmacionManual", EstadoRegistroSir.ENVIADO_PENDIENTE_CONFIRMACION_MANUAL);
		q.setParameter("tramite", EstadoRegistroSir.EN_TRAMITE);
		q.setParameter("proceso", EstadoRegistroSir.ENVIO_PROCESO);
		q.setParameter("reenviado", EstadoRegistroSir.REENVIADO);
		q.setParameter("reintentos", maxReintentos);
		
		return q.getResultList();
	}
	
	@Override
	@SuppressWarnings(value = "unchecked")
	public List<Long> getRegistrosSirRecibidosSinId(Long idEntidad) throws Exception {
		Query q = em.createQuery("Select registroSir.id from RegistroSir as registroSir "
				+ "where registroSir.entidad.id = :idEntidad and registroSir.estado = :enviadoConfirmado or registroSir.estado = :recibidoConfirmado "
				+ "or registroSir.estado = :finalizado or registroSir.fechaRecepcion != null order by id");
		q.setParameter("idEntidad", idEntidad);
		q.setParameter("enviadoConfirmado", EstadoRegistroSir.ENVIADO_CONFIRMADO);
		q.setParameter("recibidoConfirmado", EstadoRegistroSir.RECIBIDO_CONFIRMADO);
		q.setParameter("finalizado", EstadoRegistroSir.FINALIZADO);
		return q.getResultList();
	}

//	caso GEISER: identificador se obtiene de GEISER
	@Override
    public void actualizarReintentosRegistroSir(Long idRegistroSir) throws Exception {
        Query q = em.createQuery("update RegistroSir set numeroReintentos = numeroReintentos+1 where id = :idRegistroSir");
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();
    }
	
	@Override
    @SuppressWarnings(value = "unchecked")
    public void actualizarIdentificadorIntercambio(Long idRegistroSir, String identificadorIntercambio) throws Exception {

        Query q = em.createQuery("update RegistroSir set identificadorIntercambio=:identificadorIntercambio where id = :idRegistroSir");
        q.setParameter("identificadorIntercambio", identificadorIntercambio);
        q.setParameter("idRegistroSir", idRegistroSir);
        q.executeUpdate();

    }
	
    /**
     * Transforma una Lista de {@link InteresadoSir} en una Lista de {@link Interesado}
     * @param interesados
     * @return
     * @throws Exception
     */
    private List<InteresadoSir> procesarInteresadosSir(List<Interesado> interesados) throws Exception{
        List<InteresadoSir> interesadosSir = new ArrayList<InteresadoSir>();

        for (Interesado interesado : interesados) {
            InteresadoSir interesadoSir;

            if (!interesado.getIsRepresentante()){
                interesadoSir = transformarInteresadoSir(interesado, interesado.getRepresentante());
                interesadosSir.add(interesadoSir);
            }


        }
        return interesadosSir;
    }

    /**
     * Transforma un {@link InteresadoSir} en un {@link Interesado}
     * @param interesado
     * @param representante
     * @return Interesado de tipo {@link Interesado}
     * @throws Exception
     */
    private InteresadoSir transformarInteresadoSir(Interesado interesado, Interesado representante) throws Exception{

        InteresadoSir interesadoSir = new InteresadoSir();

        if (interesado.getTipoDocumentoIdentificacion() != null) {
            interesadoSir.setTipoDocumentoIdentificacionInteresado(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion())));
        }

        if (StringUtils.isNotEmpty(interesado.getDocumento())) {
            interesadoSir.setDocumentoIdentificacionInteresado(interesado.getDocumento());
        }

        if (StringUtils.isNotEmpty(interesado.getRazonSocial())) {
            interesadoSir.setRazonSocialInteresado(interesado.getRazonSocial());
        }

        if (StringUtils.isNotEmpty(interesado.getNombre())) {
            interesadoSir.setNombreInteresado(interesado.getNombre());
        }

        if (StringUtils.isNotEmpty(interesado.getApellido1())) {
            interesadoSir.setPrimerApellidoInteresado(interesado.getApellido1());
        }

        if (StringUtils.isNotEmpty(interesado.getApellido2())) {
            interesadoSir.setSegundoApellidoInteresado(interesado.getApellido2());
        }

        if(interesado.getPais() != null){
            String codigoPais = interesado.getPais().getCodigoPais().toString();

            if(codigoPais.length() == 1){
                codigoPais = "00"+codigoPais;
            }else if(codigoPais.length() == 2){
                codigoPais = "0"+codigoPais;
            }

            interesadoSir.setCodigoPaisInteresado(codigoPais);
        }

        if(interesado.getProvincia() != null){
            String codigoProvincia = interesado.getProvincia().getCodigoProvincia().toString();

            if(codigoProvincia.length() == 1){ // Le añadimos '0' delante si el código es del 1-9
                codigoProvincia = "0"+codigoProvincia;
            }
            interesadoSir.setCodigoProvinciaInteresado(codigoProvincia);
        }

        if(interesado.getLocalidad() != null){
            String codigoMunicipio = interesado.getLocalidad().getCodigoLocalidad().toString();

            if(codigoMunicipio.length() == 1){
                codigoMunicipio = "000"+codigoMunicipio;
            }else if(codigoMunicipio.length() == 2){
                codigoMunicipio = "00"+codigoMunicipio;
            }else if(codigoMunicipio.length() == 3){
                codigoMunicipio = "0"+codigoMunicipio;
            }

            interesadoSir.setCodigoMunicipioInteresado(codigoMunicipio);
        }

        if (StringUtils.isNotEmpty(interesado.getDireccion())) {
            interesadoSir.setDireccionInteresado(interesado.getDireccion());
        }

        if (StringUtils.isNotEmpty(interesado.getCp())) {
            interesadoSir.setCodigoPostalInteresado(interesado.getCp());
        }

        if (StringUtils.isNotEmpty(interesado.getEmail())) {
            interesadoSir.setCorreoElectronicoInteresado(interesado.getEmail());
        }

        if (StringUtils.isNotEmpty(interesado.getTelefono())) {
            interesadoSir.setTelefonoInteresado(interesado.getTelefono());
        }

        if (StringUtils.isNotEmpty(interesado.getDireccionElectronica())) {
            interesadoSir.setDireccionElectronicaHabilitadaInteresado(interesado.getDireccionElectronica());
        }

        if (interesado.getCanal() != null) {
            interesadoSir.setCanalPreferenteComunicacionInteresado(CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
        }

        if (StringUtils.isNotEmpty(interesado.getObservaciones())) {
            interesadoSir.setObservaciones(interesado.getObservaciones());
        }

        // Si tiene representante, también lo transformamos
        if(representante != null){

            if (representante.getTipoDocumentoIdentificacion() != null) {
                interesadoSir.setTipoDocumentoIdentificacionRepresentante(String.valueOf(CODIGO_NTI_BY_TIPODOCUMENTOID.get(representante.getTipoDocumentoIdentificacion())));
            }

            if (StringUtils.isNotEmpty(representante.getDocumento())) {
                interesadoSir.setDocumentoIdentificacionRepresentante(representante.getDocumento());
            }

            if (StringUtils.isNotEmpty(representante.getRazonSocial())) {
                interesadoSir.setRazonSocialRepresentante(representante.getRazonSocial());
            }

            if (StringUtils.isNotEmpty(representante.getNombre())) {
                interesadoSir.setNombreRepresentante(representante.getNombre());
            }

            if (StringUtils.isNotEmpty(representante.getApellido1())) {
                interesadoSir.setPrimerApellidoRepresentante(representante.getApellido1());
            }

            if (StringUtils.isNotEmpty(representante.getApellido2())) {
                interesadoSir.setSegundoApellidoRepresentante(representante.getApellido2());
            }

            if(representante.getPais() != null){
                String codigoPais = representante.getPais().getCodigoPais().toString();

                if(codigoPais.length() == 1){ // Le añadimos '00' delante si el código es del 1-9
                    codigoPais = "00"+codigoPais;
                }else if(codigoPais.length() == 2){ // Le añadimos '0' delante si el código es hasta 99
                    codigoPais = "0"+codigoPais;
                }

                interesadoSir.setCodigoPaisRepresentante(codigoPais);
            }

            if(representante.getProvincia() != null){

                String codigoProvincia = representante.getProvincia().getCodigoProvincia().toString();

                if(codigoProvincia.length() == 1){
                    codigoProvincia = "0"+codigoProvincia;  // Le añadimos '0' delante si el código es del 1-9
                }

                interesadoSir.setCodigoProvinciaRepresentante(codigoProvincia);
            }

            if(representante.getLocalidad() != null){
                String codigoMunicipio = representante.getLocalidad().getCodigoLocalidad().toString();

                if(codigoMunicipio.length() == 1){
                    codigoMunicipio = "000"+codigoMunicipio; // Le añadimos '000' delante si el código es hasta 1-9
                }else if(codigoMunicipio.length() == 2){ // Le añadimos '00' delante si el código es hasta 99
                    codigoMunicipio = "00"+codigoMunicipio;
                }else if(codigoMunicipio.length() == 3){
                    codigoMunicipio = "0"+codigoMunicipio; // Le añadimos '0' delante si el código es hasta 999
                }

                interesadoSir.setCodigoMunicipioRepresentante(codigoMunicipio);
            }

            if (StringUtils.isNotEmpty(representante.getDireccion())) {
                interesadoSir.setDireccionRepresentante(representante.getDireccion());
            }

            if (StringUtils.isNotEmpty(representante.getCp())) {
                interesadoSir.setCodigoPostalRepresentante(representante.getCp());
            }

            if (StringUtils.isNotEmpty(representante.getEmail())) {
                interesadoSir.setCorreoElectronicoRepresentante(representante.getEmail());
            }

            if (StringUtils.isNotEmpty(representante.getTelefono())) {
                interesadoSir.setTelefonoRepresentante(representante.getTelefono());
            }

            if (StringUtils.isNotEmpty(representante.getDireccionElectronica())) {
                interesadoSir.setDireccionElectronicaHabilitadaRepresentante(representante.getDireccionElectronica());
            }

            if (representante.getCanal() != null) {
                interesadoSir.setCanalPreferenteComunicacionRepresentante(CODIGO_BY_CANALNOTIFICACION.get(representante.getCanal()));
            }

        }

        return interesadoSir;

    }

    /**
     *
     * @param anexosFull
     * @param identificadorIntercambio
     * @return
     */
    private List<AnexoSir> transformarAnexosSir(List<AnexoFull> anexosFull, String identificadorIntercambio) throws Exception{

        List<AnexoSir> anexosSir = new ArrayList<AnexoSir>();
        int secuencia = 0;

        for(AnexoFull anexoFull:anexosFull){

            final int modoFirma = anexoFull.getAnexo().getModoFirma();
            Anexo anexo = anexoFull.getAnexo();
            AnexoSir anexoSir;
            byte[] hash = null;
            switch (modoFirma){

                case MODO_FIRMA_ANEXO_ATTACHED:

                    SignatureCustody sc = anexoFull.getSignatureCustody();

                    String identificador_fichero = generateIdentificadorFichero(identificadorIntercambio, secuencia, sc.getName());
                    secuencia++;
                    hash = RegwebUtils.obtenerHash(sc.getData()); //TODO por alguna razón al recuperar el hsh de la bbdd cambia de valor...revisar
                    anexoSir = crearAnexoSir(sc.getName(),identificador_fichero,
                            CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()),anexo.getCertificado(),
                            anexo.getFirma(),anexo.getTimestamp(), anexo.getValidacionOCSPCertificado(),
                            hash,sc.getMime(),sc.getData(),identificador_fichero,
                            anexo.getObservaciones(), null);

                    anexosSir.add(anexoSir);

                    break;

                case MODO_FIRMA_ANEXO_DETACHED:

                    // ================= SEGMENTO 1: DOCUMENT ==================

                    DocumentCustody dc = anexoFull.getDocumentoCustody();

                    identificador_fichero = generateIdentificadorFichero(identificadorIntercambio, secuencia, dc.getName());
                    secuencia++;
                    hash = RegwebUtils.obtenerHash(dc.getData()); //TODO por alguna razón al recuperar el hsh de la bbdd cambia de valor...revisar
                    anexoSir = crearAnexoSir(dc.getName(),identificador_fichero,
                            CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()),anexo.getCertificado(),
                            anexo.getFirma(),anexo.getTimestamp(), anexo.getValidacionOCSPCertificado(),
                            hash,dc.getMime(),dc.getData(),null,
                            anexo.getObservaciones(), null);

                    anexosSir.add(anexoSir);

                    // ================= SEGMENTO 2: FIRMA ==================

                    sc = anexoFull.getSignatureCustody();

                    String identificador_fichero_FIRMA = generateIdentificadorFichero(identificadorIntercambio, secuencia, sc.getName());
                    secuencia++;

                    anexoSir = crearAnexoSir(sc.getName(),identificador_fichero_FIRMA,
                            CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(anexo.getValidezDocumento()),
                            CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(TIPO_DOCUMENTO_FICHERO_TECNICO), null,
                            null,anexo.getTimestamp(), null,
                            hash,sc.getMime(),sc.getData(),identificador_fichero,
                            anexo.getObservaciones(), anexoSir);

                    anexosSir.add(anexoSir);

                    break;
            }


        }

        return anexosSir;
    }

    /**
     *
     * @param nombreFichero
     * @param identificadorFichero
     * @param validezDocumento
     * @param tipoDocumento
     * @param certificado
     * @param firma
     * @param timeStamp
     * @param validacionOCSPCertificado
     * @param hash
     * @param tipoMime
     * @param anexoData
     * @param identificadorDocumentoFirmado
     * @param observaciones
     * @return
     * @throws Exception 
     */
    private AnexoSir crearAnexoSir(String nombreFichero, String identificadorFichero, String validezDocumento, String tipoDocumento, byte[] certificado,
                                   String firma, byte[] timeStamp, byte[] validacionOCSPCertificado, byte[] hash, String tipoMime,
                                   byte[] anexoData, String identificadorDocumentoFirmado, String observaciones, AnexoSir documento) throws Exception{
        AnexoSir anexoSir = new AnexoSir();



        //Controlamos que el nombre fichero no supere el maxlength de SIR
        if (nombreFichero!=null && nombreFichero.length() >= RegwebConstantes.ANEXO_NOMBREFICHERO_MAXLENGTH_SIR) {
            String nombreFicheroSinExtension = nombreFichero.substring(0, nombreFichero.lastIndexOf("."));
            String extension = nombreFichero.substring(nombreFichero.lastIndexOf("."), nombreFichero.length());
            nombreFicheroSinExtension = nombreFicheroSinExtension.substring(0, RegwebConstantes.ANEXO_NOMBREFICHERO_MAXLENGTH_SIR-5);
            nombreFichero = nombreFicheroSinExtension + extension;
        }
        
        if (documento != null) {
        	anexoSir.setDocumento(documento);
        }
        anexoSir.setNombreFichero(es.caib.regweb3.utils.StringUtils.sustituirCaracteresProhibidosSIR(nombreFichero, '_'));

        anexoSir.setIdentificadorFichero(identificadorFichero);

        if(validezDocumento != null){
            anexoSir.setValidezDocumento(validezDocumento);
        }
        anexoSir.setTipoDocumento(tipoDocumento);
        if(certificado != null){
            anexoSir.setCertificado(Base64.encodeBase64String(certificado));
        }
        if(firma != null){
            anexoSir.setFirma(firma);
        }
        if(timeStamp != null){
            anexoSir.setTimestamp(Base64.encodeBase64String(timeStamp));
        }
        if(validacionOCSPCertificado != null){
            anexoSir.setValidacionOCSPCertificado(Base64.encodeBase64String(validacionOCSPCertificado));
        }
//        anexoSir.setHash(Base64.encodeBase64String(hash));
        anexoSir.setHash(RegwebUtils.obtenerStringHash(hash));
        if(tipoMime != null){
            if(tipoMime.equals("text/xml")){ //SICRES3 obliga a que el mime de un xml sea application/xml
                anexoSir.setTipoMIME("application/xml");
            }else{
//                if(tipoMimeAceptadoPorSir(tipoMime)!=null) {
                    anexoSir.setTipoMIME(tipoMime);
//                }
            }
        }
        if(anexoData != null){
            anexoSir.setAnexoData(anexoData);
        }
        if(identificadorDocumentoFirmado != null){
            anexoSir.setIdentificadorDocumentoFirmado(identificadorDocumentoFirmado);
        }

        if(observaciones!= null && observaciones.length()>= RegwebConstantes.ANEXO_OBSERVACIONES_MAXLENGTH_SIR) {
            anexoSir.setObservaciones(observaciones.substring(0, RegwebConstantes.ANEXO_OBSERVACIONES_MAXLENGTH_SIR));
        }else{
            anexoSir.setObservaciones(observaciones);
        }

        return anexoSir;
    }

    /**
     * Metodo que genera identificador de anxso según el patron
     * identificadorIntercambio_01_secuencia.extension
     * donde secuencia es cadena que repesenta secuencia en formato 0001 (leftpading con 0 y máximo de 4 caracteres)
     * donde extesion es la extension del anexo
     *
     * @param identificadorIntercambio
     * @param secuencia
     * @param fileName
     * @return
     */
    @Override
    public String generateIdentificadorFichero(String identificadorIntercambio, int secuencia, String fileName) {

        String identificadorFichero = null;
        		
        if (identificadorIntercambio != null) {
        	identificadorFichero = identificadorIntercambio +
                "_01_" +
                StringUtils.leftPad(
                        String.valueOf(secuencia), 4, "0") +
                "." + getExtension(fileName);
        }
        return identificadorFichero;
    }

    private void enviarEmailErrorConsultaSirRecibido(String numeroRegistro, Entidad entidad) {
    	Locale locale = new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO);
		// Obtenemos los usuarios a los que hay que enviarles el mail
        List<Usuario> usuariosANotificar = new ArrayList<Usuario>();
        String entorno = PropiedadGlobalUtil.getEntorno();
    	try {

            // Propietario Entidad
            usuariosANotificar.add(entidad.getPropietario());

            // Administradores Entidad
            for (UsuarioEntidad usuarioEntidad : entidad.getAdministradores()) {
                usuariosANotificar.add(usuarioEntidad.getUsuario());
            }

            // Asunto
            String[] argsEntorno = {entorno != null ? entorno : "PRO"};
            String asunto = I18NLogicUtils.tradueix(locale, "registro.sir.recibido.error.mail.asunto", argsEntorno);

            String[] args = {numeroRegistro, entidad.getNombre()};
            String mensajeTexto = I18NLogicUtils.tradueix(locale, "registro.sir.recibido.error.mail.cuerpo", args);

            //Enviamos el mail a todos los usuarios
            InternetAddress addressFrom = new InternetAddress(RegwebConstantes.APLICACION_EMAIL, RegwebConstantes.APLICACION_NOMBRE);
            
    		for (Usuario usuario : usuariosANotificar) {

                if (StringUtils.isNotEmpty(usuario.getEmail()) && isEnvioEmailErrorGeiserEnabled()) {
                	MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, usuario.getEmail());
                }
    		}
        } catch (Exception e) {
            log.error("Se ha producido una excepcion enviando email informando de un error recepción SIR");
            e.printStackTrace();
        }
    }
    
    private void enviarEmailErrorSirRecibidoBusqueda(Entidad entidad) {
    	Locale locale = new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO);
		// Obtenemos los usuarios a los que hay que enviarles el mail
        List<Usuario> usuariosANotificar = new ArrayList<Usuario>();
        String entorno = PropiedadGlobalUtil.getEntorno();
    	try {

            // Propietario Entidad
            usuariosANotificar.add(entidad.getPropietario());

            // Administradores Entidad
            for (UsuarioEntidad usuarioEntidad : entidad.getAdministradores()) {
                usuariosANotificar.add(usuarioEntidad.getUsuario());
            }

            // Asunto
            String[] argsEntorno = {entorno != null ? entorno : "PRO"};
            String asunto = I18NLogicUtils.tradueix(locale, "registro.sir.recibido.error.mail.asunto", argsEntorno);

            //Montamos el mensaje del mail con el nombre de la Entidad
            String[] args = {entidad.getNombre()};
            String mensajeTexto = I18NLogicUtils.tradueix(locale, "registro.sir.recibido.error.mail.cuerpo", args);

            //Enviamos el mail a todos los usuarios
            InternetAddress addressFrom = new InternetAddress(RegwebConstantes.APLICACION_EMAIL, RegwebConstantes.APLICACION_NOMBRE);
            
    		for (Usuario usuario : usuariosANotificar) {

                if (StringUtils.isNotEmpty(usuario.getEmail()) && isEnvioEmailErrorGeiserEnabled()) {
                	MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, usuario.getEmail());
                }
    		}
        } catch (Exception e) {
            log.error("Se ha producido una excepcion enviando email informando de un error recepción SIR");
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la Extensión de un Fichero a partir de su nombre
     *
     * @param nombreFichero
     * @return extensión del fichero
     */
    private String getExtension(String nombreFichero) {
        String extension = "";

        int i = nombreFichero.lastIndexOf('.');
        if (i > 0) {
            extension = nombreFichero.substring(i + 1);
        }

        return extension;
    }

    /**
     * Obtiene el código Oficina de Origen dependiendo de si es interna o externa
     *
     * @param registroDetalle
     * @param codigoOficia
     * @return
     * @throws Exception
     */
    private String obtenerCodigoOficinaOrigen(RegistroDetalle registroDetalle, String codigoOficia) {
        String codOficinaOrigen;

        if ((registroDetalle.getOficinaOrigenExternoCodigo() == null) && (registroDetalle.getOficinaOrigen() == null)) {
            codOficinaOrigen = codigoOficia;
        } else if (registroDetalle.getOficinaOrigenExternoCodigo() != null) {
            codOficinaOrigen = registroDetalle.getOficinaOrigenExternoCodigo();
        } else {
            codOficinaOrigen = registroDetalle.getOficinaOrigen().getCodigo();
        }

        return codOficinaOrigen;
    }

    /**
     * Obtiene el denominación Oficina de Origen dependiendo de si es interna o externa
     *
     * @param registroDetalle
     * @param denominacionOficia
     * @return
     * @throws Exception
     */
    private String obtenerDenominacionOficinaOrigen(RegistroDetalle registroDetalle, String denominacionOficia) {
        String denominacionOficinaOrigen;

        if ((registroDetalle.getOficinaOrigenExternoCodigo() == null) && (registroDetalle.getOficinaOrigen() == null)) {
            denominacionOficinaOrigen = denominacionOficia;
        } else if (registroDetalle.getOficinaOrigenExternoCodigo() != null) {
            denominacionOficinaOrigen = registroDetalle.getOficinaOrigenExternoDenominacion();
        } else {
            denominacionOficinaOrigen = registroDetalle.getOficinaOrigen().getDenominacion();
        }

        return denominacionOficinaOrigen;
    }

    /**
     *
     * @param registroDetalle
     * @return
     */
    private String obtenerCodigoUnidadTramitacionDestino(RegistroDetalle registroDetalle){

        List<Interesado> interesados = registroDetalle.getInteresados();

        for (Interesado interesado : interesados) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){

                return interesado.getCodigoDir3();
            }
        }

        return null;
    }

    /**
     *
     * @param registroDetalle
     * @return
     */
    private String obtenerDenominacionUnidadTramitacionDestino(RegistroDetalle registroDetalle){

        List<Interesado> interesados = registroDetalle.getInteresados();

        for (Interesado interesado : interesados) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){

                return interesado.getRazonSocial();
            }
        }

        return null;
    }

    /**
     * Crea un Interesado tipo Persona Juridica a partir del Código Unidad De Gestión de destino o si no está informado,
     * a partir del Código Entidad Registral de destino
     * @return
     */
    private InteresadoSir crearInteresadoJuridico(FicheroIntercambio ficheroIntercambio){

        InteresadoSir interesadoSalida = new InteresadoSir();

        if(StringUtils.isNotBlank(ficheroIntercambio.getCodigoUnidadTramitacionDestino())){

            interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
            interesadoSalida.setDocumentoIdentificacionInteresado(ficheroIntercambio.getCodigoUnidadTramitacionDestino());

            if(StringUtils.isNotBlank(ficheroIntercambio.getDescripcionUnidadTramitacionDestino())){
                interesadoSalida.setRazonSocialInteresado(ficheroIntercambio.getDescripcionUnidadTramitacionDestino());
            }else{
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(),ficheroIntercambio.getCodigoUnidadTramitacionDestino(),RegwebConstantes.UNIDAD));

            }


        }else{
            try {
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());

                OficinaTF oficinaTF = oficinasService.obtenerOficina(ficheroIntercambio.getCodigoEntidadRegistralDestino(),null,null);

                interesadoSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
                interesadoSalida.setDocumentoIdentificacionInteresado(oficinaTF.getCodUoResponsable());
                interesadoSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(),oficinaTF.getCodUoResponsable(),RegwebConstantes.UNIDAD));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return interesadoSalida;
    }
   
    
    /**
     * Transforma un {@link RegistroSir} en un {@link RegistroEntrada} y lo registra
     * @param registroSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @Override
    public RegistroEntrada aceptarRegistroSirEntrada(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, List<CamposNTI> camposNTIs, Long idOrganismoDestino)
            throws Exception, I18NException, I18NValidationException {

        Libro libro = libroEjb.findById(idLibro);

        RegistroEntrada registroEntrada = new RegistroEntrada();
        
//        registroEntrada.setUsuario(usuario);
//        registroEntrada.setOficina(oficinaActiva);
//        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
//        registroEntrada.setLibro(libro);
//
//        // Obtenemos el Organismo destino indicado
//        Organismo organismoDestino = organismoEjb.findByIdLigero(idOrganismoDestino);
//
//        registroEntrada.setDestino(organismoDestino);
//        registroEntrada.setDestinoExternoCodigo(null);
//        registroEntrada.setDestinoExternoDenominacion(null);
//
//        // RegistroDetalle
//        registroEntrada.setRegistroDetalle(getRegistroDetalle(registroSir, idIdioma));
//
//        // Interesados
//        List<Interesado> interesados = procesarInteresados(registroSir.getInteresados());
//
//        // Anexos
//        List<AnexoFull> anexosFull = procesarAnexos(registroSir, camposNTIs);
//
//        // Registramos el Registro Entrada
//        registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuario,interesados,anexosFull, true, true);
//
//
//        // Creamos la TrazabilidadSir
//        TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_CONFIRMADO);
//        trazabilidadSir.setRegistroSir(registroSir);
//        trazabilidadSir.setRegistroEntrada(registroEntrada);
//        trazabilidadSir.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralOrigen());
//        trazabilidadSir.setDecodificacionEntidadRegistralOrigen(registroSir.getDecodificacionEntidadRegistralOrigen());
//        trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralDestino());
//        trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralDestino());
//        trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
//        trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
//        trazabilidadSir.setContactoUsuario(usuario.getUsuario().getEmail());
//        trazabilidadSir.setObservaciones(registroSir.getDecodificacionTipoAnotacion());
//        trazabilidadSirEjb.persist(trazabilidadSir);
//
//        // CREAMOS LA TRAZABILIDAD
//        Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
//        trazabilidad.setRegistroSir(registroSir);
//        trazabilidad.setRegistroEntradaOrigen(null);
//        trazabilidad.setOficioRemision(null);
//        trazabilidad.setRegistroSalida(null);
//        trazabilidad.setRegistroEntradaDestino(registroEntrada);
//        trazabilidad.setFecha(new Date());
//        trazabilidadEjb.persist(trazabilidad);
//
//        // Modificamos el estado del RegistroSir
//        modificarEstado(registroSir.getId(), EstadoRegistroSir.ACEPTADO);

        return registroEntrada;
    }


    /**
     * Método que comprueba si el tipoMime es aceptado por SIR, si no lo es devuelve null
     * @param tipoMime
     * @return
     */
    private String tipoMimeAceptadoPorSir(String tipoMime){

        if(tipoMime.length() <= ANEXO_TIPOMIME_MAXLENGTH_SIR && Arrays.asList(TIPOS_MIME_ACEPTADO_SIR).contains(tipoMime)){
            return tipoMime;
        }else{
            return null;
        }

    }
    
    private String getFechaInicioProximaBusqueda(Long idEntidad) {
    	String path = PropiedadGlobalUtil.getFechaInicioBusquedaSirRecibidosPath(idEntidad);
    	Properties properties = new Properties();
    	String fechaInicio = null;
		try {
			if (path == null || path == "") {
				path = "/opt/files/";
			}
			File file = new File(path + "/config.properties");
			properties.load(new FileInputStream(file));
			fechaInicio = properties.getProperty("busqueda.sir.fecha.fechaInicio");
		} catch (Exception ex) {
			log.error("Error en la lectura de la fecha de inicio de búsqeuda", ex);
			ex.printStackTrace();
		}
		return fechaInicio;
    }

    private void updateFechaInicioProximaBusqueda(Long idEntidad, String fechaFinActual) {
    	String path = PropiedadGlobalUtil.getFechaInicioBusquedaSirRecibidosPath(idEntidad);
    	Properties properties = new Properties();
		File file;
		try {
			properties.setProperty("busqueda.sir.fecha.fechaInicio", fechaFinActual);
			if (path == null || path == "") {
				path = "/opt/files/";
			}
			file = new File(path + "/config.properties");
			OutputStream out = new FileOutputStream(file);
			
			DefaultPropertiesPersister p = new DefaultPropertiesPersister();
			p.store(properties, out, "Fecha inicio próximo búsqueda de registros SIR.");
		} catch (Exception ex) {
			log.error("Error a la hora de escribir el fichero config", ex);
			ex.printStackTrace();
		}
    }
    
    private boolean isEnvioEmailErrorGeiserEnabled() {
    	return PropiedadGlobalUtil.getEnvioEmailErrorGeiser();
    }
}