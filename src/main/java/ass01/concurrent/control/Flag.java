package ass01.concurrent.control;

public class Flag {
    int value;
    boolean available = false;

    public synchronized void set(int v){
        this.value = v;
        available = true;
        notifyAll();
    }
    public synchronized int get() {
        while (available){
            try {
                wait();
            } catch (InterruptedException ex){}
        }
        return value;
    }
}
