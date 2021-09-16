package es.caib.regweb3.persistence.ejb;


import es.caib.plugins.arxiu.api.Firma;
import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.integracion.ArxiuCaibUtils;
import es.caib.regweb3.persistence.integracion.JustificanteArxiu;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;


/**
 *
 * @author earrivi
 * Date: 04/06/21
 */

@Stateless(name = "CustodiaEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class CustodiaBean implements CustodiaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB private AnexoLocal anexoEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private ColaLocal colaEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @Autowired ArxiuCaibUtils arxiuCaibUtils;



    @Override
    public Boolean custodiarJustificanteEnCola(Cola elemento, Long idEntidad, Long tipoIntegracion) throws Exception {

        // Integración
        StringBuilder peticion = new StringBuilder();
        Date inicio = new Date();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Custodiar Justificante en Arxiu-Caib";
        String hora = "<b>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(inicio) + "</b>&nbsp;&nbsp;&nbsp;";

        String error = "";

        try {

            log.info("Procedemos a custodiar el justificante de DocumentCustody en ArxiuCaib: " + elemento.getIdObjeto());

            // Integración
            peticion.append("idCola: ").append(elemento.getId()).append(System.getProperty("line.separator"));
            peticion.append("idElemento: ").append(elemento.getIdObjeto()).append(System.getProperty("line.separator"));
            peticion.append("numeroRegistro: ").append(elemento.getDescripcionObjeto()).append(System.getProperty("line.separator"));

            // Custodiamos el justificante en Arxiu-Caib
            Anexo anexo = custodiarAnexoDocumentCustody(elemento, idEntidad);

            // Marcamos el justificante como procesado en la Cola
            colaEjb.procesarElemento(elemento);

            // Integración
            peticion.append("idAnexo: ").append(anexo.getId()).append(System.getProperty("line.separator"));
            peticion.append("expediente creado: ").append(anexo.getExpedienteID()).append(System.getProperty("line.separator"));
            peticion.append("documento creado: ").append(anexo.getCustodiaID()).append(System.getProperty("line.separator"));
            if(StringUtils.isNotEmpty(anexo.getCsv())){
                peticion.append("csv: ").append(anexo.getCsv()).append(System.getProperty("line.separator"));
            }

            integracionEjb.addIntegracionOk(inicio, tipoIntegracion, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, idEntidad, elemento.getDescripcionObjeto());

            return true;

        } catch (Exception | I18NException e) {
            log.info("Error custodiando justificante de la Cola: " + elemento.getDescripcionObjeto());
            e.printStackTrace();
            error = hora + e.getMessage();
            colaEjb.actualizarElementoCola(elemento, idEntidad, error);
            // Añadimos el error a la integración
            integracionEjb.addIntegracionError(tipoIntegracion, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, idEntidad, elemento.getDescripcionObjeto());
        }

        return false;
    }

    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void custodiarJustificantesEnCola(Long idEntidad) throws Exception {

        // Obtiene un numero de elementos (configurable) pendientes de distribuir que estan en la cola
        List<Cola> elementos = colaEjb.findByTipoEntidad(RegwebConstantes.COLA_CUSTODIA, idEntidad, PropiedadGlobalUtil.getElementosColaCustodia(idEntidad));

        log.info("");
        log.info("Hay " + elementos.size() + " elementos que se van a custodiar en esta iteracion");

        for (Cola elemento : elementos) {

            custodiarJustificanteEnCola(elemento, idEntidad, RegwebConstantes.INTEGRACION_SCHEDULERS);
        }

    }

    /**
     *
     * @param elemento
     * @param idEntidad
     * @return
     * @throws Exception
     */
    private Anexo custodiarAnexoDocumentCustody(Cola elemento, Long idEntidad) throws Exception, I18NException {

        IRegistro registro;
        String custodyIdFileSystem;

        // Obtenemos el registro correspondiente
        if (elemento.getTipoRegistro().equals(REGISTRO_ENTRADA)) {
            registro = registroEntradaEjb.getConAnexosFullLigero(elemento.getIdObjeto());
        } else {
            registro = registroSalidaEjb.getConAnexosFullLigero(elemento.getIdObjeto());
        }

        // Obtenemos el justificante a custodiar
        AnexoFull justificante = anexoEjb.getAnexoFull(registro.getRegistroDetalle().getJustificante().getId(), idEntidad);
        custodyIdFileSystem = justificante.getAnexo().getCustodiaID();

        // Cargamos los plugins de Arxiu
        arxiuCaibUtils.cargarPlugin(idEntidad);

        // Custodiamos el Justificante en Arxiu-Caib
        Firma justificanteFirmado = justificante.signatureCustodytoFirma();
        JustificanteArxiu justificanteArxiuCaib = arxiuCaibUtils.crearJustificanteArxiuCaib(registro, elemento.getTipoRegistro(), justificanteFirmado);

        try{
            // Eliminamos el Justificantre en FileSystem
            anexoEjb.eliminarCustodia(custodyIdFileSystem, justificante.getAnexo(), idEntidad);
        }catch (Exception e){
            e.printStackTrace();
            log.info("Error al eliminarCustodia");
        }

        // Actualizamos el ExpedienteId, CustodyId y Csv al anexo que vamos a crear
        justificante.getAnexo().setPerfilCustodia(RegwebConstantes.PERFIL_CUSTODIA_ARXIU);
        justificante.getAnexo().setExpedienteID(justificanteArxiuCaib.getExpediente().getIdentificador());
        justificante.getAnexo().setCustodiaID(justificanteArxiuCaib.getDocumento().getIdentificador());
        justificante.getAnexo().setCsv(justificanteArxiuCaib.getDocumento().getDocumentMetadades().getCsv());
        justificante.getAnexo().setCustodiado(true);

        return anexoEjb.merge(justificante.getAnexo());
    }

    /**
     *
     * @param elemento
     * @param idEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    /*private Anexo custodiarAnexoArxiuFileSystem(Cola elemento, Long idEntidad) throws Exception, I18NException {

        log.info("Procedemos a custodiar el anexo de ArxiuFileSystem en ArxiuCaib");

        // Obtenemos el registro de entrada que se debe distribuir
        Anexo anexo = anexoEjb.findById(elemento.getIdObjeto());

        // Cargamos los plugins de Arxiu
        arxiuCaibUtils.cargarPlugin(idEntidad, true);

        // Obtenemos El Expediente y El Documento del justificante almacenado en Filesystem
        Expedient expedientFS = arxiuCaibUtils.getExpediente(anexo.getExpedienteID(), null);
        Document documentFS = arxiuCaibUtils.getDocumento(anexo.getCustodiaID(), null, true, true);

        log.info("Obtenemos el expediente anexo de filesystem: " + expedientFS.getIdentificador());
        log.info("Obtenemos el documento anexo de filesystem: " + documentFS.getIdentificador());


        // Custodiamos el Justificante en Arxiu-Caib
        arxiuCaibUtils.cargarPlugin(idEntidad, false);
        JustificanteArxiu justificanteArxiuCaib = arxiuCaibUtils.custodiarJustificante(anexo, expedientFS, documentFS);
        log.info("");
        log.info("");
        log.info("Expediente creado en Arxiu: " + justificanteArxiuCaib.getExpediente().getIdentificador());
        log.info("Documento creado en Arxiu: " + justificanteArxiuCaib.getDocumento().getIdentificador());

        // Actualizamos el ExpedienteId, CustodyId y Csv al anexo que vamos a crear
        anexo.setFileSystem(false);
        anexo.setExpedienteID(justificanteArxiuCaib.getExpediente().getIdentificador());
        anexo.setCustodiaID(justificanteArxiuCaib.getDocumento().getIdentificador());
        if(justificanteArxiuCaib.getDocumento().getDocumentMetadades() != null){
            anexo.setCsv(justificanteArxiuCaib.getDocumento().getDocumentMetadades().getCsv());
        }

        anexoEjb.merge(anexo);

        // Eliminamos el Expediente y Documento de FileSystem
        arxiuCaibUtils.cargarPlugin(idEntidad, true);

        int reintentos = 5;
        boolean result = false;
        do{
            --reintentos;

            try{
                arxiuCaibUtils.eliminarJustificanteFS(expedientFS.getIdentificador(), documentFS.getIdentificador());
                result = true;
            }catch (ArxiuException e){
                e.printStackTrace();
                log.info("Error al eliminarJustificanteFS, reintentaremos la operacion [(" + reintentos + ")] veces mas: " +  e.getMessage());
            }

            if(result){ // si ha ido bien, salimos del while
                break;
            }

            Thread.sleep(300);
        }while (reintentos > 0);

        return anexo;

    }*/

}
