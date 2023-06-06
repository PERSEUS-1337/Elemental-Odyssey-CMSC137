package sprites.players;

public class SpeedPowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        player.setSpeed(-2);
        player.addActivePowerUp(this, getDuration());
    }

    @Override
    public void deactivate(PlayerSprite player) {
        player.setSpeed(0);
    }

    @Override
    public int getDuration() {
        return 300;
    }

}