package reseau.tables;

import reseau.adresses.Adresse;
import java.util.HashMap;

/**
 * Created by Xavier on 08/03/2016.
 */
public class DNS {

    public HashMap <String, Adresse> cache;

    public DNS(){
        this.cache = new HashMap<>();
    }


    /**
     * Fonction permettant d'ajouter une machine et son adresse IP au cache DNS
     * @param nomMachine
     * @param adrIP
     */
    public void ajouter(String nomMachine,Adresse adrIP){
        this.cache.put(nomMachine, adrIP);
    }

    public Adresse getAdresse(String nomMachine){
        return cache.get(nomMachine);
    }


    public String getMachine(Adresse adr) {
            for (String s : cache.keySet()) {
                if (cache.get(s).equals(adr)) {
                    return s;
                }
            }
       return null;
    }




    /**
     * Fonction permettant de savoir si une machine est présente dans le cache DNS
     * @param name : Nom de la machine
     * @return vrai ou faux
     */
    public boolean machineConnue(String name) {
        return this.cache.containsKey(name);
    }

}
