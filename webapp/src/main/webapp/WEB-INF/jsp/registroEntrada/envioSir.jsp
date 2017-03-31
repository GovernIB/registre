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
                    <li><a <c:if test="${oficinaActiva.sir}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroEntrada.registroEntrada"/> ${registroEntrada.numeroRegistroFormateado}cir</li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong>
                                Enviar <spring:message code="registroEntrada.registroEntrada"/> a SIR
                            </strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <div class="row">

                            <div class="col-lg-6">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <spring:message code="registroEntrada.registroEntrada"/> a enviar: ${registroEntrada.numeroRegistroFormateado}
                                    </div>
                                    <div class="panel-body">
                                        <p><strong><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficina"/>:</strong> ${registroEntrada.oficina.denominacion}</p>
                                        <p><strong><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>:</strong> <fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                        <p><strong><i class="fa fa-book"></i> <spring:message code="libro.libro"/>:</strong> ${registroEntrada.libro.nombre}</p>
                                    </div>
                                    <div class="panel-footer">
                                        Panel Footer
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-6">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        Oficina destino
                                    </div>
                                    <div class="panel-body">
                                        <p><strong><i class="fa fa-institution"></i> <spring:message code="registroEntrada.organismoDestino"/>:</strong> ${registroEntrada.destinoExternoDenominacion}</p>

                                        <form:form modelAttribute="envioSirForm" method="post" cssClass="form-horizontal">

                                            <input type="hidden" id="idRegistro" name="idRegistro" value="${registroEntrada.id}"/>
                                            <input type="hidden" id="idLibro" name="idLibro" value="${registroEntrada.libro.id}"/>
                                            <div class="form-group">
                                                <div class="col-xs-3"><strong><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficina"/>:</strong></div>
                                                <div class="col-xs-9">
                                                    <!-- Oficina Sir destinataria -->
                                                    <form:select path="oficinaSIRCodigo" items="${oficinasSIR}"
                                                                 itemLabel="denominacion"
                                                                 itemValue="codigo"
                                                                 class="form-control"/>
                                                </div>
                                            </div>

                                            <div class="form-actions">
                                                <input type="submit" value="Enviar" class="btn btn-primary btn-sm">
                                            </div>


                                        </form:form>
                                    </div>
                                    <div class="panel-footer">
                                        Panel Footer
                                    </div>
                                </div>
                            </div>

                        </div>



                    </div>
                </div>

            </div>

            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>




        </div><!-- /div.row-->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>