package org.plugin.geiser.helper;

import java.util.List;

import org.plugin.geiser.api.Anexo;
import org.plugin.geiser.api.Interesado;
import org.plugin.geiser.api.PeticionBusquedaGeiser;
import org.plugin.geiser.api.PeticionBusquedaTramitGeiser;
import org.plugin.geiser.api.PeticionConsultaGeiser;
import org.plugin.geiser.api.PeticionRegistroEnvioGeiser;
import org.plugin.geiser.api.PeticionRegistroGeiser;
import org.plugin.geiser.api.ws.AnexoType;
import org.plugin.geiser.api.ws.EstadoAsientoEnum;
import org.plugin.geiser.api.ws.InteresadoType;
import org.plugin.geiser.api.ws.PeticionBusquedaEstadoTramitacionType;
import org.plugin.geiser.api.ws.PeticionBusquedaType;
import org.plugin.geiser.api.ws.PeticionConsultaType;
import org.plugin.geiser.api.ws.PeticionRegistroEnvioSimpleType;
import org.plugin.geiser.api.ws.PeticionRegistroType;
import org.plugin.geiser.api.ws.TipoAsientoEnum;
import org.plugin.geiser.api.ws.TipoDocumentacionFisicaEnum;
import org.plugin.geiser.api.ws.TipoEnvio;
import org.plugin.geiser.api.ws.TipoIdentificacionEnum;
import org.plugin.geiser.api.ws.TipoTransporteEnum;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

@Component
public class ConversionHelper {

	private MapperFactory mapperFactory;
	
	public ConversionHelper () {
		mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<PeticionRegistroGeiser, PeticionRegistroType>() {
					@Override
					public PeticionRegistroType convert(PeticionRegistroGeiser source, Type<? extends PeticionRegistroType> destinationType) {
						PeticionRegistroType target = new PeticionRegistroType();
						target.setResumen(source.getResumen());
						target.setTipoAsiento(TipoAsientoEnum.valueOf(source.getTipoAsiento().name()));
						target.setEstado(EstadoAsientoEnum.FINALIZADO);
						target.setCdOrganoOrigen(source.getOrganoOrigen());
						target.setCdOrganoDestino(source.getOrganoDestino());
						target.setDocumentacionFisica(TipoDocumentacionFisicaEnum.valueOf(source.getDocumentacionFisica().name()));
						target.setReferenciaExterna(source.getRefExterna());
						target.setNuExpediente(source.getNuExpediente());
						target.setTipoTransporte(TipoTransporteEnum.valueOf(source.getTipoTransporte().name()));
						target.setNuTransporte(source.getNuTransporte());
						target.setObservaciones(source.getObservaciones());
						target.setCdAsunto(source.getCdAsunto()); //TODO: revisar
						for (Interesado interesado: source.getInteresados()) {
							target.getInteresados().add(
									convertir(
											interesado, 
											InteresadoType.class));	
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
						target.setTipoAsiento(TipoAsientoEnum.valueOf(source.getTipoAsiento().name()));
						target.setTipoEnvio(TipoEnvio.valueOf(source.getTipoEnvio().name()));
						target.setCdOrganoOrigen(source.getOrganoOrigen());
						target.setCdOrganoDestino(source.getOrganoDestino());
						target.setDocumentacionFisica(TipoDocumentacionFisicaEnum.valueOf(source.getDocumentacionFisica().name()));
						target.setReferenciaExterna(source.getRefExterna());
						target.setNuExpediente(source.getNuExpediente());
						target.setTipoTransporte(TipoTransporteEnum.valueOf(source.getTipoTransporte().name()));
						target.setNuTransporte(source.getNuTransporte());
						target.setObservaciones(source.getObservaciones());
						target.setCdAsunto(source.getCdAsunto()); //TODO: revisar
						for (Interesado interesado: source.getInteresados()) {
							target.getInteresados().add(
									convertir(
											interesado, 
											InteresadoType.class));	
						}
						for (Anexo anexo: source.getAnexos()) {
							target.getAnexos().add(
									convertir(
											anexo, 
											AnexoType.class));	
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
						target.setTipoIdentificadorInteresadoRepresentante(TipoIdentificacionEnum.valueOf(source.getTipoDocumentoInteresadoRepre().name()));
						target.setIdentificadorInteresadoRepresentante(source.getDocumentoInteresadoRepre());
						target.setIncluirEnviadosSIR(source.isIncluirEnviosSir());
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
