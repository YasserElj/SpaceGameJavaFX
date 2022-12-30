package view;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Controllers.UserController;
import Dao.SinglotonConnectionDB;
import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;

import javax.xml.soap.Text;


public class ViewManager {
	
	private static final int HEIGHT = 768;
	private static final int WIDTH = 1024;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private final static int MENU_BUTTON_START_X = 100;
	private final static int MENU_BUTTON_START_Y = 150;
	
	
	private SpaceRunnerSubScene creditsSubscene;
	private SpaceRunnerSubScene helpSubscene;
	private SpaceRunnerSubScene scoreSubscene;
	private SpaceRunnerSubScene shipChooserSubscene;

	private SpaceRunnerSubScene userChooserSubscene;
	
	private SpaceRunnerSubScene sceneToHide;
	
	private static User user;

	private TableView tableView = new TableView();

	List<SpaceRunnerButton> menuButtons;
	
	List<ShipPicker> shipsList;

	List<User> users=new ArrayList<>();

	private String newUser = "fdghdhg";


	private SHIP choosenShip;
	
	public ViewManager() {
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, WIDTH, HEIGHT );
		mainStage = new Stage();
		mainStage.setScene(mainScene);
		createSubScenes();
		CreateButtons();
		createBackground();
		createLogo();
	
		
	}
	
	
	private void showSubScene(SpaceRunnerSubScene subScene) {
		if (sceneToHide != null) {
			sceneToHide.moveSubScene();
		}
		
		subScene.moveSubScene();
		sceneToHide = subScene;
	}
	
	
	
	
	private void createSubScenes() {
		
		creditsSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(creditsSubscene);
		helpSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(helpSubscene);
		scoreSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(scoreSubscene);
		createShipChooserSubScene();
		createUserChooserSubScene();
	
	}

	private void createShipChooserSubScene() {
		shipChooserSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(shipChooserSubscene);

		InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
		chooseShipLabel.setLayoutX(110);
		chooseShipLabel.setLayoutY(25);
		shipChooserSubscene.getPane().getChildren().add(chooseShipLabel);
		shipChooserSubscene.getPane().getChildren().add(createShipsToChoose());
		shipChooserSubscene.getPane().getChildren().add(createButtonToStart());



	}

	private HBox createShipsToChoose() {
		HBox box = new HBox();
		box.setSpacing(60);
		shipsList = new ArrayList<>();
		for (SHIP ship : SHIP.values()) {
			ShipPicker shipToPick = new ShipPicker(ship);
			shipsList.add(shipToPick);
			box.getChildren().add(shipToPick);
			shipToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					for (ShipPicker ship : shipsList) {
						ship.setIsCircleChoosen(false);
					}
					shipToPick.setIsCircleChoosen(true);
					choosenShip = shipToPick.getShip();

				}
			});
		}

		box.setLayoutX(300 - (118*2));
		box.setLayoutY(100);
		return box;
	}

	
	private void createUserChooserSubScene() {
		userChooserSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(userChooserSubscene);
		
		InfoLabel chooseUserLabel = new InfoLabel("CHOOSE YOUR PROFILE");
		chooseUserLabel.setLayoutX(110);
		chooseUserLabel.setLayoutY(25);
		userChooserSubscene.getPane().getChildren().add(chooseUserLabel);
		userChooserSubscene.getPane().getChildren().add(creatNewUser());
		userChooserSubscene.getPane().getChildren().add(createUserToChoose());
		userChooserSubscene.getPane().getChildren().add(addUser());
		userChooserSubscene.getPane().getChildren().add(createButtonToContinue());

		

	}

	private TextField creatNewUser(){
		TextField textField = new TextField();
		textField.setLayoutX(300 );
		textField.setLayoutY(100);
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			newUser=newValue;

		});

		return textField;
	}
	private TableView createUserToChoose() {

		TableColumn<User, String> NameColumn = new TableColumn<>("Name");
		NameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

		TableColumn<User, Integer> Score = new TableColumn<>("Score");
		Score.setCellValueFactory(new PropertyValueFactory<>("Score"));
		tableView.getColumns().add(NameColumn);
		tableView.getColumns().add(Score);
		Connection connection= SinglotonConnectionDB.getConnection();
		try {
			Statement st=connection.createStatement();
			st.execute("SELECT * FROM User");
			ResultSet rs=st.getResultSet();
			while (rs.next()){
				tableView.getItems().add(new User(rs.getString("name"),rs.getInt("score")));
				users.add(new User(rs.getString("name"),rs.getInt("score")));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}



		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				user = (User) tableView.getSelectionModel().getSelectedItem();
				System.out.println(user.getName());
			}
		});

		tableView.setLayoutX(300 - (118*2));
		tableView.setLayoutY(100);
		return tableView;
	}

	private SpaceRunnerButton createButtonToContinue() {
		SpaceRunnerButton startButton = new SpaceRunnerButton("CONTINUE");
		startButton.setLayoutX(350);
		startButton.setLayoutY(300);


		startButton.setOnAction(new EventHandler<ActionEvent>() {



			@Override
			public void handle(ActionEvent event) {

					GameViewManager gameManager = new GameViewManager();
					showSubScene(shipChooserSubscene);


			}
		});

		return startButton;
	}

	private SpaceRunnerButton addUser( ) {
		SpaceRunnerButton startButton = new SpaceRunnerButton("SAVE");
		startButton.setLayoutX(350);
		startButton.setLayoutY(100);
		TextField textField = new TextField();


		startButton.setOnAction(new EventHandler<ActionEvent>() {



			@Override
			public void handle(ActionEvent event) {

				User NewUser = new User(newUser,0);
				users.add(NewUser);
				tableView.getItems().add(NewUser);

			}
		});

		return startButton;
	}
	
	
	
	private SpaceRunnerButton createButtonToStart() {
		SpaceRunnerButton startButton = new SpaceRunnerButton("START");
		startButton.setLayoutX(350);
		startButton.setLayoutY(300);
		
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			

			@Override
			public void handle(ActionEvent event) {
				if (choosenShip != null) {
					GameViewManager gameManager = new GameViewManager();
					gameManager.createNewGame(mainStage, choosenShip);;
				}
				
			}
		});
		
		return startButton;
	}
	
	
	
	


	public Stage getMainStage() {
		return mainStage;
	}
	
	private void AddMenuButtons(SpaceRunnerButton button) {
		button.setLayoutX(MENU_BUTTON_START_X);
		button.setLayoutY(MENU_BUTTON_START_Y + menuButtons.size() * 100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}
	
	
	
	private void CreateButtons() {
		createStartButton();
		createScoresButton();
		createHelpButton();
		createCreditsButton();
		createExitButton();
	}
	
	private void createStartButton() {
		SpaceRunnerButton startButton = new SpaceRunnerButton("PLAY");
		AddMenuButtons(startButton);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(userChooserSubscene);
				
			}
		});
	}
	
	private void createScoresButton() {
		SpaceRunnerButton scoresButton = new SpaceRunnerButton("SCORES");
		AddMenuButtons(scoresButton);
		
		scoresButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(scoreSubscene);
				
			}
		});
	}
	
	private void createHelpButton() {
		SpaceRunnerButton helpButton = new SpaceRunnerButton("HELP");
		AddMenuButtons(helpButton);
		
		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(helpSubscene);
				
			}
		});
	}
	
	private void createCreditsButton() {
		
		SpaceRunnerButton creditsButton = new SpaceRunnerButton("CREDITS");
		AddMenuButtons(creditsButton);
		
		creditsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(creditsSubscene);
				
			}
		});
	}
	
	private void createExitButton() {
		SpaceRunnerButton exitButton = new SpaceRunnerButton("EXIT");
		AddMenuButtons(exitButton);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainStage.close();
				
			}
		});
		
	}
	
	private void createBackground() {
		Image backgroundImage = new Image("/resources/deep_blue.png", 256, 256, false, false);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
	}
	
	private void createLogo() {
		ImageView logo = new ImageView("/resources/space_runner.png");
		logo.setLayoutX(380);
		logo.setLayoutY(50);
		
		logo.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(new DropShadow());
				
			}
		});
		
		logo.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(null);
				
			}
		});
		
		mainPane.getChildren().add(logo);
		
	}
	
	
	
	

}
