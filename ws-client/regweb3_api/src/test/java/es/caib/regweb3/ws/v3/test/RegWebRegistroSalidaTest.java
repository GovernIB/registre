package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by earrivi on 4/11/14.
 */
public class RegWebRegistroSalidaTest extends RegWebTestUtils{

    protected static RegWebRegistroSalidaWs registroSalidaApi;

    /**
     * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        //setEntorno("_localhost_PRO");
        setEntorno("_localhost");
        registroSalidaApi = getRegistroSalidaApi();
    }

    //@Test
    public void testVersio() throws Exception {
        String version = registroSalidaApi.getVersion();
        if (version.indexOf('-') != -1) {
            Assert.assertEquals("3.0.0-caib", version);
        } else {
            Assert.assertEquals("3.0.0", version);
        }
    }

    //@Test
    public void testVersioWs() throws Exception {
        Assert.assertEquals(3, registroSalidaApi.getVersionWs());
    }

    @Test
    public void obtenerRegistroSalida() {

        try {
            RegistroSalidaResponseWs registroSalidaWs = registroSalidaApi.obtenerRegistroSalida("GPRO-S-6/2022", "earrivi",getTestEntidadCodigoDir3());
            System.out.printf("Idioma: " + registroSalidaWs.getIdiomaCodigo() + "\n");
            System.out.printf("Idioma: " + registroSalidaWs.getIdiomaDescripcion() + "\n");
            System.out.printf("CodigoAsunto: " + registroSalidaWs.getCodigoAsuntoCodigo() + "\n");
            System.out.printf("CodigoAsunto desc: " + registroSalidaWs.getCodigoAsuntoDescripcion() + "\n");
            System.out.printf("TipoAsunto: " + registroSalidaWs.getTipoAsuntoCodigo() + "\n");
            System.out.printf("TipoAsunto: " + registroSalidaWs.getTipoAsuntoDescripcion() + "\n");
            System.out.printf("TipoTransporte: " + registroSalidaWs.getTipoTransporteCodigo() + "\n");
            System.out.printf("TipoTransporte: " + registroSalidaWs.getTipoTransporteDescripcion() + "\n");
            System.out.printf("Oficina: " + registroSalidaWs.getOficinaCodigo() + " - " + registroSalidaWs.getOficinaDenominacion() +"\n");
            System.out.printf("Origen: " + registroSalidaWs.getOrigenCodigo() + " - " + registroSalidaWs.getOrigenDenominacion()+ "\n");
            System.out.printf("----\n");

            for (InteresadoWs interesadoWs : registroSalidaWs.getInteresados()) {
                System.out.println(interesadoWs.getInteresado().getNombre() + " " + interesadoWs.getInteresado().getApellido1()+ " " + interesadoWs.getInteresado().getApellido2());

            }

            for (AnexoWs anexoWs : registroSalidaWs.getAnexos()) {
                System.out.println(anexoWs.getTitulo() + " " + anexoWs.getTipoDocumental());

            }

        } catch (WsI18NException e) {
            e.printStackTrace();
        } catch (WsValidationException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void anularRegistroSalida() {

      /*  try {
            registroSalidaApi.anularRegistroSalida("ADMP-E-2/2014",  "earrivi","A04006741", true);
            
            registroSalidaApi.anularRegistroSalida("ADMP-E-2/2014",  "earrivi","A04006741", false);


        } catch (WsI18NException e) {
            e.printStackTrace();
        } catch (WsValidationException e) {
            e.printStackTrace();
        }*/
    }

    @Test
    public void obtenerJustificante() throws Exception{

        JustificanteWs justificanteWs = registroSalidaApi.obtenerJustificante(getTestEntidadCodigoDir3(),"SALU-S-16/2018");

        System.out.println("Justificante: " + justificanteWs.getJustificante().length);
    }

    @Test
    public void crearRegistroSalida() throws Exception{

        for(int i=0;i<1;i++){


        RegistroSalidaWs registroSalidaWs = new RegistroSalidaWs();

        registroSalidaWs.setOrigen(getTestOrigenCodigoDir3());
        registroSalidaWs.setOficina(getTestOficinaOrigenCodigoDir3());
        registroSalidaWs.setLibro(getTestDestinoLibro());

        registroSalidaWs.setExtracto(System.currentTimeMillis() + " probando ws");
        registroSalidaWs.setDocFisica((long) 1);
        registroSalidaWs.setIdioma("es");
        //registroSalidaWs.setTipoAsunto(getTestTipoAsunto());
        registroSalidaWs.setTipoAsunto(null);
        registroSalidaWs.setCodigoAsunto("23");

        registroSalidaWs.setAplicacion("WsTest");
        registroSalidaWs.setVersion("1");

        registroSalidaWs.setCodigoUsuario(getTestUserName());
        registroSalidaWs.setContactoUsuario("earrivi@fundaciobit.org");

        registroSalidaWs.setNumExpediente("");
        registroSalidaWs.setNumTransporte("");
        registroSalidaWs.setObservaciones("");

        registroSalidaWs.setRefExterna("");
        registroSalidaWs.setTipoTransporte("");

        registroSalidaWs.setExpone("expone");
        registroSalidaWs.setSolicita("solicita");

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();

            DatosInteresadoWs interesado = new DatosInteresadoWs();
            interesado.setTipoInteresado(TIPO_INTERESADO_ADMINISTRACION);
            interesado.setTipoDocumentoIdentificacion("O");
            interesado.setDocumento("L01181113");
            interesado.setRazonSocial("Ayuntamiento de Jun");
            interesadoWs.setInteresado(interesado);

        /*DatosInteresadoWs interesado = new DatosInteresadoWs();
        interesado.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA);
        interesado.setTipoDocumentoIdentificacion("N");
        interesado.setDocumento("00000001R");
        interesado.setEmail("pgarcia@gmail.com");
        interesado.setNombre("Pepito");
        interesado.setApellido1("Garcia");
        interesado.setPais((long) 724);
        interesado.setProvincia((long) 46);
        interesadoWs.setInteresado(interesado);

        DatosInteresadoWs representante = new DatosInteresadoWs();
        representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA); // == 3
        representante.setTipoDocumentoIdentificacion("N");
        representante.setDocumento("33456299Q");
        representante.setEmail("jdelatorre@gmail.com");
        representante.setNombre("Juanito");
        representante.setApellido1("De la torre");
        representante.setPais((long) 724);
        representante.setProvincia((long) 46);
        interesadoWs.setRepresentante(representante);*/

        registroSalidaWs.getInteresados().add(interesadoWs);

        /*InteresadoWs interesadoWs2 = new InteresadoWs();
        DatosInteresadoWs organismo = new DatosInteresadoWs();
        organismo.setTipoInteresado((long)1);
        organismo.setNombre("Presidencia Govern de les Illes Balears");
        interesadoWs2.setInteresado(organismo);

        registroSalidaWs.getInteresados().add(interesadoWs2);*/



        try {
            //IdentificadorWs identificadorWs = registroSalidaApi.altaRegistroSalida(registroSalidaWs);
            IdentificadorWs identificadorWs = registroSalidaApi.nuevoRegistroSalida(getTestEntidadCodigoDir3(),registroSalidaWs);
            System.out.println("NumeroSalida: " + identificadorWs.getNumero());
            System.out.println("NumeroSalidaFormt: " + identificadorWs.getNumeroRegistroFormateado());
            System.out.println("Fecha: " + identificadorWs.getFecha());
        } catch (WsI18NException e) {
            String msg = WsClientUtils.toString(e);
            System.out.println("Error WsI18NException: " + msg);
            throw e;
        } catch (WsValidationException e) {
            String msg = WsClientUtils.toString(e);
            System.out.println("Error WsValidationException: " + msg);
            throw e;
        }

    }}


    @Test
    public void crearRegistroSalida2() {
        try {

            for (int i = 0; i < 1; i++) {
                RegistroSalidaWs registroSalidaWs = getRegistroSalida_to_PersonaFisica(false);
                IdentificadorWs identificadorWs = registroSalidaApi.nuevoRegistroSalida(getTestEntidadCodigoDir3(), registroSalidaWs);

                printIdentificadorWSBasico(identificadorWs);
            }


        } catch (WsI18NException e) {
            String msg = WsClientUtils.toString(e);
            System.out.println("Error WsI18NException: " + msg);
        } catch (WsValidationException e) {
            String msg = WsClientUtils.toString(e);
            System.out.println("Error WsValidationException: " + msg);
        }
    }
}
