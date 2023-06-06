package sprites.players;

public class FreezePowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        player.setSpeed(3);
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