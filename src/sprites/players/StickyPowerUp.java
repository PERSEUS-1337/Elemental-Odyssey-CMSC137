package sprites.players;

public class StickyPowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        player.setSpeed(2);
    }

    @Override
    public void deactivate(PlayerSprite player) {
        player.setSpeed(0);
    }

}