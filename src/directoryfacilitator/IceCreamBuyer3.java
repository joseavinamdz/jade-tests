/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directoryfacilitator;

import jade.core.AID;
import jade.core.Agent;
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
public class IceCreamBuyer3 extends Agent {
    @Override
    public void setup() {
        
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("ice-cream-selling");
        template.addServices(sd);
        
        SearchConstraints sc = new SearchConstraints();            
        ACLMessage m = DFService.createSubscriptionMessage(this, new AID("df", AID.ISLOCALNAME), template, sc);
        
        //Se envía un mensaje de suscripción que indica al DF que queremos
        //ser notificados con un mensaje cuando se disponga de los servicios
        //requeridos.
        this.send(m);
        
        //Esperamos hasta recibir esa respuesta.
        ACLMessage msg = this.blockingReceive();
        
        DFAgentDescription[] matches;
        try {
            matches = DFService.decodeNotification(msg.getContent());
            System.out.println("Sellers found by " + this.getLocalName() + ":");
            for (DFAgentDescription match : matches) {
                System.out.println("\t" + match.getName().getLocalName());
            }
            
            if (matches.length > 0) {
                this.addBehaviour(new IceCreamRequesterBehaviour(this, matches[0].getName()));
            }
        } catch (FIPAException ex) {
            Logger.getLogger(IceCreamBuyer3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
