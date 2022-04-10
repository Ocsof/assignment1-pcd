package ass01.concurrent.model;

public interface Latch {
    void notifyCompletion();

    void waitCompletion() throws InterruptedException;
}


