package org.plugin.geiser.apb.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.plugin.geiser.api.AnexoG;
import org.plugin.geiser.api.ApunteRegistro;
import org.plugin.geiser.api.DocumentHelper;
import org.plugin.geiser.api.EstadoRegistro;
import org.plugin.geiser.api.EstadoTramit;
import org.plugin.geiser.api.EstadoTramitacion;
import org.plugin.geiser.api.GeiserPluginException;
import org.plugin.geiser.api.InteresadoG;
import org.plugin.geiser.api.PeticionBusquedaGeiser;
import org.plugin.geiser.api.PeticionBusquedaTramitGeiser;
import org.plugin.geiser.api.PeticionConsultaGeiser;
import org.plugin.geiser.api.PeticionRegistroEnvioGeiser;
import org.plugin.geiser.api.PeticionRegistroGeiser;
import org.plugin.geiser.api.Respuesta;
import org.plugin.geiser.api.RespuestaBusquedaTramitGeiser;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.plugin.geiser.api.TipoAsiento;
import org.plugin.geiser.api.TipoDocumento;
import org.plugin.geiser.api.TipoRespuesta;
import org.plugin.geiser.api.ws.AnexoType;
import org.plugin.geiser.api.ws.ApunteRegistroType;
import org.plugin.geiser.api.ws.CanalNotificacionEnum;
import org.plugin.geiser.api.ws.EstadoAsientoEnum;
import org.plugin.geiser.api.ws.EstadoTramitacionRegistroType;
import org.plugin.geiser.api.ws.InteresadoType;
import org.plugin.geiser.api.ws.PeticionBusquedaEstadoTramitacionType;
import org.plugin.geiser.api.ws.PeticionBusquedaType;
import org.plugin.geiser.api.ws.PeticionConsultaType;
import org.plugin.geiser.api.ws.PeticionRegistroEnvioSimpleType;
import org.plugin.geiser.api.ws.PeticionRegistroType;
import org.plugin.geiser.api.ws.RespuestaType;
import org.plugin.geiser.api.ws.ResultadoBusquedaEstadoTramitacionType;
import org.plugin.geiser.api.ws.ResultadoConsultaType;
import org.plugin.geiser.api.ws.ResultadoRegistroType;
import org.plugin.geiser.api.ws.TipoAsientoEnum;
import org.plugin.geiser.api.ws.TipoDocAnexoEnum;
import org.plugin.geiser.api.ws.TipoDocumentacionFisicaEnum;
import org.plugin.geiser.api.ws.TipoEnvio;
import org.plugin.geiser.api.ws.TipoFirmaEnum;
import org.plugin.geiser.api.ws.TipoIdentificacionEnum;
import org.plugin.geiser.api.ws.TipoTransporteEnum;
import org.plugin.geiser.api.ws.ValidezDocumentoEnum;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

@Component
public class ConversionPluginHelper {

	private MapperFactory mapperFactory;
	
	public ConversionPluginHelper () {
		mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<PeticionRegistroGeiser, PeticionRegistroType>() {
					@Override
					public PeticionRegistroType convert(PeticionRegistroGeiser source, Type<? extends PeticionRegistroType> destinationType) {
						PeticionRegistroType target = new PeticionRegistroType();
						target.setResumen(source.getResumen() + " - " + new Date().getTime());
						target.setEstado(EstadoAsientoEnum.FINALIZADO);
						target.setCdAsunto(source.getCdAsunto());
						if (source.getTipoAsiento() != null)
							target.setTipoAsiento(TipoAsientoEnum.fromValue(source.getTipoAsiento().name()));
						target.setCdOrganoOrigen(source.getOrganoOrigen());
						target.setCdOrganoDestino(source.getOrganoDestino());
						target.setDocumentacionFisica(TipoDocumentacionFisicaEnum.DOCUMENTACION_FISICA_REQUERIDA);
						target.setReferenciaExterna(source.getRefExterna());
						target.setNuExpediente(source.getNuExpediente());
						if (source.getTipoTransporte() != null)
							target.setTipoTransporte(TipoTransporteEnum.fromValue(source.getTipoTransporte().name()));
						target.setNuTransporte(source.getNuTransporte());
						target.setObservaciones(source.getObservaciones());
						target.setCdAsunto(source.getCdAsunto()); //TODO: revisar
						if (source.getInteresados() != null) {
							for (InteresadoG interesado: source.getInteresados()) {
								target.getInteresados().add(
										convertir(
												interesado, 
												InteresadoType.class));	
							}
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<PeticionRegistroEnvioGeiser, PeticionRegistroEnvioSimpleType>() {
					@Override
					public PeticionRegistroEnvioSimpleType convert(PeticionRegistroEnvioGeiser source, Type<? extends PeticionRegistroEnvioSimpleType> destinationType) {
						PeticionRegistroEnvioSimpleType target = new PeticionRegistroEnvioSimpleType();
						target.setResumen(source.getResumen());
						if (source.getTipoAsiento() != null)
							target.setTipoAsiento(TipoAsientoEnum.fromValue(source.getTipoAsiento().name()));
						if (source.getTipoEnvio() != null)
							target.setTipoEnvio(TipoEnvio.fromValue(source.getTipoEnvio().name()));
						if (source.getTipoAsiento() != null && source.getTipoAsiento().equals(TipoAsiento.SALIDA))
							target.setCdOrganoOrigen(source.getOrganoOrigen());
						target.setCdOrganoDestino(source.getOrganoDestino());
						if (source.getDocumentacionFisica() != null)
							target.setDocumentacionFisica(TipoDocumentacionFisicaEnum.fromValue(source.getDocumentacionFisica().name()));
						target.setReferenciaExterna(source.getRefExterna());
						target.setNuExpediente(source.getNuExpediente());
						if (source.getTipoTransporte() != null)
							target.setTipoTransporte(TipoTransporteEnum.fromValue(source.getTipoTransporte().name()));
						target.setNuTransporte(source.getNuTransporte());
						target.setObservaciones(source.getObservaciones());
						target.setCdAsunto(source.getCdAsunto()); //TODO: revisar
						if (source.getInteresados() != null) {
							for (InteresadoG interesado: source.getInteresados()) {
								target.getInteresados().add(
										convertir(
												interesado, 
												InteresadoType.class));	
							}
						}
						if (source.getAnexos() != null) {
							for (AnexoG anexo: source.getAnexos()) {
								target.getAnexos().add(
										convertir(
												anexo, 
												AnexoType.class));	
							}
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<InteresadoG, InteresadoType>() {
					@Override
					public InteresadoType convert(InteresadoG source, Type<? extends InteresadoType> destinationType) {
						InteresadoType target = new InteresadoType();
						target.setNombreInteresado(source.getNombre());
						target.setPrimerApellidoInteresado(source.getPrimerApellido());
						target.setSegundoApellidoInteresado(source.getSegundoApellido());
						if (source.getTipoDocumento() != null)
							target.setTipoIdentificadorInteresado(convertir(source.getTipoDocumento(), TipoIdentificacionEnum.class));
						target.setIdentificadorInteresado(source.getDocumento());
						target.setMailInteresado(source.getEmail());
						target.setTelefonoInteresado(source.getTelefono());
						if (source.getCanalNotificacion() != null)
							target.setCanalNotificacionInteresado(CanalNotificacionEnum.fromValue(source.getCanalNotificacion().name()));
						if (source.getPais() != null)
							target.setCdPaisInteresado(source.getPais());
						if (source.getProvincia() != null)
							target.setCdProvinciaInteresado(source.getProvincia());
						if (source.getMunicipio() != null)
							target.setCdMunicipioInteresado(source.getMunicipio());
						target.setDireccionInteresado(source.getDireccion());
						target.setCodigoPostalInteresado(source.getCp());
						target.setRazonSocialInteresado(source.getRazonSocial());
						target.setDireccionElectronicaInteresado(source.getDireccionElectronica());
						target.setObservaciones(source.getObservaciones());
						InteresadoG sourceRepresentante = source.getRepresentante();
						if (sourceRepresentante != null) {
							InteresadoType targetRepresentante = new InteresadoType();
							targetRepresentante.setNombreRepresentante(sourceRepresentante.getNombre());
							targetRepresentante.setPrimerApellidoRepresentante(sourceRepresentante.getPrimerApellido());
							targetRepresentante.setSegundoApellidoRepresentante(sourceRepresentante.getSegundoApellido());
							if (sourceRepresentante.getTipoDocumento() != null)
								targetRepresentante.setTipoIdentificadorRepresentante(convertir(sourceRepresentante.getTipoDocumento(), TipoIdentificacionEnum.class));
							targetRepresentante.setIdentificadorRepresentante(sourceRepresentante.getDocumento());
							targetRepresentante.setMailRepresentante(sourceRepresentante.getEmail());
							targetRepresentante.setTelefonoRepresentante(sourceRepresentante.getTelefono());
							if (sourceRepresentante.getCanalNotificacion() != null)
								targetRepresentante.setCanalNotificacionRepresentante(CanalNotificacionEnum.fromValue(sourceRepresentante.getCanalNotificacion().name()));
							if (sourceRepresentante.getPais() != null)
								targetRepresentante.setCdPaisRepresentante(sourceRepresentante.getPais());
							if (sourceRepresentante.getProvincia() != null)
								targetRepresentante.setCdProvinciaRepresentante(sourceRepresentante.getProvincia());
							if (sourceRepresentante.getMunicipio() != null)
								targetRepresentante.setCdMunicipioRepresentante(sourceRepresentante.getMunicipio());
							targetRepresentante.setDireccionRepresentante(sourceRepresentante.getDireccion());
							targetRepresentante.setCodigoPostalRepresentante(sourceRepresentante.getCp());
							targetRepresentante.setRazonSocialRepresentante(sourceRepresentante.getRazonSocial());
							targetRepresentante.setDireccionElectronicaRepresentante(sourceRepresentante.getDireccionElectronica());
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<AnexoG, AnexoType>() {
					@Override
					public AnexoType convert(AnexoG source, Type<? extends AnexoType> destinationType) {
						AnexoType target = new AnexoType();
						target.setNombre(source.getTitulo());
						target.setValidez(ValidezDocumentoEnum.valueOf(source.getValidezDocumento().name()));
						target.setTipoDocumento(TipoDocAnexoEnum.valueOf(source.getTipoDocumentoAnexo().name()));
						target.setAnexo(source.getAnexoBase64());
//						target.setHash(DocumentHelper.getHash512Document(Base64.decodeBase64(source.getAnexoBase64())));
						if (source.getHash() != null)
							target.setHash(DocumentHelper.getHash512Document(Base64.decodeBase64(source.getAnexoBase64())));
						target.setTipoMime(source.getTipoMime());
						target.setObservaciones(source.getObservaciones());
						target.setTamanioFichero(source.getTamanioFichero());
						target.setTipoFirma(TipoFirmaEnum.fromValue(source.getTipoFirma().name()));
						if (source.getFirmaBase64() != null) {
							target.setTipoFirma(TipoFirmaEnum.valueOf(source.getTipoFirma().name()));
							target.setFirma(source.getFirmaBase64());
//							target.setHashFirma(DocumentHelper.getHash512Document(Base64.decodeBase64(source.getFirmaBase64())));
							if (source.getHashFirma() != null)
								target.setHashFirma(DocumentHelper.getHash512Document(Base64.decodeBase64(source.getFirmaBase64())));
							target.setNombreFirma(source.getNombreFirma());
							target.setTamanioFicheroFirma(source.getTamanioFicheroFirma());
							target.setTipoMimeFirma(source.getTipoMimeFirma());
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<PeticionConsultaGeiser, PeticionConsultaType>() {
					@Override
					public PeticionConsultaType convert(PeticionConsultaGeiser source, Type<? extends PeticionConsultaType> destinationType) {
						PeticionConsultaType target = new PeticionConsultaType();
						target.setNuRegistro(source.getNuRegistro());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<PeticionBusquedaGeiser, PeticionBusquedaType>() {
					@Override
					public PeticionBusquedaType convert(PeticionBusquedaGeiser source, Type<? extends PeticionBusquedaType> destinationType) {
						PeticionBusquedaType target = new PeticionBusquedaType();
						//TODO: conversión
						target.setTimestampRegistradoDesde(source.getFechaRegistroInicio().toString());
						target.setTimestampRegistradoHasta(source.getFechaRegistroFinal().toString());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<PeticionBusquedaTramitGeiser, PeticionBusquedaEstadoTramitacionType>() {
					@Override
					public PeticionBusquedaEstadoTramitacionType convert(PeticionBusquedaTramitGeiser source, Type<? extends PeticionBusquedaEstadoTramitacionType> destinationType) {
						PeticionBusquedaEstadoTramitacionType target = new PeticionBusquedaEstadoTramitacionType();
						target.setNuRegistro(source.getNuRegistro());
						if (source.getTipoDocumentoInteresadoRepre() != null)
							target.setTipoIdentificadorInteresadoRepresentante(convertir(source.getTipoDocumentoInteresadoRepre(), TipoIdentificacionEnum.class));
						target.setIdentificadorInteresadoRepresentante(source.getDocumentoInteresadoRepre());
						target.setIncluirEnviadosSIR(source.isIncluirEnviosSir());
//						target.setTimestampRegistradoDesde(source.getFechaRegistroInicio().toString());
//						target.setTimestampRegistradoHasta(source.getFechaRegistroFinal().toString());
//						target.setCdOficinaOrigen(source.getOficinaOrigen());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ResultadoRegistroType, RespuestaRegistroGeiser>() {
					@Override
					public RespuestaRegistroGeiser convert(ResultadoRegistroType source, Type<? extends RespuestaRegistroGeiser> destinationType) {
						RespuestaRegistroGeiser target = new RespuestaRegistroGeiser();
						target.setRespuesta(convertir(source.getRespuesta(), Respuesta.class));
						target.setNuRegistro(source.getApunte().getNuRegistro());
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
							Date fechaRegistro = sdf.parse(source.getApunte().getTimestampRegistrado());
							target.setFechaRegistro(fechaRegistro);
						} catch (ParseException e) {
							throw new GeiserPluginException("Ha habido un error interpretando la respuesta de GEISER");
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ResultadoBusquedaEstadoTramitacionType, RespuestaBusquedaTramitGeiser>() {
					@Override
					public RespuestaBusquedaTramitGeiser convert(ResultadoBusquedaEstadoTramitacionType source, Type<? extends RespuestaBusquedaTramitGeiser> destinationType) {
						RespuestaBusquedaTramitGeiser target = new RespuestaBusquedaTramitGeiser();
						target.setRespuesta(convertir(source.getRespuesta(), Respuesta.class));
						target.setEstadosTramitacionRegistro(convertirList(source.getEstadosTramitacion(), EstadoTramitacion.class));
						target.setNuTotalApuntes(source.getNuTotalAsientos());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<EstadoTramitacionRegistroType, EstadoTramitacion>() {
					@Override
					public EstadoTramitacion convert(EstadoTramitacionRegistroType source, Type<? extends EstadoTramitacion> destinationType) {
						EstadoTramitacion target = new EstadoTramitacion();
						target.setEstado(EstadoTramit.valueOf(source.getEstado().name()));
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
							if (source.getTimestampConfirmadoRechazado() != null) {
								Date fechaEstado = sdf.parse(source.getTimestampConfirmadoRechazado());
								target.setFechaEstado(fechaEstado);
							}
							Date fechaRegistro = sdf.parse(source.getTimestampRegistrado());
							target.setFechaRegistro(fechaRegistro);
						} catch (ParseException e) {
							throw new GeiserPluginException("Ha habido un error interpretando la respuesta de GEISER");
						}
						target.setIdentificadorIntercambioSIR(source.getIdentificadoresIntercambioSIR());
						target.setNuRegistro(source.getNuRegistro());
						target.setNuRegistroInterno(source.getNuRegistroInterno());
						target.setNuRegistroOrigen(source.getNuRegistroOrigen());
						target.setTipoAsiento(TipoAsiento.valueOf(source.getTipoAsiento().name()));
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ResultadoConsultaType, RespuestaConsultaGeiser>() {
					@Override
					public RespuestaConsultaGeiser convert(ResultadoConsultaType source, Type<? extends RespuestaConsultaGeiser> destinationType) {
						RespuestaConsultaGeiser target = new RespuestaConsultaGeiser();
						target.setRespuesta(convertir(source.getRespuesta(), Respuesta.class));
						target.setApuntes(convertirList(source.getApuntes(), ApunteRegistro.class));
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ApunteRegistroType, ApunteRegistro>() {
					@Override
					public ApunteRegistro convert(ApunteRegistroType source, Type<? extends ApunteRegistro> destinationType) {
						ApunteRegistro target = new ApunteRegistro();
						target.setTipoAsiento(TipoAsiento.valueOf(source.getTipoAsiento().name()));
						target.setNuRegistro(source.getNuRegistro());
						target.setEstado(EstadoRegistro.valueOf(source.getEstado().name()));
						//TODO: completar
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<RespuestaType, Respuesta>() {
					@Override
					public Respuesta convert(RespuestaType source, Type<? extends Respuesta> destinationType) {
						Respuesta target = new Respuesta();
						target.setCodigo(source.getCodigo());
						target.setMensaje(source.getMensaje());
						target.setTipoRespuesta(TipoRespuesta.valueOf(source.getTipo().name()));
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<TipoDocumento, TipoIdentificacionEnum>() {
					@Override
					public TipoIdentificacionEnum convert(TipoDocumento source, Type<? extends TipoIdentificacionEnum> destinationType) {
						switch (source) {
							case CIF:
								return TipoIdentificacionEnum.CIF;
							case NIE:
								return TipoIdentificacionEnum.DOCUMENTO_IDENTIFICACION_EXTRANJEROS;
							case NIF:
								return TipoIdentificacionEnum.NIF;
							case OTROS_ORIGEN_VALUE:
								return TipoIdentificacionEnum.CODIGO_DE_ORIGEN;
							case OTROS_PERSONA_FISICA:
								return TipoIdentificacionEnum.OTROS_PERSONA_FISICA;
							case PASAPORTE:
								return TipoIdentificacionEnum.PASAPORTE;
						}
						return null;
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
