package es.caib.regweb3.plugins.distribucion.email;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.*;
import org.fundaciobit.genapp.common.i18n.I18NCommonUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Este plugin distribuye los datos del registro con sus anexos via email a los emails indicados.
 * @author mgonzalez
 * @version 1
 * 10/06/2022
 */
public class DistribucionEmailPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final String basePluginDistribucionEmail = DISTRIBUCION_BASE_PROPERTY + "email.";
    private static final String PROPERTY_EMAIL_DEFAULT = basePluginDistribucionEmail + "emaildefault";
    private static final String PROPERTY_MOTIVO_DEFAULT = basePluginDistribucionEmail + "motivodefault";

    private String emails;
    private String motivo;


    public DistribucionEmailPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public DistribucionEmailPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public DistribucionEmailPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }



    @Override
    public Boolean distribuir(RegistroEntrada registro, Locale locale) throws I18NException {

       try {
           //obtenemos los emails a los que va dirigido. Los emails se envian separados por ";"

           String[] emailsParts = getEmails().split(";");
           String motivo = getMotivo();

           if (emailsParts != null) {
               //Montamos la parte de los interesados
               StringBuilder htmlInteresados = new StringBuilder();
               List<Interesado> interesados = registro.getRegistroDetalle().getInteresados();

               for (Interesado interesado : interesados) {

                   String tipoInteresado = I18NCommonUtils.tradueix(locale, "interesado.tipo." + interesado.getTipo());
                   String tipoIdentificacion = interesado.getTipoDocumentoIdentificacion() != null ? I18NCommonUtils.tradueix(locale, "tipoDocumentoIdentificacion." + interesado.getTipoDocumentoIdentificacion()) : "";
                   String documento = interesado.getDocumento() != null ? interesado.getDocumento() : "";
                   String pais = interesado.getPais() != null ? interesado.getPais().getDescripcionPais() : "";
                   String provincia = interesado.getProvincia() != null ? interesado.getProvincia().getDescripcionProvincia() : "";
                   String localidad = interesado.getLocalidad() != null ? interesado.getLocalidad().getNombre() : "";
                   String direccion = interesado.getDireccion() != null ? interesado.getDireccion() : "";
                   String cp = interesado.getCp() != null ? interesado.getCp() : "";
                   String email = interesado.getEmail() != null ? interesado.getEmail() : "";
                   String telefono = interesado.getTelefono() != null ? interesado.getTelefono() : "";
                   //SICRES4 @Deprecated
                   String direccionElectronica = interesado.getDireccionElectronica() != null ? interesado.getDireccionElectronica() : "";
                   String canal = interesado.getCanal() != null && interesado.getCanal()!= -1? interesado.getCanal().toString() : "";
                   String observacionesInteresado = interesado.getObservaciones() != null ? interesado.getObservaciones() : "";

                   htmlInteresados.append("<tr><th class=\"tableHeader\" colspan=\"2\">").append(interesado.getNombreCompleto()).append("</th></tr>")
                           .append("<tr><th>Tipus</th><td>").append(tipoInteresado).append("</td></tr>")
                           .append("<tr><th>Document</th><td>").append(tipoIdentificacion).append(": ").append(documento).append("</td></tr>");

                   if(StringUtils.isNotEmpty(pais)){htmlInteresados.append("<tr><th>País</th><td>").append(pais).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(provincia)){htmlInteresados.append("<tr><th>Provincia</th><td>").append(provincia).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(localidad)){htmlInteresados.append("<tr><th>Municipi</th><td>").append(localidad).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(direccion)){htmlInteresados.append("<tr><th>Adreça</th><td>").append(direccion).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(cp)){htmlInteresados.append("<tr><th>Codi postal</th><td>").append(cp).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(email)){htmlInteresados.append("<tr><th>Correu electrònic</th><td>").append(email).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(telefono)){htmlInteresados.append("<tr><th>Telèfon</th><td>").append(telefono).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(direccionElectronica)){htmlInteresados.append("<tr><th>Correu electrònic habilitat</th><td>").append(direccionElectronica).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(canal)){htmlInteresados.append("<tr><th>Canal preferent</th><td>").append(canal).append("</td></tr>");}
                   if(StringUtils.isNotEmpty(observacionesInteresado)){htmlInteresados.append("<tr><th>Observacions</th><td>").append(observacionesInteresado).append("</td></tr>");}
               }

               //Montamos el codigo html de los anexos y los attachements del mail.
               StringBuilder htmlAnexos = new StringBuilder();
               List<AnexoFull> anexoFulls = registro.getRegistroDetalle().getAnexosFull();
               List<Attachment> attachments = new ArrayList<>();

               for (AnexoFull anexoFull : anexoFulls) {

                   Attachment attachment;
                   if (!anexoFull.getAnexo().isJustificante()) { //Si no es justificante

                       //Datos para montar el attachment
                       attachment = new Attachment(anexoFull.getFileName(), anexoFull.getData(), anexoFull.getMime());

                       //Parte htlm de los anexos
                       long tamanoAnexo = anexoFull.getSize();
                       htmlAnexos.append("<tr><th class=\"tableHeader\" colspan=\"2\">")
                               .append(anexoFull.getAnexo().getTitulo())
                               .append("</th></tr><tr><th>Fitxer</th><td>").append(anexoFull.getFileName())
                               .append(" (").append(tamanoAnexo).append(" Kb)</td></tr>");

                   } else { //si es justificante
                       attachment = new Attachment(anexoFull.getSignFileName(), anexoFull.getData(), anexoFull.getMime());
                   }
                   attachments.add(attachment);
               }

               //Datos registro
               String codigoAsunto = registro.getRegistroDetalle().getCodigoAsunto() != null ? registro.getRegistroDetalle().getCodigoAsunto().getCodigo() : "";
               String codigoSia = registro.getRegistroDetalle().getCodigoSia() != null ? registro.getRegistroDetalle().getCodigoSia().toString() : "";
               String idioma = I18NCommonUtils.tradueix(locale, "idioma." + registro.getRegistroDetalle().getIdioma());
               String refExterna = registro.getRegistroDetalle().getReferenciaExterna() != null ? registro.getRegistroDetalle().getReferenciaExterna() : "";
               String numExpediente = registro.getRegistroDetalle().getExpediente() != null ? registro.getRegistroDetalle().getExpediente() : "";
               String transporte = registro.getRegistroDetalle().getTransporte() != null ? registro.getRegistroDetalle().getTransporte().toString() : "";
               String numTransporte = registro.getRegistroDetalle().getNumeroTransporte() != null ? registro.getRegistroDetalle().getNumeroTransporte() : "";
               String oficinaOrigen = registro.getOficina().getDenominacion() != null ? registro.getOficina().getDenominacion() : registro.getRegistroDetalle().getOficinaOrigenExternoDenominacion();
               String numRegistroOrigen = registro.getRegistroDetalle().getNumeroRegistroOrigen() != null ? registro.getRegistroDetalle().getNumeroRegistroOrigen() : "";
               String fechaRegistroOrigen = registro.getRegistroDetalle().getFechaOrigen() != null ? TimeUtils.imprimeFecha(registro.getRegistroDetalle().getFechaOrigen(), RegwebConstantes.FORMATO_FECHA_HORA) : "";
               String observaciones = registro.getRegistroDetalle().getObservaciones() != null ? registro.getRegistroDetalle().getObservaciones() : "";
               String presencial = registro.getRegistroDetalle().getPresencial() ? "Sí" : "No";

               for (String email : emailsParts) {

                   String asunto = I18NCommonUtils.tradueix(locale, "plugin.mail.asunto", registro.getNumeroRegistroFormateado());

                   StringBuilder cuerpo = new StringBuilder(new StringBuilder().append("<!DOCTYPE html><html><head><style>body {margin: 0px;font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;font-size: 14px;color: #333;}").append("table {border-radius: 4px;width: 100%;border-collapse: collapse;margin-bottom: 10px;}").append("td, th {border-bottom: solid 0.5px #ddd;height: 38px;border: 1px solid #d9edf7;").append("padding-left: 8px;padding-right: 8px;}.tableHeader {background-color: #d9edf7;border-top-left-radius: 4px;border-top-right-radius: 4px;}.header").append("{height: 30px;background-color: #ff9523;height: 90px;text-align: center;line-height: 100px;}.content {margin: auto;width: 70%;padding: 10px;}.footer").append("{height: 30px;background-color: #ff9523;text-align: center;}.headerText {font-weight: bold;font-family: \"Trebuchet MS\", Helvetica, sans-serif;").append("color: white;font-size: 30px;display: inline-block;vertical-align: middle;line-height: normal;}.footerText {").append("font-weight: bold; font-family: \"Trebuchet MS\", Helvetica, sans-serif; color: white; font-size: 13px;display: inline-block;").append("vertical-align: middle;line-height: normal; }</style></head>")
                           .append("<body><div class=\"header\"><span class=\"headerText\">Anotació de Registre ").append(registro.getNumeroRegistroFormateado()).append("</span> </div><div class=\"content\">")
                           .append("<table><tr><th class=\"tableHeader\" colspan=\"2\">Usuari que distribueix</th></tr>").append("<tr><th>Identificador</th><td>").append(registro.getUsuario().getUsuario().getIdentificador()).append("</td></tr>").append("<tr><th>Nom complet</th><td>").append(registro.getUsuario().getNombreCompleto()).append(" </td></tr>").append("<tr><th>Correu electrònic</th><td>").append(registro.getUsuario().getUsuario().getEmail()).append("</td></tr>").append("<tr><th>Motiu</th><td>").append(motivo).append("</td></tr></table>")
                           .append("<table><tr><th class=\"tableHeader\" colspan=\"2\">Anotació de registre</th></tr>").append("<tr><th>Tipus</th><td>Entrada</td></tr><tr><th>Número</th><td>").append(registro.getNumeroRegistroFormateado()).append("</td></tr>").append("<tr><th>Data</th><td>").append(TimeUtils.imprimeFecha(registro.getFecha(), RegwebConstantes.FORMATO_FECHA_HORA)).append("</td></tr>").append("<tr><th>Presencial</th><td>").append(presencial).append("</td></tr></table>")
                           .append("<table><tr><th class=\"tableHeader\" colspan=\"2\">Dades obligatòries</th></tr>").append("<tr><th>Oficina</th><td>").append(registro.getOficina()).append("</td></tr>").append("<tr><th>Llibre</th><td>").append(registro.getLibro().getNombre()).append("</td></tr>").append("<tr><th>Extracte</th><td>").append(registro.getRegistroDetalle().getExtracto()).append("</td></tr>").append("<tr><th>Documentació física</th><td>").append(I18NCommonUtils.tradueix(locale, "tipoDocumentacionFisica." + registro.getRegistroDetalle().getTipoDocumentacionFisica())).append("</td></tr>").append("<tr><th>Òrgan destí</th><td>").append(registro.getDestino().getDenominacion()).append(" - ").append(registro.getDestino().getCodigo()).append("</td></tr>").append("<tr><th>Idioma</th><td>").append(idioma).append("</td></tr></table>")

                           .append("<table><tr><th class=\"tableHeader\" colspan=\"2\">Dades opcionals</th></tr>"));
                           if(StringUtils.isNotEmpty(codigoAsunto)){cuerpo.append("<tr><th>Codi assumpte</th><td>").append(codigoAsunto).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(codigoSia)){cuerpo.append("<tr><th>Codi procediment</th><td>").append(codigoSia).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(refExterna)){cuerpo.append("<tr><th>Ref. externa</th><td>").append(refExterna).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(numExpediente)){cuerpo.append("<tr><th>Núm. expedient</th><td>").append(numExpediente).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(transporte)){cuerpo.append("<tr><th><th>Transport</th><td> ").append(transporte).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(numTransporte)){cuerpo.append("<tr><th>Transp. núm.</th><td>").append(numTransporte).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(oficinaOrigen)){cuerpo.append("<tr><th>Oficina origen</th><td>").append(oficinaOrigen).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(numRegistroOrigen)){cuerpo.append("<tr><th>Núm. reg. origen</th><td>").append(numRegistroOrigen).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(fechaRegistroOrigen)){cuerpo.append("<tr><th>Data origen</th><td>").append(fechaRegistroOrigen).append("</td></tr>");}
                           if(StringUtils.isNotEmpty(observaciones)){cuerpo.append("<tr><th>Observacions</th><td>").append(observaciones).append("</td></tr></table>");}

                            cuerpo.append("<table><tr><th class=\"tableHeader\" colspan=\"2\">Interessats</th></tr>").append(htmlInteresados).append("</table>")
                           .append("<table><tr><th class=\"tableHeader\" colspan=\"2\">Justificant</th></tr>").append("<th>Fitxer</th><td>").append(registro.getRegistroDetalle().getJustificanteAnexoFull().getSignFileName()).append(" (").append(registro.getRegistroDetalle().getJustificanteAnexoFull().getSignSize()).append(" bytes)").append("</td></tr></table>")
                           .append("<table><tr><th class=\"tableHeader\" colspan=\"2\">Annexos</th></tr>").append(htmlAnexos).append("</table></div>")
                           .append("<div class=\"footer\"><span class=\"footerText\">RegWeb3 - Govern de les Illes Baleares</span></div></body></html>");

                   //enviamos el mail con los datos y los adjuntos
                   MailUtils.enviarMailConAdjuntos(attachments, email, asunto, cuerpo);

               }
               return true;
           } else {
               log.error("No se han indicado emails a los que distribuir el registro");
               return false;
           }

       }catch (Exception e){
           e.printStackTrace();
           throw new I18NException("error.envio.email");
       }

    }

    @Override
    public Boolean getEnvioCola() throws I18NException {
        return false;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     *
     * @return
     */
    public String getPropertyEmailDefault() {
        return getProperty(PROPERTY_EMAIL_DEFAULT);
    }

    /**
     *
     * @return
     */
    public String getPropertyMotivoDefault() {
        return getProperty(PROPERTY_MOTIVO_DEFAULT);
    }

}
