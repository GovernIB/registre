package es.caib.regweb3.persistence.validator;

import es.caib.regweb3.model.CatPais;
import es.caib.regweb3.model.Persona;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import es.caib.regweb3.utils.DocumentoUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.utils.Validacion;
import org.fundaciobit.genapp.common.validation.IValidatorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Fundació BIT. Gestiona las Validaciones del formulario para crear
 * o editar una {@link es.caib.regweb3.model.Persona}
 *
 * @author earrivi
 * @author anadal adaptar a persistence i sense Spring
 *         Date: 11/02/14
 */
public class PersonaValidator<T> extends AbstractRegWebValidator<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    public void validate(IValidatorResult<T> errors, T __target__, boolean __isNou__,
                         PersonaLocal personaEjb, CatPaisLocal catPaisEjb) {

        Persona persona = (Persona) __target__;

        // TODO Verificar que existeix
        // persona.getTipoDocumentoIdentificacion().getId()

        // Validaciones si es Persona Física

        if (persona.getTipo() == null) {
            rejectIfEmptyOrWhitespace(errors, __target__, "tipo",
                    "error.valor.requerido");
        } else {
            if (persona.getTipo() != null) {
                if (persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_FISICA)) {


                    rejectIfEmptyOrWhitespace(errors, __target__, "apellido1",
                            "error.valor.requerido");

                    if (!hasFieldErrors(errors, "apellido1")) {
                        if (persona.getApellido1().length() > 30) {
                            rejectValue(errors, "apellido1", "error.valor.maxlenght");
                        }
                    }


                    rejectIfEmptyOrWhitespace(errors, __target__, "nombre", "error.valor.requerido");

                    if (!hasFieldErrors(errors, "nombre")) {
                        if (persona.getNombre().length() > 30) {
                            rejectValue(errors, "nombre", "error.valor.maxlenght");
                        }
                    }

                    if (!isNullOrEmpty(persona.getApellido2())) {
                        if (persona.getApellido2().length() > 30) {
                            rejectValue(errors, "apellido2", "error.valor.maxlenght");
                        }
                    }


                }

                // Validaciones si es Persona Jurídica
                if (persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_JURIDICA)) {
                    rejectIfEmptyOrWhitespace(errors, __target__, "razonSocial",
                            "error.valor.requerido");
                }
            }
        }

        // Gestionamos las validaciones según el Canal escogido
        if (persona.getCanal() == null) {
            //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "canal", "error.valor.requerido","El camp és obligatori");
        } else {

            if (persona.getCanal().equals(RegwebConstantes.CANAL_DIRECCION_POSTAL)) {

                rejectIfEmptyOrWhitespace(errors, __target__, "direccion", "error.valor.requerido");

                // Validaciones si el país seleccionado es ESPAÑA
                if (persona.getPais() == null || persona.getPais().getId() == null || persona.getPais().getId() == -1) {
                    rejectValue(errors, "pais.id", "error.valor.requerido");
                } else {

                    try {

                        log.info("Buscando país: " + persona.getPais().getId() + " - " + persona.getPais().getCodigoPais() + " - " + persona.getPais().getDescripcionPais());
                        CatPais pais = catPaisEjb.findById(persona.getPais().getId());

                        // Validaciones si el país seleccionado es ESPAÑA
                        if (pais.getCodigoPais().equals(RegwebConstantes.PAIS_ESPANYA)) {

                            if (persona.getProvincia() == null || persona.getProvincia().getId() == null || persona.getProvincia().getId() == -1) {
                                rejectValue(errors, "provincia.id", "error.valor.requerido");
                            }
                            if (persona.getLocalidad() == null || persona.getLocalidad().getId() == null || persona.getLocalidad().getId() == -1) {
                                rejectValue(errors, "localidad.id", "error.valor.requerido");
                            }
                            if (StringUtils.isEmpty(persona.getCp())) {
                                rejectIfEmptyOrWhitespace(errors, __target__, "cp", "error.valor.requerido");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (persona.getCanal().equals(RegwebConstantes.CANAL_DIRECCION_ELECTRONICA)) {

                rejectIfEmptyOrWhitespace(errors, __target__, "direccionElectronica", "error.valor.requerido");

            }

        }

        // CÓDIGO POSTAL
        if (persona.getCp() != null && persona.getCp().length() > 0) {
            if (persona.getCp().length() == 5) {
                for (int i = 0; i < persona.getCp().length(); i++) {
                    if (!Character.isDigit(persona.getCp().charAt(i))) {
                        rejectValue(errors, "cp", "error.cp.formato");
                        break;
                    }
                }
            } else {
                rejectValue(errors, "cp", "error.cp.largo");
            }
        }


        // DOCUMENTO (DNI, NIE, PASAPORTE)
        Long tipoDocumento = persona.getTipoDocumentoIdentificacion();

        if(tipoDocumento != null){
            rejectIfEmptyOrWhitespace(errors, __target__, "documento", "error.valor.requerido");
        }

        if(tipoDocumento != null) {

            String documento = persona.getDocumento().toUpperCase();

            Validacion validacionDocumento;
            try {
                validacionDocumento = DocumentoUtils.comprobarDocumento(documento, tipoDocumento);
            } catch (Exception e) {
                e.printStackTrace();
                validacionDocumento = new Validacion(Boolean.FALSE, "error.documento", "El document es erroni");
            }

            //Si el formato es correcto busca que no exista ya en el sistema
            if (validacionDocumento.getValido()) {
                boolean existe;
                try {
                    if (persona.getId() == null) {
                        existe = personaEjb.existeDocumentoNew(persona.getDocumento().toUpperCase(), persona.getEntidad().getId());
                    } else {
                        existe = personaEjb.existeDocumentoEdit(persona.getDocumento().toUpperCase(), persona.getId(), persona.getEntidad().getId());
                    }

                } catch (Exception e) {
                    log.error("Error comprobando si persona ya existe: ", e);
                    existe = true;
                }

                if (existe) {
                    rejectValue(errors, "documento", "error.document.existe");
                }
            } else {
                rejectValue(errors, "documento", validacionDocumento.getCodigoError());
                log.info("El formato del documento NO es correcto");
            }
        }else if(StringUtils.isNotEmpty(persona.getDocumento())){
            rejectValue(errors, "tipoDocumentoIdentificacion", "error.valor.requerido");
        }


            //DIRECCIÓN
        if (!isNullOrEmpty(persona.getDireccion())) {
            if (persona.getDireccion().length() > 160) {
                rejectValue(errors, "direccion", "error.valor.maxlenght");
            }
        }

        //EMAIL
        if (!isNullOrEmpty(persona.getEmail()) && persona.getEmail().length() > 160) {
            rejectValue(errors, "email", "error.valor.maxlenght");
        }

        //TELÉFONO
        if (!isNullOrEmpty(persona.getTelefono()) && persona.getTelefono().length() > 20) {
            rejectValue(errors, "telefono", "error.valor.maxlenght");
        }

        //DireccionElectronica
        if (!isNullOrEmpty(persona.getDireccionElectronica()) && persona.getDireccionElectronica().length() > 160) {
            rejectValue(errors, "direccionElectronica", "error.valor.maxlenght");
        }

        //RAZÓN SOCIAL
        if (!isNullOrEmpty(persona.getRazonSocial()) && persona.getRazonSocial().length() > 80) {
            rejectValue(errors, "razonSocial", "error.valor.maxlenght");
        }

        //OBSERVACIONES
        if (!isNullOrEmpty(persona.getObservaciones()) && persona.getObservaciones().length() > 80) {
            rejectValue(errors, "observaciones", "error.valor.maxlenght");
        }


    }


    @Override
    public String prefixFieldName() {
        return "persona.";
    }


    @Override
    public String adjustFieldName(String fieldName) {
        if (fieldName == null) {
            return null;
        } else {
            if (fieldName.endsWith(".id")) {
                return fieldName.substring(0, fieldName.lastIndexOf('.'));
            }
        }
        return fieldName;
    }

}
