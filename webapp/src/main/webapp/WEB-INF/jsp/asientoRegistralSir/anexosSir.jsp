<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 pull-right">

    <div class="panel panel-success">

        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message
                    code="anexo.anexos"/></strong>: <spring:message
                    code="tipoDocumentacionFisica.${asientoRegistralSir.documentacionFisica}"/>
            </h3>
        </div>

        <div class="panel-body">
            <div class="col-xs-12">
                <div id="anexosdiv" class="table-responsive">

                    <c:if test="${empty asientoRegistralSir.anexos}">
                        <div class="alert alert-warning alert-dismissable">
                            <strong><spring:message code="regweb.listado.vacio"/> <spring:message code="anexo.anexo"/></strong>
                        </div>
                    </c:if>

                    <c:if test="${not empty asientoRegistralSir.anexos}">
                        <table id="anexos" class="table table-bordered table-hover table-striped">
                            <colgroup>
                                <col>
                                <col>
                                <col width="50">
                            </colgroup>
                            <thead>
                            <tr>
                                <th><spring:message code="anexo.titulo"/></th>
                                <th><spring:message code="anexo.tipoDocumento"/></th>
                                <th><spring:message code="regweb.acciones"/></th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="anexo" items="${asientoRegistralSir.anexos}">
                                <tr id="anexo${anexo.id}">
                                    <td>${anexo.nombreFichero}</td>
                                    <td><spring:message code="tipoDocumento.${anexo.tipoDocumento}"/></td>
                                    <td class="center"><a class="btn btn-success btn-default btn-sm"
                                                          href="<c:url value="/archivo/${anexo.anexo.id}"/>"
                                                          target="_blank"
                                                          title="<spring:message code="anexo.descargar"/>"><span
                                            class="fa fa-download"></span></a></td>
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
