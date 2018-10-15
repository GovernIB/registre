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
                        <li class="active"><i class="fa fa-envelope"></i> <spring:message code="notificacion.notificaciones"/> de ${loginInfo.usuarioAutenticado.nombreCompleto}</li>
                    </ol>
                </div>
            </div><!-- /.row -->

            <c:import url="../modulos/mensajes.jsp"/>
            <div id="mensajes"></div>

            <div class="row">

                <div class="col-xs-12">
                    <div class="panel-body">

                        <aside class="col-md-2 pad-right-0">
                            <ul class="nav nav-pills nav-stacked">
                                <li <c:if test="${estado == null}">class="active"</c:if>><a href="<c:url value="/notificacion/list/1"/>"><span class="badge pull-right">${todas}</span><spring:message code="notificacion.bandeja.todas"/></a></li>
                                <li <c:if test="${estado == 0}">class="active"</c:if>><a href="<c:url value="/notificacion/0/list/1"/>"><span class="badge pull-right">${nuevas}</span><spring:message code="notificacion.bandeja.noLeidas"/></a></li>
                                <li <c:if test="${estado == 1}">class="active"</c:if>><a href="<c:url value="/notificacion/1/list/1"/>"><span class="badge pull-right">${leidas}</span><spring:message code="notificacion.bandeja.leidas"/></a></li>
                            </ul>
                            <hr>
                        </aside>

                        <div class="col-md-10">

                            <%--Nueva Notificación--%>
                            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <button class="btn btn-warning btn-sm" title="<spring:message code="notificacion.nueva"/>" data-toggle="modal" data-target="#modalCompose" onclick="limpiarNuevaNotificacion();">
                                            <span class="fa fa-edit fa-lg"></span>
                                        </button>
                                    </div><!--/col-->
                                    <div class="col-xs-12 spacer5"></div>
                                </div><!--/row-->
                            </c:if>

                            <c:if test="${empty paginacion.listado}">
                                <div class="alert alert-grey alert-dismissable">
                                    <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="notificacion.notificacion"/></strong>
                                </div>
                            </c:if>

                            <c:if test="${not empty paginacion.listado}">
                                <!--/inbox toolbar-->
                                <div class="panel panel-default">
                                    <!--message list-->
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover refresh-container pull-down">
                                            <thead class="hidden-xs">
                                                <tr>
                                                    <td class="col-sm-2"><strong><spring:message code="notificacion.fecha"/></strong></td>
                                                    <td class="col-sm-2"><strong><spring:message code="notificacion.remitente"/></strong></td>
                                                    <td class="col-sm-1"><strong><spring:message code="notificacion.tipo"/></strong></td>
                                                    <td class="col-sm-5"><strong><spring:message code="notificacion.asunto"/></strong></td>
                                                    <td class="col-sm-1"><strong><spring:message code="notificacion.estado"/></strong></td>
                                                    <td class="col-sm-1"><strong><spring:message code="regweb.acciones"/></strong></td>
                                                </tr>
                                            </thead>

                                            <tbody>
                                                <c:forEach var="notificacion" items="${paginacion.listado}">
                                                    <tr>
                                                        <td class="col-sm-2 col-xs-2" style="cursor:pointer;" onclick="verNotificacion('${notificacion.id}');">
                                                            <span>
                                                                <c:if test="${notificacion.estado == 0}">
                                                                    <strong><fmt:formatDate value="${notificacion.fechaEnviado}" pattern="dd/MM/yyyy HH:mm:ss"/></strong>
                                                                </c:if>
                                                                <c:if test="${notificacion.estado == 1}">
                                                                    <fmt:formatDate value="${notificacion.fechaEnviado}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                                </c:if>
                                                            </span>
                                                        </td>
                                                        <td class="col-sm-2 col-xs-2" style="cursor:pointer;" onclick="verNotificacion('${notificacion.id}');">
                                                            <span>
                                                                <c:if test="${notificacion.estado == 0}">
                                                                    <strong>${notificacion.nombreRemitente}</strong>
                                                                </c:if>
                                                                <c:if test="${notificacion.estado == 1}">
                                                                    ${notificacion.nombreRemitente}
                                                                </c:if>
                                                            </span>
                                                        </td>
                                                        <td class="col-sm-1 col-xs-1" style="cursor:pointer;" onclick="verNotificacion('${notificacion.id}');">
                                                            <c:if test="${notificacion.tipo == 0}">
                                                                <span class="label label-warning"><spring:message code="notificacion.tipo.${notificacion.tipo}"/></span>
                                                            </c:if>
                                                            <c:if test="${notificacion.tipo == 1}">
                                                                <span class="label label-success"><spring:message code="notificacion.tipo.${notificacion.tipo}"/></span>
                                                            </c:if>
                                                            <c:if test="${notificacion.tipo == 2}">
                                                                <span class="label label-danger"><spring:message code="notificacion.tipo.${notificacion.tipo}"/></span>
                                                            </c:if>
                                                        </td>
                                                        <td class="col-sm-5 col-xs-5" style="cursor:pointer;" onclick="verNotificacion('${notificacion.id}');">
                                                            <c:if test="${notificacion.estado == 0}">
                                                                <strong><c:out value="${notificacion.asuntoCorto}" escapeXml="true"/></strong>
                                                            </c:if>
                                                            <c:if test="${notificacion.estado == 1}">
                                                                <c:out value="${notificacion.asuntoCorto}" escapeXml="true"/>
                                                            </c:if>
                                                        </td>
                                                        <td class="col-sm-1 col-xs-1" style="cursor:pointer;" onclick="verNotificacion('${notificacion.id}');">
                                                            <c:if test="${notificacion.estado == 0}">
                                                                <span class="label label-info"><spring:message code="notificacion.estado.${notificacion.estado}" /></span>
                                                            </c:if>

                                                            <c:if test="${notificacion.estado == 1}">
                                                                <span class="label label-success" rel="popupAbajo" data-content="<fmt:formatDate value="${notificacion.fechaLeido}" pattern="dd/MM/yyyy HH:mm:ss"/>" data-toggle="popover"><spring:message code="notificacion.estado.${notificacion.estado}"/></span>
                                                            </c:if>
                                                        </td>
                                                        <td class="col-sm-1 col-xs-1 center">
                                                            <c:if test="${notificacion.estado == 0}">
                                                                <a class="btn btn-success btn-xs" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/notificacion/${notificacion.id}/leer"/>","<spring:message code="notificacion.confirmar.leer" htmlEscape="true"/>")' title="<spring:message code="notificacion.leer"/>"><span class="fa fa-check"></span></a>
                                                            </c:if>
                                                            <a class="btn btn-danger btn-xs" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/notificacion/${notificacion.id}/eliminar"/>","<spring:message code="notificacion.confirmar.eliminar" htmlEscape="true"/>")' title="<spring:message code="notificacion.eliminar"/>"><span class="fa fa-trash"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                    </div>

                                </div>

                                <div class="panel">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/>
                                            <strong>${paginacion.totalResults}</strong> <spring:message code="notificacion.notificacion"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/>
                                            <strong>${paginacion.totalResults}</strong> <spring:message code="notificacion.notificaciones"/>
                                        </c:if>

                                        <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                    </div>

                                    <!-- Paginacion -->
                                    <c:if test="${estado == null}">
                                        <c:import url="../modulos/paginacion.jsp">
                                            <c:param name="entidad" value="notificacion"/>
                                        </c:import>
                                    </c:if>
                                    <c:if test="${estado != null}">
                                        <c:import url="../modulos/paginacion.jsp">
                                            <c:param name="entidad" value="notificacion/${estado}"/>
                                        </c:import>
                                    </c:if>
                                </div>

                                <%--Ver Notificación--%>
                                <div class="modal fade" id="modalLeer">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                <h4 class="modal-title"><spring:message code="notificacion.notificacion"/></h4>
                                            </div>
                                            <div class="modal-body">
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <h3 id="asunto" title="subject"></h3>
                                                    </div>
                                                    <div class="col-md-8">
                                                        <blockquote class="bg-warning small">
                                                            <strong id="remitente"></strong> el <span id="fechaEnviado"></span>
                                                        </blockquote>
                                                    </div>
                                                    <div class="col-md-4">

                                                        <button id="botonEliminar" class="btn btn-sm btn-danger pull-right" title="<spring:message code="regweb.eliminar"/>" data-toggle="tooltip">
                                                            <i class="fa fa-trash-o fa-1x"></i>
                                                        </button>
                                                        <div class="spacer5 pull-right"></div>
                                                        <button id="botonLeer" class="btn btn-sm btn-success pull-right" title="<spring:message code="notificacion.leer"/>" data-toggle="tooltip">
                                                            <i class="fa fa-check fa-1x"></i>
                                                        </button>
                                                    </div>
                                                    <div class="col-xs-12"><hr></div>
                                                    <div id="mensaje" class="col-xs-12"></div>
                                                    <div class="col-xs-12 clearfix"></div>
                                                </div><!--/row-->
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-sm btn-warning pull-left" data-dismiss="modal"><spring:message code="regweb.cerrar"/></button>
                                            </div>
                                        </div><!-- /.modal-content -->
                                    </div><!-- /.modal-dialog -->
                                </div><!-- /.modal compose message -->
                            </c:if>

                            <%--Nueva Notificación--%>
                            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                                <%--Nueva notificación--%>
                                <div class="modal fade" id="modalCompose">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                <h4 class="modal-title"><spring:message code="notificacion.nueva"/></h4>
                                            </div>
                                            <div class="modal-body">

                                                <div id="errores" class="alert alert-danger"></div>

                                                <form id="nuevaNotificacion" method="post" class="form-horizontal">
                                                    <div class="form-group">
                                                        <label class="col-sm-2" for="destinatarios"><spring:message code="notificacion.destinatario"/></label>
                                                        <div class="col-sm-10">
                                                            <select id="destinatarios" class="chosen-select" multiple="multiple">
                                                                <option value="-1">Todos</option>
                                                                <c:forEach items="${destinatarios}" var="destinatario">
                                                                    <option value="${destinatario.id}">${destinatario.denominacion}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-2" for="tipo"><spring:message code="notificacion.tipo"/></label>
                                                        <div class="col-sm-10">
                                                            <select id="tipo" class="chosen-select">
                                                                <c:forEach items="${tipos}" var="tipo">
                                                                    <option value="${tipo}"><spring:message code="notificacion.tipo.${tipo}" /></option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-2" for="asuntoNotificacion"><spring:message code="notificacion.asunto"/></label>
                                                        <div class="col-sm-10"><input id="asuntoNotificacion" class="form-control" maxlength="100"/></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-sm-12" for="mensajeNotificacion"><spring:message code="notificacion.mensaje"/></label>
                                                        <div class="col-sm-12"><textarea id="mensajeNotificacion" rows="12" class="form-control" maxlength="4000"></textarea></div>
                                                    </div>
                                                </form>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-danger pull-left" data-dismiss="modal"><spring:message code="regweb.cancelar"/></button>
                                                <button type="button" class="btn btn-warning" onclick="nuevaNotificacion()"><spring:message code="regweb.enviar"/> <i class="fa fa-arrow-circle-right fa-lg"></i></button>
                                            </div>
                                        </div><!-- /.modal-content -->
                                    </div><!-- /.modal-dialog -->
                                </div><!-- /.modal compose message -->
                            </c:if>


                        </div>
                    </div>
             </div>

        </div>
    </div> <!-- /container -->

    <c:import url="../modulos/pie.jsp"/>

    <script src="<c:url value="/js/notificacion.js"/>"></script>

        <script type="text/javascript">

            var urlObtenerNotificacion = '<c:url value="/rest/obtenerNotificacion"/>';
            var urlNuevaNotificacion = '<c:url value="/notificacion/nueva"/>';

            var tradsNotificacion = [];
            tradsNotificacion['notificacion.tipo.0'] = "<spring:message code='notificacion.tipo.0' javaScriptEscape='true' />";
            tradsNotificacion['notificacion.tipo.1'] = "<spring:message code='notificacion.tipo.1' javaScriptEscape='true' />";
            tradsNotificacion['notificacion.tipo.2'] = "<spring:message code='notificacion.tipo.2' javaScriptEscape='true' />";
            tradsNotificacion['notificacion.eliminar'] = "<spring:message code="notificacion.confirmar.eliminar" htmlEscape="true"/>";
            tradsNotificacion['notificacion.confirmar.leer'] = "<spring:message code="notificacion.confirmar.leer" htmlEscape="true"/>";
            tradsNotificacion['notificacion.campos.obligatorios'] = "<spring:message code="notificacion.campos.obligatorios" htmlEscape="true"/>";
            tradsNotificacion['notificacion.nueva.ok'] = "<spring:message code="notificacion.nueva.ok" htmlEscape="true"/>";
            tradsNotificacion['notificacion.nueva.error'] = "<spring:message code="notificacion.nueva.error" htmlEscape="true"/>";
            tradsNotificacion['notificacion.generando'] = "<spring:message code="notificacion.generando" htmlEscape="true"/>";

        </script>

</body>
</html>