package es.caib.regweb3.persistence.ejb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.plugin.notib.api.NotibPluginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.notib.client.domini.Certificacio;
import es.caib.notib.client.domini.DocumentV2;
import es.caib.notib.client.domini.EntregaDeh;
import es.caib.notib.client.domini.EnviamentEstat;
import es.caib.notib.client.domini.EnviamentTipus;
import es.caib.notib.client.domini.EnviamentV2;
import es.caib.notib.client.domini.InteressatTipus;
import es.caib.notib.client.domini.NotificacioEstatEnum;
import es.caib.notib.client.domini.NotificacioV2;
import es.caib.notib.client.domini.PersonaV2;
import es.caib.notib.client.domini.RespostaAlta;
import es.caib.notib.client.domini.RespostaConsultaEstatEnviamentV2;
import es.caib.notib.client.domini.RespostaConsultaEstatNotificacioV2;
import es.caib.notib.client.domini.ServeiTipus;
import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.RemesaAcuse;
import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RevocacionDto;
import es.caib.regweb3.model.utils.TramiteDto;
import es.caib.regweb3.persistence.utils.DatabaseConnection;
import es.caib.regweb3.persistence.utils.DocumentoDto;
import es.caib.regweb3.persistence.utils.MailUtils;
import es.caib.regweb3.persistence.utils.NotibPluginHelper;
import es.caib.regweb3.persistence.utils.DocumentHelper;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.TramiteRowMapper;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;

/**
 * Created 14/10/14 9:55
 *
 * @author Limit Tecnologies S.L
 */
@Stateless(name = "TramiteEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TramiteBean implements TramiteLocal {

	protected final Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "regweb3")
	private EntityManager em;
	
	@EJB
	private RegistroEntradaLocal registroEntradaEjb;
	@EJB
	private RemesaLocal remesaEjb;
	@EJB
	private RemesaConsultaLocal remesaConsultaEjb;
	@EJB
	private RemesaAcuseLocal remesaAcuseEjb;
	@EJB
	private SignatureServerLocal signatureServerEjb;
	@EJB
	private AnexoLocal anexoEjb;
	@EJB
	private TipoDocumentalLocal tipoDocumentalEjb;
	
	@Autowired
	private NotibPluginHelper pluginHelper;
	
	@Override
	public List<TramiteDto> getTramitesRolsac(String lang) throws Exception {
		List<TramiteDto> tramites = new ArrayList<TramiteDto>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String fecha = formatter.format(new Date());
		String tramitsExcluir = PropiedadGlobalUtil.getIdTramitesExcluir();
		
		String sql = "SELECT p.PRO_CODSIA," + 
						"tp.TPR_NOMBRE " + 
					"FROM RSC_PROCED p, RSC_TRAPRO tp "	+ 
					"WHERE tp.TPR_CODPRO = p.PRO_CODI " + 
					"AND (p.PRO_FECCAD is null OR p.PRO_FECCAD >= '" + fecha + "') " + 
					"AND p.PRO_CODSIA IS NOT NULL " + "AND p.PRO_VALIDA = 1 AND tp.TPR_CODIDI = '" + lang + "'" + 
					"AND (" + 
							"SELECT COUNT(*) " + 
							"FROM RSC_TRAMIT t " + 
							"WHERE t.TRA_CODPRO = p.PRO_CODI " + 
							"AND t.TRA_FASE = 1 " + 
							"AND NOT REGEXP_LIKE(t.TRA_IDTRAMTEL, '" + tramitsExcluir + "')" + 
						") > 0";
		
		Connection conn = null;

		try {
			conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				tramites.add(TramiteRowMapper.mapRow(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}

		return tramites;
	}

	@TransactionTimeout(value = 1200)
	@Override
	public void revocarRegistro(
			RegistroEntrada registroEntrada, 
			UsuarioEntidad usuarioEntidad, 
			RevocacionDto revocacionForm,
			Entidad entidad) throws Exception, I18NException, I18NValidationException {
		// 1.- Generar pdf i guardarlo en Arxiu
		AnexoFull anexoFull = generarGuardarDocumento(
				entidad,
				registroEntrada, 
				usuarioEntidad,
				revocacionForm);

		// 2.- Notificar interesados
		enviarNotificacion(
				entidad, 
				registroEntrada, 
				usuarioEntidad, 
				revocacionForm, 
				anexoFull);

		// 3.- Enviar correo interesados
		enviaMailInteresados(
				registroEntrada, 
				revocacionForm);
		
		// 4.- Anular registro entrada
		registroEntradaEjb.anularRegistroEntrada(
				registroEntrada, 
				usuarioEntidad, 
				"Registro revocado");
	}	

	private AnexoFull generarGuardarDocumento(
			Entidad entidad,
			RegistroEntrada registroEntrada,
			UsuarioEntidad usuarioEntidad,
			RevocacionDto revocacionForm) throws Exception, I18NException, I18NValidationException {
		StringBuilder peticion = new StringBuilder();
		Long tipoRegistro = RegwebConstantes.REGISTRO_ENTRADA;
		Long registroId = registroEntrada.getId();
		
		peticion.append("usuario: ").append(usuarioEntidad.getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
        peticion.append("tipoRegistro: ").append(tipoRegistro).append(System.getProperty("line.separator"));
        peticion.append("idioma: ").append(Configuracio.getDefaultLanguage()).append(System.getProperty("line.separator"));
        peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
        peticion.append("clase: ").append(getClass().getName()).append(System.getProperty("line.separator"));
        
		byte[] documento = DocumentHelper.generarPdf(
				registroEntrada, 
				revocacionForm.getCodigoSia());

		SignatureCustody signatureCustody = signatureServerEjb.signDocument(
				documento, 
				Configuracio.getDefaultLanguage(), 
				entidad.getId(), 
				peticion, 
				registroEntrada.getNumeroRegistro(), 
				"Documento.pdf", 
				"Revocación del registro de entrada " + registroEntrada.getNumeroRegistro());

		AnexoFull anexoForm = generarAnexoFull(
				entidad.getId(), 
				signatureCustody);
		
		AnexoFull anexoFull = anexoEjb.crearAnexo(
				anexoForm, 
				usuarioEntidad, 
				registroId, 
				tipoRegistro, 
				null, 
				true);
		
		return anexoFull;
	}
	
	private AnexoFull generarAnexoFull(Long entidadId, SignatureCustody signatureCustody) throws Exception {
		AnexoFull anexoFull = new AnexoFull();
		Anexo anexo = new Anexo();
		
		anexoFull.setSignatureCustody(signatureCustody);
		
		anexo.setTitulo("Documento revocación");
		anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_DOC_ADJUNTO);
		anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
		anexo.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL);
		anexo.setPerfilCustodia(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);
		anexo.setNombreFichero("Documento.pdf");
		anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
		
		TipoDocumental tipoDocumental = tipoDocumentalEjb.findByCodigoEntidad("TD07", entidadId); // Notificació
		anexo.setTipoDocumental(tipoDocumental);
		
		anexoFull.setAnexo(anexo);
		
		return anexoFull;
	}
	
	private void enviarNotificacion(
			Entidad entidad,
			RegistroEntrada registroEntrada, 
			UsuarioEntidad usuarioEntidad, 
			RevocacionDto revocacionForm, 
			AnexoFull anexoFull) throws I18NException, Exception {
		String nombreFichero = anexoFull.getAnexo().getNombreFichero();
		String uuidDocumento = anexoFull.getAnexo().getCustodiaID();
		if (uuidDocumento.contains("#")) {
			uuidDocumento = uuidDocumento.substring(0, uuidDocumento.indexOf("#"));      	
        }
		
		NotificacioV2 request = generarDtoNotificacion(
				entidad, 
				registroEntrada, 
				usuarioEntidad, 
				revocacionForm,
				nombreFichero,
				uuidDocumento);

		List<Remesa> remesas = remesaEjb.guardarNotificacion(
				request, 
				entidad,
				registroEntrada,
				usuarioEntidad);
		
		try {
			RespostaAlta respuesta = pluginHelper.altaNotificacion(
					request, 
					entidad);
			
			if (respuesta != null && ! respuesta.isError()) {
				remesaEjb.actualizarNotificacionEnviada(remesas, respuesta);
			} else {
				throw new NotibPluginException(respuesta.getErrorDescripcio());
			}
		} catch (NotibPluginException e) {
			for (Remesa remesa : remesas) {
				remesaEjb.remove(remesa);
			}
			em.flush();
			throw e;
		}
	}
	
	@Override
	public void notificacionActualitzarEstado(String identificadorNotib, String referenciaEnviament, Remesa remesa) throws Exception, I18NException {
		// Consultar notificació per identificador i referencia i si existeix actualitzar estat, sino, no fer res
		try {
			Entidad entidad = remesa.getEntidad();
			RespostaConsultaEstatEnviamentV2 resposta = pluginHelper.consultarEnvio(
					referenciaEnviament, 
					entidad.getId());
			
			RespostaConsultaEstatNotificacioV2 respostaNotificioEstat = pluginHelper.consultarNotificacion(
					identificadorNotib, 
					entidad.getId());

			if (resposta != null && respostaNotificioEstat != null && ! respostaNotificioEstat.isError() && ! resposta.isError()) {
				String estadoNotificacion = obtenerEstadoNotificacion(respostaNotificioEstat.getEstat());
				String  estadoNotifica = obtenerEstadoEnvio(resposta.getEstat());
				Date estadoData = resposta.getEstatData();
				Date fechaCreacion = respostaNotificioEstat.getDataCreada();
				Date fechaEnviada = respostaNotificioEstat.getDataEnviada();
				Date fechaFinalizada = respostaNotificioEstat.getDataFinalitzada();
				
				remesaEjb.actualizarEstadoNotifica(
						null,
						identificadorNotib, 
						referenciaEnviament, 
						estadoNotificacion, 
						estadoData,
						estadoNotifica,
						fechaCreacion,
						fechaEnviada,
						fechaFinalizada);
			
			} else {
				remesaEjb.actualizarMensajeError(remesa.getId(), respostaNotificioEstat.getErrorDescripcio());
			}
			em.flush();
		} catch (NotibPluginException e) {
			throw e;
		}
		
	}

	@Override
	public DocumentoDto descargarCertificacion(String identificadorNotib, String referenciaEnviament, Entidad entidad) throws I18NException, Exception {
		DocumentoDto certificado = new DocumentoDto();
		try {
			Remesa remesa = remesaConsultaEjb.getByIdentificadorAndReferencia(identificadorNotib, referenciaEnviament);
			
			RespostaConsultaEstatEnviamentV2 resposta = pluginHelper.consultarEnvio(referenciaEnviament, entidad.getId());
			
			if (resposta != null && ! resposta.isError()) {
				Certificacio certificacioNotib = resposta.getCertificacio();
				
				if (certificacioNotib != null) {
					byte[] contenido = Base64.decodeBase64(certificacioNotib.getContingutBase64());
					
					certificado.setContenido(contenido);
					certificado.setFilename("Certificació_" + remesa.getRegistro().getNumeroRegistro() + ".pdf");
					certificado.setMimeType(certificacioNotib.getTipusMime());
					
					RemesaAcuse remesaAcuse = remesaAcuseEjb.findByRemesa(remesa.getId());
					
					if (remesaAcuse == null) {
						remesaAcuseEjb.crearReferenciaAcuse(
								null, 
								certificacioNotib.getHash(), 
								remesa);
					}
				}
			} else {
				remesaEjb.actualizarMensajeError(remesa.getId(), resposta.getErrorDescripcio());
			}
			
		} catch (I18NException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		
		return certificado;
	}
	
	private NotificacioV2 generarDtoNotificacion(
			Entidad entidad,
			RegistroEntrada registroEntrada, 
			UsuarioEntidad usuarioEntidad, 
			RevocacionDto revocacionForm, 
			String nombreFichero,
			String uuidDocumento) throws IOException, I18NException {
		String usuarioIntegracion = pluginHelper.getUsuarioIntegracionNotib(entidad.getId());
		String procedimentCodi = String.valueOf(PropiedadGlobalUtil.getCodigoSiaInstanciaGenerica());
		List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
		
		NotificacioV2 notificacio = new NotificacioV2();
		notificacio.setEmisorDir3Codi(entidad.getCodigoDir3());
		notificacio.setOrganGestor(entidad.getCodigoDir3());
		notificacio.setEnviamentTipus(EnviamentTipus.NOTIFICACIO);
		notificacio.setUsuariCodi(usuarioIntegracion);
		// notificacio.setComunicacioTipus(ComunicacioTipusEnum.ASINCRON);
		notificacio.setConcepte("Revocación del registro " + registroEntrada.getNumeroRegistro());
		notificacio.setDescripcio("Revocación del registro " + registroEntrada.getNumeroRegistro());
		notificacio.setEnviamentDataProgramada(null);
		notificacio.setCaducitat(revocacionForm.getCaducidad());
		
		String retardo = revocacionForm.getRetardo();
		if (retardo != null && ! retardo.isEmpty())
			notificacio.setRetard(Integer.valueOf(retardo));
		
		DocumentV2 document = new DocumentV2();
		document.setArxiuNom(nombreFichero);
		document.setUuid(uuidDocumento);
		document.setNormalitzat(false);
		// document.setGenerarCsv(false);
		notificacio.setDocument(document);
		notificacio.setProcedimentCodi(procedimentCodi);

		for (Interesado interesado : interesados) {
			EnviamentV2 enviament = new EnviamentV2();
			PersonaV2 titular = new PersonaV2();
			if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
				titular.setInteressatTipus(InteressatTipus.JURIDICA);
				titular.setRaoSocial(interesado.getRazonSocial());
				titular.setNif(interesado.getDocumento());
			}
			if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
				titular.setInteressatTipus(InteressatTipus.FISICA);
				titular.setNom(interesado.getNombre());
				titular.setLlinatge1(interesado.getApellido1());
				titular.setLlinatge2(interesado.getApellido2());
				titular.setNif(interesado.getDocumento());
			}
			if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
				titular.setInteressatTipus(InteressatTipus.ADMINISTRACIO);
				titular.setRaoSocial(interesado.getNombreOrganismo());
				titular.setDir3Codi(interesado.getCodigoDir3());
			}
			titular.setTelefon(interesado.getTelefono());
			titular.setEmail(interesado.getEmail());
			enviament.setTitular(titular);
			
			if (interesado.getRepresentante() != null) {
				Interesado representante = interesado.getRepresentante();
				PersonaV2 destinatari = new PersonaV2();
				destinatari.setNom(representante.getNombre());
				destinatari.setLlinatge1(representante.getApellido1());
				destinatari.setLlinatge2(representante.getApellido2());
				destinatari.setNif(representante.getDocumento());
				destinatari.setTelefon(representante.getTelefono());
				destinatari.setEmail(representante.getEmail());
				destinatari.setInteressatTipus(InteressatTipus.ADMINISTRACIO);
				if (representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION))
					destinatari.setDir3Codi(representante.getCodigoDir3());
				enviament.getDestinataris().add(destinatari);
			}
//			if (false) {
//				EntregaPostalV2 entregaPostal = new EntregaPostalV2();
//				entregaPostal.setTipus(NotificaDomiciliConcretTipusEnumDto.NACIONAL);
//				entregaPostal.setViaTipus(EntregaPostalViaTipusEnum.CALLE);
//				entregaPostal.setViaNom("Bas");
//				entregaPostal.setNumeroCasa("25");
//				entregaPostal.setNumeroQualificador("bis");
//				// entregaPostal.setApartatCorreus("0228");
//				entregaPostal.setPortal("pt" + i);
//				entregaPostal.setEscala("es" + i);
//				entregaPostal.setPlanta("pl" + i);
//				entregaPostal.setPorta("pr" + i);
//				entregaPostal.setBloc("bl" + i);
//				entregaPostal.setComplement("complement" + i);
//				entregaPostal.setCodiPostal("07500");
//				entregaPostal.setPoblacio("poblacio" + i);
//				entregaPostal.setMunicipiCodi("070337");
//				entregaPostal.setProvincia("07");
//				entregaPostal.setPaisCodi("ES");
//				entregaPostal.setLinea1("linea1_" + i);
//				entregaPostal.setLinea2("linea2_" + i);
//				entregaPostal.setCie(new Integer(0));
//				enviament.setEntregaPostal(entregaPostal);
//				enviament.setEntregaPostalActiva(true);
//			}
			EntregaDeh entregaDeh = new EntregaDeh();
			entregaDeh.setObligat(true);
			entregaDeh.setProcedimentCodi(procedimentCodi);
			enviament.setEntregaDeh(entregaDeh);
			enviament.setServeiTipus(ServeiTipus.URGENT);
			notificacio.getEnviaments().add(enviament);
		}
		return notificacio;
	}
	
	private String obtenerEstadoEnvio(EnviamentEstat estat) {
		String estado = null;
		
		switch (estat) {
		case ABSENT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_AUSENTE;
			break;
		case DESCONEGUT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_DESCONOCIDO;
			break;
		case ADRESA_INCORRECTA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_DIRECCION_INCO;
			break;
		case ENVIADA_DEH:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ENVIADO_DEH;
			break;
		case ENVIADA_CI:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ENVIADO_CI;
			break;
		case ENTREGADA_OP:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ENTREGADO_OP;
			break;
		case LLEGIDA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_LEIDA;
			break;
		case ERROR_ENTREGA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ERROR;
			break;
		case EXTRAVIADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_EXTRAVIADA;
			break;
		case MORT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_FALLECIDO;
			break;
		case NOTIFICADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_NOTIFICADA;
			break;
		case PENDENT_ENVIAMENT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_ENVIO;
			break;
		case PENDENT_CIE:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_CIE;
			break;
		case PENDENT_DEH:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_DEH;
			break;
		case PENDENT_SEU:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_SEDE;
			break;
		case REBUTJADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_REHUSADA;
			break;
		case EXPIRADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_EXPIRADA;
			break;
		case ENVIAMENT_PROGRAMAT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ENVIO_PROGRAM;
			break;
		case SENSE_INFORMACIO:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_SIN_INFO;
			break;
		case ANULADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ANULADA;
			break;
		default:
			break;
		}
		
		return estado;
	}

	private String obtenerEstadoNotificacion(NotificacioEstatEnum estat) {
		String estado = null;
		
		switch (estat) {
		case PENDENT:
			estado = RegwebConstantes.REMESA_ESTADO_REG_PENDIENTE;
			break;
		case REGISTRADA:
			estado = RegwebConstantes.REMESA_ESTADO_REG_REGISTRADA;
			break;
		case ENVIADA:
			estado = RegwebConstantes.REMESA_ESTADO_REG_ENVIADA;
			break;
		case ENVIADA_AMB_ERRORS:
			estado = RegwebConstantes.REMESA_ESTADO_REG_ENVIADA;
			break;
		case FINALITZADA:
			estado = RegwebConstantes.REMESA_ESTADO_REG_FINALIZADA;
			break;
		case FINALITZADA_AMB_ERRORS:
			estado = RegwebConstantes.REMESA_ESTADO_REG_FINALIZADA;
			break;
		case PROCESSADA:
			estado = RegwebConstantes.REMESA_ESTADO_REG_PROCESADA;
			break;
		default:
			break;
		}
		
		return estado;
	}
	
	private void enviaMailInteresados(RegistroEntrada registroEntrada, RevocacionDto revocacionForm) throws Exception {
		List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
		String asunto = "Revocación del registro de entrada " + registroEntrada.getNumeroRegistro();
		String mensajeTexto = DocumentHelper.generarHtml(
				registroEntrada, 
				revocacionForm.getCodigoSia());
		
		InternetAddress addressFrom = new InternetAddress(RegwebConstantes.APLICACION_EMAIL,
				RegwebConstantes.APLICACION_NOMBRE);

		for (Interesado interesado : interesados) {
			String emailInteresado = interesado.getEmail();
			
			if (emailInteresado != null && ! emailInteresado.isEmpty())
				MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, emailInteresado, true);
			else
				log.error("Hi ha hagut un error enviant l'email de la classficació del registre " + registroEntrada.getNumeroRegistro() + " a l'interessat " + interesado.getNombre());
			
			Interesado representante = interesado.getRepresentante();

			if (representante != null) {
				String emailRepresentante = representante.getEmail();
				
				if (emailRepresentante != null)
					MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, emailRepresentante, true);
				else
					log.error("Hi ha hagut un error enviant l'email de la classficació del registre " + registroEntrada.getNumeroRegistro() + " al representant " + interesado.getNombre());
			}
		}
	}

}
