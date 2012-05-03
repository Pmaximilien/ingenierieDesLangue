package x.ministart.sys;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * The main frame class.
 * 
 * @author Douglas DUTEIL
 *
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	final static int WIDTH = 600;
	final static int HEIGHT = 400;
	
	public MainFrame(String title) {
		super (title);

		// Get ContentPane
		SwingUI ui = new SwingUI();
		this.getContentPane().add(ui);
		this.setJMenuBar(ui.getJMenuBar());
	}

	public static void main (String args[]) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	    		MainFrame frame = new MainFrame("miniSTARTv0");
	    		frame.addWindowListener(new WindowAdapter() {
	    			public void windowClosing(WindowEvent e) {System.exit(0);}
	    		});
	    		frame.setSize(WIDTH, HEIGHT);
	    		frame.setVisible(true);
	        }
	      });
	}
}
