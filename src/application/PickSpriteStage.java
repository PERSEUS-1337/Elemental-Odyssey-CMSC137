package application;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import user.Main;

public class PickSpriteStage extends VBox {
    private Button btnPickCandy;
    private Button btnPickIce;
    private Button btnPickSlime;
    private Button btnPickWood;
    private Stage pickSpriteStage;

    private String nameOfUser;
    private String ipAddress;
    private String chatType;

    // Constructor
    public PickSpriteStage(String nameOfUser, String ipAddress, String chatType) {
        this.nameOfUser = nameOfUser;
        this.ipAddress = ipAddress;
        this.chatType = chatType;

    }

    // method to close the Pick Sprite Stage
    private void closePickSpriteStage() {
        Stage stage = (Stage) btnPickCandy.getScene().getWindow();
        stage.close();
    }

    // method to set the stage for the Pick Sprite Stage
    void setStage(Stage primaryStage) {
        this.pickSpriteStage = primaryStage;
        VBox root = new VBox();

        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(391, 98);

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("../assets/background/pickSpritesBackground.jpg")));
        background.setFitHeight(98.0);
        background.setFitWidth(391.0);
        background.setPickOnBounds(true);

        btnPickCandy = createButton("../assets/players/CandySprite_Idle.gif");
        btnPickCandy.setLayoutX(25);
        btnPickCandy.setLayoutY(18);

        btnPickIce = createButton("../assets/players/IceSprite_Idle.gif");
        btnPickIce.setLayoutX(116);
        btnPickIce.setLayoutY(18);

        btnPickSlime = createButton("../assets/players/SlimeSprite_Idle.gif");
        btnPickSlime.setLayoutX(209);
        btnPickSlime.setLayoutY(18);

        btnPickWood = createButton("../assets/players/WoodSprite_Idle.gif");
        btnPickWood.setLayoutX(301);
        btnPickWood.setLayoutY(18);

        // Set the onAction event for the buttons
        btnPickCandy.setOnAction(e -> {
            onClickedSpriteCandy(e);
        });

        btnPickIce.setOnAction(e -> {
            onClickedSpriteIce(e);
        });

        btnPickSlime.setOnAction(e -> {
            onClickedSpriteSlime(e);
        });

        btnPickWood.setOnAction(e -> {
            onClickedSpriteWood(e);
        });

        anchorPane.getChildren().addAll(background, btnPickCandy, btnPickIce, btnPickSlime, btnPickWood);
        root.getChildren().addAll(anchorPane);

        // Create the scene
        Scene scene = new Scene(root);
        // Set the scene
        pickSpriteStage.setScene(scene);
        // Set the title of the stage
        pickSpriteStage.setTitle("Pick Your Sprite");
        // Set the stage to not be resizable
        pickSpriteStage.setResizable(false);
        // Change the modality of the stage to APPLICATION_MODAL
        pickSpriteStage.initModality(Modality.APPLICATION_MODAL);
        // Show the stage
        pickSpriteStage.show();



    }

    // method to create a button with an image
    private Button createButton(String imageUrl) {
        Button button = new Button();
        button.setPrefSize(65, 65);
        button.setFont(new Font("Bookman Old Style", 12));
        // Set the background of the button to a bit transparent
        button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imageUrl)));
        imageView.setFitHeight(59);
        imageView.setFitWidth(62);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);

        return button;
    }

    // method to handle the event when the Candy Sprite button is clicked
    void onClickedSpriteCandy(ActionEvent event) {
        System.out.println("Candy Sprite Clicked");
        // Create the Chat GUI
        this.createChatGUI();
    }

    // method to handle the event when the Ice Sprite button is clicked
    void onClickedSpriteIce(ActionEvent event) {
        System.out.println("Ice Sprite Clicked");
        // Create the Chat GUI
        this.createChatGUI();

    }

    // method to handle the event when the Slime Sprite button is clicked
    void onClickedSpriteSlime(ActionEvent event) {
        System.out.println("Slime Sprite Clicked");
        // Create the Chat GUI
        this.createChatGUI();

    }

    // method to handle the event when the Wood Sprite button is clicked
    void onClickedSpriteWood(ActionEvent event) {
        System.out.println("Wood Sprite Clicked");
        // Create the Chat GUI
        this.createChatGUI();

    }

    // method to create an Instance of the Chat GUI
    private void createChatGUI() {
        ChatGUI chatGUI = new ChatGUI(chatType, nameOfUser, ipAddress);

        // Stop the music from MainGUIController
        MainGUIController.stopBackgroundMusic();
        //  Close the Game Mode Menu
        MainGUIController.closeGameModeMenu();
        // Close the Main Menu
        Main.closeMainGUI();
        // Close the Pick Sprite Stage
        this.closePickSpriteStage();

        // If the chat gui has been closed, close its socket
        chatGUI.getStage().setOnCloseRequest(e -> {
            chatGUI.closeSocket();
        });
    }

}
