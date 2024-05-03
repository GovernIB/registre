package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.Oficina;

import java.util.Comparator;

public class DenominacionComparador implements Comparator<Oficina> {


    @Override
    public int compare(Oficina oficina1, Oficina oficina2) {
        return oficina1.getDenominacion().compareTo(oficina2.getDenominacion());
    }
}
