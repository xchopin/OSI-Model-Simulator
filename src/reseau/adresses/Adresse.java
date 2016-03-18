package reseau.adresses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;



/**
 * created on 09/02/2016
 * @author Xavier CHOPIN
 */
public class Adresse implements Iterable<Octet> {
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
        assert al != null : "Adresse indéfinie";

        this.alo = new Octet[al.length];
        this.setOctets(al);
    }


    /** Adresse 0
     * @param nbBits nombre de bits
     * @throw AssertionError si le nombre total de bits n'est pas un multiple de 8 supérieur ou égal à 8
     */
    public Adresse(int nbBits) {
        if ( (nbBits%8 !=0 ) || (nbBits < 8) ) {
            throw new AssertionError("Erreur: Pas un multiple de 8");
        }else{
            int nbOctets = nbBits / 8;

            this.alo = new Octet[nbOctets];

            for(int k = 0; k < nbOctets; ++k) {
                this.alo[k] = new Octet();
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
        return this.alo.length;
    }


    /**
     * Appliquer un masque
     * @param masque masque à appliquer
     */
    public void masquer(Adresse masque) {
        assert masque.size() == this.size() : "Taille de masque incorrecte";

        for(int k = 0; k < this.getNbreOctets(); ++k) {
            Octet no = new Octet(this.getOctet(k));
            no.masquer(masque.getOctet(k));
            this.setOctet(no, k);
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

    public void setOctets(Octet... alo) {
        assert alo != null : "Paramètre incorrect";

        this.alo = alo;
    }

    public Octet[] getAlo() { return this.alo;};

    public Octet[] getOctets() {
        return this.alo;
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

    public Iterator<Octet> iterator() {
        return Arrays.asList(this.alo).iterator();
    }

    public int hashCode() {
        return this.alo[0].getValue();
    }

    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            Adresse other = (Adresse)obj;
            return this.toString().equals(other.toString());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("");

        for(int i = 0; i < this.alo.length; i++) {
            sb.append(this.alo[i] + ".");
        }

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();

    }

    public static void main(String[] args) {
        Adresse adr = new Adresse("192.45.43.100");

        assert adr.toString().equals("192.45.43.100");

        Adresse mask = new Adresse("255.255.255.255");
        adr.masquer(mask);

        assert mask.toString().equals("255.255.255.255");

        assert adr.toString().equals("192.45.43.100");

        adr = new Adresse("192.45.43.100");
        mask = new Adresse("255.255.255.000");
        adr.masquer(mask);

        assert mask.toString().equals("255.255.255.0");

        assert adr.toString().equals("192.45.43.0");

        adr = new Adresse(16, 8);

        assert adr.toString().equals("255.0");

        adr.inverser();

        assert adr.toString().equals("0.255");

        adr = new Adresse(32, 8);

        assert adr.toString().equals("255.0.0.0");

        adr.inverser();

        assert adr.toString().equals("0.255.255.255");

        adr = new Adresse(32, 12);

        assert adr.toString().equals("255.240.0.0");

        adr.inverser();

        assert adr.toString().equals("0.15.255.255");

        adr = new Adresse(32, 17);

        assert adr.toString().equals("255.255.128.0");

        adr.inverser();

        assert adr.toString().equals("0.0.127.255");

        adr = new Adresse(32, 17);
        Adresse adr2 = new Adresse(32, 17);

        assert adr.equals(adr2);

        assert adr.hashCode() == adr2.hashCode();

    }


}
