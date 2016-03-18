package reseau.couches;
import reseau.Message;
import reseau.adresses.Adresse;

/**
 * @author Xavier CHOPIN
 */
public abstract class Reseau3 extends Couche {

    protected Adresse adresse ;
    
    /**
     * @param adr Adresse de la machine sur laquelle je suis
     */
    public Reseau3(Adresse adr) {
        super();
        this.adresse = adr ;
    }

    public abstract void receiveMessage(Message message) ;

    public abstract void sendMessage(Adresse dest, Message message);

}
