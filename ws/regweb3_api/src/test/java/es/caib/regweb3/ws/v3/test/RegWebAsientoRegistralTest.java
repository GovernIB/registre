package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.Assert;
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

    @Test
    public void crearAsientoConSesion() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {

                // Obtenemos el idSesion
                Long idSesion = asientoRegistralApi.obtenerSesionRegistro(getTestEntidadCodigoDir3());
                System.out.println("IdSesion: " + idSesion);

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(idSesion, getTestEntidadCodigoDir3(), asientoRegistralWs, null, true, false);

                printAsientoBasico(asientoRegistralWs);

                // Verificar el estado de la operación
                AsientoRegistralSesionWs sesionWs = asientoRegistralApi.verificarAsientoRegistral(getTestEntidadCodigoDir3(), idSesion);

                System.out.println("Estado: " + sesionWs.getEstado());

                Assert.assertEquals(RegwebConstantes.SESION_FINALIZADA, sesionWs.getEstado());

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
    public void crearAsientoEntrada() throws Exception {

        for (int i = 0; i < 1; i++) {

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

    @Test
    public void crearAsientoEntradaConAnexos() throws Exception {

        for (int i = 0; i < 10; i++) {

            try {

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,null,false,true);

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
    public void crearAsientoSalida() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_AdministracionSir(REGISTRO_SALIDA);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,TIPO_OPERACION_COMUNICACION,true,false);

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
    public void crearAsientoSalidaNotificacion() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_SALIDA, false);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,TIPO_OPERACION_NOTIFICACION,true,false);

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
    public void crearAsientoConAnexos() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_SALIDA, true);
                asientoRegistralWs.getAnexos().addAll(getAnexos());

                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,null,false,true);

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
    public void obtenerAsiento() throws Exception{

        try {

            AsientoRegistralWs entrada = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true);
            entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(), entrada,TIPO_OPERACION_COMUNICACION,true,false);

            Assert.assertNotNull(asientoRegistralApi.obtenerAsientoRegistral(getTestEntidadCodigoDir3(), entrada.getNumeroRegistroFormateado(), RegwebConstantes.REGISTRO_ENTRADA,false));

            AsientoRegistralWs salida = getAsiento_to_PersonaFisica(REGISTRO_SALIDA, true);
            salida = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),salida,TIPO_OPERACION_COMUNICACION,true,false);

            Assert.assertNotNull(asientoRegistralApi.obtenerAsientoRegistral(getTestEntidadCodigoDir3(),salida.getNumeroRegistroFormateado(), RegwebConstantes.REGISTRO_SALIDA,false));

            printAsiento(entrada);
            printAsiento(salida);

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crear_obtenerReferenciaJustificante() throws InterruptedException {

        try {
            AsientoRegistralWs entrada = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true);
            entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(), entrada,TIPO_OPERACION_COMUNICACION,true,false);

            Thread.sleep(10000);

            JustificanteReferenciaWs just = asientoRegistralApi.obtenerReferenciaJustificante(getTestEntidadCodigoDir3(),entrada.getNumeroRegistroFormateado());

            System.out.println("Justificante csv: " + just.getCsv());
            System.out.println("Justificante: url: " + just.getUrl());

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerReferenciaJustificante() throws InterruptedException {

        try {

            JustificanteReferenciaWs just = asientoRegistralApi.obtenerReferenciaJustificante(getTestEntidadCodigoDir3(),"L18E256/2020");

            System.out.println("Justificante csv: " + just.getCsv());
            System.out.println("Justificante: url: " + just.getUrl());

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void obtenerJustificante() throws Exception{

        JustificanteWs justificanteWs = asientoRegistralApi.obtenerJustificante(getTestEntidadCodigoDir3(),"L18E256/2020", RegwebConstantes.REGISTRO_ENTRADA);

        Assert.assertNotNull(justificanteWs.getJustificante());

        System.out.println("Justificante: " + justificanteWs.getJustificante().length);
    }

    @Test
    public void crear_obtenerJustificante() throws Exception{

        AsientoRegistralWs entrada = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true);
        entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(), entrada,TIPO_OPERACION_COMUNICACION,true,false);

        Thread.sleep(10000);

        JustificanteWs justificanteWs = asientoRegistralApi.obtenerJustificante(getTestEntidadCodigoDir3(),entrada.getNumeroRegistroFormateado(), RegwebConstantes.REGISTRO_ENTRADA);

        Assert.assertNotNull(justificanteWs.getJustificante());
        System.out.println("Justificante: " + justificanteWs.getJustificante().length);
    }

    @Test
    public void distribuirAsientoregistral() throws Exception{

        try {
            AsientoRegistralWs entrada = getAsiento_to_AdministracionExterna(REGISTRO_ENTRADA);
            entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(), entrada,null,true,false);

            asientoRegistralApi.distribuirAsientoRegistral(getTestEntidadCodigoDir3(),entrada.getNumeroRegistroFormateado());

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerOficioExterno() {

        try {
            AsientoRegistralWs entrada = getAsiento_to_AdministracionExterna(REGISTRO_ENTRADA);
            entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(), entrada,TIPO_OPERACION_COMUNICACION,true,false);

            OficioWs oficio = asientoRegistralApi.obtenerOficioExterno(getTestEntidadCodigoDir3(),entrada.getNumeroRegistroFormateado());

            Assert.assertNotNull(oficio.getOficio());


        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerAsientosCiudadano() {

        try {
            ResultadoBusquedaWs asientos = asientoRegistralApi.obtenerAsientosCiudadano(getTestEntidadCodigoDir3(),"43146650F",0);

            System.out.println("Asientos encontrados: " +asientos.getTotalResults());

            /*for (Object asiento : asientos.getResults()) {
                AsientoRegistralWs asientoRegistralWs = (AsientoRegistralWs) asiento;
                System.out.println("Numero: " + asientoRegistralWs.getNumeroRegistroFormateado());
            }*/

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerAsientoCiudadano() {

        try {
            AsientoRegistralWs asientoRegistralWs = asientoRegistralApi.obtenerAsientoCiudadano(getTestEntidadCodigoDir3(),"43146650F","SALU-E-169/2019");

            printAsiento(asientoRegistralWs);

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }
}
