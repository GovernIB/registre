package es.caib.regweb3.persistence.ejb;


import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.integracion.ArxiuCaibUtils;
import es.caib.regweb3.persistence.integracion.JustificanteArxiu;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.utils.TimeUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataConstants;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.Date;
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
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class JustificanteBean implements JustificanteLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB private PluginLocal pluginEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private SignatureServerLocal signatureServerEjb;
    @EJB private IntegracionLocal integracionEjb;
    @Autowired ArxiuCaibUtils arxiuCaibUtils;


    @Override
    public AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException {

        // Comprobamos si ya se ha generado el Justificante
        if (registro.getRegistroDetalle().getTieneJustificante()) {
            throw new I18NException("aviso.justificante.existe");
        }

        if(usuarioEntidad.getEntidad().getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            return crearJustificanteDocumentCustody(usuarioEntidad, registro, tipoRegistro, idioma);

        }else if(usuarioEntidad.getEntidad().getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            return crearJustificanteArxiu(usuarioEntidad, registro, tipoRegistro, idioma);

        }

        return null;
    }

    /**
     * Crear el Justificante del Registro utilizando el API {@link org.fundaciobit.plugins.documentcustody}
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    private AnexoFull crearJustificanteDocumentCustody(UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException{

        String custodyID = null;
        boolean error = false;
        IDocumentCustodyPlugin documentCustodyPlugin = null;
        long tiempo = System.currentTimeMillis();
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Generar Justificante DocumentCustody";
        String numRegFormat = "";
        Locale locale = new Locale(idioma);
        final Long idEntidad = usuarioEntidad.getEntidad().getId();

        try {

            log.info("------------------------------------------------------------");
            log.info("Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado());
            log.info("");

            // Integración
            peticion.append("usuario: ").append(usuarioEntidad.getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(registro.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("tipoRegistro: ").append(tipoRegistro).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(registro.getOficina().getDenominacion()).append(System.getProperty("line.separator"));

            numRegFormat = registro.getNumeroRegistroFormateado();

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

            peticion.append("clase: ").append(documentCustodyPlugin.getClass().getName()).append(System.getProperty("line.separator"));

            // Cerca el Plugin de Justificant definit a les Propietats Globals
            ISignatureServerPlugin signaturePlugin = (ISignatureServerPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);


            // Comprova que existeix el plugin de justificant
            if (signaturePlugin == null) {
                // No s´ha definit cap plugin de Firma. Consulti amb el seu Administrador.
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.4"));
            }

            // Mensajes traducidos
            String fileName = I18NLogicUtils.tradueix(locale, "justificante.fichero") + "_" + registro.getNumeroRegistroFormateado() + ".pdf";
            String nombreFichero = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
            String tituloAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.titulo");
            String observacionesAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.observaciones");

            // Crea el anexo del justificante firmado
            AnexoFull anexoFull = new AnexoFull();
            Anexo anexo = anexoFull.getAnexo();
            anexo.setPerfilCustodia(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);
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

            peticion.append("custodyID: ").append(custodyID).append(System.getProperty("line.separator"));

            String csv = null;
            if (mcsv != null) {
                csv = mcsv.getValue();
            }
            anexo.setCsv(csv);


            String url = documentCustodyPlugin.getOriginalFileUrl(custodyID, custodyParameters);
            String specialValue = documentCustodyPlugin.getSpecialValue(custodyID, custodyParameters);

            // Creamos el pdf del Justificante
            byte[] pdfJustificant;
            if (registro instanceof RegistroEntrada) {
                pdfJustificant = justificantePlugin.generarJustificanteEntrada((RegistroEntrada) registro, url, specialValue, csv, idioma);
            } else {
                pdfJustificant = justificantePlugin.generarJustificanteSalida((RegistroSalida) registro, url, specialValue, csv, idioma);
            }

            // Firma el justificant
            SignatureCustody sign = signatureServerEjb.signJustificante(pdfJustificant, idioma, idEntidad, peticion, registro.getNumeroRegistroFormateado(), nombreFichero);
            sign.setName(nombreFichero);


            // Cream l'annex justificant
            anexoFull.setSignatureCustody(sign);
            anexoFull.setSignatureFileDelete(false);
            anexoFull.getAnexo().setSignType("PAdES");
            anexoFull.getAnexo().setSignFormat("implicit_enveloped/attached");
            anexoFull.getAnexo().setSignProfile("AdES-EPES");
            anexoFull.getAnexo().setEstadoFirma(RegwebConstantes.ANEXO_FIRMA_VALIDA);
            anexoFull.getAnexo().setFechaValidacion(new Date());
            anexoFull = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registro.getId(), tipoRegistro, custodyID, false);

            log.info("");
            log.info("Fin Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempo));
            log.info("------------------------------------------------------------");

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);

            return anexoFull;

        } catch (I18NValidationException | I18NException i18nve) {
            error = true;
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), i18nve, null, System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            log.error(i18nve.getMessage(), i18nve);
            throw i18nve;
        } catch (Exception e) {
            error = true;
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            log.error(e.getMessage(), e);
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
     * Crear el Justificante del Registro utilizando el API {@link es.caib.plugins.arxiu}
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    private AnexoFull crearJustificanteArxiu(UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException{

        JustificanteArxiu justificanteArxiu = null;
        IArxiuPlugin iArxiuPlugin = null;
        AnexoFull anexoFull = new AnexoFull();
        boolean error = false;
        long tiempo = System.currentTimeMillis();
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Generar Justificante Api Arxiu";
        String numRegFormat = registro.getNumeroRegistroFormateado();
        Locale locale = new Locale(idioma);

        final Entidad entidad = usuarioEntidad.getEntidad();

        try{

            log.info("------------------------------------------------------------");
            log.info("Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado());
            log.info("");

            // Integración
            peticion.append("usuario: ").append(usuarioEntidad.getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(registro.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("tipoRegistro: ").append(tipoRegistro).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(registro.getOficina().getDenominacion()).append(System.getProperty("line.separator"));


            // Cargamos el plugin de Arxiu
            iArxiuPlugin = arxiuCaibUtils.cargarPlugin(entidad.getId());

            // Comprova que existeix el plugin de Arxiu del Justificante
            if (iArxiuPlugin == null) {
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.10"));
            }

            peticion.append("clase: ").append(iArxiuPlugin.getClass().getName()).append(System.getProperty("line.separator"));


            // Carregam el plugin del Justificant per generar el pdf
            IJustificantePlugin justificantePlugin = (IJustificantePlugin) pluginEjb.getPlugin(entidad.getId(), RegwebConstantes.PLUGIN_JUSTIFICANTE);

            // Comprova que existeix el plugin de justificant
            if (justificantePlugin == null) {
                // No s´ha definit cap plugin de Justificant. Consulti amb el seu Administrador.
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.1"));
            }

            // Generamos el pdf del Justificante mediante el Plugin
            long tiempoGenerarPdf = System.currentTimeMillis();
            byte[] pdfJustificant;
            if (registro instanceof RegistroEntrada) {
                pdfJustificant = justificantePlugin.generarJustificanteEntrada((RegistroEntrada) registro, "", "", "", idioma);
            } else {
                pdfJustificant = justificantePlugin.generarJustificanteSalida((RegistroSalida) registro, "", "", "", idioma);
            }
            log.info("Generar pdf justificante: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempoGenerarPdf));

            // Mensajes traducidos
            String fileName = I18NLogicUtils.tradueix(locale, "justificante.fichero") + "_" + registro.getNumeroRegistroFormateado() + ".pdf";
            String nombreFichero = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
            String tituloAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.titulo");
            String observacionesAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.observaciones");

            // Firma el justificant
            Firma firma = signatureServerEjb.signJustificanteArxiu(pdfJustificant, idioma, entidad.getId(), peticion, registro.getNumeroRegistroFormateado(), nombreFichero);

            // Crea el anexo del justificante firmado
            Anexo anexo = crearAnexoJustificante(RegwebConstantes.PERFIL_CUSTODIA_ARXIU, tituloAnexo, observacionesAnexo, registro.getRegistroDetalle(), tipoDocumentalEjb.findByCodigoEntidad("TD99", entidad.getId()));

            // Hash
            anexo.setHash(RegwebUtils.obtenerHash(firma.getContingut()));

            // Guardamos el Justificante en Arxiu
            justificanteArxiu = arxiuCaibUtils.crearJustificante(registro, tipoRegistro, firma);

            // Asociamos el CustodyId al anexo que vamos a crear
            anexo.setExpedienteID(justificanteArxiu.getExpediente().getIdentificador());
            anexo.setCustodiaID(justificanteArxiu.getDocumento().getIdentificador());

            // Obtenemos el csv del documento creado
            String csv = arxiuCaibUtils.getCsv(anexo.getCustodiaID());
            anexo.setCsv(csv);

            peticion.append("custodyID: ").append(anexo.getCustodiaID()).append(System.getProperty("line.separator"));
            peticion.append("csv: ").append(csv).append(System.getProperty("line.separator"));

            // Guardamos el Anexo
            anexo = anexoEjb.persist(anexo);

            anexoFull.setAnexo(anexo);

            log.info("");
            log.info("Fin Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempo));
            log.info("------------------------------------------------------------");

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), numRegFormat);

            return anexoFull;

        }catch (Exception e){
            error = true;

            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), numRegFormat);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            log.error(e.getMessage(), e);
            throw new I18NException(e, "justificante.error", new I18NArgumentString(e.getMessage()));
        }finally {
            if(error){

                if (iArxiuPlugin != null && justificanteArxiu != null){
                    arxiuCaibUtils.eliminarJustificante(justificanteArxiu);
                }
            }
        }



    }

    /**
     * @param registro
     * @param anexo
     * @param anexoFull
     * @param usuarioEntidad
     * @return
     */
    private Map<String, Object> getCustodyParameters(IRegistro registro, Anexo anexo,
                                                     AnexoFull anexoFull, UsuarioEntidad usuarioEntidad) {


        Map<String, Object> custodyParameters = new HashMap<String, Object>();

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

    /**
     *
     * @param tituloAnexo
     * @param observaciones
     * @param registroDetalle
     * @param tipoDoc
     * @return
     */
    private Anexo crearAnexoJustificante(Long perfilCustodia, String tituloAnexo, String observaciones, RegistroDetalle registroDetalle, TipoDocumental tipoDoc){

        Anexo anexo = new Anexo(perfilCustodia);

        anexo.setTitulo(tituloAnexo);
        anexo.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL);
        anexo.setTipoDocumental(tipoDoc);
        anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_DOC_ADJUNTO);
        anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
        anexo.setObservaciones(observaciones);
        anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
        anexo.setJustificante(true);
        anexo.setRegistroDetalle(registroDetalle);
        anexo.setFechaCaptura(new Date());
        anexo.setFirmaValida(false);
        anexo.setSignType("PAdES");
        anexo.setSignFormat("implicit_enveloped/attached");
        anexo.setSignProfile("AdES-EPES");
        anexo.setEstadoFirma(RegwebConstantes.ANEXO_FIRMA_VALIDA);
        anexo.setFechaValidacion(new Date());

        return anexo;
    }
}
