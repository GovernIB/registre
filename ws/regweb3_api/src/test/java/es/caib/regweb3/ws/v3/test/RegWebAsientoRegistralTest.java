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

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_SALIDA, false);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,TIPO_OPERACION_NOTIFICACION,false,false);

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

            JustificanteReferenciaWs just = asientoRegistralApi.obtenerReferenciaJustificante(getTestEntidadCodigoDir3(),"GOIB-E-513/2021");

            System.out.println("Justificante csv: " + just.getCsv());
            System.out.println("Justificante: url: " + just.getUrl());

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void obtenerJustificante() throws Exception{

        JustificanteWs justificanteWs = asientoRegistralApi.obtenerJustificante(getTestEntidadCodigoDir3(),"GOIB-S-68/2021", RegwebConstantes.REGISTRO_SALIDA);

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
            ResultadoBusquedaWs asientos = asientoRegistralApi.obtenerAsientosCiudadano(getTestEntidadCodigoDir3(),"43146650F",0,"es");

            System.out.println("Asientos encontrados: " +asientos.getTotalResults());

            for (Object asiento : asientos.getResults()) {
                AsientoWs asientoWs = (AsientoWs) asiento;
                System.out.println("Num. Registro: " + asientoWs.getNumeroRegistro());
                System.out.println("Fecha: " + asientoWs.getFechaRegistro());
                System.out.println("Destino codigo: " + asientoWs.getCodigoDestino());
                System.out.println("Destino denominacion: " + asientoWs.getDenominacionDestino());
                System.out.println("Extracto: " + asientoWs.getExtracto());
                System.out.println("");
            }

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerAsientoCiudadano() {

        AsientoWs asientoWs = asientoRegistralApi.obtenerAsientoCiudadano(getTestEntidadCodigoDir3(),"43146650F","L18-E-45/2020","es");

        System.out.println("Num. Registro: " + asientoWs.getNumeroRegistro());
        System.out.println("Fecha: " + asientoWs.getFechaRegistro());
        System.out.println("Destino codigo: " + asientoWs.getCodigoDestino());
        System.out.println("Destino denominacion: " + asientoWs.getDenominacionDestino());
        System.out.println("Extracto: " + asientoWs.getExtracto());
        System.out.println("");

        if(asientoWs.getJustificante() != null){
            System.out.println("Justificante Id: " + asientoWs.getJustificante().getFileID());
            System.out.println("Justificante Nombre: " + asientoWs.getJustificante().getName());
            System.out.println("Justificante Mime: " + asientoWs.getJustificante().getMime());
            System.out.println("Justificante Size: " + asientoWs.getJustificante().getSize());

            FileContentWs fileContentWs = asientoRegistralApi.obtenerAnexoCiudadano(getTestEntidadCodigoDir3(),asientoWs.getJustificante().getFileID() , "es");
            System.out.println("Justificante url: " + fileContentWs.getUrl());
        }


        for(FileInfoWs fileInfoWs:asientoWs.getAnexos()){
            System.out.println("");
            System.out.println("Anexo Id: " + fileInfoWs.getFileID());
            System.out.println("Anexo Nombre: " + fileInfoWs.getName());
            System.out.println("Anexo Mime: " + fileInfoWs.getMime());
            System.out.println("Anexo Size: " + fileInfoWs.getSize());
        }

    }

    @Test
    public void obtenerAnexo() {

        FileContentWs fileContentWs = asientoRegistralApi.obtenerAnexoCiudadano(getTestEntidadCodigoDir3(), 107118L, "es");

        System.out.println("Id: " + fileContentWs.getFileInfoWs().getFileID());
        System.out.println("Nombre: " + fileContentWs.getFileInfoWs().getName());
        System.out.println("Mime: " + fileContentWs.getFileInfoWs().getMime());
        System.out.println("Size: " + fileContentWs.getFileInfoWs().getSize());

        System.out.println("url: " + fileContentWs.getUrl());
        System.out.println("error: " + fileContentWs.getError());
        System.out.println("Data: " + fileContentWs.getData().length);
    }
}
