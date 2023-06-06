package sprites.players;

public class BarrierPowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        player.setShield(true);
    }

    @Override
    public void deactivate(PlayerSprite player) {
        player.setShield(false);
    }

}