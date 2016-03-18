package reseau.couches;


import reseau.Machine;
import reseau.Message;
import reseau.adresses.Adresse;
import reseau.clientsServeurs.ClientDNS;
import test.TestReseau;

/**
 * @author: Xavier Chopin
 */
public abstract class Application7 extends Couche {
    
    protected int port ;
    protected Message resultat ;                   // destiné à contenir le résutat de la requête au serveur
    protected Machine machine;
    protected ClientDNS dns;
    
    public Application7(int port, Machine machine,ClientDNS clientDNS) {
        this.port = port ;
        this.machine = machine;
        this.dns = clientDNS;
    }

    public Application7(int port, Machine machine){
        this.port = port ;
        this.machine = machine;
    }


    /**
     * @param dest adresse du destinataire
     * @param portDest port du service demandé
     * @param message
     */
    public void sendMessage(Adresse dest, int portDest, Message message) {
        TestReseau.afficherTour();
        // On efface le résultat de la précédente requête
        resultat = null ;
        // Afficher une trace de l'envoi
         System.out.println("Je suis "+getNom()+" et j'envoie "+message.size()+" octets : " +message);
        // Transmettre à la couche Transport
        ((Transport4)moinsUn).sendMessage(port, dest, portDest, message);
    }

    /**
     * @param nomMachine nom de la machine destinataire
     * @param portDest port du service demandé
     * @param message
     */
    public void sendMessage(String nomMachine, int portDest, Message message) {
        TestReseau.afficherTour();
        // On efface le résultat de la précédente requête
        resultat = null ;
        // Afficher une trace de l'envoi
        //System.out.println("Je suis "+getNom()+" et j'envoie "+message.size()+" octets : " + message);
        this.dns.interrogerServeurDNS(new Message(nomMachine));
        Message infoDNS = this.dns.resultat;

        if (infoDNS != null){
           // Transmettre à la couche Transport
            Adresse dest =  infoDNS.extraireAdresse(4);
            ((Transport4)moinsUn).sendMessage(port, dest, portDest, message);
        }else{
            System.err.println("Machine: "+ nomMachine + " inconnue auprès du serveur DNS");
        }

    }


    /**
     * Je reçois un message de la couche inférieure 
     * @param source adresse de la source
     * @param portSource port de la source
     * @param message
     */
    public void receiveMessage(Adresse source, int portSource, Message message) {
            TestReseau.afficherTour();
            message = traiter(message);
            this.resultat = message;
            System.out.println("\n - - - - - - -  FIN DU TRAITEMENT- - - - - - - -");
            System.out.println("Je suis "+getNom()+" et j'envoie "+message.size()+" octets : " +message);
            sendMessage(source, portSource, message);
    }



    protected Message traiter(Message message) {
        return this.traiter(message) ;
    }

    /**
     *
     * @return Le résultat de la dernière requête
     */
    public Message getResultat() {
        return resultat;
    }

    public int getPort() {
        return this.port ;
    }

    public Machine getMachine() {
        return this.machine ;
    }
 
}