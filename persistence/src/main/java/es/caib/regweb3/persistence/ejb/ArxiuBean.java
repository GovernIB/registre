package es.caib.regweb3.persistence.ejb;

import es.caib.arxiudigital.apirest.ApiArchivoDigital;
import es.caib.arxiudigital.apirest.constantes.CodigosResultadoPeticion;
import es.caib.arxiudigital.apirest.facade.pojos.Expediente;
import es.caib.arxiudigital.apirest.facade.resultados.Resultado;
import es.caib.arxiudigital.apirest.facade.resultados.ResultadoBusqueda;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.api.CustodyException;
import org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.ArxiuDigitalCAIBDocumentCustodyPlugin;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 06/03/18
 */

@Stateless(name = "ArxiuEJB")
@SecurityDomain("seycon")
@RunAs("RWE_SUPERADMIN")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public class ArxiuBean implements ArxiuLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;


    @Override
    public void cerrarExpediente(Expediente expediente, ApiArchivoDigital apiArxiu, Long idEntidad) throws Exception {

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        int reintents = 10;

        peticion.append("expediente: ").append(expediente.getName()).append(System.getProperty("line.separator"));
        peticion.append("id: ").append(expediente.getId()).append(System.getProperty("line.separator"));

        do {
            --reintents;

            Resultado res;
            try {
                res = apiArxiu.cerrarExpediente(expediente.getId());
            } catch (Exception var23) {
                this.log.error("Error no controlat al cerrarExpediente()[Miram si podem reintentar...]: " + var23.getMessage(), var23);
                String error = var23.getMessage();
                if (reintents <= 0 || error == null || !error.contains("Proxy Error") || !error.contains("/services/closeFile")) {
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_CERRAR_EXPEDIENTE, "Cerrar expediente", peticion.toString(), var23, null,System.currentTimeMillis() - tiempo, idEntidad, expediente.getName());
                    throw var23;
                }

                res = new Resultado();
                res.setCodigoResultado("COD_020");
                res.setMsjResultado("Send timeout");
            }

            String errorCodi = res.getCodigoResultado();
            if (!this.hiHaError(errorCodi)) {
                log.info("Se ha cerrado correctamente el Expediente: " +expediente.getName());
                // Integracion
                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_CERRAR_EXPEDIENTE, "Cerrar expediente", peticion.toString(),System.currentTimeMillis() - tiempo, idEntidad, expediente.getName());
                break;
            }

            String msg = res.getMsjResultado();

            if (!"COD_020".equals(errorCodi) || !msg.startsWith("Send timeout")) {
                if ("COD_021".equals(errorCodi) && msg.startsWith("Could not have the permission of Delete to perfom the operation")) {
                    this.log.info("S'ha intentat cerrarExpediente() però aquest ja esta a RM. Es dóna per bó.");
                    break;
                }
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_CERRAR_EXPEDIENTE, "Cerrar expediente", peticion.toString(), new CustodyException("No s'ha pogut tancar l'expedient amb uuid " + expediente.getId() + ": " + res.getCodigoResultado() + " - " + res.getMsjResultado()), null,System.currentTimeMillis() - tiempo, idEntidad, expediente.getName());
                throw new CustodyException("No s'ha pogut tancar l'expedient amb uuid " + expediente.getId() + ": " + res.getCodigoResultado() + " - " + res.getMsjResultado());
            }

            this.log.warn("Gestió de reintents de apiArxiu.cerrarExpediente(): reintent compte enrera " + reintents + ". Esperam " + 5000L + " ms");
            Thread.sleep(5000L);
            if (reintents <= 0) {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_CERRAR_EXPEDIENTE, "Cerrar expediente", peticion.toString(), new CustodyException("S'han esgotat els reintents i no s'ha pogut tancar l'expedient amb uuid " + expediente.getId() + ": " + res.getCodigoResultado() + " - " + res.getMsjResultado()), null,System.currentTimeMillis() - tiempo, idEntidad, expediente.getName());
                throw new CustodyException("S'han esgotat els reintents i no s'ha pogut tancar l'expedient amb uuid " + expediente.getId() + ": " + res.getCodigoResultado() + " - " + res.getMsjResultado());
            }

        } while(reintents > 0);
    }

    public void cerrarExpedientesScheduler(Long idEntidad, String fechaInicio) throws Exception{

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();

        try {

            ArxiuDigitalCAIBDocumentCustodyPlugin custody = (ArxiuDigitalCAIBDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

            ApiArchivoDigital apiArxiu = custody.getApiArxiu(null);

            // Fecha fin búsqueda
            Date hoy = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            //String queryDM = "(+TYPE:\"eni:expediente\" AND @eni\\:cod_clasificacion:\""+custody.getPropertySerieDocumentalEL()+"\")";
            //String queryDM = "(+TYPE:\"eni:expediente\" AND @eni\\:fecha_inicio:[2018-05-01T00:00:00.000Z TO "+formatDate.format(hoy)+"T23:59:59.000Z] AND @eni\\:cod_clasificacion:\""+custody.getPropertySerieDocumentalEL()+"\") ";
            String queryDM = "(+TYPE:\"eni:expediente\" AND @eni\\:fecha_inicio:["+fechaInicio+" TO "+formatDate.format(hoy)+"T23:59:59.000Z] AND @eni\\:cod_clasificacion:\""+custody.getPropertySerieDocumentalEL()+"\") ";

            log.info("fechaInicio: " + fechaInicio);
            log.info("fechaFin: " + formatDate.format(hoy)+"T23:59:59.000Z");
            log.info("queryDM: " + queryDM);

            peticion.append("fecha inicio: ").append(fechaInicio).append(System.getProperty("line.separator"));
            peticion.append("fecha fin: ").append(formatDate.format(hoy)+"T23:59:59.000Z").append(System.getProperty("line.separator"));
            peticion.append("queryDM: ").append(queryDM).append(System.getProperty("line.separator"));

            ResultadoBusqueda<Expediente> result = apiArxiu.busquedaExpedientes(queryDM,0);

            if (hiHaErrorEnCerca(result.getCodigoResultado())) {
                log.info("Error en la búsqueda de expedientes: " + result.getCodigoResultado() + "-" + result.getMsjResultado());
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_CERRAR_EXPEDIENTE, "Scheduler: Cerrar expedientes", peticion.toString(), new CustodyException("Error en la búsqueda de expedientes: " + result.getCodigoResultado() + "-" + result.getMsjResultado()), null,System.currentTimeMillis() - tiempo, idEntidad, "");
                throw new Exception("Error en la búsqueda de expedientes: " + result.getCodigoResultado() + "-" + result.getMsjResultado());
            }

            List<Expediente> lista = result.getListaResultado();

            peticion.append("expedientes abiertos totales: ").append(result.getNumeroTotalResultados()).append(System.getProperty("line.separator"));
            peticion.append("expedientes a cerrar: ").append(lista.size()).append(System.getProperty("line.separator"));

            //log.info("Total expedientes abiertos: " + result.getNumeroTotalResultados());
            //log.info("Total expedientes a cerrar: " + lista.size());
            //log.info("");

            for (Expediente expediente : lista) {

                try{
                    cerrarExpediente(expediente,apiArxiu,idEntidad);
                }catch (CustodyException c){
                    log.info("Ha ocurrido un error al cerrar el expediente: "+ expediente.getName()+ " --> " + c.getMessage());
                }

            }

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_CERRAR_EXPEDIENTE, "Scheduler: Cerrar expedientes", peticion.toString(),System.currentTimeMillis() - tiempo, idEntidad, "");

        }catch (I18NException e) {
            e.printStackTrace();
        } catch (CustodyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean hiHaErrorEnCerca(String code) {
        return !CodigosResultadoPeticion.PETICION_CORRECTA.equals(code)
                && !CodigosResultadoPeticion.LISTA_VACIA.equals(code);
    }

    private boolean hiHaError(String code) {
        return !"COD_000".equals(code);
    }

}
