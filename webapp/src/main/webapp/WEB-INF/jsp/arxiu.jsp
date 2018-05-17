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

                <div id="mensajes"></div>

                <div class="row">

                    <div class="col-xs-12">

                        <ol class="breadcrumb">
                            <c:import url="modulos/migadepan.jsp">
                                <c:param name="avisos" value="false"/>
                            </c:import>
                        </ol>

                    </div><!-- /.col-xs-12 -->

                    <div class="col-xs-12">

                        <c:import url="modulos/mensajes.jsp"/>

                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-search"></i><strong> Búsqueda de expedientes</strong> </h3>
                            </div>

                            <div class="panel-body">

                                <div class="row">
                                    <div class="col-xs-12">

                                        <strong>App:</strong> ${app}<br>
                                        <strong>Serie:</strong> ${serie}<br>
                                        <strong>initialDate:</strong> ${initialDate}<br>
                                        <strong>endDate:</strong> ${endDate}<br>
                                        <strong>onlyCount:</strong> ${onlyCount}<br>
                                        <strong>expedientPattern:</strong> ${expedientPattern}

                                        <c:if test="${total == 0}">
                                            <div class="alert alert-grey ">
                                                No se ha encontrado ningún expediente
                                            </div>
                                        </c:if>

                                        <c:if test="${total > 0}">

                                            <div class="col-xs-12">

                                                <c:if test="${onlyCount == true}">
                                                    <div class="alert-grey">
                                                        Se han encontrado <strong>${total}</strong> expedientes
                                                    </div>
                                                </c:if>

                                                <c:if test="${onlyCount == false}">

                                                    <div class="alert-grey">
                                                        Se han encontrado <strong>${total}</strong> expedientes
                                                    </div>

                                                    <c:if test="${not empty expedientes}">
                                                        <div class="table-responsive">
                                                            <table class="table table-bordered table-hover table-striped">
                                                                <colgroup>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col>
                                                                    <col width="51">
                                                                </colgroup>
                                                                <thead>
                                                                <tr>
                                                                    <th>Nombre</th>
                                                                    <th>custodyId</th>
                                                                    <th>Tipo</th>
                                                                    <th>Fecha</th>
                                                                    <th>Libro</th>
                                                                    <th>Registro</th>
                                                                    <th>Justificante</th>
                                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="expediente" items="${expedientes}">
                                                                    <tr>
                                                                        <td>${expediente.name}</td>
                                                                        <td>${expediente.custodyId}</td>
                                                                        <td>
                                                                            <c:if test="${expediente.tipoRegistro == 'Entrada'}">
                                                                                <span class="label label-info">Entrada</span>
                                                                            </c:if>

                                                                            <c:if test="${expediente.tipoRegistro == 'Sortida'}">
                                                                                <span class="label label-danger">Salida</span>
                                                                            </c:if>
                                                                        </td>
                                                                        <td><fmt:formatDate value="${expediente.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                        <td>${expediente.codigoLibro}</td>
                                                                        <td>${expediente.numeroRegistroFormateado}</td>
                                                                        <td>
                                                                            <c:if test="${expediente.justificante}">
                                                                                <span class="label label-success">Si</span>
                                                                            </c:if>

                                                                            <c:if test="${not expediente.justificante}">
                                                                                <span class="label label-danger">No</span>
                                                                            </c:if>
                                                                        </td>

                                                                        <td class="center">
                                                                            <c:if test="${not expediente.justificante}">
                                                                                <a class="btn btn-warning btn-sm" target="_blank" href="<c:url value="/asociarJustificante/${expediente.id}"/>" title="Asociar Justificante"><span class="fa fa-pencil"></span></a>
                                                                            </c:if>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>

                                                                </tbody>
                                                            </table>

                                                        </div>
                                                    </c:if>

                                                </c:if>
                                            </div>

                                        </c:if>

                                    </div>
                                </div>
                            </div>
                        </div>


                    </div><!-- /.col-xs-12 -->

                </div><!-- /.row -->


            </div><!-- /.well-white -->
        </div> <!-- /container -->

        <c:import url="modulos/pie.jsp"/>

    </body>
</html>