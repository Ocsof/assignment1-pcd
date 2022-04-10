package ass01.concurrent.model;

public interface Barrier {

	void hitAndWaitAll() throws InterruptedException;

}
