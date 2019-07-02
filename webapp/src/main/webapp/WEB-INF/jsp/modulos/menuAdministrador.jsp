<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="btn-group">
    <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
        <spring:message code="menu.configuracion"/> <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <li class="dropdown-submenu-left toggle-left">
            <a href="<c:url value="/entidad/list"/>"><i class="fa fa-chevron-left"></i> <spring:message code="entidad.entidades"/></a>
            <ul class="dropdown-menu">
                <li><a href="<c:url value="/admin/organismo/list"/>"><i class="fa fa-institution"></i> <spring:message code="organismo.listado"/></a></li>
                <li><a href="<c:url value="/admin/oficina/list"/>"><i class="fa fa-home"></i> <spring:message code="oficina.listado"/></a></li>
            </ul>
        </li>

        <li class="submenu-complet"><a href="<c:url value="/usuario/list"/>"><i class="fa fa-users"></i> <spring:message code="menu.usuarios"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/plugin/list"/>"><i class="fa fa-plug"></i> <spring:message code="plugin.plugins"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/propiedadGlobal/list"/>"><i class="fa fa-list"></i> <spring:message code="propiedadGlobal.propiedadesGlobales"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/dir3/datosCatalogo"/>" tabindex="-1"><i class="fa fa-th"></i> <spring:message code="menu.dir3"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/configuracion/editar"/>"><i class="fa fa-gear"></i> <spring:message code="regweb.apariencia"/></a></li>
        <li class="divider"></li>
        <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Administrador_de_RegWeb3.pdf"/>" target="_blank"><i class="fa fa-file-pdf-o"></i> <spring:message code="menu.manual.admin"/></a></li>
    </ul>
</div>