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
                <i class="fa fa-pencil-square-o"></i>
                <strong>
                    <c:if test="${param.tipoRegistro == 'entrada'}">
                        <spring:message code="interesado.interesados"/>
                    </c:if>
                    <c:if test="${param.tipoRegistro == 'salida'}">
                        <spring:message code="registroSalida.destinatarios"/>
                    </c:if>
                </strong>
            </h3>
        </div>

        <div class="panel-body">

                <div class="table-responsive">

                    <c:if test="${empty registro.registroDetalle.interesados}">
                        <div class="alert alert-warning ">
                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="registroEntrada.interesado"/></strong>
                        </div>
                    </c:if>

                    <c:if test="${not empty registro.registroDetalle.interesados}">
                        <table id="interesados" class="table table-bordered table-hover table-striped">
                            <colgroup>
                                <col>
                                <col>
                                <col>
                            </colgroup>
                            <thead>
                            <tr>
                                <th><spring:message code="registroEntrada.interesado"/></th>
                                <th><spring:message code="interesado.tipoInteresado"/></th>
                                <th><spring:message code="representante.representante"/></th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="interesado" items="${registro.registroDetalle.interesados}">
                                <c:if test="${!interesado.isRepresentante}">
                                    <tr>
                                        <td>
                                            <a data-toggle="modal" href="#detalleInteresado" onclick="obtenerInteresado(${interesado.id})">${interesado.nombreCompleto}</a>
                                        </td>
                                        <td>
                                            <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}"><spring:message code="interesado.administracion"/></c:if>
                                            <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}"><spring:message code="persona.fisica"/></c:if>
                                            <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}"><spring:message code="persona.juridica"/></c:if>
                                        </td>
                                        <td class="center">
                                            <c:if test="${interesado.representante != null}">
                                                <span data-toggle="modal" href="#detalleInteresado" onclick="obtenerInteresado(${interesado.representante.id})" class="label label-success">${interesado.representante.nombreCompleto}</span>
                                            </c:if>

                                            <c:if test="${interesado.representante == null}">
                                                <span class="label label-danger">No</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>


                </div>

        </div>
    </div>
</div>
<%--Modal para ver los dtos de un Interesado--%>
<c:import url="../registro/detalleInteresado.jsp"/>
