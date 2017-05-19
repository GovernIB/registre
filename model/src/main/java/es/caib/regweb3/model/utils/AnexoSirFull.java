package es.caib.regweb3.model.utils;

import es.caib.regweb3.model.AnexoSir;

/**
 * Created by earrivi on 19/05/2017.
 */
public class AnexoSirFull {

    public AnexoSir documento;
    public AnexoSir firma;

    public AnexoSirFull() {

    }

    public AnexoSirFull(AnexoSir documento, AnexoSir firma) {
        this.documento = documento;
        this.firma = firma;
    }

    public AnexoSir getDocumento() {
        return documento;
    }

    public void setDocumento(AnexoSir documento) {
        this.documento = documento;
    }

    public AnexoSir getFirma() {
        return firma;
    }

    public void setFirma(AnexoSir firma) {
        this.firma = firma;
    }
}
