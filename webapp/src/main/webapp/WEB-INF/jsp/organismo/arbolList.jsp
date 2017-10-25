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

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
                    <li><a href="<c:url value="/entidad/${entidad.id}/edit"/>" ><i class="fa fa-globe"></i> ${entidad.nombre}</a></li>
                    <li class="active"><i class="fa fa-sitemap"></i> <spring:message code="organismo.arbol"/> ${entidad.nombre}</li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <c:if test="${empty organismosPrimerNivel}">
                    <div class="alert alert-grey alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="organismo.organismo"/></strong>
                    </div>
                </c:if>

                <c:if test="${not empty organismosPrimerNivel}">

                    <div class="btn-group pad-left">
                        <button type="button" class="btn btn-primary btn-xs" onclick="amaga(${fn:length(organismosSegundoNivel)},${fn:length(organismosTercerNivel)},${fn:length(organismosCuartoNivel)})"><i class="fa fa-sitemap fa-rotate-180"></i> <spring:message code="organismo.arbol.cierra"/></button>
                    </div>
                    <div class="btn-group pad-left">
                        <button type="button" class="btn btn-info btn-xs" onclick="goTo('<c:url value="/organismo/arbolList"/>')"><i class="fa fa-sitemap"></i> <spring:message code="organismo.arbol.abre"/></button>
                    </div>
                    <div class="btn-group pad-left">
                        <button type="button" id="infoCopy" class="btn btn-default btn-xs hide" disabled style="cursor:default"><i class="fa fa-info-circle colophon">  <spring:message code="organismo.arbol.copiar"/></i></button>
                    </div>

                    <!-- LEYENDA -->
                    <div class="col-xs-2 button-right">
                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <i class="fa fa-comment-o"></i> <strong><spring:message code="regweb.leyenda"/></strong>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-footer">
                                <div class="row">
                                    <div class="col-xs-12 pad-bottom5">
                                        <button type="button" class="btn-danger-llegenda btn-xs"><i class="fa fa-globe"></i> <spring:message code="entidad.entidad"/></button>
                                    </div>
                                    <div class="col-xs-12 pad-bottom5">
                                        <button type="button" class="btn-primary btn-xs"><i class="fa fa-globe"></i> <spring:message code="entidad.unidadOrganica"/></button>
                                    </div>
                                    <div class="col-xs-12 pad-bottom5">
                                        <button type="button" class="btn-edp btn-xs"><i class="fa fa-globe"></i> <spring:message code="organismo.edp"/></button>
                                    </div>
                                <c:if test="${librosTotal > 0}">
                                    <div class="col-xs-12 pad-bottom5">
                                        <button type="button" class="btn-llibre-llegenda btn-xs"><i class="fa fa-book"></i> <spring:message code="libro.libro"/></button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty oficinasResponsables}">
                                    <div class="col-xs-12 pad-bottom5">
                                        <button type="button" class="btn-warning btn-xs"><i class="fa fa-home"></i> <spring:message code="regweb.oficina.principal"/></button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty oficinasDependientes}">
                                    <div class="col-xs-12 pad-bottom5">
                                        <button type="button" class="btn-ofaux btn-xs col-xs-12"><i
                                                class="fa fa-home"></i> <spring:message code="regweb.oficina.auxiliar"/>
                                        </button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty oficinasSir}">
                                    <div class="col-xs-12 pad-bottom5">
                                        <button type="button" class="btn-ofsir btn-xs col-xs-12"><i
                                                class="fa fa-exchange"></i> <spring:message code="regweb.oficina.sir"/>
                                        </button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty oficinasOrganizativas}">
                                    <div class="col-xs-12 pad-bottom5">
                                        <button type="button" class="btn-success btn-xs"><i class="fa fa-institution"></i> <spring:message code="regweb.oficina.organizativa"/></button>
                                    </div>
                                </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="tree">
                        <ul>
                            <li>
                                <span class="panel-heading btn-danger vuitanta-percent" id="entidad" onclick="copyToClipboard(this)" style="cursor:copy" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                        class=""></i> ${entidad.codigoDir3} - ${entidad.nombre}</span>
                                <ul>

                                    <c:set var="contadorPrimer" value="0"></c:set>
                                    <c:set var="contadorSegon" value="0"></c:set>
                                    <c:set var="contadorTercer" value="0"></c:set>
                                    <c:set var="contadorQuart" value="0"></c:set>
                                    <c:set var="contadorCinque" value="0"></c:set>
                                    <c:set var="contadorSise" value="0"></c:set>

                                    <c:forEach var="organismo1" items="${organismosPrimerNivel}">
                                        <li>
                                            <c:if test="${organismo1.edp == false}">
                                                <span class="panel-heading btn-primary vuitanta-percent" id="govern"
                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                        class=""></i> ${organismo1.codigo} - ${organismo1.denominacion}</span>
                                            </c:if>
                                            <c:if test="${organismo1.edp == true}">
                                                <span class="panel-heading btn-edp vuitanta-percent" id="govern"
                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                        class=""></i> ${organismo1.codigo} - ${organismo1.denominacion}</span>
                                            </c:if>

                                            <ul>
                                                <!-- **** Entra si algún Organismo de primer nivel tiene libros ***-->
                                                <c:forEach var="libroOrganismo" items="${librosOrganismoPrimerNivel}">
                                                    <c:if test="${libroOrganismo.value == organismo1.id}">
                                                        <li>
                                                            <a href="javascript:void(0);"><span
                                                                    class="panel-heading btn-llibre vuitanta-percent"
                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                    class="fa fa-book"></i> ${libroOrganismo.key}</span></a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>

                                                <!-- **** Oficinas ***-->
                                                <c:forEach var="oficinaResponsable" items="${oficinasResponsables}">
                                                    <c:if test="${oficinaResponsable.organismoResponsable.id == organismo1.id}">
                                                        <li>
                                                            <a href="javascript:void(0);"><span
                                                                    class="panel-heading btn-warning vuitanta-percent"
                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                    class="fa fa-home"></i> ${oficinaResponsable.codigo} - ${oficinaResponsable.denominacion}</span></a>
                                                            <ul>
                                                                <c:forEach var="oficinasDependiente" items="${oficinasDependientes}">
                                                                    <c:if test="${oficinasDependiente.oficinaResponsable.id == oficinaResponsable.id}">
                                                                        <li>
                                                                            <a href="javascript:void(0);"><span
                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                    class="fa fa-home"></i> ${oficinasDependiente.codigo} - ${oficinasDependiente.denominacion}</span></a>
                                                                            <ul>
                                                                                <c:forEach var="oficinasDependiente2" items="${oficinasDependientes}">
                                                                                    <c:if test="${oficinasDependiente2.oficinaResponsable.id == oficinasDependiente.id}">
                                                                                        <li>
                                                                                            <a href="javascript:void(0);"><span
                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                    class="fa fa-home"></i> ${oficinasDependiente2.codigo} - ${oficinasDependiente2.denominacion}</span></a>
                                                                                        </li>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                            </ul>
                                                                        </li>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </ul>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                                <!-- **** Oficinas Funcionales ***-->
                                                <c:forEach var="oficinaOrganizativa" items="${oficinasOrganizativas}">
                                                    <c:if test="${oficinaOrganizativa.id == organismo1.id}">
                                                        <li>
                                                            <a href="javascript:void(0);"><span
                                                                    class="panel-heading btn-success vuitanta-percent"
                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                    class="fa fa-institution"></i> ${oficinaOrganizativa.nombre}</span></a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                                <!-- **** Oficinas Sir ***-->
                                                <c:forEach var="oficinaSir" items="${oficinasSir}">
                                                    <c:if test="${oficinaSir.id == organismo1.id}">
                                                        <li>
                                                            <a href="javascript:void(0);"><span
                                                                    class="panel-heading btn-ofsir vuitanta-percent"
                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                    class="fa fa-exchange"></i> ${oficinaSir.nombre}</span></a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>

                                                <c:forEach var="organismo2" items="${organismosSegundoNivel}">
                                                    <c:if test="${organismo2.organismoSuperior.id == organismo1.id}">
                                                        <li>

                                                            <c:if test="${organismo2.edp  == false}">
                                                                <span class="panel-heading btn-primary vuitanta-percent"
                                                                      id="primerNivell${contadorPrimer}"
                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                        class=""></i> ${organismo2.codigo} - ${organismo2.denominacion}</span>
                                                            </c:if>
                                                            <c:if test="${organismo2.edp == true}">
                                                                <span class="panel-heading btn-edp vuitanta-percent"
                                                                      id="primerNivell${contadorPrimer}"
                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                        class=""></i> ${organismo2.codigo} - ${organismo2.denominacion}</span>
                                                            </c:if>

                                                            <c:set var="contadorPrimer" value="${contadorPrimer+1}"></c:set>
                                                            <ul>
                                                                <!-- **** Entra si algún Organismo de segundo nivel tiene libros ***-->
                                                                <c:forEach var="libroOrganismo" items="${librosOrganismoSegundoNivel}">
                                                                    <c:if test="${libroOrganismo.value == organismo2.id}">
                                                                        <li>
                                                                            <a href="javascript:void(0);"><span
                                                                                    class="panel-heading btn-llibre vuitanta-percent"
                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                    class="fa fa-book"></i> ${libroOrganismo.key}</span></a>
                                                                        </li>
                                                                    </c:if>
                                                                </c:forEach>

                                                                <!-- **** Oficinas ***-->
                                                                <c:forEach var="oficinaResponsable" items="${oficinasResponsables}">
                                                                    <c:if test="${oficinaResponsable.organismoResponsable.id == organismo2.id}">
                                                                        <li>
                                                                            <a href="javascript:void(0);"><span
                                                                                    class="panel-heading btn-warning vuitanta-percent"
                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                    class="fa fa-home"></i> ${oficinaResponsable.codigo} - ${oficinaResponsable.denominacion}</span></a>
                                                                            <ul>
                                                                                <c:forEach var="oficinasDependiente" items="${oficinasDependientes}">
                                                                                    <c:if test="${oficinasDependiente.oficinaResponsable.id == oficinaResponsable.id}">
                                                                                        <li>
                                                                                            <a href="javascript:void(0);"><span
                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                    class="fa fa-home"></i> ${oficinasDependiente.codigo} - ${oficinasDependiente.denominacion}</span></a>
                                                                                            <ul>
                                                                                                <c:forEach var="oficinasDependiente2" items="${oficinasDependientes}">
                                                                                                    <c:if test="${oficinasDependiente2.oficinaResponsable.id == oficinasDependiente.id}">
                                                                                                        <li>
                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente2.codigo} - ${oficinasDependiente2.denominacion}</span></a>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:forEach>
                                                                                            </ul>
                                                                                        </li>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                            </ul>
                                                                        </li>
                                                                    </c:if>
                                                                </c:forEach>
                                                                <!-- **** Oficinas Funcionales ***-->
                                                                <c:forEach var="oficinaOrganizativa" items="${oficinasOrganizativas}">
                                                                    <c:if test="${oficinaOrganizativa.id == organismo2.id}">
                                                                        <li>
                                                                            <a href="javascript:void(0);"><span
                                                                                    class="panel-heading btn-success vuitanta-percent"
                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                    class="fa fa-institution"></i> ${oficinaOrganizativa.nombre}</span></a>
                                                                        </li>
                                                                    </c:if>
                                                                </c:forEach>
                                                                <!-- **** Oficinas Sir ***-->
                                                                <c:forEach var="oficinaSir" items="${oficinasSir}">
                                                                    <c:if test="${oficinaSir.id == organismo2.id}">
                                                                        <li>
                                                                            <a href="javascript:void(0);"><span
                                                                                    class="panel-heading btn-ofsir vuitanta-percent"
                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                    class="fa fa-exchange"></i> ${oficinaSir.nombre}</span></a>
                                                                        </li>
                                                                    </c:if>
                                                                </c:forEach>

                                                                <c:forEach var="organismo3" items="${organismosTercerNivel}">
                                                                    <c:if test="${organismo3.organismoSuperior.id == organismo2.id}">
                                                                        <li>

                                                                            <c:if test="${organismo3.edp == false}">
                                                                                <span class="panel-heading btn-primary vuitanta-percent"
                                                                                      id="segonNivell${contadorSegon}"
                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                        class=""></i> ${organismo3.codigo} - ${organismo3.denominacion}</span>
                                                                            </c:if>
                                                                            <c:if test="${organismo3.edp == true}">
                                                                                <span class="panel-heading btn-edp vuitanta-percent"
                                                                                      id="segonNivell${contadorSegon}"
                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                        class=""></i> ${organismo3.codigo} - ${organismo3.denominacion}</span>
                                                                            </c:if>

                                                                            <c:set var="contadorSegon" value="${contadorSegon+1}"></c:set>
                                                                            <ul>
                                                                                <!-- **** Entra si algún Organismo de tercer nivel tiene libros ***-->
                                                                                <c:forEach var="libroOrganismo" items="${librosOrganismoTercerNivel}">
                                                                                    <c:if test="${libroOrganismo.value == organismo3.id}">
                                                                                        <li>
                                                                                            <a href="javascript:void(0);"><span
                                                                                                    class="panel-heading btn-llibre vuitanta-percent"
                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                    class="fa fa-book"></i> ${libroOrganismo.key}</span></a>
                                                                                        </li>
                                                                                    </c:if>
                                                                                </c:forEach>

                                                                                <!-- **** Oficinas ***-->
                                                                                <c:forEach var="oficinaResponsable" items="${oficinasResponsables}">
                                                                                    <c:if test="${oficinaResponsable.organismoResponsable.id == organismo3.id}">
                                                                                        <li>
                                                                                            <a href="javascript:void(0);"><span
                                                                                                    class="panel-heading btn-warning vuitanta-percent"
                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                    class="fa fa-home"></i> ${oficinaResponsable.codigo} - ${oficinaResponsable.denominacion}</span></a>
                                                                                            <ul>
                                                                                                <c:forEach var="oficinasDependiente" items="${oficinasDependientes}">
                                                                                                    <c:if test="${oficinasDependiente.oficinaResponsable.id == oficinaResponsable.id}">
                                                                                                        <li>
                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente.codigo} - ${oficinasDependiente.denominacion}</span></a>
                                                                                                            <ul>
                                                                                                                <c:forEach var="oficinasDependiente2" items="${oficinasDependientes}">
                                                                                                                    <c:if test="${oficinasDependiente2.oficinaResponsable.id == oficinasDependiente.id}">
                                                                                                                        <li>
                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente2.codigo} - ${oficinasDependiente2.denominacion}</span></a>
                                                                                                                        </li>
                                                                                                                    </c:if>
                                                                                                                </c:forEach>
                                                                                                            </ul>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:forEach>
                                                                                            </ul>
                                                                                        </li>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                                <!-- **** Oficinas Funcionales ***-->
                                                                                <c:forEach var="oficinaOrganizativa" items="${oficinasOrganizativas}">
                                                                                    <c:if test="${oficinaOrganizativa.id == organismo3.id}">
                                                                                        <li>
                                                                                            <a href="javascript:void(0);"><span
                                                                                                    class="panel-heading btn-success vuitanta-percent"
                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                    class="fa fa-institution"></i> ${oficinaOrganizativa.nombre}</span></a>
                                                                                        </li>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                                <!-- **** Oficinas Sir ***-->
                                                                                <c:forEach var="oficinaSir" items="${oficinasSir}">
                                                                                    <c:if test="${oficinaSir.id == organismo3.id}">
                                                                                        <li>
                                                                                            <a href="javascript:void(0);"><span
                                                                                                    class="panel-heading btn-ofsir vuitanta-percent"
                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                    class="fa fa-exchange"></i> ${oficinaSir.nombre}</span></a>
                                                                                        </li>
                                                                                    </c:if>
                                                                                </c:forEach>

                                                                                <c:forEach var="organismo4" items="${organismosCuartoNivel}">
                                                                                    <c:if test="${organismo4.organismoSuperior.id == organismo3.id}">
                                                                                        <li>

                                                                                            <c:if test="${organismo4.edp == false}">
                                                                                                <span class="panel-heading btn-primary vuitanta-percent"
                                                                                                      id="tercerNivell${contadorTercer}"
                                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                        class=""></i> ${organismo4.codigo} - ${organismo4.denominacion}</span>
                                                                                            </c:if>
                                                                                            <c:if test="${organismo4.edp == true}">
                                                                                                <span class="panel-heading btn-edp vuitanta-percent"
                                                                                                      id="tercerNivell${contadorTercer}"
                                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                        class=""></i> ${organismo4.codigo} - ${organismo4.denominacion}</span>
                                                                                            </c:if>

                                                                                            <c:set var="contadorTercer" value="${contadorTercer+1}"></c:set>
                                                                                            <ul>
                                                                                                <!-- **** Entra si algún Organismo de cuarto nivel tiene libros ***-->
                                                                                                <c:forEach var="libroOrganismo" items="${librosOrganismoCuartoNivel}">
                                                                                                    <c:if test="${libroOrganismo.value == organismo4.id}">
                                                                                                        <li>
                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                    class="panel-heading btn-llibre vuitanta-percent"
                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                    class="fa fa-book"></i> ${libroOrganismo.key}</span></a>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:forEach>

                                                                                                <!-- **** Oficinas ***-->
                                                                                                <c:forEach var="oficinaResponsable" items="${oficinasResponsables}">
                                                                                                    <c:if test="${oficinaResponsable.organismoResponsable.id == organismo4.id}">
                                                                                                        <li>
                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                    class="panel-heading btn-warning vuitanta-percent"
                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                    class="fa fa-home"></i> ${oficinaResponsable.codigo} - ${oficinaResponsable.denominacion}</span></a>
                                                                                                            <ul>
                                                                                                                <c:forEach var="oficinasDependiente" items="${oficinasDependientes}">
                                                                                                                    <c:if test="${oficinasDependiente.oficinaResponsable.id == oficinaResponsable.id}">
                                                                                                                        <li>
                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente.codigo} - ${oficinasDependiente.denominacion}</span></a>
                                                                                                                            <ul>
                                                                                                                                <c:forEach var="oficinasDependiente2" items="${oficinasDependientes}">
                                                                                                                                    <c:if test="${oficinasDependiente2.oficinaResponsable.id == oficinasDependiente.id}">
                                                                                                                                        <li>
                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente2.codigo} - ${oficinasDependiente2.denominacion}</span></a>
                                                                                                                                        </li>
                                                                                                                                    </c:if>
                                                                                                                                </c:forEach>
                                                                                                                            </ul>
                                                                                                                        </li>
                                                                                                                    </c:if>
                                                                                                                </c:forEach>
                                                                                                            </ul>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:forEach>
                                                                                                <!-- **** Oficinas Funcionales ***-->
                                                                                                <c:forEach var="oficinaOrganizativa" items="${oficinasOrganizativas}">
                                                                                                    <c:if test="${oficinaOrganizativa.id == organismo4.id}">
                                                                                                        <li>
                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                    class="panel-heading btn-success vuitanta-percent"
                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                    class="fa fa-institution"></i> ${oficinaOrganizativa.nombre}</span></a>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:forEach>
                                                                                                <!-- **** Oficinas Sir ***-->
                                                                                                <c:forEach var="oficinaSir" items="${oficinasSir}">
                                                                                                    <c:if test="${oficinaSir.id == organismo4.id}">
                                                                                                        <li>
                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                    class="panel-heading btn-ofsir vuitanta-percent"
                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                    class="fa fa-exchange"></i> ${oficinaSir.nombre}</span></a>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:forEach>

                                                                                                <c:forEach var="organismo5" items="${organismosQuintoNivel}">
                                                                                                    <c:if test="${organismo5.organismoSuperior.id == organismo4.id}">
                                                                                                        <li>

                                                                                                            <c:if test="${organismo5.edp == false}">
                                                                                                                <span class="panel-heading btn-primary vuitanta-percent"
                                                                                                                      id="quartNivell${contadorQuart}"
                                                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                        class=""></i> ${organismo5.codigo} - ${organismo5.denominacion}</span>
                                                                                                            </c:if>
                                                                                                            <c:if test="${organismo5.edp == true}">
                                                                                                                <span class="panel-heading btn-edp vuitanta-percent"
                                                                                                                      id="quartNivell${contadorQuart}"
                                                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                        class=""></i> ${organismo5.codigo} - ${organismo5.denominacion}</span>
                                                                                                            </c:if>

                                                                                                            <c:set var="contadorQuart" value="${contadorQuart+1}"></c:set>
                                                                                                            <ul>
                                                                                                                <!-- **** Entra si algún Organismo de quinto nivel tiene libros ***-->
                                                                                                                <c:forEach var="libroOrganismo" items="${librosOrganismoQuintoNivel}">
                                                                                                                    <c:if test="${libroOrganismo.value == organismo5.id}">
                                                                                                                        <li>
                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                    class="panel-heading btn-llibre vuitanta-percent"
                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                    class="fa fa-book"></i> ${libroOrganismo.key}</span></a>
                                                                                                                        </li>
                                                                                                                    </c:if>
                                                                                                                </c:forEach>

                                                                                                                <!-- **** Oficinas ***-->
                                                                                                                <c:forEach var="oficinaResponsable" items="${oficinasResponsables}">
                                                                                                                    <c:if test="${oficinaResponsable.organismoResponsable.id == organismo5.id}">
                                                                                                                        <li>
                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                    class="panel-heading btn-warning vuitanta-percent"
                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                    class="fa fa-home"></i> ${oficinaResponsable.codigo} - ${oficinaResponsable.denominacion}</span></a>
                                                                                                                            <ul>
                                                                                                                                <c:forEach var="oficinasDependiente" items="${oficinasDependientes}">
                                                                                                                                    <c:if test="${oficinasDependiente.oficinaResponsable.id == oficinaResponsable.id}">
                                                                                                                                        <li>
                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente.codigo} - ${oficinasDependiente.denominacion}</span></a>
                                                                                                                                            <ul>
                                                                                                                                                <c:forEach var="oficinasDependiente2" items="${oficinasDependientes}">
                                                                                                                                                    <c:if test="${oficinasDependiente2.oficinaResponsable.id == oficinasDependiente.id}">
                                                                                                                                                        <li>
                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente2.codigo} - ${oficinasDependiente2.denominacion}</span></a>
                                                                                                                                                        </li>
                                                                                                                                                    </c:if>
                                                                                                                                                </c:forEach>
                                                                                                                                            </ul>
                                                                                                                                        </li>
                                                                                                                                    </c:if>
                                                                                                                                </c:forEach>
                                                                                                                            </ul>
                                                                                                                        </li>
                                                                                                                    </c:if>
                                                                                                                </c:forEach>
                                                                                                                <!-- **** Oficinas Funcionales ***-->
                                                                                                                <c:forEach var="oficinaOrganizativa" items="${oficinasOrganizativas}">
                                                                                                                    <c:if test="${oficinaOrganizativa.id == organismo5.id}">
                                                                                                                        <li>
                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                    class="panel-heading btn-success vuitanta-percent"
                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                    class="fa fa-institution"></i> ${oficinaOrganizativa.nombre}</span></a>
                                                                                                                        </li>
                                                                                                                    </c:if>
                                                                                                                </c:forEach>
                                                                                                                <!-- **** Oficinas Sir ***-->
                                                                                                                <c:forEach var="oficinaSir" items="${oficinasSir}">
                                                                                                                    <c:if test="${oficinaSir.id == organismo5.id}">
                                                                                                                        <li>
                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                    class="panel-heading btn-ofsir vuitanta-percent"
                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                    class="fa fa-exchange"></i> ${oficinaSir.nombre}</span></a>
                                                                                                                        </li>
                                                                                                                    </c:if>
                                                                                                                </c:forEach>

                                                                                                                <c:forEach var="organismo6" items="${organismosSextoNivel}">
                                                                                                                    <c:if test="${organismo6.organismoSuperior.id == organismo5.id}">
                                                                                                                        <li>

                                                                                                                            <c:if test="${organismo6.edp == false}">
                                                                                                                                <span class="panel-heading btn-primary vuitanta-percent"
                                                                                                                                      id="cinqueNivell${contadorCinque}"
                                                                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                        class=""></i> ${organismo6.codigo} - ${organismo6.denominacion}</span>
                                                                                                                            </c:if>
                                                                                                                            <c:if test="${organismo6.edp == true}">
                                                                                                                                <span class="panel-heading btn-edp vuitanta-percent"
                                                                                                                                      id="cinqueNivell${contadorCinque}"
                                                                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                        class=""></i> ${organismo6.codigo} - ${organismo6.denominacion}</span>
                                                                                                                            </c:if>

                                                                                                                            <c:set var="contadorCinque" value="${contadorCinque+1}"></c:set>
                                                                                                                            <ul>
                                                                                                                                <!-- **** Entra si algún Organismo de sexto nivel tiene libros ***-->
                                                                                                                                <c:forEach var="libroOrganismo" items="${librosOrganismoSextoNivel}">
                                                                                                                                    <c:if test="${libroOrganismo.value == organismo6.id}">
                                                                                                                                        <li>
                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                    class="panel-heading btn-llibre vuitanta-percent"
                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                    class="fa fa-book"></i> ${libroOrganismo.key}</span></a>
                                                                                                                                        </li>
                                                                                                                                    </c:if>
                                                                                                                                </c:forEach>

                                                                                                                                <!-- **** Oficinas ***-->
                                                                                                                                <c:forEach var="oficinaResponsable" items="${oficinasResponsables}">
                                                                                                                                    <c:if test="${oficinaResponsable.organismoResponsable.id == organismo6.id}">
                                                                                                                                        <li>
                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                    class="panel-heading btn-warning vuitanta-percent"
                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                    class="fa fa-home"></i> ${oficinaResponsable.codigo} - ${oficinaResponsable.denominacion}</span></a>
                                                                                                                                            <ul>
                                                                                                                                                <c:forEach var="oficinasDependiente" items="${oficinasDependientes}">
                                                                                                                                                    <c:if test="${oficinasDependiente.oficinaResponsable.id == oficinaResponsable.id}">
                                                                                                                                                        <li>
                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente.codigo} - ${oficinasDependiente.denominacion}</span></a>
                                                                                                                                                            <ul>
                                                                                                                                                                <c:forEach var="oficinasDependiente2" items="${oficinasDependientes}">
                                                                                                                                                                    <c:if test="${oficinasDependiente2.oficinaResponsable.id == oficinasDependiente.id}">
                                                                                                                                                                        <li>
                                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente2.codigo} - ${oficinasDependiente2.denominacion}</span></a>
                                                                                                                                                                        </li>
                                                                                                                                                                    </c:if>
                                                                                                                                                                </c:forEach>
                                                                                                                                                            </ul>
                                                                                                                                                        </li>
                                                                                                                                                    </c:if>
                                                                                                                                                </c:forEach>
                                                                                                                                            </ul>
                                                                                                                                        </li>
                                                                                                                                    </c:if>
                                                                                                                                </c:forEach>
                                                                                                                                <!-- **** Oficinas Funcionales ***-->
                                                                                                                                <c:forEach var="oficinaOrganizativa" items="${oficinasOrganizativas}">
                                                                                                                                    <c:if test="${oficinaOrganizativa.id == organismo6.id}">
                                                                                                                                        <li>
                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                    class="panel-heading btn-success vuitanta-percent"
                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                    class="fa fa-institution"></i> ${oficinaOrganizativa.nombre}</span></a>
                                                                                                                                        </li>
                                                                                                                                    </c:if>
                                                                                                                                </c:forEach>
                                                                                                                                <!-- **** Oficinas Sir ***-->
                                                                                                                                <c:forEach var="oficinaSir" items="${oficinasSir}">
                                                                                                                                    <c:if test="${oficinaSir.id == organismo6.id}">
                                                                                                                                        <li>
                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                    class="panel-heading btn-ofsir vuitanta-percent"
                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                    class="fa fa-exchange"></i> ${oficinaSir.nombre}</span></a>
                                                                                                                                        </li>
                                                                                                                                    </c:if>
                                                                                                                                </c:forEach>

                                                                                                                                <c:forEach var="organismo7" items="${organismosSeptimoNivel}">
                                                                                                                                    <c:if test="${organismo7.organismoSuperior.id == organismo6.id}">
                                                                                                                                        <li>

                                                                                                                                            <c:if test="${organismo7.edp == false}">
                                                                                                                                                <span class="panel-heading btn-primary vuitanta-percent"
                                                                                                                                                      id="siseNivell${contadorSise}"
                                                                                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                        class=""></i> ${organismo7.codigo} - ${organismo7.denominacion}</span>
                                                                                                                                            </c:if>
                                                                                                                                            <c:if test="${organismo7.edp == true}">
                                                                                                                                                <span class="panel-heading btn-edp vuitanta-percent"
                                                                                                                                                      id="siseNivell${contadorSise}"
                                                                                                                                                      style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                        class=""></i> ${organismo7.codigo} - ${organismo7.denominacion}</span>
                                                                                                                                            </c:if>

                                                                                                                                            <c:set var="contadorSise" value="${contadorSise+1}"></c:set>
                                                                                                                                            <ul>
                                                                                                                                                <!-- **** Entra si algún Organismo de septimo nivel tiene libros ***-->
                                                                                                                                                <c:forEach var="libroOrganismo" items="${librosOrganismoSeptimoNivel}">
                                                                                                                                                    <c:if test="${libroOrganismo.value == organismo7.id}">
                                                                                                                                                        <li>
                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                    class="panel-heading btn-llibre vuitanta-percent"
                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                    class="fa fa-book"></i> ${libroOrganismo.key}</span></a>
                                                                                                                                                        </li>
                                                                                                                                                    </c:if>
                                                                                                                                                </c:forEach>

                                                                                                                                                <!-- **** Oficinas ***-->
                                                                                                                                                <c:forEach var="oficinaResponsable" items="${oficinasResponsables}">
                                                                                                                                                    <c:if test="${oficinaResponsable.organismoResponsable.id == organismo7.id}">
                                                                                                                                                        <li>
                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                    class="panel-heading btn-warning vuitanta-percent"
                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                    class="fa fa-home"></i> ${oficinaResponsable.codigo} - ${oficinaResponsable.denominacion}</span></a>
                                                                                                                                                            <ul>
                                                                                                                                                                <c:forEach var="oficinasDependiente" items="${oficinasDependientes}">
                                                                                                                                                                    <c:if test="${oficinasDependiente.oficinaResponsable.id == oficinaResponsable.id}">
                                                                                                                                                                        <li>
                                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente.codigo} - ${oficinasDependiente.denominacion}</span></a>
                                                                                                                                                                            <ul>
                                                                                                                                                                                <c:forEach var="oficinasDependiente2" items="${oficinasDependientes}">
                                                                                                                                                                                    <c:if test="${oficinasDependiente2.oficinaResponsable.id == oficinasDependiente.id}">
                                                                                                                                                                                        <li>
                                                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                                                    class="panel-heading btn-ofaux vuitanta-percent"
                                                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                                                    class="fa fa-home"></i> ${oficinasDependiente2.codigo} - ${oficinasDependiente2.denominacion}</span></a>
                                                                                                                                                                                        </li>
                                                                                                                                                                                    </c:if>
                                                                                                                                                                                </c:forEach>
                                                                                                                                                                            </ul>
                                                                                                                                                                        </li>
                                                                                                                                                                    </c:if>
                                                                                                                                                                </c:forEach>
                                                                                                                                                            </ul>
                                                                                                                                                        </li>
                                                                                                                                                    </c:if>
                                                                                                                                                </c:forEach>
                                                                                                                                                <!-- **** Oficinas Funcionales ***-->
                                                                                                                                                <c:forEach var="oficinaOrganizativa" items="${oficinasOrganizativas}">
                                                                                                                                                    <c:if test="${oficinaOrganizativa.id == organismo7.id}">
                                                                                                                                                        <li>
                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                    class="panel-heading btn-success vuitanta-percent"
                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                    class="fa fa-institution"></i> ${oficinaOrganizativa.nombre}</span></a>
                                                                                                                                                        </li>
                                                                                                                                                    </c:if>
                                                                                                                                                </c:forEach>
                                                                                                                                                <!-- **** Oficinas Sir ***-->
                                                                                                                                                <c:forEach var="oficinaSir" items="${oficinasSir}">
                                                                                                                                                    <c:if test="${oficinaSir.id == organismo7.id}">
                                                                                                                                                        <li>
                                                                                                                                                            <a href="javascript:void(0);"><span
                                                                                                                                                                    class="panel-heading btn-ofsir vuitanta-percent"
                                                                                                                                                                    style="cursor:copy" onclick="copyToClipboard(this)" onmouseover="mostraInfo()" onmouseleave="amagaInfo()"><i
                                                                                                                                                                    class="fa fa-exchange"></i> ${oficinaSir.nombre}</span></a>
                                                                                                                                                        </li>
                                                                                                                                                    </c:if>
                                                                                                                                                </c:forEach>
                                                                                                                                            </ul>
                                                                                                                                        </li>
                                                                                                                                    </c:if>
                                                                                                                                </c:forEach>
                                                                                                                            </ul>
                                                                                                                        </li>
                                                                                                                    </c:if>
                                                                                                                </c:forEach>
                                                                                                            </ul>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:forEach>
                                                                                            </ul>
                                                                                        </li>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                            </ul>
                                                                        </li>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </ul>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </ul>
                                        </li>
                                    </c:forEach>

                                </ul>
                            </li>
                        </ul>
                    </div>

                </c:if>

            </div>
        </div> <!-- /.row-->


    </div>
</div> <!-- /container -->



<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    $(function () {
        $('.tree li:has(ul > li)').addClass('parent_li').find(' > span').attr('title', 'Amaga la branca');
        $('.tree li:has(ul > li)').addClass('parent_li').find(' > span').addClass('fa fa-minus');
        $('.tree li.parent_li > span').on('click', function (e) {
            var children = $(this).parent('li.parent_li').find(' > ul > li');
            if (children.is(":visible")) {
                children.hide('fast');
                $(this).removeClass('fa fa-minus');
                $(this).attr('title', 'Mostra la branca').addClass('fa fa-plus');
            } else {
                children.show('fast');
                $(this).removeClass('fa fa-plus');
                $(this).attr('title', 'Amaga la branca').addClass('fa fa-minus');
            }
            e.stopPropagation();
        });
    });
</script>

<script type="text/javascript">
    function amaga(numeroPrimer, numeroSegon, numeroTercer) {

        $('.tree li:has(ul > li)').addClass('parent_li').find(' > span').removeClass('fa fa-minus');

        for(var i=0;i<numeroTercer;i++){
            var variable = $("#tercerNivell" + i).parent('li.parent_li').find(' > ul > li');
            variable.hide('fast');
            if($("#tercerNivell" + i).parent('li.parent_li').find(' > ul > li').val() != undefined){
                $("#tercerNivell" + i).removeClass('fa fa-minus');
                $("#tercerNivell" + i).attr('title', 'Mostra la branca').addClass('fa fa-plus');
            }
        }

        for(var i=0;i<numeroSegon;i++){
            var variable = $("#segonNivell" + i).parent('li.parent_li').find(' > ul > li');
            variable.hide('fast');
            if($("#segonNivell" + i).parent('li.parent_li').find(' > ul > li').val() != undefined){
                $("#segonNivell" + i).removeClass('fa fa-minus');
                $("#segonNivell" + i).attr('title', 'Mostra la branca').addClass('fa fa-plus');
            }
        }

        for(var i=0;i<numeroPrimer;i++){
            var variable = $("#primerNivell" + i).parent('li.parent_li').find(' > ul > li');
            variable.hide('fast');
            if($("#primerNivell" + i).parent('li.parent_li').find(' > ul > li').val() != undefined){
                $("#primerNivell" + i).removeClass('fa fa-minus');
                $("#primerNivell" + i).attr('title', 'Mostra la branca').addClass('fa fa-plus');
            }
        }

        $("#govern").addClass('fa fa-minus');
        $("#govern").attr('title', 'Amaga la branca');
        $("#entidad").addClass('fa fa-minus');
        $("#entidad").attr('title', 'Amaga la branca');

    }
</script>

<script type="text/javascript">

    // Mostra la informació de copy text
    function mostraInfo(){
        $('#infoCopy').removeClass('hide');
    }

    // Amaga la informació de copy text
    function amagaInfo(){
        $('#infoCopy').addClass('hide');
    }

    // Permet copiar la informació d'un span a dins el portapapers
    function copyToClipboard(that){
        var inp =document.createElement('input');
        document.body.appendChild(inp)
        inp.value =that.textContent
        inp.select();
        document.execCommand('copy',false);
        inp.remove();
    }
</script>


</body>
</html>