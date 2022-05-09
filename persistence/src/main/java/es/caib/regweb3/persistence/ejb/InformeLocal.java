package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 *         Date: 05/07/16
 */
@Local
@RolesAllowed({"RWE_ADMIN", "RWE_USUARI"})
public interface InformeLocal {

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
     * @throws Exception
     */
    List<RegistroEntrada> buscaRegistroEntradasOrganismo(Date fechaInicio, Date fechaFin, String numeroRegistroFormateado, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String usuario, String extracto, Long idOrganismo, Long estado, Long idOficina, String organoDest, Long idEntidad, Boolean mostraInteressats) throws Exception;

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
     * @throws Exception
     */
    List<RegistroSalida> buscaRegistroSalidasOrganismo(Date fechaInicio, Date fechaFin, String numRegistroFormateado, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String usuario, String extracto, Long idOrganismo, Long estado, Long idOficina, String organoOrig, Long idEntidad, Boolean mostraInteressats) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long buscaIndicadoresEntradaTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;
    
    /**
     * Busca los Registros SIR enviados en función de la una fecha inicio, una fecha fin, de una entidad
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long buscaIndicadoresSirEnviadosTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;
    
    /**
     * Busca los Registros SIR enviados en función de la una fecha inicio, una fecha fin, de una entidad
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long buscaIndicadoresSirRecibidosTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long buscaIndicadoresSalidaTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de una Oficina
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long buscaIndicadoresOficinaTotalEntrada(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de una Oficina
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long buscaIndicadoresOficinaTotalSalida(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception;
    
    /**
     * Busca los Registros SIR enviados en función de la una fecha inicio, una fecha fin, de una Oficina
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long buscaIndicadoresOficinaTotalSirEnviados(Date fechaInicio, Date fechaFin, String codiOficina) throws Exception;
    
    /**
     * Busca los Registros SIR recibidos en función de la una fecha inicio, una fecha fin, de una Oficina
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long buscaIndicadoresOficinaTotalSirRecibidos(Date fechaInicio, Date fechaFin, String codiOficina) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Conselleria
     *
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws Exception
     */
    Long buscaEntradaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Conselleria
     *
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws Exception
     */
    Long buscaSalidaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception;
    
    /**
     * Busca los Registros SIR enviados en función de la una fecha inicio, una fecha fin, por Conselleria
     *
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws Exception
     */
    Long buscaSirEnviadosPorConselleria(Date fechaInicio, Date fechaFin, String codConselleria) throws Exception;
    
    /**
     * Busca los Registros SIR recibidos en función de la una fecha inicio, una fecha fin, por Conselleria
     *
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws Exception
     */
    Long buscaSirRecibidosPorConselleria(Date fechaInicio, Date fechaFin, String codConselleria) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Tipos Asunto, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoAsunto
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long buscaEntradaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Tipo de Asunto, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param tipoAsunto
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long buscaSalidaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long buscaEntradaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long buscaEntradaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Libros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param libro
     * @return
     * @throws Exception
     */
    Long buscaEntradaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Oficinas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param oficina
     * @return
     * @throws Exception
     */
    Long buscaEntradaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long buscaSalidaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long buscaSalidaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Libros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param libro
     * @return
     * @throws Exception
     */
    Long buscaSalidaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Oficinas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param oficina
     * @return
     * @throws Exception
     */
    Long buscaSalidaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception;

}