/*
 * Mouse Mouse - coded by Emmett Butler, Spring 2010
 */

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ProgressBar extends JPanel {
	
	BarFill fill;
	int increment;
	int goal;
	
	ProgressBar(int progress, int goal){
		fill = new BarFill();
		this.goal = goal;
		if(progress < goal)
			fill.setFill((progress * 1.0) / (goal * 1.0)); //fill bar with fractional portion of goal
		else
			fill.setFill(1.0);
	}
	
	public void draw(Graphics g){
		g.setColor(Color.orange);
		g.drawRect(10, 24, 200, 25);	
		fill.draw(g);
	}
	
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.orange);
		g.drawRect(x, y, 64, 7);
		fill.draw(g, x, y);
	}	
}

class BarFill extends JPanel {
	
	private double fillPct;

	BarFill(){
	}
	
	public void draw(Graphics g){
		g.setColor(Color.yellow);
		g.fillRect(10, 24, (int)(fillPct * 200), 25);	
	}
	
	public void draw(Graphics g, int x, int y){
		g.setColor(Color.yellow);
		g.fillRect(x, y + 1, (int)(fillPct * 64), 6);
	}
	
	public void setFill(double fillPct){
		this.fillPct  = fillPct;
	}
}

class LifeIndicator extends JPanel {
	
	private int position = 230;
	
	Heart[] lives;
	
	LifeIndicator(int number){
		lives = new Heart[number];
		for(int i = 0; i < lives.length; i++){
			lives[i] = new Heart(position, 26);
			position += 50;
		}
	}
	
	public void draw(Graphics g){
		for(int i = 0; i < lives.length; i++){
			lives[i].draw(g);	
		}	
	}	
}

class Heart {
	
	private int x;
	private int y;
        private URL path;
        private ImageIcon imageIcon;
	private Image image;
	
	Heart(int x, int y){
                path = getClass().getResource("images/heart.png");
                if(path != null){
                    imageIcon = new ImageIcon(path);
                    image = imageIcon.getImage();
                }
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g){
		g.drawImage(image, x, y, null);	
	}
		
}