package org.plugin.geiser.mock;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.plugin.geiser.api.AnexoGSample;
import org.plugin.geiser.api.GeiserPluginException;
import org.plugin.geiser.api.IGeiserPlugin;
import org.plugin.geiser.api.PeticionBusquedaTramitGeiser;
import org.plugin.geiser.api.PeticionConsultaGeiser;
import org.plugin.geiser.api.PeticionRegistroEnvioGeiser;
import org.plugin.geiser.api.PeticionRegistroGeiser;
import org.plugin.geiser.api.RespuestaBusquedaGeiser;
import org.plugin.geiser.api.RespuestaBusquedaTramitGeiser;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.plugin.geiser.api.ws.ApunteRegistroType;
import org.plugin.geiser.api.ws.EstadoAsientoEnum;
import org.plugin.geiser.api.ws.EstadoTramitacionRegistroType;
import org.plugin.geiser.api.ws.RespuestaType;
import org.plugin.geiser.api.ws.ResultadoBusquedaEstadoTramitacionType;
import org.plugin.geiser.api.ws.ResultadoBusquedaType;
import org.plugin.geiser.api.ws.ResultadoConsultaType;
import org.plugin.geiser.api.ws.ResultadoRegistroType;
import org.plugin.geiser.api.ws.TipoRespuestaEnum;
import org.plugin.geiser.mock.helper.ConversionPluginHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeiserMockPlugin extends AbstractPluginProperties implements IGeiserPlugin {
    
	public GeiserMockPlugin() {
        super();
    }
	
    public GeiserMockPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    public GeiserMockPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }
    
	@Override
	public RespuestaRegistroGeiser registrar(PeticionRegistroGeiser peticion) throws GeiserPluginException {
		ResultadoRegistroType resultado = new ResultadoRegistroType();
		try {
			RespuestaType respuesta = new RespuestaType();
			respuesta.setCodigo(0);
			respuesta.setTipo(TipoRespuestaEnum.OK);
			resultado.setRespuesta(respuesta);

			ApunteRegistroType apunte = new ApunteRegistroType();
			apunte.setNuRegistro("REGAGE21e00000605822");
			apunte.setTimestampRegistrado("20211209160000");
			apunte.setEstado(EstadoAsientoEnum.FINALIZADO);
			resultado.setApunte(apunte);
			
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de registro", ex);
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaRegistroGeiser.class);
	}

	@Override
	public RespuestaRegistroGeiser registrarEnviar(PeticionRegistroEnvioGeiser peticion) throws GeiserPluginException {
		ResultadoRegistroType resultado = new ResultadoRegistroType();
		try {
			RespuestaType respuesta = new RespuestaType();
			respuesta.setCodigo(0);
			respuesta.setTipo(TipoRespuestaEnum.OK);
			resultado.setRespuesta(respuesta);

			ApunteRegistroType apunte = new ApunteRegistroType();
			apunte.setNuRegistro("REGAGE21e00000605822");
			apunte.setTimestampRegistrado("20211209160000");
			apunte.setEstado(EstadoAsientoEnum.ENVIADO_PENDIENTE_CONFIRMACION);
			resultado.setApunte(apunte);
			
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de registro y envío", ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaRegistroGeiser.class);
	}

	@Override
	public RespuestaConsultaGeiser consulta(PeticionConsultaGeiser peticion) throws GeiserPluginException {
		ResultadoConsultaType resultado = new ResultadoConsultaType();
		try {
			RespuestaType respuesta = new RespuestaType();
			respuesta.setCodigo(0);
			respuesta.setTipo(TipoRespuestaEnum.OK);
			resultado.setRespuesta(respuesta);

			ApunteRegistroType apunte = new ApunteRegistroType();
			apunte.setNuRegistro("REGAGE21e00000605822");
			apunte.setTimestampRegistrado("20211209160000");
			apunte.setEstado(EstadoAsientoEnum.ENVIADO_PENDIENTE_CONFIRMACION);
			resultado.getApuntes().add(apunte);

			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de consulta", ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaConsultaGeiser.class);
	}

	@Override
	public List<RespuestaBusquedaGeiser> buscar(String fechaInicio, String fechaFin) throws GeiserPluginException {
		 List<RespuestaBusquedaGeiser> resultadosBusqueda = new ArrayList<RespuestaBusquedaGeiser>();
		try {
			ResultadoBusquedaType resultado = new ResultadoBusquedaType();
			RespuestaType respuesta = new RespuestaType();
			respuesta.setCodigo(0);
			respuesta.setTipo(TipoRespuestaEnum.OK);
			resultado.setRespuesta(respuesta);
			
			ApunteRegistroType apunte = new ApunteRegistroType();
			apunte.setNuRegistro("REGAGE21e00000605822");
			apunte.setTimestampRegistrado("20211209160000");
			apunte.setEstado(EstadoAsientoEnum.ENVIADO_PENDIENTE_CONFIRMACION);
			resultado.getApuntes().add(apunte);
			
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
			
			RespuestaBusquedaGeiser resultadoConverted = new ConversionPluginHelper().convertir(
					resultado, 
					RespuestaBusquedaGeiser.class);
			resultadosBusqueda.add(resultadoConverted);
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de búsqueda", ex.getCause());
		}
		return resultadosBusqueda;
	}

	@Override
	public RespuestaBusquedaTramitGeiser buscarEstadoTramitacion(PeticionBusquedaTramitGeiser peticion) throws GeiserPluginException {
		ResultadoBusquedaEstadoTramitacionType resultado = new ResultadoBusquedaEstadoTramitacionType();
		try {
			RespuestaType respuesta = new RespuestaType();
			respuesta.setCodigo(0);
			respuesta.setTipo(TipoRespuestaEnum.OK);
			resultado.setRespuesta(respuesta);

			EstadoTramitacionRegistroType estadoT = new EstadoTramitacionRegistroType();
			estadoT.setNuRegistro("REGAGE21e00000605822");
			estadoT.getIdentificadoresIntercambioSIR().add("O00016551_21_00000149");
			resultado.getEstadosTramitacion().add(estadoT);
			
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de búsqueda de estado de tramitación", ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaBusquedaTramitGeiser.class);
	}
    
	public class Handler1 implements SOAPHandler<SOAPMessageContext> {
		public Set<QName> getHeaders() {
			return Collections.emptySet();
		}

		public boolean handleMessage(SOAPMessageContext messageContext) {
			Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

			if (outboundProperty.booleanValue()) {
				System.out.println("\nxml petición:");
			} else {
				System.out.println("\nxml respuesta:");
			}
			// Debug 
			StringBuilder sb = new StringBuilder();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				messageContext.getMessage().writeTo(baos);
				sb.append(baos.toString());
			} catch (Exception ex) {
				sb.append("Error al processar el missatge XML: " + ex.getMessage());
			}
			logger.info(sb.toString());
			return true;
		}

		public boolean handleFault(SOAPMessageContext messageContext) {
			return true;
		}

		public void close(MessageContext messageContext) {
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(GeiserMockPlugin.class);

	@Override
	public String getUsuariCreacioRegistres() throws GeiserPluginException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnexoGSample obtenerJustificanteGEISER(PeticionConsultaGeiser peticion) throws GeiserPluginException {
		// TODO Auto-generated method stub
		return null;
	}
}
