<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.registro == 'entrada'}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.registro == 'salida'}">
    <c:set var="color" value="danger"/>
</c:if>
<c:if test="${param.registro == 'preRegistro'}">
    <c:set var="color" value="warning"/>
</c:if>

<div class="col-xs-8 pull-right">

    <div class="panel panel-${color}">

      <div class="panel-heading">
          <!-- ESTADO 8 -> Anulado
               ESTADO 7 -> Tramitado
               ESTADO 6 -> Enviado
          -->
          <c:if test="${registro.estado == 1 || registro.estado == 2 || registro.estado == 3}">
              <a href="#modalNuevoAnexo" class="btn btn-${color} btn-xs pull-right" role="button" data-toggle="modal" onclick="nuevoAnexo('${registro.id}', '${registro.registroDetalle.id}', '${param.registro}')"><i class="fa fa-plus"></i> <spring:message code="anexo.nuevo"/></a>
          </c:if>
          <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="anexo.anexos"/></strong></h3>
      </div>

      <div class="panel-body">
          <div class="col-xs-12">
              <div id="anexosdiv" class="table-responsive">

                 <c:if test="${empty registro.registroDetalle.anexos}">
                     <div class="alert alert-warning alert-dismissable">
                        <strong><spring:message code="regweb.listado.vacio"/> <spring:message code="anexo.anexo"/></strong>
                     </div>
                 </c:if>

                 <c:if test="${not empty registro.registroDetalle.anexos}">
                     <table id="anexos" class="table table-bordered table-hover table-striped">
                         <colgroup>
                             <col>
                             <col>
                             <col width="100">
                         </colgroup>
                         <thead>
                             <tr>
                                 <th><spring:message code="anexo.titulo"/></th>
                                 <th><spring:message code="anexo.tipoDocumento"/></th>
                                 <th class="center"><spring:message code="regweb.acciones"/></th>
                             </tr>
                         </thead>

                         <tbody>
                             <c:forEach var="anexo" items="${registro.registroDetalle.anexos}">
                                 <tr id="anexo${anexo.id}">

                                     <td>${anexo.titulo}</td>
                                     <td><spring:message code="tipoDocumento.${anexo.tipoDocumento}"/></td>
                                     <td class="center">
                                         <!-- ESTADO 8 -> Anulado
                                              ESTADO 7 -> Tramitado
                                              ESTADO 6 -> Enviado
                                          -->
                                         <c:if test="${registro.estado == 1 || registro.estado == 2 || registro.estado ==3}">
                                             <a class="btn btn-warning btn-sm" data-toggle="modal" role="button" href="#modalNuevoAnexo" onclick="cargarAnexo('${anexo.id}','${registro.id}','${registro.registroDetalle.id}','${param.registro}')" title="Editar"><span class="fa fa-pencil"></span></a>
                                             <a class="btn btn-danger btn-default btn-sm"  onclick="eliminarAnexo('${anexo.id}','${registro.registroDetalle.id}')" href="javascript:void(0);" title="Eliminar"><span class="fa fa-eraser"></span></a>
                                         </c:if>
                                         <c:if test="${registro.estado != 1 && registro.estado != 2 && registro.estado != 3}">
                                             <a class="btn btn-warning disabled btn-sm" href="javascript:void(0);" title="Editar"><span class="fa fa-pencil"></span></a>
                                             <a class="btn btn-danger disabled btn-sm" href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                         </c:if>

                                     </td>

                                 </tr>
                             </c:forEach>
                         </tbody>
                     </table>
                </c:if>
              </div>
          </div>
      </div>
    </div>

</div>

<c:import url="../registro/formularioAnexo.jsp"/>
<script type="text/javascript">
    var urlEliminarAnexo = '<c:url value="/anexo/delete"/>';

</script>