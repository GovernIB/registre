<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 pull-right">

    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-clock-o fa-fw"></i> <strong><spring:message code="registroEntrada.trazabilidad"/></strong></h3>
        </div>

        <div class="panel-body">
            <ul class="timeline">

                <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="status">

                    <%-- Registro que ha salido via SIR --%>
                    <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_OFICIO_SIR}">

                        <%--REGISTRO ENTRADA ORIGEN--%>
                        <c:if test="${status.first}">
                            <li>
                                <c:set var="registroEntradaOrigen" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                                <c:import url="../trazabilidad/registroEntradaOrigen.jsp">
                                    <c:param name="activo" value="true"/>
                                </c:import>
                            </li>
                        </c:if>

                        <%--REGISTRO SALIDA--%>
                        <li>
                            <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                            <c:import url="../trazabilidad/registroSalida.jsp"/>
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
                                <c:set var="registroEnviado" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                                <c:import url="../trazabilidad/asientoDestino.jsp"/>
                            </li>

                        </c:if>

                    </c:if>

                    <%--Recibido via SIR--%>
                    <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR}">

                        <%--REGISTRO SIR--%>
                        <li class="timeline-inverted">
                            <c:set var="registroSir" value="${trazabilidad.registroSir}" scope="request"/>
                            <c:import url="../trazabilidad/registroSir.jsp"/>
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

                    <%-- Oficio Remision --%>
                    <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_OFICIO}">

                        <%-- Tipo Oficio Remision Entrada --%>
                        <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">

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

                                <%--REGISTRO SALIDA--%>
                                <li>
                                    <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                                    <c:import url="../trazabilidad/registroSalida.jsp"/>
                                </li>

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

                            <c:if test="${registro.id == trazabilidad.registroEntradaDestino.id}">

                                <%--REGISTRO ENTRADA ORIGEN--%>
                                <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                    <li class="timeline-inverted">
                                        <c:set var="registroEntradaOrigen" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                                        <c:import url="../trazabilidad/registroEntradaOrigen.jsp">
                                            <c:param name="activo" value="false"/>
                                        </c:import>
                                    </li>
                                </c:if>

                                <%--REGISTRO SALIDA--%>
                                <li class="timeline-inverted">
                                    <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                                    <c:import url="../trazabilidad/registroSalida.jsp"/>
                                </li>

                                <%--OFICIO REMISION--%>
                                <li class="timeline-inverted">
                                    <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                                    <c:import url="../trazabilidad/oficioRemision.jsp"/>
                                </li>

                                <%--REGISTRO ENTRADA--%>
                                <li>
                                    <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>
                                    <div class="timeline-panel timeline-panel-activo-re">
                                        <div class="timeline-heading">
                                            <h4 class="timeline-title"><spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</h4>
                                            <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                        </div>
                                        <div class="timeline-body">
                                            <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${registro.oficina.denominacion}</small></p>
                                        </div>
                                    </div>
                                </li>

                            </c:if>

                        </c:if>

                        <%-- Tipo Oficio Remision Salida --%>
                        <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">

                            <%--REGISTRO SALIDA--%>
                            <li class="timeline-inverted">
                                <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                                <c:import url="../trazabilidad/registroSalida.jsp"/>
                            </li>

                            <%--OFICIO REMISION--%>
                            <li class="timeline-inverted">
                                <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                                <c:import url="../trazabilidad/oficioRemision.jsp"/>
                            </li>

                            <%--REGISTRO ENTRADA--%>
                            <li>
                                <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>
                                <div class="timeline-panel timeline-panel-activo-re">
                                    <div class="timeline-heading">
                                        <h4 class="timeline-title"><spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</h4>
                                        <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                    </div>
                                    <div class="timeline-body">
                                        <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${registro.oficina.denominacion}</small></p>
                                    </div>
                                </div>
                            </li>

                        </c:if>

                    </c:if>

                    <%-- RECTIFICACIÓN --%>
                    <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_RECTIFICACION_ENTRADA}">

                        <c:if test="${registro.id == trazabilidad.registroEntradaDestino.id}">

                            <%--RECTIFICADO--%>
                            <li class="timeline-inverted">
                                <c:set var="registroEntradaOrigen" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                                <c:import url="../trazabilidad/registroEntradaOrigen.jsp">
                                    <c:param name="activo" value="false"/>
                                </c:import>
                            </li>

                            <%--REGISTRO ENTRADA--%>
                            <li>
                                <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>
                                <div class="timeline-panel timeline-panel-activo-re">
                                    <div class="timeline-heading">
                                        <h4 class="timeline-title"><spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</h4>
                                        <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                    </div>
                                    <div class="timeline-body">
                                        <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${registro.oficina.denominacion}</small></p>
                                    </div>
                                </div>
                            </li>

                        </c:if>

                        <%--RECTIFICADO--%>
                        <c:if test="${registro.id == trazabilidad.registroEntradaOrigen.id}">
                            <li class="timeline-inverted">
                                <c:set var="registroEntradaDestino" value="${trazabilidad.registroEntradaDestino}" scope="request"/>
                                <c:import url="../trazabilidad/registroEntradaDestino.jsp"/>
                            </li>
                        </c:if>

                    </c:if>

                </c:forEach>

            </ul>
        </div>

    </div>

</div>
