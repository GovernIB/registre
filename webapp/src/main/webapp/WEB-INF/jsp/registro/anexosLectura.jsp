<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == 'entrada'}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == 'salida'}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="col-xs-8 pull-right">

    <div class="panel panel-${color}">

        <div class="panel-heading">
            <h3 class="panel-title">
                <i class="fa fa-pencil-square-o"></i><strong><spring:message code="anexo.anexos"/></strong>
            </h3>
        </div>

        <div class="panel-body">

            <div class="col-xs-12">

                <div class="table-responsive">

                    <c:if test="${empty registro.registroDetalle.anexos}">
                        <div class="alert alert-warning ">
                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="anexo.anexo"/></strong>
                        </div>
                    </c:if>

                    <c:if test="${not empty registro.registroDetalle.anexos}">
                        <table id="interesados" class="table table-bordered table-hover table-striped">
                            <colgroup>
                                <col>
                                <col>
                            </colgroup>
                            <thead>
                            <tr>
                                <th><spring:message code="anexo.titulo"/></th>
                                <th><spring:message code="anexo.tipoDocumento"/></th>
                                <th class="center"><spring:message code="regweb.acciones"/></th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="anexo" items="${registro.registroDetalle.anexos}">

                                    <tr>
                                        <td>
                                            <a data-toggle="modal" href="#detalleAnexo" onclick="obtenerAnexo(${anexo.id})">${anexo.titulo}</a>
                                        </td>
                                        <td>
                                            <spring:message code="tipoDocumento.0${anexo.tipoDocumento}"/>
                                        </td>
                                        <td class="center">
                                            <c:if test="${anexo.modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                            <a class="btn btn-success btn-default btn-sm"
                                               href="<c:url value="/anexo/descargarDocumento/${anexo.id}"/>"
                                               target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                    class="fa fa-download"></span></a>
                                            </c:if>
                                            <c:if test="${anexo.modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED}">
                                            <a class="btn btn-success btn-default btn-sm"
                                               href="<c:url value="/anexo/descargarFirma/${anexo.id}"/>"
                                               target="_blank" title="<spring:message code="anexo.descargar"/>"><span
                                                    class="fa fa-download"></span></a>
                                            </c:if>
                                        </td>
                                    </tr>

                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>

        </div>
    </div>
</div>
<%--Modal para ver los dtos de un Interesado--%>
<c:import url="../registro/detalleAnexo.jsp"/>
