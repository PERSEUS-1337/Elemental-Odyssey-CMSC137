package application;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MovingBackground extends Pane {
    public MovingBackground() {
        Image bgImg = new Image(getClass().getResource("/assets/bg.png").toExternalForm());
        ImageView background1 = new ImageView(bgImg);
        ImageView background2 = new ImageView(bgImg);

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