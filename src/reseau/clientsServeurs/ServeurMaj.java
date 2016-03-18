package reseau.clientsServeurs;

/**
 * @author Xavier Chopin
 */

import reseau.Machine;
import reseau.couches.Application7;
import reseau.Message;


public class ServeurMaj extends Application7 {

    /**
     * @param port le port dans la couche transport
     * @param machine: le serveur qui héberge la machine
     * @param clientDNS
     */
    public ServeurMaj(int port, Machine machine, ClientDNS clientDNS) {
        super(port,machine,clientDNS);
    }

    /**
     * @param message entier à convertir en * 2
     * @return
     */
    @Override
    protected Message traiter(Message message) {
        System.out.println("SERVEUR: Majusculisation du message");
        String chaine = message.extraireChaine();
        System.out.println("chaine = " + chaine);
        chaine = chaine.toUpperCase();
        return new Message(chaine);
    }

}
