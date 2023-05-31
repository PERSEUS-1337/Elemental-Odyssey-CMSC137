package sprites.players;

public class FreezePowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        System.out.println("Frozone!");
        player.setSpeed(4);
    }
    
    @Override
    public void deactivate(PlayerSprite player) {
        System.out.println("Move!");
        player.setSpeed(0);
        System.out.println(player.getSpeed());
        System.out.println(player.name);
    }
    
}
