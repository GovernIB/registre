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

                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-list"></i> <strong>Eventos</strong></h3>
                        </div>

                        <div class="panel-body">
                            <p>Registros de entrada <b>totales:</b> ${totalEntradas}</p>
                            <p>Registros de salida <b>totales:</b> ${totalSalidas}</p>
                            <br>
                            <p>Registros de <b>entrada</b> válidos o pendiente de visar sin evento: ${entradasPendientes}</p>
                            <p>Registros de <b>salida</b> válidos  o pendiente de visar sin evento: ${salidasPendientes}</p>
                            <br>
                            <p>Registros de <b>entrada</b> con evento asignado: ${entradasEvento}</p>
                            <p>Registros de <b>salida</b> con evento asignado: ${salidasEvento}</p>
                            <br>
                            <p>Registros de <b>entrada</b> con evento 0: ${entradasProcesadas}</p>
                            <p>Registros de <b>salida</b> con evento 0: ${salidasProcesadas}</p>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <c:import url="modulos/pie.jsp"/>

</body>
</html>


