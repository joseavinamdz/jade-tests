/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

import jade.core.behaviours.Behaviour;

/**
 *
 * @author Rafa
 */
public class GreetingBehaviour extends Behaviour{
    private int count;
    private String message;
    
    public GreetingBehaviour(String message) {
        count = 0;
        this.message = message;
    }
    
    @Override
    public void action() {
        System.out.println(message + " " + count);
        count++;
    }

    @Override
    public boolean done() {
        return count == 5;
    }
}
