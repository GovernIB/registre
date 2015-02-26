<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="modulos/imports.jsp"/>
</head>

    <body>

        <c:import url="modulos/menu.jsp"/>

        <div class="row-fluid container main">

            <div class="well well-white">

                <div class="row">

                    <div class="col-xs-12">

                        <ol class="breadcrumb">
                            <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
                        </ol>

                    </div>

                    <div class="col-xs-12">

                        <div class="panel panel-success">

                            <div class="panel-heading">

                                <h3 class="panel-title"><i class="fa fa-list"></i> <strong>Variables de Sesion</strong></h3>
                            </div>

                            <div class="panel-body">


                                <c:if test="${not empty interesados}">
                                    Listado de Interesados en la sesion <br>

                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th>id</th>
                                                <th><spring:message code="regweb.nombre"/></th>
                                                <th><spring:message code="persona.tipoPersona"/></th>
                                                <th>Representante</th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="interesado" items="${interesados}">
                                                <tr>
                                                    <td>${interesado.id}</td>
                                                    <td>
                                                        <c:if test="${interesado.tipo == 1}"> ${interesado.nombreOrganismo} </c:if>
                                                        <c:if test="${interesado.tipo == 2}"> ${interesado.nombrePersonaFisica} </c:if>
                                                        <c:if test="${interesado.tipo == 3}"> ${interesado.nombrePersonaJuridica} </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${interesado.tipo == 1}"> Organismo </c:if>
                                                        <c:if test="${interesado.tipo == 2}"> Persona Física</c:if>
                                                        <c:if test="${interesado.tipo == 3}"> Persona Jurídica </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${interesado.isRepresentante}">
                                                            Si - Representado: ${interesado.representado.id}
                                                        </c:if>

                                                        <c:if test="${!interesado.isRepresentante}">
                                                            No  - Representante: ${interesado.representante.id}
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>

                                            </tbody>
                                        </table>

                                    </div>
                                </c:if>


                            </div>
                        </div>
                    </div>

                </div><!-- /.row -->

                <c:import url="modulos/mensajes.jsp"/>

            </div><!-- /.well-white -->
        </div> <!-- /container -->

        <c:import url="modulos/pie.jsp"/>

    </body>
</html>