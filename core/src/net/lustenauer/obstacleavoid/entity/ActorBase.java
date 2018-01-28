package net.lustenauer.obstacleavoid.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Logger;

/**
 * Created by Patric Hollenstein on 28.01.18.
 *
 * @author Patric Hollenstein
 */
public class ActorBase extends Actor {
    private static final Logger log = new Logger(ActorBase.class.getName(), Logger.DEBUG);

    /*
     * ATTRIBUTES
     */
    private final Circle collisionShape = new Circle();
    private TextureRegion region;


    /*
     * CONSTRUCTORS
     */
    public ActorBase() {
    }


    /*
     * PUBLIC METHODES
     */
    public void setCollisionRadius(float radius) {
        collisionShape.setRadius(radius);
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (region == null) {
            log.error("Region not set on Actor " + getClass().getName());
            return;
        }

        batch.draw(region,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );
    }

    @Override
    protected void drawDebugBounds(ShapeRenderer shapeRenderer) {
        if (!getDebug()) {
            return;
        }
        Color oldColor = shapeRenderer.getColor().cpy();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.x(collisionShape.x, collisionShape.y, 0.1f);
        shapeRenderer.circle(collisionShape.x, collisionShape.y, collisionShape.radius, 30);

        shapeRenderer.setColor(oldColor);
    }

    @Override
    protected void positionChanged() {
        updateCollisionShape();
    }

    @Override
    protected void sizeChanged() {
        updateCollisionShape();
    }

    /*
     * PRIVATE METHODES
     */
    private void updateCollisionShape() {
        float halfWidth = getWidth() / 2f;
        float halfHeight = getHeight() / 2f;
        collisionShape.setPosition(getX() + halfWidth, getY() + halfHeight);
    }
}
