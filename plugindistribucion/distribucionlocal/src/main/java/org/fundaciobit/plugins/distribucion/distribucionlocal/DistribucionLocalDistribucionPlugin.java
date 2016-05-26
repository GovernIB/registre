package org.fundaciobit.plugins.distribucion.distribucionlocal;


import es.caib.regweb3.model.RegistroEntrada;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.distribucion.ConfiguracionDistribucion;
import org.fundaciobit.plugins.distribucion.Destinatario;
import org.fundaciobit.plugins.distribucion.Destinatarios;
import org.fundaciobit.plugins.distribucion.IDistribucionPlugin;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author mgonzalez
 */
public class DistribucionLocalDistribucionPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = Logger.getLogger(getClass());

/*
    private static final String PROPERTY_BASE = DISTRIBUCION_BASE_PROPERTY + "distribucionlocal.";


	private static final String PROPERTY_EXEMPLE = PROPERTY_BASE +"exemple";

	public String getExample() throws Exception {
		//return getProperty(PROPERTY_EXEMPLE);

		return getPropertyRequired(PROPERTY_EXEMPLE);
	}*/

    /**
     *
     */
    public DistribucionLocalDistribucionPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public DistribucionLocalDistribucionPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public DistribucionLocalDistribucionPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }

    @Override
    public Destinatarios distribuir(RegistroEntrada registro) throws Exception {

        // Este código es una prueba, aquí se deben determinar los destinatarios reales en función de la
        // implementación particular de cada entidad
        log.info("Distribuir plugin");

        Destinatarios destinatarios = new Destinatarios();

        List<Destinatario> destinatariosPosibles = new ArrayList<Destinatario>();
        List<Destinatario> destinatariosPropuestos = new ArrayList<Destinatario>();
        Destinatario destinatario = new Destinatario();
        destinatario.setId("1");
        destinatario.setName("BANDEJA 1");
        destinatariosPosibles.add(destinatario);

        destinatario = new Destinatario();
        destinatario.setId("2");
        destinatario.setName("BANDEJA 2");
        destinatariosPosibles.add(destinatario);

        destinatario = new Destinatario();
        destinatario.setId("3");
        destinatario.setName("BANDEJA 3");
        destinatariosPropuestos.add(destinatario);


        destinatarios.setPropuestos(destinatariosPropuestos);
        destinatarios.setPosibles(destinatariosPosibles);


        return destinatarios;
    }

    @Override
    public Boolean enviarDestinatarios(RegistroEntrada registro, List<Destinatario> destinatariosDefinitivos, String observaciones) throws Exception {
        // Este código es una prueba, aquí se debe distribuir el registro al listado de destinatarios indicado.
       /* log.info("OBSERVACIONES EN PLUGIN " + observaciones);
        log.info("NUMERO DE ANEXOS " + registro.getRegistroDetalle().getAnexosFull().size());
        List<AnexoFull> anexosFull = registro.getRegistroDetalle().getAnexosFull();
        for (AnexoFull anexoFull : anexosFull) {
            log.info("TITULO " + anexoFull.getAnexo().getTitulo());
            log.info(anexoFull.getDocumentoCustody().getName());
        }
        if (destinatariosDefinitivos != null) {
            for (Destinatario destinatario : destinatariosDefinitivos) {
                log.info("DESTINATARIO ID EN PLUGIN " + destinatario.getId());
                log.info("DESTINATARIO NOMBRE EN PLUGIN " + destinatario.getName());
            }
        }*/
        return false;

    }


    public ConfiguracionDistribucion configurarDistribucion() throws Exception {
        ConfiguracionDistribucion cd = new ConfiguracionDistribucion(true, 3);
        return cd;

    }
}