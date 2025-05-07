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
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.plugin.notib.api.NotibPluginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.notib.client.domini.Certificacio;
import es.caib.notib.client.domini.DocumentV2;
import es.caib.notib.client.domini.EntregaDeh;
import es.caib.notib.client.domini.EnviamentTipus;
import es.caib.notib.client.domini.EnviamentV2;
import es.caib.notib.client.domini.InteressatTipus;
import es.caib.notib.client.domini.NotificacioV2;
import es.caib.notib.client.domini.PersonaV2;
import es.caib.notib.client.domini.RespostaAlta;
import es.caib.notib.client.domini.RespostaConsultaEstatEnviamentV2;
import es.caib.notib.client.domini.ServeiTipus;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.RemesaAcuse;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.ClasificacionDto;
import es.caib.regweb3.model.utils.TramiteDto;
import es.caib.regweb3.persistence.utils.DatabaseConnection;
import es.caib.regweb3.persistence.utils.DocumentoDto;
import es.caib.regweb3.persistence.utils.MailUtils;
import es.caib.regweb3.persistence.utils.NotibPluginHelper;
import es.caib.regweb3.persistence.utils.PdfGenerator;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.TramiteRowMapper;
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
	public void clasificarRegistro(
			RegistroEntrada registroEntrada, 
			UsuarioEntidad usuarioEntidad, 
			ClasificacionDto clasificacionForm,
			Entidad entidad) throws Exception, I18NException {
		// 1.- Generar pdf
		byte[] documento = PdfGenerator.generarPdf(
				registroEntrada, 
				clasificacionForm.getCodigoSia());

		// 2.- Enviar correo interesados
		enviaMailInteresados(
				registroEntrada, 
				documento);

		// 3.- Notificar interesados
		enviarNotificacion(
				entidad, 
				registroEntrada, 
				usuarioEntidad, 
				clasificacionForm, 
				documento);
		
		// 4.- Anular registro entrada
		registroEntradaEjb.anularRegistroEntrada(
				registroEntrada, 
				usuarioEntidad, 
				"Clasificación registro de instáncia genérica");
	}
	
	@Override
	public DocumentoDto descargarCertificacion(String identificadorNotib, String referenciaEnviament, Entidad entidad) throws I18NException, Exception {
		DocumentoDto certificado = new DocumentoDto();
		try {
			Remesa remesa = remesaConsultaEjb.getByIdentificadorAndReferencia(identificadorNotib, referenciaEnviament);
			
			RespostaConsultaEstatEnviamentV2 resposta = pluginHelper.consultarEnvio(referenciaEnviament, entidad.getId());
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
		} catch (I18NException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		
		return certificado;
	}

	private void enviarNotificacion(
			Entidad entidad,
			RegistroEntrada registroEntrada, 
			UsuarioEntidad usuarioEntidad, 
			ClasificacionDto clasificacionForm, 
			byte[] documento) throws I18NException, Exception {
		
		NotificacioV2 request = generarDtoNotificacion(
				entidad, 
				registroEntrada, 
				usuarioEntidad, 
				clasificacionForm, 
				documento);

		List<Remesa> remesas = remesaEjb.guardarNotificacion(
				request, 
				entidad,
				registroEntrada,
				usuarioEntidad);
		
		try {
			RespostaAlta respuesta = pluginHelper.altaNotificacion(
					request, 
					entidad);
			
			remesaEjb.actualizarNotificacionEnviada(remesas, respuesta);
		} catch (NotibPluginException e) {
			for (Remesa remesa : remesas) {
				remesaEjb.remove(remesa);
			}
			em.flush();
			throw e;
		}
	
		//TODO: guardar document notificació a sgd?
//			guardarDocumento(
//					resposta.getIdentificador(), 
//					notificacion.getDocument().getArxiuNom(), 
//					getBytesFromInputStream(documento));
	}
	
	private NotificacioV2 generarDtoNotificacion(
			Entidad entidad,
			RegistroEntrada registroEntrada, 
			UsuarioEntidad usuarioEntidad, 
			ClasificacionDto clasificacionForm, 
			byte[] documento) throws IOException, I18NException {
		String usuarioIntegracion = pluginHelper.getUsuarioIntegracionNotib(entidad.getId());
		String procedimentCodi = clasificacionForm.getCodigoSia().toString();
		List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
		
		NotificacioV2 notificacio = new NotificacioV2();
		notificacio.setEmisorDir3Codi(entidad.getCodigoDir3());
		notificacio.setOrganGestor(entidad.getCodigoDir3());
		notificacio.setEnviamentTipus(EnviamentTipus.NOTIFICACIO);
		notificacio.setUsuariCodi(usuarioIntegracion);
		// notificacio.setComunicacioTipus(ComunicacioTipusEnum.ASINCRON);
		notificacio.setConcepte("Clasificación del registro " + registroEntrada.getNumeroRegistro());
		notificacio.setDescripcio("Clasificación del registro " + registroEntrada.getNumeroRegistro());
		notificacio.setEnviamentDataProgramada(null);
		notificacio.setCaducitat(clasificacionForm.getCaducidad());
		
		String retardo = clasificacionForm.getRetardo();
		if (retardo != null && ! retardo.isEmpty())
			notificacio.setRetard(Integer.valueOf(retardo));
		
		DocumentV2 document = new DocumentV2();
		document.setArxiuNom("Notificación_" + System.currentTimeMillis() + ".pdf");
		String arxiuB64 = Base64.encodeBase64String(documento);
		document.setContingutBase64(arxiuB64);
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
	
	private void enviaMailInteresados(RegistroEntrada registroEntrada, byte[] documento) throws Exception {
		List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
		String asunto = "Revertido registro " + registroEntrada.getNumeroRegistro();
		String mensajeTexto = "Revertido registro " + registroEntrada.getNumeroRegistro();

		InternetAddress addressFrom = new InternetAddress(RegwebConstantes.APLICACION_EMAIL,
				RegwebConstantes.APLICACION_NOMBRE);

		for (Interesado interesado : interesados) {
			String emailInteresado = interesado.getEmail();
			
			if (emailInteresado != null)
				MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, emailInteresado, documento);
			else
				log.error("Hi ha hagut un error enviant l'email de la classficació del registre " + registroEntrada.getNumeroRegistro() + " a l'interessat " + interesado.getNombre());
			
			Interesado representante = interesado.getRepresentante();

			if (representante != null) {
				String emailRepresentante = representante.getEmail();
				
				if (emailRepresentante != null)
					MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, emailRepresentante,
							documento);
				else
					log.error("Hi ha hagut un error enviant l'email de la classficació del registre " + registroEntrada.getNumeroRegistro() + " al representant " + interesado.getNombre());
			}
		}
	}

}
