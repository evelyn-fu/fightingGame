import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
public class ability{
	private int damage;
	private int xPos, yPos;
	private int speed;
	private Image image;
	private boolean left;
	private int time;
	private boolean remove;
	public ability(Image i, int x, int y, boolean l){
		image = i;
		xPos = x;
		yPos = y;
		left = l;
	}
	public int getDamage(){
		return damage;
	}
	public void move(int x, int y){
		xPos = x;
		yPos = y;
	}
	public int getXpos(){
		return xPos;
	}
	public int getYpos(){
		return yPos;
	}
	public Image getImage(){
		return image;
	}
	public int getSpeed(){
		return speed;
	}
	public boolean getLeft(){
		return left;
	}
	public int getTime(){
		return time;
	}
	public void setTime(int t){
		time = t;
	}
	public void setLeft(boolean b){
		left = b;
	}
	public void setRemove(boolean b){
		remove = b;
	}
	public boolean getRemove(){
		return remove;
	}
}