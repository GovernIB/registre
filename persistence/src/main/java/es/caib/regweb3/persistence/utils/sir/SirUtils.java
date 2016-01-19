package es.caib.regweb3.persistence.utils.sir;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.CatLocalidadLocal;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.utils.Base64;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Fundació BIT.
 * Agrupa todas las funcionalidades comunes para trabajar con SIR
 * @author earrivi
 * @author anadal (Eliminar EJB's i Component)
 */
public class SirUtils {
 
    public static final Logger log = Logger.getLogger(SirUtils.class);
    
    
    private SirUtils() {
    }

    /**
     * Transforma el xml de intercambio de SIR en un {@link es.caib.regweb3.model.PreRegistro}
     * @param sicres3
     * @return
     * @throws Exception
     */
    public static PreRegistro readFicheroIntercambioSICRES3(String sicres3,  CatPaisLocal catPaisEjb, 
        CatProvinciaLocal catProvinciaEjb, CatLocalidadLocal catLocalidadEjb) throws Exception {

        FicheroIntercambioSICRES3 fiSICRES3 = null;
        try {

            JAXBContext jc = JAXBContext.newInstance(FicheroIntercambioSICRES3.class);
            Unmarshaller u = jc.createUnmarshaller();

            //SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); // validation purpose
            //Schema schema = sf.newSchema(this.getClass().getClassLoader().getResource("xsd/SICRES3_INTERCAMBIO_APL.xsd")); // validation purpose

            //u.setSchema(schema);  // validation purpose
            //u.setEventHandler(new DefaultValidationEventHandler());
            //u.setEventHandler(new MyValidationEventHandler());

            StringReader reader = new StringReader(sicres3);
            fiSICRES3 = (FicheroIntercambioSICRES3) u.unmarshal(reader);
            log.info("fiSICRES3 getDeDestino: " + fiSICRES3.getDeDestino().toString());
            log.info("fiSICRES3 getDeAsunto: " + fiSICRES3.getDeAsunto().toString());
            log.info("fiSICRES3 getDeInternosControl: " + fiSICRES3.getDeInternosControl().toString());
            log.info("fiSICRES3 getDeFormularioGenerico: " + fiSICRES3.getDeFormularioGenerico().toString());

        }catch (UnmarshalException ue) {
            StringWriter sw = new StringWriter();
            ue.printStackTrace(new PrintWriter(sw));
            System.err.println("Error 1: " + sw.toString());
      //  }catch (SAXException se) {
        //    StringWriter sw = new StringWriter();
       //     se.printStackTrace(new PrintWriter(sw));
       //     System.err.println("Error 2: " + sw.toString());
        }catch (JAXBException je) {
            StringWriter sw = new StringWriter();
            je.printStackTrace(new PrintWriter(sw));
            System.err.println("Error 3: " + sw.toString());
        }

        PreRegistro preRegistro = crearPreregistro(fiSICRES3,  catPaisEjb, 
            catProvinciaEjb, catLocalidadEjb);

        return preRegistro;
    }

    /**
     * Transforma un Asiento registral recibido mediante SIR en un {@link es.caib.regweb3.model.PreRegistro} de REGWEB3
     * @param ficheroIntercambioSICRES3
     * @return
     * @throws Exception
     */
    private static  PreRegistro crearPreregistro(FicheroIntercambioSICRES3 ficheroIntercambioSICRES3, 
        CatPaisLocal catPaisEjb, CatProvinciaLocal catProvinciaEjb,
        CatLocalidadLocal catLocalidadEjb) throws Exception{

        FicheroIntercambioSICRES3.DeOrigenORemitente deOrigenORemitente = ficheroIntercambioSICRES3.getDeOrigenORemitente();
        FicheroIntercambioSICRES3.DeDestino deDestino = ficheroIntercambioSICRES3.getDeDestino();
        FicheroIntercambioSICRES3.DeAsunto deAsunto = ficheroIntercambioSICRES3.getDeAsunto();
        FicheroIntercambioSICRES3.DeFormularioGenerico deFormularioGenerico = ficheroIntercambioSICRES3.getDeFormularioGenerico();
        FicheroIntercambioSICRES3.DeInternosControl deInternosControl = ficheroIntercambioSICRES3.getDeInternosControl();
        
        List<FicheroIntercambioSICRES3.DeInteresado> deInteresados = ficheroIntercambioSICRES3.getDeInteresado();

        PreRegistro preRegistro = new PreRegistro();
        RegistroDetalle registroDetalle = new RegistroDetalle();
        List<Interesado> interesados = new ArrayList<Interesado>();
        List<Anexo> anexos = new ArrayList<Anexo>();

        // DeOrigenORemitente
        preRegistro.setCodigoEntidadRegistralOrigen(deOrigenORemitente.getCodigoEntidadRegistralOrigen());
        if(!StringUtils.isEmpty(deOrigenORemitente.getDecodificacionEntidadRegistralOrigen())){preRegistro.setDecodificacionEntidadRegistralOrigen(deOrigenORemitente.getDecodificacionEntidadRegistralOrigen());}
        preRegistro.setCodigoUnidadTramitacionOrigen(deOrigenORemitente.getCodigoUnidadTramitacionOrigen());
        if(!StringUtils.isEmpty(deOrigenORemitente.getDecodificacionUnidadTramitacionOrigen())){preRegistro.setDecodificacionUnidadTramitacionOrigen(deOrigenORemitente.getDecodificacionUnidadTramitacionOrigen());}

        // DeDestino
        preRegistro.setCodigoEntidadRegistralDestino(deDestino.getCodigoEntidadRegistralDestino());
        preRegistro.setDecodificacionEntidadRegistralDestino(deDestino.getDecodificacionEntidadRegistralDestino());
        preRegistro.setCodigoUnidadTramitacionDestino(deDestino.getCodigoUnidadTramitacionDestino());
        preRegistro.setDecodificacionUnidadTramitacionDestino(deDestino.getDecodificacionUnidadTramitacionDestino());

        // DeInternosControl
        preRegistro.setTipoAnotacion(RegwebConstantes.TIPO_ANOTACION_BY_CODIGO.get(deInternosControl.getTipoAnotacion()));
        preRegistro.setDescripcionTipoAnotacion(deInternosControl.getDescripcionTipoAnotacion());
        preRegistro.setTipoRegistro(deInternosControl.getTipoRegistro());
        preRegistro.setIndicadorPrueba(deInternosControl.getIndicadorPrueba());
        preRegistro.setIdIntercambio(deInternosControl.getIdentificadorIntercambio());
        if(!StringUtils.isEmpty(deInternosControl.getNombreUsuario())){preRegistro.setUsuario(deInternosControl.getNombreUsuario());}
        if(!StringUtils.isEmpty(deInternosControl.getContactoUsuario())){preRegistro.setContactoUsuario(deInternosControl.getContactoUsuario());}
        if(!StringUtils.isEmpty(deInternosControl.getTipoTransporteEntrada())) {
          registroDetalle.setTransporte(RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(deInternosControl.getTipoTransporteEntrada()));
        }
        if(!StringUtils.isEmpty(deInternosControl.getNumeroTransporteEntrada())){registroDetalle.setNumeroTransporte(deInternosControl.getNumeroTransporteEntrada());}
        if(!StringUtils.isEmpty(deInternosControl.getAplicacionVersionEmisora())){registroDetalle.setAplicacion(deInternosControl.getAplicacionVersionEmisora());}
        if(!StringUtils.isEmpty(deInternosControl.getDocumentacionFisica())){registroDetalle.setTipoDocumentacionFisica((Long.valueOf(deInternosControl.getDocumentacionFisica())));}
        if(!StringUtils.isEmpty(deInternosControl.getObservacionesApunte())){registroDetalle.setObservaciones(deInternosControl.getObservacionesApunte());}
        preRegistro.setCodigoEntidadRegistralInicio(deInternosControl.getCodigoEntidadRegistralInicio());
        if(!StringUtils.isEmpty(deInternosControl.getDecodificacionEntidadRegistralInicio())){preRegistro.setDecodificacionEntidadRegistralInicio(deInternosControl.getDecodificacionEntidadRegistralInicio());}

        // DeAsunto
        if(!StringUtils.isEmpty(deAsunto.getReferenciaExterna())){registroDetalle.setReferenciaExterna(deAsunto.getReferenciaExterna());}
        if(!StringUtils.isEmpty(deAsunto.getNumeroExpediente())){registroDetalle.setExpediente(deAsunto.getNumeroExpediente());}
        if(!StringUtils.isEmpty(deAsunto.getResumen())){registroDetalle.setExtracto(deAsunto.getResumen());}
        if(!StringUtils.isEmpty(deAsunto.getCodigoAsuntoSegunDestino())){registroDetalle.setCodigoAsunto(null);} // todo: Como mapear el CodigoAsunto

        // DeFormularioGenerico
        if(!StringUtils.isEmpty(deFormularioGenerico.getExpone())){registroDetalle.setExpone(deFormularioGenerico.getExpone());}
        if(!StringUtils.isEmpty(deFormularioGenerico.getSolicita())){registroDetalle.setSolicita(deFormularioGenerico.getSolicita());}


        // DeInteresados
        for (FicheroIntercambioSICRES3.DeInteresado deInteresado : deInteresados) {
            Interesado interesado = transformarInteresado(deInteresado,  catPaisEjb, 
                catProvinciaEjb, catLocalidadEjb);
            interesados.add(interesado);
        }

        registroDetalle.setInteresados(interesados);

        // DeAnexos TODO
        //List<FicheroIntercambioSICRES3.DeAnexo> deAnexos = ficheroIntercambioSICRES3.getDeAnexo();
        //for (FicheroIntercambioSICRES3.DeAnexo deAnexo : deAnexos) {
            //Anexo anexo = transformarAnexo(deAnexo); todo: Implementar función por Marilén
            //anexos.add(anexo);
        //}

        registroDetalle.setAnexos(anexos);

        preRegistro.setRegistroDetalle(registroDetalle);

        return preRegistro;

    }



    /**
     * Serializa un {@link es.caib.regweb3.model.RegistroEntrada} a
     * XML definido por el esquema SICRES3_INTERCAMBIO_APL.xsd
     * <p>
     * Este método realiza la validación contra el mencionado
     * esquema XSD antes de escribir la cadena XML.
     *
     * @param re el registro de entrada a serializar
     * @return Objeto FicheroIntercambioSICRES3 del registro serializado
     */
    /*
    public FicheroIntercambioSICRES3 generateFicheroIntercambioSICRES3(RegistroEntrada re)
    throws Exception {

        FicheroIntercambioSICRES3Factory objFactory = null;
        FicheroIntercambioSICRES3 fiSICRES3 = null;
        SimpleDateFormat formatoFechaOrigen = new SimpleDateFormat("yyyyMMddhhmmss");

        // try {
         

            objFactory = new FicheroIntercambioSICRES3Factory();

            fiSICRES3 = (FicheroIntercambioSICRES3) objFactory.createFicheroIntercambioSICRES3();

            String codOficinaOrigen = obtenerCodigoOficinaOrigen(re);
            String identificadorIntercambio = identificadorIntercambio(codOficinaOrigen);

			// Segmento DeOrigenORemitente 
            FicheroIntercambioSICRES3.DeOrigenORemitente deOrigenORemitente = objFactory.createFicheroIntercambioSICRES3DeOrigenORemitente();

            deOrigenORemitente.setCodigoEntidadRegistralOrigen(re.getOficina().getCodigo());
            deOrigenORemitente.setDecodificacionEntidadRegistralOrigen(re.getOficina().getDenominacion());
            deOrigenORemitente.setNumeroRegistroEntrada(re.getNumeroRegistroFormateado());
            deOrigenORemitente.setFechaHoraEntrada(formatoFechaOrigen.format(re.getFecha()));
            //deOrigenORemitente.setTimestampEntrada(); // No es necesario

            fiSICRES3.setDeOrigenORemitente(deOrigenORemitente);

            // Segmento DeDestino 
            FicheroIntercambioSICRES3.DeDestino deDestino = objFactory.createFicheroIntercambioSICRES3DeDestino();
            //deDestino.setCodigoEntidadRegistralDestino();  todo: Pendiente averiguar Oficina SIR
            //De momento la ponemos a mano  todo: Eliminar cuando se averigüe Oficina SIR
            deDestino.setCodigoEntidadRegistralDestino("A04006749");
            //deDestino.setDecodificacionEntidadRegistralDestino(); todo: Pendiente averiguar Oficina SIR
            deDestino.setCodigoUnidadTramitacionDestino(re.getDestinoExternoCodigo());
            deDestino.setDecodificacionUnidadTramitacionDestino(re.getDestinoExternoDenominacion());

            fiSICRES3.setDeDestino(deDestino);

            // Segmento DeInteresados 
            List<Interesado> interesadoList = re.getRegistroDetalle().getInteresados();
            if (interesadoList != null && !interesadoList.isEmpty()) {
                for (Interesado interesado : interesadoList) {

                    FicheroIntercambioSICRES3.DeInteresado deInteresado =  objFactory.createFicheroIntercambioSICRES3DeInteresado();

                    if(!interesado.getIsRepresentante()){ // Si no es un representante

                        if(interesado.getTipoDocumentoIdentificacion()!= null){deInteresado.setTipoDocumentoIdentificacionInteresado(interesado.getTipoDocumentoIdentificacion().getNombre());}
                        if(!StringUtils.isEmpty(interesado.getDocumento())){deInteresado.setDocumentoIdentificacionInteresado(interesado.getDocumento());}
                        if(!StringUtils.isEmpty(interesado.getRazonSocial())){deInteresado.setRazonSocialInteresado(interesado.getRazonSocial());}
                        if(!StringUtils.isEmpty(interesado.getNombre())){deInteresado.setNombreInteresado(interesado.getNombre());}
                        if(!StringUtils.isEmpty(interesado.getApellido1())){deInteresado.setPrimerApellidoInteresado(interesado.getApellido1());}
                        if(!StringUtils.isEmpty(interesado.getApellido2())){deInteresado.setSegundoApellidoInteresado(interesado.getApellido2());}
                        if(interesado.getPais()!= null){deInteresado.setPaisInteresado(interesado.getPais().getCodigoPais().toString());}
                        if(interesado.getProvincia()!= null){deInteresado.setProvinciaInteresado(interesado.getProvincia().getCodigoProvincia().toString());}
                        if(interesado.getLocalidad()!= null){deInteresado.setMunicipioInteresado(interesado.getLocalidad().getCodigoLocalidad().toString());}
                        if(!StringUtils.isEmpty(interesado.getDireccion())){deInteresado.setDireccionInteresado(interesado.getDireccion());}
                        if(!StringUtils.isEmpty(interesado.getCp())){deInteresado.setCodigoPostalInteresado(interesado.getCp());}
                        if(!StringUtils.isEmpty(interesado.getEmail())){deInteresado.setCorreoElectronicoInteresado(interesado.getEmail());}
                        if(!StringUtils.isEmpty(interesado.getTelefono())){deInteresado.setTelefonoContactoInteresado(interesado.getTelefono());}
                        if(!StringUtils.isEmpty(interesado.getDireccionElectronica())){deInteresado.setDireccionElectronicaHabilitadaInteresado(interesado.getDireccionElectronica());}
                        if(interesado.getCanal()!= null ){deInteresado.setCanalPreferenteComunicacionInteresado(interesado.getCanal().getCodigo());}

                        // Si tiene Representante
                        if(interesado.getRepresentante()!= null){
                            Interesado representante = interesado.getRepresentante();

                            if(representante.getTipoDocumentoIdentificacion()!= null){deInteresado.setTipoDocumentoIdentificacionRepresentante(representante.getTipoDocumentoIdentificacion().getNombre());}
                            if(!StringUtils.isEmpty(representante.getDocumento())){deInteresado.setDocumentoIdentificacionRepresentante(representante.getDocumento());}
                            if(!StringUtils.isEmpty(representante.getRazonSocial())){deInteresado.setRazonSocialRepresentante(representante.getRazonSocial());}
                            if(!StringUtils.isEmpty(representante.getNombre())){deInteresado.setNombreRepresentante(representante.getNombre());}
                            if(!StringUtils.isEmpty(representante.getApellido1())){deInteresado.setPrimerApellidoRepresentante(representante.getApellido1());}
                            if(!StringUtils.isEmpty(representante.getApellido2())){deInteresado.setSegundoApellidoRepresentante(representante.getApellido2());}
                            if(representante.getPais()!= null){deInteresado.setPaisRepresentante(representante.getPais().getCodigoPais().toString());}
                            if(representante.getProvincia()!= null){deInteresado.setProvinciaRepresentante(representante.getProvincia().getCodigoProvincia().toString());}
                            if(representante.getLocalidad()!= null){deInteresado.setMunicipioRepresentante(representante.getLocalidad().getCodigoLocalidad().toString());}
                            if(!StringUtils.isEmpty(representante.getDireccion())){deInteresado.setDireccionRepresentante(representante.getDireccion());}
                            if(!StringUtils.isEmpty(representante.getCp())){deInteresado.setCodigoPostalRepresentante(representante.getCp());}
                            if(!StringUtils.isEmpty(representante.getEmail())){deInteresado.setCorreoElectronicoRepresentante(representante.getEmail());}
                            if(!StringUtils.isEmpty(representante.getTelefono())){deInteresado.setTelefonoContactoRepresentante(representante.getTelefono());}
                            if(!StringUtils.isEmpty(representante.getDireccionElectronica())){deInteresado.setDireccionElectronicaHabilitadaRepresentante(representante.getDireccionElectronica());}
                            if(representante.getCanal()!= null ){deInteresado.setCanalPreferenteComunicacionRepresentante(representante.getCanal().getCodigo());}
                        }

                        if(!StringUtils.isEmpty(interesado.getObservaciones())){deInteresado.setObservaciones(interesado.getObservaciones());}


                    }

                    fiSICRES3.getDeInteresado().add(deInteresado);
                }
            }else{
                FicheroIntercambioSICRES3.DeInteresado deInteresado =  objFactory.createFicheroIntercambioSICRES3DeInteresado();
                fiSICRES3.getDeInteresado().add(deInteresado);
            }

            // Segmento DeAsunto 
            FicheroIntercambioSICRES3.DeAsunto deAsunto = objFactory.createFicheroIntercambioSICRES3DeAsunto();
            deAsunto.setResumen(re.getRegistroDetalle().getExtracto());
            if(re.getRegistroDetalle().getCodigoAsunto() != null){deAsunto.setCodigoAsuntoSegunDestino(re.getRegistroDetalle().getCodigoAsunto().getCodigo());}
            if(!StringUtils.isEmpty(re.getRegistroDetalle().getReferenciaExterna())){deAsunto.setReferenciaExterna(re.getRegistroDetalle().getReferenciaExterna());}
            if(!StringUtils.isEmpty(re.getRegistroDetalle().getExpediente())){deAsunto.setNumeroExpediente(re.getRegistroDetalle().getExpediente());}

            fiSICRES3.setDeAsunto(deAsunto);

            // Segmento DeAnexo 


            FicheroIntercambioSICRES3.DeAnexo deAnexo = objFactory.createFicheroIntercambioSICRES3DeAnexo();
            List<Anexo>  anexos = anexoEjb.getByRegistroEntrada(re.getId());

            for (Integer i = 0; i <anexos.size(); i++) {
                Anexo anexo = anexos.get(i);

                DocumentCustody documento = AnnexFileSystemManager.getArchivo(anexo.getId());

                String secuencia = "";
                switch (i.toString().length()){
                    case 1: secuencia = "000"+ i.toString();break;
                    case 2: secuencia = "00"+ i.toString();break;
                    case 3: secuencia = "00"+ i.toString();break;
                    case 4: break;

                }

                deAnexo.setNombreFicheroAnexado(anexo.getNombreFicheroAnexado());
                deAnexo.setIdentificadorFichero(identificadorIntercambio +"_"+ "01" +"_"+ secuencia +"."+ getExtension(documento.getName()));
                deAnexo.setValidezDocumento(anexo.getValidezDocumento().getCodigoSicres());
                deAnexo.setTipoDocumento("02"); //todo: Modificar en el futuro según si el documento viene de Sistra
                deAnexo.setCertificado(null);
                deAnexo.setFirmaDocumento(null);
                deAnexo.setTimeStamp(null);
                deAnexo.setHash(obtenerHash(documento));

                if(anexo.getTipoMIME() != null && anexo.getTipoMIME().length() > 20){
                    deAnexo.setTipoMIME(null);    // Parrafo 48 de la Guia de Aplicación SICRES3
                }else{
                    deAnexo.setTipoMIME(anexo.getTipoMIME());
                }
                deAnexo.setAnexo(Base64.encode(documento.getData()).getBytes());

                deAnexo.setObservaciones(anexo.getObservaciones());

                fiSICRES3.getDeAnexo().add(deAnexo);
            }



            // Segmento DeInternosControl 
            FicheroIntercambioSICRES3.DeInternosControl deInternosControl = objFactory.createFicheroIntercambioSICRES3DeInternosControl();
            if(re.getRegistroDetalle().getTransporte() != null){deInternosControl.setTipoTransporteEntrada(re.getRegistroDetalle().getTransporte().getCodigo());}
            if(!StringUtils.isEmpty(re.getRegistroDetalle().getNumeroTransporte())){deInternosControl.setNumeroTransporteEntrada(re.getRegistroDetalle().getNumeroTransporte());}
            deInternosControl.setNombreUsuario(re.getUsuario().getNombreCompleto());
            if(!StringUtils.isEmpty(re.getUsuario().getUsuario().getEmail())){deInternosControl.setContactoUsuario(re.getUsuario().getUsuario().getEmail());}


            deInternosControl.setIdentificadorIntercambio(identificadorIntercambio);

            deInternosControl.setAplicacionVersionEmisora(Versio.VERSIO_SIR);
            deInternosControl.setTipoAnotacion("02"); // todo: Pendiente de asignar correctamente
            deInternosControl.setDescripcionTipoAnotacion("Envío"); // todo: Pendiente de asignar correctamente
            deInternosControl.setTipoRegistro("0");
            deInternosControl.setDocumentacionFisica(re.getRegistroDetalle().getTipoDocumentacionFisica().getId().toString());
            if(re.getRegistroDetalle().getObservaciones()!=null && re.getRegistroDetalle().getObservaciones().length() > 0){deInternosControl.setObservacionesApunte(re.getRegistroDetalle().getObservaciones());}
            deInternosControl.setIndicadorPrueba("1"); //todo: añadir propiedad que indique si es pruebas o producción
            deInternosControl.setCodigoEntidadRegistralInicio(codOficinaOrigen);
            deInternosControl.setDecodificacionEntidadRegistralInicio(obtenerDenominacionOficinaOrigen(re));

            fiSICRES3.setDeInternosControl(deInternosControl);


            // Segmento DeFormularioGenerico 
            FicheroIntercambioSICRES3.DeFormularioGenerico deFormularioGenerico = objFactory.createFicheroIntercambioSICRES3DeFormularioGenerico();
            if(!StringUtils.isEmpty(re.getRegistroDetalle().getExpone()) ){
                deFormularioGenerico.setExpone(re.getRegistroDetalle().getExpone());
            } else{
                deFormularioGenerico.setExpone(new String());
            }
            if(!StringUtils.isEmpty(re.getRegistroDetalle().getSolicita())){
                deFormularioGenerico.setSolicita(re.getRegistroDetalle().getSolicita());
            } else{
                deFormularioGenerico.setSolicita(new String());
            }

            fiSICRES3.setDeFormularioGenerico(deFormularioGenerico);


			// para validar el XML generado, descomentar el código comentado con 'validation purpose'
			 
        //    result = marshallObject(fiSICRES3);


            //Guarda el archivo xml

//            CadenaAXML xm = new CadenaAXML();
//            Source origen = new DOMSource(xm.stringADocumento(result));
//            Result resultado = new StreamResult(new java.io.File(re.getRegistroDetalle().getExtracto() + ".xml"));
//            //nombre del archivo
//            Result consola= new StreamResult(System.out);
//            Transformer transformar = TransformerFactory.newInstance().newTransformer();
//            transformar.transform(origen, resultado);
//            transformar.transform(origen, consola);


       // } catch (Exception je) {
       //     log.error("Error serializar registro de entrada: " + je.getMessage(), je);
       // }
        
//		catch (JAXBException je) {
//			StringWriter sw = new StringWriter();
//			je.printStackTrace(new PrintWriter(sw));
//			log.error("Error serializar registro de entrada: " + sw.toString());
//		}
//
//		catch (SAXException se) { // validation purpose
//			StringWriter sw = new StringWriter();
//			se.printStackTrace(new PrintWriter(sw));
//			log.error("Error al validar registro de entrada: " + sw.toString());
//		}

        return fiSICRES3;
    }
*/





    public static String marshallObject(FicheroIntercambioSICRES3 fiSICRES3) throws Exception {
      String result;
      
      URL url = SirUtils.class.getResource("/xsd/SICRES3_INTERCAMBIO_APL.xsd");
      log.info("SICRES3 Schema: " + url);
 
      log.info("SICRES3 Validating 1");

      SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); // validation purpose
      log.info("SICRES3 Validating 2");
      Schema schema = sf.newSchema(url); // validation purpose
      

      log.info("SICRES3 JAXB");
      JAXBContext jc = null;
      jc = JAXBContext.newInstance(FicheroIntercambioSICRES3.class);
      log.info("SICRES3 MARSHALL");
      Marshaller m = jc.createMarshaller();
      m.setSchema(schema); // validation purpose
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      StringWriter sw = new StringWriter();
      m.marshal(fiSICRES3, sw);

      result = sw.toString();

      log.info(result);
      return result;
    }
    
    
    
    
    

    /**
     * Obtiene el código Oficina de Origen dependiendo de si es interna o externa
     * @param re
     * @return
     * @throws Exception
     */
    protected static String obtenerCodigoOficinaOrigen(RegistroEntrada re) throws Exception{
        String codOficinaOrigen = null;

        if((re.getRegistroDetalle().getOficinaOrigenExternoCodigo()==null)&&(re.getRegistroDetalle().getOficinaOrigen()==null)){
            codOficinaOrigen = re.getOficina().getCodigo();
        }
        else if(re.getRegistroDetalle().getOficinaOrigenExternoCodigo()!=null) {
            codOficinaOrigen = re.getRegistroDetalle().getOficinaOrigenExternoCodigo();
        }
        else {
            codOficinaOrigen = re.getRegistroDetalle().getOficinaOrigen().getCodigo();
        }

        return codOficinaOrigen;
    }

    /**
     * Obtiene el denominación Oficina de Origen dependiendo de si es interna o externa
     * @param re
     * @return
     * @throws Exception
     */
    protected static String obtenerDenominacionOficinaOrigen(RegistroEntrada re) throws Exception{
        String denominacionOficinaOrigen = null;

        if((re.getRegistroDetalle().getOficinaOrigenExternoCodigo()==null)&&(re.getRegistroDetalle().getOficinaOrigen()==null)){
            denominacionOficinaOrigen = re.getOficina().getCodigo();
        }
        else if(re.getRegistroDetalle().getOficinaOrigenExternoCodigo()!=null) {
            denominacionOficinaOrigen = re.getRegistroDetalle().getOficinaOrigenExternoDenominacion();
        }
        else {
            denominacionOficinaOrigen = re.getRegistroDetalle().getOficinaOrigen().getDenominacion();
        }

        return denominacionOficinaOrigen;
    }

    /**
     * Genera el identificador de intercambio a partir del código de la oficina de origen
     * @param codOficinaOrigen
     * @return
     * @throws Exception
     */
    protected static String identificadorIntercambio(String codOficinaOrigen) throws Exception{

        String identificador = "";
        SimpleDateFormat anyo = new SimpleDateFormat("yy"); // Just the year, with 2 digits

        identificador = codOficinaOrigen + "_" + anyo.format(Calendar.getInstance().getTime()) + "_" + getIdToken(); //todo: Añadir secuencia real


        return identificador;
    }

    /**
     * Genera el Hash mediante MD5 del contenido del documento y lo codifica en base64
     * @param documento
     * @return
     * @throws Exception
     */
    protected static byte[] obtenerHash(DocumentCustody documento) throws Exception{

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(documento.getData());

        return Base64.encode(digest).getBytes();

    }

    /**
     * Obtiene la Extensión de un Fichero a partir de su nombre
     * @param nombreFichero
     * @return
     * @throws Exception
     */
    protected static String getExtension(String nombreFichero) throws Exception{
        String extension = "";

        int i = nombreFichero.lastIndexOf('.');
        if (i > 0) {
            extension = nombreFichero.substring(i+1);
        }
        log.info("Extension fichero: " + extension);
        return extension;
    }

    /**
     * Calcula una cadena de ocho dígitos a partir del instante de tiempo actual.
     *
     * @return la cadena (String) de ocho digitos
     */
    private static final AtomicLong TIME_STAMP = new AtomicLong();

    private static String getIdToken() {
        long now = System.currentTimeMillis();
        while (true) {
            long last = TIME_STAMP.get();
            if (now <= last)
                now = last + 1;
            if (TIME_STAMP.compareAndSet(last, now))
                break;
        }
        long unsignedValue = new String(new Long(now).toString()).hashCode() & 0xffffffffl;
        String result = new String(new Long(unsignedValue).toString());
        if (result.length() > 8) {
            result = result.substring(result.length() - 8, result.length());
        } else {
            result = String.format("%08d", unsignedValue);
        }
        return result;
    }

    
/*
    *//**
     * 
     * @return
     * @throws Exception
     *//*
    public static WS_SIR6_B_PortType getWS_SIR6_B() throws Exception {
       WS_SIR6_BServiceLocator locator = new WS_SIR6_BServiceLocator();
       URL url = new URL(Configuracio.getSirServerBase()  + "/WS_SIR6_B");
       WS_SIR6_B_PortType ws_sir6_b = locator.getWS_SIR6_B(url);
       return ws_sir6_b;
    }
    
    
    
    public static RespuestaWS ws_sir6_b_recepcionFicheroDeAplicacion(String xml) throws Exception {
      
      if (Configuracio.useDirectApiSir()) {
        String url = Configuracio.getSirServerBase()  + "/WS_SIR6_B";
        return WS_SIR6_B_DirectApi.recepcionFicheroDeAplicacion(xml, url);
      } else {
        WS_SIR6_B_PortType ws_sir6_b = SirUtils.getWS_SIR6_B();
        return ws_sir6_b.recepcionFicheroDeAplicacion(xml);
      }
    }*/
    
    


    /**
     * Transforma un {@link es.caib.regweb3.persistence.utils.sir.FicheroIntercambioSICRES3.DeInteresado} en un {@link es.caib.regweb3.model.Interesado}
     * @param deInteresado
     * @return
     */
    private static Interesado transformarInteresado (FicheroIntercambioSICRES3.DeInteresado deInteresado,
        CatPaisLocal catPaisEjb, CatProvinciaLocal catProvinciaEjb,
        CatLocalidadLocal catLocalidadEjb) throws Exception{

        Interesado interesado = new Interesado();
        interesado.setIsRepresentante(false);

        // Averiguamos que tipo es el Interesado
        if(StringUtils.isEmpty(deInteresado.getRazonSocialInteresado())){
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        }else{
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if(!StringUtils.isEmpty(deInteresado.getRazonSocialInteresado())){interesado.setRazonSocial(deInteresado.getRazonSocialInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getNombreInteresado())){interesado.setNombre(deInteresado.getNombreInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getPrimerApellidoInteresado())){interesado.setApellido1(deInteresado.getPrimerApellidoInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getSegundoApellidoInteresado())){interesado.setApellido2(deInteresado.getSegundoApellidoInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getTipoDocumentoIdentificacionInteresado())){interesado.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(deInteresado.getTipoDocumentoIdentificacionInteresado().charAt(0)));}
        if(!StringUtils.isEmpty(deInteresado.getDocumentoIdentificacionInteresado())){interesado.setDocumento(deInteresado.getDocumentoIdentificacionInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getPaisInteresado())){interesado.setPais(catPaisEjb.findByCodigo(Long.valueOf(deInteresado.getPaisInteresado())));}
        if(!StringUtils.isEmpty(deInteresado.getProvinciaInteresado())){interesado.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(deInteresado.getProvinciaInteresado())));}
        if(!StringUtils.isEmpty(deInteresado.getMunicipioInteresado())){interesado.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(deInteresado.getMunicipioInteresado()),Long.valueOf(deInteresado.getProvinciaInteresado())));}
        if(!StringUtils.isEmpty(deInteresado.getDireccionInteresado())){interesado.setDireccion(deInteresado.getDireccionInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getCodigoPostalInteresado())){interesado.setCp(deInteresado.getCodigoPostalInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getCorreoElectronicoInteresado())){interesado.setEmail(deInteresado.getCorreoElectronicoInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getTelefonoContactoInteresado())){interesado.setTelefono(deInteresado.getTelefonoContactoInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getDireccionElectronicaHabilitadaInteresado())){interesado.setDireccionElectronica(deInteresado.getDireccionElectronicaHabilitadaInteresado());}
        if(!StringUtils.isEmpty(deInteresado.getCanalPreferenteComunicacionInteresado())){interesado.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(deInteresado.getCanalPreferenteComunicacionInteresado()));}        
        if(!StringUtils.isEmpty(deInteresado.getObservaciones())){interesado.setObservaciones(deInteresado.getObservaciones());}

        // Si el interesado tiene representante, lo generamos
        if(!StringUtils.isEmpty(deInteresado.getNombreRepresentante()) || !StringUtils.isEmpty(deInteresado.getRazonSocialRepresentante())){
            interesado.setRepresentante(transformarRepresentante(deInteresado, interesado,
                catPaisEjb, catProvinciaEjb, catLocalidadEjb));
        }

        return interesado;
    }

    /**
     * Transforma un {@link es.caib.regweb3.persistence.utils.sir.FicheroIntercambioSICRES3.DeInteresado} en un {@link es.caib.regweb3.model.Interesado}
     * @param deRepresentante
     * @return
     */
    private static Interesado transformarRepresentante(FicheroIntercambioSICRES3.DeInteresado deRepresentante,
        Interesado interesado, CatPaisLocal catPaisEjb, 
        CatProvinciaLocal catProvinciaEjb, CatLocalidadLocal catLocalidadEjb) throws Exception {

        Interesado representante = new Interesado();
        representante.setIsRepresentante(true);
        representante.setRepresentado(interesado);

        // Averiguamos que tipo es el Representante
        if(StringUtils.isEmpty(deRepresentante.getRazonSocialRepresentante())){
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        }else{
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if(!StringUtils.isEmpty(deRepresentante.getRazonSocialRepresentante())){representante.setRazonSocial(deRepresentante.getRazonSocialRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getNombreRepresentante())){representante.setNombre(deRepresentante.getNombreRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getPrimerApellidoRepresentante())){representante.setApellido1(deRepresentante.getPrimerApellidoRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getSegundoApellidoRepresentante())){representante.setApellido2(deRepresentante.getSegundoApellidoRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getTipoDocumentoIdentificacionRepresentante())){representante.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(deRepresentante.getTipoDocumentoIdentificacionRepresentante().charAt(0)));}
        if(!StringUtils.isEmpty(deRepresentante.getDocumentoIdentificacionRepresentante())){representante.setDocumento(deRepresentante.getDocumentoIdentificacionRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getPaisRepresentante())){representante.setPais(catPaisEjb.findByCodigo(Long.valueOf(deRepresentante.getPaisRepresentante())));}
        if(!StringUtils.isEmpty(deRepresentante.getProvinciaRepresentante())){representante.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(deRepresentante.getProvinciaRepresentante())));}
        if(!StringUtils.isEmpty(deRepresentante.getMunicipioRepresentante())){representante.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(deRepresentante.getMunicipioRepresentante()),Long.valueOf(deRepresentante.getProvinciaInteresado())));}
        if(!StringUtils.isEmpty(deRepresentante.getDireccionRepresentante())){representante.setDireccion(deRepresentante.getDireccionRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getCodigoPostalRepresentante())){representante.setCp(deRepresentante.getCodigoPostalRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getCorreoElectronicoRepresentante())){representante.setEmail(deRepresentante.getCorreoElectronicoRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getTelefonoContactoRepresentante())){representante.setTelefono(deRepresentante.getTelefonoContactoRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getDireccionElectronicaHabilitadaRepresentante())){representante.setDireccionElectronica(deRepresentante.getDireccionElectronicaHabilitadaRepresentante());}
        if(!StringUtils.isEmpty(deRepresentante.getCanalPreferenteComunicacionRepresentante())){representante.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(deRepresentante.getCanalPreferenteComunicacionRepresentante()));}
        if(!StringUtils.isEmpty(deRepresentante.getObservaciones())){representante.setObservaciones(deRepresentante.getObservaciones());}

        return representante;

    }

}
