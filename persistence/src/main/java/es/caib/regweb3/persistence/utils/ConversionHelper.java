package es.caib.regweb3.persistence.utils;

import static es.caib.regweb3.utils.RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO;
import static es.caib.regweb3.utils.RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.plugin.geiser.api.AnexoG;
import org.plugin.geiser.api.ApunteRegistro;
import org.plugin.geiser.api.CanalNotificacion;
import org.plugin.geiser.api.DocumentacionFisica;
import org.plugin.geiser.api.InteresadoG;
import org.plugin.geiser.api.PeticionBusquedaTramitGeiser;
import org.plugin.geiser.api.PeticionConsultaGeiser;
import org.plugin.geiser.api.PeticionRegistroEnvioGeiser;
import org.plugin.geiser.api.PeticionRegistroGeiser;
import org.plugin.geiser.api.TipoAsiento;
import org.plugin.geiser.api.TipoDocumento;
import org.plugin.geiser.api.TipoDocumentoAnexo;
import org.plugin.geiser.api.TipoEnvio;
import org.plugin.geiser.api.TipoFirma;
import org.plugin.geiser.api.TipoTransporte;
import org.plugin.geiser.api.ValidezDocumento;
import org.springframework.stereotype.Component;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.AnexoSir;
import es.caib.regweb3.model.CatLocalidad;
import es.caib.regweb3.model.CatPais;
import es.caib.regweb3.model.CatProvincia;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.InteresadoSir;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.sir.TipoDocumentoIdentificacion;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.model.utils.TipoRegistro;
import es.caib.regweb3.persistence.ejb.AnexoSirLocal;
import es.caib.regweb3.persistence.ejb.ArchivoLocal;
import es.caib.regweb3.persistence.ejb.CatLocalidadLocal;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

@Component
public class ConversionHelper {

	@EJB(mappedName = "regweb3/AnexoSirEJB/local")
	private AnexoSirLocal anexoSirEjb;
	@EJB(mappedName = "regweb3/ArchivoEJB/local")
	private ArchivoLocal archivoEjb;
	@EJB(mappedName = "regweb3/CatPaisEJB/local")
	private CatPaisLocal catPaisEjb;
	@EJB(mappedName = "regweb3/CatProvinciaEJB/local")
	private CatProvinciaLocal catProvinciaEjb;
	@EJB(mappedName = "regweb3/CatLocalidadEJB/local")
	private CatLocalidadLocal catLocalidadEjb;
	
	private MapperFactory mapperFactory;
	
	public ConversionHelper () {
		mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<RegistroEntrada, PeticionRegistroGeiser>() {
					@Override
					public PeticionRegistroGeiser convert(RegistroEntrada source, Type<? extends PeticionRegistroGeiser> destinationType) {
						RegistroDetalle registroDetalle = source.getRegistroDetalle();
						PeticionRegistroGeiser target = new PeticionRegistroGeiser();
						target.setResumen(StringUtils.stripToNull(source.getRegistroDetalle().getExtracto()));
						target.setTipoAsiento(TipoAsiento.ENTRADA);
//						Informar origen si es administarción
						if (registroDetalle.getInteresados() != null && registroDetalle.getInteresados().size() > 0) {
							for (Interesado interesado: registroDetalle.getInteresados()) {
								if (interesado.getTipo() == 1L) {
									target.setOrganoOrigen(interesado.getCodigoDir3());
								}
							}
						}
						String destino = source.getDestino() != null ? source.getDestino().getCodigo() : null;
						String destinoExterno = source.getDestinoExternoCodigo() != null ? source.getDestinoExternoCodigo() : null;
						target.setOrganoDestino(StringUtils.stripToNull(destino != null ? destino : destinoExterno));
						target.setDocumentacionFisica(DocumentacionFisica.getDocumentacionFisica(registroDetalle.getTipoDocumentacionFisica()));
						target.setRefExterna(StringUtils.stripToNull(registroDetalle.getReferenciaExterna()));
						target.setNuExpediente(StringUtils.stripToNull(registroDetalle.getExpediente()));
						target.setTipoTransporte(TipoTransporte.getTipoTransporte(registroDetalle.getTransporte()));
						target.setNuTransporte(StringUtils.stripToNull(registroDetalle.getNumeroTransporte()));
						target.setObservaciones(StringUtils.stripToNull(registroDetalle.getObservaciones()));
						target.setOficinaOrigen(StringUtils.stripToNull(registroDetalle.getOficinaOrigen().getCodigo()));
						target.setInteresados(convertirList(registroDetalle.getInteresados(), InteresadoG.class));
						if (source.getUsuario() != null)
							target.setUsuario(StringUtils.stripToNull(source.getUsuario().getUsuario().getDocumento()));
						return target;
					}
				});
		
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<RegistroSalida, PeticionRegistroGeiser>() {
					@Override
					public PeticionRegistroGeiser convert(RegistroSalida source, Type<? extends PeticionRegistroGeiser> destinationType) {
						RegistroDetalle registroDetalle = source.getRegistroDetalle();
						PeticionRegistroGeiser target = new PeticionRegistroGeiser();
						target.setResumen(StringUtils.stripToNull(source.getRegistroDetalle().getExtracto()));
						target.setTipoAsiento(TipoAsiento.SALIDA);
						target.setOrganoOrigen(source.getOrigen().getCodigo());
//						Informar destino si es administarción
						if (registroDetalle.getInteresados() != null && registroDetalle.getInteresados().size() > 0) {
							for (Interesado interesado: registroDetalle.getInteresados()) {
								if (interesado.getTipo() == 1L) {
									target.setOrganoDestino(interesado.getCodigoDir3());
								}
							}
						}
						target.setDocumentacionFisica(DocumentacionFisica.getDocumentacionFisica(registroDetalle.getTipoDocumentacionFisica()));
						target.setRefExterna(StringUtils.stripToNull(registroDetalle.getReferenciaExterna()));
						target.setNuExpediente(StringUtils.stripToNull(registroDetalle.getExpediente()));
						target.setTipoTransporte(TipoTransporte.getTipoTransporte(registroDetalle.getTransporte()));
						target.setNuTransporte(StringUtils.stripToNull(registroDetalle.getNumeroTransporte()));
						target.setObservaciones(StringUtils.stripToNull(registroDetalle.getObservaciones()));
						target.setOficinaOrigen(StringUtils.stripToNull(registroDetalle.getOficinaOrigen().getCodigo()));
						target.setInteresados(convertirList(registroDetalle.getInteresados(), InteresadoG.class));
						if (source.getUsuario() != null)
							target.setUsuario(StringUtils.stripToNull(source.getUsuario().getUsuario().getDocumento()));
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<RegistroSir, PeticionRegistroEnvioGeiser>() {
					@Override
					public PeticionRegistroEnvioGeiser convert(RegistroSir source, Type<? extends PeticionRegistroEnvioGeiser> destinationType) {
						PeticionRegistroEnvioGeiser target = new PeticionRegistroEnvioGeiser();
						target.setResumen(StringUtils.stripToNull(source.getResumen()));
						target.setTipoAsiento(TipoAsiento.valueOf(source.getTipoRegistro().name()));
						target.setTipoEnvio(TipoEnvio.ENVIO_DESTINO);
////						Informar origen si es administarción
//						if (source.getInteresados() != null && source.getInteresados().size() > 0) {
//							for (InteresadoSir interesado: source.getInteresados()) {
//								if (interesado.getTipoInteresado() == 1L) {
//									target.setOrganoOrigen(interesado.getDocumentoIdentificacionInteresado());
//								}
//							}
//						}
						target.setOrganoDestino(StringUtils.stripToNull(source.getCodigoUnidadTramitacionDestino()));
						target.setOrganoOrigen(source.getCodigoUnidadTramitacionOrigen());
						target.setOficinaOrigen(StringUtils.stripToNull(source.getCodigoEntidadRegistralOrigen()));
						target.setDocumentacionFisica(DocumentacionFisica.getDocumentacionFisicaName(source.getDocumentacionFisica()));
						target.setRefExterna(StringUtils.stripToNull(source.getReferenciaExterna()));
						target.setNuExpediente(StringUtils.stripToNull(source.getNumeroExpediente()));
						target.setTipoTransporte(TipoTransporte.getTipoTransporteName(source.getTipoTransporte()));
						target.setNuTransporte(StringUtils.stripToNull(source.getNumeroTransporte()));
						target.setObservaciones(StringUtils.stripToNull(source.getObservacionesApunte()));
						target.setInteresados(convertirList(source.getInteresados(), InteresadoG.class));
						if (source.getDocumentoUsuario() != null)
							target.setUsuario(StringUtils.stripToNull(source.getDocumentoUsuario()));
						
						if (source.getAnexos() != null && ! source.getAnexos().isEmpty()) {
							List<AnexoG> annexos = new ArrayList<AnexoG>();
							for (AnexoSir anexoSir: source.getAnexos()) {
								AnexoG anexo = convertir(anexoSir, AnexoG.class);
								if (anexo != null)
									annexos.add(anexo);
							}
							target.setAnexos(annexos);
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<Interesado, InteresadoG>() {
					@Override
					public InteresadoG convert(Interesado source, Type<? extends InteresadoG> destinationType) {
						InteresadoG target = new InteresadoG();
						target.setNombre(StringUtils.stripToNull(source.getNombre()));
						target.setPrimerApellido(StringUtils.stripToNull(source.getApellido1()));
						target.setSegundoApellido(StringUtils.stripToNull(source.getApellido2()));
						target.setTipoDocumento(TipoDocumento.getTipoDocumento(source.getTipoDocumentoIdentificacion()));
						target.setDocumento(StringUtils.stripToNull(source.getDocumento()));
						target.setEmail(StringUtils.stripToNull(source.getEmail()));
						target.setTelefono(StringUtils.stripToNull(source.getTelefono()));
						target.setCanalNotificacion(CanalNotificacion.getCanalNotificacion(source.getCanal()));
						target.setDireccionElectronica(StringUtils.stripToNull(source.getDireccionElectronica()));
						CatPais catPais = source.getPais(); 
						if (catPais != null) {
							if (catPais.getCodigoPais() == null) { //TODO: perquè no carrega? *revisar si està carregat
								try {
									catPais = catPaisEjb.findById(catPais.getId());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							target.setPais(StringUtils.stripToNull(String.valueOf(catPais.getCodigoPais())));
						}
						CatProvincia catProvincia = source.getProvincia();
						if (catProvincia != null) {
							if (catProvincia.getCodigoProvincia() == null) { //TODO: perquè no carrega? *revisar si està carregat
								try {
									catProvincia = catProvinciaEjb.findById(catProvincia.getId());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							target.setProvincia(StringUtils.stripToNull(String.valueOf(catProvincia.getCodigoProvincia())));
						}
						CatLocalidad catLocalidad = source.getLocalidad();
						if (catLocalidad != null) {
							if (catLocalidad.getCodigoLocalidad() == null) { //TODO: perquè no carrega? *revisar si està carregat
								try {
									catLocalidad = catLocalidadEjb.findById(catLocalidad.getId());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							target.setMunicipio(StringUtils.stripToNull(String.valueOf(catLocalidad.getCodigoLocalidad())));
						}
						target.setDireccion(StringUtils.stripToNull(source.getDireccion()));
						target.setCp(StringUtils.stripToNull(source.getCp()));
						target.setRazonSocial(StringUtils.stripToNull(source.getRazonSocial()));
						target.setObservaciones(StringUtils.stripToNull(source.getObservaciones()));
						if (source.getIsRepresentante()) {
							target.setRepresentante(
									convertir(
											source.getRepresentante(), 
											InteresadoG.class));
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<InteresadoSir, InteresadoG>() {
					@Override
					public InteresadoG convert(InteresadoSir source, Type<? extends InteresadoG> destinationType) {
						InteresadoG target = new InteresadoG();
						target.setNombre(StringUtils.stripToNull(source.getNombreInteresado()));
						target.setPrimerApellido(StringUtils.stripToNull(source.getPrimerApellidoInteresado()));
						target.setSegundoApellido(StringUtils.stripToNull(source.getSegundoApellidoInteresado()));
						target.setTipoDocumento(TipoDocumento.getTipoDocumentoName(source.getTipoDocumentoIdentificacionInteresado()));
						target.setDocumento(StringUtils.stripToNull(source.getDocumentoIdentificacionInteresado()));
						target.setEmail(StringUtils.stripToNull(source.getCorreoElectronicoInteresado()));
						target.setTelefono(StringUtils.stripToNull(source.getTelefonoInteresado()));
						target.setCanalNotificacion(CanalNotificacion.getCanalNotificacionName(source.getCanalPreferenteComunicacionInteresado()));
						if (source.getCodigoPaisInteresado() != null)
							target.setPais(StringUtils.stripToNull(String.valueOf(source.getCodigoPaisInteresado())));
						if (source.getCodigoProvinciaInteresado() != null)
							target.setProvincia(StringUtils.stripToNull(String.valueOf(source.getCodigoProvinciaInteresado())));
						if (source.getCodigoMunicipioInteresado() != null)
							target.setMunicipio(StringUtils.stripToNull(String.valueOf(source.getCodigoMunicipioInteresado())));
						target.setDireccion(StringUtils.stripToNull(source.getDireccionInteresado()));
						target.setCp(StringUtils.stripToNull(source.getCodigoPostalInteresado()));
						target.setRazonSocial(StringUtils.stripToNull(source.getRazonSocialInteresado()));
						target.setDireccionElectronica(StringUtils.stripToNull(source.getDireccionElectronicaHabilitadaInteresado()));
						target.setObservaciones(StringUtils.stripToNull(source.getObservaciones()));
						if (source.getRepresentante()) {
							target.setRepresentante(new InteresadoG());
							target.getRepresentante().setNombre(StringUtils.stripToNull(source.getNombreInteresado()));
							target.getRepresentante().setPrimerApellido(StringUtils.stripToNull(source.getPrimerApellidoInteresado()));
							target.getRepresentante().setSegundoApellido(StringUtils.stripToNull(source.getSegundoApellidoInteresado()));
							target.getRepresentante().setTipoDocumento(TipoDocumento.getTipoDocumentoName(source.getTipoDocumentoIdentificacionInteresado()));
							target.getRepresentante().setDocumento(StringUtils.stripToNull(source.getDocumentoIdentificacionInteresado()));
							target.getRepresentante().setEmail(StringUtils.stripToNull(source.getCorreoElectronicoInteresado()));
							target.getRepresentante().setTelefono(StringUtils.stripToNull(source.getTelefonoInteresado()));
							target.getRepresentante().setCanalNotificacion(CanalNotificacion.getCanalNotificacionName(source.getCanalPreferenteComunicacionInteresado()));
							if (source.getCodigoPaisInteresado() != null)
								target.getRepresentante().setPais(StringUtils.stripToNull(String.valueOf(source.getCodigoPaisInteresado())));
							if (source.getCodigoProvinciaInteresado() != null)
								target.getRepresentante().setProvincia(StringUtils.stripToNull(String.valueOf(source.getCodigoProvinciaInteresado())));
							if (source.getCodigoMunicipioInteresado() != null)
								target.getRepresentante().setMunicipio(StringUtils.stripToNull(String.valueOf(source.getCodigoMunicipioInteresado())));
							target.getRepresentante().setDireccion(StringUtils.stripToNull(source.getDireccionInteresado()));
							target.getRepresentante().setCp(StringUtils.stripToNull(source.getCodigoPostalInteresado()));
							target.getRepresentante().setRazonSocial(StringUtils.stripToNull(source.getRazonSocialInteresado()));
							target.getRepresentante().setDireccionElectronica(StringUtils.stripToNull(source.getDireccionElectronicaHabilitadaInteresado()));
							target.getRepresentante().setObservaciones(StringUtils.stripToNull(source.getObservaciones()));
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<InteresadoG, InteresadoSir>() {
					@Override
					public InteresadoSir convert(InteresadoG source, Type<? extends InteresadoSir> destinationType) {
						InteresadoSir target = new InteresadoSir();
						target.setNombreInteresado(source.getNombre());
						target.setPrimerApellidoInteresado(source.getPrimerApellido());
						target.setSegundoApellidoInteresado(source.getSegundoApellido());
						if (source.getTipoDocumento() != null)
							target.setTipoDocumentoIdentificacionInteresado(source.getTipoDocumento().getName());
						target.setDocumentoIdentificacionInteresado(source.getDocumento());
						target.setCorreoElectronicoInteresado(source.getEmail());
						target.setTelefonoInteresado(source.getTelefono());
						if (source.getCanalNotificacion() != null)
							target.setCanalPreferenteComunicacionInteresado(source.getCanalNotificacion().getName());
						
						target.setCodigoPaisInteresado(source.getPais());
						target.setCodigoProvinciaInteresado(source.getProvincia());
						target.setCodigoMunicipioInteresado(source.getMunicipio());
						
						target.setDireccionInteresado(source.getDireccion());
						target.setCodigoPostalInteresado(source.getCp());
						target.setRazonSocialInteresado(source.getRazonSocial());
						target.setDireccionElectronicaHabilitadaInteresado(source.getDireccionElectronica());
						target.setObservaciones(source.getObservaciones());
						
						if (source.getRepresentante() != null) {
							InteresadoG representante = source.getRepresentante();
							target.setNombreRepresentante(representante.getNombre());
							target.setPrimerApellidoRepresentante(representante.getPrimerApellido());
							target.setSegundoApellidoRepresentante(representante.getSegundoApellido());
							if (representante.getTipoDocumento() != null)
								target.setTipoDocumentoIdentificacionRepresentante(representante.getTipoDocumento().getName());
							target.setDocumentoIdentificacionRepresentante(representante.getDocumento());
							target.setCorreoElectronicoRepresentante(representante.getEmail());
							target.setTelefonoRepresentante(representante.getTelefono());
							if (representante.getCanalNotificacion() != null)
								target.setCanalPreferenteComunicacionRepresentante(representante.getCanalNotificacion().getName());
							
							target.setCodigoPaisRepresentante(representante.getPais());
							target.setCodigoProvinciaRepresentante(representante.getProvincia());
							target.setCodigoMunicipioRepresentante(representante.getMunicipio());
							
							target.setDireccionRepresentante(representante.getDireccion());
							target.setCodigoPostalRepresentante(representante.getCp());
							target.setRazonSocialRepresentante(representante.getRazonSocial());
							target.setDireccionElectronicaHabilitadaRepresentante(representante.getDireccionElectronica());
							target.setObservaciones(representante.getObservaciones());
						}
						
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<AnexoSir, AnexoG>() {
					@Override
					public AnexoG convert(AnexoSir source, Type<? extends AnexoG> destinationType) {
						AnexoG target = null;
						AnexoSir documentoOriginal = source.getDocumento();
						// Firma dettached
						if (documentoOriginal != null) {
							target = new AnexoG();
							// Documento
							target.setTitulo(documentoOriginal.getNombreFichero());
							target.setValidezDocumento(ValidezDocumento.getValidezDocumento(documentoOriginal.getValidezDocumento()));
							target.setTipoDocumentoAnexo(TipoDocumentoAnexo.getTipoDocumento(documentoOriginal.getTipoDocumento()));
							byte[] anexoData = documentoOriginal.getAnexoData();
							target.setAnexoBase64(Base64.encodeBase64String(anexoData));
							try {
								target.setHash(RegwebUtils.obtenerHash(anexoData));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							target.setTipoMime(documentoOriginal.getTipoMIME());
							target.setObservaciones(documentoOriginal.getObservaciones());
	//						target.setTamanioFichero(source.getTamano());
							
							// Firma
							target.setTipoFirma(TipoFirma.EXTERNA);
							byte[] firmaData = source.getAnexoData();
							target.setFirmaBase64(Base64.encodeBase64String(firmaData));
							try {
								target.setHashFirma(RegwebUtils.obtenerHash(firmaData));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							target.setNombreFirma(source.getNombreFichero());
	//						target.setTamanioFicheroFirma(firma.getTamano());
							target.setTipoMimeFirma(source.getTipoMIME());
						}
//						AnexoSir isDocumentOfFirmaDettached = anexoSirEjb.findByIdentificadorDocumentoFirmado(identificadorDocumento)
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<AnexoG, AnexoSir>() {
					@Override
					public AnexoSir convert(AnexoG source, Type<? extends AnexoSir> destinationType) {
						AnexoSir target = null;
						// Podria no venir informado el contenido
						if (source.getAnexoBase64() != null) {
							// ================= SEGMENTO 1: DOCUMENT ==================
							target =  new AnexoSir();
							// Documento
							target.setNombreFichero(source.getTitulo());
							target.setIdentificadorFichero(source.getIdentificador());
							target.setHash(source.getHashBase64());

							ArchivoManager am = null;
	                        try {
	                            am = new ArchivoManager(archivoEjb, source.getTitulo(), source.getTipoMime(), Base64.decodeBase64(source.getAnexoBase64()));
	                            target.setAnexo(am.prePersist());
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                        if (source.getValidezDocumento() != null)
	                        	target.setValidezDocumento(source.getValidezDocumento().getValue());
							if (source.getTipoDocumentoAnexo() != null)
								target.setTipoDocumento(source.getTipoDocumentoAnexo().getValue());
							target.setTipoMIME(source.getTipoMime());
							target.setObservaciones(source.getObservaciones());
							target.setAnexoData(source.getAnexo());
							
//							 Firma externa
							if (source.getTipoFirma().equals(TipoFirma.EXTERNA)) {
								target.setFirma(source.getFirmaBase64());
							}
							if (source.getTipoFirma().equals(TipoFirma.EMBEBIDA)) {
								target.setIdentificadorDocumentoFirmado(source.getIdentificador());
							}
						}
//						Es una firma dettached
						if (source.getAnexoBase64() == null && source.getFirmaBase64() != null) {
							// ================= SEGMENTO 2: FIRMA ==================
							target =  new AnexoSir();
							target.setNombreFichero(source.getNombreFirma());
							target.setIdentificadorFichero(source.getIdentificador());
							target.setHash(source.getHashFirmaBase64());
							target.setAnexoFirma(true);
							ArchivoManager am = null;
	                        try {
	                            am = new ArchivoManager(archivoEjb, source.getNombreFirma(), source.getTipoMimeFirma(), Base64.decodeBase64(source.getFirmaBase64()));
	                            target.setAnexo(am.prePersist());
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                        target.setTipoMIME(source.getTipoMimeFirma());
	                        target.setObservaciones(source.getObservaciones());
//	                        target.setFirma(source.getFirmaBase64());
	                        if (source.getValidezDocumento() != null)
	                        	target.setValidezDocumento(source.getValidezDocumento().getValue());
	                        target.setTipoDocumento(CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(TIPO_DOCUMENTO_FICHERO_TECNICO));
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<RegistroSir, PeticionBusquedaTramitGeiser>() {
					@Override
					public PeticionBusquedaTramitGeiser convert(RegistroSir source, Type<? extends PeticionBusquedaTramitGeiser> destinationType) {
						PeticionBusquedaTramitGeiser target = new PeticionBusquedaTramitGeiser();
						InteresadoSir primerInteresado = source.getInteresados().get(0);

			    		// Consultar con número origen o destino
						if (source.getNumeroRegistroOrigen() != null)
							target.setNuRegistro(source.getNumeroRegistroOrigen());
						else
							target.setNuRegistro(source.getNumeroRegistro());
						target.setTipoDocumentoInteresadoRepre(TipoDocumento.getTipoDocumentoName(primerInteresado.getTipoDocumentoIdentificacionInteresado()));
						target.setDocumentoInteresadoRepre(primerInteresado.getDocumentoIdentificacionInteresado());
						target.setIncluirEnviosSir(true);
						target.setOficinaOrigen(source.getCodigoEntidadRegistralOrigen());
						target.setUsuario(source.getDocumentoUsuario());
//						target.setFechaRegistroInicio(source.getFechaRegistro());
//						target.setFechaRegistroFinal(new Date());
//						target.setOficinaOrigen(source.getCodigoEntidadRegistralOrigen());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<RegistroSir, PeticionConsultaGeiser>() {
					@Override
					public PeticionConsultaGeiser convert(RegistroSir source, Type<? extends PeticionConsultaGeiser> destinationType) {
						PeticionConsultaGeiser target = new PeticionConsultaGeiser();
						target.setNuRegistro(source.getNumeroRegistro());
						target.setOficinaOrigen(source.getCodigoEntidadRegistralOrigen());
						target.setUsuario(source.getDocumentoUsuario());
						target.setIncluirContenidoAnexo(source.isIncluirContenidoAnexo());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ApunteRegistro, RegistroSir>() {
					@Override
					public RegistroSir convert(ApunteRegistro source, Type<? extends RegistroSir> destinationType) {
						RegistroSir target = new RegistroSir();
						String nombreOficinaOrigen = null, nombreOficinaDestino = null, nombreUnidadOrigen = null, nombreUnidadDestino = null;
						target.setEstado(EstadoRegistroSir.getEstadoRegistroSirByName(source.getEstado().name()));
						target.setFechaRecepcion(new Date());
						target.setNumeroRegistroOrigen(source.getNuRegistroOrigen());
						
						// Segmento De_Origen_o_Remitente [OFICNA / ENTIDAD]
						// Oficina origen
						String oficinaUnidadOrigen = source.getCdAmbitoOrigen();
						if (StringUtils.isNotEmpty(oficinaUnidadOrigen) && oficinaUnidadOrigen.startsWith("O")) { // Oficina
							target.setCodigoEntidadRegistralOrigen(StringUtils.stripToNull(oficinaUnidadOrigen));
							if (StringUtils.isNotEmpty(source.getNombreAmbitoOrigen())) {
								target.setDecodificacionEntidadRegistralOrigen(StringUtils.stripToNull(source.getNombreAmbitoOrigen()));
							} else {
								nombreOficinaOrigen = Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(), oficinaUnidadOrigen, RegwebConstantes.OFICINA);
								target.setDecodificacionEntidadRegistralOrigen(nombreOficinaOrigen);
							}
						}
					
						// Entidad origen
						if (StringUtils.isNotEmpty(source.getOrganoOrigen())) {
							target.setCodigoUnidadTramitacionOrigen(source.getOrganoOrigen());
		                    if (StringUtils.isNotEmpty(source.getOrganoOrigenDenominacion())) {
		                    	target.setDecodificacionUnidadTramitacionOrigen(StringUtils.stripToNull(source.getOrganoOrigenDenominacion()));
		                    } else {
		                    	nombreUnidadOrigen = Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(), source.getOrganoOrigen(), RegwebConstantes.UNIDAD);
		                    	target.setDecodificacionUnidadTramitacionOrigen(nombreUnidadOrigen);
		                    }
		                }
						
						target.setNumeroRegistro(StringUtils.stripToNull(source.getNuRegistro()));
						target.setTimestampRegistro(StringUtils.stripToNull(source.getTimeStampRegistro()));
						target.setFechaRegistro(source.getFechaRegistro());
						
						// Segmento De_Destino [OFICNA / ENTIDAD]
						// Oficina destino
						if (StringUtils.isNotEmpty(source.getCdAmbitoActual()) && source.getCdAmbitoActual().startsWith("O"))
							target.setCodigoEntidadRegistral(StringUtils.stripToNull(source.getCdAmbitoActual()));
						else
							target.setCodigoEntidadRegistral(StringUtils.stripToNull(oficinaUnidadOrigen));
						// Oficina destino interno
						String oficinaUnidadDestino = source.getCdAmbitoDestino();
						if (StringUtils.isNotEmpty(oficinaUnidadDestino) && oficinaUnidadDestino.startsWith("O")) {
							target.setCodigoEntidadRegistralDestino(oficinaUnidadDestino);
							if (StringUtils.isNotEmpty(source.getNombreAmbitoDestino())) {
								target.setDecodificacionEntidadRegistralDestino(source.getNombreAmbitoDestino());
							} else {
								nombreOficinaDestino = Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(), oficinaUnidadDestino, RegwebConstantes.OFICINA);
								target.setDecodificacionEntidadRegistralDestino(nombreOficinaDestino);
							}
		                } else { // Se ha aceptado en oficina y se ha enviado a la unidad, entidad registral destino = oficina donde se ha aceptado
		                	target.setCodigoEntidadRegistralDestino(StringUtils.stripToNull(oficinaUnidadOrigen));
							if (StringUtils.isNotEmpty(source.getNombreAmbitoOrigen())) {
								target.setDecodificacionEntidadRegistralDestino(StringUtils.stripToNull(source.getNombreAmbitoOrigen()));
							} else {
								nombreOficinaOrigen = Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(), oficinaUnidadOrigen, RegwebConstantes.OFICINA);
								target.setDecodificacionEntidadRegistralDestino(nombreOficinaOrigen);
							}
		                }
						// Entidad destino
						if (StringUtils.isNotEmpty(source.getOrganoDestino())) {
							target.setCodigoUnidadTramitacionDestino(source.getOrganoDestino());
		                    if (StringUtils.isNotEmpty(source.getOrganoDestinoDenominacion())) {
		                    	target.setDecodificacionUnidadTramitacionDestino(StringUtils.stripToNull(source.getOrganoDestinoDenominacion()));
		                    } else {
		                    	nombreUnidadDestino = Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(), source.getOrganoDestino(), RegwebConstantes.UNIDAD);
		                    	target.setDecodificacionUnidadTramitacionDestino(nombreUnidadDestino);
		                    }
		                }
						
						// Segmento De_Asunto de_Asunto
						target.setResumen(source.getResumen());
						target.setCodigoAsunto(source.getCdAsunto());
						target.setReferenciaExterna(source.getReferenciaExterna());
						target.setNumeroExpediente(source.getNuExpediente());
						
						// Segmento De_Internos_Control
						target.setAplicacion("GEISER");
						target.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
						target.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());
						target.setNumeroTransporte(source.getNuTransporte());
						target.setNombreUsuario(source.getNombreUsuario());
						target.setContactoUsuario(source.getContactoUsuario());
						target.setObservacionesApunte(source.getObservaciones());
						target.setDecodificacionEntidadRegistralInicio(source.getOrganoOrigenDenominacion());
						
						// [OFICINA ORIGEN]
						if (StringUtils.isNotEmpty(oficinaUnidadOrigen) && oficinaUnidadOrigen.startsWith("O")) {
							target.setCodigoEntidadRegistralInicio(oficinaUnidadOrigen);
							if (StringUtils.isNotEmpty(source.getNombreAmbitoOrigen())) {
			                	target.setDecodificacionEntidadRegistralInicio(source.getNombreAmbitoOrigen());
			                } else {
			                	target.setDecodificacionEntidadRegistralInicio(nombreOficinaOrigen);
			                }
						}
						// Otros
						if (source.getTipoTransporte() != null)
							target.setTipoTransporte(source.getTipoTransporte().getName());
						if (source.getTipoAsiento() != null)
							target.setTipoRegistro(TipoRegistro.valueOf(source.getTipoAsiento().name()));
						if (source.getDocumentacionFisica() != null)
							target.setDocumentacionFisica(source.getDocumentacionFisica().getName());
						target.setIndicadorPrueba(IndicadorPrueba.NORMAL);
						
						// Formulario
						target.setExpone(source.getExpone());
						target.setSolicita(source.getSolicita());

						// Si se trata de una Salida y no tiene Interesados creamos uno a partir de la Entidad destino
						if (source.getTipoAsiento().equals(TipoAsiento.SALIDA) && (source.getAnexos() == null || source.getAnexos().isEmpty())) {
							InteresadoSir intresadoSirSalida = new InteresadoSir();
							if (source.getOrganoDestino() != null) {
								intresadoSirSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
								intresadoSirSalida.setDocumentoIdentificacionInteresado(source.getOrganoDestino());
								
								// [ENTIDAD DESTINO]
								if (source.getNombreAmbitoDestino() != null) {
									intresadoSirSalida.setRazonSocialInteresado(source.getOrganoDestinoDenominacion());
								} else {
									intresadoSirSalida.setRazonSocialInteresado(nombreUnidadDestino);
								}
							} else {
								try {
									Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());

					                intresadoSirSalida.setTipoDocumentoIdentificacionInteresado(TipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE.getValue());
					                
					                if (StringUtils.isNotEmpty(source.getCdAmbitoOrigen())) {
										OficinaTF oficinaTF = oficinasService.obtenerOficina(source.getCdAmbitoOrigen(),null,null);
						                intresadoSirSalida.setDocumentoIdentificacionInteresado(oficinaTF.getCodUoResponsable());
						                intresadoSirSalida.setRazonSocialInteresado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(),oficinaTF.getCodUoResponsable(),RegwebConstantes.UNIDAD));
					                } else if (StringUtils.isNotEmpty(oficinaUnidadOrigen) && oficinaUnidadOrigen.startsWith("E")){ // Entidad
					                	intresadoSirSalida.setDocumentoIdentificacionInteresado(oficinaUnidadOrigen);
						                intresadoSirSalida.setRazonSocialInteresado(source.getNombreAmbitoOrigen());
					                }
					            } catch (Exception e) {
									e.printStackTrace();
								}

							}
							
							target.getInteresados().add(intresadoSirSalida);
						} else if (source.getInteresados() != null && ! source.getInteresados().isEmpty()) {
							target.setInteresados(convertirList(source.getInteresados(), InteresadoSir.class));
						}
						if (source.getAnexos() != null && ! source.getAnexos().isEmpty()) {
							List<AnexoSir> anexosSir = new ArrayList<AnexoSir>();
							for (AnexoG anexoGeiser: source.getAnexos()) {
								if (anexoGeiser.getAnexoBase64() != null) {
									anexosSir.add(convertir(anexoGeiser, AnexoSir.class));
								}
								if (anexoGeiser.getFirmaBase64() != null) {
									anexoGeiser.setAnexoBase64(null); // per no crear un altre converter de firmes
									anexosSir.add(convertir(anexoGeiser, AnexoSir.class));
								}
							}
							target.setAnexos(anexosSir);
						}
						
						return target;
					}
				});
	}
	
	public <T> T convertir(Object source, Class<T> targetType) {
		if (source == null)
			return null;
		return getMapperFacade().map(source, targetType);
	}
	public <T> List<T> convertirList(List<?> items, Class<T> targetType) {
		if (items == null)
			return null;
		return getMapperFacade().mapAsList(items, targetType);
	}

	private MapperFacade getMapperFacade() {
		return mapperFactory.getMapperFacade();
	}

}
