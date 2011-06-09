/*
 * Mouse Mouse - coded by Emmett Butler, Spring 2010
 */

//warning: this code is not really that well documented. sorry about that.

//ALSO: THIS FILE CONTAINS THE MAIN METHOD, SO RUN <$ java MouseMouse> TO PLAY

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.URL;

public class MouseMouse extends JFrame {
	final int WINHT = 700;
	final int WINLN = 810;
	
	Game panel = new Game(WINHT, WINLN);
		
	public MouseMouse(){
            add(panel);
	    setTitle("Mouse Mouse");
	    setLocation(100, 0);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setSize(WINLN, WINHT);
	    setVisible(true);
	}
	
	public static void main(String[] args){
		TitleFrame welcome = new TitleFrame();
		welcome.setVisible(true);
	}
}

class Game extends JPanel {
	int trapCount = 0, chCount = 0; //initialization of counters for lives lost and cheeses caught
	int pixels = 10, startHt = -40;
	int winHt, winLn;
	int chArrSize = 2; //second dimension of trap and cheese arrays
	int trapArrSize = 2;
	int moveAmnt = 7; //amount by which the objects move each frame
	int lives = 7; //initial amount of player lives
	int flyAmnt;
	int levelTime = 20; //seconds per level
	int level = 1; //starting level
	int goal = 20; //cheese goal of first level
	int points = 0; //initial player points
	int lifeFreq = -6000, spreeFreq = -10000, pointFreq = -300; //offscreen starting positions of special cheeses
	
	boolean spree = false;
	boolean go = false;
	
	String startMessage = "", levelMessage = "", lifeMessage = "", spreeMessage = "", goalMessage = "", timeMessage = "";
	String pointMessage = "";

	Cheese[][] chArray = new Cheese[chArrSize][10];
	Cheese[][] trapArray = new Cheese[trapArrSize][10];
	Cheese lifeCheese;
	Cheese pointCheese;
	Cheese spreeCheese;
	Mouse m;
	ProgressBar bar = new ProgressBar(0, 100);
	LifeIndicator life = new LifeIndicator(7);
	
	OptionFrame of;
	ScoreWriter sw;

        ImageIcon imageIcon;
        URL path;
	Image bg;

	//-----------CONSTRUCTOR-----------//
	public Game(int height, int length) {
                path = getClass().getResource("images/bg.png");
                if(path != null){
                    imageIcon = new ImageIcon(path);
                    bg = imageIcon.getImage();
                }

		winHt = height;
		winLn = length;
		
		m = new Mouse();
		
		startMessage = "START!";
		levelMessage = "Level " + level;
		goalMessage = goal + " cheeses";
		
		addMouseMotionListener(new MouseMotionAdapter() { //listens for cursor movement
			public void mouseMoved(MouseEvent e) {
				double changeX = m.getX() - e.getX();
				double changeY = m.getY() - e.getY();
				double angle = Math.atan(changeX / changeY) * -1;
				//set mouse paint angle based on cursor motion
				if (changeY < 0)
					angle += Math.PI;
				m.setAngle(angle);
					
				// make sure mouse doesn't go off playing area
				if ((e.getX() > 25) && (e.getX() < winLn))
					m.setX(e.getX());
				if ((e.getY() > 25) && (e.getY() > 100))
					m.setY(e.getY());
					
				//if cheese or trap is caught, move offscreen and increase counter
                                //this collision detection logic could easily be put into a collideDetect() method
                                //which would make a lot of sense since it's used a lot
				if(go){
					for(int i = 0; i < chArray.length; i++){
				   		for(int j = 0; j < chArray[i].length; j++){
				   			if((Math.abs(m.getY() - chArray[i][j].getY())) < 50 && //checks for a minimum displacement
				   				(Math.abs(m.getX() - chArray[i][j].getX())) < 50){ //between cursor and object
					    			chArray[i][j].setY((int)(Math.random() * -1000));
					    			chCount++;
						    		if(chCount < goal)
						    			points += 25;
					    			else
					    				points += 50;
				   			}
				   		}
					}
					for(int i = 0; i < trapArray.length; i++){
				    	for(int j = 0; j < trapArray[i].length; j++){
				    		if(!spree){
								if((Math.abs(m.getY() - trapArray[i][j].getY())) < 32 && //the distance required to register a collision with a trap
					   				(Math.abs(m.getX() - trapArray[i][j].getX())) < 32){ //is shorter than the comparable value for cheeses
						    			trapArray[i][j].setY((int)(Math.random() * -1000)); //this makes the player feel like they're really awesome
							    		if(spree){
							    			if(chCount < goal)
							    				points += 200;
							   				else
							   				points += 400;
						   				}
						   				else
						   					trapCount++;		
					   			}
				    		}
				    		else{
								if((Math.abs(m.getY() - trapArray[i][j].getY())) < 50 && 
					   				(Math.abs(m.getX() - trapArray[i][j].getX())) < 50){
						    			trapArray[i][j].setY((int)(Math.random() * -1000));
							    		if(chCount < goal)
							    			points += 200;
							   			else
							   				points += 400;		
					   			}
				    		}
				   		}
					}
					if((Math.abs(m.getY() - lifeCheese.getY())) < 50 && 
				   		(Math.abs(m.getX() - lifeCheese.getX())) < 50){
					   		lifeCheese.setY((int)(Math.random() * lifeFreq));
					   		lifeCheese.setX((int)(Math.random() * 700));
					   		if(trapCount > 0)
						   		trapCount--;	
						   	if(chCount < goal)
						   		points += 100;
						   	else
						   		points += 200;	
				    }
				    if((Math.abs(m.getY() - pointCheese.getY())) < 50 && 
				   		(Math.abs(m.getX() - pointCheese.getX())) < 50){
				    		pointCheese.setY((int)(Math.random() * pointFreq));
				    		pointCheese.setX((int)(Math.random() * 700));
					    	if(level > 8)
					    		chCount += 10;
					    	else
					    		chCount += 5;
						   	if(chCount < goal)
						   		points += 50;
						   	else
						   		points += 100;
				    }
			    	if((Math.abs(m.getY() - spreeCheese.getY())) < 50 && 
				   		(Math.abs(m.getX() - spreeCheese.getX())) < 50){
					   		spreeCheese.setY((int)(Math.random() * spreeFreq));
					   		spreeCheese.setX((int)(Math.random() * 700));
						   	spree = true;
					   		for(int i = 0; i < trapArray.length; i++){
					   			for(int j = 0; j < trapArray[i].length; j++){
					   				trapArray[i][j].setImage("images/trapblue.png");
					    		}
					    	}
					   		if(chCount < goal)
					    		points += 200;
					    	else
					    		points += 400;
				    }
				}
			}
		});
		
		//initialize cheese and trap arrays, and special cheeses
		for(int i = 0; i < chArray.length; i++){
			for(int j = 0; j < chArray[i].length; j++){
				startHt = -1 * (int)(Math.random() * (100)) * j;
				chArray[i][j] = new Cheese("images/cheese.png", pixels, startHt, winLn, winHt,
					(double)(Math.random() * 360), 64);
				pixels += 80;
			}
			pixels = 10;
		}
		
		for(int i = 0; i < trapArray.length; i++){
			for(int j = 0; j < trapArray[i].length; j++){
				startHt = -1 * (int)(Math.random() * (100)) * j;
				trapArray[i][j] = new Cheese("images/trap.png", pixels, startHt, winLn, winHt,
					(double)(Math.random() * 360), 48);
				pixels += 80;
			}
			pixels = 10;
		}
		
		lifeCheese = new Cheese("images/cheesepink.png", (int)(Math.random() * 700), 
			(int)(Math.random() * lifeFreq), winLn, winHt, (Math.random() * 360), 64);
		
		pointCheese = new Cheese("images/cheesered.png", (int)(Math.random() * 700),
			(int)(Math.random() * pointFreq), winLn, winHt, (Math.random() * 360), 64);
			
		spreeCheese = new Cheese("images/cheeseblue.png", (int)(Math.random() * 700),
			(int)(Math.random() * spreeFreq), winLn, winHt, Math.random() * 360, 64);
			
		final Timer spreeTime = new Timer(5000, new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		spree = false;
	    		for(int i = 0; i < trapArray.length; i++){
		    		for(int j = 0; j < trapArray[i].length; j++){
		    			trapArray[i][j].setImage("images/trap.png");
		    		}
		    	}
		    	spreeMessage = "";
	    	}
	   	});
	    
	   final Timer startTimer = new Timer(3000, new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		go = true;
	    		startMessage = "";
	    	}
	    });
	     
	    final Timer secondTimer = new Timer(1000, new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(levelTime > 0 && go)
	    			levelTime -= 1;
	    		timeMessage = "Time: " + levelTime + "s";
	    		if(levelTime < 17){
	    			goalMessage = "";
	    			startTimer.stop();
	    		}
	    	}
	    });
		
	    final Timer timer = new Timer(40, new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		//randomly assign positions of cheese and traps
		   		for(int i = 0; i < chArray.length; i++){
			   		for(int j = 0; j < chArray[i].length; j++){
			   			chArray[i][j].move(false, 0, moveAmnt/2); //move the objects downscreen
			   			//when offscreen, reassign random position
			    		if(chArray[i][j].getY() > winHt){
			    			if(j % 3 == 0)
								chArray[i][j].setY(-1 * (int)(Math.random() * (100)) * j);
							if(j % 2 == 0)
								chArray[i][j].setY(-1 * (int)(Math.random() * (200)) * j);
							else
								chArray[i][j].setY(-1 * (int)(Math.random() * (400)));
			   			}
			   		}
		    	}
	    		for(int i = 0; i < trapArray.length; i++){
		    		for(int j = 0; j < trapArray[i].length; j++){
			   			trapArray[i][j].move(false, 0, moveAmnt/2);
			   			//when offscreen, reassign random position
			   			if(trapArray[i][j].getY() > winHt){	
			   				if(j % 3 == 0)
								trapArray[i][j].setY(-1 * (int)(Math.random() * (100)) * j);
							if(j % 2 == 0)
								trapArray[i][j].setY(-1 * (int)(Math.random() * (200)) * j);
							else
								trapArray[i][j].setY(-1 * (int)(Math.random() * (400)));
		    			}
		    		}
		   		}
		    		
			   	lifeCheese.move(false, 0, moveAmnt/2);
			   	if(lifeCheese.getY() > winHt){
			    	lifeCheese.setY((int)(Math.random() * lifeFreq));
			    	lifeCheese.setX((int)(Math.random() * 700));
		    		lifeCheese.setAngle(Math.random() * 360);
		    	}
			    	
		   		pointCheese.move(false, 0, moveAmnt/2);
		   		if(pointCheese.getY() > winHt){
		    		pointCheese.setY((int)(Math.random() * pointFreq));
		    		pointCheese.setX((int)(Math.random() * 700));
		    		pointCheese.setAngle(Math.random() * 360);
	    		}
		    		
	    		spreeCheese.move(false, flyAmnt, (int)(moveAmnt));
		   		if(spreeCheese.getY() > winHt){
		   			spreeCheese.setY((int)(Math.random() * spreeFreq));
		   			spreeCheese.setX((int)(Math.random() * 700));
		   			spreeCheese.setAngle(Math.random() * 360);
		    		flyAmnt = (int)(Math.random() * 50);
		    	}
	    		
	    		//reassign overlapping icons to new positions within columns
	    		for(int i = 0; i < chArray[0].length; i++){
	    			for(int j = 0; j < chArray.length; j++){
	    				for(int k = 0; k < chArray.length; k++){
	    					if(j != k){
	    						if(Math.abs(chArray[j][i].getY() - chArray[k][i].getY()) < 64){
				    				chArray[j][i].setY((int)(Math.random() * (-400)));
				    			}
	    					}
	    				}
	    				for(int k = 0; k < trapArray.length; k++){
	    					if(Math.abs(chArray[j][i].getY() - trapArray[k][i].getY()) < 64){
	    						chArray[j][i].setY((int)(Math.random() * (-400)));
	    					}
	    				}
	    			}
	    			for(int j = 0; j < trapArray.length; j++){
	    				for(int k = 0; k < chArray.length; k++){
	    					if(Math.abs(trapArray[j][i].getY() - chArray[k][i].getY()) < 64){
	    						trapArray[j][i].setY((int)(Math.random() * (-400)));
	    					}
	    				}
	    				for(int k = 0; k < trapArray.length; k++){
	    					if(j != k){
	    						if(Math.abs(trapArray[j][i].getY() - trapArray[k][i].getY()) < 64){
				    				trapArray[j][i].setY((int)(Math.random() * (-400)));
				    			}
	    					}
	    				}
	    			}
	    		}
	    		
	    		//if cheese or trap is caught, move offscreen and increase counter
                        //the collision detection code is duplicated here in the timer and in the mouse listener
				if(go){
					for(int i = 0; i < chArray.length; i++){
				   		for(int j = 0; j < chArray[i].length; j++){
				   			if((Math.abs(m.getY() - chArray[i][j].getY())) < 50 && 
				   				(Math.abs(m.getX() - chArray[i][j].getX())) < 50){
					    			chArray[i][j].setY((int)(Math.random() * -1000));
					    			chCount++;
						    		if(chCount < goal)
						    			points += 25;
					    			else
					    				points += 50;
				   			}
				   		}
					}
					for(int i = 0; i < trapArray.length; i++){
				    	for(int j = 0; j < trapArray[i].length; j++){
				    		if(!spree){
								if((Math.abs(m.getY() - trapArray[i][j].getY())) < 32 && 
					   				(Math.abs(m.getX() - trapArray[i][j].getX())) < 32){
						    			trapArray[i][j].setY((int)(Math.random() * -1000));
							    		if(spree){
							    			if(chCount < goal)
							    				points += 200;
							   				else
							   				points += 400;
						   				}
						   				else
							   				if(trapCount <= 10)
							   					trapCount++;		
					   			}
				    		}
				    		else{
								if((Math.abs(m.getY() - trapArray[i][j].getY())) < 50 && 
					   				(Math.abs(m.getX() - trapArray[i][j].getX())) < 50){
						    			trapArray[i][j].setY((int)(Math.random() * -1000));
							    		if(chCount < goal)
							    			points += 200;
							   			else
							   				points += 400;	
					   			}
				    		}
				   		}
					}
					if((Math.abs(m.getY() - lifeCheese.getY())) < 50 && 
				   		(Math.abs(m.getX() - lifeCheese.getX())) < 50){
					   		lifeCheese.setY((int)(Math.random() * lifeFreq));
					   		lifeCheese.setX((int)(Math.random() * 700));
					   		if(trapCount > 0)
						   		trapCount--;	
						   	if(chCount < goal)
						   		points += 100;
						   	else
						   		points += 200;	
				    }
				    if((Math.abs(m.getY() - pointCheese.getY())) < 50 && 
				   		(Math.abs(m.getX() - pointCheese.getX())) < 50){
				    		pointCheese.setY((int)(Math.random() * pointFreq));
				    		pointCheese.setX((int)(Math.random() * 700));
					    	if(level > 8)
					    		chCount += 10;
					    	else
					    		chCount += 5;
						   	if(chCount < goal)
						   		points += 50;
						   	else
						   		points += 100;
				    }
			    	if((Math.abs(m.getY() - spreeCheese.getY())) < 50 && 
				   		(Math.abs(m.getX() - spreeCheese.getX())) < 50){
					   		spreeCheese.setY((int)(Math.random() * spreeFreq));
					   		spreeCheese.setX((int)(Math.random() * 700));
						   	spree = true;
					   		for(int i = 0; i < trapArray.length; i++){
					   			for(int j = 0; j < trapArray[i].length; j++){
					   				trapArray[i][j].setImage("images/trapblue.png");
					    		}
					    	}
					   		if(chCount < goal)
					    		points += 200;
					    	else
					    		points += 400;
				    }
				}

	    		life = new LifeIndicator(lives - trapCount);
	    		bar = new ProgressBar(chCount, goal);
	    		
	    		repaint();
	    		
	    		pointMessage = "Score: " + points;
	    		
	    		if(spree){
	    			spreeTime.start();
	    			spreeMessage = "Invulnerable!";
	    		}	
	    		
	    		if(trapCount > 6){
	    			startMessage = "GAME OVER";
	    			go = false;
	    			if(sw == null)
	    				sw = new ScoreWriter(JOptionPane.showInputDialog("Enter your name", 
	    					"username"), points + "");
	    			if(of == null)
	    				of = new OptionFrame();
	    		}
	    		
	    	}
	    });
	    
	    final Timer levelTimer = new Timer(20000, new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(chCount < goal){
	    			startMessage = "GAME OVER";
	    			go = false;
	    			timer.stop();
	    			if(sw ==  null)
	    				sw = new ScoreWriter(JOptionPane.showInputDialog("Enter your name",
	    					"username"), points + "");
	    			if(of == null)
	    				of = new OptionFrame();
	    		}
	    		else{
	    			if(go){
		    			level++;
		    			levelMessage = "Level " + level;
		    			goal += 5;
		    			goalMessage = goal + " cheeses";
		    			chCount = 0;
		    			moveAmnt += 2;
		    			levelTime = 20;
		    			if(trapCount > 0)
		    				trapCount--;
	    			}
	    		}
	    	}
	    });
	    
	    
	    timer.start();
	    startTimer.start();
	    secondTimer.start();
            levelTimer.start();
	}
	
	//draw components
	public void paintComponent(Graphics g){
		g.drawImage(bg, 0, 0, null);
		for(int i = 0; i < chArray.length; i++){	
			for(int j = 0; j < chArray[i].length; j++){
				if(chArray[i][j].getY() > 70)
					chArray[i][j].draw(g);
			}
		}
		for(int i = 0; i < trapArray.length; i++){	
			for(int j = 0; j < trapArray[i].length; j++){
				if(trapArray[i][j].getY() > 70)
					trapArray[i][j].draw(g);
			}
		}
		if(lifeCheese.getY() > 70)
			lifeCheese.draw(g);
		if(pointCheese.getY() > 70)
			pointCheese.draw(g);
		if(spreeCheese.getY() > 70)
			spreeCheese.draw(g);
		g.setColor(Color.yellow);
		g.setFont(new Font("Courie", Font.BOLD, 15));
		g.drawString("Lives: " + (lives - trapCount), 232, 20);
		g.drawString("Cheese-O-Meter: " + chCount + "/" + goal, 12, 20);
		g.drawString("Emmett Butler 2010", 20, 665);
		g.drawString(timeMessage, 12, 72);
		if(levelTime < 17)
			g.drawString(levelMessage, 100, 72);
		else{
			g.drawString(levelMessage, 100, 72);
			g.setFont(new Font("Courie", Font.BOLD, 30));
			g.drawString(levelMessage, 350, 350);
		}
		g.setFont(new Font("Courie", Font.BOLD, 15));
		g.drawString(pointMessage, 350, 20);
		if(!go){
			g.setFont(new Font("Courie", Font.BOLD, 30));
			g.drawString(pointMessage, 350, 450);
		}
		bar.draw(g);
		life.draw(g);
		m.draw(g);
		bar.draw(g, m.getX() - 33, m.getY() - 36);
		g.setColor(Color.yellow);
		g.setFont(new Font("Courie", Font.BOLD, 30));
		g.drawString(startMessage, 350, 300);
		g.drawString(goalMessage, 350, 400);
		g.drawString(spreeMessage, 350, 250); 
	}
}