<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${entidadActiva.nombre}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="tipoAsunto.tipoAsunto"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                       <a class="btn btn-warning btn-xs pull-right" href="<c:url value="/tipoAsunto/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="tipoAsunto.nuevo"/></a>
                       <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="tipoAsunto.listado"/></strong></h3>
                   </div>

                   <div class="panel-body">
                       <c:import url="../modulos/mensajes.jsp"/>

                       <c:if test="${empty listado}">
                           <div class="alert alert-grey alert-dismissable">
                               <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                               <strong> <spring:message code="regweb.listado.vacio"/> <spring:message code="tipoAsunto.tipoAsunto"/></strong>
                           </div>
                       </c:if>

                       <c:if test="${not empty listado}">
                           <div class="alert-grey">
                              <c:if test="${paginacion.totalResults == 1}">
                                  <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="tipoAsunto.tipoAsunto"/>
                              </c:if>
                              <c:if test="${paginacion.totalResults > 1}">
                                  <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="tipoAsunto.tipoAsuntos"/>
                              </c:if>

                              <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                           </div>

                           <div class="table-responsive">
                               <table class="table table-bordered table-hover table-striped tablesorter">
                                   <colgroup>
                                       <col>
                                       <col>
                                       <col>
                                       <col width="200">
                                       <col width="100">
                                   </colgroup>
                                   <thead>
                                       <tr>
                                           <th><spring:message code="regweb.nombre"/></th>
                                           <th><spring:message code="codigoAsunto.codigo"/></th>
                                           <th><spring:message code="regweb.activo"/></th>
                                           <th class="center"><spring:message code="codigoAsunto.codigoAsuntos"/></th>
                                           <th class="center"><spring:message code="regweb.acciones"/></th>
                                       </tr>
                                   </thead>

                                   <tbody>
                                       <c:forEach var="tipoAsunto" items="${listado}">
                                           <tr>
                                               <td><i:trad value="${tipoAsunto}" property="nombre"/></td>
                                               <td>${tipoAsunto.codigo}</td>
                                               <c:if test="${tipoAsunto.activo}"><td><span class="label label-success"><spring:message code="regweb.si"/></span></td></c:if>
                                               <c:if test="${not tipoAsunto.activo}"><td><span class="label label-danger"><spring:message code="regweb.no"/></span></td></c:if>
                                               <td class="center">${fn:length(tipoAsunto.codigosAsunto)}</td>
                                               <td class="center">
                                                   <a class="btn btn-warning btn-sm" href="<c:url value="/tipoAsunto/${tipoAsunto.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                   <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/tipoAsunto/${tipoAsunto.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                               </td>

                                           </tr>
                                       </c:forEach>


                                   </tbody>
                               </table>

                               <!-- Paginacion -->
                               <c:import url="../modulos/paginacion.jsp">
                                   <c:param name="entidad" value="tipoAsunto"/>
                               </c:import>


                           </div>
                       </c:if>
                   </div>

                </div>
            </div>
        </div>

    </div>
</div>

 <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>