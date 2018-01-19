package es.caib.regweb3.plugins.distribucion.mock;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.plugins.distribucion.ConfiguracionDistribucion;
import es.caib.regweb3.plugins.distribucion.Destinatario;
import es.caib.regweb3.plugins.distribucion.Destinatarios;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Plugin de distribución por defecto
 * @author mgonzalez
 */
public class DistribucionMockPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = Logger.getLogger(getClass());



    /**
     *
     */
    public DistribucionMockPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public DistribucionMockPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public DistribucionMockPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }

    @Override
    public Destinatarios distribuir(RegistroEntrada registro) throws Exception {


        log.info("Distribuir plugin");



        //La implementación Mock no devuelve destinatarios.
       Destinatarios destinatarios = new Destinatarios();

        //Código de ejemplo para posibles implementaciones
       /* List<Destinatario> destinatariosPosibles = new ArrayList<Destinatario>();
        List<Destinatario> destinatariosPropuestos = new ArrayList<Destinatario>();
        Destinatario destinatario = new Destinatario();
        destinatario.setId("1");
        destinatario.setName("BANDEJA 1");
        destinatariosPosibles.add(destinatario);

        destinatario = new Destinatario();
        destinatario.setId("2");
        destinatario.setName("BANDEJA 2");
        destinatariosPosibles.add(destinatario);

        for (int i = 4; i <= 30; i++) {
            destinatario = new Destinatario();
            destinatario.setId(i + "");
            destinatario.setName("BANDEJA " + i);
            destinatariosPropuestos.add(destinatario);
        }

        destinatario = new Destinatario();
        destinatario.setId("3");
        destinatario.setName("BANDEJA 3");
        destinatariosPropuestos.add(destinatario);

        destinatarios.setPropuestos(destinatariosPropuestos);
        destinatarios.setPosibles(destinatariosPosibles);*/


        return destinatarios;

    }

    @Override
    public Boolean enviarDestinatarios(RegistroEntrada registro,
        List<Destinatario> destinatariosDefinitivos, String observaciones,
        Locale lang) throws Exception {


       /* código de ejemplo
        log.info("OBSERVACIONES EN PLUGIN " + observaciones);
        log.info("NUMERO DE ANEXOS FULL" + registro.getRegistroDetalle().getAnexosFull().size());
        log.info("NUMERO DE ANEXOS " + registro.getRegistroDetalle().getAnexos().size());

        List<AnexoFull> anexosFull = registro.getRegistroDetalle().getAnexosFull();
        for (AnexoFull anexoFull : anexosFull) {
            log.info("TITULO " + anexoFull.getAnexo().getTitulo());
            log.info("DC " + anexoFull.getDocumentoCustody());
            log.info("SC " + anexoFull.getSignatureCustody());
        }

        List<Anexo> anexos = registro.getRegistroDetalle().getAnexos();
        for (Anexo anexo : anexos) {
            log.info("TITULO " + anexo.getTitulo());
        }
        if (destinatariosDefinitivos != null) {
            for (Destinatario destinatario : destinatariosDefinitivos) {
                log.info("DESTINATARIO ID EN PLUGIN " + destinatario.getId());
                log.info("DESTINATARIO NOMBRE EN PLUGIN " + destinatario.getName());
            }
        }
        return true;*/

        //La implementación Mock devuelve que ha ido bien el envio del registro
        return true;

    }

    @Override
    public ConfiguracionDistribucion configurarDistribucion() throws Exception {
        //Configuración por defecto de la implementación Mock
        ConfiguracionDistribucion cd = new ConfiguracionDistribucion(false, 2);
        return cd;

    }
}