package org.fundaciobit.plugins.postproceso.postprocesolocal.test;


import es.caib.regweb3.model.RegistroEntrada;
import org.fundaciobit.plugins.postproceso.postprocesolocal.PostProcesoLocalPostProcesoPlugin;

/**
 * @author anadal
 * @author mgonzalez
 */
public class Test {

    @org.junit.Test
    public void test() {

        System.out.println();
    }


    public static void main(String[] args) {

        try {

            PostProcesoLocalPostProcesoPlugin postProcesoPlugin = new PostProcesoLocalPostProcesoPlugin();

             postProcesoPlugin.nuevoRegistroEntrada(new RegistroEntrada());


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


}
