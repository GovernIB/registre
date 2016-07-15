<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 pull-right">
    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message
                    code="interesado.interesados"/></strong></h3>
        </div>
        <div class="panel-body">
            <div class="col-xs-12">
                <div class="table-responsive">
                    <c:if test="${empty asientoRegistralSir.interesados}">
                        <div class="alert alert-warning ">
                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message
                                code="registroEntrada.interesado"/></strong>
                        </div>
                    </c:if>
                    <c:if test="${not empty asientoRegistralSir.interesados}">
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
                            <c:forEach var="interesado" items="${asientoRegistralSir.interesados}">
                                <tr>
                                    <td>
                                        <a data-toggle="modal" href="#detalleInteresadoSir"
                                           onclick="obtenerInteresadoSir(${interesado.id})">${interesado.nombreCompleto}</a>
                                    </td>
                                    <td>
                                        <c:if test="${interesado.tipoInteresado == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}"><spring:message
                                                code="persona.fisica"/></c:if>
                                        <c:if test="${interesado.tipoInteresado == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}"><spring:message
                                                code="persona.juridica"/></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${interesado.representante}">
                                                        <span data-toggle="modal" href="#detalleInteresadoSir"
                                                              onclick="obtenerInteresadoSir(${interesado.id})"
                                                              class="label label-success">${interesado.nombreCompletoRepresentante}</span>
                                        </c:if>

                                        <c:if test="${!interesado.representante}">
                                            <span class="label label-danger">No</span>
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

<c:import url="detalleInteresadoSir.jsp"/>
