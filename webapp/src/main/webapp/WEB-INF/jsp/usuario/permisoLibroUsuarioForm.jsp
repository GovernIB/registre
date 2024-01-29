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
                        a ${permisoLibroUsuarioForm.usuarioEntidad.usuario.nombreCompleto}</strong></li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->


        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong>
                                <c:if test="${permisoLibroUsuarioForm.usuarioEntidad != null}">
                                    <spring:message code="usuario.modificar.permisos"/> ${permisoLibroUsuarioForm.usuarioEntidad.usuario.nombreCompleto}
                                </c:if>
                            </strong>
                        </h3>
                    </div>

                    <!-- Formulario -->
                    <div class="panel-body">

                        <form:form modelAttribute="permisoLibroUsuarioForm" method="post" cssClass="form-horizontal">
                            <form:hidden path="usuarioEntidad.id"/>
                            <div class="form-group col-xs-12">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th style="text-align:center;cursor: pointer;"><spring:message code="libro.libros"/></th>
                                                <c:forEach var="permiso" items="${permisos}" varStatus="status">
                                                    <th style="text-align:center;cursor: pointer;" onclick="seleccionarColumna('${status.index}','${fn:length(permisoLibroUsuarioForm.permisoLibroUsuarios)}');"><spring:message code="permiso.nombre.${permiso}" /></th>
                                                </c:forEach>
                                            </tr>
                                        </thead>
                                        <tbody>

                                            <c:if test="${empty libros}">
                                                <tr>
                                                    <td colspan="9"><spring:message code="permisos.libro.ninguno"/></td>
                                                </tr>
                                            </c:if>
                                            <c:if test="${not empty libros}">

                                                <c:forEach var="libro" items="${libros}" varStatus="contador">
                                                    <c:set var="sizePermisos" value="${RegwebConstantes.TOTAL_PERMISOS}"/>
                                                    <c:set var="inicio" value="${contador.index * sizePermisos}"/>
                                                    <c:set var="fin" value="${inicio+sizePermisos-1}"/>
                                                    <tr>
                                                        <td style="cursor: pointer;" onclick="seleccionarFila('${contador.index}');">${libro.nombreCompleto}</td>
                                                        <c:forEach var="plus" items="${permisoLibroUsuarioForm.permisoLibroUsuarios}" varStatus="status" begin="${inicio}" end="${fin}">
                                                                <form:hidden path="permisoLibroUsuarios[${status.index}].id"/>
                                                                <%--<form:hidden path="permisoLibroUsuarios[${status.index}].libro.id"/>
                                                                <form:hidden path="permisoLibroUsuarios[${status.index}].permiso"/>--%>
                                                                <td style="text-align:center;"><form:checkbox path="permisoLibroUsuarios[${status.index}].activo"/></td>
                                                        </c:forEach>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>

                                    </table>
                                </div>

                            </div>


                    </div>


                </div>
                    <!-- Botonera -->
                    <c:if test="${not empty libros}">
                        <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                    </c:if>
                    <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/usuarioEntidad/list"/>')" class="btn btn-sm">
                    <!-- Fin Botonera -->
                </form:form>
            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">

    //Selecciona todos los permisos de una columna (el mismo permiso de todos los libros)
    function seleccionarColumna(columna, filas){

        var totalPermisos = parseInt(${RegwebConstantes.TOTAL_PERMISOS});
        columna = parseInt(columna);
        filas = filas / totalPermisos;
        var len = parseInt(filas);
        var nombre = "#permisoLibroUsuarios"+columna+"\\.activo1";

        //Selecciona todos los checks o los deselecciona todos a la vez
        if(len>0) {
            if($(nombre).prop('checked')){
                for ( var i = 0; i < len; i++){
                    nombre = "#permisoLibroUsuarios"+columna+"\\.activo1";
                    $(nombre).prop('checked', false);
                    columna = columna + totalPermisos;
                }
            }else{
                for ( var i = 0; i < len; i++){
                    nombre = "#permisoLibroUsuarios"+columna+"\\.activo1";
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
        var nombre = "#permisoLibroUsuarios"+permiso+"\\.activo1";

        //Selecciona todos los checks o los deselecciona todos a la vez
        if(totalPermisos>0) {
            if($(nombre).prop('checked')){
                for ( var i = 0; i < totalPermisos; i++){
                    nombre = "#permisoLibroUsuarios"+permiso+"\\.activo1";
                    $(nombre).prop('checked', false);
                    permiso = permiso + 1;
                }
            }else{
                for ( var i = 0; i < totalPermisos; i++){
                    nombre = "#permisoLibroUsuarios"+permiso+"\\.activo1";
                    $(nombre).prop('checked', true);
                    permiso = permiso + 1;
                }
            }
        }
    }

</script>

</body>
</html>