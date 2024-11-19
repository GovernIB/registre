package org.plugin.lema.apb;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;
import org.plugin.lema.api.ConsultaAcuseReciboRequest;
import org.plugin.lema.api.ConsultaAcuseReciboResponse;
import org.plugin.lema.api.Envio;
import org.plugin.lema.api.IdentificadorAcuseRecibo;
import org.plugin.lema.api.LocalizaRequest;
import org.plugin.lema.api.LocalizaResponse;

public class LemaApbPluginTest {

	private static final BigInteger NOTIFICACION = new java.math.BigInteger("2");
	private static final String NIF_TITULAR = "Q0767004E";
	private static final String NIF_RECEPTOR = "Q0767004E";
	private static final String NIF_DESTINATARIO = "EA0004518";

	@Test
	public void localizaTest() throws ParseException, DatatypeConfigurationException {
		LemaApbPlugin plugin = new LemaApbPlugin();

		LocalizaRequest request = new LocalizaRequest();
		request.setNifTitular(NIF_TITULAR);
		request.setFechaDesde(stringToDate("2024-10-30T00:00:00"));
		request.setFechaHasta(stringToDate("2024-10-31T09:00:00"));
		LocalizaResponse response = plugin.localiza(request);

		for (Envio envio : response.getEnvios()) {
			System.out.println();
			System.out.println("Resultat - identificado: " + envio.getIdentificador());
		}
	}
	
//	@Test
	public void consultaAcuseReciboTest() throws ParseException, DatatypeConfigurationException {
		LemaApbPlugin plugin = new LemaApbPlugin();

		ConsultaAcuseReciboRequest request = new ConsultaAcuseReciboRequest();
		request.setNifReceptor(NIF_RECEPTOR);
		request.setIdentificador("65614626728b45804953");
		request.setCodigoOrigen(2);
		IdentificadorAcuseRecibo identificador = new IdentificadorAcuseRecibo();
		identificador.setCsvResguardo("DEHU-988c90c86d6defc2f6391ae3faad4a1a");
		request.setIdentificadorAcuse(identificador);
		
		ConsultaAcuseReciboResponse consultaAcuseReciboResponse = plugin.consultaAcuseRecibo(request);

		System.out.println();
		System.out.println("Resultat - nombre acuse: " + consultaAcuseReciboResponse.getAcuseRecibo().getNombreAcuse());
	
	}

	private static Date stringToDate(String dateString) throws ParseException, DatatypeConfigurationException {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString);
	}
	

}
