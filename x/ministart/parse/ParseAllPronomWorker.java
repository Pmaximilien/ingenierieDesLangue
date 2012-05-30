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
//private String Get_Mot(Document doc, int ind){


public class ParseAllPronomWorker{

    private SwingUI ui;




   protected int skip_sujet(Document doc, int ind, int fin) throws Exception {

    	String verbe = "<gv>";
    	String fin_verbe = "</gv>";
	String mot;
	boolean found = false;
	int tmp;
	int i;

	for(i = ind; i < fin; i++){
		mot = Get_Mot(doc, ind);	


		if (isPronom(mot)){
			i += mot.length();

			mot = Get_Mot(doc, i);
			// Je | me fait des patte
			if (isSecondPronom(mot))
				i += mot.length();
				// Je me | fait des patte
				doc.insertString(i, verbe, null);

				i += verbe.length();
				// Je me <gv> | fait des patte

				mot = Get_Mot(doc, i);
				i += mot.length();
				// Je me <gv> fait | des pattes

				found =  true;
		}

		if (isComp(mot)){
			// Le garde bu | de l'eau
			 if (found){
				// Le garde bu de l'eau | de la rivière
			 	doc.insertString(i, fin_verbe, null);
				return verbe.length() + fin_verbe.length();
			}
			else {
				// Le garde bu | de l'eau
				tmp = i - 1;			
				while(doc.getText(tmp, 1)  != " " && tmp > ind){
					tmp--;


				if (tmp == ind){
					return 0;



				}else{
					doc.insertString(tmp, verbe, null);
					i += verbe.length();
					found = true;
				}
			}
		}
		}

	}
	if (found){
		doc.insertString(fin, fin_verbe, null);
		return verbe.length() + fin_verbe.length();
	}else{
		return 0;
	}
					
}

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

		String verbe = "<gv>";
		String f_verbe = "</gv>";

		int sentence_size;
		int mine = 0;
		int gv;


		for (int txt_ind = 0; txt_ind < docLen; txt_ind++) {
			//doc.insertString(txt_ind, debut, null);
			//txt_ind += 8;
			sentence_size = 0;	
			while(! isDelimiteur( doc.getText(txt_ind + sentence_size, 1))){
				sentence_size++;
			}
    			gv = skip_sujet(doc, txt_ind, txt_ind + sentence_size);

			txt_ind += sentence_size + gv;
			//doc.insertString(txt_ind, verbe, null);
			//txt_ind += 9;
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

	try {
	mot = doc.getText(ind++, 1);
	 } catch (BadLocationException e) {
		e.printStackTrace();
	throw new RuntimeException(e);
	   }
	   

	if (mot.equals("\n"))
		ret.append(" ");
	else
		ret.append(mot);

	while(true){
	    try 
	    {
		mot = doc.getText(ind++, 1);
		if (mot.equals(" ")) // fin du mot
		    break;
		ret.append(mot);

	    } catch (BadLocationException e) {
		e.printStackTrace();
		throw new RuntimeException(e);
	    }
	}

	return ret.toString();
    }

    private boolean isPronom(String mot){
	String [] pronoms = {"je", "tu", "il", "elle","ont", "nous", "vous", "ils", "elles"};
	int i;
	mot = mot.trim();

	for(i = 0; i < pronoms.length; i++){
	    if(mot.equalsIgnoreCase(pronoms[i]))
		return true;
	}
	return false;
    }


     private boolean isSecondPronom(String mot){
	String [] pronoms = {"me", "te", "le", "la", "nous", "vous", "les", "elles"};

	int i;
	 
	mot = mot.trim();

	for(i = 0; i < pronoms.length; i++){
	    if(mot.equalsIgnoreCase(pronoms[i]))
		return true;
	}
	return false;
    }
    	

    private boolean isDelimiteur(String mot){
	String [] stop = {".", "!", "?", ","};
	int i;
	mot = mot.trim();

	for(i = 0; i < stop.length; i++){
	    if(mot.equalsIgnoreCase(stop[i]))
		return true;
	}
	return false;
    }

    private boolean isComp(String mot){
    	int i;
    	String [] stop = {"que", "qui", "à", "au", "aux", "en"};

	for(i = 0; i < stop.length; i++){
	    if(mot.equalsIgnoreCase(stop[i]))
		return true;
	}
	return false;
    }
    	
}
