/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafa
 */
public class Doctor extends Agent {
    
    public void setup() {
        System.out.println("Doctor");
        SearchConstraints sc = new SearchConstraints();
        
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("hospital-servicio");
        template.addServices(sd);
        
        try {
            DFAgentDescription[] matches = DFService.searchUntilFound(this, new AID("df", AID.ISLOCALNAME), template, sc, 1000);
            if (matches.length > 0) {
                this.addBehaviour(new SolicitarPaciente(this, matches[0].getName()));
            }
        } catch (FIPAException ex) {
            Logger.getLogger(Ambulancia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class SolicitarPaciente extends CyclicBehaviour {
        
        AID hospital;
        
        public SolicitarPaciente(Agent a, AID hospital) {
            super(a);
            this.hospital = hospital;
        }

        @Override
        public void action() {
            //Petición para atender a un paciente.
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.setConversationId("hospital-doctor-conversacion");
            request.setContent("doctor-request");
            request.setSender(this.getAgent().getAID());
            request.addReceiver(hospital);
            this.getAgent().send(request);
            
            
            ACLMessage response = this.getAgent().blockingReceive();
            
            if (response.getPerformative() == ACLMessage.INFORM) {
                //Hay un paciente.
                int gravedad = Integer.parseInt(response.getContent());
                System.out.println(this.getAgent().getAID().getLocalName() + 
                        ": atiendo a un paciente de gravedad: " + gravedad);
                
                try {
                    //Bloqueamos al doctor durante un tiempo que depende de la gravedad
                    //del paciente.
                    Thread.sleep(gravedad * 1000);
                    
                    System.out.println(this.getAgent().getAID().getLocalName() + 
                        ": he terminado con el paciente");
                } catch (InterruptedException ex) {
                }
            }
        }

    }
}
