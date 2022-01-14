package es.caib.regweb3.ws.v3.test;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ConcurrentTestRunner.class)
public class ConcurrentWsTest extends RegWebTestUtils{

    private final static int THREAD_COUNT = 5;

    protected static RegWebAsientoRegistralWs asientoRegistralApi;
    protected static RegWebRegistroEntradaWs registroEntradaApi;

    /**
     * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        setEntorno("_dev");
        asientoRegistralApi = getAsientoRegistralApi();
        registroEntradaApi = getRegistroEntradaApi();
    }

    @Test
    @ThreadCount(THREAD_COUNT)
    public void crearAsiento() throws Exception {

        try {
            long inicio = System.currentTimeMillis();
            AsientoRegistralWs entrada = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true,false);
            entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),entrada,null,false,false);

            //AsientoRegistralWs salida = getAsiento_to_PersonaFisica(REGISTRO_SALIDA, false);
            //salida = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),salida,null,false,false);

            System.out.println("Después crear asiento: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio));

            printAsientoBasico(entrada);
            //printAsientoBasico(salida);

        } catch (WsI18NException e) {
            String msg = WsClientUtils.toString(e);
            System.out.println("Error WsI18NException: " + msg);
            throw e;
        } catch (WsValidationException e) {
            String msg = WsClientUtils.toString(e);
            System.out.println("Error WsValidationException: " + msg);
            throw e;
        }
    }


    //@Test
    @ThreadCount(THREAD_COUNT)
    public void crearRegistroEntradaApiAntigua() throws Exception {

        try {

            RegistroEntradaWs registroEntradaWs = getRegistroEntrada_to_PersonaFisica(false);
            IdentificadorWs identificadorWs = registroEntradaApi.nuevoRegistroEntrada(getTestEntidadCodigoDir3(),registroEntradaWs);

            printIdentificadorWSBasico(identificadorWs);


        } catch (WsI18NException e) {
            String msg = WsClientUtils.toString(e);
            System.out.println("Error WsI18NException: " + msg);
            throw e;
        } catch (WsValidationException e) {
            String msg = WsClientUtils.toString(e);
            System.out.println("Error WsValidationException: " + msg);
            throw e;
        }
    }



}
