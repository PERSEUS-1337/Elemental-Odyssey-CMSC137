package sprites.objects;

import javafx.scene.image.Image;
import sprites.Sprite;

public class TerrainSprite extends Sprite {
    private String name;
    public final static String SPRITE_NAME = "TerrainSprite";
    public final static Image SPRITE_IMAGE = new Image("assets/objects/TerrainSprite_1.png", 
            Sprite.SPRITE_WIDTH, Sprite.SPRITE_WIDTH, false,
            false);

    public TerrainSprite(String name, int x, int y) {
        super(x, y);
        // this.name = name;

        this.loadImage(TerrainSprite.SPRITE_IMAGE);
    }

    String getName() {
        return this.name;
    }

}