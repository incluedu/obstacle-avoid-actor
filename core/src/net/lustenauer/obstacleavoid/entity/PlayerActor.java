package net.lustenauer.obstacleavoid.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import net.lustenauer.obstacleavoid.config.GameConfig;

/**
 * Created by Patric Hollenstein on 28.01.18.
 *
 * @author Patric Hollenstein
 */
public class PlayerActor extends ActorBase {
    /*
     * CONSTRUCTORS
     */

    public PlayerActor() {
        setCollisionRadius(GameConfig.PLAYER_BOUNDS_RADIUS);
        setSize(GameConfig.PLAYER_SIZE, GameConfig.PLAYER_SIZE);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update();
    }

    /*
         * PRIVATE METHODES
         */
    private void update() {
        float xSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }

        setX(getX() + xSpeed);

        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(getX(), 0, GameConfig.WORLD_WIDTH - getWidth());
        setPosition(playerX, getY());
    }

}
