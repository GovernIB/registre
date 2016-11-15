<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 col-xs-offset pull-right">

    <div class="panel panel-danger">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-clock-o fa-fw"></i> <strong><spring:message code="registroEntrada.trazabilidad"/></strong></h3>
        </div>

        <div class="panel-body">
            <ul class="timeline">

                <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="loopStatus" >

                    <%--OFICIO REMISION ENTRADA--%>
                    <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">

                        <%--REGISTRO ENTRADA ORIGEN--%>
                        <li>
                            <c:set var="registroEntradaOrigen" value="${trazabilidad.registroEntradaOrigen}" scope="request"/>
                            <c:import url="../trazabilidad/registroEntradaOrigen.jsp"/>
                        </li>

                        <%--OFICIO REMISION--%>
                        <li>
                            <c:set var="oficioRemision" value="${trazabilidad.oficioRemision}" scope="request"/>
                            <c:import url="../trazabilidad/oficioRemision.jsp"/>
                        </li>

                        <%--REGISTRO SALIDA--%>
                        <li>
                            <c:set var="registroSalida" value="${trazabilidad.registroSalida}" scope="request"/>
                            <c:import url="../trazabilidad/registroSalida.jsp">
                                <c:param name="activo" value="true"/>
                            </c:import>
                        </li>

                    </c:if>

                    <%--OFICIO REMISION SALIDA--%>
                    <c:if test="${trazabilidad.oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">

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

                        <%--REGISTRO ENTRADA DESTINO--%>
                        <c:if test="${trazabilidad.registroEntradaDestino != null}">
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
