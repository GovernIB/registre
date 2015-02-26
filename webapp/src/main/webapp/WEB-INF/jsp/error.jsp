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
                        <c:import url="modulos/migadepan.jsp"/>
                    </ol>

                </div>

            </div><!-- /.row -->

            <div class="row">
                <div class="col-xs-12">
                    <div class="panel panel-danger">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-bug"></i> <strong>Error</strong></h3>
                        </div>

                        <div class="panel-body">

                            <div class="box-content">
                                <div class="alert alert-danger alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    <h4 class="alert-heading"><spring:message code="error.exception"/></h4>
                                    <br>
                                    <p>Excepcion:  ${exception}</p>
                                    <p>URL: ${url}</p>
                                    <p>
                                        <a class="btn btn-danger" href="#myModal" role="button" class="btn" data-toggle="modal">Ver error</a>
                                    </p>

                                </div>

                                <div id="myModal" class="modal fade bs-example-modal-lg">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                                                <h3 id="myModalLabel">Excepcion:  ${exception}</h3>
                                            </div>
                                            <div class="modal-body">
                                                <p>${trazaError}</p>
                                            </div>
                                            <div class="modal-footer">
                                                <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message code="regweb.cerrar"/></button>
                                            </div>
                                        </div>
                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>
                </div>
            </div>

        </div>
    </div>
    

    <c:import url="modulos/pie.jsp"/>

</body>
</html>


