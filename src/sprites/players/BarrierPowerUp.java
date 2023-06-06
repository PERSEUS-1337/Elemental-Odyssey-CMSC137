package sprites.players;

public class BarrierPowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        player.setShield(true);
        player.addActivePowerUp(this, getDuration());
    }

    @Override
    public void deactivate(PlayerSprite player) {
        player.setShield(false);
    }

    @Override
    public int getDuration() {
        return 300;
    }

}