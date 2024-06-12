package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Created by mgonzalez on 28/02/24.
 *
 * @author mgonzalez
 */
public class RegWebAsientoRegistralLibSirTest extends RegWebTestUtils {

    protected static RegWebAsientoRegistralWs asientoRegistralApi;

    /**
     * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
       // setEntorno("_localhost_PRO");
        //setEntorno("_registre3");
        //setEntorno("_proves");
        setEntorno("_localhost");
        asientoRegistralApi = getAsientoRegistralApi();
    }

    @Test
    public void obtenerSesion() throws Exception{

        // Obtenemos el idSesion
        Long idSesion = asientoRegistralApi.obtenerSesionRegistro(getTestEntidadCodigoDir3());

        Assert.assertNotNull(idSesion);

    }

    @Test
    public void verificarSesionNoIniciada() throws Exception{

        // Obtenemos el idSesion
        Long idSesion = asientoRegistralApi.obtenerSesionRegistro(getTestEntidadCodigoDir3());

        Assert.assertNotNull(idSesion);

        AsientoRegistralSesionWs sesionWs = asientoRegistralApi.verificarAsientoRegistral(getTestEntidadCodigoDir3(), idSesion);

        Assert.assertEquals(RegwebConstantes.SESION_NO_INICIADA, sesionWs.getEstado());
    }



    //Método que crea un registro de entrada con los parámetros del SIR-CER-12081-plan-pruebas-protocolo-intercambio de LIBSIR
    @Test
    public void crearAsientoEntradaSIR_IN_000() throws Exception {
        //sin organismo origen, con 1 interesado, sin anexos

        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_LIBSIR( REGISTRO_ENTRADA, true, true, false, false);

                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,null,true,false);

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


    //Método que crea un registro de salida con los parámetros del SIR-CER-12081-plan-pruebas-protocolo-intercambio de LIBSIR
    @Test
    public void crearAsientoSalidaSIR_IN_000() throws Exception {
        //con organismo origen, 3 ficheros adjuntos

        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_LIBSIR( REGISTRO_SALIDA,false, true, true, false);

                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,null,true,false);

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


    @Test
    public void crearAsientoEntradaSIRGE_PR_001() throws Exception {
        //sin organismo origen, con 1 interesado, sin anexos

        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_LIBSIR( REGISTRO_ENTRADA, false, false, true, false);

                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,null,true,false);

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


    @Test
    //Método que crea un registro de entrada con los parámetros del SIR-CER-12080-plan-pruebas-emision-recepcionSICRES4 de LIBSIR
    public void crearAsientoEntradaSIRGE_PR_002() throws Exception {


        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_LIBSIR( REGISTRO_ENTRADA, false, true, true, false);

                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,null,true,false);

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

    @Test
    //Método que crea un registro de entrada con los parámetros del SIR-CER-12080-plan-pruebas-emision-recepcionSICRES4 de LIBSIR
    public void crearAsientoEntradaSIRGE_PR_003() throws Exception {


        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_LIBSIRMaximo( REGISTRO_ENTRADA, false, true, true, true);

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

    @Test
    //Método que crea un registro de entrada con los parámetros del SIR-CER-12080-plan-pruebas-emision-recepcionSICRES4 de LIBSIR
    public void crearAsientoEntradaSIRGE_PR_019() throws Exception {


        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_LIBSIR( REGISTRO_ENTRADA, false, false
                        , true, true);

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

    @Test
    //Método que crea un registro de salida con los parámetros del SIR-CER-12080-plan-pruebas-emision-recepcionSICRES4 de LIBSIR
    public void crearAsientoSalidaSIRGE_PR_022() throws Exception {


        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_LIBSIR( REGISTRO_SALIDA, false, true
                        , false, true);

                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,null,true,false);

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
