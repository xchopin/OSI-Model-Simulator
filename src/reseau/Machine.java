package reseau;

import com.sun.deploy.util.SessionState;
import reseau.adresses.Adresse;
import reseau.adresses.AdresseMac;
import reseau.clientsServeurs.ClientDNS;
import reseau.clientsServeurs.ClientNumerique;
import reseau.clientsServeurs.ServeurFoisDeux;
import reseau.couches.*;

import java.util.ArrayList;

/**
 * @author: Xavier CHOPIN
 */

public class Machine {

    private String nomMachine;
    private Adresse adrIP;
    private AdresseMac adrMac;
    private Adresse masque;
    private Ethernet ethernet;
    private IP ip;
    private UDP udp;
    private ArrayList <Application7> appList;
    private ClientDNS clientDNS; //Application Cliente capable d'interroger le serveur DNS

    /**
     * Constructeur d'une Machine
     * @param nom
     * @param IP
     * @param mac
     * @param masque
     */
    public Machine(String nom, Adresse IP, AdresseMac mac, Adresse masque){

        this.nomMachine = nom;
        this.adrIP = IP;
        this.adrMac = mac;
        this.masque = masque;

        /** Création des couches */
        this.ethernet = new Ethernet(adrMac);
        this.ip = new IP(adrIP, masque);
        this.udp = new UDP();
        this.appList = new ArrayList<>();

        /** Client DNS */
        this.clientDNS = new ClientDNS(1024,this);
        this.ajouter(1024,this.clientDNS);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Machine machine = (Machine) o;

        if (adrIP != null ? !adrIP.equals(machine.adrIP) : machine.adrIP != null) return false;
        if (nomMachine != null ? !nomMachine.equals(machine.nomMachine) : machine.nomMachine != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nomMachine != null ? nomMachine.hashCode() : 0;
        result = 31 * result + (adrIP != null ? adrIP.hashCode() : 0);
        return result;
    }

    /**
     * Fonction ajouter permettant de définir les différentes couches de la machine
     * @param port : port UDP
     * @param app : Application Cliente ou Serveur
     */
    public void ajouter(int port, Application7 app){

        /** Liaison des couches */
            // .Supérieures
            this.udp.setCoucheSuperieure(app);
            this.ip.setCoucheSuperieure(this.udp);
            this.ethernet.setCoucheSuperieure(this.ip);

            // .Inférieures
            app.setCoucheInferieure(this.udp);
            this.udp.setCoucheInferieure(this.ip);
            this.ip.setCoucheInferieure(this.ethernet);

            this.udp.ajouter(port,app);
            this.appList.add(app);
    }

    /**
     * Fonction pour brancher en ethernet ce PC à une autre machine
     * @param m : la machine destination
     */
    public void relier(Machine m){
        this.ethernet.setVoisin(m.getEthernet());
        m.getEthernet().setVoisin(this.ethernet);
    }



    /** Getters & Setters */

    public ClientDNS getClientDNS() { return this.clientDNS; }

    public Liaison12 getLiaison12(){
        return this.ethernet;
    }

    public String getNomMachine() {
        return nomMachine;
    }

    public void setNomMachine(String nomMachine) {
        this.nomMachine = nomMachine;
    }

    public Adresse getAdrIP() {
        return adrIP;
    }

    public void setAdrIP(Adresse adrIP) {
        this.adrIP = adrIP;
    }

    public AdresseMac getAdrMac() {
        return adrMac;
    }

    public void setAdrMac(AdresseMac adrMac) {
        this.adrMac = adrMac;
    }

    public Adresse getMasque() {
        return masque;
    }

    public void setMasque(Adresse masque) {
        this.masque = masque;
    }

    public Ethernet getEthernet() {
        return ethernet;
    }

    public void setEthernet(Ethernet ethernet) {
        this.ethernet = ethernet;
    }

    public IP getIp() {
        return ip;
    }

    public void setIp(IP ip) {
        this.ip = ip;
    }

    public UDP getUdp() {
        return udp;
    }

    public void setUdp(UDP udp) {
        this.udp = udp;
    }



}
