package sprites.objects;

import javafx.scene.image.Image;
import sprites.Sprite;

public class LeverSprite extends Sprite {
    private String name;
    public final static String SPRITE_NAME = "LeverSprite";
    public final static Image SPRITE_IMAGE = new Image("assets/objects/LeverLeft.png",
            Sprite.SPRITE_WIDTH, Sprite.SPRITE_WIDTH, false,
            false);

    public LeverSprite(String name, int x, int y) {
        super(x, y);

        this.loadImage(LeverSprite.SPRITE_IMAGE);
    }

    String getName() {
        return this.name;
    }
    
}
