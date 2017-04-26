/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directoryfacilitator;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author Rafa
 */
public class IceCreamRequesterBehaviour extends OneShotBehaviour {

    private AID seller;
    
    public IceCreamRequesterBehaviour(Agent requester, AID seller) {
        super(requester);
        this.seller = seller;
    }
    
    @Override
    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setContent("ICE CREAM REQUEST");
        message.addReceiver(seller);
        message.setSender(this.getAgent().getAID());
        
        this.getAgent().send(message);
        
        //Las plantillas ayudan a obtener mensajes con determinadas características.
        //En este caso, el vendedor emitirá un mensaje cuya performatica es INFORM
        //cuando nos quiera vender el helado.
        MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        
        ACLMessage response = this.getAgent().blockingReceive(template);
        
        System.out.println(this.getAgent().getAID().getLocalName() + " got an ice cream:" + response.getContent());
    }
    
    
    
}
