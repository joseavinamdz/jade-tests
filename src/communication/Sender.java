/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Rafa
 */
public class Sender extends Agent {
    public void setup() {
        SendMessageBehaviour smb = new SendMessageBehaviour("Hola agent2", "agent2", this);
        
        this.addBehaviour(smb);
    }
    
    class SendMessageBehaviour extends OneShotBehaviour {
        String message;
        String receiver;
        boolean sent;
        
        public SendMessageBehaviour(String message, String receiver, Agent sender) {
            super(sender);
            this.receiver = receiver;
            this.message = message;
        }
        
        @Override
        public void action() {
            System.out.println("Enviado mensaje...");
            ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
            aclMessage.addReceiver(new AID(receiver, AID.ISLOCALNAME));
            aclMessage.setSender(this.getAgent().getAID());
            aclMessage.setContent(message);
            
            this.getAgent().send(aclMessage);
        }
        
    }
}
