package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.*;
import org.junit.BeforeClass;
import org.junit.Test;



/**
 * Created by earrivi on 06/05/19.
 *
 * @author earrivi
 */
public class RegWebAsientoRegistralTest extends RegWebTestUtils {

    protected static RegWebAsientoRegistralWs asientoRegistralApi;

    /**
     * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        asientoRegistralApi = getAsientoRegistralApi();
    }

    @Test
    public void obtenerRegistroEntrada() {

        try {
            JustificanteReferenciaWs just = asientoRegistralApi.obtenerReferenciaJustificante(getTestEntidadCodigoDir3(),"");

            System.out.println("Justificante csv: " + just.getCsv());
            System.out.println("Justificante: url" + just.getUrl());

        } catch (WsValidationException e) {
            e.printStackTrace();
        } catch (WsI18NException e) {
            e.printStackTrace();
        }
    }


    //@Test
    public void obtenerJustificante() throws Exception{

        JustificanteWs justificanteWs = asientoRegistralApi.obtenerJustificante(getTestEntidadCodigoDir3(),"SALU-E-48/2018", RegwebConstantes.REGISTRO_ENTRADA);

        System.out.println("Justificante: " + justificanteWs.getJustificante().length);
    }





}
