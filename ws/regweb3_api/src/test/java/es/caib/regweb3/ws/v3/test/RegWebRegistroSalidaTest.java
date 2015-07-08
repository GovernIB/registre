package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.ws.api.v3.*;
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
            RegistroSalidaResponseWs registroSalidaWs = registroSalidaApi.obtenerRegistroSalida("PRES-S-2/2015", "mgonzalez","EA0004518");
            System.out.printf("Idioma: " + registroSalidaWs.getIdioma() + "\n");
            System.out.printf("TipoAsunto: " + registroSalidaWs.getTipoAsunto() + "\n");
            System.out.printf("TipoTransporte: " + registroSalidaWs.getTipoTransporte() + "\n");
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

        try {
            registroSalidaApi.anularRegistroSalida("ADMP-E-2/2014", "earrivi", "A04006741", true);
            
            registroSalidaApi.anularRegistroSalida("ADMP-E-2/2014", "earrivi", "A04006741", false);

        } catch (WsI18NException e) {
            e.printStackTrace();
        } catch (WsValidationException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void crearRegistroSalida() {

        for(int i=0;i<10;i++){


        RegistroSalidaWs registroSalidaWs = new RegistroSalidaWs();

        registroSalidaWs.setOrigen("A04008542");
        registroSalidaWs.setOficina("O00015972");
        registroSalidaWs.setLibro("ADMP");

        registroSalidaWs.setExtracto("earrivi-concurrencia");
        registroSalidaWs.setDocFisica((long) 1);
        registroSalidaWs.setIdioma("gl");
        registroSalidaWs.setTipoAsunto("A01");

        registroSalidaWs.setAplicacion("WsTest");
        registroSalidaWs.setVersion("1");

        registroSalidaWs.setCodigoUsuario("earrivi");
        registroSalidaWs.setContactoUsuario("earrivi@gmail.com");

        registroSalidaWs.setNumExpediente("");
        registroSalidaWs.setNumTransporte("");
        registroSalidaWs.setObservaciones("");

        registroSalidaWs.setRefExterna("");
        registroSalidaWs.setCodigoAsunto(null);
        registroSalidaWs.setTipoTransporte("");

        registroSalidaWs.setExpone("");
        registroSalidaWs.setSolicita("");

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();

        DatosInteresadoWs interesado = new DatosInteresadoWs();
        interesado.setTipoInteresado((long)2);
        interesado.setNombre("Lucas");
        interesado.setApellido1("Martinez");
        interesadoWs.setInteresado(interesado);

        DatosInteresadoWs representante = new DatosInteresadoWs();
        representante.setTipoInteresado((long)3);
        representante.setRazonSocial("Endesa");
        interesadoWs.setRepresentante(representante);

        registroSalidaWs.getInteresados().add(interesadoWs);

        InteresadoWs interesadoWs2 = new InteresadoWs();
        DatosInteresadoWs organismo = new DatosInteresadoWs();
        organismo.setTipoInteresado((long)1);
        organismo.setNombre("Presidencia Govern de les Illes Balears");
        interesadoWs2.setInteresado(organismo);

        registroSalidaWs.getInteresados().add(interesadoWs2);

/*
        // Anexos
        String fichero = "ITM_Formulario de solicitud de examen_MARILENGONZALEZ.doc";
        String sfirma = "ITM_Formulario de solicitud de examen.doc";
        // Anexo sin firma
        AnexoWs anexoWs = new AnexoWs();

        anexoWs.setTitulo("Primer Anexo via WS");
        anexoWs.setValidezDocumento("01");
        anexoWs.setTipoDocumental("TD01");
        anexoWs.setTipoDocumento("01");
        anexoWs.setOrigenCiudadanoAdmin(new Integer(0));
        anexoWs.setObservaciones("Observaciones de Marilen");

        anexoWs.setModoFirma(0);
        // Fichero Anexado
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        try{


           File file = new File(getTestArchivosPath()+fichero);
           System.out.println(file.exists());
           System.out.println(file.getAbsolutePath());

           anexoWs.setFicheroAnexado(FileUtils.readFileToByteArray(file));
           anexoWs.setNombreFicheroAnexado(file.getName());
           anexoWs.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
           anexoWs.setTamanoFicheroAnexado(file.length());

        }catch (Exception e){
            e.printStackTrace();
        }

        GregorianCalendar gc = (GregorianCalendar)GregorianCalendar.getInstance();
        gc.setTimeInMillis(new Date().getTime());
        anexoWs.setFechaCaptura(new XMLGregorianCalendarImpl(gc));

        registroSalidaWs.getAnexos().add(anexoWs);

        *//* ANEXO CON FIRMA ATACHED   *//*
        AnexoWs anexoFirmaAtached = new AnexoWs();

        anexoFirmaAtached.setTitulo("Anexo firma atached");
        anexoFirmaAtached.setValidezDocumento("02");
        anexoFirmaAtached.setTipoDocumental("TD02");
        anexoFirmaAtached.setTipoDocumento("02");
        anexoFirmaAtached.setOrigenCiudadanoAdmin(new Integer(1));
        anexoFirmaAtached.setObservaciones("Observaciones firma atached");
        anexoFirmaAtached.setModoFirma(1);
        // Fichero Anexado

        try{
           File file = new File(getTestArchivosPath()+fichero);

           anexoFirmaAtached.setFicheroAnexado(FileUtils.readFileToByteArray(file));
           anexoFirmaAtached.setNombreFicheroAnexado(file.getName());
           anexoFirmaAtached.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
           anexoFirmaAtached.setTamanoFicheroAnexado(file.length());

        }catch (Exception e){
            e.printStackTrace();
        }


        gc.setTimeInMillis(new Date().getTime());
        anexoFirmaAtached.setFechaCaptura(new XMLGregorianCalendarImpl(gc));

        registroSalidaWs.getAnexos().add(anexoFirmaAtached);
         *//* FIN ANEXO CON FIRMA ATACHED   *//*

        *//** ANEXO CON FIRMA DETACHED *//*
        AnexoWs anexoFirmaDetached = new AnexoWs();

        anexoFirmaDetached.setTitulo("Anexo firma detached");
        anexoFirmaDetached.setValidezDocumento("03");
        anexoFirmaDetached.setTipoDocumental("TD03");
        anexoFirmaDetached.setTipoDocumento("03");
        anexoFirmaDetached.setOrigenCiudadanoAdmin(new Integer(0));
        anexoFirmaDetached.setObservaciones("Observaciones firma detached");
        anexoFirmaDetached.setModoFirma(2);
        // Fichero Anexado

        try{
           //archivo
           File file = new File(getTestArchivosPath()+ fichero);

           anexoFirmaDetached.setFicheroAnexado(FileUtils.readFileToByteArray(file));
           anexoFirmaDetached.setNombreFicheroAnexado(file.getName());
           anexoFirmaDetached.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
           anexoFirmaDetached.setTamanoFicheroAnexado(file.length());

           //firma
           File firma = new File(getTestArchivosPath()+ sfirma);

           anexoFirmaDetached.setFirmaAnexada(FileUtils.readFileToByteArray(firma));
           anexoFirmaDetached.setNombreFirmaAnexada(firma.getName());
           anexoFirmaDetached.setTipoMIMEFirmaAnexada(mimeTypesMap.getContentType(firma));
           anexoFirmaDetached.setTamanoFirmaAnexada(firma.length());

        }catch (Exception e){
            e.printStackTrace();
        }


        gc.setTimeInMillis(new Date().getTime());
        anexoFirmaDetached.setFechaCaptura(new XMLGregorianCalendarImpl(gc));
        registroSalidaWs.getAnexos().add(anexoFirmaDetached);
        *//** FIN ANEXO CON FIRMA DETACHED *//*

        *//** ANEXO CON FIRMA DETACHED COPIA *//*
        AnexoWs anexoFirmaDetachedCopia = new AnexoWs();

        anexoFirmaDetachedCopia.setTitulo("Anexo firma detached copia");
        anexoFirmaDetachedCopia.setValidezDocumento("01");
        anexoFirmaDetachedCopia.setTipoDocumental("TD03");
        anexoFirmaDetachedCopia.setTipoDocumento("01");
        anexoFirmaDetachedCopia.setOrigenCiudadanoAdmin(new Integer(0));
        anexoFirmaDetachedCopia.setObservaciones("Observaciones firma detached copia");
        anexoFirmaDetachedCopia.setModoFirma(2);
        // Fichero Anexado

        try{
           //archivo
           File file = new File(getTestArchivosPath()+fichero);

           anexoFirmaDetachedCopia.setFicheroAnexado(FileUtils.readFileToByteArray(file));
           anexoFirmaDetachedCopia.setNombreFicheroAnexado(file.getName());
           anexoFirmaDetachedCopia.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
           anexoFirmaDetachedCopia.setTamanoFicheroAnexado(file.length());

           //firma
           File firma = new File(getTestArchivosPath()+sfirma);

           anexoFirmaDetachedCopia.setFirmaAnexada(FileUtils.readFileToByteArray(firma));
           anexoFirmaDetachedCopia.setNombreFirmaAnexada(firma.getName());
           anexoFirmaDetachedCopia.setTipoMIMEFirmaAnexada(mimeTypesMap.getContentType(firma));
           anexoFirmaDetachedCopia.setTamanoFirmaAnexada(firma.length());

        }catch (Exception e){
            e.printStackTrace();
        }


        gc.setTimeInMillis(new Date().getTime());
        anexoFirmaDetachedCopia.setFechaCaptura(new XMLGregorianCalendarImpl(gc));
        registroSalidaWs.getAnexos().add(anexoFirmaDetachedCopia);
        *//** FIN ANEXO CON FIRMA DETACHED *//*

        *//* Probando VALIDADOR *//*
        AnexoWs anexoWsInvalido = new AnexoWs();

        anexoWsInvalido.setTitulo("");
        anexoWsInvalido.setValidezDocumento("01");
       // anexoWsInvalido.setTipoDocumental("TD01");
       // anexoWsInvalido.setTipoDocumento("01");
        anexoWsInvalido.setOrigenCiudadanoAdmin(new Integer(-1));
        anexoWsInvalido.setObservaciones("Observaciones de Marilen Invalido");

        anexoWsInvalido.setModoFirma(0);
        // Fichero Anexado

        try{


           File file = new File(getTestArchivosPath()+fichero);
           System.out.println(file.exists());
           System.out.println(file.getAbsolutePath());

           anexoWsInvalido.setFicheroAnexado(FileUtils.readFileToByteArray(file));
           anexoWsInvalido.setNombreFicheroAnexado(file.getName());
           anexoWsInvalido.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
           anexoWsInvalido.setTamanoFicheroAnexado(file.length());

        }catch (Exception e){
            e.printStackTrace();
        }

        gc.setTimeInMillis(new Date().getTime());
        anexoWsInvalido.setFechaCaptura(new XMLGregorianCalendarImpl(gc));

        registroSalidaWs.getAnexos().add(anexoWsInvalido);*/


        try {
            IdentificadorWs identificadorWs = registroSalidaApi.altaRegistroSalida(registroSalidaWs);
            System.out.println("NumeroSalida: " + identificadorWs.getNumero());
            System.out.println("Fecha: " + identificadorWs.getFecha());
        } catch (WsI18NException e) {
            e.printStackTrace();
        } catch (WsValidationException e) {
            e.printStackTrace();
        }
        }

    }
}
