package es.caib.regweb3.plugins.arxiu.caib;

import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.utils.AnexoFull;

public interface Regweb3PluginArxiu {

    /**
     * Crea un contenidor de contingut, per exemple un expedient
     * al gestor d'arxius remot
     *
     * @param registro
     *
     * @return L'identificador UUID del contenidor creat
     * @throws Exception
     *            Si hi ha hagut algun problema per dur a terme l'acció.
     */
    String crearJustificante(IRegistro registro, Long tipoRegistro, AnexoFull anexoFull) throws Exception;



}
