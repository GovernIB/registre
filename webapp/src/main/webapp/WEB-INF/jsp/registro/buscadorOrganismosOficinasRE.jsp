<%@ page import="es.caib.regweb3.utils.Configuracio" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!-- TODO BORRAR NO SE EMPLEA se ha sustituido por buscadorOrganismosOficinasRE.jsp-->

<!-- Formulario de busqueda compartido. Se usa tanto para buscar organismos como oficinas, en función del parámetro
que se le indica -->

<div id="modalBuscador${param.tipo}" class="modal fade bs-example-modal-lg">
  <div class="modal-dialog modal-lg">
  <div class="modal-content">
      <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarFormularioBusqueda('${param.tipo}')">x</button>
          <h3><spring:message code="organismo.buscador.${param.tipo}"/></h3>
      </div>
 <div class="modal-body" >
     <form id="organismoREBusquedaForm${param.tipo}" class="form-horizontal" action="" method="post">
           <input id="nivelAdministracion" type="hidden" value="${oficina.organismoResponsable.nivelAdministracion.codigoNivelAdministracion}"/>
           <input id="comunidadAutonoma" type="hidden" value="${oficina.organismoResponsable.codAmbComunidad.codigoComunidad}"/>
         <input id="entidadGeografica" type="hidden" value=""/>

           <div class="form-group col-xs-6">
               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                  <label for="codigoOrganismo${param.tipo}"><spring:message code="organismo.buscador.codigo"/></label>
              </div>
              <div class="col-xs-8">
                  <input id="codigoOrganismo${param.tipo}" name="codigoOrganismo" class="form-control" type="text" value=""/>
              </div>
          </div>
          <div class="form-group col-xs-6">
              <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                  <label for="denominacion${param.tipo}"><spring:message code="organismo.buscador.denominacion"/></label>
              </div>
              <div class="col-xs-8">
                  <input id="denominacion${param.tipo}" name="denominacion" class="form-control" type="text" value=""/>
              </div>
          </div>

          <!-- Nivel Administracion -->

          <div class="form-group col-xs-6">
              <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                  <label for="codNivelAdministracion${param.tipo}"><spring:message code="organismo.buscador.nivelAdministracion"/></label>
              </div>
              <div class="col-xs-8">
                  <select id="codNivelAdministracion${param.tipo}" name="codNivelAdministracion" class="chosen-select">
                      <option value="-1">...</option>
                      <c:forEach items="${nivelesAdministracion}" var="nivelAdministracion">
                            <option value="${nivelAdministracion.codigoNivelAdministracion}" >${nivelAdministracion.descripcionNivelAdministracion}</option>
                      </c:forEach>

                  </select>
              </div>
          </div>


          <!-- Comunidad Autonoma -->
          <div class="form-group col-xs-6">
              <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                  <label for="codComunidadAutonoma${param.tipo}"><spring:message code="organismo.buscador.comunidadAutonoma"/></label>
              </div>
              <div class="col-xs-8">
                  <select id="codComunidadAutonoma${param.tipo}" name="codComunidadAutonoma" class="chosen-select">
                      <option value="-1">...</option>
                      <c:forEach items="${comunidadesAutonomas}" var="codComunidadAutonoma">
                        <option value="${codComunidadAutonoma.codigoComunidad}">${codComunidadAutonoma.descripcionComunidad}</option>
                      </c:forEach>

                  </select>
              </div>
          </div>

          <c:if test="${param.tipo == 'OrganismoInteresado'}">
          <!-- Unidad Raiz -->
          <div class="form-group col-xs-6">
              <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                 <label for="unidadRaiz${param.tipo}"><spring:message code="organismo.buscador.unidadRaiz"/></label>
              </div>
              <div class="col-xs-8">
                 <input type="checkbox" id="unidadRaiz${param.tipo}"/>
              </div>
          </div>
          </c:if>

         <div class="clearfix"></div>
     </form>
 </div>
      <div class="modal-footer">
          <!-- Imatge de reload -->
          <div class="col-xs-12 text-center centrat" id="reloadorg${param.tipo}">
            <img  src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
          </div>

          <input type="button" id="buscarorganimos${param.tipo}"
                 onclick="organismoBusqueda('${param.tipo}','<%=Configuracio.getDir3CaibServer()%>','${param.idRegistroDetalle}')"
                 class="btn btn-warning btn-sm" title="<spring:message code="regweb.buscar"/>"
                 value="<spring:message code="regweb.buscar"/>"/>
          <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true"
                  onclick="limpiarFormularioBusqueda('${param.tipo}')"><spring:message code="regweb.cerrar"/></button>
          <br/>
          <br/>
          <br/>
          <!-- Mostrar llistat de la busqueda-->
          <div class="form-group col-xs-12">
              <div id="resultadosbusqueda${param.tipo}">
              </div>
          </div>

      </div>
  </div>
  </div>
</div>
<c:set var="val"><spring:message code="organismo.raiz"/></c:set>
<input id="organismo_raiz" type="hidden" value="${val}"/>
<c:set var="valor"><spring:message code="organismo.superior"/></c:set>
<input id="organismo_superior" type="hidden" value="${valor}"/>
<script type="text/javascript">
    <%-- traduccions para busquedaorganismo.js--%>
    var tradorganismo = new Array();
    tradorganismo['organismo.denominacion'] = "<spring:message code='organismo.denominacion' javaScriptEscape='true' />";
    tradorganismo['regweb.acciones'] = "<spring:message code='regweb.acciones' javaScriptEscape='true' />";
    tradorganismo['organismo.superior'] = "<spring:message code='organismo.superior' javaScriptEscape='true' />";
    tradorganismo['organismo.superior.vacio'] = "<spring:message code='organismo.superior.vacio' javaScriptEscape='true' />";
    tradorganismo['organismo.raiz'] = "<spring:message code='organismo.raiz' javaScriptEscape='true' />";
    tradorganismo['organismo.localidad'] = "<spring:message code='organismo.localidad' javaScriptEscape='true' />";

</script>