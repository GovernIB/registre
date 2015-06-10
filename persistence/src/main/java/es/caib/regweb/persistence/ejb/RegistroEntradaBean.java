package es.caib.regweb.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb.model.*;
import es.caib.regweb.model.utils.RegistroBasico;
import es.caib.regweb.persistence.utils.*;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RegistroEntradaEJB")
@SecurityDomain("seycon")
public class RegistroEntradaBean extends RegistroEntradaCambiarEstadoBean
   implements RegistroEntradaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;

    @EJB(name = "LibroEJB")
    public LibroLocal libroEjb;

    @EJB(name = "ContadorEJB")
    public ContadorLocal contadorEjb;

    @EJB(name = "OficinaEJB")
    public OficinaLocal oficinaEjb;

    @EJB(name = "HistoricoRegistroEntradaEJB")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb/AnexoEJB/local")
    public AnexoLocal anexoEjb;



    @Override
    public List<RegistroEntrada> getByUsuario(Long idUsuarioEntidad) throws Exception{

        Query q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return q.getResultList();
    }
    
    
    
    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada)
        throws Exception, I18NException, I18NValidationException {
      return registrarEntrada(registroEntrada,null, null);
    }
    
    

    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
        UsuarioEntidad usuarioEntidad, List<AnexoFull> anexosFull) 
            throws Exception, I18NException, I18NValidationException {


        // Obtenemos el Número de registro
        Libro libro = libroEjb.findById(registroEntrada.getLibro().getId());
        NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorEntrada().getId());
        registroEntrada.setNumeroRegistro(numeroRegistro.getNumero());
        registroEntrada.setFecha(numeroRegistro.getFecha());

        if(registroEntrada.getLibro().getCodigo() != null && registroEntrada.getOficina().getCodigo() != null){
            registroEntrada.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroEntrada, registroEntrada.getLibro(), registroEntrada.getOficina()));
        }else {
            registroEntrada.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroEntrada, libroEjb.findById(registroEntrada.getLibro().getId()), oficinaEjb.findById(registroEntrada.getOficina().getId())));
        }

        // Si no ha introducido ninguna fecha de Origen
        if(registroEntrada.getRegistroDetalle().getFechaOrigen() == null){
            registroEntrada.getRegistroDetalle().setFechaOrigen(registroEntrada.getFecha());
        }

        List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
        if (interesados != null && interesados.size() != 0) {
          for (Interesado interesado : interesados) {
            interesado.setRegistroDetalle(registroEntrada.getRegistroDetalle());
          }
        }

        registroEntrada = persist(registroEntrada);

        //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
        if(StringUtils.isEmpty(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen())){

            registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());

            registroEntrada = merge(registroEntrada);
        }

        // TODO Controlar custodyID y si hay fallo borrar todos los Custody
        if (anexosFull != null && anexosFull.size() != 0) {
          final Long registroID = registroEntrada.getId();
          for (AnexoFull anexoFull : anexosFull) {
            
            anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());
            anexoFull = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "entrada");
            
          }
        }

        return registroEntrada;

    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, List<Libro> libros, String interesadoNom, String interesadoDoc, String organoDest, Boolean anexos) throws Exception{

         Query q;
         Query q2;
         // Query qInteressats;
         Map<String, Object> parametros = new HashMap<String, Object>();
         List<String> where = new ArrayList<String>();

         StringBuffer query = new StringBuffer("Select DISTINCT registroEntrada from RegistroEntrada as registroEntrada left join registroEntrada.registroDetalle.interesados interessat ");
         									 //+ ", registroDetalle from RegistroDetalle as registroDetalle "
        		 							 //+ ", interesado from Interesado as interesado ");

         if(registroEntrada.getNumeroRegistroFormateado()!= null && registroEntrada.getNumeroRegistroFormateado().length() > 0){
        	 where.add(" registroEntrada.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
        	 parametros.put("numeroRegistroFormateado", "%"+registroEntrada.getNumeroRegistroFormateado()+"%");
         }

         if(registroEntrada.getRegistroDetalle().getExtracto() != null && registroEntrada.getRegistroDetalle().getExtracto().length() > 0){
        	 where.add(DataBaseUtils.like("registroEntrada.registroDetalle.extracto","extracto",parametros,registroEntrada.getRegistroDetalle().getExtracto()));
         }

         //where.add(" (registroEntrada.registroDetalle.id = registroDetalle.id) ");
         //where.add(" (registroDetalle.interesados.id = interesado.id) ");

         //Filtros para interesado y organo destinatario
         boolean filtramosInteresado = false;
         if (interesadoNom!=null && !"".equals(interesadoNom) && !"null".equalsIgnoreCase(interesadoNom)) {
        	 where.add(" ((UPPER(interessat.nombre)||' '||UPPER(interessat.apellido1)||' '||UPPER(interessat.apellido2) LIKE UPPER(:interesadoNom)) or"
        	 		+  " (UPPER(interessat.razonSocial) LIKE UPPER(:interesadoNom)) ) "); //or (UPPER(registroEntrada.destino.denominacion) LIKE UPPER(:interesadoNom))
        	 parametros.put("interesadoNom", "%"+interesadoNom.trim()+"%");
        	 filtramosInteresado=true;
         }
         boolean filtramosDoc = false;
         if (interesadoDoc!=null && !"".equals(interesadoDoc) && !"null".equalsIgnoreCase(interesadoDoc)) {
        	 where.add(" (UPPER(interessat.documento) LIKE UPPER(:interesadoDoc)) ");
        	 parametros.put("interesadoDoc", "%"+interesadoDoc.trim()+"%");
        	 filtramosDoc = true;
         }
         
         /*if (organoDest!=null && !"".equals(organoDest) && !"null".equalsIgnoreCase(organoDest)) {
       		 where.add(" ( (registroEntrada.destinoExternoCodigo = :organoDest) or (registroEntrada.destino.codigo = :organoDest) ) ");
       		 parametros.put("organoDest",organoDest);
         }*/

        // Estado registro
         if(registroEntrada.getEstado() != null && registroEntrada.getEstado() > 0) {
           where.add(" registroEntrada.estado = :idEstadoRegistro ");
           parametros.put("idEstadoRegistro",registroEntrada.getEstado());
         }

        // Oficina Registro
        if(registroEntrada.getOficina().getId() != null && registroEntrada.getOficina().getId() > 0) {
            where.add(" registroEntrada.oficina.id = :idOficina ");
            parametros.put("idOficina",registroEntrada.getOficina().getId());
        }

//         if (filtramosInteresado && filtramosDoc) {
//	         //Forzamos a que el Nombre y Documento del interesado introducidos, pertenezcan al mismo interesado
//	         where.add(" ("
//	         			+ "interessat.id in "
//	         				+ "(SELECT intere.id from Interesado as intere "
//	         					+ "WHERE UPPER(intere.documento) = UPPER(:interesadoDoc) "
//	         					+ "AND ("
//	         						+ "(UPPER(intere.nombre)||' '||UPPER(intere.apellido1)||' '||UPPER(intere.apellido2) LIKE UPPER(:interesadoNom)) "
//	         						+ "OR (UPPER(intere.razonSocial) LIKE UPPER(:interesadoNom))"
//	         					+ " )"
//	         				+ ")"
//	         			+ ") ");
//         }
         
        where.add(" (registroEntrada.fecha >= :fechaInicio  ");parametros.put("fechaInicio", fechaInicio);
        where.add(" registroEntrada.fecha <= :fechaFin) ");parametros.put("fechaFin", fechaFin);

         // Comprobamos si la búsqueda es sobre un libro en concreto o sobre todos a los que tiene acceso el usuario.
         if(registroEntrada.getLibro().getId() != null && registroEntrada.getLibro().getId() > 0){
            where.add(" registroEntrada.libro.id = :idLibro"); parametros.put("idLibro",registroEntrada.getLibro().getId());
         }else{
            where.add(" registroEntrada.libro in (:libros)"); parametros.put("libros",libros);
         }

        // Buscamos registros de entrada con anexos
        if(anexos){
            where.add(" registroEntrada.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
        }

         if (parametros.size() != 0) {

             query.append("where ");
             int count = 0;
             for (String w : where) {
                 if (count != 0) {
                   query.append(" and ");
                 }
                 query.append(w);
                 count++;
             }
             
             System.out.println(query);
             
             q2 = em.createQuery(query.toString().replaceAll("Select DISTINCT registroEntrada from RegistroEntrada as registroEntrada ", "Select DISTINCT count(registroEntrada.id) from RegistroEntrada as registroEntrada "));
             query.append(" order by registroEntrada.id desc");
             q = em.createQuery(query.toString());

             for (Map.Entry<String, Object> param : parametros.entrySet()) {

                 q.setParameter(param.getKey(), param.getValue());
                 q2.setParameter(param.getKey(), param.getValue());
             }

         }else{
             q2 = em.createQuery(query.toString().replaceAll("Select DISTINCT registroEntrada from RegistroEntrada as registroEntrada ", "Select DISTINCT count(registroEntrada.id) from RegistroEntrada as registroEntrada "));
             query.append("order by registroEntrada.id desc");
             q = em.createQuery(query.toString());
         }
         
         
//         StringBuffer query_ints = new StringBuffer("SELECT intere from Interesado as intere "
//					+ "WHERE UPPER(intere.documento) = UPPER("+interesadoDoc+") "
//					+ "AND "
//						+ "((UPPER(intere.nombre)||' '||UPPER(intere.apellido1)||' '||UPPER(intere.apellido2) LIKE UPPER("+interesadoNom+")) "
//						+ "OR (UPPER(intere.razonSocial) LIKE UPPER("+interesadoNom+")))");
//         
//         qInteressats = em.createQuery(query_ints.toString());
         
         //List<Interesado> ints =  qInteressats.getResultList();
         
         Paginacion paginacion = null;
         List<Object> resultadosPrincipal = q.getResultList();

         if (filtramosInteresado && filtramosDoc) {
        	 for (int r=resultadosPrincipal.size()-1; r>=0; r--) {
        		 RegistroEntrada re = (RegistroEntrada)resultadosPrincipal.get(r);
        		 if (!registreEntradaConteInteressat(re, interesadoNom, interesadoDoc)) {
        			 resultadosPrincipal.remove(r);
        		 }
        	 }
         }
         
         if (organoDest!=null && !"".equals(organoDest) && !"null".equalsIgnoreCase(organoDest)) {
        	 for (int r=resultadosPrincipal.size()-1; r>=0; r--) {
        		 RegistroEntrada re = (RegistroEntrada)resultadosPrincipal.get(r);
        		 if (!registreEntradaEsDeOrganDesti(re, organoDest)) {
        			 resultadosPrincipal.remove(r);
        		 }
        	 }
         }
         
         if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
             int total = resultadosPrincipal.size();//(Long)q2.getSingleResult();
             paginacion = new Paginacion(total, pageNumber);
             int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
             q.setFirstResult(inicio);
             q.setMaxResults(RESULTADOS_PAGINACION);
         }else{
             paginacion = new Paginacion(0, 0);
         }

         paginacion.setListado(resultadosPrincipal);

         return paginacion;
     }
    
    private boolean registreEntradaConteInteressat(RegistroEntrada ri, String int_nom, String int_doc) {
  		 for (int i=0; i<ri.getRegistroDetalle().getInteresados().size(); i++) {
			 Interesado interesat = ri.getRegistroDetalle().getInteresados().get(i);
			 String nomComplet = "";
			 
			 if (interesat.getNombre()!=null && !"".equals(interesat.getNombre()) && !" ".equals(interesat.getNombre()) && !"null".equalsIgnoreCase(interesat.getNombre())) {
				 nomComplet = interesat.getNombre() +" "+ interesat.getApellido1();
				 if (interesat.getApellido2()!=null && !"".equals(interesat.getApellido2()) && !" ".equals(interesat.getApellido2()) && !"null".equalsIgnoreCase(interesat.getApellido2())) {
					 nomComplet += " "+ interesat.getApellido2();
				 }				 
			 }else{
				 nomComplet = interesat.getRazonSocial();
			 }

			 if (nomComplet.trim().toUpperCase().contains(int_nom.trim().toUpperCase()) && interesat.getDocumento().trim().toUpperCase().contains(int_doc.trim().toUpperCase())) {
				 return true;
			 }
		 }
  		 return false;
    }
    
    private boolean registreEntradaEsDeOrganDesti(RegistroEntrada ri, String org_cod) {
    	
    	if (org_cod.equals(ri.getDestinoExternoCodigo())) {
    		return true;
    	}
    	
    	if (ri.getDestino()!=null && org_cod.equals(ri.getDestino().getCodigo())) {
    		return true;
    	}
    	
    	return false;
    }

    public List<String> oficiosPendientesRemisionInterna(Libro libro) throws Exception{
        // Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar

        Query q1;
        q1 = em.createQuery("Select distinct(re.destino.denominacion) from RegistroEntrada as re where " +
                "re.estado = :idEstadoRegistro and re.libro.id = :idLibro and " +
                "re.destino != null and " +
                "re.oficina.organismoResponsable.id != re.destino.id and " +
                "re.oficina.organismoResponsable.id != re.destino.organismoSuperior.id and " +
                "re.destino.id not in (select rso.organismo.id from RelacionOrganizativaOfi as rso where rso.oficina.id = re.oficina.id and rso.estado.codigoEstadoEntidad='V') and " +
                "re.destino.organismoSuperior.id not in (select rso.organismo.id from RelacionOrganizativaOfi as rso where rso.oficina.id = re.oficina.id and rso.estado.codigoEstadoEntidad='V') and " +
                "re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra)");

        q1.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);
        q1.setParameter("idLibro", libro.getId());

        return q1.getResultList();

    }

    @Override
    public List<OficiosRemisionOrganismo> oficiosPendientesRemisionInterna(Integer any, Libro libro) throws Exception{

        // Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar

        Query q1;
        q1 = em.createQuery("Select distinct(re.destino.id) from RegistroEntrada as re where " +
                "re.estado = :idEstadoRegistro and year(re.fecha) = :any and re.libro.id = :idLibro and " +
                "re.destino != null and " +
                "re.oficina.organismoResponsable.id != re.destino.id and " +
                "re.oficina.organismoResponsable.id != re.destino.organismoSuperior.id and " +
                "re.destino.id not in (select rso.organismo.id from RelacionOrganizativaOfi as rso where rso.oficina.id = re.oficina.id and rso.estado.codigoEstadoEntidad='V') and " +
                "re.destino.organismoSuperior.id not in (select rso.organismo.id from RelacionOrganizativaOfi as rso where rso.oficina.id = re.oficina.id and rso.estado.codigoEstadoEntidad='V') and " +
                "re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra)");

        q1.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);
        q1.setParameter("any", any);
        q1.setParameter("idLibro", libro.getId());

        List<Long> organismosPropios =  q1.getResultList();

        log.info("organismosPropios: " + organismosPropios.size());


        // Buscamos los RegistroEntrada pendientes de tramitar de cada uno de los Organismos encontrados
        List<OficiosRemisionOrganismo> oficiosRemisionOrganismo = new ArrayList<OficiosRemisionOrganismo>();

        // Por cada organismo Propio, buscamos sus RegistrosEntrada
        for (Long organismo : organismosPropios) {

            OficiosRemisionOrganismo  oficios = new OficiosRemisionOrganismo();

            //Buscamos los Registros de Entrada, pendientes de tramitar mediante un Oficio de Remision
            oficios.setOficiosRemision(oficiosRemisionByOrganismoPropio(organismo, any, libro.getId()));

            //Los añadimos a la lista
            oficiosRemisionOrganismo.add(oficios);

        }


        return oficiosRemisionOrganismo;

    }

    public Boolean isOficioRemisionInterno(Long idRegistro) throws Exception{
        Query q;
        q = em.createQuery("Select re.id from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.estado = :idEstadoRegistro and " +
                "re.destino != null and " +
                "re.oficina.organismoResponsable.id != re.destino.id and " +
                "re.oficina.organismoResponsable.id != re.destino.organismoSuperior.id and " +
                "re.destino.id not in (select rso.organismo.id from RelacionOrganizativaOfi as rso where rso.oficina.id = re.oficina.id and rso.estado.codigoEstadoEntidad='V') and " +
                "re.destino.organismoSuperior.id not in (select rso.organismo.id from RelacionOrganizativaOfi as rso where rso.oficina.id = re.oficina.id and rso.estado.codigoEstadoEntidad='V') and " +
                "re.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra)");

        q.setParameter("idRegistro", idRegistro);
        q.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);

        return q.getResultList().size() > 0;
    }

    public List<String> oficiosPendientesRemisionExterna(Libro libro) throws Exception{

        // Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar

        Query q1;
        q1 = em.createQuery("Select distinct(registroEntrada.destinoExternoDenominacion) from RegistroEntrada as registroEntrada where " +
                "registroEntrada.estado = :idEstadoRegistro and registroEntrada.libro.id = :idLibro and " +
                "registroEntrada.destino is null and " +
                "registroEntrada.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra)");

        q1.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);
        q1.setParameter("idLibro", libro.getId());

        return q1.getResultList();
    }

    public List<OficiosRemisionOrganismo> oficiosPendientesRemisionExterna(Integer any, Libro libro, Entidad entidadActiva) throws Exception{

        // Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar

        Query q1;
        q1 = em.createQuery("Select distinct(registroEntrada.destinoExternoCodigo) from RegistroEntrada as registroEntrada where " +
                "registroEntrada.estado = :idEstadoRegistro and year(registroEntrada.fecha) = :any and registroEntrada.libro.id = :idLibro and " +
                "registroEntrada.destino is null and " +
                "registroEntrada.id not in (select tra.registroEntradaOrigen.id from Trazabilidad as tra)");

        q1.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);
        q1.setParameter("any", any);
        q1.setParameter("idLibro", libro.getId());

        List<String> organismosExternos =  q1.getResultList();

        // Buscamos los RegistroEntrada pendientes de tramitar de cada uno de los Organismos encontrados
        List<OficiosRemisionOrganismo> oficiosRemisionOrganismo = new ArrayList<OficiosRemisionOrganismo>();


        // Por cada organismo Externo, buscamos sus RegistrosEntrada
        for (String organismo : organismosExternos) {

            OficiosRemisionOrganismo  oficios = new OficiosRemisionOrganismo();

            // Consulta en base de datos si la Entidad Actual está en SIR
            //Entidad entidadActual = entidadEjb.findById(entidadActiva.getId());
            Entidad entidadActual = (Entidad) em.createQuery("select e from Entidad as e where e.id = :id").setParameter("id", entidadActiva.getId()).getSingleResult();
            if(entidadActual.getSir()) {

                // Averiguamos si el Organismos Externo está en Sir o no
                Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
                if (!organismosExternos.isEmpty()) {
                    List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(organismo);
                    if (oficinasSIR.size() > 0) {
                        oficios.setSir(true);
                        oficios.setOficinasSIR(oficinasSIR);
                    }
                }
            }
            //Buscamos los Registros de Entrada, pendientes de tramitar mediante un Oficio de Remision
            oficios.setOficiosRemision(oficiosRemisionByOrganismoExterno(organismo, any, libro.getId()));

            //Los añadimos a la lista
            oficiosRemisionOrganismo.add(oficios);

        }

        return oficiosRemisionOrganismo;

    }

    public List<RegistroEntrada> oficiosRemisionByOrganismoPropio(Long idOrganismo, Integer any, Long idLibro) throws Exception{

        Query q;

        q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.libro.id = :idLibro " +
                "and registroEntrada.destino.id = :idOrganismo and year(registroEntrada.fecha) = :any " +
                "and registroEntrada.estado = :idEstadoRegistro " +
                "order by registroEntrada.fecha desc");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("any", any);
        q.setParameter("idLibro", idLibro);
        q.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);


        return q.getResultList();
    }

    public List<RegistroEntrada> oficiosRemisionByOrganismoExterno(String codigoOrganismo, Integer any, Long idLibro) throws Exception{

        Query q;

        q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.libro.id = :idLibro " +
                "and registroEntrada.destinoExternoCodigo = :codigoOrganismo and year(registroEntrada.fecha) = :any " +
                "and registroEntrada.estado = :idEstadoRegistro " +
                "order by registroEntrada.fecha desc");

        q.setParameter("codigoOrganismo", codigoOrganismo);
        q.setParameter("any", any);
        q.setParameter("idLibro", idLibro);
        q.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);


        return q.getResultList();
    }

    @Override
    public List<RegistroEntrada> buscaLibroRegistro(Date fechaInicio, Date fechaFin, List<Libro> libros) throws Exception{

        Query q;

        q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where (registroEntrada.fecha >= :fechaInicio " +
                    "and registroEntrada.fecha <= :fechaFin) and registroEntrada.libro in (:libros) order by registroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public List<RegistroEntrada> buscaIndicadores(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception{

        Query q;

        q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente and " +
                "registroEntrada.libro.organismo.entidad.id = :idEntidad order by registroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return q.getResultList();
    }

    @Override
    public Long buscaEntradaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.oficina.organismoResponsable.id = :conselleria and registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("conselleria", conselleria);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.registroDetalle.tipoAsunto.id = :tipoAsunto and " +
                "registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente and registroEntrada.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("tipoAsunto", tipoAsunto);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.registroDetalle.idioma = :idioma and " +
                "registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente and registroEntrada.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.libro.id = :libro and registroEntrada.estado != :anulado  and registroEntrada.estado != :pendiente");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("libro", libro);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception{

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.oficina.id = :oficina and registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("oficina", oficina);
        q.setParameter("anulado",RegwebConstantes.ESTADO_ANULADO);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<RegistroBasico> getByOficinaEstado(Long idOficinaActiva, Long idEstado, Integer total) throws Exception {

        Query q;

        String s = "re.registroDetalle.extracto ";

        if(idEstado.equals(RegwebConstantes.ESTADO_PENDIENTE)){
            s = "re.registroDetalle.reserva ";
        }

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, " + s +
                "from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);

       return getRegistroBasicoList(q.getResultList());

    }

    @Override
    public List<RegistroEntrada> buscaEntradaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception{

        Query q;

        q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.usuario.id = :idUsuario and registroEntrada.libro in (:libros) order by registroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public List<RegistroEntrada> buscaEntradaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception{

        Query q;

        q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.usuario.id = :idUsuario and registroEntrada.libro.id = :idLibro and registroEntrada.estado != :pendiente order by registroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("pendiente",RegwebConstantes.ESTADO_PENDIENTE);

        return q.getResultList();
    }

    @Override
    public List<RegistroBasico> getByLibrosEstado(List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, re.registroDetalle.extracto " +
                "from RegistroEntrada as re where re.libro in (:libros) " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        return  getRegistroBasicoList(q.getResultList());

    }

    @Override
    public List<RegistroEntrada> buscaPorLibroTipoNumero(Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception{

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroEntrada from RegistroEntrada as registroEntrada ");

        if(fechaInicio != null){where.add(" registroEntrada.fecha >= :fechaInicio"); parametros.put("fechaInicio",fechaInicio);}
        if(fechaFin != null){where.add(" registroEntrada.fecha <= :fechaFin"); parametros.put("fechaFin",fechaFin);}
        if(idLibro != null && idLibro > 0){where.add(" registroEntrada.libro.id = :idLibro"); parametros.put("idLibro",idLibro);}
        if(numeroRegistro != null && numeroRegistro > 0){where.add(" registroEntrada.numeroRegistro = :numeroRegistro"); parametros.put("numeroRegistro",numeroRegistro);}

        query.append("where ");
        int count = 0;
        for (String w : where) {
            if (count != 0) {
                query.append(" and ");
            }
            query.append(w);
            count++;
        }
        query.append(" order by registroEntrada.id desc");
        q = em.createQuery(query.toString());

        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
        }

        return q.getResultList();
    }

    @Override
    public void cambiarEstado(Long idRegistro, Long idEstado) throws Exception{
        RegistroEntrada registroEntrada = findById(idRegistro);
        registroEntrada.setEstado(idEstado);
        merge(registroEntrada);
    }

    public List<RegistroBasico> getUltimosRegistros(Long idOficina, Integer total) throws Exception{

        Query q;

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, re.registroDetalle.extracto " +
                "from RegistroEntrada as re where re.oficina.id = :idOficina " +
                "and re.estado = :idEstadoRegistro " +
                "order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficina", idOficina);
        q.setParameter("idEstadoRegistro", RegwebConstantes.ESTADO_VALIDO);

        return  getRegistroBasicoList(q.getResultList());
    }

    @Override
    public RegistroEntrada findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception {

        Query q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.numeroRegistroFormateado = :numeroRegistroFormateado ");

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);

        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }
    
    
    @Override
    public RegistroEntrada findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception {

        Query q = em.createQuery("Select registroEntrada "
            + " from RegistroEntrada as registroEntrada"
            + " where registroEntrada.numeroRegistro = :numero "
              + " AND  YEAR(registroEntrada.fecha) = :anyo "
              + " AND  registroEntrada.libro.codigo = :libro ");

        q.setParameter("numero", numero);
        q.setParameter("anyo", anyo);
        q.setParameter("libro", libro);
        
        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }
    
    
    
    
    
    

    @Override
    public void anularRegistroEntrada(RegistroEntrada registroEntrada,
        UsuarioEntidad usuarioEntidad) throws Exception{

        RegistroEntrada old = registroEntrada;

        // Estado anulado
        registroEntrada.setEstado(RegwebConstantes.ESTADO_ANULADO);

        // Actualizamos el RegistroEntrada
        registroEntrada = merge(registroEntrada);

        // Creamos el HistoricoRegistroEntrada para la modificación d estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(old,
            usuarioEntidad,RegwebConstantes.TIPO_MODIF_ESTADO,false);


    }

    @Override
    public void tramitarRegistroEntrada(RegistroEntrada registroEntrada, 
        UsuarioEntidad usuarioEntidad) throws Exception{

        RegistroEntrada old = registroEntrada;

        // Estado anulado
        registroEntrada.setEstado(RegwebConstantes.ESTADO_TRAMITADO);

        // Actualizamos el RegistroEntrada
        registroEntrada = merge(registroEntrada);

        // Creamos el HistoricoRegistroEntrada para la modificación d estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(old,
            usuarioEntidad,RegwebConstantes.TIPO_MODIF_ESTADO,false);

    }

    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb.model.utils.RegistroBasico}
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoList(List<Object[]> result) throws Exception{

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        for (Object[] object : result){
            //RegistroBasico registroBasico = new RegistroBasico((Long)object[0],(String)object[1],(Date)object[2],(String)object[3],(String)object[4],(String)object[5]);
            RegistroBasico registroBasico = new RegistroBasico();
            registroBasico.setId((Long)object[0]);
            registroBasico.setNumeroRegistroFormateado((String)object[1]);
            registroBasico.setFecha((Date)object[2]);
            registroBasico.setLibro((String)object[3]);
            registroBasico.setUsuario((String)object[4]);
            if(StringUtils.isEmpty((String)object[5])){
                registroBasico.setExtracto((String) object[6]);
            }else{
                registroBasico.setExtracto((String) object[5]);
            }
            registros.add(registroBasico);

        }

        return registros;
    }

    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb.model.utils.RegistroBasico}
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoCompleto(List<Object[]> result) throws Exception{

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        log.info("Total registros: " + result.size());

        for (Object[] object : result){
            //RegistroBasico registroBasico = new RegistroBasico((Long)object[0],(String)object[1],(Date)object[2],(String)object[3],(String)object[4],(String)object[5]);
            RegistroBasico registroBasico = new RegistroBasico();
            registroBasico.setId((Long)object[0]);
            registroBasico.setNumeroRegistroFormateado((String)object[1]);
            registroBasico.setFecha((Date)object[2]);
            registroBasico.setLibro((String)object[3]);
            registroBasico.setUsuario((String)object[4]);
            if(StringUtils.isEmpty((String)object[5])){
                registroBasico.setExtracto((String) object[6]);
            }else{
                registroBasico.setExtracto((String) object[5]);
            }


            registroBasico.setIdOficina((Long) object[7]);
            registroBasico.setOficina((String)object[8]);
            if(StringUtils.isEmpty((String)object[9])){
                registroBasico.setDestinatario((String)object[10]);
            }else{
                registroBasico.setDestinatario((String)object[9]);
            }
            registroBasico.setEstado((Long)object[11]);
            //registroBasico.setAnexos((Long)object[12]);
            registros.add(registroBasico);
        }

        return registros;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> registros =  em.createQuery("Select distinct(re.id) from RegistroEntrada as re where re.usuario.entidad.id = :idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }


}
