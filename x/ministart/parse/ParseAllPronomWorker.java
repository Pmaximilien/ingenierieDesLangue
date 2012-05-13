package x.ministart.parse; 
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DefaultStyledDocument;

import x.ministart.sys.SwingUI;
import x.ministart.sys.SwingWorkerExecutor;
import x.ministart.utils.Chrono;

/**
 * Parse all the letters.<br>
 * Add a "_" between them.
 * 
 * TODO Performance Boost (cf. http://docs.oracle.com/javase/6/docs/api/javax/swing/text/Document.html)	
 * @author Douglas DUTEIL
 *
 */
public class ParseAllPronomWorker{

    private SwingUI ui;

    public ParseAllPronomWorker(final SwingUI ui) {
	this.ui = ui;

	SwingWorker<Integer, Object> swingWorker = new ParsePronom();

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
    public class ParsePronom extends SwingWorker<Integer, Object> {

	private Chrono chrono = new Chrono();

	@Override
	    protected Integer doInBackground() throws Exception {

		Document doc = ui.main_txtarea.getDocument();
		ui.main_txtarea.setDocument(new DefaultStyledDocument());

		ui.statusInfo.setText ("Parse Pronoms - running");
		this.chrono.start();

		int 
		    docLen = doc.getLength(),
			   insertion_count = 0,
			   parse_ind = 0;

		String debut = "<phrase>";
		String fin = "</phrase>";

		int sentence_size;
		int mine = 0;
		for (int txt_ind = 0; txt_ind < docLen; txt_ind++) {
			doc.insertString(txt_ind, debut, null);
			txt_ind += 8;
			sentence_size = 0;	
			while(! isDelimiteur( doc.getText(txt_ind + sentence_size, 1))){
				sentence_size++;
			}
			txt_ind += sentence_size;
			doc.insertString(txt_ind, fin, null);
			txt_ind += 9;
		}

			

			// Parcours jusqu'à la fin de la phrase


		//parse_ind += sep.length() + 1;
		insertion_count++;


		ui.statusInfo.setText ("Parse ALL ");
		System.out.println("Pronoms trouve : "+mine);
		ui.main_txtarea.setDocument(doc);

		return insertion_count;
		//			  textComponent.setCaretPosition(doc.getLength() - 1);
	    }



    @Override
	protected void done(){
	    int strParsed = 0;
	    try {
		strParsed = get();
		this.chrono.stop();
		ui.statusInfo.setText("Document parsed..." + strParsed+ " insertions [" + this.chrono.displayInterval() + "]");
	    }catch(Exception e){

		JOptionPane.showMessageDialog(ui, 
			"Error :\n" + e.getLocalizedMessage(),
			"Parse", 
			JOptionPane.ERROR_MESSAGE);
		ui.statusInfo.setText("Parse error on \"ParseAllLettersWorker\".");
	    }
	}
	}

    private String Get_Mot(Document doc, int ind){
	String mot;
	StringBuilder ret = new StringBuilder();

	int taille = 0;

	int max = doc.getLength();
	while(true){
	    try 
	    {
		mot = doc.getText(ind++, 1);
		if (mot.equals(" ") || mot.equals("'") || ind >= max) 
		    break;
		ret.append(mot);

	    } catch (BadLocationException e) {
		e.printStackTrace();
		throw new RuntimeException(e);
	    }
	}

	return ret.toString();
    }

    private boolean isDelimiteur(String mot){
	String [] pronoms = {".", "!", "?"};
	int i;

	for(i = 0; i < 3; i++){
	    if(mot.equalsIgnoreCase(pronoms[i]))
		return true;
	}
	return false;
    }
    	
}
