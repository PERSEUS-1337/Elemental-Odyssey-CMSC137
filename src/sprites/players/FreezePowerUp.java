package sprites.players;

public class FreezePowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        player.setSpeed(4);
    }

    @Override
    public void deactivate(PlayerSprite player) {
        player.setSpeed(0);
    }

}