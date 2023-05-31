package sprites.players;


public class SpeedPowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        System.out.println("Speed!");
        player.setSpeed(-2);
    }
    
    @Override
    public void deactivate(PlayerSprite player) {
        System.out.println("Normal!");
        player.setSpeed(0);
    }
    
}
