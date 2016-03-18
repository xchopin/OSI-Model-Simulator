package reseau.couches;
import java.util.HashMap;
import reseau.Message;
import reseau.adresses.Adresse;
import test.TestReseau;

/**
 * @author Xavier Chopin
 */
public abstract class Transport4 extends Couche {
    private HashMap<Integer, Application7> portsAppli ;     // Accès aux applications via le port
    
    public Transport4() {
        portsAppli = new HashMap<>();
    }

    /**
     * Ajouter une nouvelle application avec son no de port
     * @param port
     * @param appli 
     */
    public void ajouter(int port, Application7 appli) {
        portsAppli.put(port, appli) ;
    }
    
    /**
     * @param port
     * @return l'application liée au port donné
     */
    public Application7 getApplication(int port) {
        Application7 a = portsAppli.get(port) ;
        if (a==null) throw new IllegalArgumentException("Port inconnu "+port);
        return a;
    }
   
    /**
     * Envoyer un message à un destinataire précis
     * @param portSource
     * @param dest
     * @param portDest
     * @param message 
     */
    public void sendMessage(int portSource, Adresse dest, int portDest, Message message) {
        TestReseau.afficherTour();
             // Transmettre à la couche Transport
                 Message m = new Message(this.getEntete(portSource,dest,portDest,message));
                 m.ajouter(message);
                 System.out.println("Je suis "+getNom()+" et j'envoie "+m.size()+" octets : " +m);
                 ((Reseau3)moinsUn).sendMessage(dest, m);

    }


    /**
     * Fonction pour recevoir un message de la part d'une autre machine
     * @param source
     * @param message
     */
     
    public void receiveMessage(Adresse source, Message message) {
        TestReseau.afficherTour();
        int portSource = message.extraireEntier(0);
        message.supprimer(2);
        int portDest = message.extraireEntier(0);
        message.supprimer(6);   // suppression du reste de l'entete UDP
        System.out.println("Je suis "+getNom()+" apres suppression de l'entete je recois "+message.size()+" octets : " + message);

        Application7 app = this.getApplication(portDest);
        if (app != null){
            app.receiveMessage(source, portSource, message);
        }else{
            System.err.println("Il n'y a aucune application qui écoute le port " + portDest + " sur cette machine");
        }

}

    
    protected abstract Message getEntete(int portSource, Adresse dest, int portDest, Message message);

    
}
