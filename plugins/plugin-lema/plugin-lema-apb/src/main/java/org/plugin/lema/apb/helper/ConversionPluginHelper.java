package org.plugin.lema.apb.helper;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.plugin.lema.api.AcuseRecibo;
import org.plugin.lema.api.Anexos;
import org.plugin.lema.api.ConsultaAcuseReciboRequest;
import org.plugin.lema.api.ConsultaAcuseReciboResponse;
import org.plugin.lema.api.ConsultaAnexoRequest;
import org.plugin.lema.api.ConsultaAnexoResponse;
import org.plugin.lema.api.ConsultaRealizadaRequest;
import org.plugin.lema.api.ConsultaRealizadaResponse;
import org.plugin.lema.api.Contenido;
import org.plugin.lema.api.ContenidoConsulta;
import org.plugin.lema.api.DetalleDocumento;
import org.plugin.lema.api.DocumentoAnexo;
import org.plugin.lema.api.EnlaceDocumento;
import org.plugin.lema.api.Envio;
import org.plugin.lema.api.EnvioRealizada;
import org.plugin.lema.api.Estado;
import org.plugin.lema.api.HashDocumento;
import org.plugin.lema.api.IdentificadorAcuseRecibo;
import org.plugin.lema.api.LocalizaRealizadaResponse;
import org.plugin.lema.api.LocalizaRequest;
import org.plugin.lema.api.LocalizaResponse;
import org.plugin.lema.api.Opcion;
import org.plugin.lema.api.Organismo;
import org.plugin.lema.api.Persona;
import org.plugin.lema.api.PeticionAccesoRequest;
import org.plugin.lema.api.PeticionAccesoResponse;
import org.plugin.lema.api.Receptor;
import org.plugin.lema.api.ReferenciaDocumento;
import org.plugin.lema.api.Sia;
import org.plugin.lema.api.realizadas.ws.ConsultaRealizadas;
import org.plugin.lema.api.realizadas.ws.ContenidoMtomInfo;
import org.plugin.lema.api.realizadas.ws.RespuestaConsultaRealizadas;
import org.plugin.lema.api.realizadas.ws.RespuestaLocalizaRealizadas;
import org.plugin.lema.api.ws.AcusePdf;
import org.plugin.lema.api.ws.ConsultaAcusePdf;
import org.plugin.lema.api.ws.ConsultaAnexos;
import org.plugin.lema.api.ws.IdentificadorAcusePdf;
import org.plugin.lema.api.ws.Localiza;
import org.plugin.lema.api.ws.PeticionAcceso;
import org.plugin.lema.api.ws.RespuestaConsultaAcusePdf;
import org.plugin.lema.api.ws.RespuestaConsultaAnexos;
import org.plugin.lema.api.ws.RespuestaPeticionAcceso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

@Component
public class ConversionPluginHelper {

	private MapperFactory mapperFactory;

	public ConversionPluginHelper() {
		mapperFactory = new DefaultMapperFactory.Builder().build();

		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<Localiza, LocalizaRequest>() {
					@Override
					public LocalizaRequest convert(Localiza source,
							Type<? extends LocalizaRequest> destinationType) {
						LocalizaRequest target = new LocalizaRequest();
						target.setNifTitular(source.getNifTitular());
						target.setNifDestinatario(source.getNifDestinatario());
						return target;
					}
				});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<LocalizaRequest, Localiza>() {
			@Override
			public Localiza convert(LocalizaRequest source,
					Type<? extends Localiza> destinationType) {
				Localiza target = new Localiza();
				target.setNifTitular(source.getNifTitular());
				target.setFechaDesde(convertir(source.getFechaDesde(), XMLGregorianCalendar.class));
				target.setFechaHasta(convertir(source.getFechaHasta(), XMLGregorianCalendar.class));
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<PeticionAccesoRequest, PeticionAcceso>() {
			@Override
			public PeticionAcceso convert(PeticionAccesoRequest source,
					Type<? extends PeticionAcceso> destinationType) {
				PeticionAcceso target = new PeticionAcceso();
				target.setEvento("1");
				target.setIdentificador(source.getIdentificador());
				target.setCodigoOrigen(BigInteger.valueOf((source.getCodigoOrigen())));
				target.setNifReceptor(source.getNifReceptor());
				target.setNombreReceptor(source.getNombreReceptor());
				target.setConcepto(source.getConcepto());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<RespuestaPeticionAcceso, PeticionAccesoResponse>() {
			@Override
			public PeticionAccesoResponse convert(RespuestaPeticionAcceso source,
					Type<? extends PeticionAccesoResponse> destinationType) {
				PeticionAccesoResponse target = new PeticionAccesoResponse();
				target.setCodigoRespuesta(source.getCodigoRespuesta());
				target.setDescripcionRespuesta(source.getDescripcionRespuesta());
				target.setDocumento(convertir(source.getDocumento(), DetalleDocumento.class));
				target.setFechaEvento(source.getFechaEvento());
				target.setAnexos(convertir(source.getAnexos(), Anexos.class));
				if (source.getOpcionesRespuestaPeticionAcceso() != null)
					target.setOpcionesRespuestaPeticionAcceso(convertirList(source.getOpcionesRespuestaPeticionAcceso().getOpcion(), Opcion.class));
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.realizadas.ws.DetalleDocumento, DetalleDocumento>() {
			@Override
			public DetalleDocumento convert(org.plugin.lema.api.realizadas.ws.DetalleDocumento source,
					Type<? extends DetalleDocumento> destinationType) {
				DetalleDocumento target = new DetalleDocumento();
				target.setNombre(source.getNombre());
				target.setMimeType(source.getMimeType());
				target.setMetadatos(source.getMetadatos());
				target.setHashDocumento(convertir(source.getHashDocumento(), HashDocumento.class));
				target.setCsvResguardo(source.getCsvResguardo());
				target.setContenido(convertir(source.getContenido(), Contenido.class));
				target.setEnlaceDocumento(source.getEnlaceDocumento());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.ws.DetalleDocumento, DetalleDocumento>() {
			@Override
			public DetalleDocumento convert(org.plugin.lema.api.ws.DetalleDocumento source,
					Type<? extends DetalleDocumento> destinationType) {
				DetalleDocumento target = new DetalleDocumento();
				target.setNombre(source.getNombre());
				target.setMimeType(source.getMimeType());
				target.setMetadatos(source.getMetadatos());
				target.setHashDocumento(convertir(source.getHashDocumento(), HashDocumento.class));
				target.setCsvResguardo(source.getCsvResguardo());
				target.setContenido(convertir(source.getContenido(), Contenido.class));
				target.setReferenciaDocumento(source.getReferenciaDocumento());
				target.setReferenciaPdfAcuse(source.getReferenciaPdfAcuse());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.ws.Anexos, Anexos>() {
			@Override
			public Anexos convert(org.plugin.lema.api.ws.Anexos source,
					Type<? extends Anexos> destinationType) {
				Anexos target = new Anexos();
				if (source.getAnexosUrl() != null)
					target.setEnlaceDocumentos(convertirList(source.getAnexosUrl().getAnexoUrl(), EnlaceDocumento.class));
				if (source.getAnexosReferencia() != null)
					target.setReferenciaDocumentos(convertirList(source.getAnexosReferencia().getAnexoReferencia(), ReferenciaDocumento.class));
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.ws.AnexoUrl, EnlaceDocumento>() {
			@Override
			public EnlaceDocumento convert(org.plugin.lema.api.ws.AnexoUrl source,
					Type<? extends EnlaceDocumento> destinationType) {
				EnlaceDocumento target = new EnlaceDocumento();
				target.setEnlaceDocumento(source.getEnlaceDocumento());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.ws.AnexoReferencia, ReferenciaDocumento>() {
			@Override
			public ReferenciaDocumento convert(org.plugin.lema.api.ws.AnexoReferencia source,
					Type<? extends ReferenciaDocumento> destinationType) {
				ReferenciaDocumento target = new ReferenciaDocumento();
				target.setReferenciaDocumento(source.getReferenciaDocumento());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.realizadas.ws.HashDocumento, HashDocumento>() {
			@Override
			public HashDocumento convert(org.plugin.lema.api.realizadas.ws.HashDocumento source,
					Type<? extends HashDocumento> destinationType) {
				HashDocumento target = new HashDocumento();
				target.setHash(source.getHash());
				target.setAlgoritmoHash(source.getAlgoritmoHash());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<ConsultaAcuseReciboRequest, ConsultaAcusePdf>() {
			@Override
			public ConsultaAcusePdf convert(ConsultaAcuseReciboRequest source,
					Type<? extends ConsultaAcusePdf> destinationType) {
				ConsultaAcusePdf target = new ConsultaAcusePdf();
				target.setIdentificador(source.getIdentificador());
				target.setCodigoOrigen(BigInteger.valueOf((source.getCodigoOrigen())));
				target.setNifReceptor(source.getNifReceptor());
				target.setIdentificadorAcusePdf(convertir(source.getIdentificadorAcuse(), IdentificadorAcusePdf.class));
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<IdentificadorAcuseRecibo, IdentificadorAcusePdf>() {
			@Override
			public IdentificadorAcusePdf convert(IdentificadorAcuseRecibo source,
					Type<? extends IdentificadorAcusePdf> destinationType) {
				IdentificadorAcusePdf target = new IdentificadorAcusePdf();
				target.setReferencia(source.getReferencia());
				target.setCsvResguardo(source.getCsvResguardo());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<RespuestaConsultaAcusePdf, ConsultaAcuseReciboResponse>() {
			@Override
			public ConsultaAcuseReciboResponse convert(RespuestaConsultaAcusePdf source,
					Type<? extends ConsultaAcuseReciboResponse> destinationType) {
				ConsultaAcuseReciboResponse target = new ConsultaAcuseReciboResponse();
				target.setCodigoRespuesta(source.getCodigoRespuesta());
				target.setDescripcionRespuesta(source.getDescripcionRespuesta());
				target.setAcuseRecibo(convertir(source.getAcusePdf(), AcuseRecibo.class));
				if (source.getOpcionesRespuestaConsultaAcusePdf() != null)
					target.setOpcionesRespuestaConsultaAcuseRecibo(convertirList(source.getOpcionesRespuestaConsultaAcusePdf().getOpcion(), Opcion.class));
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<ConsultaAnexoRequest, ConsultaAnexos>() {
			@Override
			public ConsultaAnexos convert(ConsultaAnexoRequest source,
					Type<? extends ConsultaAnexos> destinationType) {
				ConsultaAnexos target = new ConsultaAnexos();
				target.setIdentificador(source.getIdentificador());
				target.setCodigoOrigen(BigInteger.valueOf((source.getCodigoOrigen())));
				target.setNifReceptor(source.getNifReceptor());
				target.setReferencia(source.getReferenciaAnexo());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<RespuestaConsultaAnexos, ConsultaAnexoResponse>() {
			@Override
			public ConsultaAnexoResponse convert(RespuestaConsultaAnexos source,
					Type<? extends ConsultaAnexoResponse> destinationType) {
				ConsultaAnexoResponse target = new ConsultaAnexoResponse();
				target.setCodigoRespuesta(source.getCodigoRespuesta());
				target.setDescripcionRespuesta(source.getDescripcionRespuesta());
				target.setDocumentoAnexo(convertir(source.getDocumento(), DocumentoAnexo.class));
				if (source.getOpcionesRespuestaConsultaAnexo() != null)
					target.setOpcionesRespuestaConsultaAcuseRecibo(convertirList(source.getOpcionesRespuestaConsultaAnexo().getOpcion(), Opcion.class));
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.ws.DocumentoAnexo, DocumentoAnexo>() {
			@Override
			public DocumentoAnexo convert(org.plugin.lema.api.ws.DocumentoAnexo source,
					Type<? extends DocumentoAnexo> destinationType) {
				DocumentoAnexo target = new DocumentoAnexo();
				target.setNombre(source.getNombre());
				target.setMimeType(source.getMimeType());
				if (source.getContenido() != null)
					target.setContenido(convertir(source.getContenido(), Contenido.class));
				target.setMetadatos(source.getMetadatos());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<AcusePdf, AcuseRecibo>() {
			@Override
			public AcuseRecibo convert(AcusePdf source,
					Type<? extends AcuseRecibo> destinationType) {
				AcuseRecibo target = new AcuseRecibo();
				if (source.getContenido() != null)
					target.setContenido(convertir(source.getContenido(), Contenido.class));
				target.setMetadatos(source.getMetadatos());
				target.setMimeType(source.getMimeType());
				target.setNombreAcuse(source.getNombreAcuse());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.ws.Contenido, Contenido>() {
			@Override
			public Contenido convert(org.plugin.lema.api.ws.Contenido source,
					Type<? extends Contenido> destinationType) {
				Contenido target = new Contenido();
				target.setHref(source.getHref());
				target.setValue(source.getValue());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.ws.HashDocumento, HashDocumento>() {
			@Override
			public HashDocumento convert(org.plugin.lema.api.ws.HashDocumento source,
					Type<? extends HashDocumento> destinationType) {
				HashDocumento target = new HashDocumento();
				target.setHash(source.getHash());
				target.setAlgoritmoHash(source.getAlgoritmoHash());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<ContenidoMtomInfo, Contenido>() {
			@Override
			public Contenido convert(ContenidoMtomInfo source,
					Type<? extends Contenido> destinationType) {
				Contenido target = new Contenido();
				target.setContenido(convertir(source.getContenido(), ContenidoConsulta.class));
				target.setTipoMIME(source.getTipoMIME());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.realizadas.ws.Contenido, ContenidoConsulta>() {
			@Override
			public ContenidoConsulta convert(org.plugin.lema.api.realizadas.ws.Contenido source,
					Type<? extends ContenidoConsulta> destinationType) {
				ContenidoConsulta target = new ContenidoConsulta();
				target.setContent(source.getContent());
				target.setContentType(source.getContentType());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.ws.Contenido2, Contenido>() {
			@Override
			public Contenido convert(org.plugin.lema.api.ws.Contenido2 source,
					Type<? extends Contenido> destinationType) {
				Contenido target = new Contenido();
				target.setHref(source.getHref());
				target.setValue(source.getValue());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<org.plugin.lema.api.ws.RespuestaLocaliza, LocalizaResponse>() {
					@Override
					public LocalizaResponse convert(org.plugin.lema.api.ws.RespuestaLocaliza source,
							Type<? extends LocalizaResponse> destinationType) {
						LocalizaResponse target = new LocalizaResponse();
						target.setCodigoRespuesta(source.getCodigoRespuesta());
						target.setDescripcionRespuesta(source.getDescripcionRespuesta());
						if (source.getEnvios() != null)
							target.setEnvios(convertirList(source.getEnvios().getItem(), Envio.class));
						return target;
					}
				});
		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<ConsultaRealizadaRequest, ConsultaRealizadas>() {
					@Override
					public ConsultaRealizadas convert(ConsultaRealizadaRequest source,
							Type<? extends ConsultaRealizadas> destinationType) {
						ConsultaRealizadas target = new ConsultaRealizadas();
						target.setIdentificador(source.getIdentificador());
						target.setCodigoOrigen(BigInteger.valueOf((source.getCodigoOrigen())));
						target.setConcepto(source.getConcepto());
						target.setNifPeticion(source.getNifPeticion());
						target.setNombrePeticion(source.getNombrePeticion());
						return target;
					}
		});
		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<RespuestaConsultaRealizadas, ConsultaRealizadaResponse>() {
					@Override
					public ConsultaRealizadaResponse convert(RespuestaConsultaRealizadas source,
							Type<? extends ConsultaRealizadaResponse> destinationType) {
						ConsultaRealizadaResponse target = new ConsultaRealizadaResponse();
						target.setCodigoRespuesta(source.getCodigoRespuesta());
						target.setDescripcionRespuesta(source.getDescripcionRespuesta());
						target.setCodigoOrigen(source.getCodigoOrigen());
						target.setCodigoProcedimiento(convertir(source.getCodigoProcedimiento(), Sia.class));
						target.setDocumento(convertir(source.getDocumento(), DetalleDocumento.class));
						target.setFechaUltimoEstado(source.getFechaUltimoEstado());
						target.setIdentificador(source.getIdentificador());
						if (source.getOpcionesRespuestaConsultaRealizadas() != null)
							target.setOpcionesRespuestaConsultaRealizadas(convertirList(source.getOpcionesRespuestaConsultaRealizadas().getOpcion(), Opcion.class));
						target.setPostal(source.isPostal());
						return target;
					}
		});
		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<RespuestaLocalizaRealizadas, LocalizaRealizadaResponse>() {
					@Override
					public LocalizaRealizadaResponse convert(RespuestaLocalizaRealizadas source,
							Type<? extends LocalizaRealizadaResponse> destinationType) {
						LocalizaRealizadaResponse target = new LocalizaRealizadaResponse();
						target.setCodigoRespuesta(source.getCodigoRespuesta());
						target.setDescripcionRespuesta(source.getDescripcionRespuesta());
						if (source.getEnvios() != null)
							target.setEnvios(convertirList(source.getEnvios().getItem(), EnvioRealizada.class));
						return target;
					}
		});
		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<org.plugin.lema.api.ws.Envio, Envio>() {
					@Override
					public Envio convert(org.plugin.lema.api.ws.Envio source, Type<? extends Envio> destinationType) {
						Envio target = new Envio();
						target.setIdentificador(source.getIdentificador());
						target.setCodigoOrigen(source.getCodigoOrigen());
						target.setConcepto(source.getConcepto());
						target.setDescripcion(source.getDescripcion());
						target.setTipoEnvio(source.getTipoEnvio());
						target.setOrganismoEmisor(convertir(source.getOrganismoEmisor(), Organismo.class));
						target.setTitular(convertir(source.getTitular(), Persona.class));
						target.setFechaPuestaDisposicion(source.getFechaPuestaDisposicion());
						if (source.getOpcionesEnvio() != null)
							target.setOpcionesEnvio(convertirList(source.getOpcionesEnvio().getOpcion(), Opcion.class));
						return target;
					}
				});
		mapperFactory.getConverterFactory()
		.registerConverter(new CustomConverter<org.plugin.lema.api.realizadas.ws.Envio, EnvioRealizada>() {
			@Override
			public EnvioRealizada convert(org.plugin.lema.api.realizadas.ws.Envio source, Type<? extends EnvioRealizada> destinationType) {
				EnvioRealizada target = new EnvioRealizada();
				target.setIdentificador(source.getIdentificador());
				target.setCodigoOrigen(new BigInteger(source.getCodigoOrigen()));
				target.setConcepto(source.getConcepto());
				target.setDescripcion(source.getDescripcion());
				target.setTipoEnvio(source.getTipoEnvio());
				target.setOrganismoEmisor(convertir(source.getOrganismoEmisor(), Organismo.class));
				target.setTitular(convertir(source.getTitular(), Persona.class));
				target.setFechaPuestaDisposicion(source.getFechaPuestaDisposicion());
				if (source.getOpcionesEnvio() != null)
					target.setOpcionesEnvio(convertirList(source.getOpcionesEnvio().getOpcion(), Opcion.class));
				target.setCodigoProcedimiento(convertir(source.getCodigoProcedimiento(), Sia.class));
				target.setEstado(Estado.valueOf(source.getEstado().name()));
				target.setReceptor(convertir(source.getReceptor(), Receptor.class));
				target.setReferenciaPdfAcuse(source.getReferenciaPdfAcuse());
				target.setCsvResguardo(source.getCsvResguardo());
				return target;
			}
		});
		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<org.plugin.lema.api.realizadas.ws.Sia, Sia>() {
					@Override
					public Sia convert(org.plugin.lema.api.realizadas.ws.Sia source,
							Type<? extends Sia> destinationType) {
						Sia target = new Sia();
						target.setCodigo(source.getCodigo());
						target.setDescripcion(source.getDescripcion());
						return target;
					}
		});
		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<org.plugin.lema.api.realizadas.ws.Receptor, Receptor>() {
					@Override
					public Receptor convert(org.plugin.lema.api.realizadas.ws.Receptor source,
							Type<? extends Receptor> destinationType) {
						Receptor target = new Receptor();
						target.setNifReceptor(source.getNifReceptor());
						target.setNombreReceptor(source.getNombreReceptor());
						target.setNifRepresentante(source.getNifRepresentante());
						target.setNombreRepresentante(source.getNombreRepresentante());
						return target;
					}
		});
		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<org.plugin.lema.api.ws.Persona, Persona>() {
					@Override
					public Persona convert(org.plugin.lema.api.ws.Persona source,
							Type<? extends Persona> destinationType) {
						Persona target = new Persona();
						target.setNifTitular(source.getNifTitular());
						target.setNombreTitular(source.getNombreTitular());
						target.setCodigoDIR3(source.getCodigoDIR3());
						target.setDescripcionEntidad(source.getDescripcionEntidad());
						return target;
					}
				});

		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<org.plugin.lema.api.ws.Organismo, Organismo>() {
					@Override
					public Organismo convert(org.plugin.lema.api.ws.Organismo source,
							Type<? extends Organismo> destinationType) {
						Organismo target = new Organismo();
						target.setCodigoOrganismo(source.getCodigoOrganismo());
						target.setNombreOrganismo(source.getNombreOrganismo());
						return target;
					}
				});

		mapperFactory.getConverterFactory()
				.registerConverter(new CustomConverter<org.plugin.lema.api.ws.Opcion8, Opcion>() {
					@Override
					public Opcion convert(org.plugin.lema.api.ws.Opcion8 source,
							Type<? extends Opcion> destinationType) {
						Opcion target = new Opcion();

						return target;
					}
				});

		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<Date, XMLGregorianCalendar>() {
			@Override
			public XMLGregorianCalendar convert(Date source, Type<? extends XMLGregorianCalendar> destinationType) {
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				gregorianCalendar.setTime(source);

				XMLGregorianCalendar xmlGregorianCalendar = null;
				try {
					DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

					xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
					xmlGregorianCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
					xmlGregorianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
				} catch (DatatypeConfigurationException e) {
					e.printStackTrace();
				}
				return xmlGregorianCalendar;
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

	private static final Logger logger = LoggerFactory.getLogger(ConversionPluginHelper.class);
}
