<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="modulos/imports.jsp"/>
</head>

    <body>

        <c:import url="modulos/menu.jsp"/>

        <div class="row-fluid container main">

            <div class="well well-white">

            <c:import url="modulos/mensajes.jsp"/>

                <div class="row">

                    <div class="col-xs-12">

                        <ol class="breadcrumb">
                            <c:import url="modulos/migadepan.jsp">
                                <c:param name="avisos" value="false"/>
                            </c:import>
                        </ol>

                        <c:if test="${rolAutenticado.nombre == 'RWE_SUPERADMIN' || rolAutenticado.nombre == 'RWE_ADMIN'}">
                            <c:if test="${catalogo == null}">
                                <div class="alert alert-danger">
                                    <strong><spring:message code="regweb.aviso"/>: </strong> <spring:message code="catalogoDir3.catalogo.vacio"/>
                                </div>
                            </c:if>
                        </c:if>

                    </div><!-- /.col-xs-12 -->

                    <!-- Afegim els panels i després els estils els agrupen en 2 columnes -->
                    <div class="col-xs-12">

                        <div class="columnesInici">

                            <%--REGISTROS DE ENTRADA PENDIENTES (RESERVA)--%>
                            <c:if test="${not empty reservas}">
                                <div class="col-xs-6 centrat">
                                    <div id="reserves" class="panel panel-warning">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.reservas"/></strong> </h3>
                                        </div>

                                        <div class="panel-body">

                                            <div class="table-responsive-inici">

                                                <table class="table1 table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col width="80">
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="51">
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                        <th><spring:message code="registroEntrada.fecha"/></th>
                                                        <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                        <th><spring:message code="registroEntrada.usuario"/></th>
                                                        <th><spring:message code="registroEntrada.reserva"/></th>
                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="registroEntrada" items="${reservas}" varStatus="status">
                                                        <tr>
                                                            <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                            <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${registroEntrada.libro}</td>
                                                            <td>${registroEntrada.usuario}</td>
                                                            <td>${registroEntrada.extracto}</td>
                                                            <td class="center">
                                                                <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <%--REGISTROS DE ENTRADA PENDIENTES DE VISAR--%>
                            <%--<c:set var="avisos" value="${avisos+1}"/>
                                <div class="col-xs-6 centrat">
                                    <div class="panel panel-primary">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.pendientesVisar"/></strong> </h3>
                                        </div>

                                        <div class="panel-body">

                                            <div class="table-responsive-inici">

                                                <table class="table1 table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col width="80">
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="51">
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                        <th><spring:message code="registroEntrada.fecha"/></th>
                                                        <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                        <th><spring:message code="registroEntrada.usuario"/></th>
                                                        <th><spring:message code="registroEntrada.extracto"/></th>
                                                        <th class="center"><spring:message code="regweb3.acciones"/></th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="registroEntrada" items="${pendientesVisar}" varStatus="status">
                                                        <tr>
                                                            <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                            <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${registroEntrada.libro}</td>
                                                            <td>${registroEntrada.usuario}</td>
                                                            <td>${registroEntrada.extracto}</td>
                                                            <td class="center">
                                                                <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            --%>

                            <%--OFICIOS PENDIENTES DE LLEGADA--%>
                            <c:if test="${not empty oficiosPendientesLlegada}">
                                <div class="col-xs-6 centrat">

                                    <div id="pendientesLleg" class="panel panel-success">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="oficioRemision.pendientesLlegada.ultimos"/></strong> </h3>
                                        </div>

                                        <div class="panel-body">

                                            <div class="table-responsive-inici">

                                                <table class="table1 table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="51">
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                            <th><spring:message code="oficioRemision.fecha"/></th>
                                                            <th><spring:message code="oficioRemision.oficina"/></th>
                                                            <th><spring:message code="organismo.destino"/></th>
                                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                                        </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="oficioRemision" items="${oficiosPendientesLlegada}" end="5">
                                                        <tr>
                                                            <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${oficioRemision.oficina.denominacion}</td>
                                                            <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>
                                                            <td class="center">
                                                                <a class="btn btn-success btn-sm" href="<c:url value="/oficioRemision/${oficioRemision.id}/aceptar"/>" title="<spring:message code="oficioRemision.aceptar"/>"><span class="fa fa-check"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>

                                                    </tbody>
                                                </table>

                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <%--OFICIOS DE ENTRADA PENDIENTES DE REMISIÓN--%>
                            <c:if test="${not empty organismosOficioRemisionEntrada}">
                                <div class="col-xs-6 centrat">

                                    <div id="pendientesRemEnt" class="panel panel-success">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.oficiosRemision"/></strong> </h3>
                                        </div>

                                        <div class="panel-body">

                                            <div class="table-responsive-inici">

                                                <table class="table1 table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col>
                                                        <col width="51">
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th><spring:message code="organismo.destino"/></th>
                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="organismo" items="${organismosOficioRemisionEntrada}">
                                                        <tr>
                                                            <td>${organismo.denominacion}</td>
                                                            <td class="center">
                                                                <a class="btn btn-info btn-sm" href="<c:url value="/oficioRemision/entradasPendientesRemision"/>" title="<spring:message code="oficioRemision.buscador"/>"><span class="fa fa-search"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>

                                                    </tbody>
                                                </table>

                                            </div>

                                        </div>
                                    </div>

                                </div>
                            </c:if>

                            <%--OFICIOS DE SALIDA PENDIENTES DE REMISIÓN--%>
                            <c:if test="${not empty organismosOficioRemisionSalida}">
                                <div class="col-xs-6 centrat">

                                    <div id="pendientesRemSal" class="panel panel-success">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroSalida.oficiosRemision"/></strong> </h3>
                                        </div>

                                        <div class="panel-body">

                                            <div class="table-responsive-inici">

                                                <table class="table1 table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col>
                                                        <col width="51">
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th><spring:message code="organismo.destino"/></th>
                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="organismo" items="${organismosOficioRemisionSalida}">
                                                        <tr>
                                                            <td>${organismo.denominacion}</td>
                                                            <td class="center">
                                                                <a class="btn btn-danger btn-sm" href="<c:url value="/oficioRemision/salidasPendientesRemision"/>" title="<spring:message code="oficioRemision.buscador"/>"><span class="fa fa-search"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>

                                                    </tbody>
                                                </table>

                                            </div>

                                        </div>
                                    </div>

                                </div>
                            </c:if>

                            <%--REGISTROS SIR PENDIENTES DE PROCESAR--%>
                            <c:if test="${not empty registrosSir}">
                                <div class="col-xs-6 centrat">

                                    <div id="pendientesProc" class="panel panel-primary">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroSir.pendientesProcesar.inicio"/></strong> </h3>
                                        </div>

                                        <div class="panel-body">

                                            <div class="table-responsive-inici">

                                                <table class="table1 table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="51">
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th><spring:message code="registroSir.oficinaOrigen"/></th>
                                                        <th><spring:message code="registroSir.fechaRegistro"/></th>
                                                        <th><spring:message code="registroSir.extracto"/></th>
                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="registroSir" items="${registrosSir}">
                                                        <tr>
                                                            <td>${registroSir.decodificacionEntidadRegistralOrigen}</td>
                                                            <td><fmt:formatDate value="${registroSir.fechaRegistro}" pattern="dd/MM/yyyy"/></td>
                                                            <td>
                                                                <c:if test="${fn:length(registroSir.resumen) <= 40}">
                                                                    ${registroSir.resumen}
                                                                </c:if>

                                                                <c:if test="${fn:length(registroSir.resumen) > 40}">
                                                                    <p rel="resumen" data-content="${registroSir.resumen}" data-toggle="popover">${registroSir.resumenCorto}</p>
                                                                </c:if>

                                                            </td>
                                                            <td class="center">
                                                                <a class="btn btn-primary btn-sm" href="<c:url value="/registroSir/${registroSir.id}/detalle"/>" title="<spring:message code="registroSir.detalle"/>"><span class="fa fa-eye"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>

                                                    </tbody>
                                                </table>

                                            </div>

                                        </div>
                                    </div>

                                </div>
                            </c:if>

                            <%--REGISTROS RECHAZADOS--%>
                            <c:if test="${not empty registrosRechazados}">
                                <div class="col-xs-6 centrat">

                                    <div id="rechazados" class="panel panel-info">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.rechazados.inicio"/></strong> </h3>
                                        </div>

                                        <div class="panel-body">

                                            <div class="table-responsive-inici">

                                                <table class="table1 table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col width="80">
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="51">
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                        <th><spring:message code="registroEntrada.fecha"/></th>
                                                        <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                        <th><spring:message code="registroEntrada.usuario"/></th>
                                                        <th><spring:message code="registroEntrada.extracto"/></th>
                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="registroEntrada" items="${registrosRechazados}" varStatus="status">
                                                        <tr>
                                                            <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                            <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${registroEntrada.libro}</td>
                                                            <td>${registroEntrada.usuario}</td>
                                                            <td>${registroEntrada.extracto}</td>
                                                            <td class="center">
                                                                <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <%--REGISTROS REENVIADOS--%>
                            <c:if test="${not empty registrosReenviados}">
                                <div class="col-xs-6 centrat">

                                    <div id="reenviados" class="panel panel-info">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.reenviados.inicio"/></strong> </h3>
                                        </div>

                                        <div class="panel-body">

                                            <div class="table-responsive-inici">

                                                <table class="table1 table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col width="80">
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="51">
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                        <th><spring:message code="registroEntrada.fecha"/></th>
                                                        <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                        <th><spring:message code="registroEntrada.usuario"/></th>
                                                        <th><spring:message code="registroEntrada.extracto"/></th>
                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <c:forEach var="registroEntrada" items="${registrosReenviados}" varStatus="status">
                                                        <tr>
                                                            <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                            <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                            <td>${registroEntrada.libro}</td>
                                                            <td>${registroEntrada.usuario}</td>
                                                            <td>${registroEntrada.extracto}</td>
                                                            <td class="center">
                                                                <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </c:if>

                        </div><!-- /.columnesInici -->
                    </div><!-- /.col-xs-12 -->

                </div><!-- /.row -->


            </div><!-- /.well-white -->
        </div> <!-- /container -->

        <c:import url="modulos/pie.jsp"/>

        <script type="text/javascript">
            $("[rel='resumen']").popover({ trigger: 'hover',placement: 'top',container:"body", html:true});
        </script>


    </body>
</html>