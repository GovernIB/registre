/**
 * Añade el Organismo , y lo pinta en la tabla de interesados.
 * @param tipo
 * @param representante
 * @param idRegistroDetalle
 */
function addOrganismoInteresado(tipo,idRegistroDetalle){

    var denominacion = $('#organismoInteresado option:selected').text();
    var codigoDir3 = $('#organismoInteresado option:selected').val();


    if(codigoDir3 != '-1'){

        restOrganismoInteresado(codigoDir3, denominacion, idRegistroDetalle, tipo);
    }
}

/**
 * Añade el Organismo de la plantilla, y lo pinta en la tabla de interesados.
 * @param tipo
 * @param idRegistroDetalle
 * @param codigoDir3
 * @param denominacion
 */
function addOrganismoInteresadoPlantilla(tipo,idRegistroDetalle, codigoDir3, denominacion){

    if(codigoDir3 != '-1'){
        restOrganismoInteresado(codigoDir3, denominacion, idRegistroDetalle, tipo);
    }
}

/**
 * Realiza una petición REST para procesar el Organimo seleccionado
 * @param codigoDir3
 * @param denominacion
 * @param idRegistroDetalle
 * @param tipo
 */
function restOrganismoInteresado(codigoDir3, denominacion, idRegistroDetalle, tipo){

    var denominacionCodificada = encodeURI(denominacion);

    $.ajax({
        url: urlAddOrganismoInteresado,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        data: { codigoDir3: codigoDir3, denominacion: denominacionCodificada, idRegistroDetalle: idRegistroDetalle },

        success: function(result) {

            if(result.status === 'SUCCESS'){
                if(tipoRegistro==2){ //Si es una salida eliminamos los anteriores
                    eliminarOrganismos();
                }
                addOrganismoInteresadoHtml(codigoDir3, denominacion, tipo, idRegistroDetalle, true);

            }else if(result.status === 'FAIL'){

                mensajeError("#mensajes", result.error);
            }

        }
    });
}

/**
 * Elimina todos los Interesados de la tabla
 */
function eliminarTodosInteresados(){
    $('#interesados > tbody  > tr').each(function() {
        if($(this).attr('id').startsWith("organismo")){
            $(this).remove();
        }
        if($(this).attr('id').startsWith("persona")){
            $(this).remove();
        }
    });
}

/**
 * Elimina todos los Organismos de la tabla Interesados
 */
function eliminarOrganismos(){
    $('#interesados > tbody  > tr').each(function() {
        if($(this).attr('id').startsWith("organismo")){
            $(this).remove();
        }
    });
}

/**
 * Añade una nueva fila con el Organismo la tabla de interesados
 * @param codigoDir3
 * @param denominacion
 * @param tipo
 * @param idRegistroDetalle
 * @param mensaje true o false para mostrar un mensaje aviso
 */
function addOrganismoInteresadoHtml(codigoDir3, denominacion, tipo, idRegistroDetalle, mensaje) {

    var fila = "<tr id=\"organismo"+codigoDir3+"\"><td>"+denominacion+"</td><td>"+tipo+"</td><td><span class=\"label label-danger\">No</span></td>"+
        "<td class=\"center\">"+
        "<a class=\"btn btn-danger btn-default btn-sm\"  onclick=\"eliminarOrganisnoInteresado('"+codigoDir3+"','"+idRegistroDetalle+"')\" href=\"javascript:void(0);\" title=\"Eliminar\"><span class=\"fa fa-eraser\"></span></a></td></tr>";

    $('#interesados').append(fila);
    // Mostramos mensaje de información
    if(idRegistroDetalle != null) {
        if (mensaje && idRegistroDetalle.length > 0) {
            mensajeSuccess("#mensajes", tradsinteresado['interesado.añadido']);

            // Actualizamos el evento del registro de salida
            if(tipoRegistro == '2'){
             actualizarEventoRegistroSalida(idRegistroSalida);
             }
        }
    }
    mostrarOcultarTabla();
}

/**
 * Elimina la Administracion seleccionada de la Sesion, y la quita en la tabla de interesados.
 * @param codigoDir3
 * @param idRegistroDetalle
 */
function eliminarOrganisnoInteresado(codigoDir3,idRegistroDetalle){

    var elemento = "#organismo"+codigoDir3;

    $.ajax({
        url: urlEliminarOrganismoInteresado,
        type: 'GET',
        dataType: 'json',
        data: { codigoDir3: codigoDir3, idRegistroDetalle:idRegistroDetalle },
        contentType: 'application/json',

        success: function(result) {
            if(result==true){
                $(elemento).remove();
                mostrarOcultarTabla();
                // Mostramos mensaje de información
                if (idRegistroDetalle.length > 0) {
                    mensajeSuccess("#mensajes", tradsinteresado['interesado.eliminado']);

                    // Actualizamos el evento del registro de salida
                    if(tipoRegistro == 2){
                        actualizarEventoRegistroSalida(idRegistroSalida);
                    }
                }
            } else {
                mensajeError("#mensajes", tradsinteresado['interesado.eliminar.ultimo']);
            }
        }
    });

}

/**
 * Elimina todos los organismos y personas de la Sesion, y los quita en la tabla de interesados.
 * @param idRegistroDetalle
 */
function eliminarInteresados(){

    $.ajax({
        url: urlEliminarInteresados,
        type: 'GET',
        dataType: 'json',
        data: { idRegistroDetalle: '' },
        contentType: 'application/json',

        success: function(result) {
            if(result==true){
                eliminarTodosInteresados();
            } else {
                mensajeError("#mensajes", tradsinteresado['interesado.eliminar.ultimo']);
            }
        }
    });

}


/**
 * Añade la Persona seleccionada a la Sesion, y la muestra en la tabla de interesados.
 * @param id
 * @param nombre
 * @param tipo
 * @param representante
 * @param modal
 * @param idRegistroDetalle
 */
function addPersonaInteresado(id, nombre,tipo,representante, modal,idRegistroDetalle){

    if(id != '-1'){
        $.ajax({
            url: urlAddPersonaInteresado,
            type: 'GET',
            dataType: 'json',
            data: { id: id , idRegistroDetalle:idRegistroDetalle},
            contentType: 'application/json',

            success: function(result) {
                if(result != null){

                    if (result == 0) {//Sesion
                        addInteresadoHtml(id,nombre,tipo,representante,idRegistroDetalle);
                    } else { //bbdd
                        addInteresadoHtml(result,nombre,tipo,representante,idRegistroDetalle);
                    }
                }else{
                    mensajeError("#mensajes", tradsinteresado['interesado.añadir.error']);
                }


            }
        });
    }

    if(modal != null){ // Ocultamos el modal
        $(modal).modal('hide');
    }

}

/**
 * Elimina la Persona seleccionada de la Sesion, y la quita en la tabla de interesados.
 * @param idPersona
 * @param idRegistroDetalle
 */
function eliminarPersonaInteresado(idPersona,idRegistroDetalle){

    var elemento = "#persona"+idPersona;

    $.ajax({
        url: urlEliminarPersonaInteresado,
        type: 'GET',
        dataType: 'json',
        data: { id: idPersona, idRegistroDetalle:idRegistroDetalle },
        contentType: 'application/json',

        success: function(result) {
            if(result==true){
                $(elemento).remove();
                mostrarOcultarTabla();
                // Mostramos mensaje de información
                if (idRegistroDetalle.length > 0) {
                    mensajeSuccess("#mensajes", tradsinteresado['interesado.eliminado']);
                }
            } else {
                mensajeError("#mensajes", tradsinteresado['interesado.eliminar.ultimo']);
            }


        }
    });

}

/**
 * Añade una nueva fila con la Persona Física o Jurídica a la tabla de interesados sin Representante
 * @param idPersona
 * @param nombre
 * @param tipo
 * @param representante
 * @param idRegistroDetalle
 * @param mensaje true o false para mostrar un mensaje aviso
 */
function addInteresadoHtml(idPersona, nombre,tipo,representante,idRegistroDetalle){
    var vacio = "";
    var representanteButton = "<div class=\"btn-group\">"+
        "<button type=\"button\" class=\"btn btn-danger btn-xs dropdown-toggle\" data-toggle=\"dropdown\">"+representante+"<span class=\"caret\"></span></button>"+
        "<ul class=\"dropdown-menu\" role=\"menu\">"+
        "<li><a href=\"#modalInteresado\" onclick=\"gestionarRepresentante('"+vacio+"',"+idPersona+",'"+urlObtenerInteresado+"')\">"+tradsinteresado['interesado.representante.nuevo']+"</a></li>"+
        "<li><a data-toggle=\"modal\" href=\"#modalBuscadorPersonasTodas\" onclick=\"busquedaRepresentantes("+idPersona+")\">"+tradsinteresado['interesado.representante.buscar']+"</a></li></ul></div>";

    var fila = "<tr id=\"persona"+idPersona+"\"><td>"+nombre+"</td><td>"+tipo+"</td><td>"+representanteButton+"</td>"+
        "<td class=\"center\">"+
        "<a class=\"btn btn-warning btn-default btn-sm\" data-toggle=\"modal\" role=\"button\" href=\"#modalInteresado\" onclick=\"editarInteresado('"+idPersona+"')\" title=\"Editar\"><span class=\"fa fa-pencil\"></span></a> "+
        "<a class=\"btn btn-danger btn-default btn-sm\" onclick=\"eliminarPersonaInteresado('"+idPersona+"','"+idRegistroDetalle+"')\" href=\"javascript:void(0);\" title=\"Eliminar\"><span class=\"fa fa-eraser\"></span></a></td></tr>";

    $('#interesados').append(fila);

    if (idRegistroDetalle.length > 0) {
        mensajeSuccess("#mensajes", tradsinteresado['interesado.añadido']);
    }

    mostrarOcultarTabla();
}

/**
 * Muestra u oculta la Tabla Interesados en función de si tiene contenido o no
 */
function mostrarOcultarTabla(){
    var total = $("#interesados > tbody > tr").length;

    if(total == 0){
        $('#interesados').hide();
    }else{
        $('#interesados').show();
    }
}

/**
 * Añade un Interesado con Representante
 * @param nombre
 * @param tipo
 * @param idRepresentante
 * @param idInteresado
 * @param idRegistroDetalle
 */
function addInteresadoRepresentanteHtml(idInteresado,nombreInteresado,tipo,idRepresentante,nombreRepresentante,idRegistroDetalle){

    var vacio = "";
    if(idRepresentante != null && idRepresentante.length > 0){ // Si hay representate
        // Botonera de acciones de un representante
        var representanteButton = "<div class=\"btn-group\">"+
            "<button type=\"button\" class=\"btn btn-success btn-xs dropdown-toggle\" data-toggle=\"dropdown\">"+nombreRepresentante+" <span class=\"caret\"></span></button>"+
            "<ul class=\"dropdown-menu\" role=\"menu\">"+
            "<li><a href=\"#modalInteresado\" onclick=\"gestionarRepresentante("+idRepresentante+","+idInteresado+",'"+urlObtenerInteresado+"')\">"+tradsinteresado['interesado.representante.editar']+"</a></li>"+
            "<li><a href=\"javascript:void(0);\" onclick=\"eliminarRepresentante("+idRepresentante+","+idInteresado+",'"+idRegistroDetalle+"')\">"+tradsinteresado['interesado.representante.eliminar']+"</a></li></ul></div>";
    }else{
        var representanteButton = "<div class=\"btn-group\">"+
            "<button type=\"button\" class=\"btn btn-danger btn-xs dropdown-toggle\" data-toggle=\"dropdown\">No<span class=\"caret\"></span></button>"+
            "<ul class=\"dropdown-menu\" role=\"menu\">"+
            "<li><a href=\"#modalInteresado\" onclick=\"gestionarRepresentante('"+vacio+"',"+idInteresado+",'"+urlObtenerInteresado+"')\">"+tradsinteresado['interesado.representante.nuevo']+"</a></li>"+
            "<li><a data-toggle=\"modal\" href=\"#modalBuscadorPersonasTodas\" onclick=\"busquedaRepresentantes("+idInteresado+")\">"+tradsinteresado['interesado.representante.buscar']+"</a></li></ul></div>";
    }


    // Fila con el interesado y su representante
    var fila = "<tr id=\"persona"+idInteresado+"\"><td>"+nombreInteresado+"</td><td>"+tipo+"</td><td>"+representanteButton+"</td>"+
        "<td class=\"center\">"+
        "<a class=\"btn btn-warning btn-default btn-sm\" data-toggle=\"modal\" role=\"button\" href=\"#modalInteresado\" onclick=\"editarInteresado('"+idInteresado+"')\" title=\"Editar\"><span class=\"fa fa-pencil\"></span></a> "+
        "<a class=\"btn btn-danger btn-default btn-sm\" onclick=\"eliminarPersonaInteresado('"+idInteresado+"','"+idRegistroDetalle+"')\" href=\"javascript:void(0);\" title=\"Eliminar\"><span class=\"fa fa-eraser\"></span></a></td></tr>";

    // Añadimos a la celda la nueva información
    $('#interesados').append(fila);

    mostrarOcultarTabla();

}


/**
 * Carga los datos de una Persona en el formulario correspondiente, para su posterior edición.
 * @param id
 * @param url
 */
function editarInteresado(id){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarInteresado();

    // Marcamos la acción para Editar
    $('#accion').val('editar');

    //Obtenemos los datos de la Persona a editar
    $.ajax({
        url: urlObtenerInteresado,
        data: { id: id },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(objeto){
            $('#formularioInteresado').hide();
        },

        success: function(result) {
            //Activamos o desactivamos los campos en función del TipoInteresado
            camposTipoPersona(result.tipo);

            if(result.tipo == 2){
                $('#interesadoTitulo').html(tradsinteresado['interesado.personafisica.editar']);
                $('#tipo').val("2");
            }else if(result.tipo == 3){
                $('#interesadoTitulo').html(tradsinteresado['interesado.personajuridica.editar']);
                $('#tipo').val("3");
            }

            // Rellenamos los campos del formulario
            $('#id').val(result.id);
            $('#tipo').val(result.tipo);
            $('#nombre').val(result.nombre);
            $('#apellido1').val(result.apellido1);
            $('#apellido2').val(result.apellido2);
            if(result.tipoDocumentoIdentificacion != null && result.tipoDocumentoIdentificacion != '-1'){
                $('#documento').removeAttr("disabled","disabled");
                $('#tipoDocumentoIdentificacion').val(result.tipoDocumentoIdentificacion);
            }
            $('#documento').val(result.documento);
            if(result.pais != null){$("#pais\\.id").val(result.pais.id);}
            if(result.provincia != null){
                $("#provincia\\.id").val(result.provincia.id);
                $("#provincia\\.id").removeAttr("disabled","disabled");
                actualizarLocalidad();

                if(result.localidad != null){
                    $("#localidad\\.id").val(result.localidad.id);
                    $("#localidad\\.id").removeAttr("disabled","disabled");
                }
            }

            $('#direccion').val(result.direccion);
            $('#razonSocial').val(result.razonSocial);
            $('#codigoDire').val(result.codigoDire);
            $('#direccionElectronica').val(result.direccionElectronica);
            $('#email').val(result.email);
            $('#cp').val(result.cp);
            $('#telefono').val(result.telefono);
            if(result.canal != null){
                $("#canal").val(result.canal);
                actualizarCanalNotificacionInicio();
            }
            $('#observaciones').val(result.observaciones);

            // Actualizamos los select Chosen
            $('#tipoDocumentoIdentificacion').trigger("chosen:updated");
            $('#canal').trigger("chosen:updated");
            $('#tipo').trigger("chosen:updated");
            $('#pais\\.id').trigger("chosen:updated");
            $('#provincia\\.id').trigger("chosen:updated");
            $('#localidad\\.id').trigger("chosen:updated");

            $('#formularioInteresado').show();

        }
    });
}

/**
 * Prepara el formulario de Interesados para dar de alta uno nuevo.
 */
function nuevoInteresado(titulo){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarInteresado();

    // Título del formulario
    $('#interesadoTitulo').html(titulo);

    // Indicamos que se trata de un nuevo interesado
    $('#accion').val('nuevo');

    // Marcamos que nos es representante
    $('#isRepresentante').val('false');

    // Activamos o deshabilitamos campos según el TipoPersona escogido
    var tipoInteresado = $('input[name=tipoInteresado]:radio:checked').val();
    camposTipoPersona(tipoInteresado);
}


/**
 * Gestiona el alta y edición de Personas interesadas, también de representantes.
 * Se utiliza cuando se realiza un submit del formulario de Interesados..
 */
function procesarInteresado() {

    var accion = $('#accion').val();
    var idRegistroDetalle = $('#idRegistroDetalle').val();
    if(idRegistroDetalle.length == 0){
        idRegistroDetalle = 'null';
    }
    var url = $("#interesadoForm").attr("action").concat('/'+accion).concat('/'+idRegistroDetalle);

    var json = { "id": $('#id').val(), "tipo": $('#tipo').val(), "nombre" : $('#nombre').val(), "apellido1" : $('#apellido1').val(), "apellido2" : $('#apellido2').val(),"tipoDocumentoIdentificacion": $('#tipoDocumentoIdentificacion').val(), "documento" : $('#documento').val(),
        "pais" : $('#pais\\.id').val(),"provincia" : $('#provincia\\.id').val(), "localidad" : $('#localidad\\.id').val(), "direccion" : $('#direccion').val(), "razonSocial": $('#razonSocial').val(),"codigoDire": $('#codigoDire').val(), "email" : $('#email').val(), "cp" : $('#cp').val(), "telefono" : $('#telefono').val(),
        "direccionElectronica":$('#direccionElectronica').val(),"canal":$('#canal').val(), "observaciones":$('#observaciones').val(), "guardarInteresado":$('#guardarInteresado').prop('checked'), "isRepresentante" : $('#isRepresentante').val()};

    if($('#isRepresentante').val() == 'true'){ // Si es un representate, le añadimos a que persona representa

        json['representado'] = $('#representado\\.id').val();
    }

    $.ajax({
        url: url,
        data: JSON.stringify(json),
        type: "POST",

        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function(respuesta) {

            if(respuesta.status == 'FAIL'){ // Si ha habido erroes
                quitarErroresInteresado();

                for(var i =0 ; i < respuesta.errores.length ; i++){

                    // Hacemos esto por el '.' en el nombre Id de los select
                    var variable = respuesta.errores[i].field;
                    variable = variable.replace(".", "\\.");
                    variable = "#"+variable+"Error";

                    // Mostramos los errores de validación encontrados
                    var htmlError = "<span id=\""+respuesta.errores[i].field+"Error\" class=\"help-block\">"+respuesta.errores[i].defaultMessage+"</span>";
                    $(variable).html(htmlError);
                    $(variable).parents(".form-group").addClass("has-error");

                }

            }else if(respuesta.status == 'SUCCESS'){ // Si no hay errores


                if(respuesta.result.isRepresentante){ // Es un representante

                    if(accion == 'nuevo'){ // Si es un representante nuevo

                        addRepresentanteHtml(respuesta.result.id,respuesta.result.representado.id,respuesta.result.nombre,idRegistroDetalle);

                    }else if(accion == 'editar'){ // Si estamos editando un representante existente
                        actualizarNombrePersonaInteresados(respuesta.result.id, respuesta.result.nombre, true);
                    }

                }else{ // No es un representante

                    if(accion == 'nuevo'){ //Si es una persona nueva

                        if($('#tipo').val() == 2){
                            addInteresadoHtml(respuesta.result.id,respuesta.result.nombre,'Persona física','No',idRegistroDetalle);
                        }else if($('#tipo').val() == 3){
                            addInteresadoHtml(respuesta.result.id,respuesta.result.nombre,'Persona juridica','No',idRegistroDetalle);
                        }

                    }else if(accion == 'editar'){ // Si estamos editando una existente
                        actualizarNombrePersonaInteresados(respuesta.result.id, respuesta.result.nombre, true);
                    }

                }

                // Ocultamos el modal de Interesado
                $('#modalInteresado').modal('hide');
            }
        }
    });


    event.preventDefault();

}


/**
 * Limpia el formulario de interesado y los posibles mensajes de error
 */
function limpiarInteresado(){
    clearForm("#interesadoForm");
    quitarErroresInteresado();
    $('#isRepresentante').val('false');
    $('#eliminarRepresentante').hide();
    $('#tipoInteresadoSelect').hide();
    $('#tipoDocumentoIdentificacion').val("-1");
    $('#documento').val('');
    $('#documento').attr("disabled","disabled");

    // Reseteamos Provincia y Localidad
    $('#provincia\\.id').val('-1');
    $('#localidad\\.id').html('');
    $('#provincia\\.id').attr("disabled","disabled");
    $('#localidad\\.id').attr("disabled","disabled");
    $('#provincia\\.id').trigger("chosen:updated");
    $('#localidad\\.id').trigger("chosen:updated");

}

/**
 * Elimina los mensajes de error de los campos del formulario interesado
 */
function quitarErroresInteresado(){
    quitarError('nombre');
    quitarError('apellido1');
    quitarError('razonSocial');
    quitarError('codigoDire');
    quitarError('email');
    quitarError('documento');
    quitarError('direccion');
    quitarError('direccionElectronica');
    quitarError('tipoDocumentoIdentificacion');
    quitarError('canalNotificacion');
    quitarError('cp');
    quitarError('provincia\\.id');
    quitarError('localidad\\.id');
    quitarError('pais\\.id');
}

/**
 * Actualiza el nombre de la Persona pasada por parámetro, despues de realizar una edición.
 * @param idPersona
 * @param nombre
 * @param mensaje true o false para mostrar un mensaje aviso
 */
function actualizarNombrePersonaInteresados(idPersona, nombre, mensaje) {
    var elemento = "#persona"+idPersona;

    $(elemento + ' td:first').text(nombre);

    if (mensaje && idRegistroDetalle.length > 0) {
        mensajeSuccess("#mensajes", tradsinteresado['interesado.actualizado']);
    }
}


/**
 * Realiza la búsqueda de personas y muestra los resultados en una tabla con paginación
 */
function buscarPersonas(tipoPersonas, idRegistroDetalle) {

    var busqueda = true;

    var tabla = $('<table id="resultadosBusquedaPersona"></table>').addClass('paginated table table-bordered table-hover table-striped');
    tabla.append('<colgroup><col><col><col><col width="100"></colgroup>');

    if(tipoPersonas == 'Fisicas'){ // Personas Físicas

        var tipo = '2';
        var json = { "nombre" : $('#nombre'+tipoPersonas).val(), "apellido1" : $('#apellido1'+tipoPersonas).val(), "apellido2" : $('#apellido2'+tipoPersonas).val(), "documento" : $('#documento'+tipoPersonas).val(), "tipo": tipo};

        tabla.append('<thead><tr><th>'+tradsinteresado['regweb3.nombre']+'</th><th>'+tradsinteresado['persona.documento']+'</th><th>'+tradsinteresado['persona.tipoPersona']+'</th><th>'+tradsinteresado['regweb.acciones']+'</th></tr></thead><tbody></tbody>');

    }else if(tipoPersonas == 'Juridicas'){ // Personas Jurídicas

        var tipo = '3';
        var json = { "razonSocial" : $('#razonSocial'+tipoPersonas).val(), "documento" : $('#documento'+tipoPersonas).val(), "tipo": tipo};

        tabla.append('<thead><tr><th>'+tradsinteresado['persona.razonSocial']+'</th><th>'+tradsinteresado['persona.documento']+'</th><th>'+tradsinteresado['persona.tipoPersona']+'</th><th>'+tradsinteresado['regweb.acciones']+'</th></tr></thead><tbody></tbody>');

    }else if(tipoPersonas == 'Todas'){ // Todas las personas Personas

        var tipo = '0';
        var json = { "nombre" : $('#nombre'+tipoPersonas).val(), "apellido1" : $('#apellido1'+tipoPersonas).val(), "apellido2" : $('#apellido2'+tipoPersonas).val(), "documento" : $('#documento'+tipoPersonas).val(),"razonSocial" : $('#razonSocial'+tipoPersonas).val(), "tipo": tipo};

        if(json.nombre.length==0 && json.apellido1.length==0 && json.apellido2.length==0 && json.documento.length==0 && json.razonSocial.length==0){

            busqueda = false;
        }

        tabla.append('<thead><tr><th>'+tradsinteresado['persona.persona']+'</th><th>'+tradsinteresado['persona.documento']+'</th><th>'+tradsinteresado['persona.tipoPersona']+'</th><th>'+tradsinteresado['regweb.acciones']+'</th></tr></thead><tbody></tbody>');
    }


    if(busqueda){

        //Mostram la imatge de reload
        $('#reload' + tipoPersonas).show();
        // realizamos la busqueda en dir3 mediante REST.

        $.ajax({
            url: $("#buscadorPersonasForm"+tipoPersonas).attr( "action"),
            data: JSON.stringify(json),
            type: "POST",

            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function(result) {

                // Limpiamos los resultados:
                $('#resultadosBusquedaPersonas'+tipoPersonas).html('');
                $('#paginacionPersonas').remove();
                var total = result.length;

                // Ocultamos imagen reload
                $('#reload' + tipoPersonas).hide();

                if(total == 0){ // Si no hay resultados

                    if(tipoPersonas == 'Todas'){
                        $('#resultadosBusquedaPersonas'+tipoPersonas).html("<div class=\"alert alert-grey\" style=\"text-align:left;\">"+tradsinteresado['interesado.noresultados.escoge']+" <a href=\"#modalInteresado\" onclick=\"gestionarRepresentante("+representado+",'','"+urlObtenerInteresado+"')\">"+tradsinteresado['interesado.representante.anadir']+"</a></div>");
                    }else{
                        $('#resultadosBusquedaPersonas'+tipoPersonas).html('<div class="alert alert-grey" style="text-align:left;">'+tradsinteresado['interesado.noresultados']+'</div>');
                    }

                }else if(total != 0){

                    if(total > 1000) {  // Miramos si la búsqueda devuelve más de 1000 resultados

                        $('#resultadosBusquedaPersonas'+tipoPersonas).html('<div class="alert alert-grey" style="text-align:left;">'+tradsinteresado['interesado.maxresultados']+'</div>');

                    } else{

                        for (var i = 0; i < total; i++) {

                            var documento = '';
                            if (result[i].documento != null) {
                                documento = result[i].documento;
                            }

                            if (tipoPersonas == 'Fisicas') {
                                var nombrePersonaFisica = normalizarTexto(result[i].nombrePersonaFisica);
                                var linea = "<tr><td style=\"text-align:left;\">" + result[i].nombrePersonaFisica + "</td><td style=\"text-align:left;\">" + documento + "</td><td style=\"text-align:left;\">" + tradsinteresado['persona.fisica'] + "</td><td class=\"center\"><input type=\"button\" class=\"btn btn-warning btn-sm\" value=" + tradsinteresado['regweb3.anadir'] + " onclick=\"addPersonaInteresado('" + result[i].id + "','" + nombrePersonaFisica + "','Persona Física','No','#modalBuscadorPersonasFisicas','" + idRegistroDetalle + "')\"/></td></tr>";
                                tabla.append(linea);

                            } else if (tipoPersonas == 'Juridicas') {
                                var nombrePersonaJuridica = normalizarTexto(result[i].nombrePersonaJuridica);
                                var linea = "<tr><td style=\"text-align:left;\">" + result[i].nombrePersonaJuridica + "</td><td style=\"text-align:left;\">" + documento + "</td><td style=\"text-align:left;\">" + tradsinteresado['persona.juridica'] + "</td><td class=\"center\"><input type=\"button\" class=\"btn btn-warning btn-sm\" value=" + tradsinteresado['regweb3.anadir'] + " onclick=\"addPersonaInteresado('" + result[i].id + "','" + nombrePersonaJuridica + "','Persona Juridica','No','#modalBuscadorPersonasJuridicas','" + idRegistroDetalle + "')\"/></td></tr>";
                                tabla.append(linea);

                            } else if (tipoPersonas == 'Todas') {
                                var representado = $('#representado').val();

                                var nombre = '';
                                var tipoPersona = '';

                                if (result[i].tipo == 2) {
                                    nombre = result[i].nombrePersonaFisicaCorto;
                                    tipoPersona = tradsinteresado['persona.fisica'];
                                } else if (result[i].tipo == 3) {
                                    nombre = result[i].nombrePersonaJuridica;
                                    tipoPersona = tradsinteresado['persona.juridica'];
                                }


                                var linea = "<tr><td style=\"text-align:left;\">" + nombre + "</td><td style=\"text-align:left;\">" + documento + "</td><td style=\"text-align:left;\">" + tipoPersona + "</td><td class=\"center\"><input type=\"button\" class=\"btn btn-warning btn-sm\" value=\"Afegir\" onclick=\"addRepresentante('" + result[i].id + "','" + representado + "','" + idRegistroDetalle + "')\"/></td></tr>";
                                tabla.append(linea);
                            }
                        }

                        // Mensaje con el total de resultados obtenidos
                        $('#resultadosBusquedaPersonas'+tipoPersonas).attr("display:block");
                        $('#resultadosBusquedaPersonas'+tipoPersonas).append('<div class="alert-grey" style="text-align:left;">'+tradsinteresado['interesado.hay']+' <strong>'+total+'</strong> '+tradsinteresado['interesado.resultados']+'</div>');
                        $('#resultadosBusquedaPersonas'+tipoPersonas).append(tabla);


                        // Paginamos el listado

                        if(total > 10 ) {


                            var currentPage = 1;
                            var numPerPage = 10;

                            var totalResults = total;
                            var totalPages = Math.floor(totalResults / numPerPage);
                            if (totalResults % numPerPage != 0) {
                                totalPages = totalPages + 1;
                            }

                            var beginIndex = Math.max(1, currentPage - numPerPage);
                            var endIndex = Math.min(beginIndex + 10, totalPages);
                            var $pager = $('<ul class="pagination pagination-sm"></ul>');

                            var $table = $("#resultadosBusquedaPersona");
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
                                    $table.trigger('repaginate');
                                }).appendTo($pager);

                                if (currentPage != 1 && currentPage >= endIndex) {
                                    $('<li id="previous"><a href="javascript:void(0);"><i class="fa fa-angle-left"></i></a></li>').bind('click', {
                                        newPage: currentPage - 1
                                    }, function (event) {
                                        currentPage = event.data['newPage'];
                                        $table.trigger('repaginate');
                                    }).appendTo($pager);

                                }

                                for (var page = beginIndex; page <= endIndex; page++) {
                                    $('<li id=li' + page + '><a href ="javascript:void(0);">' + page + '</a></li>').bind('click', {
                                        newPage: page
                                    }, function (event) {
                                        currentPage = event.data['newPage'];
                                        $table.trigger('repaginate');
                                    }).appendTo($pager);

                                }

                                if (currentPage != totalPages) {
                                    $('<li id="next"><a href="javascript:void(0);"><i class="fa fa-angle-right"></i></a></li>').bind('click', {
                                        newPage: currentPage + 1
                                    }, function (event) {
                                        currentPage = event.data['newPage'];
                                        $table.trigger('repaginate');
                                    }).appendTo($pager);

                                }

                                $('<li id="last"><a href="javascript:void(0);"><i class="fa fa-angle-double-right"></i></a></li>').bind('click', {
                                    newPage: totalPages
                                }, function (event) {
                                    currentPage = event.data['newPage'];
                                    $table.trigger('repaginate');
                                }).appendTo($pager);

                                $pager.insertAfter($table).find('#li' + currentPage).addClass('active');

                            });
                            $table.trigger('repaginate');


                        }

                    }

                }

            }
        });

    }else{

        $('#resultadosBusquedaPersonas'+tipoPersonas).html('<div class="alert alert-grey" style="text-align:left;">'+tradsinteresado['interesado.camposBusqueda.vacios']+'</div>');

    }



}

/* Función que limpia el formulario de búsqueda de personas y vacia la tabla de resultados de la búsqueda
 *
 * */
function limpiarBusquedaPersona(tipoPersonas){
    clearForm("#buscadorPersonasForm"+tipoPersonas);
    $('#paginacionPersonas').remove();
    $('#resultadosBusquedaPersonas'+tipoPersonas).empty();
    $('#resultadosBusquedaPersonas'+tipoPersonas).html('');
    $('#resultadosBusquedaPersonas'+tipoPersonas).attr("display:none");
    $('#reload' + tipoPersonas).hide();
}


/**
 * Oculta los Select de Persona Física y Jurídica y muestra el de Adminitraciones
 *
 * */
function mostrarOrganismos(){

    // Ocultamos personas
    ocultaPersonaFisica();
    ocultaPersonaJuridica();

    // Mostramos organismoInteresado
    $('#organismoInteresado').removeAttr("disabled","disabled");
    $('#organismoInteresado').hide();
    $('#organismoInteresado_chosen').show();
    $('#organismoInteresadoLabel').show();
    $('#addOrganismo').show();
    $('#buscarOrganismo').show();
    $('#organismoInteresado').trigger("chosen:updated");
}

/**
 * Oculta el Select de Persona Física
 */
function ocultaPersonaFisica(){

    // Ocultamos PersonaFisica
    $('#nuevaPersonaFisica').hide();
    $('#personaFisica').val("");
    $('#personaFisica').hide();
    $('#personaFisicaLabel').hide();
    $('#buscarPersonaFisica').hide();
}

/**
 * Oculta el Select de Persona Jurídica
 */
function ocultaPersonaJuridica(){

    // Ocultamos PersonaFisica
    $('#nuevaPersonaJuridica').hide();
    $('#personaJuridica').val("");
    $('#personaJuridica').hide();
    $('#personaJuridicaLabel').hide();
    $('#buscarPersonaJuridica').hide();
}

/**
 * Oculta el Select de Organismo
 */
function ocultaOrganismo(){

    // Ocultamos organismoInteresado
    $('#organismoInteresado').val("-1");
    $('#organismoInteresado').hide();
    $('#organismoInteresado_chosen').hide();
    $('#organismoInteresadoLabel').hide();
    $('#addOrganismo').hide();
    $('#buscarOrganismo').hide();
    $('#organismoInteresado').attr("disabled","disabled");
    $('#organismoInteresado').trigger("chosen:updated");
}

/**
 * Muestra el Select de Persona Física y oculta el de Persona Física el de Adminitraciones
 *
 * */
function mostrarPersonaFisica(){

    $('#nuevaPersonaJuridica').hide();
    $('#nuevaPersonaFisica').show();

    // Ocultamos organismo y persona Juridica
    ocultaOrganismo();
    ocultaPersonaJuridica();

    // Mostramos Persona Juridica
    $('#personaFisica').show();
    $('#personaFisicaLabel').show();
    $('#buscarPersonaFisica').show();

}

/**
 * Muestra el Select de Persona Física y oculta el de Persona Física el de Adminitraciones
 *
 * */
function mostrarPersonaJuridica(){

    $('#nuevaPersonaFisica').hide();
    $('#nuevaPersonaJuridica').show();

    // Ocultamos organismo y persona fisica
    ocultaOrganismo();
    ocultaPersonaFisica();

    // Mostramos Persona Juridica
    $('#personaJuridica').show();
    $('#personaJuridicaLabel').show();
    $('#buscarPersonaJuridica').show();

}

/*
 * Según el tipo persona seleccionado, habilita o deshabilita una serie de campos
 */
function camposTipoPersona(tipoInteresado){

    if(tipoInteresado == 2){ //Persona fisica

        $('#razonSocial').attr("disabled", "disabled");
        $('#codigoDire').attr("disabled", "disabled");
        $('#nombre').removeAttr("disabled", "disabled");
        $('#apellido1').removeAttr("disabled", "disabled");
        $('#apellido2').removeAttr("disabled", "disabled");
        $('#tipo').val("2");
        $('#nom').html("<span class=\"text-danger\">*</span> " + tradsinteresado['regweb3.nombre']);
        $('#llinatge1').html("<span class=\"text-danger\">*</span> " + tradsinteresado['usuario.apellido1']);
        $('#rao').html(tradsinteresado['persona.razonSocial']);

        tiposDocumentoPersonaFisica();
    }

    if(tipoInteresado == 3){ //Persona juridica

        $('#razonSocial').removeAttr("disabled", "disabled");
        $('#codigoDire').removeAttr("disabled", "disabled");
        $('#nombre').attr("disabled", "disabled");
        $('#apellido1').attr("disabled", "disabled");
        $('#apellido2').attr("disabled", "disabled");
        $('#tipo').val("3");
        $('#rao').html("<span class=\"text-danger\">*</span> " + tradsinteresado['persona.razonSocial']);
        $('#nom').html(tradsinteresado['regweb3.nombre']);
        $('#llinatge1').html(tradsinteresado['usuario.apellido1']);

        tiposDocumentoPersonaJuridica();
    }

}

function actualizarTipoDocumentoIdentificacion(){

    var tipoDocumento = $('#tipoDocumentoIdentificacion option:selected').val();

    if(tipoDocumento != ''){
        $('#documento').removeAttr("disabled","disabled");
        if(tipoDocumento == 2){
            $('#razonSocial').removeAttr("disabled","disabled");
        }else{
            $('#razonSocial').val('');
            $('#razonSocialError').html('');
            $('#razonSocial').parents(".form-group").removeClass("has-error");
            $('#razonSocial').attr("disabled","disabled");
        }
    }else{
        $('#documento').val('');
        $('#documento').attr("disabled","disabled");
    }

}

/**
 * Activa o deshabilita campos en función del País seleccionado
 */
function actualizarPais(){

    if($('#pais\\.id option:selected').text() != 'España'){
        $('#provincia\\.id').val('-1');
        $('#localidad\\.id').val('-1');
        $('#provincia\\.id').attr("disabled","disabled").trigger("chosen:updated");
        $('#localidad\\.id').attr("disabled","disabled").trigger("chosen:updated");

    }else if($('#pais\\.id option:selected').text() == 'España'){
        $('#provincia\\.id').removeAttr("disabled","disabled").trigger("chosen:updated");

    }else if($('#pais\\.id option:selected').text() == '...'){
       resetPais();
    }

}

/**
 * Inicializa el campo país y sus relacionados
 */
function resetPais(){

    $('#pais\\.id').val("-1");
    $('#provincia\\.id').val("-1");
    $('#localidad\\.id').empty();
    $('#pais\\.id').attr("disabled","disabled").trigger("chosen:updated");
    $('#provincia\\.id').attr("disabled","disabled").trigger("chosen:updated");
    $('#localidad\\.id').attr("disabled","disabled").trigger("chosen:updated");

}

/**
 * Activa o deshabilita campos en función del Canal seleccionado
 */
function actualizarCanalNotificacion() {

    if($('#canal option:selected').val() == '-1'){
        $('#direccion').val('');
        $('#direccion').attr("disabled","disabled");
        $('#cp').val('');
        $('#cp').attr("disabled","disabled");
        $('#direccionElectronica').val('');
        $('#direccionElectronica').attr("disabled","disabled");
        resetPais();

    }else if($('#canal option:selected').val() == '1'){
        $('#direccion').removeAttr("disabled","disabled");
        $('#pais\\.id').removeAttr("disabled", "disabled");
        $('select[name="pais\\.id"]').find('option:contains("España")').attr("selected", true).trigger("chosen:updated");
        actualizarPais();
        $('#cp').removeAttr("disabled","disabled");
        $('#direccionElectronica').attr("disabled","disabled");

    }else  if($('#canal option:selected').val() == '2'){
        $('#direccion').val('');
        $('#direccion').attr("disabled","disabled");
        $('#cp').val('');
        $('#cp').attr("disabled","disabled");
        resetPais();

        $('#direccionElectronica').removeAttr("disabled","disabled");

    }else  if($('#canal option:selected').val() == '3'){
        $('#direccion').val('');
        $('#direccion').attr("disabled","disabled");
        $('#cp').val('');
        $('#cp').attr("disabled","disabled");
        $('#direccionElectronica').val('');
        $('#direccionElectronica').attr("disabled","disabled");
        resetPais();
    }

}

/**
 * Activa o deshabilita campos en función del Canal seleccionado al recargar la página
 */
function actualizarCanalNotificacionInicio() {

    if($('#canal option:selected').val() == '-1'){
        $('#direccion').val('');
        $('#direccion').attr("disabled","disabled");
        $('#cp').val('');
        $('#cp').attr("disabled","disabled");
        $('#direccionElectronica').val('');
        $('#direccionElectronica').attr("disabled","disabled");

    }else if($('#canal option:selected').val() == '1'){
        $('#direccion').removeAttr("disabled","disabled");
        $('#pais\\.id').removeAttr("disabled", "disabled");
        $('#cp').removeAttr("disabled","disabled");
        $('#direccionElectronica').attr("disabled","disabled");

    }else  if($('#canal option:selected').val() == '2'){
        $('#direccion').val('');
        $('#direccion').attr("disabled","disabled");
        $('#cp').val('');
        $('#cp').attr("disabled","disabled");
        $('#direccionElectronica').removeAttr("disabled","disabled");

    }else  if($('#canal option:selected').val() == '3'){
        $('#direccion').val('');
        $('#direccion').attr("disabled","disabled");
        $('#cp').val('');
        $('#cp').attr("disabled","disabled");
        $('#direccionElectronica').val('');
        $('#direccionElectronica').attr("disabled","disabled");
    }

}


/**
 * Habilita/Deshabilita los tipos correspondientes
 */
function tiposDocumentoPersonaFisica(){

    $('#tipoDocumentoIdentificacion option[value="1"]').removeAttr("disabled");
    $('#tipoDocumentoIdentificacion option[value="2"]').attr("disabled", "disabled");
    $('#tipoDocumentoIdentificacion option[value="3"]').removeAttr("disabled");
    $('#tipoDocumentoIdentificacion option[value="4"]').removeAttr("disabled");
    $('#tipoDocumentoIdentificacion option[value="5"]').removeAttr("disabled");
    $('#tipoDocumentoIdentificacion option[value="6"]').removeAttr("disabled");
    if( $('#tipoDocumentoIdentificacion').val() > 0){
        $('#documento').removeAttr("disabled","disabled");
    }
    $('#tipoDocumentoIdentificacion').trigger("chosen:updated");

}

/**
 * Habilita/Deshabilita los tipos correspondientes
 */
function tiposDocumentoPersonaJuridica(){

    $('#tipoDocumentoIdentificacion option[value="1"]').attr("disabled", "disabled");
    $('#tipoDocumentoIdentificacion option[value="2"]').removeAttr("disabled");
    $('#tipoDocumentoIdentificacion option[value="3"]').attr("disabled", "disabled");
    $('#tipoDocumentoIdentificacion option[value="4"]').attr("disabled", "disabled");
    $('#tipoDocumentoIdentificacion option[value="5"]').attr("disabled", "disabled");
    $('#tipoDocumentoIdentificacion option[value="6"]').attr("disabled", "disabled");
    if( $('#tipoDocumentoIdentificacion').val() > 0){
        $('#documento').removeAttr("disabled","disabled");
    }
    $('#tipoDocumentoIdentificacion').trigger("chosen:updated");

}

/**
 * Carga los datos de un Interesado para mostrarlos en un modal
 * @param idInteresado
 */
function obtenerInteresado(idInteresado){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarInteresadoDetalle();

    //Obtenemos los datos de la Persona a editar
    $.ajax({
        url: urlCargarInteresado,
        data: { idInteresado: idInteresado},
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            $('#interesadoTitulo').html(result.nombreCompleto);

            $('#nombre').html(result.nombre);
            $('#apellido1').html(result.apellido1);
            $('#apellido2').html(result.apellido2);
            if(result.tipoDocumentoIdentificacion != null && result.tipoDocumentoIdentificacion != '-1'){
                $('#tipoDocIdentificacion').html(tradsinteresado['tipoDocumentoIdentificacion.'+result.tipoDocumentoIdentificacion]);
                $('#documento').html(result.documento);
            }

            if(result.pais != null){$("#pais").html(result.pais.descripcionPais);}
            if(result.provincia != null){
                $("#provincia").html(result.provincia.descripcionProvincia);

                if(result.localidad != null){
                    $("#localidad").html(result.localidad.nombre);
                }
            }

            $('#direccion').html(result.direccion);
            $('#razonSocial').html(result.razonSocial);
            $('#codigoDire').html(result.codigoDire);
            $('#direccionElectronica').html(result.direccionElectronica);
            $('#email').html(result.email);
            $('#cp').html(result.cp);
            $('#telefono').html(result.telefono);
            if(result.canal != null){
                $("#canal").html(tradsinteresado['canalNotificacion.'+result.canal]);
            }
            $('#observaciones').html(result.observaciones);

        }
    });
}

/**
 * Limpia el formulario de interesado y los posibles mensajes de error
 */
function limpiarInteresadoDetalle(){

    $('#interesadoTitulo').html('');
    $('#nombre').html('');
    $('#apellido1').html('');
    $('#apellido2').html('');
    $('#tipoDocIdentificacion').html('');
    $('#documento').html('');
    $('#email').html('');
    $('#telefono').html('');
    $('#canal').html('');
    $('#pais').html('');
    $('#provincia').html('');
    $('#localidad').html('');
    $('#direccion').html('');
    $('#cp').html('');
    $('#razonSocial').html('');
    $('#codigoDire').html('');
    $('#direccionElectronica').html('');
    $('#observaciones').html('');
}

/**
 *
 * @param idRegistroSalida
 */
function actualizarEventoRegistroSalida(idRegistroSalida){

    //Obtenemos los datos de la Persona a editar
    $.ajax({
        url: urlActualizarEventoRegistroSalida,
        async: false,
        data: { idRegistroSalida: idRegistroSalida},
        type: "GET",
        success: function(result) {
            location.reload();
        }
    });
}