package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
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
     * @param numRegistro
     * @param interesadoNom
     * @param interesadoLli1
     * @param interesadoLli2
     * @param interesadoDoc
     * @param anexos
     * @param observaciones
     * @param usuario
     * @param extracto
     * @param libros
     * @param estado
     * @param idOficina
     * @param idTipoAsunto
     * @param organoDest
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaLibroRegistroEntradas(Date fechaInicio, Date fechaFin, String numRegistro, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String usuario, String extracto, List<Libro> libros, Long estado, Long idOficina, Long idTipoAsunto, String organoDest, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Salida en función de varios parámetros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param numRegistro
     * @param interesadoNom
     * @param interesadoLli1
     * @param interesadoLli2
     * @param interesadoDoc
     * @param anexos
     * @param observaciones
     * @param usuario
     * @param extracto
     * @param libros
     * @param estado
     * @param idOficina
     * @param idTipoAsunto
     * @param organoOrig
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> buscaLibroRegistroSalidas(Date fechaInicio, Date fechaFin, String numRegistro, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String usuario, String extracto, List<Libro> libros, Long estado, Long idOficina, Long idTipoAsunto, String organoOrig, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long buscaIndicadoresEntradaTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long buscaIndicadoresSalidaTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén pendientes en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaEntradaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Salida que no estén pendientes en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> buscaSalidaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin, por Usuario en los Libros gestionados
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaEntradaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> buscaSalidaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin, Libro y Número de registro
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idLibro
     * @param numeroRegistro
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> buscaEntradasPorLibroTipoNumero(Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception;

    /**
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin, Libro y Número de registro
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idLibro
     * @param numeroRegistro
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> buscaSalidasPorLibroTipoNumero(Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de una Oficina
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    public Long buscaIndicadoresOficinaTotalEntrada(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de una Oficina
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    public Long buscaIndicadoresOficinaTotalSalida(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Conselleria
     *
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Conselleria
     *
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception;

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
    public Long buscaEntradaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception;

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
    public Long buscaSalidaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception;

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
    public Long buscaEntradaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception;

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
    public Long buscaEntradaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Libros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param libro
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Oficinas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param oficina
     * @return
     * @throws Exception
     */
    public Long buscaEntradaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception;

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
    public Long buscaSalidaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception;

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
    public Long buscaSalidaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Libros
     *
     * @param fechaInicio
     * @param fechaFin
     * @param libro
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Oficinas
     *
     * @param fechaInicio
     * @param fechaFin
     * @param oficina
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception;

}