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

                    <div class="alert alert-danger">
                        <div class="row vertical-align">
                            <div class="col-xs-1 text-center">
                                <i class="fa fa-times-circle fa-2x"></i>
                            </div>
                            <div class="col-xs-11">
                                <strong><spring:message code="regweb.aviso"/>: </strong> ${aviso}
                                <c:remove var="aviso" scope="session"/>
                            </div>
                        </div>
                    </div>

                </div>

            </div><!-- /.row -->

        </div>
    </div>

    <c:import url="modulos/pie.jsp"/>

</body>
</html>


