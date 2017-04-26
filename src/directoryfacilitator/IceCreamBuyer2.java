/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directoryfacilitator;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafa
 */
public class IceCreamBuyer2 extends Agent {
    @Override
    public void setup() {
        AskForServicesBehaviour behaviour = new AskForServicesBehaviour(this, 1000);
        
        this.addBehaviour(behaviour);
    }
    
    /*
    TickerBehaviour es un tipo de comportamiento que ejecuta una acción (onTick())
    periódicamente.
    */
    class AskForServicesBehaviour extends TickerBehaviour {
        
        DFAgentDescription template;
        
        public AskForServicesBehaviour(Agent a, long period) {
            super(a, period);
            
            template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("ice-cream-selling");
            template.addServices(sd);
        }

        @Override
        protected void onTick() {
            try {
                DFAgentDescription[] matches = DFService.search(this.getAgent(), template);
                
                if (matches.length > 0) {
                    System.out.println("Sellers found by " + this.getAgent().getLocalName() + ":");
                    for (DFAgentDescription match : matches) {
                        System.out.println("\t" + match.getName().getLocalName());
                    }
                    
                    this.stop();
                    
                    this.getAgent().addBehaviour(
                            new IceCreamRequesterBehaviour(this.getAgent(), matches[0].getName()));
                }
                
                
                
            } catch (FIPAException ex) {
                Logger.getLogger(IceCreamBuyer2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
