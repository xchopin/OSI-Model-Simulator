package reseau.tables;

import java.util.HashMap;
import reseau.adresses.Adresse;
import reseau.adresses.AdresseMac;

/**
 * @author Xavier Chopin
 */
public class ARP {

    private HashMap<Adresse, AdresseMac> ipMac;
    private HashMap<AdresseMac, Adresse> macIp;

    public ARP() {
        ipMac = new HashMap<>();
        macIp = new HashMap<>();
        // Attention, l'octet de poids fort ne doit pas être impair dans une adresse Mac, sinon, c'est du multicast
        ajouter(new Adresse("192.23.23.23"), new AdresseMac("00.01.02.03.04.05"));
        ajouter(new Adresse("192.23.12.12"), new AdresseMac("24.88.90.00.FF.AB"));
        ajouter(new Adresse("192.23.89.41"), new AdresseMac("AA.CD.EF.00.AA.54"));
        
    }

    public void ajouter(Adresse adresse, AdresseMac adresseMac) {
        ipMac.put(adresse, adresseMac);
        macIp.put(adresseMac, adresse);
    }
    
    public boolean adresseConnue(Adresse adr) {
        return ipMac.containsKey(adr);
    }
    
    public boolean adresseConnue(AdresseMac adr) {
        return macIp.containsKey(adr);
    }
    
    /**
     * @param adr
     * @return l'adresse Mac correspondant à cette adr IP ou null si l'adresse est inconnue
     */
    public AdresseMac get(Adresse adr) {
        return ipMac.get(adr) ;
    }
    
    /**
     * @param adr
     * @return l'adresse IP correspondant à cette adr Mac
     * @exception AssertionError si l'adresse n'est pas connue
     */
    public Adresse get(AdresseMac adr) {
        return macIp.get(adr) ;
    }
}
