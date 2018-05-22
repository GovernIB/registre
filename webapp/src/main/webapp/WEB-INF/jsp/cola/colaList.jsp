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
                        <c:import url="../modulos/migadepan.jsp"/>
                        <li class="active"><i class="fa fa-ellipsis-h"></i> <spring:message code="cola.colas"/></li>
                    </ol>
                </div>
            </div><!-- /.row -->

            <c:import url="../modulos/mensajes.jsp"/>

            <div class="row">
                <div class="col-xs-12">

                    <ul class="nav nav-tabs" role="tablist">

                        <c:forEach items="${tipos}" var="tipoCola">
                            <li <c:if test="${tipo == tipoCola}">class="active"</c:if>><a href="<c:url value="/cola/list/${tipoCola}"/>"><i class="fa fa-file-o"></i> <spring:message code="cola.tipo.${tipoCola}" /></a></li>
                        </c:forEach>

                    </ul>

                    <div class="tab-content">

                        <div class="panel-body">

                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="form-group col-xs-12">
                                        <form:form modelAttribute="colaBusqueda" method="post" cssClass="form-horizontal">
                                            <form:hidden path="pageNumber"/>
                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="estado"><spring:message code="regweb.estado"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:select path="estado" cssClass="chosen-select" onchange="doForm('#colaBusqueda')">
                                                            <form:option value="" label="..."/>
                                                            <c:forEach var="estado" items="${estados}">
                                                                <form:option value="${estado}"><spring:message code="cola.estado.${estado}"/></form:option>
                                                            </c:forEach>
                                                        </form:select>
                                                    </div>
                                                </div>
                                            </div>
                                        </form:form>
                                    </div>
                                </div>
                            </div>


                            <c:if test="${empty paginacion.listado}">
                                <div class="alert alert-grey alert-dismissable">
                                    <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="regweb.elemento"/></strong>
                                </div>
                            </c:if>

                            <c:if test="${not empty paginacion.listado}">

                                <div class="alert-grey">
                                    <c:if test="${paginacion.totalResults == 1}">
                                        <spring:message code="regweb.resultado"/>
                                        <strong>${paginacion.totalResults}</strong> <spring:message code="regweb.elemento"/>
                                    </c:if>
                                    <c:if test="${paginacion.totalResults > 1}">
                                        <spring:message code="regweb.resultados"/>
                                        <strong>${paginacion.totalResults}</strong> <spring:message code="regweb.elementos"/>
                                    </c:if>

                                    <p class="pull-right"><spring:message code="regweb.pagina"/>
                                        <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col width="120">
                                            <col width="120">
                                            <col width="170">
                                            <col width="60">
                                            <col width="60">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="cola.fecha"/></th>
                                            <th><spring:message code="cola.descripcion.${tipo}"/></th>
                                            <th><spring:message code="oficina.oficina"/></th>
                                            <th><spring:message code="cola.numeroreintentos"/></th>
                                            <th><spring:message code="regweb.errores"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="cola" items="${paginacion.listado}">
                                            <tr>
                                                <td><fmt:formatDate value="${cola.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                <td>${cola.descripcionObjeto}</td>
                                                <td>${cola.denominacionOficina}</td>
                                                <td>
                                                    <c:if test="${empty cola.estado}"><span class="label label-info"><span class="fa fa-repeat"></span> ${cola.numeroReintentos}</span></c:if>
                                                    <c:if test="${cola.estado == 1}"><span class="label label-danger"><span class="fa fa-warning"></span> ${cola.numeroReintentos}</span></c:if>
                                                    <c:if test="${cola.estado == 2}"><span class="label label-warning"><span class="fa fa-warning"></span> ${cola.numeroReintentos}</span></c:if>
                                                </td>
                                                <td class="center">
                                                    <a class="btn btn-warning btn-sm" data-toggle="modal" role="button" href="#infoCola" onclick="infoCola('${cola.id}')" title="<spring:message code="regweb.info"/>"><span class="fa fa-info-circle"></span></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                    <!-- Paginacion -->
                                    <c:import url="../modulos/paginacionBusqueda.jsp">
                                        <c:param name="entidad" value="cola"/>
                                    </c:import>
                                    <button type="button" onclick="goTo('<c:url value="/cola/${entidadActiva.id}/reiniciarCola/${tipo}"/>')" class="btn btn-warning btn-sm"><spring:message code="cola.reiniciar"/></button>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div><!-- col 12-->
            </div><!-- row-->
        </div>
    </div> <!-- /container -->

    <div id="infoCola" class="modal fade">
        <div class="modal-dialog modal-lg">

            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarCola()">x
                    </button>
                    <h4 class="modal-title"><spring:message code="cola.informacion"/></h4>
                </div>
                <div class="modal-body">


                    <div class="form-group col-xs-12" id="errorBox">
                        <div class="col-xs-12 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="regweb.errores"/>:</label>
                        </div>

                        <pre id="error" class=".pre-scrollable"></pre>

                    </div>


                    <div class="clearfix"></div>

                </div>
                <div class="modal-footer">
                    <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarCola()">
                        <spring:message code="regweb.cerrar"/>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <c:import url="../modulos/pie.jsp"/>

    <script type="text/javascript" src="<c:url value="/js/cola.js"/>"></script>
    <script type="text/javascript">

            var urlobtenerCola = '<c:url value="/rest/obtenerCola"/>';

    </script>
   </body>
</html>