package application;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sprites.players.CandySprite;
import sprites.players.IceSprite;
import sprites.players.SlimeSprite;
import sprites.players.WoodSprite;

public class LeaderBoardStage extends Pane {

    // GameOver-related variables
    private ArrayList<String> playerRanking;
    private HashMap<String,Integer> playerTimeFinished;

    public static final int WINDOW_WIDTH = 300;
    public static final int WINDOW_HEIGHT = 300;
    
    // Coordinates of the rankings
    public static final int FIRST_PLACE_X = 275;
    public static final int FIRST_PLACE_Y = 111;
    public static final int SECOND_PLACE_X = 212;
    public static final int SECOND_PLACE_Y = 246;
    public static final int THIRD_PLACE_X = 341;
    public static final int THIRD_PLACE_Y = 257;

    // Constructor
    public LeaderBoardStage(ArrayList<String> rankings, HashMap<String,Integer> timeFinished) {
        this.playerRanking = rankings;
        this.playerTimeFinished = timeFinished;
        this.setStage();
    }

    // Method to add the stage elements
    private void setStage() {
        // Get the winners
        String firstPlace = playerRanking.get(0);
        String secondPlace = playerRanking.get(1);
        String thirdPlace = playerRanking.get(2);

        // Add the winner images
        ImageView firstPlaceImage = generateWinnerImage(firstPlace);
        ImageView secondPlaceImage = generateWinnerImage(secondPlace);
        ImageView thirdPlaceImage = generateWinnerImage(thirdPlace);

        // Set the coordinates of the winner images
        firstPlaceImage.setX(FIRST_PLACE_X);
        firstPlaceImage.setY(FIRST_PLACE_Y);
        secondPlaceImage.setX(SECOND_PLACE_X);
        secondPlaceImage.setY(SECOND_PLACE_Y);
        thirdPlaceImage.setX(THIRD_PLACE_X);
        thirdPlaceImage.setY(THIRD_PLACE_Y);

        getChildren().addAll(firstPlaceImage, secondPlaceImage, thirdPlaceImage);
    }

    private ImageView generateWinnerImage(String winnerName) {
        ImageView winnerImage = new ImageView();
        switch(winnerName) {
            case WoodSprite.SPRITE_NAME:
                winnerImage.setImage(WoodSprite.SPRITE_IMAGE);
                break;
            case CandySprite.SPRITE_NAME:
                winnerImage.setImage(CandySprite.SPRITE_IMAGE);
                break;
            case SlimeSprite.SPRITE_NAME:
                winnerImage.setImage(SlimeSprite.SPRITE_IMAGE);
                break;
            case IceSprite.SPRITE_NAME:
                winnerImage.setImage(IceSprite.SPRITE_IMAGE);
                break;
            default:
                break;
        }
        return winnerImage;
    }
    
}
