package reseau;

import reseau.couches.Liaison12;

import java.util.ArrayList;

/**
 * @author: Xavier Chopin
 */
public class ReseauLocal {

    private ArrayList<Machine> listMachines;

    public ReseauLocal(){
        this.listMachines = new ArrayList<>();
    }

    /**
     * Fonction pour ajouter une machine au réseau local
     * @param m
     */
    public void ajouter(Machine m){
        this.listMachines.add(m);
        m.getEthernet().setReseau(this);
    }


    /**
     * Fonction pour ajouter une liste de machines au réseau local
     * @param m
     */
    public void ajouter(Machine...m){

        for (Machine machine : m){
            this.listMachines.add(machine);
             machine.getEthernet().setReseau(this);
        }

    }

    /**
     * Fonction pour relier les machines du réseau local entre elles
     */
    public void relierMachines(){
        //S'il y au moins deux machines...
        if (this.listMachines.size() > 0){
            Machine actuelle,suivante;
            for (int i = 0; i < this.listMachines.size() - 1; i++) {
                actuelle = this.listMachines.get(i);
                suivante = this.listMachines.get(i + 1);
                actuelle.relier(suivante);
            }
        }else{
            throw new AssertionError("Il y a seulement " + this.listMachines.size() +
                                     " machines, ce n'est pas assez pour le câblage");
        }
    }


    public void sendTrame(Message m){
        Message original;

        for (Machine machine : listMachines){
            original = new Message(m);
            System.out.println("- - - \nEnvoie du message à "+ machine.getNomMachine());
            machine.getEthernet().receiveMessage(original);
        }

    }

}
