package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Dao.SinglotonConnectionDB;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.SHIP;
import model.Shot;
import model.SmallInfoLabel;
import model.User;

public class GameViewManager {
	
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	
	private static final int GAME_WIDTH = 1024;
	private static final int GAME_HEIGHT = 768;
	
	private Stage menuStage;
	private ImageView ship;
	private User PLAYER=null;
	private String DIFFICULTY=null;
	
	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;
	private boolean isSpaceKeyPressed;

	private AnimationTimer gameTimer;
	private int angle;
	
	private GridPane gridPane1;
	private GridPane gridPane2;
	private final static String BACKGROUND_IMAGE = "/resources/deep_blue.png"; 

	private final static String METEOR_BROWN_IMAGE = "/resources/meteor_brown.png";
	private final static String METEOR_GREY_IMAGE = "/resources/meteor_grey.png";

	private long timerDelay = 0;
	
	
	private ImageView[] brownMeteors;

	private List<ImageView> shots = new ArrayList<>();
	private ImageView[] greyMeteors;
	Random randomPositionGenerator;
	private SHIP choosership;
	
	
	private ImageView star;
	private SmallInfoLabel pointsLabel;
	private ImageView[] playerLifes;
	private int playerLife;
	private int points;
	private final static String GOLD_STAR_IMAGE = "/resources/star_gold.png";
	private final static String shotImage = "/resources/bullet.png";



	private final static int STAR_RADIUS = 12;
	private final static int SHIP_RADIUS = 27;
	private final static int METEOR_RADIUS = 20;
	private final static int BILLET_RADIUS = 32;
	
	public GameViewManager() {
		initializeStage();
		createKeyListeners();
		randomPositionGenerator = new Random();
		
	}
	
	private void createKeyListeners() {
		
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.LEFT) {
					isLeftKeyPressed = true;
					
				} else if (event.getCode() == KeyCode.RIGHT) {
					isRightKeyPressed = true;

				} else if (event.getCode() == KeyCode.SPACE) {
					isSpaceKeyPressed = true;
				}
				
			}
		});
		
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.LEFT) {
					isLeftKeyPressed = false;
					
				} else if (event.getCode() == KeyCode.RIGHT) {
					isRightKeyPressed = false;
					
				}	else if (event.getCode() == KeyCode.SPACE) {
					isSpaceKeyPressed = false;

				}
				
			}
		});

	}
	
	
	private void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
	}


	public void createNewGame(Stage menuStage, SHIP choosenShip,User PLAYER,String DIFF) {
		this.DIFFICULTY=DIFF;
		this.menuStage = menuStage;
		this.menuStage.hide();
		this.PLAYER=PLAYER;
		this.choosership=choosenShip;
		createBackground();
		createShip(choosenShip);
		createGameElements(choosenShip);
		createGameLoop();
		gameStage.show();
	}
	
	private void createGameElements(SHIP choosenShip) {
		
		playerLife = 2;
		star = new ImageView(GOLD_STAR_IMAGE);
		setNewElementPosition(star);
		gamePane.getChildren().add(star);
		pointsLabel = new SmallInfoLabel("POINTS : 00");
		pointsLabel.setLayoutX(460);
		pointsLabel.setLayoutY(20);
		gamePane.getChildren().add(pointsLabel);
		playerLifes = new ImageView[3];
		
		for(int i = 0; i < playerLifes.length; i++) {
			playerLifes[i] = new ImageView(choosenShip.getUrlLife());
			playerLifes[i].setLayoutX(455 + (i * 50));
			playerLifes[i].setLayoutY(80);
			gamePane.getChildren().add(playerLifes[i]);
			
		}

		brownMeteors = new ImageView[3];
		for(int i = 0; i < brownMeteors.length; i++) {
			brownMeteors[i] = new ImageView(METEOR_BROWN_IMAGE);
			setNewElementPosition(brownMeteors[i]);
			gamePane.getChildren().add(brownMeteors[i]);
		}



		greyMeteors = new ImageView[3];
		for(int i = 0; i < greyMeteors.length; i++) {
			greyMeteors[i] = new ImageView(METEOR_GREY_IMAGE);
			setNewElementPosition(greyMeteors[i]);
			gamePane.getChildren().add(greyMeteors[i]);
		}


	}
	
	
	private void moveGameElements() {

		int speed = 1;

		if(this.DIFFICULTY == "medium"){
			speed = 2;
		}
		else if (this.DIFFICULTY == "hard"){
			speed = 3;
		}
	
		star.setLayoutY(star.getLayoutY() + 4*speed);
		
		for(int i = 0; i < brownMeteors.length; i++) {
			brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+4*speed);
			brownMeteors[i].setRotate(brownMeteors[i].getRotate()+4);
		}

		for(int i = 0; i < greyMeteors.length; i++) {
			greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY()+4*speed);
			greyMeteors[i].setRotate(greyMeteors[i].getRotate()+4);
		}
	}
	
	private void checkIfElementAreBehindTheShipAndRelocated() {
		
		if(star.getLayoutY() > 1200) {
			setNewElementPosition(star);
		}
		
		for(int i = 0; i< brownMeteors.length; i++) {
			if(brownMeteors[i].getLayoutY() > 900) {
				setNewElementPosition(brownMeteors[i]);
			}
		}
		
		
		for(int i = 0; i< greyMeteors.length; i++) {
			if(greyMeteors[i].getLayoutY() > 900) {
				setNewElementPosition(greyMeteors[i]);
			}
		}
	}
	
	
	
	
	
	private void setNewElementPosition(ImageView image) {

		image.setLayoutX(randomPositionGenerator.nextInt(GAME_WIDTH));
		image.setLayoutY(-randomPositionGenerator.nextInt(3200)+600);

	}
	

	
	
	private void createShip(SHIP choosenShip) {
		ship = new ImageView(choosenShip.getUrl());
		ship.setLayoutX(GAME_WIDTH/2);
		ship.setLayoutY(GAME_HEIGHT - 90);
		gamePane.getChildren().add(ship);
	}

	private void createShot(){
		ImageView shot = new ImageView(shotImage);


		shot.setLayoutX(ship.getLayoutX());
		shot.setLayoutY(ship.getLayoutY()-50);
		shots.add(shot);


		gamePane.getChildren().add(shot);
	}
	private void updateShot(){
		for(int i = 0; i < shots.size(); i++) {
			moveShots();
//			setNewElementPosition(shots.get(i));;
		}
	}
	
	
	private void createGameLoop() {

		gameTimer = new AnimationTimer() {


			@Override
			public void handle(long now) {

				moveBackground();
				moveGameElements();
				checkIfElementAreBehindTheShipAndRelocated();
				try {
					checkIfElementsCollide();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}

				if(now - timerDelay >= 200_000_000){
					shoot();
					timerDelay = now;
				}

				moveShip();



				updateShot();

			}
			
		};

		gameTimer.start();


	}
	
	private void moveShip() {
		
		if (isLeftKeyPressed && !isRightKeyPressed) {
			if(angle > -30) {
				angle -= 5;
			}
			ship.setRotate(angle);
			if(ship.getLayoutX() > -20) {
				ship.setLayoutX(ship.getLayoutX() - 3);
			}
		}
		
		if (isRightKeyPressed && !isLeftKeyPressed) {
			if(angle < 30) {
				angle += 5;
			}
			ship.setRotate(angle);
			if(ship.getLayoutX() < GAME_WIDTH-50) {
				ship.setLayoutX(ship.getLayoutX() + 3);
			}
		}
		
		if (!isLeftKeyPressed && !isRightKeyPressed) {
			if(angle < 0) {
				angle += 5;
			} else if (angle > 0) {
				angle -= 5;
			}
			ship.setRotate(angle);
		}
		
		if (isLeftKeyPressed &&  isRightKeyPressed) {
			if(angle < 0) {
				angle += 5;
			} else if (angle > 0) {
				angle -= 5;
			}
			ship.setRotate(angle);
		}
		
	}

	private void shoot(){

		if (isSpaceKeyPressed) {
			createShot();
		}
	}

	private void moveShots(){
		for(int i = 0; i< shots.size(); i++) {
			if(shots.get(i).getLayoutY() > -70) {
				shots.get(i).setLayoutY(shots.get(i).getLayoutY()-5);
			}
			else {
				shots.remove(i);
			}
		}
	}


	
	private void createBackground() {
		gridPane1 = new GridPane();
		gridPane2 = new GridPane();
		
		for (int i = 0 ; i < 16; i++) {
			ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
			ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);
			GridPane.setConstraints(backgroundImage1, i% 4, i / 4 );
			GridPane.setConstraints(backgroundImage2, i% 4, i / 4 );
			gridPane1.getChildren().add(backgroundImage1);
			gridPane2.getChildren().add(backgroundImage2);
		}
		
		gridPane2.setLayoutY(- 1024);
		gamePane.getChildren().addAll(gridPane1, gridPane2);
	}
	
	private void moveBackground() {
		gridPane1.setLayoutY(gridPane1.getLayoutY() + 0.5);
		gridPane2.setLayoutY(gridPane2.getLayoutY() + 0.5);
		
		if (gridPane1.getLayoutY() >= 1024) {
			gridPane1.setLayoutY(-1024);
		}
		
		if (gridPane2.getLayoutY() >= 1024) {
			gridPane2.setLayoutY(-1024);
		}
	}

	private void checkIfElementsCollide() throws SQLException {
		String textToSet = "POINTS : ";
		if (points < 10) {
			textToSet = textToSet + "0";
		}

		if(SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX() + 49, star.getLayoutX() + 15,
				ship.getLayoutY() + 37, star.getLayoutY() + 15)) {
			setNewElementPosition(star);

			points = points + 3;


			pointsLabel.setText(textToSet + points);
		}

		for (int i = 0; i < brownMeteors.length; i++) {

			if (METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, brownMeteors[i].getLayoutX() + 20,
					ship.getLayoutY() + 37, brownMeteors[i].getLayoutY() + 20)) {

				removeLife();
				setNewElementPosition(brownMeteors[i]);
			}
			for (int j = 0; j< shots.size();j++){
				if (METEOR_RADIUS + BILLET_RADIUS > calculateDistance(shots.get(j).getLayoutX() + 20, brownMeteors[i].getLayoutX() + 20,
						shots.get(j).getLayoutY() + 37, brownMeteors[i].getLayoutY() + 32)) {
					gamePane.getChildren().remove(shots.get(j));
					shots.remove(j);
					setNewElementPosition(brownMeteors[i]);
					points++;
					pointsLabel.setText(textToSet + points);

				}
			}

		}

		for (int i = 0; i < greyMeteors.length; i++) {

			if (METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, greyMeteors[i].getLayoutX() + 20,
					ship.getLayoutY() + 37, greyMeteors[i].getLayoutY() + 20)) {

				removeLife();
				setNewElementPosition(greyMeteors[i]);
			}
			for (int j = 0; j< shots.size();j++){
				if (METEOR_RADIUS + BILLET_RADIUS > calculateDistance(shots.get(j).getLayoutX() + 20, greyMeteors[i].getLayoutX() + 20,
						shots.get(j).getLayoutY() + 37, greyMeteors[i].getLayoutY() + 32)) {

					gamePane.getChildren().remove(shots.get(j));
					shots.remove(j);
					setNewElementPosition(greyMeteors[i]);
					points++;
					pointsLabel.setText(textToSet + points);
				}
			}

		}
	}


	private void removeLife() throws SQLException {

		gamePane.getChildren().remove(playerLifes[playerLife]);
		playerLife--;
		if(playerLife < 0) {

			gameStage.close();
			gameTimer.stop();
			updateScore();
			Alert a = new Alert(Alert.AlertType.CONFIRMATION);
			a.setHeaderText("Your Score : "+PLAYER.getName()+" ! "+points);
			a.setContentText("Would you like to play again ?");
			ButtonType Yes = new ButtonType("YES");
			ButtonType Home = new ButtonType("HOME");

			a.getButtonTypes().clear();
			a.getButtonTypes().addAll(Yes,Home);
			a.show();
			a.setOnHidden(evt -> createNewGame(this.menuStage, this.choosership,this.PLAYER,this.DIFFICULTY));

			//createNewGame(this.menuStage, this.choosership,this.PLAYER,this.DIFFICULTY);
			menuStage.show();
		}
	}

	private void updateScore() throws SQLException {
		if(PLAYER.getScore() < points) {
			Connection connection = SinglotonConnectionDB.getConnection();
			PreparedStatement stm = connection.prepareStatement("update USER set SCORE=? where NAME=?");
			stm.setInt(1,points);
			stm.setString(2, PLAYER.getName());
			stm.executeUpdate();
			System.out.println("New High Score "+PLAYER.getName()+" ! ");
			System.out.println(points);
		}


	}
	
	private double calculateDistance(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

}
