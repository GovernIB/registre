package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 05/07/16
 */
@Local
public interface InformeLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/InformeEJB";


    /**
     * Busca los Registros de Entrada en función de varios parámetros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param numeroRegistroFormateado
     * @param interesadoNom
     * @param interesadoLli1
     * @param interesadoLli2
     * @param interesadoDoc
     * @param anexos
     * @param observaciones
     * @param usuario
     * @param extracto
     * @param idOrganismo
     * @param estado
     * @param idOficina
     * @param organoDest
     * @param mostraInteressats
     * @return
     * @throws I18NException
     */
    List<RegistroEntrada> buscaRegistroEntradasOrganismo(Date fechaInicio, Date fechaFin, String numeroRegistroFormateado, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String usuario, String extracto, Long idOrganismo, Long estado, Long idOficina, String organoDest, Long idEntidad, Boolean mostraInteressats) throws I18NException;

    /**
     * Busca los Registros de Salida en función de varios parámetros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param numRegistroFormateado
     * @param interesadoNom
     * @param interesadoLli1
     * @param interesadoLli2
     * @param interesadoDoc
     * @param anexos
     * @param observaciones
     * @param usuario
     * @param extracto
     * @param idORganismo
     * @param estado
     * @param idOficina
     * @param organoOrig
     * @return
     * @throws I18NException
     */
    List<RegistroSalida> buscaRegistroSalidasOrganismo(Date fechaInicio, Date fechaFin, String numRegistroFormateado, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String usuario, String extracto, Long idOrganismo, Long estado, Long idOficina, String organoOrig, Long idEntidad, Boolean mostraInteressats) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long buscaIndicadoresEntradaTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long buscaIndicadoresSalidaTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de una Oficina
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Long buscaIndicadoresOficinaTotalEntrada(Date fechaInicio, Date fechaFin, Long idOficina) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de una Oficina
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Long buscaIndicadoresOficinaTotalSalida(Date fechaInicio, Date fechaFin, Long idOficina) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Conselleria
     *
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws I18NException
     */
    Long buscaEntradaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Conselleria
     *
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws I18NException
     */
    Long buscaSalidaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Tipos Asunto, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoAsunto
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long buscaEntradaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Tipo de Asunto, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoAsunto
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long buscaSalidaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long buscaEntradaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Long buscaEntradaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Libros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param libro
     * @return
     * @throws I18NException
     */
    Long buscaEntradaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Oficinas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param oficina
     * @return
     * @throws I18NException
     */
    Long buscaEntradaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Long buscaSalidaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long buscaSalidaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Libros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param libro
     * @return
     * @throws I18NException
     */
    Long buscaSalidaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Oficinas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param oficina
     * @return
     * @throws I18NException
     */
    Long buscaSalidaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws I18NException;

}