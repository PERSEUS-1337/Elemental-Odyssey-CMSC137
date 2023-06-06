package application;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sprites.players.CandySprite;
import sprites.players.IceSprite;
import sprites.players.SlimeSprite;
import sprites.players.WoodSprite;

public class LeaderBoardStage extends Pane {

    // GameOver-related variables
    private ArrayList<String> playerRanking;
    private HashMap<String,String> playerTimeFinished;

    public static final int WINDOW_WIDTH = 300;
    public static final int WINDOW_HEIGHT = 300;
    
    // Coordinates of the rankings
    public static final int FIRST_PLACE_X = 287;
    public static final int FIRST_PLACE_Y = 142;
    public static final int SECOND_PLACE_X = 225;
    public static final int SECOND_PLACE_Y = 276;
    public static final int THIRD_PLACE_X = 352;
    public static final int THIRD_PLACE_Y = 288;

    // Constructor
    public LeaderBoardStage(ArrayList<String> rankings, HashMap<String,String> rankingArray) {
        this.playerRanking = rankings;
        this.playerTimeFinished = rankingArray;
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

        // Add the winner time texts using the playerTimeFinished HashMap
        String firstPlaceTime = String.valueOf(playerTimeFinished.get(firstPlace));
        String secondPlaceTime = String.valueOf(playerTimeFinished.get(secondPlace));
        String thirdPlaceTime = String.valueOf(playerTimeFinished.get(thirdPlace));

        // Add the winner time texts
        Text firstPlaceTimeText = new Text(firstPlaceTime + "st");
        Text secondPlaceTimeText = new Text(secondPlaceTime + "nd");
        Text thirdPlaceTimeText = new Text(thirdPlaceTime + "rd");

        // Set the fonts of the texts to "Press Start 2P" using the Font file in the assets/fonts folder
        Font timeTextFont = Font.loadFont(getClass().getResourceAsStream("/assets/fonts/PressStart2P-Regular.ttf"), 15);
        firstPlaceTimeText.setFont(timeTextFont);
        secondPlaceTimeText.setFont(timeTextFont);
        thirdPlaceTimeText.setFont(timeTextFont);

        // Set the coordinates of the winner time texts above each winner image
        firstPlaceTimeText.setX(FIRST_PLACE_X - 5);
        firstPlaceTimeText.setY(FIRST_PLACE_Y - 12);
        secondPlaceTimeText.setX(SECOND_PLACE_X - 5);
        secondPlaceTimeText.setY(SECOND_PLACE_Y - 12);
        thirdPlaceTimeText.setX(THIRD_PLACE_X - 5);
        thirdPlaceTimeText.setY(THIRD_PLACE_Y - 12);

        getChildren().addAll(firstPlaceImage, secondPlaceImage, thirdPlaceImage, firstPlaceTimeText, secondPlaceTimeText, thirdPlaceTimeText);
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
