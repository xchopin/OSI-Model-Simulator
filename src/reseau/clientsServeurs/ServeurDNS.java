package reseau.clientsServeurs;

import reseau.Machine;
import reseau.Message;
import reseau.adresses.Adresse;
import reseau.couches.Application7;
import reseau.couches.Transport4;
import reseau.tables.DNS;

/**
 * @author: Xavier CHOPIN
 */
public class ServeurDNS extends Application7 {

        private DNS tableDNS;

        /**
         * @param port le port dans la couche transport
         * @param machine: le serveur qui héberge la machine
         * @param dns
         */
        public ServeurDNS(int port, Machine machine, DNS dns) {
            super(port,machine);
            this.tableDNS = dns;
        }

        /**
         * @param message Nom de la machine à checker sur la table DNS
         * @return message: l'adresse IP
         */
        @Override
        protected Message traiter(Message message) {
            String nomMachine = message.extraireChaine();
            System.out.println("SERVEUR: Accès à la table DNS");
            if (this.tableDNS.machineConnue(nomMachine)){
                Adresse dest = this.tableDNS.getAdresse(nomMachine);
                return new Message(dest);
            }else{
                System.err.println("Machine: "+ nomMachine + " inconnue auprès du serveur DNS");
            }

            return null;
        }

}
