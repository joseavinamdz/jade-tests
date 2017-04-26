/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
-gui seller:directoryfacilitator.IceCreamSeller;buyer1:directoryfacilitator.IceCreamBuyer;buyer2:directoryfacilitator.IceCreamBuyer2;buyer3:directoryfacilitator.IceCreamBuyer3
*/

package directoryfacilitator;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafa
 */
public class IceCreamSeller extends Agent {
    @Override
    public void setup() {
        
        DFAgentDescription dfd = new DFAgentDescription();
        
        dfd.setName(this.getAID());
        
        ServiceDescription sd = new ServiceDescription();
        sd.setType("ice-cream-selling");
        sd.setName("ice-cream-selling");
        
        dfd.addServices(sd);
        
        try {
            DFService.register(this, dfd);
            System.out.println("Service registered");
        } catch (FIPAException ex) {
            Logger.getLogger(IceCreamSeller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.addBehaviour(new IceCreamResponderBehaviour(this));
    }
    
    
    class IceCreamResponderBehaviour extends Behaviour {

        public IceCreamResponderBehaviour(Agent a) {
            super(a);
        }

        @Override
        public void action() {
            
            //Se nos solicita un helado.
            ACLMessage message = this.getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
            
            if (message != null) {
                AID buyer = message.getSender();
                
                //Se le da un helado al cliente.
                ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                response.setContent("price: 2.55€");
                response.addReceiver(buyer);
                response.setSender(this.getAgent().getAID());
                
                this.getAgent().send(response);
            }
            else {
                System.out.println(this.getAgent().getAID().getLocalName() + " waiting for customer.");
                this.block();
            }
        }

        @Override
        public boolean done() {
            return false;
        }
        
    }
}
