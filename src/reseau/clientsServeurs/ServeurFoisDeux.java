package reseau.clientsServeurs;

/**
 * @author Xavier Chopin
 */

import reseau.Machine;
import reseau.couches.Application7;
import reseau.Message;


public class ServeurFoisDeux extends Application7 {
    
    /**
     * @param port le port dans la couche transport
     * @param machine: le serveur qui héberge la machine
     * @param clientDNS
     */
    public ServeurFoisDeux(int port, Machine machine, ClientDNS clientDNS) {
        super(port,machine);
    }

    /**
     * @param message entier à convertir en * 2
     * @return 
     */
    @Override
    protected Message traiter(Message message) {
        System.out.println("SERVEUR: Calcul du double de l'int");
        int entier = message.extraireEntier(0) ;
        int res = entier * 2 ;
        return new Message(res);
    }

}
