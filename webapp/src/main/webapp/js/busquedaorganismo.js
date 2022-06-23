/*
 Funciones especficas para la búsqueda de organismo y oficinas en un registro de entrada.
 En función del tipoOrganismo indicado, se realizará búsqueda de organismos u oficinas.
 */


/*
 Función que pinta una tabla con los resultados obtenidos de la búsqueda.
 @param tipoOrganismo indica desde donde se realiza la búsqueda para poder asignar
 los resultados al select que corresponde.
 */


/**
 *
 * @param tipoOrganismo
 * @returns {string}
 */
function getUrlBusqueda(tipoOrganismo, urlServidor) {

    if (tipoOrganismo == 'OficinaOrigen') {
        return urlServidor + "/rest/busqueda/oficinas";
    } else if (tipoOrganismo == 'OficinaSir'){
        return urlServidor + "/rest/busqueda/oficinas";
    }
    else {
        return urlServidor + "/rest/busqueda/organismos";
    }
}

/**
 *
 * @param tipoOrganismo
 * @returns {*}
 */
function getIdSelect(tipoOrganismo) {

    /* REGISTRO ENTRADA y REGISTRO SALIDA */
    // Caso de organismo Destino
    if (tipoOrganismo == 'OrganismoDestino') {
        return "#destino\\.codigo";
    }
    // Caso de oficina origen
    if (tipoOrganismo == 'OficinaOrigen') {
        return "#registroDetalle\\.oficinaOrigen\\.codigo";
    }
    // Caso administracion interesado
    if (tipoOrganismo == 'OrganismoInteresado') {
        return "#registroDetalle\\.organismoInteresado\\.codigo";
    }
    // Caso Organismo Origen de registro salida
    if (tipoOrganismo == 'OrganismoOrigen') {
        return "#origen\\.codigo";
    }

    /*LISTADOS REGISTRO ENTRADA Y SALIDA*/
    if (tipoOrganismo == 'listaRegEntrada') {
        return "#organDestinatari";
    }

    if (tipoOrganismo == 'listaRegSalida') {
        return "#organOrigen";
    }
}

/**
 *
 * @param tipoOrganismo
 * @returns {*}
 */
function getIdDenominacion(tipoOrganismo) {

    /* REGISTRO ENTRADA Y REGISTRO SALIDA */
    // Caso de organismo Destino
    if (tipoOrganismo == 'OrganismoDestino') {
        return "#destino\\.denominacion";
    }
    // Caso de oficina origen
    if (tipoOrganismo == 'OficinaOrigen') {
        return "#registroDetalle\\.oficinaOrigen\\.denominacion";
    }
    // Caso administracion interesado
    if (tipoOrganismo == 'OrganismoInteresado') {
        return "#registroDetalle\\.organismoInteresado\\.denominacion";
    }
    // Caso Organismo Origen de registro salida
    if (tipoOrganismo == 'OrganismoOrigen') {
        return "#origen\\.denominacion";
    }

    /* LISTADOS REGISTRO ENTRADA Y SALIDA */
    if (tipoOrganismo == 'listaRegEntrada') {
        return "#organDestinatariNom";
    }

    if (tipoOrganismo == 'listaRegSalida') {
        return "#organOrigenNom";
    }
}

function getIdDenominacionExterna(tipoOrganismo) {
    /* REGISTRO ENTRADA Y REGISTRO SALIDA */
    // Caso de organismo Destino
    if (tipoOrganismo == 'OrganismoDestino') {
        return "#destinoExternoDenominacion";
    }
    // Caso de oficina origen
    if (tipoOrganismo == 'OficinaOrigen') {
        return "#registroDetalle\\.oficinaOrigenExternoDenominacion";
    }
}

/**
 *
 * @param tipoOrganismo
 * @param urlServidor
 * @param idRegistroDetalle
 */
function organismoBusqueda(tipoOrganismo, urlServidor, idRegistroDetalle) {


    // obtenemos los valores del formulario para realizar la búsqueda.
    var denominacion = $('#denominacion' + tipoOrganismo).val();
    var codigoOrganismo = $('#codigoOrganismo' + tipoOrganismo).val();
    var codNivelAdministracion = $('#codNivelAdministracion' + tipoOrganismo).val();
    var codComunidadAutonoma = $('#codComunidadAutonoma' + tipoOrganismo).val();
    var provincia = $('#provincia' + tipoOrganismo).val();

    //La localidad es una dupla (codigoLocalidad - codigoEntidadGeografica
    var localidad = $('#localidad' + tipoOrganismo).val();


    // indica si queremos obtener aquellos organismos que son unidad Raiz
    var unidadRaiz = false;
    // indica que queremos obtener de la búsqueda de organismos aquellos que tienen oficinas
    //de registro
    var conOficinas = false;
    var oficinasSir = false;


    var vigentes = true;
    if(tipoOrganismo == 'listaRegEntrada' || tipoOrganismo == 'listaRegSalida'){
        vigentes = false
    }

    // Variables configurables en función del tipo de organismo indicado.
    var url = getUrlBusqueda(tipoOrganismo, urlServidor);
    // Caso administracion interesado
    if (tipoOrganismo == 'OrganismoInteresado') {
        unidadRaiz = $('#unidadRaiz' + tipoOrganismo).prop('checked');
    }

    // Inicializamos el div del resultado de busqueda.
    $('#resultadosbusqueda' + tipoOrganismo).empty();
    $('#resultadosbusqueda' + tipoOrganismo).html('');
    $('#resultadosbusqueda' + tipoOrganismo).attr("display:none");
    $('#arbol' + tipoOrganismo).html("");
    if (tipoOrganismo != 'OficinaOrigen') {
        $('#tab' + tipoOrganismo + ' a:last').hide();
    }
    if(tipoOrganismo == 'OficinaSir'){
        oficinasSir = true;
    }


    // Definimos la tabla que contendrá los resultados.
    var idTablaResultados = "tresultadosbusqueda" + tipoOrganismo;
    var table = $('<table id="' + idTablaResultados + '"></table>').addClass('paginated table table-bordered table-hover table-striped tablesorter ');


    //Gestión de mostrar la localidad y si tiene oficinas sir en función de niveles de administración y si son organismos o oficinas
    var mostrarThLocalidad="";
    var mostrarColLocalidad="";
    //Mostramos la localidad en Administraciones Locales y de Justicia si son organismos y siempre que sean oficinas.
    if(codNivelAdministracion == 3 || (codNivelAdministracion == 6 && (tipoOrganismo == 'OficinaOrigen' || tipoOrganismo == 'OficinaSir'))){
        mostrarThLocalidad = '<th>' + tradorganismo['organismo.localidad'] + '</th>';
        mostrarColLocalidad = '<col width="125">';
    }

    //Mostramos el estado del organismo o oficina
    var mostrarThEstado="";
    var mostrarColEstado="";
    if(tipoOrganismo == 'listaRegEntrada' ){
        mostrarThEstado = '<th>' + tradorganismo['organismo.estado'] + '</th>';
        mostrarColEstado ='<col width="100">';
    }

    var mostrarThSir="";
    var mostrarColSir="";
    //Mostramos si tiene Oficinas sir cuando sean organismos
    if(tipoOrganismo != 'OficinaOrigen' && tipoOrganismo != 'OficinaSir' ){
        mostrarThSir='<th>' + tradorganismo['organismo.oficinaSir'] + '</th>';
        mostrarColSir='<col width="50">';
    }

    table.append('<colgroup><col width="310"><col width="310">'+  mostrarColLocalidad +  mostrarColEstado + mostrarColSir +'<col width="100"></colgroup>');
    table.append('<thead><tr><th>' + tradorganismo['organismo.denominacion'] + '</th><th>' + tradorganismo['organismo.raiz'] + '</th>' + mostrarThLocalidad + mostrarThEstado + mostrarThSir +'<th>' + tradorganismo['regweb.acciones'] + '</th></tr></thead><tbody></tbody>');


    //Mostram la imatge de reload
    $('#reloadorg' + tipoOrganismo).show();
    // realizamos la busqueda en dir3 mediante REST.
    $.ajax({
        async: false,
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        data: {
            codigo: codigoOrganismo,
            denominacion: denominacion,
            codNivelAdministracion: codNivelAdministracion,
            codComunidadAutonoma: codComunidadAutonoma,
            conOficinas: conOficinas,
            unidadRaiz: unidadRaiz,
            provincia: provincia,
            localidad: localidad,
            oficinasSir: oficinasSir,
            vigentes: vigentes
        },
        success: function (result,estado) {

            $('#resultadosbusqueda' + tipoOrganismo).css('display', 'block');
            if (estado == 'nocontent') {
                $('#reloadorg' + tipoOrganismo).hide();
                $('#resultadosbusqueda' + tipoOrganismo).html('<br/><div class="alert alert-grey" style="text-align:left;">' + tradorganismo['regweb.noresultados'] + '</div>');
            } else {
                var len = result.length;
                for (var i = 0; i < len; i++) {
                    var denominacion = '';
                    var codigo = '';


                    // Sustituimos comillas simples y dobles para evitar problemas en la llamada
                    // a la función posterior
                    denominacion = normalizarTexto(result[i].denominacion);
                    codigo = result[i].codigo;


                    var title = result[i].raiz;
                    // el elemento superior puede ser null, en ese caso le ponemos un texto por defecto
                    var superior = $('#organismo_superior').val() + ": " + result[i].superior;
                    if (result[i].superior == null) {
                        superior = tradorganismo['organismo.superior.vacio'];
                    }


                    // definimos el contenido de la tabla en función de los resultados de la busqueda.
                    var organigramaTab = '#organigrama' + tipoOrganismo;

                    //Mostramos el estado
                    var mostrarEstado="";
                    var estado=result[i].descripcionEstado;

                    //definimos la variable para desabilitar el botón del arbol en caso de que no sea el estado vigente
                    var arbolDisabled="";
                    if(estado!='V'){
                        arbolDisabled = "disabled=disabled";
                    }

                    // si es busqueda de registros de entrada, se muestra el estado
                    if(tipoOrganismo == 'listaRegEntrada'){
                        if(estado == 'V') {
                            mostrarEstado = '<td style=\"text-align:center;\"><span class="label label-success">' + tradorganismo['organismo.estado.vigente']  + '</span></td>';
                        }else if(estado == 'A'){
                            mostrarEstado = '<td style=\"text-align:center;\"><span class="label label-danger">' +  tradorganismo['organismo.estado.anulado'] + '</span></td>';
                        }else if(estado == 'E'){
                            mostrarEstado = '<td style=\"text-align:center;\"><span class="label label-danger">' +  tradorganismo['organismo.estado.extinguido'] + '</span></td>';
                        }else if(estado == 'T'){
                            mostrarEstado = '<td style=\"text-align:center;\"><span class="label label-danger">' +  tradorganismo['organismo.estado.transitorio'] + '</span></td>';
                        }
                    }

                    //Definimos si tiene oficinas sir o no
                    var tieneOficinaSir = '<span class="label label-danger">' + tradorganismo['organismo.oficinaSir.no'] + '</span>';
                    if (result[i].tieneOficinaSir) {
                        tieneOficinaSir = '<span class="label label-success">' + tradorganismo['organismo.oficinaSir.si'] + '</span>'
                    }

                    //Definimos si se muestra la información de que tieneOficinaSir
                    var mostrarOficinaSir = "";
                    if (tipoOrganismo != 'OficinaOrigen' && tipoOrganismo != 'OficinaSir') {
                        mostrarOficinaSir = '<td style=\"text-align:center;\">' + tieneOficinaSir + '</td>';
                    }

                    //Mostramos la localidad en Administraciones Locales y de Justicia si son organismos y siempre que sean oficinas.
                    var mostrarLocalidad = "";
                    if (codNivelAdministracion == 3 || (codNivelAdministracion == 6 && (tipoOrganismo == 'OficinaOrigen' || tipoOrganismo == 'OficinaSir'))) {
                        mostrarLocalidad = '<td style=\"text-align:left;\"> ' + result[i].localidad + '</td>';
                    }

                    if (tipoOrganismo == 'OrganismoInteresado') {
                        var linea = "<tr><td style=\"text-align:left;\"><label rel=\"popover\" class=\"no-bold text-gris\" style=\"cursor: pointer;\" title=\"" + superior + "\">" + result[i].denominacion + " - " + result[i].codigo + "</label></td><td style=\"text-align:left;\"> " + title + "</td>" + mostrarLocalidad + mostrarOficinaSir + "<td class=\"center\"><a class=\"btn btn-warning btn-sm\" title=\"Seleccionar\" onclick=\"addOrganismoInteresadoModal('" + codigo + "','" + denominacion + "','Administración','" + tipoOrganismo + "','" + idRegistroDetalle + "')\"><span class=\"fa fa-hand-o-right\"></span></a><a class=\"btn btn-success btn-sm\" title=\"Arbol\" onclick=\"mostrarArbol('" + codigo + "','" + urlServidor + "','" + tipoOrganismo + "','" + idRegistroDetalle + "')\"><span class=\"fa fa-sitemap\"></span></a></td></tr>";
                    } else if (tipoOrganismo == 'OrganismoDestino' || tipoOrganismo == 'listaRegEntrada' || tipoOrganismo == 'listaRegSalida') {
                        if(arbolDisabled){
                            var linea = "<tr><td style=\"text-align:left;\"><label rel=\"popover\" class=\"no-bold text-gris\" style=\"cursor: pointer;\" title=\"" + superior + "\">" + result[i].denominacion + " - " + result[i].codigo + "</label></td><td style=\"text-align:left;\"> " + title + "</td>" + mostrarLocalidad +  mostrarEstado + mostrarOficinaSir + "<td class=\"center\"><a class=\"btn btn-warning btn-sm\" title=\"Seleccionar\" onclick=\"asignarOrganismo('" + codigo + "','" + denominacion + "','" + tipoOrganismo + "')\"><span class=\"fa fa-hand-o-right\"></span></a><a class=\"btn btn-success btn-sm\" title=\"Arbol\" disabled='disabled' ><span class=\"fa fa-sitemap\"></span></a></td></tr>";
                        }else{
                            var linea = "<tr><td style=\"text-align:left;\"><label rel=\"popover\" class=\"no-bold text-gris\" style=\"cursor: pointer;\" title=\"" + superior + "\">" + result[i].denominacion + " - " + result[i].codigo + "</label></td><td style=\"text-align:left;\"> " + title + "</td>" + mostrarLocalidad +  mostrarEstado + mostrarOficinaSir + "<td class=\"center\"><a class=\"btn btn-warning btn-sm\" title=\"Seleccionar\" onclick=\"asignarOrganismo('" + codigo + "','" + denominacion + "','" + tipoOrganismo + "')\"><span class=\"fa fa-hand-o-right\"></span></a><a class=\"btn btn-success btn-sm\" title=\"Arbol\" onclick=\"mostrarArbol('" + codigo + "','" + urlServidor + "','" + tipoOrganismo + "','" + idRegistroDetalle + "')\"  ><span class=\"fa fa-sitemap\"></span></a></td></tr>";
                        }
                    } else if (tipoOrganismo == 'OficinaOrigen') {
                        var linea = "<tr><td style=\"text-align:left;\"><label rel=\"popover\" class=\"no-bold text-gris\" style=\"cursor: pointer;\" title=\"" + superior + "\">" + result[i].codigo + " - " + result[i].denominacion + "</label></td><td style=\"text-align:left;\"> " + title + "</td>" + mostrarLocalidad + mostrarOficinaSir + "<td class=\"center\"><a class=\"btn btn-warning btn-sm\" title=\"Seleccionar\" onclick=\"asignarOrganismo('" + codigo + "','" + denominacion + "','" + tipoOrganismo + "')\"><span class=\"fa fa-hand-o-right\"></span></a></td></tr>";
                    } else if (tipoOrganismo == 'OficinaSir') {
                        // Obtenemos el organismo responsable de la oficina viene como string combinado en el elemento raiz de la siguiente forma " Denominacion - Codigo".
                        var codigoOrganismoResponsable = result[i].codigoSuperior;
                        var denominacionOrganismoResponsable = normalizarTexto(result[i].superior);
                        var linea = "<tr><td style=\"text-align:left;\"><label rel=\"popover\" class=\"no-bold text-gris\" style=\"cursor: pointer;\" title=\"" + superior + "\">" + result[i].codigo + " - " + result[i].denominacion + "</label></td><td style=\"text-align:left;\"> " + title + "</td>" + mostrarLocalidad + mostrarOficinaSir + "<td class=\"center\"><a class=\"btn btn-warning btn-sm\" title=\"Seleccionar\" onclick=\"asignarOficinaSir('" + codigo + "','" + denominacion + "','" + codigoOrganismoResponsable + "','" + denominacionOrganismoResponsable + "','" + tipoOrganismo + "')\"><span class=\"fa fa-hand-o-right\"></span></a></td></tr>";
                    }

                    table.append(linea);
                }
                // Ocultamos imagen reload
                $('#reloadorg' + tipoOrganismo).hide();
                // Se muestra solo si hay resultados
                if (len != 0) {
                    $('#resultadosbusqueda' + tipoOrganismo).attr("display:block");
                    var paginacion = '';
                    paginacion += "<div class='alert-grey'>";

                    if (len == 1) {
                        paginacion += tradorganismo['regweb.resultado'] + ' <strong>' + len + '</strong>&nbsp;' + tradorganismo['interesado.resultado'];
                    }
                    if (len > 1) {
                        paginacion += tradorganismo['regweb.resultados'] + ' <strong>' + len + '</strong>&nbsp;' + tradorganismo['interesado.resultados'];
                    }
                    paginacion += '<div/>';

                    $('#resultadosbusqueda' + tipoOrganismo).append(paginacion);
                    $('#resultadosbusqueda' + tipoOrganismo).append(table);

                } else if (len == 0) {
                    alert("Entramos aqui en len 0");
                    //$('#resultadosbusqueda'+tipoOrganismo).attr("display:none");
                    $('#resultadosbusqueda' + tipoOrganismo).html('<br/><div class="alert alert-grey" style="text-align:left;">' + tradorganismo['regweb.noresultados'] + '</div>');
                }

                // mostramos el listado paginado.
                $('#tresultadosbusqueda' + tipoOrganismo, 'td').each(function (i) {
                    $(this).text(i + 1);
                });

                $('#resultadosbusqueda' + tipoOrganismo).append('<ul class="pagination pagination-lg pager" ></ul>');


                $('#tresultadosbusqueda' + tipoOrganismo).each(function () {

                    var currentPage = 1;
                    var numPerPage = 10;
                    // calculo de las paginas a mostrar

                    var totalResults = len;
                    var totalPages = Math.floor(totalResults / numPerPage);
                    if (totalResults % numPerPage != 0) {
                        totalPages = totalPages + 1;
                    }


                    var beginIndex = Math.max(1, currentPage - numPerPage);
                    var endIndex = Math.min(beginIndex + 10, totalPages);
                    var $pager = $('<ul class="pagination pagination-sm"></ul>');


                    var $table = $(this);
                    $table.bind('repaginate', function () {

                        $table.find('tbody tr').hide().slice((currentPage - 1) * numPerPage, currentPage * numPerPage).show();
                        beginIndex = Math.max(1, currentPage - numPerPage);
                        endIndex = Math.min(beginIndex + 10, totalPages);

                        $pager.empty();
                        $pager = $('<ul class="pagination pagination-sm"></ul>');

                        $('<li id="first"><a href="javascript:void(0);"><i class="fa fa-angle-double-left"></i></a></li>').bind('click', {
                            newPage: 1
                        }, function (event) {
                            currentPage = event.data['newPage'];
                            // $(this).addClass('active').siblings().removeClass('active');
                            $table.trigger('repaginate');
                        }).appendTo($pager);

                        if (currentPage != 1 && currentPage >= endIndex) {
                            $('<li id="previous"><a href="javascript:void(0);"><i class="fa fa-angle-left"></i></a></li>').bind('click', {
                                newPage: currentPage - 1
                            }, function (event) {
                                currentPage = event.data['newPage'];
                                // $(this).addClass('active').siblings().removeClass('active');
                                $table.trigger('repaginate');
                            }).appendTo($pager);

                        }

                        for (var page = beginIndex; page <= endIndex; page++) {
                            $('<li id=li' + page + '><a href ="javascript:void(0);">' + page + '</a></li>').bind('click', {
                                newPage: page
                            }, function (event) {
                                currentPage = event.data['newPage'];
                                // $(this).addClass('active').siblings().removeClass('active');
                                $table.trigger('repaginate');
                            }).appendTo($pager);

                        }


                        if (currentPage != totalPages) {
                            $('<li id="next"><a href="javascript:void(0);"><i class="fa fa-angle-right"></i></a></li>').bind('click', {
                                newPage: currentPage + 1
                            }, function (event) {
                                currentPage = event.data['newPage'];
                                // $(this).addClass('active').siblings().removeClass('active');
                                $table.trigger('repaginate');
                            }).appendTo($pager);

                        }

                        $('<li id="last"><a href="javascript:void(0);"><i class="fa fa-angle-double-right"></i></a></li>').bind('click', {
                            newPage: totalPages
                        }, function (event) {
                            currentPage = event.data['newPage'];
                            // $(this).addClass('active').siblings().removeClass('active');
                            $table.trigger('repaginate');
                        }).appendTo($pager);

                        $pager.insertAfter($table).find('#li' + currentPage).addClass('active');

                    });
                    $table.trigger('repaginate');

                });


            }
        },
        error : function(request) {

            $('#reloadorg' + tipoOrganismo).hide();
            var mensajeError = tradorganismo['organismo.buscador.error'] + request.status ;
            $('#resultadosbusqueda' + tipoOrganismo).html('<br/><div class="alert alert-danger" style="text-align:left;">' + mensajeError + '</div>');

        }

    });

}


/* Función que limpia el formulario de búsqueda y vacia la tabla de resultados de la búsqueda
 * @param tipoOrganismo, coletilla para los id de los formularios, ya que se importa el mismo jsp varias veces
 * */
function limpiarFormularioBusqueda(tipoOrganismo) {
    clearForm("#organismoREBusquedaForm" + tipoOrganismo);
    $('#resultadosbusqueda' + tipoOrganismo).empty();
    $('#resultadosbusqueda' + tipoOrganismo).html('');
    $('#resultadosbusqueda' + tipoOrganismo).attr("display:none");
    $('#reloadorg' + tipoOrganismo).hide();
}

/**
 *
 * @param selectNivelAdministracion ID Select
 * @param selectComunidadAutonoma ID Select
 * @param selectProvincia ID Select
 * @param selectLocalidad ID Select
 * @param idNivelAdministracion Identificador
 * @param idComunidadAutonoma
 * @param idProvincia
 * @param idLocalidad
 * @param tipoOrganismo
 */
function inicializarBuscador(selectNivelAdministracion, selectComunidadAutonoma, selectProvincia, selectLocalidad, idNivelAdministracion, idComunidadAutonoma, tipoOrganismo) {

    limpiarFormularioBusqueda(tipoOrganismo);
    $('#reloadorg' + tipoOrganismo).hide();
    $(selectNivelAdministracion).val(idNivelAdministracion);
    $(selectNivelAdministracion).trigger("chosen:updated");
    $(selectComunidadAutonoma).val(idComunidadAutonoma);
    $(selectComunidadAutonoma).trigger("chosen:updated");
    $(selectProvincia).attr("disabled", "disabled");
    $(selectLocalidad).attr("disabled", "disabled");
    actualizarProvinciaDestinatarios(tipoOrganismo);
    $('#arbol' + tipoOrganismo).html("");
    $('#tab' + tipoOrganismo + ' a:first').tab('show');
    if (tipoOrganismo != 'OficinaOrigen') {
        $('#tab' + tipoOrganismo + ' a:last').hide();
    }
    $('#resultadosbusqueda' + tipoOrganismo).empty();
}

function asignarOficinaSir(codigo,denominacion,codigoOrganismoResponsable, denominacionOrganismoResponsable, tipoOrganismo){
    var idModal = "#modalBuscador" + tipoOrganismo;
    if(tipoOrganismo == 'OficinaSir'){

        $('#codigoOficina').val(codigo);
        $('#denominacionOficina').val(denominacion);
        $('#codigoOrganismoResponsable').val(codigoOrganismoResponsable);
        $('#denominacionOrganismoResponsable').val(denominacionOrganismoResponsable);
        $('#datosOficinaReenvio').val(denominacion+" ("+codigo+")\n"+denominacionOrganismoResponsable+" ("+ codigoOrganismoResponsable+")");

        $(idModal).modal('hide');
    }
}


function validarFormReenvio() {
    var observaciones = $('#observaciones').val();
    var datosOficinaReenvio = $('#datosOficinaReenvio').val();

    if(validaCampo(datosOficinaReenvio,'idDatosOficinaReenvio')&& validaCampo(observaciones,'idObservaciones')) {
        doForm('#reenviarForm');
    }else{
        return false;
    }
}

/* Función que asigna el valor seleccionado de la búsqueda al select correspondiente
 * y cierra el modal de la búsqueda */
function asignarOrganismo(codigo, denominacion, tipoOrganismo) {

    var idSelect = getIdSelect(tipoOrganismo);
    var idDenominacion = getIdDenominacion(tipoOrganismo);
    var idDenominacionExterna = getIdDenominacionExterna(tipoOrganismo);
    var anadir = true;
    var idModal = "#modalBuscador" + tipoOrganismo;
    var integradoInternOrSir = true;
    /* Comprobamos que esté integrado con SIR */
    $.ajax({
    	async: false,
        crossDomain: true,
        url: urlComprobacionSir,
        data: { codigoOrganismo: codigo },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',
        success: function(result) {
            if(result !== true){
                alert(tradorganismo['organismo.nosir']);
                integradoInternOrSir = false;
            }
        }
    });
    
    if (integradoInternOrSir) {
	    /* Miramos si el organismo ya existe en el select, si existe lo seleccionamos,
	     si no lo indicamos con la variable anadir para añadirlo posteriormente */
	    $(idSelect + " option").each(function () {
	
	        if ($(this).val() == codigo) {
	            anadir = false;
	            $(this).prop("selected", true);
	        }
	
	    });
	    // Si no existe lo añadimos al select
	    if (anadir) {
	        var html = '';
	        var selected = 'selected="selected"';
	        html += '<option ' + selected + ' value="' + codigo + '">'
	            + denominacion + '</option>';
	
	        $(idSelect).append(html);
	        $(idDenominacion).val(denominacion);
	        if (idDenominacionExterna != null) {
	            $(idDenominacionExterna).val(denominacion);
	        }
	    }
	    $(idSelect).trigger("chosen:updated");
	    $(idModal).modal('hide');
	    limpiarFormularioBusqueda(tipoOrganismo);
    }
}

/**
 * Añade el Organismo , y lo pinta en la tabla de interesados.
 * @param codigoDir3
 * @param denominacion
 * @param tipo
 * @param representante
 * @param tipoOrganismo
 */
function addOrganismoInteresadoModal(codigoDir3, denominacion, tipo, tipoOrganismo, idRegistroDetalle) {

    var idModal = "#modalBuscador" + tipoOrganismo;
    var integradoInternOrSir = true;
    /* 
    if (tipoRegistro==2) {
	    //Comprobamos que esté integrado con SIR 
	    $.ajax({
	    	async: false,
	        crossDomain: true,
	        url: urlComprobacionSir,
	        data: { codigoOrganismo: codigoDir3 },
	        type: "GET",
	        dataType: 'json',
	        contentType: 'application/json',
	        success: function(result) {
	            if(result !== true){
	                alert(tradorganismo['organismo.nosir']);
	                integradoInternOrSir = false;
	            }
	        }
	    });
    }
	*/
    if (integradoInternOrSir) {
    	restOrganismoInteresado(codigoDir3, denominacion, idRegistroDetalle, tipo);

    	$(idModal).modal('hide');
    }

}

/**
 *
 * @param organismo
 * @param urlServidor
 * @param tipoOrganismo
 * @param idRegistroDetalle
 */
function mostrarArbol(organismo, urlServidor, tipoOrganismo, idRegistroDetalle) {

    $('#arbol' + tipoOrganismo).html("");
    var url = urlServidor + "/rest/organigrama";
    var html = '';
    $.ajax({
        async: false,
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        data: {codigo: organismo},
        success: function (result,estado) {
            if (estado == 'nocontent') {
                $('#organigrama' + tipoOrganismo).html('<br/><div class="alert alert-grey" style="text-align:left;">' + tradorganismo['regweb.noresultados'] + '</div>');
            } else {
                var denominacion = normalizarTexto(result.denominacion);

                var path = window.location.pathname;
                var retall = path.substring(1);
                var context = "/" + retall.substring(0,retall.indexOf("/"));

                html += '<ul>';
                html += '<li>';
                if (tipoOrganismo == 'OrganismoInteresado') {
                    html += "<span class=\"panel-heading btn-primary vuitanta-percent\" id=\"entidad\" onclick=\"addOrganismoInteresadoModal('" + result.codigo + "','" + denominacion + "','Administración','" + tipoOrganismo + "','" + idRegistroDetalle + "')\">" + result.denominacion + " - " + result.codigo + "</span>";
                    if(result.tieneOficinaSir){
                        html += "<span class=\"panel-heading btn-ofsir vuitanta-percent\" style=\"\"><img src=\""+context+"/img/logo-SIR.png\" width=\"20\" alt=\"SIR\" title=\"SIR\"/></span>";
                    }
                } else {
                    html += "<span class=\"panel-heading btn-primary vuitanta-percent\" id=\"entidad\" onclick=\"asignarOrganismo('" + result.codigo + "','" + denominacion + "','" + tipoOrganismo + "')\">" + result.denominacion + " - " + result.codigo + "</span>";
                    if(result.tieneOficinaSir){
                        html += "<span class=\"panel-heading btn-ofsir vuitanta-percent\" style=\"\"><img src=\""+context+"/img/logo-SIR.png\" width=\"20\" alt=\"SIR\" title=\"SIR\"/></span>";
                    }
                }

                //imprimir los hijos
                var hijoslen = result.hijos.length;

                if (hijoslen > 0) {
                    html += pintarHijos(result.hijos, tipoOrganismo, idRegistroDetalle);
                }
                html += '</li>';
                html += '</ul>';
                html += '</li>';
                html += '</ul>';
                html += '</li>';
                html += '</ul>';
                $('#arbol' + tipoOrganismo).html(html);
            }
        },
        error : function(request){
            var mensajeError = tradorganismo['organismo.buscador.error'] + request.status ;
            $('#organigrama' + tipoOrganismo).html('<br/><div class="alert alert-danger" style="text-align:left;">'+mensajeError+'</div>');
        }

    });
    $('#tab' + tipoOrganismo + ' a:last').show();
    $('#tab' + tipoOrganismo + ' a:last').tab('show');

}


function pintarHijos(hijos, tipoOrganismo, idRegistroDetalle) {

    var htmlp = '';

    var path = window.location.pathname;
    var retall = path.substring(1);
    var context = "/" + retall.substring(0,retall.indexOf("/"));

    htmlp += '<ul>';
    for (var i = 0; i < hijos.length; i++) {
        var denominacion = normalizarTexto(hijos[i].denominacion);
        if (tipoOrganismo == 'OrganismoInteresado') {
            htmlp += "<li> <span class=\"panel-heading btn-primary vuitanta-percent\" onclick=\"addOrganismoInteresadoModal('" + hijos[i].codigo + "','" + denominacion + "','Administración','" + tipoOrganismo + "','" + idRegistroDetalle + "')\">" + hijos[i].denominacion + " - " + hijos[i].codigo + "</span>";
            if(hijos[i].tieneOficinaSir){
                htmlp += "<span class=\"panel-heading btn-ofsir vuitanta-percent\" style=\"\"><img src=\""+context+"/img/logo-SIR.png\" width=\"20\" alt=\"SIR\" title=\"SIR\"/></span>";
            }
        } else {
            htmlp += "<li> <span class=\"panel-heading btn-primary vuitanta-percent\" onclick=\"asignarOrganismo('" + hijos[i].codigo + "','" + denominacion + "','" + tipoOrganismo + "')\">" + hijos[i].denominacion + " - " + hijos[i].codigo + "</span>";
            if(hijos[i].tieneOficinaSir){
                htmlp += "<span class=\"panel-heading btn-ofsir vuitanta-percent\" style=\"\"><img src=\""+context+"/img/logo-SIR.png\" width=\"20\" alt=\"SIR\" title=\"SIR\"/></span>";
            }
        }
        if (hijos[i].hijos.length > 0) {
            htmlp += pintarHijos(hijos[i].hijos, tipoOrganismo, idRegistroDetalle);
        }
    }
    htmlp += '</ul>';

    return htmlp;
}
