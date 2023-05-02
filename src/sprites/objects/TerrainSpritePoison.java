package sprites.objects;

import javafx.scene.image.Image;
import sprites.Sprite;

public class TerrainSpritePoison extends Sprite {
    private String name;

    public final static Image SPRITE_IMAGE = new Image("assets/objects/TerrainPoison.png", 
            Sprite.SPRITE_WIDTH, Sprite.SPRITE_WIDTH, false,
            false);

    public TerrainSpritePoison(String name, int x, int y) {
        super(x, y);
        // this.name = name;

        this.loadImage(TerrainSpritePoison.SPRITE_IMAGE);
    }

    String getName() {
        return this.name;
    }

}