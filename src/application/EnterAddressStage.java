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

public class EnterAddressStage {
    private TextField txtAddress;
    private Button btnEnterAddress;
    private String chatType;
    static String ipAddress;
    public static final Integer WINDOW_WIDTH = 582;
    public static final Integer WINDOW_HEIGHT = 129;

    // Constructor
    public EnterAddressStage(String chatType) {
        this.chatType = chatType;
    }

    void onButtonClickedSetAddress() {
        System.out.println("Set Address Button Clicked");

        // Get the address from the text field
        String address = txtAddress.getText();

        // The address should not be empty and should resemble an ip address. If it does not, then it is not a valid address
        if (address.isEmpty() || !address.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}")) {
            System.out.println("Enter Address Stage: Invalid Address");
            
            // If the address is invalid, then clear the text field
            txtAddress.clear();
            return;
        } else {
            // If the address is valid, then set the ip address
            ipAddress = address;
            System.out.println("Enter Address Stage: Address Set");
            System.out.println("Enter Address Stage: Address: " + ipAddress);

            // if the chatType is SERVER, then check if the ipAddress is already in use
            if (chatType.equals(ChatGUI.SERVER)) {
                // if the ipAddress is already in use, then display an error message
                if (ChatGUI.isServerRunning(ipAddress)) {
                    System.out.println("Enter Address Stage: Server is already running");
                    // Close the Enter Name Stage if ip address is already in use
                    this.closeEnterAddressStage();
                    return;
                }
            }

            EnterNameStage enterNameStage = new EnterNameStage(ipAddress, chatType);
            enterNameStage.setStage(new Stage());

            // Close the Enter Address Stage
            this.closeEnterAddressStage();

        }
    }

    // method to get the ip address
    static String getIpAddress() {
        return ipAddress;
    }

    // method to close the Enter Address Stage
    private void closeEnterAddressStage() {
        Stage stage = (Stage) btnEnterAddress.getScene().getWindow();
        stage.close();
    }

    // method to set the stage
    void setStage(Stage primaryStage) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(582, 129);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(582, 137);

        txtAddress = new TextField();
        txtAddress.setLayoutX(37);
        txtAddress.setLayoutY(41);
        txtAddress.setPrefSize(310, 45);
        txtAddress.setPromptText("Please enter the chat room IP address");
        txtAddress.setFont(new Font("Bookman Old Style", 15));

        btnEnterAddress = new Button("Enter");
        btnEnterAddress.setLayoutX(371);
        btnEnterAddress.setLayoutY(41);
        btnEnterAddress.setPrefSize(167, 46);
        btnEnterAddress.setFont(new Font("Bookman Old Style", 16));

        ImageView lowerLeftCorner = createImageView("../assets/blocks/lowerLeftCorner_Iron.png", 39, 67, 0, 100);
        ImageView lowerRightCorner = createImageView("../assets/blocks/lowerRightCorner_Iron.png", 39, 67, 543, 100);
        ImageView upperRightCorner = createImageView("../assets/blocks/upperRightCorner_Iron.png", 39, 67, 543, -1);
        ImageView upperLeftCorner = createImageView("../assets/blocks/upperLeftCorner_Iron.png", 39, 67, 0, 0);
        ImageView longHorizontalBorder1 = createImageView("../assets/blocks/longHorizontalIronBorder.png", 504, 19, 39, 0);
        ImageView longHorizontalBorder2 = createImageView("../assets/blocks/longHorizontalIronBorder.png", 504, 19, 39, 124);
        ImageView longVerticalBorder1 = createImageView("../assets/blocks/longVerticalIronBorder.png", 62, 69, 0, 30);
        ImageView longVerticalBorder2 = createImageView("../assets/blocks/longVerticalIronBorder.png", 13, 75, 581, 25);


        btnEnterAddress.setOnAction(e -> {
            onButtonClickedSetAddress();
        });

        MovingBackground bg = new MovingBackground(MovingBackground.blueColor, MovingBackground.defaultWindowSize);
        anchorPane.getChildren().addAll(
                txtAddress, btnEnterAddress, lowerLeftCorner, lowerRightCorner, upperRightCorner, upperLeftCorner,
                longHorizontalBorder1, longHorizontalBorder2, longVerticalBorder1, longVerticalBorder2
        );

        root.getChildren().addAll(bg, anchorPane);

        Scene scene = new Scene(root);
        primaryStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
        primaryStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
        primaryStage.setScene(scene);
        primaryStage.setTitle("Enter the IP Address");
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