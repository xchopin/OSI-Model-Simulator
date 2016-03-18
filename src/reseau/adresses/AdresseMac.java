package reseau.adresses;

public class AdresseMac extends Adresse {
    public AdresseMac(Adresse a) {
        super(a);
    }

    public AdresseMac(Octet... alo) {
        this.setOctets(alo);
    }

    public AdresseMac() {
        super(48);
    }

    public AdresseMac(String s) {
        String[] spli = s.split("\\.");
        if(spli.length != 6) {
            throw new IllegalArgumentException("Nombre d\'octets incorrect");
        } else {
            this.alo = new Octet[6];
            int k = 0;
            String[] arr$ = spli;
            int len$ = spli.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String oc = arr$[i$];
                int val = Integer.parseInt(oc, 16);
                this.alo[k] = new Octet(val);
                ++k;
            }

        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("");
        Octet[] arr$ = this.alo;
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Octet o = arr$[i$];
            int val = o.getValue();
            sb.append(Integer.toHexString(val) + ".");
        }

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args) {
        AdresseMac adr = new AdresseMac("00.ff.00.ff.00.ff");

        assert adr.toString().equals("0.ff.0.ff.0.ff");

    }
}
