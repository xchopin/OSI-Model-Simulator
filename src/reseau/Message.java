package reseau;

import reseau.adresses.Adresse;
import reseau.adresses.AdresseMac;
import reseau.adresses.Octet;
import java.util.ArrayList;
import java.util.Iterator;



/**
 * @author Xavier Chopin
 */


public class Message implements Iterable<Octet> {
    protected ArrayList<Octet> alo;

    public Message() {
        this.alo = new ArrayList();
    }

    public Message(Message mess) {
        if (mess == null){
            throw new AssertionError("Erreur");
        }else {
            this.alo = new ArrayList(mess.alo);
        }
    }

    /**
     * @param v des petits entiers qui constituent le message
     */

    public Message(short... v) {
        this.alo = new ArrayList<Octet>(v.length); // allocation mémoire, mieux optimisé si on connait l'espace nécessaire de base //cf: API Java
        for( short b : v ) {
            this.alo.add(new Octet((int) b));
        }

    }


    /**
     * @param v des entiers qui constituent le message
     * Sujet: Int codé sur deux octets
     */
    public Message(int... v) {
        this.alo = new ArrayList();
        int[] arr$ = v;
        int len$ = v.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            int x = arr$[i$];
            this.ajouter((int)x);
        }

    }


    /**
     * @param mot
     */
    public Message(String mot) {

        this.alo = new ArrayList<Octet>();
        char[] tab = mot.toCharArray();

        for (int i = 0; i<tab.length;i++){
            this.alo.add( new Octet( (int)(tab[i]) ) );
        }

    }

    public Message(Adresse adr) {
        this.alo = new ArrayList<>();

        for ( int i = 0 ; i < adr.getAlo().length ; i++){
            this.alo.add( adr.getOctet(i) );
        }
    }

    public int size() {
        return this.alo.size();
    }


    /**
     * Ajouter un entier à la fin
     * @param x : un int
     */
    public void ajouter(short x) {
        this.alo.add(new Octet(x));
    }


    /**
     * Ajouter un entier à la fin
     * @param x : un short
     */
    public void ajouter(int x) {
        int xForts = x / 128;
        int xFaibles = x % 128;
        this.alo.add(new Octet(xForts));
        this.alo.add(new Octet(xFaibles));
    }



    /**
     * Ajouter un octet à la fin
     * @param o
     * @exception AssertionError si o est null
     */
    public void ajouter(Octet o) {
        if (o == null){
            throw new AssertionError("Erreur");
        }else{
            this.alo.add(o);
        }
    }


    /**
     * Concaténer un autre message
     * @param mess message à ajouter à la fin
     * @exception AssertionError si mess est null
     */
    public void ajouter(Message mess) {
        assert mess != null : "Message indéfini :" + mess;

        Iterator i = mess.iterator();

        while(i.hasNext()) {
            Octet o = (Octet)i.next();
            this.ajouter((Octet)o);
        }

    }

    public ArrayList<Octet> getAlo(){
        return this.alo;
    }

    public void ajouter(Adresse adr) {
        if (adr == null){
            throw new AssertionError("Erreur");
        }else{
            for ( int i = 0 ; i < adr.getAlo().length ; i++){
                this.alo.add(adr.getOctet(i));
            }
        }
    }



    @Override
    public String toString() {
        int i = 0;
        String res = "[";
        for( Octet o : this.alo ) {
            i++;
            if (i == alo.size()) {
                res += o.toString();
            }else{
                res += o.toString() + ", ";
            }

        }
        return res + "]";
    }
    public Iterator<Octet> iterator() {
        return this.alo.iterator();
    }

    public int extraireEntier(int index) {
        assert index >= 0 && index <= this.alo.size() - 1 : "Index incorrect :" + index;

        Octet forts = (Octet)this.alo.get(index);
        Octet faibles = (Octet)this.alo.get(index + 1);
        return forts.getValue() * 128 + faibles.getValue();
    }

    /**
     * Extraire les nbOctets premiers octets pour en faire une adresse
     * @param nbOctets
     * @exception AssertionError si nbOctets > longueur du message
     * @return une adresse
     */
    public Adresse extraireAdresse(int nbOctets) {
        Adresse adr = null;
        if (nbOctets > this.alo.size() ){
            throw new AssertionError("Erreur");
        }else{
            Octet[] tab = new Octet[nbOctets];

            for (int i = 0; i < nbOctets ; i++){
                tab[i] = this.alo.get(i);
            }

            adr = new Adresse(tab);

        }

        return adr;
    }
    /**
     * Extraire les 6 premiers octets pour en faire une adresse Mac
     * @exception AssertionError si le message ne contient pas au moins 8 octets
     * @return une adresse Mac
     */
    public AdresseMac extraireAdresseMac() {

        AdresseMac adr = null;
        if (this.alo.size() < 8 ){
            throw new AssertionError("Erreur");
        }else{
            Octet[] tab = new Octet[6];

            for (int i = 0; i < 6 ; i++){
                tab[i] = this.alo.get(i);
            }

            adr = new AdresseMac(tab);

        }

        return adr;
    }



    /**
     * Transformer le message en une suite de lettres, si possible
     * @return null si l'un des octets n'est pas une lettre (maj ou min)
     */
    public String extraireChaine() {
        StringBuilder sb = new StringBuilder("");
        Iterator i$ = this.alo.iterator();

        while(i$.hasNext()) {
            Octet o = (Octet)i$.next();
            if(o.estUneLettre() || o.estUnPoint()) {
                sb.append((char)o.getValue());
            }
        }

        return sb.toString();
    }

    /**
     * Augmenter de i chaque octet compris entre bi et bs
     * @param i
     * @param bi
     * @param bs
     */
    public void augmenter(int i, int bi, int bs) {

        for( Octet o : this.alo ) {
            if ( bi <= o.getValue() && o.getValue() <= bs ){
                o.ajouter(i);
            }
        }


    }


    /**
     * On enlève les i premiers éléments
     * @param i
     * @exception AssertionError si i n'est pas dans le domaine du tableau
     */
    public void supprimer(int i) {

        if (i < 0 || i > this.alo.size() ){
            throw new AssertionError("Erreur");
        }else {
            this.alo.subList(0, i).clear();
        }
    }

    /**
     * On enlève les éléments entre debut et fin inclus
     * @param debut
     * @param fin
     * @exception AssertionError si on n'a pas 0<=debut<=fin<size()
     */
    public void supprimer(int debut, int fin) {
        if (!(0 <= debut && debut <= fin && fin < this.alo.size() ) ){
            throw new AssertionError("Erreur");
        }else {
            this.alo.subList(debut, fin+1).clear();
        }
    }


}
