package org.plugin.geiser.apb.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.plugin.geiser.api.GeiserPluginException;
import org.plugin.geiser.api.InteresadoG;
import org.plugin.geiser.api.PeticionBusquedaGeiser;
import org.plugin.geiser.api.PeticionBusquedaTramitGeiser;
import org.plugin.geiser.api.PeticionConsultaGeiser;
import org.plugin.geiser.api.PeticionRegistroGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.plugin.geiser.api.TipoDocumento;
import org.plugin.geiser.api.TipoRespuesta;
import org.plugin.geiser.api.ws.CanalNotificacionEnum;
import org.plugin.geiser.api.ws.EstadoAsientoEnum;
import org.plugin.geiser.api.ws.InteresadoType;
import org.plugin.geiser.api.ws.PeticionBusquedaEstadoTramitacionType;
import org.plugin.geiser.api.ws.PeticionBusquedaType;
import org.plugin.geiser.api.ws.PeticionConsultaType;
import org.plugin.geiser.api.ws.PeticionRegistroType;
import org.plugin.geiser.api.ws.ResultadoRegistroType;
import org.plugin.geiser.api.ws.TipoAsientoEnum;
import org.plugin.geiser.api.ws.TipoDocumentacionFisicaEnum;
import org.plugin.geiser.api.ws.TipoIdentificacionEnum;
import org.plugin.geiser.api.ws.TipoTransporteEnum;
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
						for (InteresadoG interesado: source.getInteresados()) {
							target.getInteresados().add(
									convertir(
											interesado, 
											InteresadoType.class));	
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
						//TODO: conversión
						target.setTimestampRegistradoDesde(source.getFechaRegistroInicio().toString());
						target.setTimestampRegistradoHasta(source.getFechaRegistroFinal().toString());
						target.setCdOficinaOrigen(source.getOficinaOrigen());
						if (source.getTipoDocumentoInteresadoRepre() != null)
							target.setTipoIdentificadorInteresadoRepresentante(TipoIdentificacionEnum.fromValue(source.getTipoDocumentoInteresadoRepre().name()));
						target.setIdentificadorInteresadoRepresentante(source.getDocumentoInteresadoRepre());
						target.setIncluirEnviadosSIR(source.isIncluirEnviosSir());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<ResultadoRegistroType, RespuestaRegistroGeiser>() {
					@Override
					public RespuestaRegistroGeiser convert(ResultadoRegistroType source, Type<? extends RespuestaRegistroGeiser> destinationType) {
						RespuestaRegistroGeiser target = new RespuestaRegistroGeiser();
						target.setCodigo(source.getRespuesta().getCodigo());
						target.setTipoRespuesta(TipoRespuesta.valueOf(source.getRespuesta().getTipo().name()));
						target.setNuRegistro(source.getApunte().getNuRegistro());
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
							Date fechaRegistro = sdf.parse(source.getApunte().getTimestampRegistrado());
							target.setFechaRegistro(fechaRegistro);
						} catch (ParseException e) {
							throw new GeiserPluginException("Ha habido un error interpretando la respuesta de GEISER");
						}
						target.setMensaje(source.getRespuesta().getMensaje());
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
