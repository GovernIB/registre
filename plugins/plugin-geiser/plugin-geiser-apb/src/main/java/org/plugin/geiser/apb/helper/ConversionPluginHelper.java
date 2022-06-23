package org.plugin.geiser.apb.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.plugin.geiser.api.AnexoG;
import org.plugin.geiser.api.ApunteRegistro;
import org.plugin.geiser.api.CanalNotificacion;
import org.plugin.geiser.api.DocumentHelper;
import org.plugin.geiser.api.DocumentacionFisica;
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
import org.plugin.geiser.api.RespuestaBusquedaGeiser;
import org.plugin.geiser.api.RespuestaBusquedaTramitGeiser;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.plugin.geiser.api.TipoAsiento;
import org.plugin.geiser.api.TipoDocumento;
import org.plugin.geiser.api.TipoDocumentoAnexo;
import org.plugin.geiser.api.TipoFirma;
import org.plugin.geiser.api.TipoRespuesta;
import org.plugin.geiser.api.TipoTransporte;
import org.plugin.geiser.api.ValidezDocumento;

import org.springframework.stereotype.Component;

import es.gob.minhap.geiser.rgeco.ws.client.registro.types.*;
import es.gob.minhap.geiser.rgeco.ws.client.types.RespuestaType;

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
						target.setResumen(source.getResumen());
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
//						if (source.getTipoAsiento() != null && source.getTipoAsiento().equals(TipoAsiento.SALIDA))
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
						if (source.getPrimerApellido() != null && !source.getPrimerApellido().isEmpty()) { // Notib envia los dos valores siempre...
							target.setNombreInteresado(source.getNombre());
							target.setPrimerApellidoInteresado(source.getPrimerApellido());
							target.setSegundoApellidoInteresado(source.getSegundoApellido());
						}
						if (source.getTipoDocumento() != null)
							target.setTipoIdentificadorInteresado(convertir(source.getTipoDocumento(), TipoIdentificacionEnum.class));
						target.setIdentificadorInteresado(source.getDocumento());
						target.setMailInteresado(source.getEmail());
						target.setTelefonoInteresado(source.getTelefono());
//						if (source.getCanalNotificacion() != null)
//							target.setCanalNotificacionInteresado(CanalNotificacionEnum.fromValue(source.getCanalNotificacion().name()));
//						target.setDireccionElectronicaInteresado(source.getDireccionElectronica());
						//Format codigo País [3 dígitos]
						String pais = source.getPais();
						if (pais != null) {
							while (pais.length() != 3) { // Completar hasta llegar a 3 dígitos
								pais = "0" + pais;
							}
							target.setCdPaisInteresado(pais);
						}
						//Format codigo Província [2 dígitos]
						String provincia = source.getProvincia();
						if (provincia != null) {
							provincia = provincia.length() == 1 ? "0" + provincia : provincia;
							target.setCdProvinciaInteresado(provincia.length() == 1 ? "0" + provincia : provincia);
						}
						
						//Format codigo Municipio  [5 dígitos -> provincia + municipio sin DC]
						String municipio = source.getMunicipio();
						if (municipio != null) {
							municipio = municipio.substring(0, municipio.length() - 1);  // Eliminar dígito control
							while (municipio.length() != 3) { // Completar hasta llegar a 3 dígitos
								municipio = "0" + municipio;
							}
							municipio = provincia + municipio;
							target.setCdMunicipioInteresado(municipio);
						}
						target.setDireccionInteresado(source.getDireccion());
						target.setCodigoPostalInteresado(source.getCp());
						TipoDocumento tipoDocumento = source.getTipoDocumento();
						if (tipoDocumento.equals(TipoDocumento.OTROS_ORIGEN_VALUE) || tipoDocumento.equals(TipoDocumento.CIF) || tipoDocumento.equals(TipoDocumento.NIF))
							target.setRazonSocialInteresado(source.getRazonSocial());
						target.setObservaciones(source.getObservaciones());
						InteresadoG sourceRepresentante = source.getRepresentante();
						if (sourceRepresentante != null) {
//							InteresadoType targetRepresentante = new InteresadoType();
							if (sourceRepresentante.getPrimerApellido() != null && !sourceRepresentante.getPrimerApellido().isEmpty()) {
								target.setNombreRepresentante(sourceRepresentante.getNombre());
								target.setPrimerApellidoRepresentante(sourceRepresentante.getPrimerApellido());
								target.setSegundoApellidoRepresentante(sourceRepresentante.getSegundoApellido());
							}
							if (sourceRepresentante.getTipoDocumento() != null)
								target.setTipoIdentificadorRepresentante(convertir(sourceRepresentante.getTipoDocumento(), TipoIdentificacionEnum.class));
							target.setIdentificadorRepresentante(sourceRepresentante.getDocumento());
							target.setMailRepresentante(sourceRepresentante.getEmail());
							target.setTelefonoRepresentante(sourceRepresentante.getTelefono());
//							if (sourceRepresentante.getCanalNotificacion() != null)
//								target.setCanalNotificacionRepresentante(CanalNotificacionEnum.fromValue(sourceRepresentante.getCanalNotificacion().name()));
//							target.setDireccionElectronicaRepresentante(sourceRepresentante.getDireccionElectronica());
						
							//Format codigo País
							String paisRepres = sourceRepresentante.getPais();
							if (paisRepres != null) {
								while (paisRepres.length() != 3) {
									paisRepres = "0" + paisRepres;
								}
								target.setCdPaisRepresentante(paisRepres);
							}
							//Format codigo Província
							String provinciaRepres = sourceRepresentante.getProvincia();
							if (provinciaRepres != null) {
								provinciaRepres = provinciaRepres.length() == 1 ? "0" + provinciaRepres : provinciaRepres;
								target.setCdProvinciaRepresentante(provinciaRepres);
							}
							//Format codigo Municipio
							String municipioRepres = sourceRepresentante.getMunicipio();
							if (municipioRepres != null) {
								municipioRepres = municipioRepres.substring(0, municipioRepres.length() - 1);  // Eliminar dígito control
								while (municipioRepres.length() != 3) {
									municipioRepres = "0" + municipioRepres;
								}
								municipioRepres = provincia + municipioRepres;
								target.setCdMunicipioRepresentante(municipioRepres);
							}
							
							target.setDireccionRepresentante(sourceRepresentante.getDireccion());
							target.setCodigoPostalRepresentante(sourceRepresentante.getCp());
							tipoDocumento = sourceRepresentante.getTipoDocumento();
							if (tipoDocumento.equals(TipoDocumento.OTROS_ORIGEN_VALUE) || tipoDocumento.equals(TipoDocumento.CIF) || tipoDocumento.equals(TipoDocumento.NIF))
								target.setRazonSocialRepresentante(sourceRepresentante.getRazonSocial());
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<InteresadoType, InteresadoG>() {
					@Override
					public InteresadoG convert(InteresadoType source, Type<? extends InteresadoG> destinationType) {
						InteresadoG target = new InteresadoG();
						target.setNombre(source.getNombreInteresado());
						target.setPrimerApellido(source.getPrimerApellidoInteresado());
						target.setSegundoApellido(source.getSegundoApellidoInteresado());
						if (source.getTipoIdentificadorInteresado() != null)
							target.setTipoDocumento(convertir(source.getTipoIdentificadorInteresado(), TipoDocumento.class));
						target.setEmail(source.getMailInteresado());
						target.setTelefono(source.getTelefonoInteresado());
						if (source.getCanalNotificacionInteresado() != null)
							target.setCanalNotificacion(CanalNotificacion.valueOf(source.getCanalNotificacionInteresado().name()));
						target.setPais(source.getCdPaisInteresado());
						target.setProvincia(source.getCdProvinciaInteresado());
						target.setMunicipio(source.getCdMunicipioInteresado());
						target.setDireccion(source.getDireccionInteresado());
						target.setCp(source.getCodigoPostalInteresado());
						target.setRazonSocial(source.getRazonSocialInteresado());
						target.setDireccionElectronica(source.getDireccionElectronicaInteresado());
						target.setObservaciones(source.getObservaciones());
						target.setDocumento(source.getIdentificadorInteresado());
						if (source.getTipoIdentificadorRepresentante() != null) {
							InteresadoG representante = new InteresadoG();
							representante.setNombre(source.getNombreRepresentante());
							representante.setPrimerApellido(source.getPrimerApellidoRepresentante());
							representante.setSegundoApellido(source.getSegundoApellidoRepresentante());
							if (source.getTipoIdentificadorRepresentante() != null)
								representante.setTipoDocumento(convertir(source.getTipoIdentificadorRepresentante(), TipoDocumento.class));
							representante.setEmail(source.getMailRepresentante());
							representante.setTelefono(source.getTelefonoRepresentante());
							if (source.getCanalNotificacionRepresentante() != null)
								representante.setCanalNotificacion(CanalNotificacion.valueOf(source.getCanalNotificacionRepresentante().name()));
							representante.setPais(source.getCdPaisRepresentante());
							representante.setProvincia(source.getCdProvinciaRepresentante());
							representante.setMunicipio(source.getCdMunicipioRepresentante());
							representante.setDireccion(source.getDireccionRepresentante());
							representante.setCp(source.getCodigoPostalRepresentante());
							representante.setRazonSocial(source.getRazonSocialRepresentante());
							representante.setDireccionElectronica(source.getDireccionElectronicaRepresentante());
							representante.setDocumento(source.getIdentificadorRepresentante());
							target.setRepresentante(representante);
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<AnexoG, AnexoType>() {
					@Override
					public AnexoType convert(AnexoG source, Type<? extends AnexoType> destinationType) {
						AnexoType target = new AnexoType();
						String titulo = source.getTitulo();
						target.setNombre(titulo);
						if (source.getValidezDocumento() != null)
							target.setValidez(ValidezDocumentoEnum.valueOf(source.getValidezDocumento().name()));
						if (source.getTipoDocumentoAnexo() != null)
							target.setTipoDocumento(TipoDocAnexoEnum.valueOf(source.getTipoDocumentoAnexo().name()));
						target.setAnexo(source.getAnexoBase64());
						target.setHash(DocumentHelper.decodeBase64Hash(source.getHash()));
//						if (source.getHash() != null)
//							target.setHash(DocumentHelper.getHash512Document(Base64.decodeBase64(source.getAnexoBase64())));
						String tipoMime = source.getTipoMime();
						target.setTipoMime(tipoMime);
						
						if (tipoMime.equals("application/octet-stream") && titulo.endsWith(".pdf"))
							target.setTipoMime("application/pdf");
						target.setObservaciones(source.getObservaciones());
						target.setTamanioFichero(source.getTamanioFichero());
						target.setTipoFirma(TipoFirmaEnum.fromValue(source.getTipoFirma().name()));
						if (source.getFirmaBase64() != null) {
							if (source.getTipoFirma() != null)
								target.setTipoFirma(TipoFirmaEnum.valueOf(source.getTipoFirma().name()));
							target.setFirma(source.getFirmaBase64());
							target.setHashFirma(DocumentHelper.decodeBase64Hash(source.getHashFirma()));
//							if (source.getHashFirma() != null)
//								target.setHashFirma(DocumentHelper.getHash512Document(Base64.decodeBase64(source.getFirmaBase64())));
							target.setNombreFirma(source.getNombreFirma());
							target.setTamanioFicheroFirma(source.getTamanioFicheroFirma());
							target.setTipoMimeFirma(source.getTipoMimeFirma());
						}
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<AnexoType, AnexoG>() {
					@Override
					public AnexoG convert(AnexoType source, Type<? extends AnexoG> destinationType) {
						AnexoG target = new AnexoG();
						target.setTitulo(source.getNombre());
						if (source.getValidez() != null)
							target.setValidezDocumento(ValidezDocumento.valueOf(source.getValidez().name()));
						if (source.getTipoDocumento() != null)
							target.setTipoDocumentoAnexo(TipoDocumentoAnexo.valueOf(source.getTipoDocumento().name()));
						target.setAnexoBase64(source.getAnexo());
//						target.setHash(DocumentHelper.getHash512Document(Base64.decodeBase64(source.getAnexoBase64())));
						if (source.getHash() != null)
							target.setHashBase64(source.getHash());
						target.setTipoMime(source.getTipoMime());
						target.setObservaciones(source.getObservaciones());
						target.setTamanioFichero(source.getTamanioFichero());
						if (source.getTipoFirma() != null)
							target.setTipoFirma(TipoFirma.valueOf(source.getTipoFirma().name()));
						if (source.getFirma() != null) {
							target.setFirmaBase64(source.getFirma());
							if (source.getHashFirma() != null)
								target.setHashFirmaBase64(source.getHashFirma());
							target.setNombreFirma(source.getNombreFirma());
							target.setTamanioFicheroFirma(source.getTamanioFicheroFirma());
							target.setTipoMimeFirma(source.getTipoMimeFirma());
						}
						target.setIdentificador(source.getIdentificador());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<PeticionConsultaGeiser, PeticionConsultaType>() {
					@Override
					public PeticionConsultaType convert(PeticionConsultaGeiser source, Type<? extends PeticionConsultaType> destinationType) {
						PeticionConsultaType target = new PeticionConsultaType();
						target.setNuRegistro(source.getNuRegistro());
						target.setIncluirContenidoAnexo(source.isIncluirContenidoAnexo());
						target.setIncluirContenidoAnexoCSV(source.isIncluirContenidoCSV());
						target.setIncluirJustificante(source.isIncluirJustificante());
						return target;
					}
				});
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<PeticionBusquedaGeiser, PeticionBusquedaType>() {
					@Override
					public PeticionBusquedaType convert(PeticionBusquedaGeiser source, Type<? extends PeticionBusquedaType> destinationType) {
						PeticionBusquedaType target = new PeticionBusquedaType();
						target.setTimestampRegistradoDesde(source.getFechaRegistroInicio());
						target.setTimestampRegistradoHasta(source.getFechaRegistroFinal());
						target.setCdOrganoDestino(source.getCdOrganoDestino());
						if (source.getTipoAsiento() != null)
							target.setTipoAsiento(TipoAsientoEnum.fromValue(source.getTipoAsiento().name()));
						if (source.getEstado() != null)
							target.setEstado(EstadoAsientoEnum.valueOf(source.getEstado().name()));
						
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
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
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
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
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
						target.setNuRegistroOrigen(source.getNuRegistroOrigen());
						target.setEstado(EstadoRegistro.valueOf(source.getEstado().name()));
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
							Date fechaRegistro = sdf.parse(source.getTimestampRegistrado());
							target.setFechaRegistro(fechaRegistro);
						} catch (ParseException e) {
							throw new GeiserPluginException("Ha habido un error interpretando la respuesta de GEISER");
						}
						target.setTimeStampRegistro(source.getTimestampRegistrado());
						target.setResumen(source.getResumen());
						if (source.getDocumentacionFisica() != null)
							target.setDocumentacionFisica(DocumentacionFisica.valueOf(source.getDocumentacionFisica().name()));
						target.setCdAmbitoOrigen(source.getCdAmbitoCreacion());
						target.setNombreAmbitoOrigen(source.getAmbitoCreacion());
						target.setOrganoOrigen(source.getCdOrganoOrigen());
						target.setOrganoOrigenDenominacion(source.getOrganoOrigen());
						target.setCdAmbitoActual(source.getCdAmbitoActual());
						target.setNombreAmbitoActual(source.getAmbitoActual());
						target.setCdAmbitoDestino(source.getCdAmbitoActual());
						target.setNombreAmbitoDestino(source.getAmbitoActual());
						target.setOrganoDestino(source.getCdOrganoDestino());
						target.setOrganoDestinoDenominacion(source.getOrganoDestino());
						
						target.setCdAsunto(source.getCdAsunto());
						target.setReferenciaExterna(source.getReferenciaExterna());
						target.setNuExpediente(source.getNuExpediente());
						target.setNuTransporte(source.getNuTransporte());
						target.setNombreUsuario(source.getNombreUsuario());
						target.setContactoUsuario(source.getContactoUsuario());
						
						if (source.getTipoTransporte() != null)
							target.setTipoTransporte(TipoTransporte.valueOf(source.getTipoTransporte().name()));
						target.setExpone(source.getExpone());
						target.setSolicita(source.getSolicita());
						
						target.setObservaciones(source.getObservaciones());
						if (source.getAnexos() != null && !source.getAnexos().isEmpty())
							target.setAnexos(convertirList(source.getAnexos(), AnexoG.class));
						if (source.getInteresados() != null && !source.getInteresados().isEmpty())
							target.setInteresados(convertirList(source.getInteresados(), InteresadoG.class));
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
				new CustomConverter<ResultadoBusquedaType, RespuestaBusquedaGeiser>() {
					@Override
					public RespuestaBusquedaGeiser convert(ResultadoBusquedaType source, Type<? extends RespuestaBusquedaGeiser> destinationType) {
						RespuestaBusquedaGeiser target = new RespuestaBusquedaGeiser();
						target.setRespuesta(convertir(source.getRespuesta(), Respuesta.class));
						target.setApuntes(convertirList(source.getApuntes(), ApunteRegistro.class));
						target.setNuTotalApuntes(source.getNuTotalApuntes());
						target.setUidIterator(source.getUidIterator());
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
		mapperFactory.getConverterFactory().registerConverter(
				new CustomConverter<TipoIdentificacionEnum, TipoDocumento>() {
					@Override
					public TipoDocumento convert(TipoIdentificacionEnum source, Type<? extends TipoDocumento> destinationType) {
						switch (source) {
							case CIF:
								return TipoDocumento.CIF;
							case DOCUMENTO_IDENTIFICACION_EXTRANJEROS:
								return TipoDocumento.NIE;
							case NIF:
								return TipoDocumento.NIF;
							case CODIGO_DE_ORIGEN:
								return TipoDocumento.OTROS_ORIGEN_VALUE;
							case OTROS_PERSONA_FISICA:
								return TipoDocumento.OTROS_PERSONA_FISICA;
							case PASAPORTE:
								return TipoDocumento.PASAPORTE;
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
