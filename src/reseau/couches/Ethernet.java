package reseau.couches;


import reseau.Message;
import reseau.adresses.AdresseMac;
import test.TestReseau;

/**
 * @author Xavier Chopin
 */
public class Ethernet extends Liaison12 {
    
    public Ethernet(AdresseMac am ) {
        super(am); 
    }
        
    public void sendMessage(AdresseMac dest, Message message) {
        TestReseau.afficherTour();
        Message m = new Message(getEntete(dest,message));
        m.ajouter(message);
        System.out.println("Je suis " + getNom() + " et j'envoie "+ m.size()+ " octets : " + m);
        System.out.println(" - - - - - - - - TRANSMISSION AU RESEAU LOCAL - - - - - - - - - -");
        reseau.sendTrame(m);
    }

    
    /**
     * Je reçois un message de la couche moinsUn
     * @param message
     */
    @Override
    public void receiveMessage(Message message) {
        TestReseau.afficherTour();
        if (message.size() > 5 ){
            AdresseMac adrm = message.extraireAdresseMac();
            // vérifier que l'adresse destination du message c'est bien nous
            if (adrm.equals(this.adrMac)){
                  message.supprimer(6); // suppression des 48 bits de l'entete : on veut prendre l'adresse mac source pour après !
                  System.out.println("Je suis "+getNom()+" apres suppression de l'entete je recois "+message.size()+" octets : " + message);
                 ((Reseau3)plusUn).receiveMessage(message);
            }else{
                System.err.println("Le message ne nous était pas destiné: Source: " + adrm + " | Nous: " + this.adrMac);
            }
        }else{
            System.err.println("Erreur: le message ne contient pas assez d'octet !");
        }

    }

    @Override
    protected Message getEntete(AdresseMac dest, Message message) {
        // Adresse Mac destination (48 bits), adresse Mac source (48 bits)
         Message m = new Message(dest);
         m.ajouter(this.adrMac);
         return m;
    }

}
