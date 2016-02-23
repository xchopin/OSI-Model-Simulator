package reseau.adresses;

public class Octet {
    private boolean[] bits;

    public Octet() {
        this(0);
    }

    public Octet(int entier) {
        this.setValue(entier);
    }

    public Octet(Octet o) {
        this.bits = new boolean[8];

        for(int k = 7; k >= 0; --k) {
            this.bits[k] = o.bits[k];
        }

    }

    private void setValue(int entier) {
        assert entier <= 255 && entier >= 0 : "Entier incorrect :" + entier;

        this.bits = new boolean[8];

        for(int k = 7; k >= 0; --k) {
            this.bits[k] = entier % 2 == 1;
            entier /= 2;
        }

    }

    public void setUn(int nbUn) {
        assert nbUn >= 0 && nbUn <= 8 : "Nombre de bits incorrect";

        for(int k = 0; k < nbUn; ++k) {
            this.bits[k] = true;
        }

    }

    public void setUn() {
        this.setUn(8);
    }

    public boolean estUneLettre() {
        int val = this.getValue();
        return val >= 97 && val <= 122 || val >= 65 && val <= 90;
    }

    public boolean estUnPoint() {
        int val = this.getValue();
        return val == 46;
    }

    public boolean plusPetitQue(int i) {
        return this.getValue() < i;
    }

    public int getValue() {
        int res = 0;
        int puiss2 = 1;

        for(int k = 7; k >= 0; --k) {
            if(this.bits[k]) {
                res += puiss2;
            }

            puiss2 *= 2;
        }

        return res;
    }

    public void inverser() {
        for(int k = 7; k >= 0; --k) {
            this.bits[k] = !this.bits[k];
        }

    }

    public void masquer(Octet masque) {
        assert masque != null : "Masque indéfini";

        for(int k = 7; k >= 0; --k) {
            this.bits[k] = this.bits[k] && masque.bits[k];
        }

    }

    public void ajouter(int i) {
        this.setValue(this.getValue() + i);
    }

    public String toString() {
        return this.getValue() + "";
    }

    public static void main(String[] args) {
        Octet o = new Octet(126);
        Octet m = new Octet();
        o.masquer(m);

        assert o.toString().equals("0");

        assert m.toString().equals("0");

        o = new Octet(126);
        m = new Octet(1);
        o.masquer(m);

        assert o.toString().equals("0");

        assert m.toString().equals("1");

        m = new Octet(127);
        o.masquer(m);

        assert o.toString().equals("0");

        assert m.toString().equals("127");

        o = new Octet(126);
        m = new Octet(116);
        o.masquer(m);

        assert o.toString().equals("116");

        assert m.toString().equals("116");

        o = new Octet(10);
        m = new Octet(100);
        o.masquer(m);

        assert o.toString().equals("0");

        assert m.toString().equals("100");

        o = new Octet(100);
        m = new Octet(100);
        o.masquer(m);

        assert o.toString().equals("100");

        assert m.toString().equals("100");

        o = new Octet(127);
        o.inverser();

        assert o.toString().equals("128");

        o = new Octet(12);
        o.inverser();

        assert o.toString().equals("243");

        o = new Octet(1);
        o.inverser();

        assert o.toString().equals("254");

        byte val = 33;
        o = new Octet(val);

        assert o.toString().equals("33");

        o = new Octet(98);

        assert o.estUneLettre();

        o.ajouter(-32);

        assert (char)o.getValue() == 66;

        o = new Octet(122);

        assert o.estUneLettre();

        o.ajouter(-32);

        assert (char)o.getValue() == 90;

    }
}
