package reseau;
import reseau.adresses.*;
import java.util.ArrayList;


/**
 * @author Xavier Chopin
 */

public class Message {

    ArrayList<Octet> liste;

    public Message() {
        this.liste = new ArrayList<Octet>();
    }
    
    /**
     * Constructeur de copie
     * @param mess 
     * @exception AssertionError si mess est null
     */
    public Message(Message mess) {
        if (mess == null){
            throw new AssertionError("Erreur");
        }else{
            this.liste = new ArrayList<Octet>(mess.liste);
        }

    }
    
    /**
     * @param v des petits entiers qui constituent le message
     */
    public Message(short... v) {
        this.liste = new ArrayList<Octet>(v.length); // allocation mémoire, mieux optimisé si on connait l'espace nécessaire de base //cf: API Java
        for( short b : v ) {
            this.liste.add(new Octet((int) b));
        }

    }
    
    /**
     * @param v des entiers qui constituent le message
     * Sujet: Int codé sur deux octets
     */
    public Message(int... v) {
        int taille, part1, part2, diviseur;
        this.liste = new ArrayList<Octet>( v.length * 2); // allocation mémoire, mieux optimisé si on connait l'espace nécessaire de base //cf: API Java
        for( int b : v ) {

            if (b > 255){
                /**  Méthode:
                 *
                 * I)
                 * Big Endian:    1)   256   => (taille <- 3
                 *                              diviseur <- ( (3-2) * 10) = 10 //// nb/diviseur => 25
                 *                               25 * diviseur = 250 ainsi: 256 - 250 = 6
                 *                                      =>  octet1: 25 | octet2: 6 <=  MARCHE
                 *
                 *                        2)  7500   => (taille <- 4 , diviseur <- (4-2) * 10 = 100 ,  nb/diviseur => 75
                 *                              Ensuite: 75 * diviseur : 7500 , 7500 -7500 = 0 ...
                 *                              
                 *                                          NE MARCHE PAS TOTALEMENT (UN SEUL ZERO AU LIEU DE DEUX)
                 *
                 *
                 *                         3)  7501   => (taille <- 4 , diviseur <- (4-2) * 10 = 100 ,  nb/diviseur => 75
                 *                              Ensuite: 75 * diviseur : 7500 , 7500 -7501 = 1 ...
                 *
                 *                                              =>  octet1: 75 | octet2: 1 <=
                 *                                    NE MARCHE PAS TOTALEMENT (PAS DE ZERO AVANT LE 1)
                 *
                 *
                 */

                taille = String.valueOf(b).length();
                diviseur = (taille-2) * 10;
                part1 = (int) Math.floor( (  b / diviseur ) ); // prend la valeur inférieur ex: 15.6 devient 15
                part2 = b - (part1 * diviseur);
                this.liste.add(new Octet(part1));
                this.liste.add(new Octet(part2));

            }else{

                this.liste.add(new Octet());
                this.liste.add(new Octet(b));
            }



        }
    }



    /**
     * @param mot 
     */
    public Message(String mot) {
        this.liste = new ArrayList<Octet>();
        char[] tab = mot.toCharArray();

        for (int i = 0; i<tab.length;i++){
            this.liste.add( new Octet( (int)(tab[i]) ) );
        }

    }
    
    /**
     * @param adr 
     */
    public Message(Adresse adr) {
        this.liste = new ArrayList<Octet>();

        for ( int i = 0 ; i < adr.getAlo().length ; i++){
            this.liste.add( adr.getOctet(i) );
        }

    }
        
    /**
     * @return le nombre d'octets
     */
    public int size() {
        return this.liste.size();
    }
    
    /**
     * Ajouter un petit entier à la fin
     * @param x
     */
    public void ajouter(short x) {
        this.liste.add(new Octet( (int) x ));
    }
    
    /**
     * Ajouter un entier à la fin 
     * @param x
     */
    public void ajouter(int x) {
        int taille, part1, part2, diviseur;

        if (x > 255){
            taille = String.valueOf(x).length();
            diviseur = (taille-2) * 10;
            part1 = (int) Math.floor( (  x / diviseur ) ); // prend la valeur inférieur ex: 15.6 devient 15
            part2 = x - (part1 * diviseur);
            this.liste.add(new Octet(part1));
            this.liste.add(new Octet(part2));
        }else{
            this.liste.add(new Octet());
            this.liste.add(new Octet( x ));
        }


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
            this.liste.add(o);
        }

    }
    
    /**
     * Concaténer un autre message
     * @param mess message à ajouter à la fin
     * @exception AssertionError si mess est null
     */
    public void ajouter(Message mess) {
        if (mess == null){
            throw new AssertionError("Erreur");
        }else{
            for (int i = 0; i < mess.liste.size(); i++){
              this.liste.add(mess.liste.get(i));
            }
        }
    }
    
    /**
     * Ajouter les octets d'une adresse à la fin
     * @param adr
     * @exception AssertionError si adr est null
     */
    public void ajouter(Adresse adr) {
        if (adr == null){
            throw new AssertionError("Erreur");
        }else{
            for ( int i = 0 ; i < adr.getAlo().length ; i++){
                this.liste.add( adr.getOctet(i) );
            }
        }
    }
    
    @Override
    public String toString() {
        int i = 0;
        String res = "[";
        for( Octet o : this.liste ) {
            i++;
            if (i == liste.size()) {
                res += o.toString();
            }else{
                res += o.toString() + ", ";
            }

        }
        return res + "]";
    }

    /**
     * Extraire les 2 octets situés en index et index+1 pour en faire un entier 
     * octets forts puis faibles (big endian)
     * @param index
     * @exception AssertionError si index ou index+1 n'est pas dans le domaine du tableau
     * @return un entier
     */
    public int extraireEntier(int index) {
        int res;

        if (index < 0 || index+1 > this.liste.size()){
            throw new AssertionError("Erreur");
        }else{
            // marche jusqu'à 999, j'ai rien trouvé de mieux...
            res = ( (this.liste.get(index).getValue() * 10) + this.liste.get(index+1).getValue());
        }

        return res;
    }
    
    /**
     * Extraire les nbOctets premiers octets pour en faire une adresse
     * @param nbOctets
     * @exception AssertionError si nbOctets > longueur du message 
     * @return une adresse
     */
    public Adresse extraireAdresse(int nbOctets) {
        Adresse adr = null;
        if (nbOctets > this.liste.size() ){
            throw new AssertionError("Erreur");
        }else{
           Octet[] tab = new Octet[nbOctets];

            for (int i = 0; i < nbOctets ; i++){
                tab[i] = this.liste.get(i);
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
        if (this.liste.size() < 8 ){
            throw new AssertionError("Erreur");
        }else{
            Octet[] tab = new Octet[6];

            for (int i = 0; i < 6 ; i++){
                tab[i] = this.liste.get(i);
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
        String res = "";

        for( Octet o : this.liste ) {
            if (o.estUneLettre() == false){
                return null;
            }else{
                res += Character.toString((char) o.getValue());
            }
        }

        return res ;
    }

    /**
     * Augmenter de i chaque octet compris entre bi et bs
     * @param i 
     * @param bi 
     * @param bs 
     */
    public void augmenter(int i, int bi, int bs) {

        for( Octet o : this.liste ) {
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

        if (i < 0 || i > this.liste.size() ){
            throw new AssertionError("Erreur");
        }else {
            this.liste.subList(0, i).clear();
        }
    }
    
    /**
     * On enlève les éléments entre debut et fin inclus
     * @param debut
     * @param fin
     * @exception AssertionError si on n'a pas 0<=debut<=fin<size()
     */
    public void supprimer(int debut, int fin) {
        if (!(0 <= debut && debut <= fin && fin < this.liste.size() ) ){
            throw new AssertionError("Erreur");
        }else {
            this.liste.subList(debut, fin+1).clear();
        }
    }

}
