package org.fundaciobit.plugins.postproceso.postprocesolocal;


import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.postproceso.IPostProcesoPlugin;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import java.util.Properties;

/**
 * @author mgonzalez
 */
public class PostProcesoLocalPostProcesoPlugin extends AbstractPluginProperties implements IPostProcesoPlugin {

    protected final Logger log = Logger.getLogger(getClass());


    /**
     *
     */
    public PostProcesoLocalPostProcesoPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public PostProcesoLocalPostProcesoPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public PostProcesoLocalPostProcesoPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }


    /**
     * crear un registro nuevo
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    @Override
    public void nuevoRegistroEntrada(RegistroEntrada registroEntrada) throws Exception{
        log.info("Nuevo registro entrada");
        log.info("NUMERO: " +registroEntrada.getNumeroRegistroFormateado());
        if(registroEntrada.getRegistroDetalle()!=null){
            log.info("INTERESADOS: " +registroEntrada.getRegistroDetalle().getInteresados().size());
            log.info("ANEXOS: " +registroEntrada.getRegistroDetalle().getAnexos().size());
        }



    }

    /**
     * crear un registro nuevo
     * @param registroSalida
     * @return
     * @throws Exception
     */
    @Override
    public void nuevoRegistroSalida(RegistroSalida registroSalida) throws Exception{
        log.info("Nuevo registro salida");
        log.info("NUMERO: " +registroSalida.getNumeroRegistroFormateado());

    //    log.info("INTERESADOS "+ registroSalida.getRegistroDetalle().getInteresados().size());

    }


    /**
     * actualizar un registro
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    @Override
    public void actualizarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception{
        log.info("actualizar registro entrada");
        log.info("NUMERO: " +registroEntrada.getNumeroRegistroFormateado());
        log.info("INTERESADOS: " +registroEntrada.getRegistroDetalle().getInteresados().size());
        log.info("ANEXOS: " +registroEntrada.getNumeroRegistroFormateado());
        //Ojo no vienen los interesados

    }

    /**
     * actualizar un registro
     * @param registroSalida
     * @return
     * @throws Exception
     */
    @Override
    public void actualizarRegistroSalida(RegistroSalida registroSalida) throws Exception{
        log.info("Actualizar registro salida");
        log.info("NUMERO: " +registroSalida.getNumeroRegistroFormateado());


    }



    /**
     * nuevo interesado
     * @param interesado
     * @param numeroEntrada
     * @return
     * @throws Exception
     */
    @Override
    public void nuevoInteresadoEntrada(Interesado interesado, String numeroEntrada) throws Exception{
        log.info("nuevo interesado entrada");
        log.info("Nombre Interesado :" + interesado.getNombreCompleto());

    }


    /**
     * nuevo interesado
     * @param interesado
     * @param numeroSalida
     * @return
     * @throws Exception
     */
    @Override
    public void nuevoInteresadoSalida(Interesado interesado, String numeroSalida) throws Exception{
        log.info("nuevo interesado salida");
        log.info("Nombre Interesado :" + interesado.getNombreCompleto());

    }

    /**
     * actualizar interesado
     * @param interesado
     * @param numeroEntrada
     * @return
     * @throws Exception
     */
    @Override
    public void actualizarInteresadoEntrada(Interesado interesado, String numeroEntrada) throws Exception{
        log.info("actualizar interesado entrada");
        log.info("Nombre Interesado :" + interesado.getNombreCompleto());

    }

    /**
     * actualizar interesado
     * @param interesado
     * @param numeroSalida
     * @return
     * @throws Exception
     */
    @Override
    public void actualizarInteresadoSalida(Interesado interesado, String numeroSalida) throws Exception{
        log.info("actualizar interesado salida");
        log.info("Nombre Interesado :" + interesado.getNombreCompleto());

    }

    /**
     * eliminar interesado
     * @param idInteresado
     * @param numeroEntrada
     * @return
     * @throws Exception
     */
    @Override
    public void eliminarInteresadoEntrada(Long idInteresado, String numeroEntrada) throws Exception{
        log.info("Eliminar interesado entrada " + idInteresado);


    }

    /**
     * eliminar interesado
     * @param idInteresado
     * @param numeroSalida
     * @return
     * @throws Exception
     */
    @Override
    public void eliminarInteresadoSalida(Long idInteresado, String numeroSalida) throws Exception{
        log.info("Eliminar interesado salida " + idInteresado);

    }



}