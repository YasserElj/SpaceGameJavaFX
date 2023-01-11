package view;


import Dao.SinglotonConnectionDB;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ViewManager {

	private static final int HEIGHT = 768;
	private static final int WIDTH = 1024;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;

	private final static int MENU_BUTTON_START_X = 100;
	private final static int MENU_BUTTON_START_Y = 250;


	private SpaceRunnerSubScene creditsSubscene;
	private SpaceRunnerSubScene helpSubscene;
	private SpaceRunnerSubScene scoreSubscene;
	private SpaceRunnerSubScene shipChooserSubscene;
	private SpaceRunnerSubScene difficultyChooserSubscene;

	private SpaceRunnerSubScene userChooserSubscene;
	private SpaceRunnerSubScene NewUserSubscene;

	private SpaceRunnerSubScene sceneToHide;

	private static User user=null;

	private String DIFFICULTY ="EASY";

	private TableView NametableView = new TableView();
	private TableView NameScoretableView = new TableView();

	List<SpaceRunnerButton> menuButtons;

	List<ShipPicker> shipsList;

	List<User> users=new ArrayList<>();

	private String newUser = "";


	private SHIP choosenShip;



	public ViewManager() throws IOException {
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, WIDTH, HEIGHT );
		mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
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




	private void createSubScenes() throws IOException {

		creditsSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(creditsSubscene);
		helpSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(helpSubscene);

		createscoreSubScene();
		createUserChooserSubScene();
		createNewUserSubScene();
		createShipChooserSubScene();
		createCreditsSubScene();
		createdifficultyChooserSubscene();
	}

	private void createdifficultyChooserSubscene() throws IOException {
		difficultyChooserSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(difficultyChooserSubscene);

		InfoLabel chooseShipLabel = new InfoLabel("CHOOSE THE DIFFICULTY");
		chooseShipLabel.setLayoutX(110);
		chooseShipLabel.setLayoutY(25);
		difficultyChooserSubscene.getPane().getChildren().add(chooseShipLabel);
		difficultyChooserSubscene.getPane().getChildren().add(difficultyButton("easy",195,80));
		difficultyChooserSubscene.getPane().getChildren().add(difficultyButton("medium",195,150));
		difficultyChooserSubscene.getPane().getChildren().add(difficultyButton("hard",195,220));
		//	difficultyChooserSubscene.getPane().getChildren().add(createButtonToStart());
		difficultyChooserSubscene.getPane().getChildren().add(createButtonToReturn());



	}
	private void createShipChooserSubScene() throws IOException {
		shipChooserSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(shipChooserSubscene);

		InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
		chooseShipLabel.setLayoutX(110);
		chooseShipLabel.setLayoutY(25);
		shipChooserSubscene.getPane().getChildren().add(chooseShipLabel);
		shipChooserSubscene.getPane().getChildren().add(createShipsToChoose());
		shipChooserSubscene.getPane().getChildren().add(createButtonToStart());
		shipChooserSubscene.getPane().getChildren().add(createButtonToReturn());



	}

	private void createCreditsSubScene() throws IOException{
		creditsSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(creditsSubscene);

		InfoLabel credits = new InfoLabel(" MADE BY ");
		credits.setLayoutX(120);
		credits.setLayoutY(20);
		Label credit1 = new Label("MANSOURI Anas");
		Label credit2 = new Label("AJOUAD Racha");
		Label credit3 = new Label("LAÂROUSSI Tribek Soukayna");
		Label credit4 = new Label("ELJARIDA Yasser");

		credit1.getStyleClass().add("done");
		credit2.getStyleClass().add("done");
		credit3.getStyleClass().add("done");
		credit4.getStyleClass().add("done");

		VBox creditsBox = new VBox(35,credit2,credit4,credit3,credit1);
		creditsBox.getStyleClass().add("vbox");
		creditsBox.setLayoutX(0);
		creditsBox.setLayoutY(130);
		creditsBox.setPrefWidth(600);
		creditsSubscene.getPane().getChildren().addAll(credits, creditsBox);

		Application app = new Application() {@Override public void start(Stage primaryStage) throws Exception{}};
		HostServices services = app.getHostServices();

	}

	private void createscoreSubScene() throws IOException {
		scoreSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(scoreSubscene);

		InfoLabel scoreLabel = new InfoLabel("LEADERBOARD");
		scoreLabel.setLayoutX(110);
		scoreLabel.setLayoutY(25);
		scoreSubscene.getPane().getChildren().add(scoreLabel);
		scoreSubscene.getPane().getChildren().add(LeaderBoard());

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


	private void createUserChooserSubScene() throws IOException {
		userChooserSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(userChooserSubscene);

		InfoLabel chooseUserLabel = new InfoLabel("CHOOSE YOUR PROFILE");
		chooseUserLabel.setLayoutX(110);
		chooseUserLabel.setLayoutY(25);
		userChooserSubscene.getPane().getChildren().add(chooseUserLabel);
		userChooserSubscene.getPane().getChildren().add(createUserToChoose());
		userChooserSubscene.getPane().getChildren().add(createButtonToContinue());
		userChooserSubscene.getPane().getChildren().add(createButtonToAddNewUser());



	}

	private void createNewUserSubScene() throws IOException {
		NewUserSubscene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(NewUserSubscene);

		InfoLabel chooseUserLabel = new InfoLabel("ENTER YOUR NAME");
		chooseUserLabel.setLayoutX(110);
		chooseUserLabel.setLayoutY(25);
		NewUserSubscene.getPane().getChildren().add(chooseUserLabel);
		NewUserSubscene.getPane().getChildren().add(creatNewUser());
		//	NewUserSubscene.getPane().getChildren().add(createUserToChoose());
		NewUserSubscene.getPane().getChildren().add(addUser());
		NewUserSubscene.getPane().getChildren().add(createButtonToAddNewUser());
		NewUserSubscene.getPane().getChildren().add(createButtonToReturn());
		//	NewUserSubscene.getPane().getChildren().add(createButtonToSaveUser());



	}



	private TextField creatNewUser(){
		TextField textField = new TextField();
		textField.setLayoutX(100);
		textField.setLayoutY(150);
		textField.setPrefHeight(70);
		textField.setPrefWidth(400);
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			newUser=newValue;
		});
		return textField;
	}
	private TableView createUserToChoose() {
		TableColumn<User, String> NameColumn = new TableColumn<>("Name");
		NameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
		//	TableColumn<User, Integer> Score = new TableColumn<>("Score");
		//	Score.setCellValueFactory(new PropertyValueFactory<>("Score"));

		NametableView.setLayoutX(110);
		NametableView.setLayoutY(85);
		NametableView.setPrefHeight(200);
		NameColumn.setPrefWidth(400);
		//	Score.setPrefWidth(200);


		NametableView.getColumns().add(NameColumn);
		//	NametableView.getColumns().add(Score);
		ShowUsersTable(NametableView);
		NametableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				user = (User) NametableView.getSelectionModel().getSelectedItem();
				System.out.println(user.getName());
			}
		});




//		NametableView.setLayoutX(400);
//		NametableView.setLayoutY(50);
		return NametableView;
	}

	private TableView LeaderBoard() {

		TableColumn<User, String> NameColumn = new TableColumn<>("Name");
		NameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
		TableColumn<User, Integer> Score = new TableColumn<>("Score");
		Score.setCellValueFactory(new PropertyValueFactory<>("Score"));

		NameScoretableView.setLayoutX(110);
		NameScoretableView.setLayoutY(85);
		NameScoretableView.setPrefHeight(200);

		NameColumn.setPrefWidth(200);
		Score.setPrefWidth(200);


		NameScoretableView.getColumns().add(NameColumn);
		NameScoretableView.getColumns().add(Score);
		ShowUsersTable(NameScoretableView);


		return NameScoretableView;
	}



	private SpaceRunnerButton createButtonToContinue() {
		SpaceRunnerButton startButton = new SpaceRunnerButton("CONTINUE");
		startButton.setLayoutX(350);
		startButton.setLayoutY(300);


		startButton.setOnAction(new EventHandler<ActionEvent>() {



			@Override
			public void handle(ActionEvent event) {
				if(user!=null) showSubScene(difficultyChooserSubscene);
				else {
					Alert alert=new Alert(Alert.AlertType.WARNING);
					alert.setContentText("Please choose an existing player or create a new one !  ");
					alert.show();
				}




			}
		});

		return startButton;
	}

	private SpaceRunnerButton createButtonToAddNewUser() {
		SpaceRunnerButton startButton = new SpaceRunnerButton("New User");
		startButton.setLayoutX(100);
		startButton.setLayoutY(300);


		startButton.setOnAction(new EventHandler<ActionEvent>() {



			@Override
			public void handle(ActionEvent event) {
				showSubScene(NewUserSubscene);
			}
		});

		return startButton;
	}

	private SpaceRunnerButton createButtonToReturn() {
		SpaceRunnerButton startButton = new SpaceRunnerButton("Go Back");
		startButton.setLayoutX(100);
		startButton.setLayoutY(300);


		startButton.setOnAction(new EventHandler<ActionEvent>() {



			@Override
			public void handle(ActionEvent event) {
				showSubScene(userChooserSubscene);
			}
		});

		return startButton;
	}

	private SpaceRunnerButton addUser( ) {
		SpaceRunnerButton startButton = new SpaceRunnerButton("SAVE");
		startButton.setLayoutX(350);
		startButton.setLayoutY(300);
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				User NewUser = new User(newUser,0);
				users.add(NewUser);
				user=NewUser;
				NametableView.getItems().add(NewUser);

				Connection connection = SinglotonConnectionDB.getConnection();
				PreparedStatement stm = null;
				try {
					stm = connection.prepareStatement("insert into USER(NAME)" +
							" values (?)");
					stm.setString(1,NewUser.getName());
					stm.executeUpdate();
					showSubScene(shipChooserSubscene);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}



				System.out.println(user.getName());
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
					gameManager.createNewGame(mainStage, choosenShip,user,DIFFICULTY);
				}

			}
		});

		return startButton;
	}

	private SpaceRunnerButton difficultyButton(String difficulty,int X,int Y) {
		SpaceRunnerButton startButton = new SpaceRunnerButton(difficulty);
		startButton.setLayoutX(X);
		startButton.setLayoutY(Y);

		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DIFFICULTY = startButton.getText();
				System.out.println(DIFFICULTY);
				showSubScene(shipChooserSubscene);
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
		//	createHelpButton();
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

	private void ShowUsersTable(TableView T) {

		Connection connection= SinglotonConnectionDB.getConnection();
		try {
			Statement st=connection.createStatement();
			st.execute("SELECT * FROM User");
			ResultSet rs=st.getResultSet();
			while (rs.next()){
				T.getItems().add(new User(rs.getString("name"),rs.getInt("score")));
				users.add(new User(rs.getString("name"),rs.getInt("score")));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
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
		logo.setLayoutX(210);
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
