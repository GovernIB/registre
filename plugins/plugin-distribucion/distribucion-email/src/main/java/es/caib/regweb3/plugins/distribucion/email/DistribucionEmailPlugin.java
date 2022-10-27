package es.caib.regweb3.plugins.distribucion.email;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;

import es.caib.regweb3.utils.MailUtils;
import es.caib.regweb3.utils.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fundaciobit.genapp.common.i18n.I18NCommonUtils;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;

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
    public Boolean distribuir(RegistroEntrada registro, Locale locale) throws Exception {

       try {
           //obtenemos los emails a los que va dirigido. Los emails se envian separados por ";"

           String[] emailsParts = getEmails() != null ? getEmails().split(";") : getPropertyEmailDefault().split(";");
           String motivo = getMotivo() != null ? getMotivo() : getPropertyMotivoDefault();

           if (emailsParts != null) {
               //Montamos la parte de los interesados
               String parteInteresados = "";
               List<Interesado> interesados = registro.getRegistroDetalle().getInteresados();
               for (Interesado interesado : interesados) {

                   //Asignar string vacio en caso de null
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
                   String canal = interesado.getCanal() != null ? interesado.getCanal().toString() : "";
                   String observacionesInteresado = interesado.getObservaciones() != null ? interesado.getObservaciones() : "";


                   parteInteresados += "<tr><th class=\"tableHeader\" colspan=\"2\">" + interesado.getNombreCompleto() + "</th></tr><tr><th>Tipus</th>\n" +
                           "<td>" + tipoInteresado + "</td></tr><tr><th>Document</th><td>" + tipoIdentificacion + ": " + documento + "</td></tr><tr>\n" +
                           "<th>País</th><td>" + pais + "</td></tr><tr><th>Provincia</th><td>" + provincia + "</td></tr><tr><th>Municipi</th><td>" + localidad + "</td></tr>\n" +
                           "<tr><th>Adreça</th><td>" + direccion + "</td></tr><tr><th>Codi postal</th><td>" + cp + "</td>\n" +
                           "</tr><tr><th>Correu electrònic</th>\n" +
                           "<td>" + email + "</td></tr><tr><th>Telèfon</th><td>" + telefono + "</td></tr><tr>\n" +
                           "<th>Canal preferent</th><td>" + canal + "</td></tr><tr><th>Observacions</th><td>" + observacionesInteresado + "</td></tr>";
               }

               //Montamos el codigo htlm de los anexos y los attachements del mail.
               String parteAnexos = "";
               List<AnexoFull> anexoFulls = registro.getRegistroDetalle().getAnexosFull();


               List<Attachment> attachments = new ArrayList<>();

               for (AnexoFull anexoFull : anexoFulls) {

                   Attachment attachment;
                   String filename = "";
                   if (!anexoFull.getAnexo().isJustificante()) { //Si no es justificante

                       //Datos para montar el attachment
                       attachment = new Attachment(anexoFull.getFileName(), anexoFull.getData(), anexoFull.getMime());

                       //Parte htlm de los
                       long tamanoAnexo = anexoFull.getSize();
                       parteAnexos += "<tr><th class=\"tableHeader\" colspan=\"2\">" + anexoFull.getAnexo().getTitulo() + "</th></tr><tr>\n" +
                               "<th>Fitxer</th><td>" + filename + "  ( " + tamanoAnexo + " bytes )\n" + "</td></tr>";

                   } else { //si es justificante
                       attachment = new Attachment(anexoFull.getSignFileName(), anexoFull.getData(), anexoFull.getMime());
                   }
                   attachments.add(attachment);

               }

               //Asignar string vacio en caso de valores null
               String tipoAsunto = registro.getRegistroDetalle().getTipoAsunto() != null ? registro.getRegistroDetalle().getTipoAsunto().getCodigo() : "";
               String codigoAsunto = registro.getRegistroDetalle().getCodigoAsunto() != null ? registro.getRegistroDetalle().getCodigoAsunto().getCodigo() : "";
               String codigoSia = registro.getRegistroDetalle().getCodigoSia() != null ? registro.getRegistroDetalle().getCodigoSia().toString() : "";
               String idioma = I18NCommonUtils.tradueix(locale, "idioma." + registro.getRegistroDetalle().getIdioma());

               String refExterna = registro.getRegistroDetalle().getReferenciaExterna() != null ? registro.getRegistroDetalle().getReferenciaExterna() : "";
               String numExpediente = registro.getRegistroDetalle().getExpediente() != null ? registro.getRegistroDetalle().getExpediente() : "";
               String transporte = registro.getRegistroDetalle().getTransporte() != null ? registro.getRegistroDetalle().getTransporte().toString() : "";
               String numTransporte = registro.getRegistroDetalle().getNumeroTransporte() != null ? registro.getRegistroDetalle().getNumeroTransporte() : "";
               String oficinaOrigen = registro.getRegistroDetalle().getOficinaOrigenExternoDenominacion() != null ? registro.getRegistroDetalle().getOficinaOrigenExternoDenominacion() + " - " + registro.getRegistroDetalle().getOficinaOrigenExternoCodigo() : "";
               String numRegistroOrigen = registro.getRegistroDetalle().getNumeroRegistroOrigen() != null ? registro.getRegistroDetalle().getNumeroRegistroOrigen() : "";
               String fechaRegistroOrigen = registro.getRegistroDetalle().getFechaOrigen() != null ? registro.getRegistroDetalle().getFechaOrigen().toString() : "";
               String observaciones = registro.getRegistroDetalle().getObservaciones() != null ? registro.getRegistroDetalle().getObservaciones() : "";
               String sPresencial = registro.getRegistroDetalle().getPresencial() ? "Sí" : "No";

               for (String email : emailsParts) {

                   String asunto = I18NCommonUtils.tradueix(locale, "plugin.mail.asunto", registro.getNumeroRegistroFormateado());
                   String mensajeTexto = "<!DOCTYPE html><html><head><style>body {margin: 0px;font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;font-size: 14px;color: #333;}\n" +
                           "table {border-radius: 4px;width: 100%;border-collapse: collapse;margin-bottom: 10px;}\n" +
                           "td, th {border-bottom: solid 0.5px #ddd;height: 38px;border: 1px solid #d9edf7;\n" +
                           "padding-left: 8px;padding-right: 8px;}.tableHeader {background-color: #d9edf7;border-top-left-radius: 4px;border-top-right-radius: 4px;}.header \n" +
                           "{height: 30px;background-color: #ff9523;height: 90px;text-align: center;line-height: 100px;}.content {margin: auto;width: 70%;padding: 10px;}.footer \n" +
                           "{height: 30px;background-color: #ff9523;text-align: center;}.headerText \n" +
                           "{    font-weight: bold;    font-family: \"Trebuchet MS\", Helvetica, sans-serif;    \n" +
                           "color: white;    font-size: 30px;display: inline-block;vertical-align: middle;line-height: normal; }.footerText {    \n" +
                           "font-weight: bold;    font-family: \"Trebuchet MS\", Helvetica, sans-serif;    color: white;  font-size: 13px;display: inline-block;\n" +
                           "vertical-align: middle;line-height: normal; }</style></head><body><div class=\"header\">\n" +
                           "<span class=\"headerText\">Anotació de Registre</span> </div><div class=\"content\">\n" +
                           "<table><tr><th class=\"tableHeader\" colspan=\"2\">Remitent Regweb3</th></tr><tr>\n" +
                           "<th>Identificador</th><td>" + registro.getUsuario() + "</td></tr>\n" +
                           "<tr><th>Nom complet</th><td>" + registro.getUsuario().getNombreCompleto() + " </td></tr><tr><th>Correu electrònic</th><td>" + registro.getUsuario().getUsuario().getEmail() + "</td></tr>\n" +
                           "<tr><th>Motiu</th><td>" + motivo + "</td></tr></table><table><tr>\n" +
                           "<th class=\"tableHeader\" colspan=\"2\">Anotació de registre</th>\n" +
                           "</tr><tr><th>Tipus</th><td>Entrada</td></tr><tr><th>Número</th><td>" + registro.getNumeroRegistroFormateado() + "</td></tr><tr><th>Data</th>\n" +
                           "<td>" + registro.getFecha() + "</td></tr><tr><th>Presencial</th><td>\n" + sPresencial + "</td></tr>\n" +
                           "</table><table><tr><th class=\"tableHeader\" colspan=\"2\">Dades obligatòries</th></tr><tr>\n" +
                           "<th>Oficina</th><td>" + registro.getOficina() + "</td>\n" +
                           "</tr><tr><th>Llibre</th><td>" + registro.getLibro().getNombre() + " - " + registro.getLibro().getCodigo() + "</td></tr><tr>\n" +
                           "<th>Extracte</th><td>" + registro.getRegistroDetalle().getExtracto() + "</td></tr><tr>\n" +
                           "<th>Documentació física</th><td>" + registro.getRegistroDetalle().getTipoDocumentacionFisica() + "</td></tr><tr>\n" +
                           "<th>Òrgan destí</th><td>" + registro.getDestino().getDenominacion() + " - " + registro.getDestino().getCodigo() + "</td>\n" +
                           "</tr><tr><th>Tipus d'assumpte</th><td> " + tipoAsunto + "</td></tr><tr>\n" +
                           "<th>Idioma</th><td>" + idioma + "</td></tr></table><table><tr><th colspan=\"4\" class=\"tableHeader\" colspan=\"2\">Dades opcionals</th></tr>\n" +
                           "<tr><th colspan=\"2\">Codi assumpte</th>\n" +
                           "<td colspan=\"2\">" + codigoAsunto + "</td></tr>\n" +
                           "<tr><th colspan=\"2\">Codi procediment</th><td colspan=\"2\">" + codigoSia + "</td></tr><tr><th>Ref. externa</th>\n" +
                           "<td>" + refExterna + "</td><th>Núm. expedient</th><td>" + numExpediente + "</td>\n" +
                           "</tr><tr><th>Transport</th><td> " + transporte + "</td><th>Transp. núm.</th><td>" + numTransporte + "</td></tr><tr><th colspan=\"2\">Origen oficina</th>\n" +
                           "<td colspan=\"2\">" + oficinaOrigen + "</td></tr>\n" +
                           "<tr><th>Origen núm.</th><td>" + numRegistroOrigen + "</td><th>Origen data</th><td>" + fechaRegistroOrigen + "</td></tr>\n" +
                           "<tr><th colspan=\"2\">Observacions</th><td colspan=\"2\">" + observaciones + "</td></tr></table><table><tr>\n" +
                           "<th class=\"tableHeader\" colspan=\"2\">Justificant</th></tr>\n" +
                           "<th>Fitxer</th><td>" + registro.getRegistroDetalle().getJustificanteAnexoFull().getSignFileName() + " ( " + registro.getRegistroDetalle().getJustificanteAnexoFull().getSignSize() + " bytes ) \n" +
                           "</td></tr></table><table><tr><th class=\"tableHeader\" colspan=\"2\">Interessats</th></tr>\n" +
                           parteInteresados +
                           "</table><table><tr><th class=\"tableHeader\" colspan=\"2\">Annexos</th></tr>\n" +
                           parteAnexos +
                           "</tbody></table></td></tr></table></div><div class=\"footer\"><span class=\"footerText\">RegWeb3 - Govern Illes Baleares</span></div></body></html>";


                   //enviamos el mail con los datos y los adjuntos
                   MailUtils.enviarMailConAdjuntos(attachments, email, asunto, mensajeTexto);

               }
               return true;
           } else {
               log.error("No se han indicado emails a los que distribuir el registro");
               return false;
           }

       }catch (Exception e){

           throw new Exception(e);
       }


    }

    @Override
    public Boolean getEnvioCola() throws Exception {
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



    private  String getPropertyEmailDefault()  throws Exception{
        return getProperty(PROPERTY_EMAIL_DEFAULT);
    }

    private  String getPropertyMotivoDefault()  throws Exception{
        return getProperty(PROPERTY_MOTIVO_DEFAULT);
    }


}
