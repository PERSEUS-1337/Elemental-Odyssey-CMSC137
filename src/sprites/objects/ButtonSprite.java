package sprites.objects;

import javafx.scene.image.Image;
import sprites.Sprite;

public class ButtonSprite extends Sprite {
    private String name;
    public final static String SPRITE_NAME = "ButtonSprite";
    public final static Image SPRITE_IMAGE = new Image("assets/objects/ButtonSprite.png",
            Sprite.SPRITE_WIDTH, Sprite.SPRITE_WIDTH, false,
            false);

    public ButtonSprite(String name, int x, int y) {
        super(x, y);

        this.loadImage(ButtonSprite.SPRITE_IMAGE);
    }

    String getName() {
        return this.name;
    }
    
}
