package es.caib.regweb.ws.v3.test;

import es.caib.regweb.ws.api.v3.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by earrivi on 4/11/14.
 */
public class RegWebRegistroEntradaTest extends RegWebTestUtils{

    protected static RegWebRegistroEntradaWs registroEntradaApi;

    /**
     * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        registroEntradaApi = getRegistroEntradaApi();
    }

    //@Test
    public void testVersio() throws Exception {
        String version = registroEntradaApi.getVersion();
        if (version.indexOf('-') != -1) {
            Assert.assertEquals("3.0.0-caib", version);
        } else {
            Assert.assertEquals("3.0.0", version);
        }
    }

    //@Test
    public void testVersioWs() throws Exception {
        Assert.assertEquals(3, registroEntradaApi.getVersionWs());
    }

    @Test
    public void obtenerRegistroEntrada() {

        try {
            RegistroEntradaWs registroEntradaWs = registroEntradaApi.obtenerRegistroEntrada("SACO-E-1/2015", "mgonzalez","A04006741");
            System.out.printf("Destino: " + registroEntradaWs.getDestino());

            for (InteresadoWs interesadoWs : registroEntradaWs.getInteresados()) {
                System.out.println(interesadoWs.getInteresado().getNombre() + " " + interesadoWs.getInteresado().getApellido1()+ " " + interesadoWs.getInteresado().getApellido2());

            }

            for (AnexoWs anexoWs : registroEntradaWs.getAnexos()) {
                System.out.println(anexoWs.getTitulo() + " " + anexoWs.getTipoDocumental());

            }

        } catch (WsI18NException e) {
            e.printStackTrace();
        } catch (WsValidationException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void tramitarRegistroEntrada() {

        try {
            registroEntradaApi.tramitarRegistroEntrada("ADMP-E-11/2014", "earrivi","A04006741");

        } catch (WsI18NException e) {
            e.printStackTrace();
        } catch (WsValidationException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void anularRegistroEntrada() {

        try {
            registroEntradaApi.anularRegistroEntrada("ADMP-E-2/2014", "earrivi", "A04006741", true);
            
            registroEntradaApi.anularRegistroEntrada("ADMP-E-2/2014", "earrivi", "A04006741", false);

        } catch (WsI18NException e) {
            e.printStackTrace();
        } catch (WsValidationException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void crearRegistroEntrada() {

        for(int i=0;i<10;i++){


        RegistroEntradaWs registroEntradaWs = new RegistroEntradaWs();

        registroEntradaWs.setDestino("A04008544");
        registroEntradaWs.setOficina("O00015972");
        registroEntradaWs.setLibro("ADMP");

        registroEntradaWs.setExtracto("earrivi-concurrencia");
        registroEntradaWs.setDocFisica((long) 1);
        registroEntradaWs.setIdioma("es");
        registroEntradaWs.setTipoAsunto("A01");

        registroEntradaWs.setAplicacion("WsTest");
        registroEntradaWs.setVersion("1");

        registroEntradaWs.setCodigoUsuario("earrivi");
        registroEntradaWs.setContactoUsuario("earrivi@gmail.com");

        registroEntradaWs.setNumExpediente("");
        registroEntradaWs.setNumTransporte("");
        registroEntradaWs.setObservaciones("");

        registroEntradaWs.setRefExterna("");
        registroEntradaWs.setCodigoAsunto(null);
        registroEntradaWs.setTipoTransporte("");

        registroEntradaWs.setExpone("");
        registroEntradaWs.setSolicita("");

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();

        DatosInteresadoWs interesado = new DatosInteresadoWs();
        interesado.setTipoInteresado((long)2);
        interesado.setNombre("Gerardo");
        interesado.setApellido1("Martinez");
        interesadoWs.setInteresado(interesado);

        DatosInteresadoWs representante = new DatosInteresadoWs();
        representante.setTipoInteresado((long)3);
        representante.setRazonSocial("Endesa");
        interesadoWs.setRepresentante(representante);

        registroEntradaWs.getInteresados().add(interesadoWs);

        InteresadoWs interesadoWs2 = new InteresadoWs();
        DatosInteresadoWs organismo = new DatosInteresadoWs();
        organismo.setTipoInteresado((long)1);
        organismo.setNombre("Presidencia Govern de les Illes Balears");
        interesadoWs2.setInteresado(organismo);

        registroEntradaWs.getInteresados().add(interesadoWs2);



        // Anexos
      /*  String fichero = "SENZA.pdf";
        String sfirma = "SpringMVC.pdf";
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

        registroEntradaWs.getAnexos().add(anexoWs);   */

        /* ANEXO CON FIRMA ATACHED   */
     /*   AnexoWs anexoFirmaAtached = new AnexoWs();

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

        registroEntradaWs.getAnexos().add(anexoFirmaAtached);  */
         /* FIN ANEXO CON FIRMA ATACHED   */

        /** ANEXO CON FIRMA DETACHED */
      /*  AnexoWs anexoFirmaDetached = new AnexoWs();

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
        registroEntradaWs.getAnexos().add(anexoFirmaDetached);   */
        /** FIN ANEXO CON FIRMA DETACHED */

        /** ANEXO CON FIRMA DETACHED COPIA */
    /*    AnexoWs anexoFirmaDetachedCopia = new AnexoWs();

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
        registroEntradaWs.getAnexos().add(anexoFirmaDetachedCopia); */
        /** FIN ANEXO CON FIRMA DETACHED */



        try {
            IdentificadorWs identificadorWs = registroEntradaApi.altaRegistroEntrada(registroEntradaWs);
            System.out.println("NumeroEntrada: " + identificadorWs.getNumero());
            System.out.println("Fecha: " + identificadorWs.getFecha());
        } catch (WsI18NException e) {
            e.printStackTrace();
        } catch (WsValidationException e) {
            e.printStackTrace();
        }

        }

    }
}
