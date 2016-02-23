package reseau;
import reseau.adresses.*;
/**
 * @author Xavier Chopin
 */

public class TestMessage {

    public static void main(String[] args) {
        Message m ;
        Message m1 ;

        m = new Message(10, 20, 30);
        assert m.toString().equals("[0, 10, 0, 20, 0, 30]");

        m = new Message(256);
        assert m.toString().equals("[25, 6]");

        m = new Message(875);
        assert m.toString().equals("[87, 5]");

        // A compléter bien sûr .....

        m1 = new Message(m);
        assert m1.toString().equals("[87, 5]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        short[] tab_short = {1,2,3,4};
        m = new Message(tab_short);
        assert m.toString().equals("[1, 2, 3, 4]");


        // - - - - - - - - - - - - - - - - - - - - - - - - -

        String s = "Xavier";
        m = new Message(s);
        assert m.toString().equals("[88, 97, 118, 105, 101, 114]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        Adresse adr = new Adresse("192.168.1.1");
        m = new Message(adr);
        assert m.toString().equals("[192, 168, 1, 1]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        assert (m.size() == 4);

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        short x = 1;
        m.ajouter(x);
        assert m.toString().equals("[192, 168, 1, 1, 1]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        m.ajouter(10);
        assert m.toString().equals("[192, 168, 1, 1, 1, 0, 10]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        m.ajouter(321);
        assert m.toString().equals("[192, 168, 1, 1, 1, 0, 10, 32, 1]");


        // - - - - - - - - - - - - - - - - - - - - - - - - -

        m.ajouter(new Octet(5));
        assert m.toString().equals("[192, 168, 1, 1, 1, 0, 10, 32, 1, 5]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        m1 = new Message(9);
        m.ajouter(m1);
        assert m.toString().equals("[192, 168, 1, 1, 1, 0, 10, 32, 1, 5, 0, 9]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        adr = new Adresse("192.168.1.1");
        m = new Message("X");
        m.ajouter(adr);
        assert m.toString().equals("[88, 192, 168, 1, 1]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        m = new Message(26,857);
        assert ( m.extraireEntier(0)==26);
        assert ( m.extraireEntier(2)==857);


        // - - - - - - - - - - - - - - - - - - - - - - - - -

        adr = new Adresse("192.168.1.1");
        m = new Message(adr);
        adr = m.extraireAdresse(4);

        assert(adr.toString().equals("192.168.1.1"));


        // - - - - - - - - - - - - - - - - - - - - - - - - -
        Adresse mac;
        Octet[] t =  { new Octet(0),new Octet(0),new Octet(0),new Octet(0),
                new Octet(0),new Octet(0),new Octet(0),new Octet(0)};
        m = new Message(new AdresseMac(t));
        mac = m.extraireAdresseMac();
        assert mac.toString().equals("0.0.0.0.0.0");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        m = new Message("Xavier");
        assert ( m.extraireChaine().equals("Xavier") );

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        short[] tab_s = {10,12,1,15};
        m = new Message(tab_s);
        m.augmenter(2,10,12);
        assert m.toString().equals("[12, 14, 1, 15]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        m.supprimer(2);
        assert m.toString().equals("[1, 15]");

        // - - - - - - - - - - - - - - - - - - - - - - - - -

        m = new Message(tab_s); // [10, 12, 1, 15]
        m.supprimer(1,2);
        assert m.toString().equals("[10, 15]");



    }

}
