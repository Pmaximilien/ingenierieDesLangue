package x.ministart.sys;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

//import x.ministart.parse.ParseAllLettersWorker;
//import x.ministart.parse.ParseAllPronomWorker;
//import x.ministart.parse.ParseParagWorker;
import x.ministart.parse.*;
import x.ministart.test.ThreadTest;

/**
 * The class which contains all Graphical User Interface (GUI) elements. 
 * 
 * @author Douglas DUTEIL
 *
 */
public class SwingUI extends JPanel {
	private static final long serialVersionUID = 1L;

	public JMenuBar menuBar;
	public JLabel statusInfo;
	public JProgressBar progressBar;
	public JTextArea console_txtarea;
	public JTextPane main_txtarea;

	private JPanel console_panel;

	public SwingUI() {
		this.setLayout(new BorderLayout());
		//=============================Center Panel
		main_txtarea = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(main_txtarea);
		this.add(scrollPane, BorderLayout.CENTER);

		//=============================Bottom Panel
		JPanel bottom = new JPanel(new BorderLayout());
		bottom.setMinimumSize(new Dimension(100, 100));

		//=================Status Panel
		JPanel statusPanel = new JPanel(new BorderLayout());
		this.progressBar = new JProgressBar(0, 100);
		this.statusInfo = new JLabel("Pret");
		JSeparator sep = new JSeparator();
		statusPanel.add(sep, BorderLayout.NORTH);
		statusPanel.add(this.statusInfo, BorderLayout.WEST);

		//=================BarAndStop Panel
		JPanel barAndStopPanel = new JPanel();
		JMenuItem stop_btn = new JMenuItem(new Icon() {

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				g.setColor(Color.RED);
				g.fillRect (x, y, getIconWidth(), getIconHeight());

			}

			@Override
			public int getIconWidth() {
				return 10;
			}

			@Override
			public int getIconHeight() {
				return 10;
			}
		});
		stop_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingWorkerExecutor.instance().cancel();
			}
		});


		barAndStopPanel.add(progressBar);
		barAndStopPanel.add(stop_btn);
		statusPanel.add(barAndStopPanel, BorderLayout.EAST);
		bottom.add (statusPanel, BorderLayout.SOUTH);

		//=================Console Panel
		this.console_panel = new JPanel(new BorderLayout());
		this.console_panel.setMinimumSize(new Dimension(100, 100));
		this.console_panel.add(new JLabel("Console :"), BorderLayout.NORTH);
		this.console_txtarea = new JTextArea(5,0);
		scrollPane = new JScrollPane (this.console_txtarea);
		this.console_panel.add(scrollPane, BorderLayout.CENTER);
		bottom.add (this.console_panel, BorderLayout.CENTER);
		this.console_panel.setVisible(false);


		this.add (bottom, BorderLayout.SOUTH);

	}

	/**
	 * Returns the menu bar.
	 * @return The menu bar.
	 */
	public JMenuBar getJMenuBar() {

		if (this.menuBar == null){
			this.menuBar = new JMenuBar();

			JMenu menu, submenu;
			JMenuItem item;
			final SwingUI ui = this;

			//=============================File Menu
			this.menuBar.add (menu = new JMenu ("File"));
			
			menu.add (submenu = new JMenu ("Open Example"));
				submenu.add (item = new JMenuItem ("LesMiserables_pg135.txt"));
				item.addActionListener (new ActionListener() {
					public void actionPerformed (ActionEvent e) {
						new OpenFileWorker(ui, "data/LesMiserables_pg135.txt");
					}
				});
				submenu.add (item = new JMenuItem ("LesTroisMousquetaires_pg13951.txt"));
				item.addActionListener (new ActionListener() {
					public void actionPerformed (ActionEvent e) {
						new OpenFileWorker(ui, "data/LesTroisMousquetaires_pg13951.txt");
					}
				});
				submenu.add (item = new JMenuItem ("Monte-Cristo_Tome1_pg17989.txt"));
				item.addActionListener (new ActionListener() {
					public void actionPerformed (ActionEvent e) {
						new OpenFileWorker(ui, "data/Monte-Cristo_Tome1_pg17989.txt");
					}
				});
				submenu.add (item = new JMenuItem ("Monte-Cristo_Tome2_pg17990.txt"));
				item.addActionListener (new ActionListener() {
					public void actionPerformed (ActionEvent e) {
						new OpenFileWorker(ui, "data/Monte-Cristo_Tome2_pg17990.txt");
					}
				});
				submenu.add (item = new JMenuItem ("Monte-Cristo_Tome3_pg17991.txt"));
				item.addActionListener (new ActionListener() {
					public void actionPerformed (ActionEvent e) {
						new OpenFileWorker(ui, "data/Monte-Cristo_Tome3_pg17991.txt");
					}
				});
				submenu.add (item = new JMenuItem ("Monte-Cristo_Tome4_pg17992.txt"));
				item.addActionListener (new ActionListener() {
					public void actionPerformed (ActionEvent e) {
						new OpenFileWorker(ui, "data/Monte-Cristo_Tome4_pg17992.txt");
					}
				});
			menu.addSeparator();
			menu.add (item = new JMenuItem ("Close"));
			item.addActionListener (new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					System.exit (0);
				}
			});

			//=============================Window Menu
			this.menuBar.add (menu = new JMenu ("Window"));
			menu.add (item = new JCheckBoxMenuItem("Show Console"));
			item.addActionListener (new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					JCheckBoxMenuItem aButton = (JCheckBoxMenuItem) e.getSource();
					if (aButton.getState()) {
						ui.console_panel.setVisible(true);
					} else {
						ui.console_panel.setVisible(false);
					}
				}
			});
			menu.add (item = new JMenuItem("Clear Console"));
			item.addActionListener (new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					ui.console_txtarea.setText(new String());
				}
			});

			//=============================DO Menu
			this.menuBar.add (menu =  new JMenu("DO"));

			menu.add (item = new JMenuItem ("Run test thread"));
			item.addActionListener (new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					new ThreadTest(ui);
				}

			});
			menu.add (item = new JMenuItem ("Parse ALL"));
			item.addActionListener (new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new ParseAllLettersWorker(ui);
				}

			});

			menu.add (item = new JMenuItem ("Parse Pronoms"));
			item.addActionListener (new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new ParseAllPronomWorker(ui);
				}

			});
			menu.add (item = new JMenuItem ("Parse Parag"));
			item.addActionListener (new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new ParseParagWorker(ui);
				}

			});

			
			/**
			 * ADD NEW PARSER HERE !
			 */
		}
		return this.menuBar;
	}

}
