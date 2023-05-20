package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EnterNameStage {
    private TextField txtName;
    private Button btnEnterName;
    private String chatType;
    private String nameOfUser;
    static String ipAddress;
    public static final Integer WINDOW_WIDTH = 582;
    public static final Integer WINDOW_HEIGHT = 129;
    
    // Constructor
    public EnterNameStage(String ipAddress, String chatType) {
        this.chatType = chatType;
    }

    // method to close the Enter Address Stage
    private void closeEnterAddressStage() {
        Stage stage = (Stage) btnEnterName.getScene().getWindow();
        stage.close();
    }

    void onButtonClickedSetName() {
        System.out.println("Set Name Button Clicked");

        nameOfUser = txtName.getText();

        ChatGUI chatGUI = new ChatGUI(chatType, nameOfUser, ipAddress);

        // Close the Enter Name Stage
        this.closeEnterAddressStage();

        // Close the Game Mode Menu
        MainGUIController.closeGameModeMenu();

        // If the chat gui has been closed, close its socket
        chatGUI.getStage().setOnCloseRequest(e -> {
            chatGUI.closeSocket();
        });
    }

     // method to set the stage
     void setStage(Stage primaryStage) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(582, 129);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(582, 137);

        txtName = new TextField();
        txtName.setLayoutX(37);
        txtName.setLayoutY(41);
        txtName.setPrefSize(310, 45);
        txtName.setPromptText("Please enter the chat room IP address");
        txtName.setFont(new Font("Bookman Old Style", 15));

        btnEnterName = new Button("Enter");
        btnEnterName.setLayoutX(371);
        btnEnterName.setLayoutY(41);
        btnEnterName.setPrefSize(167, 46);
        btnEnterName.setFont(new Font("Bookman Old Style", 16));

        ImageView lowerLeftCorner = createImageView("../assets/blocks/lowerLeftCorner_Iron.png", 39, 67, 0, 100);
        ImageView lowerRightCorner = createImageView("../assets/blocks/lowerRightCorner_Iron.png", 39, 67, 543, 100);
        ImageView upperRightCorner = createImageView("../assets/blocks/upperRightCorner_Iron.png", 39, 67, 543, -1);
        ImageView upperLeftCorner = createImageView("../assets/blocks/upperLeftCorner_Iron.png", 39, 67, 0, 0);
        ImageView longHorizontalBorder1 = createImageView("../assets/blocks/longHorizontalIronBorder.png", 504, 19, 39, 0);
        ImageView longHorizontalBorder2 = createImageView("../assets/blocks/longHorizontalIronBorder.png", 504, 19, 39, 124);
        ImageView longVerticalBorder1 = createImageView("../assets/blocks/longVerticalIronBorder.png", 62, 69, 0, 30);
        ImageView longVerticalBorder2 = createImageView("../assets/blocks/longVerticalIronBorder.png", 13, 75, 581, 25);


        btnEnterName.setOnAction(e -> {
            onButtonClickedSetName();
        });

        MovingBackground bg = new MovingBackground(MovingBackground.blueColor, MovingBackground.defaultWindowSize);
        anchorPane.getChildren().addAll(
                txtName, btnEnterName, lowerLeftCorner, lowerRightCorner, upperRightCorner, upperLeftCorner,
                longHorizontalBorder1, longHorizontalBorder2, longVerticalBorder1, longVerticalBorder2
        );

        root.getChildren().addAll(bg, anchorPane);

        Scene scene = new Scene(root);
        primaryStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
        primaryStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
        primaryStage.setScene(scene);
        primaryStage.setTitle("Enter Your Name");
        primaryStage.show();
    }

    // method to set an ImageView component
    private ImageView createImageView(String imageUrl, double fitWidth, double fitHeight, double layoutX, double layoutY) {
        Image image = new Image(getClass().getResourceAsStream(imageUrl));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
