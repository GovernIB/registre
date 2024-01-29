<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                    <li><a href="<c:url value="/usuarioEntidad/list"/>" ><i class="fa fa-list-ul"></i> <spring:message code="organismo.usuarios"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <strong><spring:message
                            code="usuario.modificar.permisos"/>
                        a ${permisoOrganismoUsuarioForm.usuarioEntidad.usuario.nombreCompleto}</strong></li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong>
                                <c:if test="${permisoOrganismoUsuarioForm.usuarioEntidad != null}">
                                    <spring:message code="usuario.modificar.permisos"/> ${permisoOrganismoUsuarioForm.usuarioEntidad.usuario.nombreCompleto}
                                </c:if>
                            </strong>
                        </h3>
                    </div>

                    <!-- Formulario -->
                    <div class="panel-body">

                        <c:if test="${not empty organismosActivos}">
                            <div class="col-xs-12">
                                <div class="form-group col-xs-12 espaiLinies senseMargeLat">
                                    <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="organismosActivos"><spring:message code="organismo.organismos"/></form:label>
                                    </div>
                                    <div class="col-xs-6">
                                        <form:select id="organismosActivos" path="organismosActivos"  items="${organismosActivos}" itemValue="id" itemLabel="denominacion" cssClass="chosen-select"/>
                                    </div>
                                    <div class="col-xs-1">
                                        <button class="btn btn-warning btn-sm" onclick="asignarOrganismo()"><spring:message code="permisos.asignar"/></button>
                                    </div>
                                    <c:if test="${fn:length(organismosActivos) > 1}">
                                        <div class="col-xs-2">
                                            <button class="btn btn-danger btn-sm" onclick="asignarOrganismosTodos()"><spring:message code="permisos.asignar.todos"/></button>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </c:if>

                        <div class="col-xs-12">
                            <c:if test="${empty organismosAsociados}">
                                <div class="alert alert-grey">
                                    <spring:message code="permiso.organismos.sinAsignar"/>
                                </div>
                            </c:if>
                        </div>

                        <c:if test="${not empty organismosAsociados}">

                            <form:form modelAttribute="permisoOrganismoUsuarioForm" method="post" cssClass="form-horizontal">
                                <form:hidden path="usuarioEntidad.id"/>
                                <div class="form-group col-xs-12">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped">
                                            <thead>
                                                <tr>
                                                    <th style="text-align:center;cursor: pointer;"><spring:message code="organismo.organismo"/></th>
                                                    <c:forEach var="permiso" items="${permisos}" varStatus="status">
                                                        <th style="text-align:center;cursor: pointer;" onclick="seleccionarColumna('${status.index}','${fn:length(permisoOrganismoUsuarioForm.permisoOrganismoUsuarios)}');"><spring:message code="permiso.nombre.${permiso}" /></th>
                                                    </c:forEach>
                                                </tr>
                                            </thead>
                                            <tbody>

                                                <c:forEach var="organismo" items="${organismosAsociados}" varStatus="contador">
                                                    <c:set var="sizePermisos" value="${RegwebConstantes.TOTAL_PERMISOS}"/>
                                                    <c:set var="inicio" value="${contador.index * sizePermisos}"/>
                                                    <c:set var="fin" value="${inicio+sizePermisos-1}"/>
                                                    <tr>
                                                        <td style="cursor: pointer;" onclick="seleccionarFila('${contador.index}');">${organismo.denominacion}</td>
                                                        <c:forEach var="plus" items="${permisoOrganismoUsuarioForm.permisoOrganismoUsuarios}" varStatus="status" begin="${inicio}" end="${fin}">
                                                                <form:hidden path="permisoOrganismoUsuarios[${status.index}].id"/>
                                                                <td style="text-align:center;"><form:checkbox path="permisoOrganismoUsuarios[${status.index}].activo"/></td>
                                                        </c:forEach>
                                                        <td><a class="btn btn-danger btn-sm" href="<c:url value="/permisos/${permisoOrganismoUsuarioForm.usuarioEntidad.id}/${organismo.id}/eliminar"/>" title="Eliminar relación"><span class="fa fa-eraser"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>

                                            </tbody>

                                        </table>
                                    </div>
                                </div>
                                <!-- Botonera -->
                                <c:if test="${not empty organismosAsociados}">
                                    <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                                </c:if>
                                <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/usuarioEntidad/list"/>')" class="btn btn-sm">
                                <!-- Fin Botonera -->
                            </form:form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">

    function asignarOrganismo(){

        var idOrganismo = $('#organismosActivos option:selected').val();
        var url = '<c:url value="/permisos/${permisoOrganismoUsuarioForm.usuarioEntidad.id}"/>/'+idOrganismo+'/asignar';
        document.location.href=url;
    }

    function asignarOrganismosTodos(){

        var url = '<c:url value="/permisos/${permisoOrganismoUsuarioForm.usuarioEntidad.id}"/>/asignarTodos';
        document.location.href=url;
    }

    //Selecciona todos los permisos de una columna (el mismo permiso de todos los libros)
    function seleccionarColumna(columna, filas){

        var totalPermisos = parseInt(${RegwebConstantes.TOTAL_PERMISOS});
        columna = parseInt(columna);
        filas = filas / totalPermisos;
        var len = parseInt(filas);
        var nombre = "#permisoOrganismoUsuarios"+columna+"\\.activo1";

        //Selecciona todos los checks o los deselecciona todos a la vez
        if(len>0) {
            if($(nombre).prop('checked')){
                for ( var i = 0; i < len; i++){
                    nombre = "#permisoOrganismoUsuarios"+columna+"\\.activo1";
                    $(nombre).prop('checked', false);
                    columna = columna + totalPermisos;
                }
            }else{
                for ( var i = 0; i < len; i++){
                    nombre = "#permisoOrganismoUsuarios"+columna+"\\.activo1";
                    $(nombre).prop('checked', true);
                    columna = columna + totalPermisos;
                }
            }
        }
    }


    // Selecciona todos los permisos de una fila (todos los permisos de un libro)
    function seleccionarFila(fila){
        fila = parseInt(fila);
        var totalPermisos = parseInt(${RegwebConstantes.TOTAL_PERMISOS});
        var permiso = fila*totalPermisos;
        var nombre = "#permisoOrganismoUsuarios"+permiso+"\\.activo1";

        //Selecciona todos los checks o los deselecciona todos a la vez
        if(totalPermisos>0) {
            if($(nombre).prop('checked')){
                for ( var i = 0; i < totalPermisos; i++){
                    nombre = "#permisoOrganismoUsuarios"+permiso+"\\.activo1";
                    $(nombre).prop('checked', false);
                    permiso = permiso + 1;
                }
            }else{
                for ( var i = 0; i < totalPermisos; i++){
                    nombre = "#permisoOrganismoUsuarios"+permiso+"\\.activo1";
                    $(nombre).prop('checked', true);
                    permiso = permiso + 1;
                }
            }
        }
    }

</script>

</body>
</html>