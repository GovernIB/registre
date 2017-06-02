<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 col-xs-offset pull-right">

    <div class="panel panel-danger">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-clock-o fa-fw"></i> <strong><spring:message code="registroEntrada.trazabilidad"/></strong></h3>
        </div>

        <div class="panel-body">
            <ul class="timeline">

                <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="status" >

                    <%-- Registro que ha salido via SIR --%>
                    <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_OFICIO_SIR}">

                        <%--REGISTRO SALIDA--%>
                        <li>
                            <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                            <c:import url="../trazabilidad/registroSalida.jsp">
                                <c:param name="activo" value="true"/>
                            </c:import>
                        </li>

                        <%--OFICIO REMISION--%>
                        <li>
                            <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                            <c:import url="../trazabilidad/oficioRemision.jsp"/>
                        </li>

                        <%--OFICIO ACEPTADO O DEVUELTO--%>
                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO || trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}">

                            <li class="timeline-inverted">
                                <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                                <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                                <c:import url="../trazabilidad/asientoDestino.jsp"/>
                            </li>

                        </c:if>

                    </c:if>

                    <%-- Oficio Remision --%>
                    <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_OFICIO}">

                        <%--Tipo Oficio Remision Entrada--%>
                        <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">

                            <%--REGISTRO ENTRADA ORIGEN--%>
                            <li>
                                <c:set var="registroEntradaOrigen" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                                <c:import url="../trazabilidad/registroEntradaOrigen.jsp"/>
                            </li>

                            <%--REGISTRO SALIDA--%>
                            <li>
                                <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                                <c:import url="../trazabilidad/registroSalida.jsp">
                                    <c:param name="activo" value="true"/>
                                </c:import>
                            </li>

                            <%--OFICIO REMISION--%>
                            <li>
                                <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                                <c:import url="../trazabilidad/oficioRemision.jsp"/>
                            </li>

                        </c:if>

                        <%--Tipo Oficio Remision Salida--%>
                        <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">

                            <%--REGISTRO SALIDA--%>
                            <c:if test="${status.first}">
                                <li>
                                    <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                                    <c:import url="../trazabilidad/registroSalida.jsp">
                                        <c:param name="activo" value="true"/>
                                    </c:import>
                                </li>

                            </c:if>

                            <%--OFICIO REMISION--%>
                            <li>
                                <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                                <c:import url="../trazabilidad/oficioRemision.jsp"/>
                            </li>

                            <%--REGISTRO ENTRADA DESTINO--%>
                            <c:if test="${trazabilidad.registroEntradaDestino != null}">
                                <li class="timeline-inverted">
                                    <c:set var="registroEntradaDestino" value="${trazabilidad.registroEntradaDestino}" scope="request"/>
                                    <c:import url="../trazabilidad/registroEntradaDestino.jsp"/>
                                </li>
                            </c:if>
                        </c:if>

                    </c:if>

                    <%-- RECTIFICACIÓN --%>
                    <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_RECTIFICACION_SALIDA}">

                        <c:if test="${registro.id == trazabilidad.registroSalida.id}">

                            <%--RECTIFICADO--%>
                            <li class="timeline-inverted">
                                <c:set var="registroSalida" value="${trazabilidad.registroSalidaRectificado}" scope="request"/>
                                <c:import url="../trazabilidad/registroSalida.jsp">
                                    <c:param name="activo" value="false"/>
                                </c:import>
                            </li>

                            <%--REGISTRO SALIDA--%>
                            <li>
                                <div class="timeline-badge danger"><i class="fa fa-external-link"></i></div>
                                <div class="timeline-panel timeline-panel-activo-rs">
                                    <div class="timeline-heading">
                                        <h4 class="timeline-title"><spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}</h4>
                                        <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                    </div>
                                    <div class="timeline-body">
                                        <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroSalida.oficina"/>:</strong> ${registro.oficina.denominacion}</small></p>
                                    </div>
                                </div>
                            </li>

                        </c:if>

                        <%--RECTIFICADO--%>
                        <c:if test="${registro.id == trazabilidad.registroSalidaRectificado.id}">
                            <li class="timeline-inverted">
                                <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                                <c:import url="../trazabilidad/registroSalida.jsp"/>
                            </li>
                        </c:if>

                    </c:if>

                </c:forEach>

            </ul>
        </div>

    </div>

</div>
