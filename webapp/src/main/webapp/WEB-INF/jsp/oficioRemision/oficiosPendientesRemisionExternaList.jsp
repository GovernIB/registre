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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message
                            code="registroEntrada.oficiosRemisionExterna"/></strong></li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-success">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong><spring:message code="registroEntrada.buscador.oficiosRemisionExterna"/></strong></h3>
                    </div>
                    <div class="panel-body">

                        <c:if test="${empty organismosDestino}">
                            <div class="row">
                                <div class="col-xs-12">

                                    <div class="alert alert-warning ">
                                        <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="oficioRemision.oficioRemision"/></strong>
                                    </div>

                                </div>
                            </div>
                        </c:if>

                        <c:if test="${not empty organismosDestino}">

                            <form:form modelAttribute="registroEntradaBusqueda" method="post" cssClass="form-horizontal">
                                <form:hidden path="pageNumber"/>

                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left align-right"><spring:message code="registroEntrada.oficina"/></div>
                                    <div class="col-xs-8">
                                        <form:select path="registroEntrada.oficina.id" items="${oficinasRegistro}"
                                                     itemValue="id" itemLabel="denominacion" cssClass="chosen-select"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left align-right"><spring:message code="registroEntrada.libro"/></div>
                                    <div class="col-xs-8">
                                        <form:select path="registroEntrada.libro.id" items="${librosRegistro}"
                                                     itemValue="id" itemLabel="nombreCompleto"
                                                     cssClass="chosen-select"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left align-right"><spring:message code="registroEntrada.organDestinatari"/></div>
                                    <div class="col-xs-8">
                                        <form:select path="registroEntrada.destinoExternoCodigo"
                                                     items="${organismosDestino}"
                                                     itemValue="codigo" itemLabel="denominacion"
                                                     cssClass="chosen-select"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left align-right"><spring:message code="registroEntrada.anyRegistro"/></div>
                                    <div class="col-xs-8">
                                        <form:select path="anyo" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <c:forEach items="${anys}" var="anyo">
                                                <form:option value="${anyo}">${anyo}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>

                                <div class="form-group col-xs-12">
                                    <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                                </div>
                            </form:form>
                        </c:if>

                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${paginacion.totalResults == 0}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message
                                                code="registroEntrada.registroEntrada"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${paginacion.totalResults > 0}">

                                        <c:if test="${oficiosRemisionOrganismo.vigente}">
                                            <div class="alert alert-warning center">
                                                <strong>${oficiosRemisionOrganismo.organismo.denominacion}
                                                    (${oficiosRemisionOrganismo.organismo.estado.descripcionEstadoEntidad})</strong>
                                            </div>
                                        </c:if>

                                        <c:if test="${oficiosRemisionOrganismo.vigente == false}">
                                            <div class="alert alert-danger center">
                                                <strong>${oficiosRemisionOrganismo.organismo.denominacion}</strong>
                                                <br> <br>
                                                <strong><spring:message code="oficioRemision.organismoDestino.extinguido"/></strong>
                                            </div>
                                        </c:if>

                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                            </c:if>

                                            <p class="pull-right"><spring:message code="regweb.pagina"/>
                                                <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}
                                            </p>
                                        </div>

                                        <c:if test="${!oficiosRemisionOrganismo.sir}"> <%--El organismo no está en SIR--%>

                                            <div class="table-responsive">
                                                <form:form action="${pageContext.request.contextPath}/oficioRemision/new"
                                                        id="oficio" modelAttribute="registroEntradaListForm" method="post" cssClass="form-horizontal">

                                                    <input type="hidden" id="organismoExternoCodigo" name="organismoExternoCodigo"
                                                           value="${oficiosRemisionOrganismo.paginacion.listado[0].destinoExternoCodigo}"/>
                                                    <input type="hidden" id="organismoExternoDenominacion" name="organismoExternoDenominacion"
                                                           value="${oficiosRemisionOrganismo.paginacion.listado[0].destinoExternoDenominacion}"/>

                                                    <input type="hidden" id="idLibro" name="idLibro" value="${registroEntradaBusqueda.registroEntrada.libro.id}"/>

                                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="50">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th style="text-align:center;cursor: pointer;" onclick="seleccionarTodo('oficio','${paginacion.totalResults}');">
                                                                <i class="fa fa-check-square"></i></th>
                                                            <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                                            <th><spring:message code="registroEntrada.oficina"/></th>
                                                            <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                                            <th><spring:message code="registroEntrada.extracto"/></th>
                                                            <th><spring:message code="registroEntrada.interesados"/></th>
                                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="registroEntrada" items="${paginacion.listado}" varStatus="indice">
                                                            <tr>
                                                                <td><form:checkbox id="oficio_id${indice.index}" path="registros[${indice.index}].id"
                                                                                   value="${registroEntrada.id}"/></td>
                                                                <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                                <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td><label class="no-bold" rel="ayuda" data-content="${registroEntrada.oficina.codigo}"
                                                                           data-toggle="popover">${registroEntrada.oficina.denominacion}</label>
                                                                </td>
                                                                <td>${registroEntrada.destinoExternoDenominacion}</td>
                                                                <td>${registroEntrada.registroDetalle.extracto}</td>
                                                                <td class="center"><label class="no-bold representante"
                                                                                          rel="ayuda"
                                                                                          data-content="${registroEntrada.registroDetalle.nombreInteresadosHtml}"
                                                                                          data-toggle="popover">${registroEntrada.registroDetalle.totalInteresados}</label>
                                                                </td>

                                                                <td class="center">
                                                                    <a class="btn btn-info btn-sm"
                                                                       href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>"
                                                                       title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>

                                                        </tbody>
                                                    </table>

                                                    <!-- Paginacion -->
                                                    <c:import url="../modulos/paginacionBusqueda.jsp">
                                                        <c:param name="entidad" value="registroEntrada"/>
                                                    </c:import>

                                                </form:form>

                                                <div class="btn-group">
                                                    <c:if test="${oficiosRemisionOrganismo.vigente}">
                                                        <button type="button" onclick="doForm('#oficio')"
                                                                class="btn btn-sm btn-warning dropdown-toggle">
                                                            <spring:message code="oficioRemision.boton.crear"/>
                                                        </button>
                                                    </c:if>

                                                    <c:if test="${oficiosRemisionOrganismo.vigente == false}">
                                                        <button type="button" class="btn btn-sm btn-warning disabled">
                                                            <spring:message code="oficioRemision.boton.crear"/>
                                                        </button>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </c:if>

                                        <c:if test="${oficiosRemisionOrganismo.sir}"> <%--El organismo está en SIR--%>

                                            <div class="table-responsive">
                                                <form:form action="${pageContext.request.contextPath}/oficioRemision/sir"
                                                        id="oficio" modelAttribute="sirForm" method="post" cssClass="form-horizontal">

                                                    <input type="hidden" id="organismoExterno" name="organismoExterno"
                                                           value="${oficiosRemisionOrganismo.paginacion.listado[0].destinoExternoCodigo}"/>
                                                    <input type="hidden" id="organismoExternoDenominacion" name="organismoExternoDenominacion"
                                                           value="${oficiosRemisionOrganismo.paginacion.listado[0].destinoExternoDenominacion}"/>

                                                    <input type="hidden" id="idLibro" name="idLibro" value="${registroEntradaBusqueda.registroEntrada.libro.id}"/>

                                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                                        <colgroup>
                                                            <col>
                                                            <col width="80">
                                                            <col>
                                                            <col width="100">
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="100">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th style="text-align:center;cursor: pointer;"
                                                                onclick="seleccionarTodo('oficio','${paginacion.totalResults}');">
                                                                <i class="fa fa-check-square"></i></th>
                                                            <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                                            <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                            <th><spring:message code="registroEntrada.oficina"/></th>
                                                            <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                                            <th><spring:message code="registroEntrada.extracto"/></th>
                                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                            <c:forEach var="registroEntrada" items="${paginacion.listado}" varStatus="indice">
                                                                <tr>
                                                                    <td><form:checkbox id="oficio_id${indice.index}" path="registros[${indice.index}].id"
                                                                                       value="${registroEntrada.id}"/></td>
                                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="yyyy"/>/ ${registroEntrada.numeroRegistro}</td>
                                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                    <td>${registroEntrada.libro.nombre}</td>
                                                                    <td>${registroEntrada.oficina.denominacion}</td>
                                                                    <c:if test="${registroEntrada.destino != null}">
                                                                        <td>${registroEntrada.destino.denominacion}
                                                                            (${registroEntrada.destino.estado.descripcionEstadoEntidad})
                                                                        </td>
                                                                    </c:if>
                                                                    <c:if test="${registroEntrada.destino == null}">
                                                                        <td>${registroEntrada.destinoExternoDenominacion}</td>
                                                                    </c:if>
                                                                    <td>${registroEntrada.registroDetalle.extracto}</td>
                                                                    <td class="center">
                                                                        <a class="btn btn-info btn-sm"
                                                                           href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>"
                                                                           title="<spring:message code="registroEntrada.detalle"/>"><span
                                                                                class="fa fa-eye"></span></a>
                                                                        <a class="btn btn-warning btn-sm"
                                                                           href="<c:url value="/registroEntrada/${registroEntrada.id}/edit"/>"
                                                                           title="<spring:message code="regweb.editar"/>"><span
                                                                                class="fa fa-pencil"></span></a>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>

                                                    </table>

                                                    <div class="row">
                                                        <div class="form-group col-xs-12">
                                                            <div class="col-xs-2 pull-left align-right">
                                                                <spring:message code="oficioRemision.oficinaSir"/></div>
                                                            <div class="col-xs-10">
                                                                <form:select path="oficinaSIR"
                                                                             items="${oficiosRemisionOrganismo.oficinasSIR}"
                                                                             itemLabel="denominacion"
                                                                             itemValue="codOfiResponsable"
                                                                             class="form-control"/>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Paginacion -->
                                                    <c:import url="../modulos/paginacionBusqueda.jsp">
                                                        <c:param name="entidad" value="registroEntrada"/>
                                                    </c:import>

                                                </form:form>

                                                <div class="btn-group">
                                                    <c:if test="${oficiosRemisionOrganismo.vigente && oficiosRemisionOrganismo.oficinasSIR}">
                                                        <button type="button"
                                                                onclick="doForm('#oficio')"
                                                                class="btn btn-sm btn-warning dropdown-toggle">
                                                            <spring:message code="oficioRemision.boton.crear"/>
                                                        </button>
                                                    </c:if>

                                                    <c:if test="${oficiosRemisionOrganismo.vigente == false || oficiosRemisionOrganismo.oficinasSIR == false}">
                                                        <button type="button"
                                                                class="btn btn-sm btn-warning disabled">
                                                            <spring:message code="oficioRemision.boton.crear"/>
                                                        </button>
                                                    </c:if>
                                                </div>

                                            </div>
                                        </c:if>
                                    </c:if>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- FIN BUSCADOR -->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    //Selecciona todos los oficios de remision de un organismo
    function seleccionarTodo(nomOficio, filas) {
        var len = parseInt(filas);
        var nombre = "#" + nomOficio + "_id0";
        //Selecciona todos los checks o los deselecciona todos a la vez
        if (len > 0) {
            if ($(nombre).prop('checked')) {
                for (var i = 0; i < len; i++) {
                    nombre = "#" + nomOficio + "_id" + i;
                    $(nombre).prop('checked', false);
                }
            } else {
                for (var i = 0; i < len; i++) {
                    nombre = "#" + nomOficio + "_id" + i;
                    $(nombre).prop('checked', true);
                }
            }
        }
    }
</script>

</body>
</html>