package ass01.concurrent.model;

import ass01.concurrent.model.Latch;

public class LatchImpl implements Latch {

	private int nWorker;

	public LatchImpl(int nWorker) {
		this.nWorker = nWorker;
	}

	@Override
	public synchronized void notifyCompletion() {
		this.nWorker--;
		if(this.nWorker == 0){
			notifyAll();
		}
	}

	@Override
	public synchronized void waitCompletion() throws InterruptedException {
		while (this.nWorker > 0){
			wait();
		}
	}
	
	
}
