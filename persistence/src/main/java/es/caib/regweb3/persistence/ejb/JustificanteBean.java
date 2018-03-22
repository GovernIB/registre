package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.MetadataConstants;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 20/03/18
 */

@Stateless(name = "JustificanteEJB")
@SecurityDomain("seycon")
public class JustificanteBean implements JustificanteLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB private PluginLocal pluginEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private SignatureServerLocal signatureServerEjb;


    @Override
    public AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro, String tipoRegistro, String idioma) throws I18NException, I18NValidationException {

        String custodyID = null;
        boolean error = false;
        IDocumentCustodyPlugin documentCustodyPlugin = null;
        long tiempo = System.currentTimeMillis();

        try {
            // Comprobamos si ya se ha generado el Justificante
            if (registro.getRegistroDetalle().getTieneJustificante()) {
                throw new I18NException("aviso.justificante.existe");
            }

            log.info("------------------------------------------------------------");
            log.info("Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado());
            log.info("");
            final Long idEntidad = usuarioEntidad.getEntidad().getId();

            // Carregam el plugin del Justificant
            IJustificantePlugin justificantePlugin = (IJustificantePlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_JUSTIFICANTE);

            // Comprova que existeix el plugin de justificant
            if (justificantePlugin == null) {
                // No s´ha definit cap plugin de Justificant. Consulti amb el seu Administrador.
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.1"));
            }

            // Carregam el plugin del Custodia del Justificante
            documentCustodyPlugin = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

            // Comprova que existeix el plugin de Custodia del Justificante
            if (documentCustodyPlugin == null) {
                // No s´ha definit cap plugin de Custòdia-Justificant. Consulti amb el seu Administrador.
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.7"));
            }

            // Cerca el Plugin de Justificant definit a les Propietats Globals
            ISignatureServerPlugin signaturePlugin = (ISignatureServerPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);

            // Comprova que existeix el plugin de justificant
            if (signaturePlugin == null) {
                // No s´ha definit cap plugin de Firma. Consulti amb el seu Administrador.
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.4"));
            }

            // Mensajes traducidos
            Locale locale = new Locale(idioma);
            String fileName = I18NLogicUtils.tradueix(locale, "justificante.fichero") + "_" + registro.getNumeroRegistroFormateado() + ".pdf";
            String nombreFichero = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
            String tituloAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.titulo");
            String observacionesAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.observaciones");

            // Crea el anexo del justificante firmado
            AnexoFull anexoFull = new AnexoFull();
            Anexo anexo = anexoFull.getAnexo();
            anexo.setTitulo(tituloAnexo);
            anexo.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL);
            anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad("TD99", idEntidad));
            anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_DOC_ADJUNTO);
            anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
            anexo.setObservaciones(observacionesAnexo);
            anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
            anexo.setJustificante(true);

            // Generam la Custòdia per tenir el CSV
            Map<String, Object> custodyParameters = getCustodyParameters(registro, anexo, anexoFull, usuarioEntidad);
            custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);
            Metadata mcsv = documentCustodyPlugin.getOnlyOneMetadata(custodyID, MetadataConstants.ENI_CSV);

            String csv = null;
            if (mcsv != null) {
                csv = mcsv.getValue();
            }
            anexo.setCsv(csv);

            String url = documentCustodyPlugin.getValidationUrl(custodyID, custodyParameters);
            String specialValue = documentCustodyPlugin.getSpecialValue(custodyID, custodyParameters);
            // Obtenim el ByteArray per generar el pdf
            byte[] pdfSignat;
            if (registro instanceof RegistroEntrada) {
                pdfSignat = justificantePlugin.generarJustificanteEntrada((RegistroEntrada) registro, url, specialValue, csv, idioma);
            } else {
                pdfSignat = justificantePlugin.generarJustificanteSalida((RegistroSalida) registro, url, specialValue, csv, idioma);
            }

            // Cream l'annex justificant i el firmam
            // Firma el justificant
            SignatureCustody sign = signatureServerEjb.signJustificante(pdfSignat, idioma, idEntidad);
            sign.setName(nombreFichero);

            anexoFull.setSignatureCustody(sign);
            anexoFull.setSignatureFileDelete(false);
            anexoFull.getAnexo().setSignType("PAdES");
            anexoFull.getAnexo().setSignFormat("implicit_enveloped/attached");
            anexoFull.getAnexo().setSignProfile("AdES-EPES");

            // Cream l'annex justificant
            anexoFull = anexoEjb.crearJustificanteAnexo(anexoFull, usuarioEntidad, registro.getId(), tipoRegistro, custodyID);

            log.info("");
            tiempo = System.currentTimeMillis() - tiempo;
            log.info("Fin Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(tiempo));
            log.info("------------------------------------------------------------");

            return anexoFull;

        } catch (I18NValidationException i18nve) {
            error = true;
            throw i18nve;
        } catch (I18NException i18ne) {
            error = true;
            throw i18ne;
        } catch (Exception e) {
            error = true;
            throw new I18NException(e, "justificante.error", new I18NArgumentString(e.getMessage()));
        } finally {
            if (error) {

                if (documentCustodyPlugin != null && custodyID != null) {
                    try {
                        documentCustodyPlugin.deleteCustody(custodyID);
                    } catch (Throwable th) {
                        log.warn("Error esborrant justificant a custodia: " + th.getMessage(), th);
                    }
                }
            }

        }

    }

    /**
     *
     * @param registro
     * @param anexo
     * @param anexoFull
     * @param usuarioEntidad
     * @return
     */
    private Map<String, Object> getCustodyParameters(IRegistro registro, Anexo anexo,
                                                       AnexoFull anexoFull, UsuarioEntidad usuarioEntidad)  {

long inicio = System.currentTimeMillis();
        Map<String,Object> custodyParameters = new HashMap<String, Object>();

        custodyParameters.put("registro", registro);
        custodyParameters.put("anexo", anexo);
        custodyParameters.put("anexoFull", anexoFull);
        custodyParameters.put("usuarioEntidad", usuarioEntidad);



        Interesado interesado = registro.getRegistroDetalle().getInteresados().get(0);
        custodyParameters.put("ciudadano_nombre", interesado.getNombreCompleto());

        String documentAdministratiu = interesado.getDocumento();
        if (documentAdministratiu == null) {
            documentAdministratiu = interesado.getCodigoDir3();
        }

        custodyParameters.put("ciudadano_idadministrativo", documentAdministratiu);

        return custodyParameters;

    }
}
