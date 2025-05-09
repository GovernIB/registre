package es.caib.regweb3.persistence.ejb;

import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;

import es.caib.regweb3.model.Remesa;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Local
public interface PublicLocal {

	public Remesa getByIdentificadorAndReferencia(String identificador, String referencia);

	public void notificacionActualitzarEstado(String identificadorNotib, String referenciaEnviament, Remesa remesa)
			throws Exception, I18NException;
	
}
