package sprites.objects;

import javafx.scene.image.Image;
import sprites.Sprite;

/**
 * TerrainSprite
 */
public class TerrainSprite extends Sprite {
    private String name;

    public final static int SPRITE_WIDTH = 50;
    public final static Image SPRITE_IMAGE = new Image("assets/TerrainSprite.png", 
            TerrainSprite.SPRITE_WIDTH, TerrainSprite.SPRITE_WIDTH, false,
            false);

    public final static int MAX_SPRITE_STRENGTH = 150;
    public final static int MIN_SPRITE_STRENGTH = 100;

    public TerrainSprite(String string, int x, int y) {
        super(x, y);
        this.name = name;

        this.loadImage(TerrainSprite.SPRITE_IMAGE);
    }

    String getName() {
        return this.name;
    }

}