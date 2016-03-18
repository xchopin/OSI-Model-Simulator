package test;

import reseau.Machine;
import reseau.ReseauLocal;
import reseau.couches.IP;
import reseau.Message;
import reseau.couches.*;
import reseau.adresses.*;
import reseau.clientsServeurs.*;
import reseau.tables.ARP;
import reseau.tables.DNS;

/**
 * @author Xavier Chopin
 */

public class TestReseau {
    public static int tour = 0;


    /**
     * Fonction pour mieux afficher les étapes ( juste pour faciliter la lecture des SYSOUT dans la console ;) )
     */
    public static void afficherTour(){
        tour++;
        System.out.println("ETAPE NUMERO [" + tour+"]");
    };

    public TestReseau() {


        /** -------- CREATION D'UN SERVEUR DNS -------- */
        DNS tableDNS = new DNS();

        /** -------- CREATION DU CLIENT -------- */

        Adresse    IP     = new Adresse("192.23.23.23");
        AdresseMac mac    = new AdresseMac("00.01.02.03.04.05");
        Adresse    masque = new Adresse("255.255.0.0");
        int        port   = 45;

        Machine macBookPro = new Machine("MacBookPro", IP, mac, masque);
        ClientDNS clientDNS = macBookPro.getClientDNS();
        Application7 client = new ClientNumerique(port,macBookPro,clientDNS);
        macBookPro.ajouter(port, client );



        /** -------- CREATION DES SERVEURS -------- */

        IP   = new Adresse("192.23.12.12");
        mac  = new AdresseMac("24.88.90.00.FF.AB");
        port = 888;

        Machine serveurOVH = new Machine("ServeurOVH", IP, mac, masque);
        Application7 serveur = new ServeurFoisDeux(port,serveurOVH,serveurOVH.getClientDNS());
        serveurOVH.ajouter(port, serveur);

        // Ajout d'une nouvelle application serveur
        port = 42;
        serveur = new ServeurMaj(port,serveurOVH,serveurOVH.getClientDNS());
        serveurOVH.ajouter(port, serveur);


        // - - - DNS - - - /

        IP   = new Adresse("192.23.89.41");
        mac  = new AdresseMac("AA.CD.EF.00.AA.54");
        port = 52; // port classique du DNS dans un réseau local

        Machine serveurOpenDNS = new Machine("ServeurOpenDNS", IP, mac, masque);
        serveur = new ServeurDNS(port,serveurOpenDNS,tableDNS);
        serveurOpenDNS.ajouter(port,serveur);


        /** -------- AJOUT DES MACHINES AU CACHE DNS--------  */

        tableDNS.ajouter(macBookPro.getNomMachine(), macBookPro.getAdrIP());
        tableDNS.ajouter(serveurOVH.getNomMachine(), serveurOVH.getAdrIP());
        tableDNS.ajouter(serveurOpenDNS.getNomMachine(), serveurOpenDNS.getAdrIP());

        /** -------- CREATION D'UN RESEAU LOCAL -------- */

        ReseauLocal localNetwork = new ReseauLocal();
        localNetwork.ajouter(macBookPro,serveurOVH,serveurOpenDNS);

        /** -------- UTILISATION DE L'APPLICATION -------- */

        //  .1) Serveur Foix Deux

            //Description: le client envoie un message (ici un entier)
            // au serveur qui va le doubler,
            // puis le renvoyer au client à la fin du traitement

            int valeur ; Message mess, res;
            valeur = 10;
            mess = new Message(valeur);
            System.out.println("Je voudrais le double de l'entier " + valeur);

             client.sendMessage("ServeurOVH", 888, mess);
             res = client.getResultat();
             System.out.println("Et j'obtiens: " + res.extraireEntier(0) + "\n \n - - - - SERVEUR MAJ MAINTENANT: - - - - ");


            tour = 0;

        // 2.) Serveur MaJ

            //Description: le client envoie un message (ici une chaine de caractères)
            // au serveur qui va la majusculiser
            // puis le renvoyer au client à la fin du traitement
            // Attention l'algo fait en sorte de prendre uniquement les lettres, on ne s'occupe pas des accents ni des espaces (Sujet)

            String texte ;
            texte = "yolo le tp rajoute des questions sans prévenir";
            mess = new Message(texte);
            System.out.println("Majusculisation de: " + texte);

            client.sendMessage("ServeurOVH", 42, mess);
            res = client.getResultat();
            System.out.println("Et j'obtiens (sans les espaces): " + res.extraireChaine());



    }

    public static void main(String[] args) {
        new TestReseau();
    }
    
}