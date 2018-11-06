package es.caib.regweb3.plugins.distribucion.goib.test;


import es.caib.regweb3.plugins.distribucion.Destinatarios;
import es.caib.regweb3.plugins.distribucion.goib.DistribucionGoibPlugin;

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

            DistribucionGoibPlugin distribucionPlugin = new DistribucionGoibPlugin();

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
