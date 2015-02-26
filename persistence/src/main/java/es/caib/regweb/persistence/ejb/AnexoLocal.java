package es.caib.regweb.persistence.ejb;


import es.caib.regweb.model.Anexo;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface AnexoLocal extends BaseEjb<Anexo, Long> {


  /**
   *  Eliminar un anexo
   * @param idAnexo
   * @param idRegistroDetalle
   * @return
   * @throws Exception
   */
     public boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle) throws Exception;

    /**
      *
      * @param anexo
      * @return
      * @throws Exception
      */
    public boolean actualizarAnexo(Anexo anexo) throws Exception;

  /**
   * Actualiza un anexo y guarda sus archivos asociados
   * @param idAnexo
   * @param ficheroAnexado
   * @param nombreFicheroAnexado
   * @param tipoMIMEFicheroAnexado
   * @param tamanoFicheroAnexado
   * @param firmaAnexada
   * @param nombreFirmaAnexada
   * @param tipoMIMEFirmaAnexada
   * @param tamanoFirmaAnexada
   * @param modoFirma
   * @param fechaCaptura
   * @return
   * @throws Exception
   */
    public Anexo actualizarAnexoConArchivos(Long idAnexo, byte[] ficheroAnexado,String nombreFicheroAnexado, String tipoMIMEFicheroAnexado, Long tamanoFicheroAnexado,
                               byte[] firmaAnexada, String nombreFirmaAnexada, String tipoMIMEFirmaAnexada, Long tamanoFirmaAnexada,
                               Integer modoFirma, Date fechaCaptura ) throws Exception;


    /**
     *
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroEntrada(Long idRegistro) throws Exception;

    /**
     *
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroSalida(Long idRegistro) throws Exception;

    /**
     *  Obtiene los anexos de un registroDetalle
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroDetalle(Long idRegistroDetalle) throws Exception;

}
