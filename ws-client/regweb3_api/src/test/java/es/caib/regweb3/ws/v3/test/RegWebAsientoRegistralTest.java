package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


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

    @Test
    public void crearAsientoConSesion() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {

                // Obtenemos el idSesion
                Long idSesion = asientoRegistralApi.obtenerSesionRegistro(getTestEntidadCodigoDir3());
                System.out.println("IdSesion: " + idSesion);

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true, true);
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

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true, true);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null, getTestEntidadCodigoDir3(), asientoRegistralWs, null, true, false);

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
    public void crearAsientoEntradaJuridica() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaJuridica(REGISTRO_ENTRADA, true, true);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null, getTestEntidadCodigoDir3(), asientoRegistralWs, null, true, false);

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
    public void crearAsientoEntradaAdministracion() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_Administracion(REGISTRO_ENTRADA, true, "Agencia Tributaria Islas Baleares (ATIB)", "A04013587");
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null, getTestEntidadCodigoDir3(), asientoRegistralWs, null, true, false);

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

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true, true);
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
    public void crearAsientoSalida() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_AdministracionExterna(REGISTRO_SALIDA, true);
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


    /**
     * Tipo notificación: Se crea el justificante del registroSalida y se marca como REGISTRO_ENVIADO_NOTIFICAR
     * @throws Exception
     */
    @Test
    public void crearAsientoSalidaNotificacion() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {

                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_SALIDA, false, true);
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

    /**
     * Tipo comunicación:
     *                    1-  Si va dirigido a una Persona física o jurídica se crea el justificante y se marca como REGISTRO_VALIDO
     *                    2-  Si va dirigido a una administración se crea el justificante y dependiendo de si el destino está en SIR o no se envía o se marca como OFICIO_EXTERNO
     * @throws Exception
     */
    @Test
    public void crearAsientoSalidaComunicacion() throws Exception {

        for (int i = 0; i < 1; i++) {

            try {
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_AdministracionSir(REGISTRO_SALIDA, true);
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),asientoRegistralWs,TIPO_OPERACION_COMUNICACION,false,false);

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
                AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisica(REGISTRO_SALIDA, true, true);
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

            AsientoRegistralWs registroEntrada = asientoRegistralApi.obtenerAsientoRegistral(getTestEntidadCodigoDir3(), "GPRO-E-7/2022", RegwebConstantes.REGISTRO_ENTRADA,true);
            Assert.assertNotNull(registroEntrada);

            printAsiento(registroEntrada);


        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crearObtenerAsiento() throws Exception{

        try {

            AsientoRegistralWs entrada = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true, true);
            entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(), entrada,TIPO_OPERACION_COMUNICACION,false,false);

            AsientoRegistralWs registroEntrada = asientoRegistralApi.obtenerAsientoRegistral(getTestEntidadCodigoDir3(), entrada.getNumeroRegistroFormateado(), RegwebConstantes.REGISTRO_ENTRADA,true);
            Assert.assertNotNull(registroEntrada);

            AsientoRegistralWs salida = getAsiento_to_PersonaFisica(REGISTRO_SALIDA, true, true);
            salida = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(),salida,TIPO_OPERACION_NOTIFICACION,false,false);

            AsientoRegistralWs registroSalida = asientoRegistralApi.obtenerAsientoRegistral(getTestEntidadCodigoDir3(),salida.getNumeroRegistroFormateado(), RegwebConstantes.REGISTRO_SALIDA,true);
            Assert.assertNotNull(registroSalida);

            printAsiento(registroEntrada);
            printAsiento(registroSalida);

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crear_obtenerReferenciaJustificante() throws InterruptedException {

        try {
            AsientoRegistralWs entrada = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true, true);
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

        AsientoRegistralWs entrada = getAsiento_to_PersonaFisica(REGISTRO_ENTRADA, true, true);
        entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(), entrada,TIPO_OPERACION_COMUNICACION,true,false);

        Thread.sleep(10000);

        JustificanteWs justificanteWs = asientoRegistralApi.obtenerJustificante(getTestEntidadCodigoDir3(),entrada.getNumeroRegistroFormateado(), RegwebConstantes.REGISTRO_ENTRADA);

        Assert.assertNotNull(justificanteWs.getJustificante());
        System.out.println("Justificante: " + justificanteWs.getJustificante().length);
    }

    @Test
    public void distribuirAsientoregistral() throws Exception{

        try {
            AsientoRegistralWs entrada = getAsiento_to_AdministracionExterna(REGISTRO_SALIDA, false);
            entrada = asientoRegistralApi.crearAsientoRegistral(null,getTestEntidadCodigoDir3(), entrada,null,true,false);

            asientoRegistralApi.distribuirAsientoRegistral(getTestEntidadCodigoDir3(),entrada.getNumeroRegistroFormateado());

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerOficioExterno() {

        try {
            AsientoRegistralWs entrada = getAsiento_to_AdministracionExterna(REGISTRO_SALIDA, false);
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
            ResultadoBusquedaWs asientos = asientoRegistralApi.obtenerAsientosCiudadano(getTestEntidadCodigoDir3(),"99999999R",0);

            System.out.println("Asientos encontrados: " +asientos.getTotalResults());

            for (Object asiento : asientos.getResults()) {
                AsientoRegistralWs asientoRegistralWs = (AsientoRegistralWs) asiento;
                printAsiento(asientoRegistralWs);
            }

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerAsientoCiudadano() {

        try {
            AsientoRegistralWs asientoRegistralWs = asientoRegistralApi.obtenerAsientoCiudadano(getTestEntidadCodigoDir3(),"99999999R","GOIBE315/2020");

            printAsiento(asientoRegistralWs);

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerAsientosCiudadanoCarpeta() {

        try {


            Timestamp tFechaInicio = setDate(01,03,2023);//ojo el month es 1 menos (03 = Abril)

            Timestamp tFechaFin = setDate(01,04,2023);
            Integer resultPorPagina= 5;
            String numeroRegistroFormateado="GPRO-E-3/2021";

            List<Integer> estados = new ArrayList<>();
            estados.add(1);
            estados.add(3);
            estados.add(5);
            estados.add(6);
            estados.add(7);

            ResultadoBusquedaWs asientos = asientoRegistralApi.obtenerAsientosCiudadanoCarpeta(getTestEntidadCodigoDir3(),"44328254D",0,"es",tFechaInicio, tFechaFin,"", estados, "", resultPorPagina );

            System.out.println("Asientos encontrados: " +asientos.getTotalResults());

            for (Object asiento : asientos.getResults()) {
                AsientoWs asientoWs = (AsientoWs) asiento;
                System.out.println("Num. Registro: " + asientoWs.getNumeroRegistro());
                System.out.println("Fecha: " + asientoWs.getFechaRegistro());
                System.out.println("Destino codigo: " + asientoWs.getCodigoDestino());
                System.out.println("Destino denominacion: " + asientoWs.getDenominacionDestino());
                System.out.println("Extracto: " + asientoWs.getExtracto());
                System.out.println("Estado: " + asientoWs.getEstado());
                System.out.println("");
            }

        } catch (WsValidationException | WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerAsientoCiudadanoCarpeta() {

        AsientoWs asientoWs = asientoRegistralApi.obtenerAsientoCiudadanoCarpeta(getTestEntidadCodigoDir3(),"44328254D","GPRO-E-161/2021","es");

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
            System.out.println("Anexo Hash: " + fileInfoWs.getHash());
        }

    }

    @Test
    public void obtenerAnexo() {

        FileContentWs fileContentWs = asientoRegistralApi.obtenerAnexoCiudadano(getTestEntidadCodigoDir3(), 89641L, "es");

        System.out.println("Id: " + fileContentWs.getFileInfoWs().getFileID());
        System.out.println("Nombre: " + fileContentWs.getFileInfoWs().getName());
        System.out.println("Mime: " + fileContentWs.getFileInfoWs().getMime());
        System.out.println("Size: " + fileContentWs.getFileInfoWs().getSize());

        System.out.println("url: " + fileContentWs.getUrl());
        System.out.println("error: " + fileContentWs.getError());
        System.out.println("Data: " + fileContentWs.getData().length);
    }
}
