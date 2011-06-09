/*
 * Mouse Mouse - coded by Emmett Butler, Spring 2010
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class InstructionFrame extends JFrame {
	
	private InstructionPanel ins = new InstructionPanel();
	private JButton cont = new JButton("Start the Game!");
	private boolean contClicked = false;
	

	InstructionFrame() {
		setSize(810, 455);
		setResizable(false);
		setTitle("Mouse Mouse - How to Play");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add opening panel
		ins.add(cont, BorderLayout.SOUTH);
		add(ins);
		
		// When button is clicked, set contClicked true
		cont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MouseMouse mm = new MouseMouse();
				mm.setVisible(true);
			}
		});
	}
}

class InstructionPanel extends JPanel {

        private URL path;
        private ImageIcon imageIcon;
        private Image howto;
	
	InstructionPanel() {
                path = getClass().getResource("images/howto.png");
                if(path != null){
                    imageIcon = new ImageIcon(path);
                    howto = imageIcon.getImage();
                }
		setLayout(new BorderLayout());
	}
	
	protected void paintComponent(Graphics g) {
		g.drawImage(howto, 0, 0, this);
	}
}

class TitleFrame extends JFrame {
	
	private TitlePanel tp = new TitlePanel();
	private JButton cont = new JButton("Start");
	private boolean contClicked = false;
	

	TitleFrame() {
		setSize(797, 550);
		setResizable(false);
		setTitle("Mouse Mouse");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add opening panel
		tp.add(cont, BorderLayout.SOUTH);
		add(tp);
		
		// When button is clicked, set contClicked true
		cont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				InstructionFrame inst = new InstructionFrame();
				inst.setVisible(true);
			}
		});
	}
}

class TitlePanel extends JPanel {

        private ImageIcon imageIcon;
        private Image image;
        private URL path;
	
	TitlePanel() {
                path = getClass().getResource("images/title.png");
                if(path != null){
                    imageIcon = new ImageIcon(path);
                    image = imageIcon.getImage();
                }
		setLayout(new BorderLayout());
	}
	
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this);
	}
}

class OptionFrame extends JFrame {
	
	JButton scoreButton = new JButton("Scores");	
	JButton newButton = new JButton("New Game");
	JButton quitButton = new JButton("Quit");
	
	OptionFrame(){
		setLayout(new FlowLayout());
		setSize(300, 60);
		setResizable(false);
		setTitle("Mouse Mouse - Options");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MouseMouse mm = new MouseMouse();
			}
		});
		
		scoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ScoreFrame sf = new ScoreFrame();
			}
		});
			
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		add(newButton);
		add(scoreButton);
		add(quitButton);
		setVisible(true);	
	}		
}