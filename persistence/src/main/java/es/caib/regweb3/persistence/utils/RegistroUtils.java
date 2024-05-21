package es.caib.regweb3.persistence.utils;

import es.caib.dir3caib.ws.api.oficina.ContactoTF;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.PlantillaJson;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 * Agrupa todas las funcionalidades comunes para trabajar con Registros de Entrada y Salida
 * @author earrivi
 * Date: 14/07/14
 */

public class RegistroUtils{

    public static final Logger log = LoggerFactory.getLogger(RegistroUtils.class);

    private RegistroUtils() {
    }


    /**
     * Serializa en XML un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param bean
     * @return
     */
    public static String serilizarXml(Object bean) throws JAXBException {

        JAXBContext jaxbCtx = null;

        if(bean instanceof RegistroEntrada){
            jaxbCtx = JAXBContext.newInstance(RegistroEntrada.class);
        }else if(bean instanceof RegistroSalida){
            jaxbCtx = JAXBContext.newInstance(RegistroSalida.class);
        }else if(bean instanceof PlantillaJson){
            jaxbCtx = JAXBContext.newInstance(PlantillaJson.class);
        }

        StringWriter xmlWriter = new StringWriter();
        Marshaller marshaller = jaxbCtx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(bean, xmlWriter);

        return xmlWriter.toString();
    }

    /**
     *  Desserializa un XML en un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param plantilla
     * @return
     */
    public static PlantillaJson desSerilizarPlantillaXml(String plantilla) {

        JAXBContext jaxbCtx;
        PlantillaJson plantillaJson = null;
        try {
            jaxbCtx = JAXBContext.newInstance(PlantillaJson.class);
            plantillaJson = (PlantillaJson) jaxbCtx.createUnmarshaller().unmarshal(
                    new StringReader(plantilla));

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return plantillaJson;
    }


    /**
     *  Desserializa un XML en un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param registroEntradaOriginal
     * @return
     */
    public static RegistroEntrada desSerilizarREXml(String registroEntradaOriginal) {

        JAXBContext jaxbCtx;
        RegistroEntrada registroEntrada = null;
        try {
            jaxbCtx = JAXBContext.newInstance(RegistroEntrada.class);
            registroEntrada = (RegistroEntrada) jaxbCtx.createUnmarshaller().unmarshal(
                    new StringReader(registroEntradaOriginal));

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return registroEntrada;
    }

    /**
     *  Desserializa un XML en un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param registroSalidaOriginal
     * @return
     */
    public static RegistroSalida desSerilizarRSXml(String registroSalidaOriginal) {

        JAXBContext jaxbCtx;
        RegistroSalida registroEntrada = null;
        try {
            jaxbCtx = JAXBContext.newInstance(RegistroSalida.class);
            registroEntrada = (RegistroSalida) jaxbCtx.createUnmarshaller().unmarshal(
                    new StringReader(registroSalidaOriginal));

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return registroEntrada;
    }

    /**
     * Obtiene los días transcurridos a partir de una fecha comparandolo con la fecha actual
     * @param fechaCreacionRegistro
     * @return
     * @throws Exception
     */
    public static Long obtenerDiasRegistro(Date fechaCreacionRegistro) throws I18NException {


       final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
       java.util.Date hoy = new Date(); //Fecha de hoy

       long diferencia = ( hoy.getTime() - fechaCreacionRegistro.getTime() )/MILLSECS_PER_DAY;

       return diferencia;

    }

    /**
     * Obtiene el Numero de registro Entrada formateado según la configuración de la Entidad a la que pertecene
     * @param registroEntrada
     * @return
     */
    public static  String numeroRegistroFormateado(RegistroEntrada registroEntrada, Libro libro, Entidad entidad) throws I18NException{

        String formatNumRegistre = entidad.getNumRegistro();
        if(formatNumRegistre != null){
            SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            formatNumRegistre = formatNumRegistre.replace("${numRegistre}", String.valueOf(registroEntrada.getNumeroRegistro()));
            formatNumRegistre = formatNumRegistre.replace("${codiOficina}", registroEntrada.getOficina().getCodigo());
            formatNumRegistre = formatNumRegistre.replace("${nomOficina}", registroEntrada.getOficina().getDenominacion());
            formatNumRegistre = formatNumRegistre.replace("${tipusRegistre}", "E");
            formatNumRegistre = formatNumRegistre.replace("${dataRegistre}", formatDate.format(registroEntrada.getFecha()));
            formatNumRegistre = formatNumRegistre.replace("${anyRegistre}", formatYear.format(registroEntrada.getFecha()));
            formatNumRegistre = formatNumRegistre.replace("${numLlibre}", libro.getCodigo());
            formatNumRegistre = formatNumRegistre.replace("${nomLlibre}", libro.getNombre());
        }

        return  formatNumRegistre;
    }

    /**
     * Obtiene el Numero de registro Salida formateado según la configuración de la Entidad a la que pertecene
     * @param registroSalida
     * @return
     */
    public static String numeroRegistroFormateado(RegistroSalida registroSalida, Libro libro, Entidad entidad) throws I18NException{

        String formatNumRegistre = entidad.getNumRegistro();
        if(formatNumRegistre != null){
            SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            formatNumRegistre = formatNumRegistre.replace("${numRegistre}", String.valueOf(registroSalida.getNumeroRegistro()));
            formatNumRegistre = formatNumRegistre.replace("${codiOficina}", registroSalida.getOficina().getCodigo());
            formatNumRegistre = formatNumRegistre.replace("${nomOficina}", registroSalida.getOficina().getDenominacion());
            formatNumRegistre = formatNumRegistre.replace("${tipusRegistre}", "S");
            formatNumRegistre = formatNumRegistre.replace("${dataRegistre}", formatDate.format(registroSalida.getFecha()));
            formatNumRegistre = formatNumRegistre.replace("${anyRegistre}", formatYear.format(registroSalida.getFecha()));
            formatNumRegistre = formatNumRegistre.replace("${numLlibre}", libro.getCodigo());
            formatNumRegistre = formatNumRegistre.replace("${nomLlibre}", libro.getNombre());
        }

        return  formatNumRegistre;
    }

    /**
     * Compone el nombre del fichero del Justificante
     * @param idioma
     * @param numeroRegistroFormateado
     * @return
     */
    public static String getNombreFicheroJustificante(String idioma, String numeroRegistroFormateado){

        String fileName = I18NLogicUtils.tradueix(new Locale(idioma), "justificante.fichero") + "_" + numeroRegistroFormateado + ".pdf";
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    /**
     * Recibe una fecha y devuelve la misma fecha con hora 23, minutos 59 y segundos 59
     * @param fecha
     * @return
     * @throws Exception
     */
    public static Date ajustarHoraBusqueda(Date fecha) {

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(fecha);
        calendarDate.add(Calendar.HOUR, 23);
        calendarDate.add(Calendar.MINUTE, 59);
        calendarDate.add(Calendar.SECOND, 59);
        fecha = calendarDate.getTime();

        return fecha;
    }

    /**
     * Obtiene el idioma para generar el Justificante
     * @param registro
     * @return
     * @throws Exception
     */
    public static String getIdiomaJustificante(IRegistro registro){

        if(registro.getRegistroDetalle().getIdioma().equals(RegwebConstantes.IDIOMA_CATALAN_ID)){
            return RegwebConstantes.IDIOMA_CATALAN_CODIGO;
        }else if(registro.getRegistroDetalle().getIdioma().equals(RegwebConstantes.IDIOMA_CASTELLANO_ID)){
            return RegwebConstantes.IDIOMA_CASTELLANO_CODIGO;
        } else{
            return Configuracio.getDefaultLanguage();
        }
    }

    /**
     * Método que obtiene los contactos de la oficina Sir de destino
     *
     * @param oficinaSir
     * @return
     */
    public static String getContactosOficinaSir(OficinaTF oficinaSir)  {
        StringBuilder stb = new StringBuilder();
        for (ContactoTF contactoTF : oficinaSir.getContactos()) {
            String scontactoTF = "<b>" + contactoTF.getTipoContacto() + "</b>: " + contactoTF.getValorContacto();
            stb.append(scontactoTF);
            stb.append("<br>");
        }

        return stb.toString();

    }

    /**
     * Comprueba si alguno de los valores de búsqueda referentes al Interesado se ha rellenado
     * @param interesadoNom
     * @param interesadoLli1
     * @param interesadoLli2
     * @param interesadoDoc
     * @return
     */
    public static boolean busquedaInteresados(String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc) {

        return StringUtils.isNotEmpty(interesadoNom) || StringUtils.isNotEmpty(interesadoLli1) || StringUtils.isNotEmpty(interesadoLli2) || StringUtils.isNotEmpty(interesadoDoc);
    }

}
