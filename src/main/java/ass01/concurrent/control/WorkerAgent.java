package ass01.concurrent.control;

import ass01.concurrent.model.Body;

import java.util.ArrayList;

public class WorkerAgent extends Thread{
    private final ArrayList<Body> bodies; /* bodies in the field */
    private final TaskCompletionLatch synchLatch;

    public WorkerAgent(ArrayList<Body> bodies, int indexFrom, int indexTo, TaskCompletionLatch synchLatch) {
        this.bodies = bodies;
        this.synchLatch = synchLatch;
    }

    public void run(){


    }
}
