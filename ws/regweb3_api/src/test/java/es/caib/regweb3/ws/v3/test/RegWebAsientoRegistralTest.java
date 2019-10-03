package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
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
    public void crearAsientoRegistral() throws Exception {

        for (int i = 0; i < 1; i++) {

            AsientoRegistralWs asientoRegistralWs = new AsientoRegistralWs();
            asientoRegistralWs.setTipoRegistro(REGISTRO_ENTRADA);

            asientoRegistralWs.setAplicacion("REGWEB3");
            asientoRegistralWs.setAplicacionTelematica("REGWEB3");
            asientoRegistralWs.setCodigoAsunto(null);
            asientoRegistralWs.setCodigoSia(getTestCodigoSia());
            asientoRegistralWs.setCodigoUsuario(getTestUserName());
            asientoRegistralWs.setEntidadCodigo(getTestEntidadCodigoDir3());

            asientoRegistralWs.setEntidadRegistralOrigenCodigo(getTestOficinaOrigenCodigoDir3());
            asientoRegistralWs.setExpone("Expone");
            asientoRegistralWs.setSolicita("Solicita");
            asientoRegistralWs.setIdioma(RegwebConstantes.IDIOMA_CATALAN_ID);
            asientoRegistralWs.setLibroCodigo(getTestDestinoLibro());
            asientoRegistralWs.setPresencial(false);
            asientoRegistralWs.setResumen("Registro test Ws");
            asientoRegistralWs.setUnidadTramitacionOrigenCodigo(getTestOrigenCodigoDir3());
            asientoRegistralWs.setUnidadTramitacionDestinoCodigo(getTestDestinoCodigoDir3());
            asientoRegistralWs.setTipoDocumentacionFisicaCodigo(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);
            //asientoRegistralWs.setTipoAsunto(getTestTipoAsunto());


            // Interesados
            InteresadoWs interesadoWs = new InteresadoWs();

            DatosInteresadoWs interesado = new DatosInteresadoWs();
            interesado.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA);
            interesado.setTipoDocumentoIdentificacion("N");
            interesado.setDocumento("43146650F");
            interesado.setEmail("pgarcia@gmail.com");
            interesado.setNombre("Julian");
            interesado.setApellido1("González");
            interesado.setCanal((long) 1);
            interesado.setDireccion("Avenida picasso");
            interesado.setLocalidad((long) 407);
            interesado.setPais((long) 724);
            interesado.setProvincia((long) 7);
            interesadoWs.setInteresado(interesado);

            /*DatosInteresadoWs representante = new DatosInteresadoWs();
            representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA); // == 3
            representante.setTipoDocumentoIdentificacion("N");
            representante.setDocumento("33456299Q");
            representante.setEmail("jdelatorre@gmail.com");
            representante.setNombre("Juanito");
            representante.setApellido1("De la torre");
            representante.setPais((long) 724);
            representante.setProvincia((long) 46);
            interesadoWs.setRepresentante(representante);*/

            asientoRegistralWs.getInteresados().add(interesadoWs);

             InteresadoWs interesadoWs2 = new InteresadoWs();
            DatosInteresadoWs organismo = new DatosInteresadoWs();
            organismo.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
            organismo.setTipoDocumentoIdentificacion("O");
            organismo.setRazonSocial("Ayuntamiento de Alaior");
            organismo.setDocumento("L01070027");
            interesadoWs2.setInteresado(organismo);

           // asientoRegistralWs.getInteresados().add(interesadoWs2);

            //asientoRegistralWs.getAnexos().addAll(getAnexos());

            try {
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(getTestEntidadCodigoDir3(),asientoRegistralWs,null,true);

                //asientoRegistralApi.distribuirAsientoRegistral(getTestEntidadCodigoDir3(),asientoRegistralWs.getNumeroRegistroFormateado());
                System.out.println("NumeroEntrada: " + asientoRegistralWs.getNumeroRegistroFormateado());
                System.out.println("Fecha: " + asientoRegistralWs.getFechaRegistro());
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
    public void crearAsientoRegistralSalida() throws Exception {

        for (int i = 0; i < 1; i++) {

            AsientoRegistralWs asientoRegistralWs = new AsientoRegistralWs();
            asientoRegistralWs.setTipoRegistro(REGISTRO_SALIDA);

            asientoRegistralWs.setAplicacion("REGWEB3");
            asientoRegistralWs.setAplicacionTelematica("REGWEB3");
            asientoRegistralWs.setCodigoAsunto(null);
            asientoRegistralWs.setCodigoSia(getTestCodigoSia());
            asientoRegistralWs.setCodigoUsuario(getTestUserName());
            asientoRegistralWs.setEntidadCodigo(getTestEntidadCodigoDir3());

            asientoRegistralWs.setEntidadRegistralOrigenCodigo(getTestOficinaOrigenCodigoDir3());
            asientoRegistralWs.setExpone("Expone");
            asientoRegistralWs.setSolicita("Solicita");
            asientoRegistralWs.setIdioma(RegwebConstantes.IDIOMA_CATALAN_ID);
            asientoRegistralWs.setLibroCodigo(getTestDestinoLibro());
            asientoRegistralWs.setPresencial(false);
            asientoRegistralWs.setResumen("Registro test Ws");
            asientoRegistralWs.setUnidadTramitacionOrigenCodigo(getTestOrigenCodigoDir3());
            asientoRegistralWs.setUnidadTramitacionDestinoCodigo(getTestDestinoCodigoDir3());
            asientoRegistralWs.setTipoDocumentacionFisicaCodigo(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);
            //asientoRegistralWs.setTipoAsunto(getTestTipoAsunto());


            // Interesados
            InteresadoWs interesadoWs = new InteresadoWs();

            DatosInteresadoWs interesado = new DatosInteresadoWs();
            interesado.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA);
            interesado.setTipoDocumentoIdentificacion("N");
            interesado.setDocumento("43146650F");
            interesado.setEmail("pgarcia@gmail.com");
            interesado.setNombre("Julian");
            interesado.setApellido1("González");
            interesado.setCanal((long) 1);
            interesado.setDireccion("Avenida picasso");
            interesado.setLocalidad((long) 407);
            interesado.setPais((long) 724);
            interesado.setProvincia((long) 7);
            interesadoWs.setInteresado(interesado);

            /*DatosInteresadoWs representante = new DatosInteresadoWs();
            representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA); // == 3
            representante.setTipoDocumentoIdentificacion("N");
            representante.setDocumento("33456299Q");
            representante.setEmail("jdelatorre@gmail.com");
            representante.setNombre("Juanito");
            representante.setApellido1("De la torre");
            representante.setPais((long) 724);
            representante.setProvincia((long) 46);
            interesadoWs.setRepresentante(representante);*/

            asientoRegistralWs.getInteresados().add(interesadoWs);

            InteresadoWs interesadoWs2 = new InteresadoWs();
            DatosInteresadoWs organismo = new DatosInteresadoWs();
            organismo.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
            organismo.setTipoDocumentoIdentificacion("O");
            organismo.setRazonSocial("Ayuntamiento de Alaior");
            organismo.setDocumento("L01070027");
            interesadoWs2.setInteresado(organismo);

            // asientoRegistralWs.getInteresados().add(interesadoWs2);

            //asientoRegistralWs.getAnexos().addAll(getAnexos());

            try {

                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(getTestEntidadCodigoDir3(),asientoRegistralWs,RegwebConstantes.TIPO_OPERACION_NOTIFICACION,false);

                //asientoRegistralApi.distribuirAsientoRegistral(getTestEntidadCodigoDir3(),asientoRegistralWs.getNumeroRegistroFormateado());
                System.out.println("NumeroEntrada: " + asientoRegistralWs.getNumeroRegistroFormateado());
                System.out.println("Fecha: " + asientoRegistralWs.getFechaRegistro());
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

            AsientoRegistralWs asientoRegistralWs = new AsientoRegistralWs();

            asientoRegistralWs.setAplicacion("REGWEB3");
            asientoRegistralWs.setAplicacionTelematica("REGWEB3");
            asientoRegistralWs.setCodigoAsunto(null);
            asientoRegistralWs.setCodigoSia(getTestCodigoSia());
            asientoRegistralWs.setCodigoUsuario(getTestUserName());
            asientoRegistralWs.setEntidadCodigo(getTestEntidadCodigoDir3());
            asientoRegistralWs.setEntidadRegistralOrigenCodigo(getTestOficinaOrigenCodigoDir3());
            asientoRegistralWs.setExpone("Expone");
            asientoRegistralWs.setSolicita("Solicita");
            asientoRegistralWs.setIdioma(RegwebConstantes.IDIOMA_CATALAN_ID);
            asientoRegistralWs.setLibroCodigo(getTestDestinoLibro());
            asientoRegistralWs.setPresencial(false);
            asientoRegistralWs.setResumen("Prueba via RegwebAsientoRegistralTest");
            asientoRegistralWs.setUnidadTramitacionDestinoCodigo(getTestDestinoCodigoDir3());
            asientoRegistralWs.setTipoRegistro(REGISTRO_ENTRADA);
            asientoRegistralWs.setTipoDocumentacionFisicaCodigo(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);


            // Interesados
            InteresadoWs interesadoWs = new InteresadoWs();

            DatosInteresadoWs interesado = new DatosInteresadoWs();
            interesado.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA);
            interesado.setTipoDocumentoIdentificacion("N");
            interesado.setDocumento("43146650F");
            interesado.setEmail("pgarcia@gmail.com");
            interesado.setNombre("Julian");
            interesado.setApellido1("González");
            interesado.setCanal((long) 1);
            interesado.setDireccion("Avenida picasso");
            interesado.setLocalidad((long) 407);
            interesado.setPais((long) 724);
            interesado.setProvincia((long) 7);
            interesadoWs.setInteresado(interesado);

            /*DatosInteresadoWs representante = new DatosInteresadoWs();
            representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA); // == 3
            representante.setTipoDocumentoIdentificacion("N");
            representante.setDocumento("33456299Q");
            representante.setEmail("jdelatorre@gmail.com");
            representante.setNombre("Juanito");
            representante.setApellido1("De la torre");
            representante.setPais((long) 724);
            representante.setProvincia((long) 46);
            interesadoWs.setRepresentante(representante);*/

            asientoRegistralWs.getInteresados().add(interesadoWs);

            /* InteresadoWs interesadoWs2 = new InteresadoWs();
            DatosInteresadoWs organismo = new DatosInteresadoWs();
            organismo.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
            organismo.setNombre("Presidencia Govern de les Illes Balears");
            interesadoWs2.setInteresado(organismo);

            asientoRegistralWs.getInteresados().add(interesadoWs2);*/

            asientoRegistralWs.getAnexos().addAll(getAnexos());


            try {
                asientoRegistralWs = asientoRegistralApi.crearAsientoRegistral(getTestEntidadCodigoDir3(),asientoRegistralWs,null,false);
                System.out.println("NumeroEntrada: " + asientoRegistralWs.getNumeroRegistroFormateado());
                System.out.println("Fecha: " + asientoRegistralWs.getFechaRegistro());
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
            AsientoRegistralWs asientoRegistralWs = asientoRegistralApi.obtenerAsientoRegistral(getTestEntidadCodigoDir3(),"SALU-S-103/2019", RegwebConstantes.REGISTRO_SALIDA,false);

            System.out.println("Fecha Registro: " + asientoRegistralWs.getFechaRegistro());
            System.out.println("Codigo Asunto: " + asientoRegistralWs.getCodigoAsunto());
            System.out.println("Codigo Asunto " + asientoRegistralWs.getCodigoAsuntoDenominacion());
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


            System.out.println("Numero: " + asientoRegistralWs.getNumeroRegistroFormateado());
            System.out.println("Extracto: " + asientoRegistralWs.getResumen());
            System.out.println("Anexos: " + asientoRegistralWs.getAnexos().size());


        } catch (WsValidationException e) {
            e.printStackTrace();
        } catch (WsI18NException e) {
            e.printStackTrace();
        }
    }




}
