package sprites.players;

public interface PowerUp {
    void activate(PlayerSprite player);
    void deactivate(PlayerSprite player);
    int getDuration();
}