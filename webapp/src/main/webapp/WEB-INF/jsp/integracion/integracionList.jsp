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
                        <li class="active"><i class="fa fa-gears"></i> <spring:message code="integracion.integraciones"/></li>
                    </ol>
                </div>
            </div><!-- /.row -->

            <c:import url="../modulos/mensajes.jsp"/>

            <div class="row">
                <div class="col-xs-12">

                    <ul class="nav nav-tabs nav-justified" role="tablist">

                        <c:forEach items="${tiposIntegracion}" var="tipoIntegracion">
                            <li <c:if test="${tipo == tipoIntegracion}">class="active"</c:if>><a href="<c:url value="/integracion/list/${tipoIntegracion}"/>"><i class="fa fa-file-o"></i> <spring:message code="integracion.tipo.${tipoIntegracion}" /></a></li>
                        </c:forEach>

                    </ul>

                    <div class="tab-content">

                        <div class="panel-body">

                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <form:form modelAttribute="integracionBusqueda" method="post" cssClass="form-horizontal">
                                        <form:hidden path="pageNumber"/>

                                        <div class="form-group col-xs-3 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="estado"><spring:message code="integracion.estado"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:select path="estado" cssClass="chosen-select" onchange="doForm('#integracionBusqueda')">
                                                    <form:option value="" label="..."/>
                                                    <c:forEach var="estado" items="${estados}">
                                                        <form:option value="${estado}"><spring:message code="integracion.estado.${estado}"/></form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </div>

                                        <div class="form-group col-xs-4 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="numRegFormat"><spring:message code="registroEntrada.numeroRegistro"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input path="numRegFormat" cssClass="form-control" />
                                            </div>
                                        </div>

                                        <div class="form-group col-xs-4 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="descripcion"><spring:message code="integracion.descripcion"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input path="descripcion" cssClass="form-control" />
                                            </div>
                                        </div>

                                        <div class="form-group col-xs-1 espaiLinies senseMargeLat">
                                            <button type="button" onclick="doForm('#integracionBusqueda')" class="btn btn-warning btn-sm btn-block">
                                                <spring:message code="regweb.buscar"/>
                                            </button>
                                        </div>

                                    </form:form>
                                </div>

                            </div>

                            <c:if test="${empty paginacion.listado}">
                                <div class="alert alert-grey alert-dismissable">
                                    <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="integracion.integracion"/></strong>
                                </div>
                            </c:if>

                            <c:if test="${not empty paginacion.listado}">

                                <div class="alert-grey">
                                    <c:if test="${paginacion.totalResults == 1}">
                                        <spring:message code="regweb.resultado"/>
                                        <strong>${paginacion.totalResults}</strong> <spring:message code="integracion.integracion"/>
                                    </c:if>
                                    <c:if test="${paginacion.totalResults > 1}">
                                        <spring:message code="regweb.resultados"/>
                                        <strong>${paginacion.totalResults}</strong> <spring:message code="integracion.integraciones"/>
                                    </c:if>

                                    <%--Select de "Ir a página"--%>
                                    <c:import url="../modulos/paginas.jsp"/>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col width="140">
                                            <col width="140">
                                            <col width="200">
                                            <col width="80">
                                            <col width="70">
                                            <col>
                                            <col width="60">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="integracion.fecha"/></th>
                                            <c:if test="${tipo != RegwebConstantes.INTEGRACION_SIR}">
                                                <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                            </c:if>
                                            <c:if test="${tipo == RegwebConstantes.INTEGRACION_SIR}">
                                                <th><spring:message code="registroSir.identificadorIntercambio"/></th>
                                            </c:if>
                                            <th><spring:message code="integracion.descripcion"/></th>
                                            <th><spring:message code="integracion.tiempo"/></th>
                                            <th><spring:message code="integracion.estado"/></th>
                                            <th><spring:message code="integracion.error"/></th>
                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="integracion" items="${paginacion.listado}">
                                            <tr>
                                                <td><fmt:formatDate value="${integracion.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                <td><a href="javascript:void(0);" onclick="buscarIntegraciones('${integracion.numRegFormat}')">${integracion.numRegFormat}</a> </td>
                                                <td>${integracion.descripcion}</td>
                                                <td>${integracion.tiempoFormateado}</td>
                                                <td>
                                                    <c:if test="${integracion.estado == 0}"><span class="label label-success"><span class="fa fa-check"></span>  Ok</span></c:if>
                                                    <c:if test="${integracion.estado == 1}"><span class="label label-danger"><span class="fa fa-warning"></span> Error</span></c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${fn:length(integracion.error) <= 50}">
                                                        ${integracion.error}
                                                    </c:if>
                                                    <c:if test="${fn:length(integracion.error) > 50}">
                                                        ${integracion.errorCorto}
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <a class="btn btn-warning btn-sm" data-toggle="modal" role="button" href="#infoIntegracion" onclick="infoIntegracion('${integracion.id}')" title="<spring:message code="regweb.info"/>"><span class="fa fa-info-circle"></span></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                    <form:form modelAttribute="integracion" action="${pageContext.request.contextPath}/integracion/busqueda" method="post" cssClass="form-horizontal" target="_blank">
                                        <form:hidden path="texto"/>
                                    </form:form>

                                    <!-- Paginacion -->
                                    <c:import url="../modulos/paginacionBusqueda.jsp">
                                        <c:param name="entidad" value="integracion"/>
                                    </c:import>

                                </div>
                            </c:if>
                        </div>
                    </div>

                </div>
            </div>


        </div>
    </div> <!-- /container -->


    <div id="infoIntegracion" class="modal fade">
        <div class="modal-dialog modal-lg">

            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarIntegracion()">x
                    </button>
                    <h4 class="modal-title"><spring:message code="integracion.informacion"/></h4>
                </div>
                <div class="modal-body">

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.fecha"/>:</label>
                        </div>
                        <div class="col-xs-10" id="fecha"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.tipo"/>:</label>
                        </div>
                        <div class="col-xs-10" id="tipo"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.descripcion"/>:</label>
                        </div>
                        <div class="col-xs-10" id="descripcionIntegracion"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.tiempo"/>:</label>
                        </div>
                        <div class="col-xs-10" id="tiempo"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.estado"/>:</label>
                        </div>
                        <div class="col-xs-10" id="estadoIntegracion"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.peticion"/>:</label>
                        </div>
                        <div class="col-xs-10" id="peticion"></div>
                    </div>

                    <div class="form-group col-xs-12" id="errorBox">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.error"/>:</label>
                        </div>
                        <div class="col-xs-10" id="error"></div>
                    </div>

                    <div class="form-group col-xs-12" id="excepcionBox">
                        <div class="col-xs-12 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.excepcion"/>:</label>
                        </div>

                            <pre id="excepcion" class=".pre-scrollable"></pre>

                    </div>



                    <div class="clearfix"></div>

                </div>
                <div class="modal-footer">
                    <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarIntegracion()">
                        <spring:message code="regweb.cerrar"/>
                    </button>
                </div>
            </div>
        </div>

    </div>

    <c:import url="../modulos/pie.jsp"/>

    <script type="text/javascript" src="<c:url value="/js/integracion.js"/>"></script>

<script type="text/javascript">

    var urlobtenerIntegracion = '<c:url value="/rest/obtenerIntegracion"/>';

    var tradsIntegracion = [];
    tradsIntegracion['integracion.tipo.0'] = "<spring:message code='integracion.tipo.0' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.1'] = "<spring:message code='integracion.tipo.1' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.2'] = "<spring:message code='integracion.tipo.2' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.3'] = "<spring:message code='integracion.tipo.3' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.4'] = "<spring:message code='integracion.tipo.4' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.5'] = "<spring:message code='integracion.tipo.5' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.6'] = "<spring:message code='integracion.tipo.6' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.7'] = "<spring:message code='integracion.tipo.7' javaScriptEscape='true' />";

</script>


</body>
</html>