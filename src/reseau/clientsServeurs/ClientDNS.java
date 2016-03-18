package reseau.clientsServeurs;

import reseau.Machine;
import reseau.Message;
import reseau.adresses.Adresse;
import reseau.couches.Application7;

/**
 * @author Xavier Chopin
 */
public class ClientDNS extends Application7 {


    public ClientDNS(int port, Machine machine){super(port,machine);}

    /**
     * Je reçois un message de la couche inférieure
     * @param source adresse de la source
     * @param portSource port de la source
     * @param message
     */
    @Override
    public void receiveMessage(Adresse source, int portSource, Message message) {
        System.out.println("Je suis "+getNom()+" et je reçois "+message);
        resultat = message ;
    }

    /**
     * Fonction qui permet d'interroger L'UNIQUE serveur DNS du réseau Local, son adresse IP et son port sont connus en clair, dans cette fonction
     * @param: nomMachine le nom de la machine dont on cherche l'adresse IP
     */
    public void interrogerServeurDNS(Message nomMachine){
        int port = 1024;
        int portDest = 52;
        Adresse dest = new Adresse("192.23.89.41");
        sendMessage(dest, portDest, nomMachine);
    }



}
