package reseau.couches;
import reseau.Message;
import reseau.tables.ARP;
import reseau.adresses.*;
import test.TestReseau;

/**
 * @author Xavier Chopin
 */
public class IP extends Reseau3 {

    static private int compteur = 0;
    protected Adresse masque ;
    private ARP arp = new ARP() ;
    
    /**
     * @param ici la machine où je suis
     * @param masque
     */
    public IP(Adresse ici, Adresse masque) {
        super(ici) ;
        this.masque = masque;
    }
    
     

    /**
     * @param dest
     * @param message
     * @return
     */
    public Message getEntete(Adresse dest, Message message) {
        // Longueur totale (16 bits), Identification (16 bits), Protocole (8 bits)
        // Adresse IP source (32 bits), Adresse IP destination (32 bits)
        Message m = new Message(dest.size() + message.size(),0);
        short protocole = 0;
        m.ajouter(protocole);
        m.ajouter(this.adresse);
        m.ajouter(dest);

        return m;
    }

        // N'EXISTE PAS C'EST IMPOSSIBLE => Retourne une adresse "mac" à 4 valeurs, ce qui est impossible.
    private AdresseMac getAdresseMac(Adresse adr) {
        return new AdresseMac(adr);
    }
    
    /**
     * @return mon adresse IP
     */
    public Adresse getAdresseIP() {
        return this.adresse;
    }
    
    /**
     * @param dest adresse du destinataire
     * @param message 
     */
    public void sendMessage(Adresse dest, Message message) {
        TestReseau.afficherTour();
        Message m = new Message(getEntete(dest,message));
        m.ajouter(message);
        Adresse sourceMasquee = new Adresse(this.adresse);
        sourceMasquee.masquer(this.masque);

        Adresse destMasquee = new Adresse(dest);
        destMasquee.masquer(this.masque);

        // Vérification s'ils font parti du même réseau local
        if (sourceMasquee.equals(destMasquee)){
           // System.out.println("Je suis "+getNom()+" et j'envoie "+m.size()+" octets : " +m);
            if (arp.adresseConnue(dest)){
                ((Liaison12)moinsUn).sendMessage(arp.get(dest), m);
            }else{
                System.out.println("Adresse IP: " + dest + " n'est pas référencé dans la table ARP de celle ci");
            }
        }else{
            System.out.println("La machine source et destination ne font pas partie du même réseau local");
        }



    } 
    



    @Override
    public void receiveMessage(Message message) {
        TestReseau.afficherTour();
        Adresse adrSource =  arp.get(message.extraireAdresseMac());
        message.supprimer(15); // suppression de l'@mac + début de l'entete
        Adresse adrDest = message.extraireAdresse(4);
        message.supprimer(4);
        System.out.println("Je suis "+getNom()+" apres suppression de l'entete je recois "+message.size()+" octets : " + message);

        if (adrDest.equals(this.adresse)){
            ((Transport4)plusUn).receiveMessage(adrSource,message); // On envoie l'adresse SOURCE
        }else{
            System.out.println("Pas la bonne adresse ip , Destination: " + adrDest+ " Nous: " + this.adresse);
        }

    }

    

}
