package sprites.players;


public class BarrierPowerUp implements PowerUp {

    @Override
    public void activate(PlayerSprite player) {
        System.out.println("Protect!");
        player.setShield(true);
    }

    @Override
    public void deactivate(PlayerSprite player) {
        System.out.println("Not protect!");
        player.setShield(false);
    }

}