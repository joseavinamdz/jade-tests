/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
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
public class Ambulancia extends Agent {
    public void setup() {
        SearchConstraints sc = new SearchConstraints();
        
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("hospital-service");
        template.addServices(sd);
        
        try {
            DFAgentDescription[] matches = DFService.searchUntilFound(this, new AID("df", AID.ISLOCALNAME), template, sc, 1000);
            
            if (matches.length > 0) {
                this.addBehaviour(new EnviarPacientes(this, matches[0].getName(), 5000));
            }
        } catch (FIPAException ex) {
            Logger.getLogger(Ambulancia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class EnviarPacientes extends TickerBehaviour {
        
        AID hospital;
        
        public EnviarPacientes(Agent ambulancia, AID hospital, long period) {
            super(ambulancia, period);
            this.hospital = hospital;
        }

        @Override
        protected void onTick() {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setSender(this.getAgent().getAID());
            message.setConversationId("hospital-ambulancia-conversacion");
            message.addReceiver(hospital);
            
            int gravedad = (int)((Math.random() * 9) + 1);
            
            message.setContent(String.valueOf(gravedad));
            
            this.getAgent().send(message);
            
            //Se reinicia estableciendo un nuevo periodo.
            this.reset((int)(Math.random() * 5000) + 1);
        }
        
    }
}
