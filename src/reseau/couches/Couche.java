package reseau.couches;

/**
 * @author Xavier Chopin
 */
public abstract class Couche {
    protected Couche plusUn ;
    protected Couche moinsUn ;
    
    protected Couche() {    
    }
    
    /**
     * @param plusUn couche de niveau +1
     * @param moinsUn couche de niveau -1
     */
    protected Couche(Couche plusUn, Couche moinsUn) {
        setCouches(plusUn, moinsUn) ;
    }
    
    public void setCouches(Couche plusUn, Couche moinsUn) {
        this.setCoucheSuperieure(plusUn) ;
        this.setCoucheInferieure(moinsUn) ;
    }
    
    /**
     * Fixer la couche supérieure
     * @param c couche supérieure 
     */
    public void setCoucheSuperieure(Couche c) {
        this.plusUn = c ;
    }
    
    /**
     * Fixer la couche inférieure
     * @param c couche inférieure 
     */
    public void setCoucheInferieure(Couche c) {
        this.moinsUn = c ;
    }     
    
    public  String getNom() {
        return getClass().getName() ;
    }

    public String toString() {
        return "Couche+1: " + this.plusUn.getNom() + " - Couche-1:" + this.plusUn.getNom();
    }
    
}
