package es.caib.regweb3.plugins.postproceso.mock.test;


import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.plugins.postproceso.mock.PostProcesoMockPlugin;


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

            PostProcesoMockPlugin postProcesoPlugin = new PostProcesoMockPlugin();

             postProcesoPlugin.nuevoRegistroEntrada(new RegistroEntrada());


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


}
