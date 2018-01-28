package net.lustenauer.obstacleavoid.screen.game._old;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.lustenauer.obstacleavoid.assets.AssetDescriptors;
import net.lustenauer.obstacleavoid.assets.RegionNames;
import net.lustenauer.obstacleavoid.config.GameConfig;
import net.lustenauer.obstacleavoid.entity._old.Background;
import net.lustenauer.obstacleavoid.entity._old.Obstacle;
import net.lustenauer.obstacleavoid.entity._old.Player;
import net.lustenauer.obstacleavoid.util.debug.DebugCameraController;

/**
 * Created by Patric Hollenstein on 07.01.18.
 *
 * @author Patric Hollenstein
 */
@Deprecated
public class GameRendererOld implements Disposable {

    // == attributes ==
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private DebugCameraController debugCameraController;
    private final GameControllerOld controller;
    private final AssetManager assetManager;
    private final SpriteBatch batch;

    private TextureRegion playerRegion;
    private TextureRegion obstacleRegion;
    private TextureRegion backgroundRegion;

    // == constructors ==
    public GameRendererOld(SpriteBatch batch, AssetManager assetManager, GameControllerOld controller) {
        this.batch = batch;
        this.assetManager = assetManager;
        this.controller = controller;
        init();
    }

    // == init ==
    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        font = assetManager.get(AssetDescriptors.FONT);

        // create debug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        obstacleRegion = gamePlayAtlas.findRegion(RegionNames.OBSTACLE);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
    }

    // == public methodes ==
    public void render(float delta) {
        // not wrapping inside alive because we want be able to control camera even when game is over
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        if (Gdx.input.isTouched() && !controller.isGameOver()) {
            Vector2 screenTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 worldTouch = viewport.unproject(new Vector2(screenTouch));

            Player player = controller.getPlayer();
            worldTouch.x = MathUtils.clamp(worldTouch.x, 0, GameConfig.WORLD_WIDTH - player.getWidth());
            player.setX(worldTouch.x);
        }

        // clear screen
        net.lustenauer.obstacleavoid.util.GdxUtils.clearScreen();

        // render gameplay
        renderGamePlay();

        // render hud
        renderHud();

        // render debug graphics
        renderDebug();
    }


    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        net.lustenauer.obstacleavoid.util.ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    // == private methodes ==
    private void renderGamePlay() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //draw background
        Background background = controller.getBackground();
        batch.draw(backgroundRegion, background.getX(), background.getY(), background.getWidth(), background.getHeight());

        //draw player
        Player player = controller.getPlayer();
        batch.draw(playerRegion, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        //draw obstacles
        for (Obstacle obstacle : controller.getObstacles()) {
            batch.draw(obstacleRegion, obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

        batch.end();
    }


    private void renderHud() {
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES: " + controller.getLives();
        layout.setText(font, livesText);
        font.draw(batch, livesText, 20, GameConfig.HUD_HEIGHT - layout.height);

        String scoreText = "SCORE: " + controller.getDisplayScore();
        layout.setText(font, scoreText);
        font.draw(batch, scoreText, GameConfig.HUD_WIDTH - layout.width - 20, GameConfig.HUD_HEIGHT - layout.height);

        batch.end();
    }

    private void renderDebug() {
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();

        net.lustenauer.obstacleavoid.util.ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
        controller.getPlayer().drawDebug(renderer);
        for (Obstacle obstacle : controller.getObstacles()) {
            obstacle.drawDebug(renderer);
        }
    }
}
