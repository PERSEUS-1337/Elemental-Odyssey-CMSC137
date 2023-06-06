package sprites.players;
import java.util.Timer;
import java.util.TimerTask;


public class FreezePowerUp implements PowerUp {
    private Timer timer;

    @Override
    public void activate(PlayerSprite player) {
        System.out.println("Frozon!");
        player.setSpeed(4);
    }

    @Override
    public void deactivate(PlayerSprite player) {
        System.out.println("Stop!");
        player.setSpeed(0);
    }

}