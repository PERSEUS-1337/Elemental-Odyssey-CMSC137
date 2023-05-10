package sprites.objects;

import javafx.scene.image.Image;
import sprites.Sprite;

public class CrateSprite extends Sprite {
    private String name;
    public final static String SPRITE_NAME = "CrateSprite";
    public final static Image SPRITE_IMAGE = new Image("assets/objects/CrateSprite_1.png", 
            Sprite.SPRITE_WIDTH, Sprite.SPRITE_WIDTH, false,
            false);

    public CrateSprite(String name, int x, int y) {
        super(x, y);
        // this.name = name;

        this.loadImage(CrateSprite.SPRITE_IMAGE);
    }

    String getName() {
        return this.name;
    }

}