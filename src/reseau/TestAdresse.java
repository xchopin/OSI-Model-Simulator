package reseau;

import reseau.adresses.*;

/**
 * created on 09/02/2016
 * @author Xavier CHOPIN
 */

public class TestAdresse {

    public static void main(String[] args) {
        Adresse adr;
        Adresse adr2;


        adr = new Adresse(new Octet(10)) ;
        assert(adr.toString().equals("10")) ;

        adr = new Adresse("192.45.43.100");
        assert adr.toString().equals("192.45.43.100");

        // A compléter, bien sûr ....

        adr = new Adresse(new Octet(10)) ;
        assert(adr.toString().equals("10")) ;

        adr2 = new Adresse(adr);
        assert(adr2.toString().equals("10")) ;

        // - - - - - - - - - - - - - - - - -

        adr2 = new Adresse(adr.getAlo());
        assert(adr2.toString().equals("10")) ;

        // - - - - - - - - - - - - - - - - -

        adr = new Adresse(16);
        assert (adr.getNbreOctets() == 2);


        // - - - - - - - - - - - - - - - - -

        adr = new Adresse(32,8);
        assert(adr.toString().equals("255.0.0.0")) ;

        adr = new Adresse(32,10);
        assert(adr.toString().equals("255.192.0.0")) ;

        // - - - - - - - - - - - - - - - - -

        adr = new Adresse("192.168.1.1");
        adr2 = new Adresse("0.0.0.255");
        adr.masquer(adr2);
        assert(adr.toString().equals("0.0.0.1")) ;

        // - - - - - - - - - - - - - - - - -

        adr.inverser();
        assert(adr.toString().equals("255.255.255.254")) ;
        adr2.setOctets(adr.getAlo());

        assert(adr2.toString().equals("255.255.255.254")) ;

        // - - - - - - - - - - - - - - - - -

        adr2 = new Adresse(32);
        adr2.setOctet(new Octet(255),0);
        assert(adr2.toString().equals("255.0.0.0"));

        // - - - - - - - - - - - - - - - - -

        adr = new Adresse("192.168.1.1");
        assert( adr.getOctet(0).toString().equals("192") );



    }

}
