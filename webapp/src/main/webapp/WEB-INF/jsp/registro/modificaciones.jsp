<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-12">

    <div class="">

        <table id="historicos" class="table table-bordered table-hover table-striped">
            <colgroup>
                <col>
                <c:if test="${isAdministradorLibro}"> <col> </c:if>
                <col>
                <col width="100">
            </colgroup>
            <thead>
            <tr>
                <th><spring:message code="historicoEntrada.fecha"/></th>
                <c:if test="${isAdministradorLibro}"> <th><spring:message code="historicoEntrada.usuario"/></th> </c:if>
                <th><spring:message code="historicoEntrada.modificacion"/></th>
                <th><spring:message code="historicoEntrada.estado"/></th>
                <th class="center"><spring:message code="regweb.acciones"/></th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="historico" items="${historicos}">
                <tr>
                    <td><fmt:formatDate value="${historico.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                    <c:if test="${isAdministradorLibro}"> <td>${historico.usuario.nombreCompleto}</td> </c:if>
                    <td>${historico.modificacion}</td>
                    <td>
                        <c:import url="../registro/estadosRegistro.jsp">
                            <c:param name="estado" value="${historico.estado}"/>
                        </c:import>
                    </td>

                    <%--BOTÓN COMPARAR--%>
                    <td class="center">

                        <%--REGISTRO ENTRADA--%>
                        <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                            <c:if test="${not empty historico.registroEntradaOriginal}">
                                <a data-toggle="modal" role="button" href="#modalCompararRegistros" onclick="comparaRegistros('${historico.id}')" class="btn btn-warning btn-sm">Comparar</a>
                            </c:if>
                            <c:if test="${empty historico.registroEntradaOriginal}">
                                <a href="javascript:void(0);" class="btn btn-warning disabled btn-sm">Comparar</a>
                            </c:if>
                        </c:if>

                                <%--REGISTRO SALIDA--%>
                        <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                            <c:if test="${not empty historico.registroSalidaOriginal}">
                                <a data-toggle="modal" role="button" href="#modalCompararRegistros" onclick="comparaRegistros('${historico.id}')" class="btn btn-warning btn-sm">Comparar</a>
                            </c:if>
                            <c:if test="${empty historico.registroSalidaOriginal}">
                                <a href="javascript:void(0);" class="btn btn-warning disabled btn-sm">Comparar</a>
                            </c:if>
                        </c:if>

                    </td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>

</div>

<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:import url="../registroEntrada/comparaRegistros.jsp"/>
</c:if>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <c:import url="../registroSalida/comparaRegistros.jsp"/>
</c:if>


