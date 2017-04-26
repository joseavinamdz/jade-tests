/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author Rafa
 */
public class HelloWorldAgent extends Agent {

    public void setup() {
        System.out.println("Inicializango agente...");
        
        GreetingBehaviour gb = new GreetingBehaviour("Hi");
        GreetingBehaviour gb2 = new GreetingBehaviour("Hola");
        
        this.addBehaviour(gb);
        this.addBehaviour(gb2);
        
    }
    
}