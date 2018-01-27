package net.lustenauer.obstacleavoid.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Patric Hollenstein on 01.01.18.
 *
 * @author Patric Hollenstein
 */
public class GdxUtils {

    private GdxUtils() {
    }


    public static void clearScreen() {
        clearScreen(Color.BLACK);
    }

    public static void clearScreen(Color color) {
        // DRY - Don't repeat yourself
        // WET - Was everyone's time
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
