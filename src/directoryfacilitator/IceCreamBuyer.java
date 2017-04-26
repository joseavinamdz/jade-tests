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
public class IceCreamBuyer extends Agent {
    @Override
    public void setup() {
        
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("ice-cream-selling");
        template.addServices(sd);
        
        try {
            SearchConstraints sc = new SearchConstraints();
            
            /*
            Esto no funcionará siempre desde setup:
            DFAgentDescription[] matches = DFService.search(this, template);
            */
            
            DFAgentDescription[] matches = 
                    DFService.searchUntilFound(this, new AID("df", AID.ISLOCALNAME), template, sc, 1000);
            
            System.out.println("Sellers found by " + this.getLocalName() + ":");
            for (DFAgentDescription match : matches) {
                System.out.println("\t" + match.getName().getLocalName());
            }
            
            if (matches.length > 0) {
                this.addBehaviour(new IceCreamRequesterBehaviour(this, matches[0].getName()));
            }
        } catch (Exception ex) {
            Logger.getLogger(IceCreamBuyer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
