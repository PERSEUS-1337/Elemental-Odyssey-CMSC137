package application;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MovingBackground extends Pane {

    // Colored images for the background
    public final Image blueBg = new Image(getClass().getResource("/assets/background/blue.png").toExternalForm());
    public final Image brownBg = new Image(getClass().getResource("/assets/background/brown.png").toExternalForm());
    public final Image grayBg = new Image(getClass().getResource("/assets/background/gray.png").toExternalForm());
    public final Image greenBg = new Image(getClass().getResource("/assets/background/green.png").toExternalForm());
    public final Image pinkBg = new Image(getClass().getResource("/assets/background/pink.png").toExternalForm());
    public final Image purpleBg = new Image(getClass().getResource("/assets/background/purple.png").toExternalForm());
    public final Image yellowBg = new Image(getClass().getResource("/assets/background/yellow.png").toExternalForm());

    public static final String blueColor = "blue";
    public static final String brownColor = "brown";
    public static final String grayColor = "gray";
    public static final String greenColor = "green";
    public static final String pinkColor = "pink";
    public static final String purpleColor = "purple";
    public static final String yellowColor = "yellow";

    public static final Integer defaultWindowSize = 0;
    public static final Integer adjustedBackgroundSize = 10000; 


    public MovingBackground(String color, Integer setWidth) {

        Image bgImg = null;
        switch (color) {
            case MovingBackground.blueColor:
                bgImg = blueBg;
                break;
            case MovingBackground.brownColor:
                bgImg = brownBg;
                break;
            case MovingBackground.grayColor:
                bgImg = grayBg;
                break;
            case MovingBackground.greenColor:
                bgImg = greenBg;
                break;
            case MovingBackground.pinkColor:
                bgImg = pinkBg;
                break;
            case MovingBackground.purpleColor:
                bgImg = purpleBg;
                break;
            case MovingBackground.yellowColor:
                bgImg = yellowBg;
                break;
            default:
                break;
        }

        ImageView background1 = new ImageView(bgImg);
        ImageView background2 = new ImageView(bgImg);

        // If not default size, we set the proper background window size for a particular level
        if(setWidth!=MovingBackground.defaultWindowSize){
            background1.setFitWidth(setWidth);
            background2.setFitWidth(setWidth);
            // Since the width is stretched due to a custom width, we also need to set the window height
            background1.setFitHeight(MovingBackground.adjustedBackgroundSize);
            background2.setFitHeight(MovingBackground.adjustedBackgroundSize);
        } 


        background1.setY(-7200);
        background2.setY(-7200);

        getChildren().addAll(background2, background1);

        TranslateTransition tt1 = new TranslateTransition(Duration.seconds(5), background1);
        tt1.setByY(bgImg.getHeight());
        tt1.setRate(0.05);
        tt1.setCycleCount(TranslateTransition.INDEFINITE);
        tt1.play();

        TranslateTransition tt2 = new TranslateTransition(Duration.seconds(5), background2);
        tt2.setByY(bgImg.getHeight());
        tt2.setRate(0.05);
        tt2.setCycleCount(TranslateTransition.INDEFINITE);
        tt2.setDelay(Duration.seconds(30));
        tt2.play();
    }
}