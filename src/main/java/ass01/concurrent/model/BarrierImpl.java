package ass01.concurrent.model;

import ass01.concurrent.model.Barrier;

public class BarrierImpl implements Barrier {

    private final int nPartecipants;
    private int nHits;

    public BarrierImpl(int nPartecipants) {
        this.nPartecipants = nPartecipants;
        this.nHits = 0;
    }

    @Override
    public synchronized void hitAndWaitAll() throws InterruptedException {
        nHits++;
        if(nHits == nPartecipants) {
            notifyAll();
        } else {
            while (nHits < nPartecipants) {
                wait();
            }
        }
    }
}
