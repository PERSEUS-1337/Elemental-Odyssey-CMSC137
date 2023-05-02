package sprites.objects;

import javafx.scene.image.Image;
import sprites.Sprite;

public class DoorSprite extends Sprite {
    private String name;

    public final static Image SPRITE_IMAGE = new Image("assets/objects/DoorSprite_Closed.png", 
            Sprite.SPRITE_WIDTH, Sprite.SPRITE_WIDTH, false,
            false);

    public DoorSprite(String name, int x, int y) {
        super(x, y);
        // this.name = name;

        this.loadImage(DoorSprite.SPRITE_IMAGE);
    }

    String getName() {
        return this.name;
    }

}