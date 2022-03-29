package ass01.concurrent.control;

public interface Latch {
    void notifyCompletion();

    void waitCompletion() throws InterruptedException;
}


