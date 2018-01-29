package net.lustenauer.obstacleavoid.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import net.lustenauer.obstacleavoid.config.GameConfig;

/**
 * Created by Patric Hollenstein on 28.01.18.
 *
 * @author Patric Hollenstein
 */

public class ObstacleActor extends ActorBase implements Pool.Poolable {

    /*
     * ATTRIBUTES
     */
    private float ySpeed = GameConfig.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;


    /*
     * CONSTRUCTORS
     */
    public ObstacleActor() {
        setCollisionRadius(GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }

    /*
     * PUBLIC METHODES
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        update();
    }

    public void update() {
        setPosition(getX(), getY() - ySpeed);
    }

    public void setYSpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public boolean isPlayerColliding(PlayerActor player) {
        Circle playerBounds = player.getCollisionShape();
        // check if player bounds overlap with obstacle bounds
        boolean overlaps = Intersector.overlaps(playerBounds, getCollisionShape());
        if (overlaps) hit = true; // set hit to true
        return overlaps;
    }

    public boolean isNotHit(){
        return !hit;
    }

    @Override
    public void reset() {
        setRegion(null);
        hit = false;

    }

}
