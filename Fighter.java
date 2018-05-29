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
public class Fighter{
	private int health, maxHealth, damage, speed, xPos, yPos;
	private double critChance, maxCrit;

	private Image start, punch, kick, crouch, startL, punchL, kickL, crouchL;

	private boolean left;

	private boolean isJumping;
	private int jumpTime, jumpSpeed;

	private boolean isPunching, isKicking;
	private int punchDelay, kickDelay, punchTime, kickTime;

	boolean demobilized;
	int demobilizedTime, demobilizedDelay;


	public Fighter(boolean l){
		health = 3000;
		maxHealth = 3000;
		damage = 5;
		speed = 10;
		critChance = .2;
		maxCrit = 3;
		isJumping = false;
		isPunching = false;
		isKicking = false;
		demobilized = false;

		if(l) {
			start = new Image("defaultStart.png");
			punch = new Image("defaultPunch.png");
			kick = new Image("defaultKick.png");
			crouch = new Image("defaultCrouch.png");
			startL = new Image("defaultStartL.png");
			punchL = new Image("defaultPunchL.png");
			kickL = new Image("defaultKickL.png");
			crouchL = new Image("defaultCrouchL.png");

			jumpTime = 0;
			jumpSpeed = 10;
		}
		else {
			startL = new Image("defaultStart.png");
			punchL = new Image("defaultPunch.png");
			kickL = new Image("defaultKick.png");
			crouchL = new Image("defaultCrouch.png");
			start = new Image("defaultStartL.png");
			punch = new Image("defaultPunchL.png");
			kick = new Image("defaultKickL.png");
			crouch = new Image("defaultCrouchL.png");

			jumpTime = 0;
			jumpSpeed = 10;
		}
	}

	//Images
	public Image getStart(){
		if(left)
			return startL;
		return start;
	}
	public Image getPunch(){
		if(left)
			return punchL;
		return punch;
	}
	public Image getKick(){
		if(left)
			return kickL;
		return kick;
	}
	public Image getCrouch(){
		if(left)
			return crouchL;
		return crouch;
	}
	public boolean getLeft(){
		return left;
	}
	public void setLeft(boolean b){
		left = b;
	}

	//stuff thats happening
	public int getSpeed(){
		return speed;
	}
	public int getXpos(){
		return xPos;
	}
	public int getYpos(){
		return yPos;
	}

	//Movement
	public void move(double x, double y){
		xPos -= x;
		yPos += y;
	}
	public boolean getIsJumping(){
		return isJumping;
	}
	public int getJumpTime(){
		return jumpTime;
	}
	public int getJumpSpeed(){
		return jumpSpeed;
	}
	public void setIsJumping(boolean b){
		isJumping = b;
	}
	public void setJumpTime(int b){
		jumpTime = b;
	}
	public void setJumpSpeed(int b){
		jumpSpeed = b;
	}

	//Punches and Kicks
	public boolean isPunching(){
		return isPunching;
	}
	public boolean isKicking(){
		return isKicking;
	}
	public void setPunching(boolean b){
		isPunching = b;
	}
	public void setKicking(boolean b){
		isKicking = b;
	}
	public int getPunchDelay(){
		return punchDelay;
	}
	public void setPunchDelay(int x){
		punchDelay = x;
	}
	public int getKickDelay(){
		return kickDelay;
	}
	public void setKickDelay(int x){
		kickDelay = x;
	}
	public int getPunchTime(){
		return punchTime;
	}
	public void setPunchTime(int x){
		punchTime = x;
	}
	public int getKickTime(){
		return kickTime;
	}
	public void setKickTime(int x){
		kickTime = x;
	}

	//Health and damage
	public int getHealth(){
		return health;
	}
	public int getMaxHealth(){
		return maxHealth;
	}
	public void setHealth(int x){
		health = x;
	}
	public int getDamage(){
		return damage;
	}
	public double getCritChance(){
		return critChance;
	}
	public double getMaxCrit(){
		return maxCrit;
	}

	//demobilzed
	public boolean getDemobilized(){
		return demobilized;
	}
	public void setDemobilized(boolean b){
		demobilized = b;
	}
	public int getDemTime(){
		return demobilizedTime;
	}
	public void setDemTime(int t){
		demobilizedTime = t;
	}
	public int getDemDelay(){
		return demobilizedDelay;
	}
	public void setDemDelay(int t){
		demobilizedDelay = t;
	}
}