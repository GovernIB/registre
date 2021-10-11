package es.caib.regweb3.persistence.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.plugin.geiser.api.CanalNotificacion;
import org.plugin.geiser.api.DocumentacionFisica;
import org.plugin.geiser.api.InteresadoG;
import org.plugin.geiser.api.PeticionRegistroGeiser;
import org.plugin.geiser.api.TipoAsiento;
import org.plugin.geiser.api.TipoDocumento;
import org.plugin.geiser.api.TipoTransporte;
import org.springframework.stereotype.Component;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
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
						target.setOrganoDestino(StringUtils.stripToNull(source.getDestino().getCodigo()));
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
