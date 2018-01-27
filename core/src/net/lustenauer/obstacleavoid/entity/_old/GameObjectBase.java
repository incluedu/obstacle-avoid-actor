package net.lustenauer.obstacleavoid.entity._old;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * Created by Patric Hollenstein on 06.01.18.
 *
 * @author Patric Hollenstein
 */
@Deprecated
public abstract class GameObjectBase {

    // == attributes ==
    private float x;
    private float y;
    private float width = 1;
    private float height = 1;


    private Circle bounds;

    // == constructor ==
    public GameObjectBase(float boundsRadius) {
        bounds = new Circle(x, y, boundsRadius);
    }

    // == private methodes ==

    // == public methodes ==
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public float getY() {
        return y;
    }

    protected void setY(float y) {
        this.y = y;
        updateBounds();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public void updateBounds() {
        bounds.setPosition(x + width / 2f, y + height / 2f);
    }


    public void drawDebug(ShapeRenderer renderer) {
        renderer.circle(bounds.x, bounds.y, bounds.radius, 30);
    }

    public Circle getBounds() {
        return bounds;
    }
}
