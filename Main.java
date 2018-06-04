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
import javafx.scene.control.TextArea;
import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import java.util.ArrayList;
import java.awt.Rectangle;

public class Main extends Application implements EventHandler<InputEvent>
{
	GraphicsContext gc;
	AnimateObjects animate;
	Canvas canvas;
	Fighter fighter1, fighter2;
	Image stance1, stance2;
	String stance1str, stance2str;
	boolean left1 = false, left2 = false;
	double mouseX, mouseY;
	int delay = 15;
	int atkTime = 15;
	Group root;
	Text f1Label, f2Label;

	double xSpeed1, xSpeed2, ySpeed1, ySpeed2;
	public static void main(String[] args)
	{
		launch();
	}
	public class AnimateObjects extends AnimationTimer{
		public void handle(long now) {
			if(fighter1.getHealth() > 0 && fighter2.getHealth() > 0){
				clear();
				fighter1.move(xSpeed1, ySpeed1);
				fighter2.move(xSpeed2, ySpeed2);
				gc.drawImage(stance1, 180+fighter1.getXpos(), 100-fighter1.getYpos());
				gc.drawImage(stance2, 500+fighter2.getXpos(), 100-fighter2.getYpos());

				f1Label.setX(180+fighter1.getXpos()); //fighter labels
				f1Label.setY(100-fighter1.getYpos());
				f2Label.setX(500+fighter2.getXpos());
				f2Label.setY(100-fighter2.getYpos());

				gc.setFill(Color.RED); //health bar
				gc.fillRect(25,300,300,10);
				gc.fillRect(425,300,300,10);

				gc.setFill(Color.GREEN);
				gc.fillRect(25,300, (int)(300 * (double)fighter2.getHealth()/(double)fighter2.getMaxHealth()),10);
				gc.fillRect(425,300, (int)(300 * (double)fighter1.getHealth()/(double)fighter1.getMaxHealth()) ,10);

				if(fighter1.getIsJumping()){ //fighter1 jump
					ySpeed1 = fighter1.getJumpSpeed() - .5*fighter1.getJumpTime();
					fighter1.setJumpTime(fighter1.getJumpTime()+1);
					if(ySpeed1 == -fighter1.getJumpSpeed()) {
						fighter1.setJumpTime(0);
						ySpeed1 = 0;
						fighter1.setIsJumping(false);
					}
				}
				if(fighter2.getIsJumping()){//fighter2 jump
					ySpeed2 = fighter2.getJumpSpeed() - .5*fighter2.getJumpTime();
					fighter2.setJumpTime(fighter2.getJumpTime()+1);
					if(ySpeed2 == -fighter2.getJumpSpeed()) {
						fighter2.setJumpTime(0);
						ySpeed2 = 0;
						fighter2.setIsJumping(false);
					}
				}

				//Hit registration
				Rectangle2D hitbx1 = new Rectangle2D(180+fighter1.getXpos(),100-fighter1.getYpos(),stance1.getWidth(),stance1.getHeight());
				Rectangle2D hitbx2 = new Rectangle2D(500+fighter2.getXpos(),100-fighter2.getYpos(),stance2.getWidth(),stance2.getHeight());
				if (hitbx1.intersects(hitbx2) && fighter2.isPunching()) {
					System.out.println("Fighter 2 punched Fighter 1");
					if(fighter1.getDemDelay() == 0 && !fighter1.getDemobilized() && !fighter1.getIsJumping()){
						fighter1.setDemTime(1);
						fighter1.setDemobilized(true);
					}
					double crit = Math.random();
					if(crit < fighter1.getCritChance()){
						System.out.println("CRIT");
						int damage = (int)(fighter1.getDamage() * (1.5+(Math.random()*(fighter1.getMaxCrit()-.1))) * (Math.random()+.5));
						fighter2.setHealth(fighter2.getHealth() - damage);
					}
					else
						fighter2.setHealth(fighter2.getHealth() - fighter1.getDamage());
				}
				if (hitbx2.intersects(hitbx1) && fighter1.isPunching()) {
					double crit = Math.random();
					System.out.println(crit);
					System.out.println("Fighter 1 punched Fighter 2");
					if(fighter2.getDemDelay() == 0 && !fighter2.getDemobilized() && !fighter2.getIsJumping()){
						fighter2.setDemTime(1);
						fighter2.setDemobilized(true);
					}
					if(crit < fighter1.getCritChance()){
						System.out.println("CRIT");
						int damage = (int)(fighter2.getDamage() * (1.5+(Math.random()*(fighter2.getMaxCrit()-.1))) * (Math.random()+.5));
						fighter1.setHealth(fighter1.getHealth() - damage);
					}
					else
						fighter1.setHealth(fighter1.getHealth() - fighter2.getDamage());
				}
				if (hitbx1.intersects(hitbx2) && fighter2.isKicking()) {
					System.out.println("Fighter 2 kicked Fighter 1");
					if(fighter1.getDemDelay() == 0 && !fighter1.getDemobilized() && !fighter1.getIsJumping()){
						fighter1.setDemTime(1);
						fighter1.setDemobilized(true);
					}
					double crit = Math.random();
					if(crit < fighter1.getCritChance()){
						int damage = (int)(fighter1.getDamage() * (1.5+(Math.random()*(fighter1.getMaxCrit()-.1))) * (Math.random()+.5));
						fighter2.setHealth(fighter2.getHealth() - damage);
					}
					else
						fighter2.setHealth(fighter2.getHealth() - fighter1.getDamage());
				}
				if (hitbx2.intersects(hitbx1) && fighter1.isKicking()) {
					System.out.println("Fighter 1 kicked Fighter 2");
					if(fighter2.getDemDelay() == 0 && !fighter2.getDemobilized() && !fighter2.getIsJumping()){
						fighter2.setDemTime(1);
						fighter2.setDemobilized(true);
					}
					double crit = Math.random();
					if(crit < fighter2.getCritChance()){
						int damage = (int)(fighter2.getDamage() * (1.5+(Math.random()*(fighter2.getMaxCrit()-.1))) * (Math.random()+.5));
						fighter1.setHealth(fighter1.getHealth() - damage);
					}
					else
						fighter1.setHealth(fighter1.getHealth() - fighter2.getDamage());
				}

				//Demobilization
				if(fighter1.getDemTime() > 0){
					System.out.println("Demobilized");
					fighter1.setDemTime(fighter1.getDemTime() + 1);
					ySpeed1 = fighter1.getJumpSpeed()/2 - .5*fighter1.getDemTime();
					if(((fighter1.getXpos() + 180) > (fighter2.getXpos() + 500)) && !fighter2.getAtBounds()){
						xSpeed1 = -3;
					}
					else if(!fighter1.getAtBounds()) {
						xSpeed1 = 3;
					}
					if(ySpeed1 <= -fighter1.getJumpSpeed()/2 + 1) {
						ySpeed1 = 0;
						xSpeed1 = 0;
						fighter1.setDemTime(0);
						fighter1.setDemobilized(false);
						fighter1.setDemDelay(1);
					}
				}
				if(fighter2.getDemTime() > 0){
					System.out.println("Demobilized");
					fighter2.setDemTime(fighter2.getDemTime() + 1);
					ySpeed2 = fighter2.getJumpSpeed()/2 - .5*fighter2.getDemTime();
					if(((fighter2.getXpos() + 500) < (fighter1.getXpos() + 180)) && !fighter2.getAtBounds()){
						xSpeed2 = 3;
					}
					else if(!fighter2.getAtBounds()) {
						xSpeed2 = -3;
					}
					if(ySpeed2 <= -fighter2.getJumpSpeed()/2 + 1) {
						ySpeed2 = 0;
						xSpeed2 = 0;
						fighter2.setDemTime(0);
						fighter2.setDemobilized(false);
						fighter2.setDemDelay(1);
					}
				}
				if(fighter1.getDemDelay() > 0){
					if(fighter1.getDemDelay() == 10)
						fighter1.setDemDelay(0);
					else fighter1.setDemDelay(fighter1.getDemDelay() + 1);
				}
				if(fighter2.getDemDelay() > 0){
					if(fighter2.getDemDelay() == 10)
						fighter2.setDemDelay(0);
					else fighter2.setDemDelay(fighter2.getDemDelay() + 1);
				}

				//Punching and kicking delay
				if(fighter1.getPunchDelay() > 0 && fighter1.getPunchDelay() <= delay){
					fighter1.setPunchDelay(fighter1.getPunchDelay()+1);
					if(fighter1.getPunchDelay() == delay)
						fighter1.setPunchDelay(0);
				}
				if(fighter1.getKickDelay() > 0 && fighter1.getKickDelay() <= delay){
					fighter1.setKickDelay(fighter1.getKickDelay()+1);
					if(fighter1.getKickDelay() == delay)
						fighter1.setKickDelay(0);
				}
				if(fighter2.getPunchDelay() > 0 && fighter2.getPunchDelay() <= delay){
					fighter2.setPunchDelay(fighter2.getPunchDelay()+1);
					if(fighter2.getPunchDelay() == delay)
						fighter2.setPunchDelay(0);
				}
				if(fighter2.getKickDelay() > 0 && fighter2.getKickDelay() <= delay){
					fighter2.setKickDelay(fighter2.getKickDelay()+1);
					if(fighter2.getKickDelay() == delay)
						fighter2.setKickDelay(0);
				}

				//crouch
				if(!stance1str.equals("crouch") && !fighter1.isPunching() && !fighter1.isKicking()){
					stance1str = "start";
					stance1 = fighter1.getStart();
				}
				if(!stance2str.equals("crouch") && !fighter2.isPunching() && !fighter2.isKicking()){
					stance2str = "start";
					stance2 = fighter2.getStart();
				}

				//punch time
				if(fighter1.getPunchTime() > 0 && fighter1.getPunchTime() < atkTime){
					fighter1.setPunchTime(fighter1.getPunchTime()+1);
					if(fighter1.getPunchTime() == atkTime) {
						fighter1.setPunching(false);
						fighter1.setPunchTime(0);
						fighter1.setPunchDelay(1);
					}
				}
				if(fighter1.getKickTime() > 0 && fighter1.getKickTime() < atkTime){
					fighter1.setKickTime(fighter1.getKickTime()+1);
					if(fighter1.getKickTime() == atkTime) {
						fighter1.setKicking(false);
						fighter1.setKickTime(0);
						fighter1.setKickDelay(1);
					}
				}
				if(fighter2.getPunchTime() > 0 && fighter2.getPunchTime() < atkTime){
					fighter2.setPunchTime(fighter2.getPunchTime()+1);
					if(fighter2.getPunchTime() == atkTime) {
						fighter2.setPunching(false);
						fighter2.setPunchTime(0);
						fighter2.setPunchDelay(1);
					}
				}
				if(fighter2.getKickTime() > 0 && fighter2.getKickTime() < atkTime){
					fighter2.setKickTime(fighter2.getKickTime()+1);
					if(fighter2.getKickTime() == atkTime) {
						fighter2.setKicking(false);
						fighter2.setKickTime(0);
						fighter2.setKickDelay(1);
					}
				}

				//Boundries
				if(fighter1.getXpos() > (620 - stance1.getWidth()) || fighter1.getXpos() < -170){
					xSpeed1 = 0;
					fighter1.setAtBounds(true);
				}
				else fighter1.setAtBounds(false);
				if(fighter2.getXpos() > (300 - stance2.getWidth()) || fighter2.getXpos() < -490){
					xSpeed2 = 0;
					fighter2.setAtBounds(true);
				}
				else fighter2.setAtBounds(false);

			}
			else if(fighter1.getHealth() <= 0 && fighter2.getHealth() <= 0){
				if(fighter1.getHealth() > fighter2.getHealth()){
					gc.setFill(Color.YELLOW); //Fills the text in yellow
					gc.setStroke(Color.BLACK); //Changes the outline the black
					gc.setLineWidth(1); //How big the black lines will be
					Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
					gc.setFont(font);
					gc.fillText("Fighter 1 Wins!", 100, 50 ); //draws the yellow part of the text
					gc.strokeText("Fighter 1 Wins!", 100, 50 ); //draws the outline part of the text
				}
				else if(fighter1.getHealth() < fighter2.getHealth()){
					gc.setFill(Color.YELLOW); //Fills the text in yellow
					gc.setStroke(Color.BLACK); //Changes the outline the black
					gc.setLineWidth(1); //How big the black lines will be
					Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
					gc.setFont(font);
					gc.fillText("Fighter 2 Wins!", 100, 50 ); //draws the yellow part of the text
					gc.strokeText("Fighter 2 Wins!", 100, 50 ); //draws the outline part of the text
				}
				else{
					gc.setFill(Color.YELLOW); //Fills the text in yellow
					gc.setStroke(Color.BLACK); //Changes the outline the black
					gc.setLineWidth(1); //How big the black lines will be
					Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
					gc.setFont(font);
					gc.fillText("Tie!", 100, 50 ); //draws the yellow part of the text
					gc.strokeText("Tie!", 100, 50 ); //draws the outline part of the text
				}
			}
			else if(fighter1.getHealth() <= 0) {
					gc.setFill(Color.YELLOW); //Fills the text in yellow
					gc.setStroke(Color.BLACK); //Changes the outline the black
					gc.setLineWidth(1); //How big the black lines will be
					Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
					gc.setFont(font);
					gc.fillText("Fighter 1 Wins!", 100, 50 ); //draws the yellow part of the text
					gc.strokeText("Fighter 1 Wins!", 100, 50 ); //draws the outline part of the text
			}
			else if(fighter2.getHealth() <= 0) {
					gc.setFill(Color.YELLOW); //Fills the text in yellow
					gc.setStroke(Color.BLACK); //Changes the outline the black
					gc.setLineWidth(1); //How big the black lines will be
					Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
					gc.setFont(font);
					gc.fillText("Fighter 2 Wins!", 100, 50 ); //draws the yellow part of the text
					gc.strokeText("Fighter 2 Wins!", 100, 50 ); //draws the outline part of the text
			}
		}
	}
	public void handle(final InputEvent event){
		if(event instanceof KeyEvent){
			//Fighter 1 movements
			if(!fighter1.getDemobilized()){
				if(fighter1.getXpos() > -170){
					if (((KeyEvent)event).getCode() == KeyCode.A ){
						if(event.getEventType().toString().equals("KEY_PRESSED") ) {
							if(!fighter1.getLeft()){
								fighter1.setLeft(true);
								if(stance1str.equals("start"))
									stance1 = fighter1.getStart();
								if(stance1str.equals("crouch"))
									stance1 = fighter1.getCrouch();
								if(stance1str.equals("punch"))
									stance1 = fighter1.getPunch();
								if(stance1str.equals("kick"))
									stance1 = fighter1.getKick();
							}
							xSpeed1 = fighter1.getSpeed();
						}
						if(event.getEventType().toString().equals("KEY_RELEASED") ){
							xSpeed1 = 0;
						}
					}
				}
				if(fighter1.getXpos() < (620 - stance1.getWidth())){
					if (((KeyEvent)event).getCode() == KeyCode.D ){
						if(event.getEventType().toString().equals("KEY_PRESSED") ) {
							if(fighter1.getLeft()){
								fighter1.setLeft(false);
								if(stance1str.equals("start"))
									stance1 = fighter1.getStart();
								if(stance1str.equals("crouch"))
									stance1 = fighter1.getCrouch();
								if(stance1str.equals("punch"))
									stance1 = fighter1.getPunch();
								if(stance1str.equals("kick"))
									stance1 = fighter1.getKick();
							}
							xSpeed1 = -fighter1.getSpeed();
						}
						if(event.getEventType().toString().equals("KEY_RELEASED") ){
							xSpeed1 = 0;
						}
					}
				}
				if (((KeyEvent)event).getCode() == KeyCode.W ){
					if(!fighter1.getIsJumping()){
						if(event.getEventType().toString().equals("KEY_PRESSED") ) {
							fighter1.setIsJumping(true);
						}
					}
				}
				if (((KeyEvent)event).getCode() == KeyCode.S ){
					if(event.getEventType().toString().equals("KEY_PRESSED") ) {
						stance1str = "crouch";
						stance1 = fighter1.getCrouch();
					}
					if(event.getEventType().toString().equals("KEY_RELEASED") ){
						stance1str = "start";
						stance1 = fighter1.getStart();
					}

				}
				if (((KeyEvent)event).getCode() == KeyCode.E && fighter1.getPunchDelay() == 0 && fighter1.getPunchTime() == 0){
					if(event.getEventType().toString().equals("KEY_PRESSED") ) {
						stance1str = "punch";
						stance1 = fighter1.getPunch();
						fighter1.setPunching(true);
						fighter1.setPunchTime(1);
					}
					if(event.getEventType().toString().equals("KEY_RELEASED") ){
						stance1str = "start";
						stance1 = fighter1.getStart();
						fighter1.setPunching(false);
					}
				}
				if (((KeyEvent)event).getCode() == KeyCode.R && fighter1.getKickDelay() == 0 && fighter1.getKickTime() == 0){
					if(event.getEventType().toString().equals("KEY_PRESSED") ) {
						stance1str = "kick";
						stance1 = fighter1.getKick();
						fighter1.setKicking(true);
						fighter1.setKickTime(1);
					}
					if(event.getEventType().toString().equals("KEY_RELEASED") ){
						stance1str = "start";
						stance1 = fighter1.getStart();
						fighter1.setKicking(false);
					}
				}
			}
			//Fighter 2 movements
			if(!fighter2.getDemobilized()){
				if(fighter2.getXpos() > -490){
					if (((KeyEvent)event).getCode() == KeyCode.LEFT ){
						if(event.getEventType().toString().equals("KEY_PRESSED") ) {
							if(fighter2.getLeft()){
								fighter2.setLeft(false);
								if(stance2str.equals("start"))
									stance2 = fighter2.getStart();
								if(stance2str.equals("crouch"))
									stance2 = fighter2.getCrouch();
								if(stance2str.equals("punch"))
									stance2 = fighter2.getPunch();
								if(stance2str.equals("kick"))
									stance2 = fighter2.getKick();
							}
							xSpeed2 = fighter2.getSpeed();
						}
						if(event.getEventType().toString().equals("KEY_RELEASED") ){
							xSpeed2 = 0;
						}
					}
				}
				if(fighter2.getXpos() < (300 - stance1.getWidth())){
					if (((KeyEvent)event).getCode() == KeyCode.RIGHT ){
						if(event.getEventType().toString().equals("KEY_PRESSED") ) {
							if(!fighter2.getLeft()){
								fighter2.setLeft(true);
								if(stance2str.equals("start"))
									stance2 = fighter2.getStart();
								if(stance2str.equals("crouch"))
									stance2 = fighter2.getCrouch();
								if(stance2str.equals("punch"))
									stance2 = fighter2.getPunch();
								if(stance2str.equals("kick"))
									stance2 = fighter2.getKick();
							}
							xSpeed2 = -fighter2.getSpeed();
						}
						if(event.getEventType().toString().equals("KEY_RELEASED") ){
							xSpeed2 = 0;
						}
					}
				}
				if (((KeyEvent)event).getCode() == KeyCode.UP ){
					if(!fighter2.getIsJumping()){
						if(event.getEventType().toString().equals("KEY_PRESSED") ) {
							fighter2.setIsJumping(true);
						}
					}
				}
				if (((KeyEvent)event).getCode() == KeyCode.DOWN ){
					if(event.getEventType().toString().equals("KEY_PRESSED") ) {
						stance2str = "crouch";
						stance2 = fighter2.getCrouch();
					}
					if(event.getEventType().toString().equals("KEY_RELEASED") ){
						stance2str = "start";
						stance2 = fighter2.getStart();
					}

				}
				if (((KeyEvent)event).getCode() == KeyCode.SLASH && fighter2.getPunchDelay() == 0 && fighter2.getPunchTime() == 0){
					if(event.getEventType().toString().equals("KEY_PRESSED") ) {
						stance2str = "punch";
						stance2 = fighter2.getPunch();
						fighter2.setPunching(true);
						fighter2.setPunchTime(1);
					}
					if(event.getEventType().toString().equals("KEY_RELEASED") ){
						stance2str = "start";
						stance2 = fighter2.getStart();
						fighter2.setPunching(false);
					}
				}
				if (((KeyEvent)event).getCode() == KeyCode.PERIOD && fighter2.getKickDelay() == 0 && fighter2.getKickTime() == 0){
					if(event.getEventType().toString().equals("KEY_PRESSED") ) {
						stance2str = "kick";
						stance2 = fighter2.getKick();
						fighter2.setKicking(true);
						fighter2.setKickTime(1);
					}
					if(event.getEventType().toString().equals("KEY_RELEASED") ){
						stance2str = "start";
						stance2 = fighter2.getStart();
						fighter2.setKicking(false);
					}
				}
			}
		}
		if (event instanceof MouseEvent){
			mouseX = ((MouseEvent)event).getX();
			mouseY = ((MouseEvent)event).getY();
		}
	}
	public void start(Stage stage)
	{
		stage.setTitle("Final Project");
		root = new Group();
		canvas = new Canvas(800, 400);
		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		gc = canvas.getGraphicsContext2D();

		fighter1 = new Fighter(true);
		stance1 = fighter1.getStart();
		stance1str = "start";
		gc.drawImage(stance1, 80 , 180);
		fighter2 = new Fighter(false);
		stance2 = fighter2.getStart();
		stance2str = "start";
		gc.drawImage(stance2, 500 , 180);
		f1Label = new Text(180+fighter1.getXpos(), 100-fighter1.getYpos(), "Fighter 1");
		f2Label = new Text(500+fighter2.getXpos(), 100-fighter2.getYpos(), "Fighter 2");
		root.getChildren().add(f1Label);
		root.getChildren().add(f2Label);

		animate = new AnimateObjects();
		animate.start();
		stage.show();
		scene.addEventHandler(KeyEvent.KEY_PRESSED, this);
		scene.addEventHandler(KeyEvent.KEY_RELEASED,this);
		scene.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
	}
	public void clear(){
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
}