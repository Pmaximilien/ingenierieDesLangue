package x.ministart.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker;

import x.ministart.sys.SwingUI;
import x.ministart.sys.SwingWorkerExecutor;
import x.ministart.utils.Chrono;

/**
 * A class to test the background worker thread.<br>
 * It put the thread to sleep 10 x  0.1s 
 * 
 * @author Douglas DUTEIL
 *
 */
public class ThreadTest {

	private Chrono chrono = new Chrono();

	public ThreadTest(final SwingUI ui) {

		SwingWorker<String, Void> swingWorker = new SwingWorker<String, Void>() {
			// This method executes on the UI thread
			@Override
			protected void done() {
				String result = null;
				try {
					result = get();
					ui.console_txtarea.append("Test Successed with " + result + "\n");
				} catch (Exception e) {
					ui.console_txtarea.append("Test Interrupted : " + e.getLocalizedMessage() + "\n");
					ui.progressBar.setValue(0);
				}
				chrono.stop();
				if (result != null)
					ui.statusInfo.setText(result+ " [takes " + chrono.displayInterval() + "]");
				else
					ui.statusInfo.setText("/!\\ [takes " + chrono.displayInterval() + "]");
			}
			// This method executes on the background worker thread
			@Override
			protected String doInBackground() throws Exception {
				chrono.start();
				String result = Thread.currentThread().getName();
				for (int i = 0; i < 10; i++) {
					Thread.sleep(100);
					setProgress(100 * (i+1) / 10);
				}
				return result;
			}
		};

		// Tells the SwingWorkerExecutor to execute the swingWorker
		SwingWorkerExecutor.instance().execute(swingWorker);

		// Listens to the setProgress method to update the progress bar
		swingWorker.addPropertyChangeListener( new PropertyChangeListener() {
			@Override
			public  void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					if (ui.progressBar != null){
						ui.statusInfo.setText ("Test running...");
						ui.progressBar.setValue((Integer)evt.getNewValue());
					}
				}
			}

		});
	}
}
