<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 pull-right">

    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-clock-o fa-fw"></i> <strong><spring:message code="registroEntrada.trazabilidad"/></strong></h3>
        </div>

        <div class="panel-body">
            <ul class="timeline">

                <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="status">

                    <%--Recibido via SIR--%>
                    <c:if test="${trazabilidad.oficioRemision == null}">

                        <%--ASIENTO REGISTRAL SIR--%>
                        <li class="timeline-inverted">
                            <c:set var="asientoRegistralSir" value="${trazabilidad.asientoRegistralSir}" scope="request"/>
                            <c:import url="../trazabilidad/asientoRegistralSir.jsp"/>
                        </li>

                        <%--REGISTRO ENTRADA DESTINO--%>
                        <c:if test="${trazabilidad.registroEntradaDestino != null}">
                            <li>
                                <c:set var="registroEntradaDestino" value="${trazabilidad.registroEntradaDestino}" scope="request"/>
                                <c:import url="../trazabilidad/registroEntradaDestino.jsp">
                                    <c:param name="activo" value="true"/>
                                </c:import>
                            </li>
                        </c:if>

                    </c:if>

                </c:forEach>

            </ul>
        </div>

    </div>

</div>
