package reseau.adresses;

/**
 * created on 09/02/2016
 * @author Xavier CHOPIN
 */


public class Adresse {

    protected Octet[] alo;

    /**
     * Constructeur de copie
     * @param a
     * @exception AssertionError si a est null
     */
    public Adresse(Adresse a) {

        if (a == null) {
            throw new AssertionError("Erreur: l'argument est null");
        }else{
            this.alo = new Octet[a.alo.length];
            System.arraycopy(a.alo, 0, this.alo, 0, a.alo.length);
        }
    }

    /**
     * @param al octets
     * @exception AssertionError si al est null
     */
    public Adresse(Octet... al) {
        int i = 0;
        this.alo = new Octet[al.length];
        for( Octet o : al ) {
            if ( o == null) {
                throw new AssertionError("Erreur: l'argument est null");
            }else{
                alo[i] = o;
                i++;
            }

        }

    }

    /** Adresse 0
     * @param nbBits nombre de bits
     * @throw AssertionError si le nombre total de bits n'est pas un multiple de 8 supérieur ou égal à 8
     */
    public Adresse(int nbBits) {
        // deuxième test un peu inutile mais bon ...
        if ( (nbBits%8 !=0 ) || (nbBits < 8) ) {
            throw new AssertionError("Erreur: Pas un multiple de 8");
        }else{
            int nb = nbBits / 8;
            this.alo = new Octet[nb];
            for (int i = 0; i < nb; i++){
                this.alo[i] = new Octet();
            }
        }
    }

    /** Adresse masque
     * @param nbBits nombre de bits total
     * @param nbBitsUn nombre de bits à 1
     * @exception AssertionError si le nombre total de bits n'est pas un multiple de 8 supérieur ou égal à 8
     * @exception AssertionError si le nombre total de 1 est incorrect (négatif ou supérieur à nbBits)
     */
    public Adresse(int nbBits, int nbBitsUn) {

        if ( (nbBits%8 !=0 ) || (nbBits < 8) || (nbBitsUn < 0) || (nbBitsUn > nbBits) ) {
            throw new AssertionError("Erreur");
        }else {

            Octet[] tab=new Octet[nbBits/8];
            int compteurOctet=0;
            int compteurBits=0;
            for (int i = 1; i <= nbBitsUn; i++) {
                Octet o = new Octet();
                if (i%8==0 && i!=0) {
                    compteurBits=0;
                    o.setUn();
                    tab[compteurOctet++] = o;
                } else {
                    compteurBits++;

                    if ((8-compteurBits)>nbBitsUn-i) {
                        o.setUn(i % 8);

                        if (compteurBits == i%8 && i==nbBitsUn){
                            tab[compteurOctet++] = o;
                        }


                    }

                }

            }
            for (int i = compteurOctet; i < nbBits/8; i++) {
                tab[i]=new Octet();
            }
            this.alo=tab;

        }
    }


    public Octet[] getAlo(){
        return this.alo;
    }

    /**
     * @param s notation décimale pointée d'une adresse (par ex : 245.156.0.0)
     * @exception AssertionError si le nombre d'octets est différent de 4, 6, 8 ou si un entier est trop grand
     */
    public Adresse(String s) {
        String[] tab=s.split("\\.");

        if ( ! ( (tab.length == 4) || (tab.length == 6) || (tab.length == 8))){
            throw new AssertionError("Erreur");
        }else {

            this.alo = new Octet[tab.length];
            int i = 0;
            for (String comp : tab) {
                int nb = Integer.parseInt(comp);

                if (nb > 255) {
                    throw new AssertionError("Erreur");
                } else {
                    this.alo[i] = new Octet(nb);
                    i++;
                }

            }
        }

    }

    /**
     * @return le nombre de bits
     */
    public int size() {
        return alo.length * 8 ;
    }

    /**
     * @return le nombre d'octets
     */
    public int getNbreOctets() {
        return alo.length;
    }

    /**
     * Appliquer un masque
     * @param masque masque à appliquer
     * @exception AssertionError si le masque et le receveur ne sont pas de la même taille
     */
    public void masquer(Adresse masque) {
        if (getNbreOctets() != masque.getNbreOctets()){
            throw new AssertionError("Erreur: different");
        }else{
             for (int i = 0 ; i < this.alo.length ; i++){
                 alo[i].masquer(masque.getAlo()[i]);
             }
        }
    }

    /**
     * Inverser les octets (0 -> 1, 1 -> 0)
     */
    public void inverser() {
        for (int i = 0 ; i < this.alo.length ; i++){
            alo[i].inverser();
        }
    }

    /**  Fixer les octets
     * @param alo octets
     * @exception AssertionError si alo est null
     */
    public void setOctets(Octet... alo)  {
        int i = 0;
        this.alo = new Octet[alo.length];
        for( Octet o : alo ) {
            if ( o == null) {
                throw new AssertionError("Erreur: l'argument est null");
            }else{
                this.alo[i] = o;
                i++;
            }

        }
    }

    /**
     * Fixer un des octets de l'adresse
     * @param o octet
     * @param k et
     * @exception AssertionError si k n'est pas entre 0 et le nombre d'octets
     */
    public void setOctet(Octet o, int k)  {
        if ( (o == null) || (  ( (0 > k) || (k+1 > this.alo.length) ) ) )   {
            throw new AssertionError("Erreur");
        }else{
            this.alo[k] = o;
        }

    }

    /**
     * Consulter un des octets de l'adresse
     * @param k rang de l'octet
     * @exception AssertionError si k n'est pas entre 0 et le nombre d'octets
     */
    public Octet getOctet(int k) {
        Octet res = null;
        if (  ( (k < 0) || (k > this.alo.length) )  ) {
            throw new AssertionError("Erreur");
        }else{
            res = this.alo[k];
        }

        return res;
    }

    public String toString() {

        String res = "";

        if (getNbreOctets() > 1) {
            for (int i = 0; i < this.getNbreOctets(); i++) {

                res += this.getOctet(i);
                if (i != this.getNbreOctets()-1) {
                    res += ".";
                }
            }

        } else {
            res= this.getOctet(0).toString();
        }

        return res;

    }

}
