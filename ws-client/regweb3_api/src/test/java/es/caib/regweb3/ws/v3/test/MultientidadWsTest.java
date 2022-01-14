package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.ws.api.v3.AsientoRegistralWs;
import es.caib.regweb3.ws.api.v3.RegWebAsientoRegistralWs;
import es.caib.regweb3.ws.api.v3.WsI18NException;
import es.caib.regweb3.ws.api.v3.WsValidationException;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Fundació BIT.
 *
 *
 * Tests para verificar la funcionalidad de la multientidad
 *
 *
 * @author mgonzalez
 * Date: 09/07/2021
 */

public class MultientidadWsTest  extends RegWebTestUtils {

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


    /* REGISTROS DE ENTRADA */


   /**
    * Test que crea un asiento registral de entrada al que se le especifica un destino
    *
    * @throws Exception
    */
   public void crearAsientoEntradaDestino(String destino) throws Exception {

      try {
         AsientoRegistralWs asientoRegistralWs = getAsiento_to_PersonaFisicaDestino(REGISTRO_ENTRADA, false, false, destino);
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


   /**
    * Método que crea varios registros de entrada asignandoles el destino pertinente
    *
    * @throws Exception
    */
   @Test
   public void crearAsientosEntradaVariosDestinosPRO() throws Exception {

      crearAsientoEntradaDestino(getDestinoInterno("PRO"));
      crearAsientoEntradaDestino(getDestinoSirMultientidad("PRO"));
      crearAsientoEntradaDestino(getDestinoNoSirMultientidad("PRO"));
      crearAsientoEntradaDestino(getDestinoExternoSir("PRO"));
      crearAsientoEntradaDestino(getDestinoExternoNoSir("PRO"));
   }


   /**
    * Método que crea varios registros de entrada asignandoles el destino pertinente
    *
    * @throws Exception
    */
   @Test
   public void crearAsientosEntradaVariosDestinosPRE() throws Exception {

      crearAsientoEntradaDestino(getDestinoInterno("PRE"));
      crearAsientoEntradaDestino(getDestinoSirMultientidad("PRE"));
      crearAsientoEntradaDestino(getDestinoExternoSir("PRE"));
      crearAsientoEntradaDestino(getDestinoExternoNoSir("PRE"));
   }


   /* REGISTROS DE SALIDA */

   /**
    * Test que crea un asiento registral de salida con el destinatario un organismo que está repetido por ser entorno multientidad Ex: ATIB (PRO MADRID)
    * @throws Exception
    */
   @Test
   public void crearAsientoSalidaMultiEntidadSIRPRO() throws Exception {

         try {

            AsientoRegistralWs asientoRegistralWs = getAsiento_to_Administracion(REGISTRO_SALIDA, false, "Agencia Tributaria Islas Baleares (ATIB)", "A04013587");
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

   /* REGISTROS DE SALIDA */

   /**
    * Test que crea un asiento registral de salida con el destinatario un organismo que está repetido por ser entorno multientidad Ex: ATIB (PRE MADRID)
    * @throws Exception
    */
   @Test
   public void crearAsientoSalidaMultiEntidadSIRPRE() throws Exception {

      try {

         AsientoRegistralWs asientoRegistralWs = getAsiento_to_Administracion(REGISTRO_SALIDA, false, "Agència Tributària de Les Illes Balears", "A04032198");
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


   /**
    * Test que crea un asiento registral de salida con el destinatario un organismo que está repetido por ser entorno multientidad pero no está activo en SIR Ex: Delegacion ATIB Ibiza
    * @throws Exception
    */
   @Test
   public void crearAsientoSalidaMultiEntidadNoSIRPRO() throws Exception {


         try {

            AsientoRegistralWs asientoRegistralWs = getAsiento_to_Administracion(REGISTRO_SALIDA, false, "Delegación de la Atib de Ibiza", "A04019927");
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

   /**
    * Test que crea un asiento registral de salida con el destinatario un organismo de la misma entidad en la que estamos
    * @throws Exception
    */
   @Test
   public void crearAsientoSalidaInternoPRO() throws Exception {


         try {

            AsientoRegistralWs asientoRegistralWs = getAsiento_to_Administracion(REGISTRO_SALIDA, false, "Consejería de Presidencia, Función Pública e Igualdad", "A04027007");

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

   /**
    * Test que crea un asiento registral de salida con el destinatario un organismo de la misma entidad en la que estamos
    * @throws Exception
    */
   @Test
   public void crearAsientoSalidaInternoPRE() throws Exception {


      try {

         AsientoRegistralWs asientoRegistralWs = getAsiento_to_Administracion(REGISTRO_SALIDA, false, "Servei D'Ocupació de Les Illes Balears", "A04031290");

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

   /**
    * Test que crea un asiento registral de salida con el destinatario un organismo externo que tiene SIR Ex: Aj de Jun
    * @throws Exception
    */
   @Test
   public void crearAsientoSalidaSir() throws Exception {


         try {
            AsientoRegistralWs asientoRegistralWs = getAsiento_to_AdministracionSir(REGISTRO_SALIDA, false);
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

   /**
    * Test que crea un asiento registral de salida con el destinatario un organismo que es externo y no tiene SIR(Ex: Ajuntament d'Escorca)
    * @throws Exception
    */

   @Test
   public void crearAsientoSalidaExterno() throws Exception {

         try {

            AsientoRegistralWs asientoRegistralWs = getAsiento_to_AdministracionExterna(REGISTRO_SALIDA, false);
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


   @Test
   public void crearAsientoSalidaComunicacionMultiEntidadSIRPRO() throws Exception {

      for (int i = 0; i < 1; i++) {

         try {

            AsientoRegistralWs asientoRegistralWs = getAsiento_to_Administracion(REGISTRO_SALIDA, false, "Agencia Tributaria Islas Baleares (ATIB)", "A04013587");
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
   public void crearAsientoSalidaComunicacionMultiEntidadNoSIRPRO() throws Exception {

      for (int i = 0; i < 1; i++) {

         try {

            AsientoRegistralWs asientoRegistralWs = getAsiento_to_Administracion(REGISTRO_SALIDA, false, "Delegación de la Atib de Ibiza", "A04019927");
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
   public void crearAsientoSalidaComunicacionExterno() throws Exception {

      for (int i = 0; i < 1; i++) {

         try {

            AsientoRegistralWs asientoRegistralWs = getAsiento_to_AdministracionExterna(REGISTRO_SALIDA, false);
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

}
