package edu.gcu.cst239.askarov.chingiz.milestone_app;

import edu.gcu.cst239.askarov.chingiz.milestone_app.actions.StartingActions;
import edu.gcu.cst239.askarov.chingiz.milestone_app.services.StoreFront;

/**
 * The beginning for the store application
 * Creates the objects and workflow needs and begins.
 */
public class MilestoneApplication {
/**
 * Starts the application.
 * 
 * @param args command line arguments, they are not used
 */
    public static void main(String[] args) {
        System.out.println("Milestone application starting...");
        
        StoreFront store = new StoreFront();
        StartingActions actions = new StartingActions(store);
        actions.start();
    }
}
