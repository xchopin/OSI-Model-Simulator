package reseau.couches;


import reseau.Message;
import reseau.adresses.Adresse;

/**
 * @author Xavier Chopin
 */
public class UDP extends Transport4 {

    public UDP() { 
        super();
    }
    
    @Override
    public Message getEntete(int portSource, Adresse adrDest, int portDest, Message message) {
        // Port source (16 bits), port destination (16 bits), longueur entete + données (16 bits), somme de controle (16 bits)

        //  Longueur Entete: on a 4 segments
        // chaque segment est composé de 2 octets
        // soit 8bits à multiplier par 2 pour le nombre de bits par segment qu'on multiplie par 4 à savoir le nombre de segment de l'entête

        Message m = new Message(portSource,portDest, 2*4 + message.size(),0);
        return m;
    }


}
