package x.ministart.sys;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.SwingWorker;

/**
 * SwingWorkerExecutor singleton class.
 * Use it to execute functions on background.
 * 
 * (Multithread Safe ?)
 * 
 * @author Douglas DUTEIL
 *
 */
public class SwingWorkerExecutor {
	private static final int MAX_WORKER_THREAD = 2;
	private static SwingWorkerExecutor instance=  new SwingWorkerExecutor();
	// Thread pool for worker thread execution
	private ExecutorService workerThreadPool = Executors.newFixedThreadPool(MAX_WORKER_THREAD);
	private Future<?> exeFuture = null;

	/**
	 * Private constructor required for the singleton pattern.
	 */
	private SwingWorkerExecutor() {}

	/**
	 * Returns the singleton instance.
	 * @return SwingWorkerExecutor - Singleton.
	 */
	public static SwingWorkerExecutor instance() {
		return instance;
	}

	/**
	 * Adds the SwingWorker to the thread pool for execution.
	 * @param worker - The SwingWorker thread to execute.
	 */
	public void execute(SwingWorker<?, ?> worker) {
		if (isReady())
			exeFuture  = workerThreadPool.submit(worker);
	}
	/**
	 * Cancel the current background execution.
	 * (the "get()" method will throw a InterruptedException)
	 */
	public void cancel(){
		exeFuture.cancel(true);
	}

	/**
	 * Returns true if the executor is ready.
	 * @return true if the executor is ready.
	 */
	public boolean isReady(){
		if (exeFuture == null) return true;
		return exeFuture.isDone();
	}
}
