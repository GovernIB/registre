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

        <!-- Miga de pan -->
       <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <%--<li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>--%>
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li><a href="<c:url value="/oficioRemision/oficiosPendientesLlegada"/>" ><i class="fa fa-list"></i> <spring:message code="oficioRemision.pendientesLlegada"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="oficioRemision.oficioRemision"/> <fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</li>
                </ol>
            </div>
       </div><!-- Fin miga de pan -->

        <div class="row">

            <div class="col-xs-4">

                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="oficioRemision.oficioRemision"/> <fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <dl class="detalle_registro">
                            <dt><i class="fa fa-globe"></i> Entidad: </dt> <dd> ${oficioRemision.oficina.organismoResponsable.entidad.nombre}</dd>
                            <dt><i class="fa fa-briefcase"></i> <spring:message code="registroEntrada.oficina"/>: </dt> <dd> ${oficioRemision.oficina.denominacion}</dd>
                            <dt><i class="fa fa-clock-o"></i> Fecha: </dt> <dd> <fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            <dt><i class="fa fa-book"></i> <spring:message code="libro.libro"/>: </dt> <dd> ${oficioRemision.libro.nombre}</dd>
                            <dt><i class="fa fa-user"></i> <spring:message code="usuario.usuario"/>: </dt> <dd> ${oficioRemision.usuarioResponsable.usuario.nombreCompleto}</dd>
                            <dt><i class="fa fa-exchange"></i> <spring:message code="oficioRemision.organismoDestino"/>: </dt> 
                            <dd>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</dd>
                            <dt><i class="fa fa-bookmark"></i> <spring:message code="oficioRemision.estado"/> </dt>
                            <dd>
                                <span class="label ${(oficioRemision.estado == 2)?'label-success':'label-danger'}">
                                <spring:message code="oficioRemision.estado.${oficioRemision.estado}"/>
                                <c:if test="${not empty oficioRemision.fechaEstado && oficioRemision.estado != 0}">
                                    - <fmt:formatDate value="${oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </c:if>
                                </span>
                            </dd>

                        </dl>

                    </div>

                    <c:if test="${oficioRemision.estado == 0}">
                        <div class="panel-footer">
                            <button type="button" onclick="doForm('#oficioPendienteLlegadaForm')" class="btn btn-success btn-sm btn-block"><spring:message code="oficioRemision.procesar"/></button>
                        </div>
                    </c:if>

                </div>

            </div>

            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <%--Registros de Entrada--%>

            <c:if test="${not empty oficioRemision.registrosEntrada}">
                <div class="col-xs-8 col-xs-offset">

                    <div class="panel panel-info">

                        <div class="panel-heading">

                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="oficioRemision.registrosEntrada"/></strong></h3>
                        </div>

                        <div class="panel-body">
                            <div class="col-xs-12">
                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col width="80">
                                            <col>
                                            <col>
                                            <col>
                                            <col>
                                            <col width="50">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <th><spring:message code="registroEntrada.libro.corto"/></th>
                                            <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                            <th><spring:message code="registroEntrada.extracto"/></th>
                                            <th><spring:message code="regweb.acciones"/></th>

                                        </tr>
                                        </thead>

                                        <tbody>
                                            <form:form modelAttribute="oficioPendienteLlegadaForm" method="post" cssClass="form-horizontal">

                                                <c:forEach var="registroEntrada" items="${oficioRemision.registrosEntrada}" varStatus="status">
                                                    <form:hidden path="oficios[${status.index}].idRegistroEntrada"/>

                                                    <tr>
                                                        <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="yyyy"/> / ${registroEntrada.numeroRegistro}</td>
                                                        <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                        <td><form:select path="oficios[${status.index}].idLibro" items="${libros}" itemLabel="nombre" itemValue="id"/></td>
                                                        <td>${(empty registroEntrada.destino)? registroEntrada.destinoExternoDenominacion : registroEntrada.destino.denominacion}</td>
                                                        <td>${registroEntrada.registroDetalle.extracto}</td>
                                                        <td class="center">
                                                            <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </form:form>
                                        </tbody>
                                    </table>

                                </div>
                            </div>
                        </div>
                    </div>

                </div>

            </c:if>



        </div><!-- /div.row-->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    function imprimirOficio(url) {

        var idModelo = $('#id').val();
        var url2=url.concat(idModelo);

        document.location.href=url2;
    }
</script>


</body>
</html>