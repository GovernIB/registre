package es.caib.regweb3.ws.v3.test;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ConcurrentTestRunner.class)
public class ConcurrentWsTest extends RegWebTestUtils{

    private final static int THREAD_COUNT = 10;

    protected static RegWebAsientoRegistralWs asientoRegistralApi;
    protected static RegWebRegistroEntradaWs registroEntradaApi;

    /**
     * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        asientoRegistralApi = getAsientoRegistralApi();
        registroEntradaApi = getRegistroEntradaApi();
    }

    @Test
    @ThreadCount(THREAD_COUNT)
    public void crearAsiento() throws Exception {

        for (int i = 0; i < 2; i++) {

            try {

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,null,false,false);

                printAsientoBasico(asientoRegistralWs);

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


}
