package x.ministart.sys;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;

import x.ministart.utils.Chrono;

/**
 * Opens file on background 
 * TODO Optimize it ! http://java.sun.com/developer/technicalArticles/Programming/PerfTuning/ 
 * 
 * @author Douglas DUTEIL
 *
 */
public class OpenFileWorker {

	private SwingUI ui;
	private String filepath;
	
	public OpenFileWorker(final SwingUI ui, final String filepath) {
		this.ui = ui;
		this.filepath = filepath;

		SwingWorker<Document, String> swingWorker = new DoOpenFile();

		SwingWorkerExecutor.instance().execute(swingWorker);

		swingWorker.addPropertyChangeListener( new PropertyChangeListener() {
			@Override
			public  void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					if (ui.progressBar != null){
						ui.progressBar.setValue((Integer)evt.getNewValue());
					}
				}
			}

		});	
	}
	
	
	private class DoOpenFile extends SwingWorker<Document, String> {

		private Chrono chrono = new Chrono();
		
		@Override
		protected Document doInBackground() throws Exception {
			if ( (filepath != null) && filepath.isEmpty())
				throw new FileNotFoundException();

			File f = new File(filepath);
			if (!f.exists()) {
		      throw new FileNotFoundException("File does not exist: " + filepath);
		    }
		    if (!f.isFile()) {
		      throw new IllegalArgumentException("Is not a file : " + filepath);
		    }
		    if (!f.canRead()) {
		      throw new IllegalArgumentException("File cannot be read: " + filepath);
		    }
		    long fileLenght = f.length();
			
			
			Document doc = new DefaultStyledDocument();
			ui.main_txtarea.setDocument(new DefaultStyledDocument());//Clean up !
			
			chrono.start();
			
			ui.statusInfo.setText (filepath + "- file openning");

			InputStreamReader isr = new InputStreamReader(new FileInputStream(filepath), "UTF-8");
			int 
				bufferSize = 2048,
				buffer_count = 0;
			char[] buf= new char[bufferSize];
			while (isr.read(buf) != -1){
				setProgress( (int) ((( bufferSize * buffer_count )) * 100 / fileLenght) + 2 );
				String tmp_str = new String(buf);
				//normalize EOL character
				tmp_str = tmp_str.replaceAll("\\r\\n", "\n");
				tmp_str = tmp_str.replaceAll("\\r", "\n");
				
				doc.insertString(doc.getLength(), tmp_str , null);
				
				buffer_count ++;
			}
			isr.close();

			ui.statusInfo.setText (filepath + " - sending to text area");
			
			return doc;
		}

		@Override
		protected void done(){
			Document new_doc = null;
			try {
				new_doc = get();
				this.chrono.stop();
				ui.main_txtarea.setDocument(new_doc);
				ui.statusInfo.setText(filepath + " loaded. [" + this.chrono.displayInterval() + "]");
			}catch(Exception e){
				JOptionPane.showMessageDialog(ui, 
						"Error :\n" + e.getLocalizedMessage(),
						"Parse", 
						JOptionPane.ERROR_MESSAGE);
				ui.console_txtarea.append(filepath + " Error : " + e.getLocalizedMessage() + "\n");
				ui.statusInfo.setText(filepath + " error.");
			}
		}
	
	}

}
