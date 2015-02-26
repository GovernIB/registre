/**
 *
 * Agrupa todas las funcionalidades relacionadas con los anexos de un registro.
 *
 */

$(window).load(function() {

    // Muestra u Oculta el input firma en función de si es autofirma
    $('input[name=autofirma]:radio').click(function () {
        var autofirma = $('input[name=autofirma]:radio:checked').val();

        if(autofirma == 1 || autofirma == 0){
            $('#divInputFirma').hide();
        }else if(autofirma == 2){
            $('#divInputFirma').show();
        }


    });
});

function nuevoAnexo(idRegistro, idRegistroDetalle, tipoRegistro){

    limpiarAnexo();

    // inicializamos la accion el titulo y el registro
    $('#anexoTitulo').html("Nou Annexe");
    $('#accion').val('nuevo');
    parametrosAnexo(idRegistro, idRegistroDetalle, tipoRegistro);
    $('#sinfirma').prop("checked", "checked");
    configuracionBasicaAnexo();
}


/**
 * Gestiona el alta y edición de Anexos de un registro de entrada
 * En primer lugar se guarda el anexo sin el archivo asociado y posteriormente se hace el submit del formulario
 * archivoAnexoForm con el archivo del anexo y se guarda y se asocia al anexo.
 * Se ha tenido que hacer en dos formularios separadados, porque los campos del anexo se envian en JSON y
 * el archivo en texto. Aparte en los controller trabajan con diferentes clases de request
 * que no he podido utilizar conjuntamente.
 */
function procesarAnexo() {

    var accion = $('#accion').val();
    var idRegistro = $('#idRegistro').val();
    var idRegistroDetalle = $('#idRegistroDetalle').val();
    var tipoRegistro = $('#tipoRegistro').val();
    var url = $("#anexoForm").attr("action").concat('/'+accion+"/"+idRegistro+"/"+idRegistroDetalle+"/"+tipoRegistro);
    var borrar = $('#borrar').prop('checked');

   // var borrarfirma = $('#borrarfirma').prop('checked');

    var json = { "id": $('#id').val(), "titulo": $('#titulo').val(), "tipoDocumental" : $('#tipoDocumental').val(), "validezDocumento" : $('#validezDocumento').val(), "tipoDocumento" : $('#tipoDocumento').val(),"observaciones": $('#observacionesAnexo').val(), "origenCiudadanoAdmin" : $('#origenCiudadanoAdmin').val(), "nombreFicheroAnexado": $('#nombreFicheroAnexado').val(),"nombreFirmaAnexada": $('#nombreFirmaAnexada').val(),"borrar":$('#borrar').prop('checked'), "modoFirma":$('input[name=autofirma]:radio:checked').val()};
    $('#reload').show();
    $.ajax({
        url: url,
        data: JSON.stringify(json),
        type: "POST",

        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function(respuesta) {

            if(respuesta.status == 'FAIL'){
                quitarErroresAnexo();

                for(var i =0 ; i < respuesta.errores.length ; i++){

                    // Hacemos esto por el '.' en el nombre Id de los select
                    var variable = respuesta.errores[i].field;
                    variable = variable.replace(".", "\\.");
                    variable = "#"+variable+"Error";

                    var htmlError = "<span id=\""+respuesta.errores[i].field+"Error\" class=\"help-block\">"+respuesta.errores[i].defaultMessage+"</span>";
                    $(variable).html(htmlError);
                    $(variable).parents(".form-group").addClass("has-error");

                }

            }else if(respuesta.status == 'SUCCESS'){
                 //aquí ya se ha creado el anexo en la base de datos, ahora falta crear el archivo asociado.

                 // Montamos la url para guardar el archivo asociado al anexo

                 var urlArchivo = $("#archivoAnexoForm").attr("action").concat('/'+respuesta.result.id+'/'+accion+'/'+borrar);

                 /* Upload file   http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/ */
                 /* TODO Mirar de unificar en un solo formulario */
                 /* Hacemos el submit del formulario que contiene el archivo una vez que
                    ya se ha creado el anexo previamente */
                 $("#archivoAnexoForm").ajaxForm({
                   url: urlArchivo,
                   type:"POST",
                   success:function() {
                 },
                  dataType:"text"
                 }).submit();

                 // Definimos la información necesaria para crear la fila nueva en la tabla de anexos.
                 // Aquí definimos la url para poderlo borrar.
                 /*var urlDelete = getContextPath()+"anexo/"+idRegistro+"/"+respuesta.result.id+"/delete";*/

                 //Gestión de la fila en la tabla de anexos
                 if(accion == 'nuevo'){// Si es nuevo añadimos la fila a la tabla
                    if(respuesta.result.primerAnexo){

                        addPrimerAnexo(respuesta.result.nombre, $('#tipoDocumento option:selected').text(), respuesta.result.id ,idRegistro, idRegistroDetalle, tipoRegistro);
                    }else{
                        addAnexo(respuesta.result.nombre, $('#tipoDocumento option:selected').text(), respuesta.result.id, idRegistro, idRegistroDetalle, tipoRegistro);
                    }
                 }else {// si es modif actualizamos los campos visibles de la fila.
                     actualizarTituloTipoDocumentoAnexo(respuesta.result.id, respuesta.result.nombre,$('#tipoDocumento option:selected').text() );
                 }
                 // ocultamos el modal
                 $('#modalNuevoAnexo').modal('hide');


            }
        }
    });

    event.preventDefault();
}


/**
 * Carga los datos de un Anexo en el formulario correspondiente para su posterios edición
 * @param idAnexo
 */
function cargarAnexo(idAnexo, idRegistro, idRegistroDetalle, tipoRegistro){

    limpiarAnexo();

    // Marcamos la acción para Editar
    $('#accion').val('editar');
    $('#anexoTitulo').html("Editar Annexe");
    //configuraciones basicas
    parametrosAnexo(idRegistro, idRegistroDetalle, tipoRegistro);
    configuracionBasicaAnexo();

    //Obtenemos los datos del anexo a editar
    $.ajax({
        url: urlObtenerAnexo,
        data: { id: idAnexo },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',


        success: function(anexo) {
            // asignar la parte de archivoAnexoForm
            var nombreFicheroAnexado = anexo.nombreFicheroAnexado;
            var nombreFirmaAnexada = anexo.nombreFirmaAnexada;
            // Si hay archivo se muestran sus datos
            if(nombreFicheroAnexado != null && nombreFicheroAnexado.length >0){
                $('#nombreFicheroAnexado').val(anexo.nombreFicheroAnexado);
                $('#linkFichero').text(anexo.nombreFicheroAnexado);
                $('#linkFichero').attr("href" , getContextPath()+ "anexo/"+anexo.id);
                $('#divArchivoActual').show();
                $('input[name=autofirma]:radio').prop("disabled", true);
                $('#validezDocumento').prop("disabled", true);

            }
            //Si hay firma detached se muestran sus datos
            if(nombreFirmaAnexada != null && nombreFirmaAnexada.length > 0){
                $('#nombreFirmaAnexada').val(anexo.nombreFirmaAnexada);
                $('#linkFirma').text(anexo.nombreFirmaAnexada);
                $('#linkFirma').attr("href" , getContextPath()+ "anexo/firma/"+anexo.id);
                $('#borrarfirma').hide();
                $('#labelborrarfirma').hide();
                $('#divFirmaActual').show();
            }


            // Asignar el resto de valores en anexoForm
            $('#id').val(anexo.id);
            $('#titulo').val(anexo.titulo);
            $('#tipoDocumental').val(anexo.tipoDocumental.id);
            $('#tipoDocumento').val(anexo.tipoDocumento);

            if(anexo.validezDocumento != null && anexo.validezDocumento != '-1'){
                $('#validezDocumento').val(anexo.validezDocumento);
            }
            $('#observacionesAnexo').val(anexo.observaciones);
            $('#origenCiudadanoAdmin').val(anexo.origenCiudadanoAdmin);
            //Gestión del radiobutton en función del modo de firma
            if(anexo.modoFirma == 2){ // Si no es autofirma, mostramos el input de firma
                $('#autofirmano').prop("checked", "checked");
                if(anexo.validezDocumento ==1){// Para cuando vienen de ws.
                   $('#divInputFirma').hide();
                }else{
                   $('#divInputFirma').show();
                }
            }else {
                if(anexo.modoFirma == 0){
                  $('#sinfirma').prop("checked", "checked");
                }else{
                  $('#autofirmasi').prop("checked", "checked");
                }
                $('#divInputFirma').hide();
                $('#firma').val("");
            }

            $('#tipoDocumento').trigger("chosen:updated");
            $('#tipoDocumental').trigger("chosen:updated");
            $('#validezDocumento').trigger("chosen:updated");
            $('#origenCiudadanoAdmin').trigger("chosen:updated");

        }
    });

}



/**
 * Limpia el formulario y los posibles mensajes de error
 */
function limpiarAnexo(){
    clearForm("#anexoForm");
    clearForm("#archivoAnexoForm");
    // clear el href de archivoAnexoForm
    $('#linkFichero').text("");
    $('#linkFichero').attr("href" , "");
    $('#linkFirma').text("");
    $('#linkFirma').attr("href" , "");
     quitarErroresAnexo();

}

function quitarErroresAnexo(){
    quitarError('titulo');
    quitarError('validezDocumento');
    quitarError('tipoDocumento');
    quitarError('tipoDocumental');
    quitarError('observacionesAnexo');
    quitarError('origenCiudadanoAdmin');
    $('#reload').hide();
}

/* Función que obtiene el contextpath en js */
function getContextPath() {
    var ctx = window.location.pathname,
        path = '/' !== ctx ? ctx.substring(0, ctx.indexOf('/', 1) + 1) : ctx;
    return path + (/\/$/.test(path) ? '' : '/');
}

/**
 * Añade una nueva fila con el anexo a la tabla de anexos
 * Los parametros indicados son para montar todas las urls y llamadas necesarias,
 * así como mostrar los datos correspondientes
 * @param titulo
 * @param tipoDocumento
 * @param url indica url de borrado
 * @param idAnexo identificador del anexo
 * @param idRegistroDetalle identificador del anexo
 */
function addAnexo(titulo, tipoDocumento, idAnexo, idRegistro, idRegistroDetalle, tipoRegistro){

    var fila = "<tr id=\"anexo"+idAnexo+"\"><td>"+titulo+"</td><td>"+tipoDocumento+"</td>"+
        "<td class=\"center\">"+
        "<a class=\"btn btn-warning btn-default btn-sm\" data-toggle=\"modal\" role=\"button\" href=\"#modalNuevoAnexo\" onclick=\"cargarAnexo('"+idAnexo+"','"+idRegistro+"','"+idRegistroDetalle+"','"+tipoRegistro+"')\" title=\"Editar\"><span class=\"fa fa-pencil\"></span></a> "+
        "<a class=\"btn btn-danger btn-default btn-sm\"  onclick=\"eliminarAnexo('"+idAnexo+"','"+idRegistroDetalle+"')\" href=\"javascript:void(0);\" title=\"Eliminar\"><span class=\"fa fa-eraser\"></span></a></td></tr>";
    $('#anexos').append(fila);
}
/**
 * Añade el primer anexo a la tabla de anexos.
 * de la tabla con el anexo
 * @param titulo Título del anexo
 * @param tipoDocumento  Tipo de Documento
 * @param idAnexo  identificador del anexo
 * @param idRegistro   identificador del registro
 * @param idRegistroDetalle  identificador del registro detalle
 * @param tipoRegistro  tipo de registro (Entrada o Salida)
 */
function addPrimerAnexo(titulo, tipoDocumento, idAnexo, idRegistro, idRegistroDetalle, tipoRegistro){

    // Html de la tabla
    var html='';
    html += '<table id="anexos" class="table table-bordered table-hover table-striped">';
    html += '<colgroup>';
    html += '<col>';
    html += '<col>';
    html += '<col width="100">';
    html += '</colgroup>';
    html += '<thead>';
    html += '<tr>';
    html += '<th>Titol</th>';
    html += '<th>Tipus de Document</th>';
    html += '<th>Accions</th>';
    html += '</tr>';
    html += '</thead>';

    html += '<tbody></tbody></table>';


    $('#anexosdiv').html(html);

    //Fila con los datos del anexo
    var fila = "<tr id=\"anexo"+idAnexo+"\"><td>"+titulo+"</td><td>"+tipoDocumento+"</td>"+
        "<td class=\"center\">"+
        "<a class=\"btn btn-warning btn-default btn-sm\" data-toggle=\"modal\" role=\"button\" href=\"#modalNuevoAnexo\" onclick=\"cargarAnexo('"+idAnexo+"','"+idRegistro+"','"+idRegistroDetalle+"','"+tipoRegistro+"')\" title=\"Editar\"><span class=\"fa fa-pencil\"></span></a> "+
        "<a class=\"btn btn-danger btn-default btn-sm\"  onclick=\"eliminarAnexo('"+idAnexo+"','"+idRegistroDetalle+"')\" href=\"javascript:void(0);\" title=\"Eliminar\"><span class=\"fa fa-eraser\"></span></a></td></tr>";


    $('#anexos').append(fila);
}

/**
 * Actualiza el titulo y el tipo de documento del idAnexo pasado por parámetro, despues de realizar una edición.
 * @param idAnexo
 * @param titulo
 * @param tipoDocumento
 */
function actualizarTituloTipoDocumentoAnexo(idAnexo, titulo, tipoDocumento){
    // Cogemos el tr del anexo en cuestión
    var elemento = "#anexo"+idAnexo;

    $(elemento + ' td:first').text(titulo);
    // Modificamos el segundo td
    $(elemento + ' td:eq(1)').text(tipoDocumento);
}

/**
 * Elimina el anexo seleccionado de la Sesion, y la quita en la tabla de anexos.
 * @param idAnexo
 * @param idRegistroDetalle
 */
function eliminarAnexo(idAnexo,idRegistroDetalle){

    var elemento = "#anexo"+idAnexo;

    $.ajax({
        url: urlEliminarAnexo,
        type: 'GET',
        dataType: 'json',
        data: { idAnexo: idAnexo, idRegistroDetalle:idRegistroDetalle },
        contentType: 'application/json',

        success: function(result) {
            $(elemento).remove();
        }
    });

}
/**
 * Función que inicializa los elementos básicos del formulario de anexo.
 * Se emplea para la creación y la edición
 */
function configuracionBasicaAnexo(){
    $('#divArchivoActual').hide();
    $('#divFirmaActual').hide();
    $('#divInputFirma').hide();
    $('#divautofirma').show();
    $('input[name=autofirma]:radio').prop("disabled", false);
    $('#reload').hide();
    $('#validezDocumento').prop("disabled", false);

    $('#tipoDocumento').trigger("chosen:updated");
    $('#tipoDocumental').trigger("chosen:updated");
    $('#validezDocumento').trigger("chosen:updated");
    $('#origenCiudadanoAdmin').trigger("chosen:updated");
}
/**
 * Actualiza los valores necesarios para trabajar entre llamadas js.
 * @param idRegistro  identificador del registro (Entrada o Salida)
 * @param idRegistroDetalle identificador del registro detalle asociado
 * @param tipoRegistro tipo de Registro (Entrada o Salida)
 */
function parametrosAnexo(idRegistro, idRegistroDetalle, tipoRegistro){
    $('#idRegistro').val(idRegistro);
    $('#idRegistroDetalle').val(idRegistroDetalle);
    $('#tipoRegistro').val(tipoRegistro);
}

/**
 *  Oculta o muestra el bloque de firma en función de la validez del documento escogido.
 * @param idRegistro
 * @param idRegistroDetalle
 * @param tipoRegistro
 */
function bloquearFirma(idRegistro, idRegistroDetalle, tipoRegistro){

     parametrosAnexo(idRegistro, idRegistroDetalle, tipoRegistro);
    // Si la validez del documento es 1-> Copia no se puede adjuntar anexo con firma.
     if($('#validezDocumento').val() == 1){
       $('#divFirmaActual').hide();
       $('#divInputFirma').hide();
       $('#divautofirma').hide();
	   $('#firma').val("");
	   $('#textefirma').val("");
       $('#sinfirma').prop("checked", "checked");
     }else{
       $('#divautofirma').show();
	   $('#autofirmasi').prop("checked", "checked");
	   $('#divFirmaActual').hide();
       $('#divInputFirma').hide();
     }
}






