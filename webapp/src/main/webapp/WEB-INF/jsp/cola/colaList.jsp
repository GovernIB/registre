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

                        <c:forEach items="${tiposCola}" var="tipoCola">
                            <li <c:if test="${tipo == tipoCola.id}">class="active"</c:if>>
                                <a href="<c:url value="/cola/list/${tipoCola.id}"/>">
                                    <i class="fa fa-file-o"></i> <spring:message code="cola.tipo.${tipoCola.id}" />
                                    <c:if test="${tipoCola.activa == true}"><span class="text-vermell">(<spring:message code="cola.parada"/> ${tipoCola.pendientes} p.)</span></c:if>
                                    <c:if test="${tipoCola.activa == false}"><span class="text-verd">(<spring:message code="cola.activa"/> ${tipoCola.pendientes} p.)</span></c:if>
                                </a>
                            </li>
                        </c:forEach>

                    </ul>

                    <div class="tab-content">

                        <div class="panel-body">

                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <form:form modelAttribute="colaBusqueda" method="post" cssClass="form-horizontal">
                                        <form:hidden path="pageNumber"/>

                                            <div class="form-group col-xs-3 espaiLinies senseMargeLat">
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

                                            <div class="form-group col-xs-4 espaiLinies senseMargeLat">
                                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                    <form:label path="descripcionObjeto"><spring:message code="cola.descripcion"/></form:label>
                                                </div>
                                                <div class="col-xs-8">
                                                    <form:input path="descripcionObjeto" cssClass="form-control" maxlength="255" />
                                                </div>
                                            </div>

                                            <div class="form-group col-xs-1 espaiLinies senseMargeLat">
                                                <button type="button" onclick="doForm('#colaBusqueda')" class="btn btn-warning btn-sm btn-block">
                                                    <spring:message code="regweb.buscar"/>
                                                </button>
                                            </div>

                                    </form:form>
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

                                    <%--Select de "Ir a página"--%>
                                    <c:import url="../modulos/paginas.jsp"/>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th class="center"><spring:message code="cola.fechaEntrada"/></th>
                                                <th class="center"><spring:message code="cola.descripcion"/></th>
                                                <th class="center"><spring:message code="regweb.estado"/></th>
                                                <th class="center"><spring:message code="oficina.oficina"/></th>
                                                <th class="center" ><spring:message code="cola.numeroreintentos"/></th>
                                                <th class="center"><spring:message code="cola.fechaProcesado"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <c:forEach var="cola" items="${paginacion.listado}">
                                                <tr>
                                                    <td class="center"><fmt:formatDate value="${cola.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                    <td class="center"><a href="javascript:void(0);" onclick="buscarIntegraciones('${cola.descripcionObjeto}')" title="<spring:message code="integracion.busqueda"/>">${cola.descripcionObjeto}</a></td>
                                                    <td class="center">
                                                        <c:if test="${cola.estado == RegwebConstantes.COLA_ESTADO_PROCESADO}">
                                                            <span class="label label-success"><spring:message code="cola.estado.${cola.estado}"/></span>
                                                        </c:if>
                                                        <c:if test="${cola.estado == RegwebConstantes.COLA_ESTADO_PENDIENTE}">
                                                            <span class="label label-warning"><spring:message code="cola.estado.${cola.estado}"/></span>
                                                        </c:if>
                                                        <c:if test="${cola.estado == RegwebConstantes.COLA_ESTADO_ERROR}">
                                                            <span class="label label-danger"><spring:message code="cola.estado.${cola.estado}"/></span>
                                                        </c:if>
                                                    </td>
                                                    <td class="center">${cola.denominacionOficina}</td>
                                                    <td class="center">
                                                        <c:if test="${cola.estado == 1}"> <%--ERROR--%>
                                                            <p rel="popupAbajo" data-content="<spring:message code="cola.maxreintentos.alcanzado"/>" data-toggle="popover"><span class="label label-danger"><span class="fa fa-warning"></span> ${cola.numeroReintentos}</span></p>
                                                            <a class="btn btn-warning btn-sm" data-toggle="modal" role="button" href="#infoCola" onclick="infoCola('${cola.id}')" title="<spring:message code="regweb.info"/>"><span class="fa fa-info-circle"></span></a>
                                                        </c:if>
                                                        <c:if test="${cola.estado == 2}"> <%--PENDIENTE--%>
                                                            <c:if test="${cola.numeroReintentos == 0}">
                                                                <span class="label label-info"><span class="fa fa-repeat"></span> ${cola.numeroReintentos}</span>
                                                            </c:if>
                                                            <c:if test="${cola.numeroReintentos > 0}">
                                                                <p rel="popupAbajo" data-content="<spring:message code="cola.numeroreintentos.alcanzado"/>" data-toggle="popover"><span class="label label-warning"><span class="fa fa-warning"></span> ${cola.numeroReintentos}</span></p>
                                                                <a class="btn btn-warning btn-sm" data-toggle="modal" role="button" href="#infoCola" onclick="infoCola('${cola.id}')" title="<spring:message code="regweb.info"/>"><span class="fa fa-info-circle"></span></a>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${cola.estado == 3}"> <%--PROCESADO--%>
                                                            <span class="label label-success"><span class="fa fa-repeat"></span> ${cola.numeroReintentos}</span>
                                                            <c:if test="${cola.numeroReintentos > 0}"><a class="btn btn-warning btn-sm" data-toggle="modal" role="button" href="#infoCola" onclick="infoCola('${cola.id}')" title="<spring:message code="regweb.info"/>"><span class="fa fa-info-circle"></span></a></c:if>
                                                        </c:if>
                                                    </td>
                                                    <td class="center"><fmt:formatDate value="${cola.fechaProcesado}" pattern="dd/MM/yyyy HH:mm:ss"/></td>

                                                    <td class="center">
                                                        <c:if test="${cola.estado == RegwebConstantes.COLA_ESTADO_ERROR}">
                                                            <a class="btn btn-success btn-sm" onclick='confirm("<c:url value="/cola/${cola.id}/reiniciar/${tipo}"/>","<spring:message code="cola.reiniciar.elemento" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="cola.reiniciar.elemento"/>"><span class="fa fa-repeat"></span></a>
                                                        </c:if>

                                                        <%--Opciones pendiente procesar--%>
                                                        <c:if test="${cola.estado != RegwebConstantes.COLA_ESTADO_PROCESADO}">
                                                            <a class="btn btn-danger btn-sm" onclick='confirm("<c:url value="/cola/${cola.id}/eliminar"/>","<spring:message code="cola.confirmar.eliminar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                                            <c:if test="${cola.numeroReintentos > 0}">
                                                                <a class="btn btn-info btn-sm" onclick='confirm("<c:url value="/cola/${cola.id}/procesar"/>","<spring:message code="cola.confirmar.procesar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="cola.procesar"/>"><span class="fa fa-check"></span></a>
                                                            </c:if>
                                                            <c:if test="${cola.tipo == RegwebConstantes.COLA_CUSTODIA}">
                                                                <a class="btn btn-primary btn-sm" onclick='confirm("<c:url value="/cola/${cola.id}/custodiarJustificante"/>","<spring:message code="regweb.confirmar.custodiar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="anexo.custodiar"/>"><span class="fa fa-share-square-o"></span></a>
                                                            </c:if>
                                                            <c:if test="${cola.tipo == RegwebConstantes.COLA_DISTRIBUCION}">
                                                                <a class="btn btn-primary btn-sm" onclick='confirm("<c:url value="/cola/${cola.id}/distribuirRegistro"/>","<spring:message code="regweb.confirmar.distribuir" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.distribuir"/>"><span class="fa fa-share-square-o"></span></a>
                                                            </c:if>
                                                        </c:if>

                                                        <%--Re Distribuir--%>
                                                        <c:if test="${cola.estado == RegwebConstantes.COLA_ESTADO_PROCESADO && cola.tipo == RegwebConstantes.COLA_DISTRIBUCION}">
                                                            <a class="btn btn-primary btn-sm" onclick='confirm("<c:url value="/cola/${cola.id}/distribuirRegistro"/>","<spring:message code="registroEntrada.redistribuir" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.distribuir"/>"><span class="fa fa-share-square-o"></span></a>
                                                        </c:if>

                                                        <%--Reiniciar elemento Cola Custodia--%>
                                                        <c:if test="${cola.estado == RegwebConstantes.COLA_ESTADO_PROCESADO && cola.tipo == RegwebConstantes.COLA_CUSTODIA}">
                                                            <a class="btn btn-success btn-sm" onclick='confirm("<c:url value="/cola/${cola.id}/reiniciar/${tipo}"/>","<spring:message code="cola.reiniciar.elemento" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="cola.reiniciar.elemento"/>"><span class="fa fa-repeat"></span></a>
                                                        </c:if>

                                                        <%--Botón detalle registro--%>
                                                        <c:if test="${cola.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                                                            <a class="btn btn-info btn-sm" href="<c:url value="/adminEntidad/registroEntrada/${cola.idObjeto}/detalle"/>" target="_blank" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                        </c:if>
                                                        <c:if test="${cola.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                                                            <a class="btn btn-danger btn-sm" href="<c:url value="/adminEntidad/registroSalida/${cola.idObjeto}/detalle"/>" target="_blank" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <%--Formulario de búsqueda de integraciones--%>
                                    <form id="integracion" action="${pageContext.request.contextPath}/integracion/busqueda" method="post" target="_blank">
                                        <input type="hidden" id="texto" name="texto" value=""/>
                                    </form>

                                    <!-- Paginacion -->
                                    <c:import url="../modulos/paginacionBusqueda.jsp">
                                        <c:param name="entidad" value="cola"/>
                                    </c:import>
                                    <button type="button" onclick="goTo('<c:url value="/cola/${loginInfo.entidadActiva.id}/reiniciarCola/${tipo}"/>')" class="btn btn-warning btn-sm"><spring:message code="cola.reiniciar"/></button>
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
    <script type="text/javascript" src="<c:url value="/js/integracion.js"/>"></script>

    <script type="text/javascript">

        var urlobtenerCola = '<c:url value="/rest/obtenerCola"/>';

    </script>

   </body>
</html>