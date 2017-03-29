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

                    <%-- Registro que ha salido via SIR --%>
                    <c:if test="${trazabilidad.oficioRemision != null && trazabilidad.oficioRemision.sir}">

                        <%--Enviado via SIR--%>
                        <c:if test="${registro.id == trazabilidad.registroEntradaOrigen.id}">

                            <%--REGISTRO ENTRADA ORIGEN--%>
                            <c:if test="${status.first}">
                                <li>
                                    <c:set var="registroEntradaOrigen" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                                    <c:import url="../trazabilidad/registroEntradaOrigen.jsp">
                                        <c:param name="activo" value="true"/>
                                    </c:import>
                                </li>
                            </c:if>

                            <%--OFICIO REMISION--%>
                            <li>
                                <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                                <c:import url="../trazabilidad/oficioRemision.jsp"/>
                            </li>

                            <%--REGISTRO SALIDA--%>
                            <li>
                                <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                                <c:import url="../trazabilidad/registroSalida.jsp"/>
                            </li>

                            <%--ASIENTO REGISTRAL SIR--%>
                            <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_ACEPTADO}">

                                <li class="timeline-inverted">
                                    <c:set var="asientoRegistralSir" value="${trazabilidad.asientoRegistralSir}" scope="request"/>
                                    <c:import url="../trazabilidad/asientoRegistralSir.jsp"/>
                                </li>

                            </c:if>


                        </c:if>

                    </c:if>

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
