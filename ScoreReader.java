/*
 * Mouse Mouse - coded by Emmett Butler, Spring 2010
 */

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class ScoreReader {
	
	String[][] list = new String[11][2];
        File file;
        URL url;
        URI uri;
	
	public ScoreReader(String Score, String username){
		try{
			int score = Integer.parseInt(Score);
			int i = 0;
			
                        url = getClass().getResource("data/scores.txt");
                        try{
                            uri = url.toURI();
                            file = new File(uri);
                        } catch (URISyntaxException e) {}
			Scanner input = new Scanner(file);
			
			while(input.hasNext() && i < 10){
				
				list[i][0] = input.next() + " ";
				
				if(!input.hasNextInt())
					list[i][0] += input.next() + " ";
				
				list[i][1] = input.next();
				
				if(Integer.parseInt(list[i][1]) < score){
					list[i + 1][0] = list[i][0];
					list[i + 1][1] = list[i][1];
					
					list[i][0] = username + " ";
					list[i][1] = Score;
					
					i++;
					
					score = 0;
				}
				
				i++;
			}
		}
		catch(Exception e){
		}
	}
	
	public String[][] getScores(){
		return list;	
	}
}

class ScoreWriter {
	
	public ScoreWriter(String Username, String Score) {
		try{
			ScoreReader reader = new ScoreReader(Score, Username);
			String[][] list = reader.getScores();
			File scores = new File("data/scores.txt");
			
			PrintWriter output = new PrintWriter(scores);
			
			for(int i = 0; i < 10; i++){
				output.print(list[i][0]);
				output.println(list[i][1]);	
			}
			
			output.close();
		}
		catch(Exception e){
		}
	}	
}

class ScoreFrame extends JFrame { 
	
	ScorePanel panel = new ScorePanel();
	
	public	ScoreFrame() {
		add(panel);	
		setTitle("Mouse Mouse - High Scores");
	    setLocationRelativeTo(null); // Center the frame
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setSize(300, 400);
	    setVisible(true);
	}
}

class ScorePanel extends JPanel {

        File file;
        URL textPath;
        URI uri;	
	Scanner input;
	JButton newButton = new JButton("Play Again");
	JButton quitButton = new JButton("Quit");
	int i = 0;
        private Image image;
        private ImageIcon imageIcon;
        private URL path;
	
	public ScorePanel() {
            try{
                textPath = getClass().getResource("data/scores.txt");
                try{
                    uri = textPath.toURI();
                    file = new File(uri);
                } catch (URISyntaxException e){}

                path = getClass().getResource("images/wood.png");
                if (path != null){
                    imageIcon = new ImageIcon(path);
                    image = imageIcon.getImage();
                }
                try{
			input = new Scanner(file);
			
			add(newButton);
			add(quitButton);
			
			newButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MouseMouse mm = new MouseMouse();
				}
			});
			
			quitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		catch(Exception e){
		}
            } catch (Exception e){}
	}	
	
	public void paintComponent(Graphics g){
		g.drawImage(image, 0, 0, null);
		g.setColor(Color.yellow);
		g.setFont(new Font("Courie", Font.BOLD, 24));
		g.drawString("High Scores", 20, 60);
		while(input.hasNext()){
			g.setFont(new Font("Courie", Font.BOLD, 15));
			g.drawString(input.nextLine() + "\n", 20, 90 + i);	
			i += 30;
		}	
	}
}