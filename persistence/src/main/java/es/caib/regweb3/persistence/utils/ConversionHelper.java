package es.caib.regweb3.persistence.utils;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.plugin.geiser.api.AnexoG;
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

import es.caib.regweb3.model.AnexoSir;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.InteresadoSir;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.persistence.ejb.AnexoSirLocal;
import es.caib.regweb3.utils.RegwebUtils;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

@Component
public class ConversionHelper {

	@EJB(mappedName = "regweb3/AnexoSirEJB/local")
	AnexoSirLocal anexoSirEjb;
	
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
						if (source.getPais() != null)
							target.setPais(StringUtils.stripToNull(String.valueOf(source.getPais().getCodigoPais())));
						if (source.getProvincia() != null)
							target.setProvincia(StringUtils.stripToNull(String.valueOf(source.getProvincia().getCodigoProvincia())));
						if (source.getLocalidad() != null)
							target.setMunicipio(StringUtils.stripToNull(String.valueOf(source.getLocalidad().getCodigoLocalidad())));
						target.setDireccion(StringUtils.stripToNull(source.getDireccion()));
						target.setCp(StringUtils.stripToNull(source.getCp()));
						target.setRazonSocial(StringUtils.stripToNull(source.getRazonSocial()));
						target.setDireccionElectronica(StringUtils.stripToNull(source.getDireccionElectronica()));
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
				new CustomConverter<RegistroSir, PeticionBusquedaTramitGeiser>() {
					@Override
					public PeticionBusquedaTramitGeiser convert(RegistroSir source, Type<? extends PeticionBusquedaTramitGeiser> destinationType) {
						PeticionBusquedaTramitGeiser target = new PeticionBusquedaTramitGeiser();
						InteresadoSir primerInteresado = source.getInteresados().get(0);
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
