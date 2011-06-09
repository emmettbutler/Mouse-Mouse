/*
 * Mouse Mouse - coded by Emmett Butler, Spring 2010
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.net.URL;

//The mouse class is instantiated once in the Game() constructor - it is the player character
class Mouse {
	private int x = 350; //starting position
	private int y = 250;
	private int size = 48;
        private URL path; //path to image file
        private ImageIcon imageIcon;
	private Image image;
	private double angle = 0.0;
	private AffineTransform affineTransform = new AffineTransform(); //used for image rotation

	//---------CONSTRUCTOR-----------//
	public Mouse(){
            path = getClass().getResource("images/mouse.png"); //this code is repeated elsewhere for other game objects
            if(path != null){ //get the path to the file and assign it to a URL object
                imageIcon = new ImageIcon(path); //use this URL to create an imageIcon
                image = imageIcon.getImage(); //and then get the image from it
            }
	}
	
	//----------METHODS-------------//
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		affineTransform.setToTranslation(x - (size / 2), y - (size / 2));
		affineTransform.rotate(angle, size / 2, size / 2);
		g2d.drawImage(image, affineTransform, null);
	}
	
	//---------GETTERS--------------//
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getSize(){
		return size;
	}
	
	public double getAngle(){
		return angle;
	}
	
	//-----------SETTERS------------//
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setAngle(double angle){
		this.angle = angle;
	}
}

public class Cheese {
    private int x;
    private int y;
    private int yDir;
    private int xDir;
    private int winHt;
    private int winLn;
    private int size;
    private int radius;
    private int centerX;
    private int centerY;
    private Image image;
    private URL path;
    private ImageIcon imageIcon;
    private double angle = 0.0;
    private AffineTransform affineTransform = new AffineTransform();

    /*the cheese class is used for all of the falling objects, including traps. the image and type are defined with the
     * icon parameter in the contructor.
     */

	//------------CONSTRUCTOR---------//
    public Cheese(String icon, int x, int y, int winHt, int winLn, double angle, int size) {
	    this.x = x;
	    this.y = y;
	    this.xDir = 1;
	    this.yDir = 1;
	    this.winHt = winHt;
	    this.winLn = winLn;
	    this.angle = angle;
	    this.size = size;
	    radius = size / 2;
	    centerX = this.x + radius;
	    centerY = this.y + radius;
            path = getClass().getResource(icon);
            if(path != null){
                imageIcon = new ImageIcon(path);
                image = imageIcon.getImage();
            }
	}
	
	public Cheese(String icon, int winHt, int winLn, int size){
		this(icon, (int)(Math.random() * (winLn - size - 10)), 
			(int)(Math.random() * (winHt - size - 10)), winHt, winLn, (double)(Math.random() * 360),
			size);
	}
	
	//---------DRAWER-----------//
	public void draw(Graphics g) {	
		Graphics2D g2d = (Graphics2D)g;
		affineTransform.setToTranslation(x - (size / 2), y - (size / 2));
		affineTransform.rotate(angle, size / 2, size / 2);
		g2d.drawImage(image, affineTransform, null);
	}

        //this method is one of the few things that the cheese object has that Mouse doesn't
	public void move(boolean bound, int xSpeed, int ySpeed){
	   	x += xSpeed * xDir;
	   	y += ySpeed * yDir;
	   	
	   	if(bound){
		   	if (x < xSpeed)
		   		xDir = 1;
		   	else if (x > winLn - xSpeed - 64)
		   		xDir = -1;
		   	
		   	if (y < ySpeed)
		   		yDir = 1;
		   	else if (y > winHt - 64 - ySpeed)
				yDir = -1;
	   	}
	}
	
	//--------GETTERS---------------//
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getXDir(){
		return xDir;	
	}
	
	public int getYDir(){
		return yDir;	
	}
	
	public Image getImage(){
		return image;
	}
	
	public int getSize(){
		return size;	
	}
	
	//----------SETTERS--------------//
	public void setX(int changePos){
		x = changePos;
	}
	
	public void setY(int changePos){
		y = changePos;
	}
	
	public void setXDir(int mult){
		xDir *= mult;	
	}
	
	public void setYDir(int mult){
		yDir *= mult;	
	}
	
	public void setImage(String img){
                path = getClass().getResource(img);
                if(path != null){
                    imageIcon = new ImageIcon(path);
                    image = imageIcon.getImage();
                }
	}
	
	public void setAngle(double angle){
		this.angle = angle;
	}
}