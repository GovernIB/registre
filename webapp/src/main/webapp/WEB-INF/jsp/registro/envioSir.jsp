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

<c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="row-fluid container main">

    <div class="well well-white">

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a <c:if test="${oficinaActiva.sirEnvio || oficinaActiva.sirRecepcion}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO}">
                            <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}
                        </c:if>
                        <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO}">
                            <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}
                        </c:if>
                    </li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-${color}">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO}">
                                <strong>Enviar <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado} a ${destino}, mediante SIR</strong>
                            </c:if>
                            <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO}">
                                <strong>Enviar <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado} a ${destino}, mediante SIR</strong>
                            </c:if>

                        </h3>
                    </div>
                    <div class="panel-body">

                        <div class="row">

                          <form:form modelAttribute="envioSirForm" method="post" cssClass="form-horizontal">
                            <div class="col-lg-6">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <strong><spring:message code="oficina.origen"/>: ${registro.oficina.denominacion}</strong>
                                    </div>
                                    <div class="panel-body">
                                        <p><strong><i class="fa fa-home"></i> <spring:message code="registroEntrada.numeroRegistro"/>:</strong> ${registro.numeroRegistroFormateado}</p>
                                        <p><strong><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>:</strong> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-6">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <strong>Oficina destino</strong>
                                    </div>
                                    <div class="panel-body">
                                        <p><strong><i class="fa fa-institution"></i> <spring:message code="registroEntrada.organismoDestino"/>:</strong> ${destino}</p>

                                            <input type="hidden" id="idRegistro" name="idRegistro" value="${registro.id}"/>

                                            <!-- Oficina Sir destinataria -->
                                            <c:if test="${fn:length(oficinasSIR) == 1}">
                                                <p><strong><i class="fa fa-home"></i> <spring:message code="oficina.destino"/>:</strong> ${oficinasSIR[0].denominacion}</p>
                                                <input type="hidden" id="oficinaSIRCodigo" name="oficinaSIRCodigo" value="${oficinasSIR[0].codigo}"/>
                                            </c:if>
                                            <c:if test="${fn:length(oficinasSIR) > 1}">
                                                <div class="form-group">
                                                    <div class="col-xs-4"><strong><i class="fa fa-home"></i> <spring:message code="oficina.destino"/>:</strong></div>
                                                    <div class="col-xs-8">

                                                        <form:select path="oficinaSIRCodigo" items="${oficinasSIR}"
                                                                     itemLabel="denominacion"
                                                                     itemValue="codigo"
                                                                     class="form-control"/>
                                                    </div>
                                                </div>
                                            </c:if>

                                    </div>
                                </div>
                            </div>

                            <div class="form-actions col-xs-12">
                                <input type="submit" value="<spring:message code="regweb.enviar"/>" class="btn btn-${color} btn-sm"/>

                                <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO}">
                                    <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/detalle"/>')" class="btn btn-sm"/>
                                </c:if>
                                <c:if test="${tipoRegistro == RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO}">
                                    <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/registroSalida/${registro.id}/detalle"/>')" class="btn btn-sm"/>
                                </c:if>

                            </div>

                          </form:form>
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