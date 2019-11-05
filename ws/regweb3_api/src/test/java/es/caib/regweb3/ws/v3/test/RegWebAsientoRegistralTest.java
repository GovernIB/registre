package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.BeforeClass;
import org.junit.Test;

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
        asientoRegistralApi = getAsientoRegistralApi();
    }



    @Test
    public void crearAsientoRegistral() throws Exception {

        for (int i = 0; i < 1; i++) {

            AsientoRegistralWs asientoRegistralWs = getAsientoRegistral(REGISTRO_ENTRADA, true);

            try {
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(getTestEntidadCodigoDir3(),asientoRegistralWs,TIPO_OPERACION_COMUNICACION,true,false);

                printAsientoRegistral(asientoRegistralWs);

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
    public void crearAsientoRegistralConAnexos() throws Exception {

        for (int i = 0; i < 1; i++) {

            AsientoRegistralWs asientoRegistralWs = getAsientoRegistral(REGISTRO_SALIDA, true);

            asientoRegistralWs.getAnexos().addAll(getAnexos());

            try {
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(getTestEntidadCodigoDir3(),asientoRegistralWs,null,false,true);

                printAsientoRegistral(asientoRegistralWs);

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
    public void obtenerAsientoregistral() throws Exception{

        try {
            AsientoRegistralWs entrada = asientoRegistralApi.obtenerAsientoRegistral(getTestEntidadCodigoDir3(),"SALU-E-214/2019", RegwebConstantes.REGISTRO_ENTRADA,false);
            AsientoRegistralWs salida = asientoRegistralApi.obtenerAsientoRegistral(getTestEntidadCodigoDir3(),"SALU-S-117/2019", RegwebConstantes.REGISTRO_SALIDA,false);

            printAsientoRegistral(entrada);
            printAsientoRegistral(salida);

        } catch (WsValidationException e) {
            e.printStackTrace();
        } catch (WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerReferenciaJustificante() {

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

    @Test
    public void distribuirAsientoregistral() throws Exception{

        try {
            asientoRegistralApi.distribuirAsientoRegistral(getTestEntidadCodigoDir3(),"PRES-E-2/2019");

        } catch (WsValidationException e) {
            e.printStackTrace();
        } catch (WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerOficioExterno() {

        try {
            OficioWs oficio = asientoRegistralApi.obtenerOficioExterno(getTestEntidadCodigoDir3(),"");

            System.out.println("Oficio: " + oficio.getOficio());

        } catch (WsValidationException e) {
            e.printStackTrace();
        } catch (WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerAsientosCiudadano() {

        try {
            ResultadoBusquedaWs asientos = asientoRegistralApi.obtenerAsientosCiudadano(getTestEntidadCodigoDir3(),"43146650F",0);

            System.out.println("Asientos encontrados: " +asientos.getTotalResults());

            for (Object asiento : asientos.getResults()) {
                AsientoRegistralWs asientoRegistralWs = (AsientoRegistralWs) asiento;
                System.out.println("Numero: " + asientoRegistralWs.getNumeroRegistroFormateado());
            }

        } catch (WsValidationException e) {
            e.printStackTrace();
        } catch (WsI18NException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void obtenerAsientoCiudadano() {

        try {
            AsientoRegistralWs asientoRegistralWs = asientoRegistralApi.obtenerAsientoCiudadano(getTestEntidadCodigoDir3(),"43146650F","SALU-E-169/2019");

            printAsientoRegistral(asientoRegistralWs);

        } catch (WsValidationException e) {
            e.printStackTrace();
        } catch (WsI18NException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra por pantala el contenido de un AsientoRegistralWs
     * @param asientoRegistralWs
     */
    private void printAsientoRegistral(AsientoRegistralWs asientoRegistralWs){
        System.out.println("-------------------------------------------------------------");

        System.out.println("Num. Registro: " + asientoRegistralWs.getNumeroRegistroFormateado());
        System.out.println("Tipo registro: " + asientoRegistralWs.getTipoRegistro());
        System.out.println("Fecha Registro: " + asientoRegistralWs.getFechaRegistro());
        System.out.println("Resumen: " + asientoRegistralWs.getResumen());

        printAnexosWs(asientoRegistralWs.getAnexos());
        printInteresadosWs(asientoRegistralWs.getInteresados());

        System.out.println("");
    }

    /**
     *
     * @param anexos
     */
    private void printAnexosWs(List<AnexoWs> anexos){

        System.out.println("");
        System.out.println("Total anexos: " + anexos.size());
        for (AnexoWs anexo : anexos) {
            System.out.println("");
            System.out.println("Nombre anexo: " + anexo.getTitulo());
            System.out.println("isJustificante: " + anexo.isJustificante());
        }
    }

    /**
     *
     * @param interesados
     */
    private void printInteresadosWs(List<InteresadoWs> interesados){

        System.out.println("");
        System.out.println("Total interesados: " + interesados.size());

        for (InteresadoWs i : interesados) {
                System.out.println("");
            System.out.println("Interesado: " + printInteresadoWs(i.getInteresado()));

            if(i.getRepresentante() != null){
                System.out.println("Representante: " +printInteresadoWs(i.getRepresentante()));

            }
        }
    }

    private String printInteresadoWs(DatosInteresadoWs i){

        if(i.getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){
            return i.getRazonSocial() + " " + i.getDocumento();

        }else if(i.getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
            return i.getNombre() + " " + i.getApellido1();

        }else if(i.getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
            return i.getNombre() + " " + i.getApellido1();
        }

        return "";
    }
}
