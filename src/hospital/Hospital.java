/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafa
 */
public class Hospital extends Agent {
    
    PriorityQueue<Integer> salaDeEspera;
    public static final int MAX_PACIENTES = 100;
    
    @Override
    public void setup() {
        salaDeEspera = new PriorityQueue(10, new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.intValue() - o1.intValue();
            }
        });
        
        //Registrar el servicio para ofrecerlo a ambulancias.
        DFAgentDescription description = new DFAgentDescription();
        description.setName(this.getAID());
        
        ServiceDescription sd = new ServiceDescription();
        sd.setType("hospital-service");
        sd.setName("hospital-service");
        description.addServices(sd);
        
        try {
            DFService.register(this, description);
        } catch (FIPAException ex) {
            Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.addBehaviour(new RecibirPacientes(this));
        this.addBehaviour(new AtenderDoctores(this));
        
    }
    
    private void addPaciente(int gravedad) {
        salaDeEspera.add(gravedad);
        System.out.printf("Paciente recibido (%d / %d)\n", salaDeEspera.size(), MAX_PACIENTES);
        
    }
    
    private int atenderPaciente() {
        return salaDeEspera.poll();
    }
    
    public int getTotalPacientes() {
        return salaDeEspera.size();
    }
    
    public boolean lleno() {
        return getTotalPacientes() >= MAX_PACIENTES;
    }
    
    public boolean vacio() {
        return getTotalPacientes() == 0;
    }
    
    /*
    Esta clase se encarga de que el hospital esté pendiente de las ambulancias
    que traen pacientes. Si se ha superado el límite de pacientes del hospital,
    el paciente es rechazado.
    */
    class RecibirPacientes extends Behaviour {
        
        private Hospital hospital;
        
        public RecibirPacientes(Hospital hospital) {
            super(hospital);
            this.hospital = hospital;
        }
        
        @Override
        public void action() {
            
            //Sólo interesan las peticiones de la ambulancia.
            MessageTemplate tpl = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.MatchConversationId("hospital-ambulancia-conversacion"));
            
            ACLMessage request = hospital.receive(tpl);
            if (request != null) {
                if (hospital.lleno()) {
                    //Se manda un mensaje a la ambulancia rechazando al paciente.
                    ACLMessage refuse = new ACLMessage(ACLMessage.REFUSE);
                    refuse.setConversationId("hospital-ambulancia-conversacion");
                    refuse.addReceiver(request.getSender());
                    refuse.setContent("Paciente rechazado. Hospital lleno.");
                    hospital.send(refuse);
                }
                else {
                    hospital.addPaciente(Integer.parseInt(request.getContent()));
                }
            }
            else {
                this.block();
            }
            
        }

        @Override
        public boolean done() {
            return false;
        }
        
    }
    
    /*
    Esta clase se encarga de que el hospital atienda las peticiones de los doctores.
    Cuando un doctor no está tratando a un paciente envía un mensaje solicitando uno.
    Si hay algún paciente que atender, el hospital se lo asigna al doctor y lo elimina
    de la sala de espera.
    */
    
    class AtenderDoctores extends Behaviour {
        private Hospital hospital;
        
        public AtenderDoctores(Hospital hospital) {
            super(hospital);
            this.hospital = hospital;
        }
        
        @Override
        public void action() {
            //Sólo interesan las peticiones de los doctores.
            MessageTemplate tpl = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.MatchConversationId("hospital-doctor-conversacion"));
            
            ACLMessage request = hospital.receive(tpl);
            if (request != null) {
                ACLMessage response = new ACLMessage(ACLMessage.UNKNOWN);
                response.addReceiver(request.getSender());
                response.setSender(this.hospital.getAID());
                response.setConversationId("hospital-doctor-conversacion");
                
                if (hospital.vacio()) {
                    response.setPerformative(ACLMessage.REFUSE);
                    response.setContent("No hay pacientes en espera.");
                }
                else {
                    int gravedad = hospital.atenderPaciente();
                    response.setPerformative(ACLMessage.INFORM);
                    response.setContent(String.valueOf(gravedad));
                }
                
                hospital.send(response);
            }
            else {
                this.block();
            }
        }

        @Override
        public boolean done() {
            return false;
        }
        
    }
    
    
}
