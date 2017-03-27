package es.caib.regweb3.plugins.distribucion.ripea.test;


import es.caib.regweb3.plugins.distribucion.Destinatarios;
import es.caib.regweb3.plugins.distribucion.ripea.DistribucionRipeaPlugin;

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

            DistribucionRipeaPlugin distribucionPlugin = new DistribucionRipeaPlugin();

            Destinatarios destinatarios = distribucionPlugin.distribuir(null);
            if (destinatarios != null) {
                System.out.println(" ------- Destinatarios ------- ");
                System.out.println("DESTINATARIOS POSIBLES: " + destinatarios.getPosibles().size());
                System.out.println();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


}
