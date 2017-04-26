/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Rafa
 */
public class Receiver extends Agent {
    @Override
    public void setup() {
        this.addBehaviour(new ReceiveMessageBehaviour(this));
    }
    
    class ReceiveMessageBehaviour extends Behaviour {
        boolean received = false;
        
        public ReceiveMessageBehaviour(Agent receiver) {
            super(receiver);
        }
        
        @Override
        public void action() {
            ACLMessage message = this.getAgent().receive();
            
            if (message != null) {
                System.out.println("Mensaje recibido de " + 
                        message.getSender().getLocalName() + ":" + message.getContent());
                received = true;
            }
            else {
                this.block();
            }
        }

        @Override
        public boolean done() {
            return received;
        }
        
    }
}
