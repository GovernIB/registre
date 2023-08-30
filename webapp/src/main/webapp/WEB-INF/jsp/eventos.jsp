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

                        <c:import url="modulos/mensajes.jsp"/>

                    </div>
                </div>

                <div class="row">

                    <!-- Panel Lateral -->
                    <div class="col-xs-5">

                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-list"></i> <strong>Eventos:</strong> Resumen del cálculo del evento </h3>
                            </div>

                            <div class="panel-body">

                                <dl class="detalle_registro">
                                    El Evento es un nuevo campo de los registros que indica cual será su próxima acción dependiendo
                                    del destino del registro. Puede ser: <strong>Oficio Interno, Oficio Externo, Oficio Sir o Distribuir</strong>.
                                    <br>
                                    Es necesario que todos los registros del sistema tengan un Evento asignado.
                                    <br>
                                    Mediante está pantalla se podrá agilizar el proceso de asignar un evento a cada uno de los registros del sistema.
                                    Paralelamente, un proceso automático irá asignando evento a los registros que no se pueda hacer directamente.
                                    <hr class="divider-warning">
                                    <dt>Registros de entrada totales:</dt> <dd> ${totalEntradas}</dd>
                                    <dt>Registros de salida totales:</dt> <dd> ${totalSalidas}</dd>
                                </dl>

                            </div>
                        </div>

                    </div>

                    <!-- Panel central Oficinas -->
                    <div class="col-xs-7">

                    </div>

                </div>
            </div>
        </div>

        <c:import url="modulos/pie.jsp"/>

    </body>

</html>


