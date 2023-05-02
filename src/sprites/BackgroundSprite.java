package sprites;

import javafx.scene.image.Image;

public class BackgroundSprite extends Sprite {
    private String name;

    public final static Image SPRITE_IMAGE = new Image("assets/Background.png",
            Sprite.SPRITE_WIDTH, Sprite.SPRITE_WIDTH, false,
            false);


    public BackgroundSprite(String name, int x, int y) {
        super(x, y);
        //TODO Auto-generated constructor stub
        this.loadImage(BackgroundSprite.SPRITE_IMAGE);
    }

    String getName() {
        return this.name;
    }
    
    
}
