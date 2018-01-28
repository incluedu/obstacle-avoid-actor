package net.lustenauer.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.lustenauer.obstacleavoid.ObstacleAvoidGame;
import net.lustenauer.obstacleavoid.assets.AssetDescriptors;
import net.lustenauer.obstacleavoid.assets.RegionNames;
import net.lustenauer.obstacleavoid.config.GameConfig;
import net.lustenauer.obstacleavoid.entity.PlayerActor;
import net.lustenauer.obstacleavoid.util.GdxUtils;
import net.lustenauer.obstacleavoid.util.ViewportUtils;
import net.lustenauer.obstacleavoid.util.debug.DebugCameraController;

/**
 * Created by Patric Hollenstein on 27.01.18.
 *
 * @author Patric Hollenstein
 */
public class GameScreen extends ScreenAdapter {

    /*
     * CONSTANTS
     */
    private static final float PADDING = 20f;


    /*
     * ATTRIBUTES
     */
    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private final GlyphLayout layout = new GlyphLayout();

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private ShapeRenderer renderer;

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private BitmapFont font;

    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;

    private Sound hitSound;

    private float startPlayerX = (GameConfig.WORLD_WIDTH - GameConfig.PLAYER_SIZE) / 2f;
    private float startPlayerY = (GameConfig.PLAYER_SIZE / 2f);

    private DebugCameraController debugCameraController;

    private PlayerActor player;

    /*
     * CONSTRUCTOR
     */

    public GameScreen(ObstacleAvoidGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        this.batch = game.getBatch();

    }

    /*
     * PUBLIC METHODES
     */

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, batch);
        stage.setDebugAll(true);

        renderer = new ShapeRenderer();

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera);
        font = assetManager.get(AssetDescriptors.FONT);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);

        player = new PlayerActor();
        player.setPosition(startPlayerX,startPlayerY);
        player.setRegion(gamePlayAtlas.findRegion(RegionNames.PLAYER));

        stage.addActor(player);
    }

    @Override
    public void render(float delta) {
        // handle debug camera input and apply configuration to our camera
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        //clear screen
        GdxUtils.clearScreen();

        viewport.apply();
        renderGamePlay();

        uiViewport.apply();
        renderUi();

        viewport.apply();
        renderDebug();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    /*
     * PRIVATE METHODES
     */
    private void renderGamePlay() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawGamePlay();
        batch.end();

        stage.act();
        stage.draw();
    }

    private void drawGamePlay() {
    }

    private void renderUi() {
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        // draw lives
        String livesText = "LIVES: " + lives;
        layout.setText(font, livesText);
        font.draw(batch, layout, PADDING, GameConfig.HUD_HEIGHT - layout.height);

        // draw score
        String scoreText = "SCORE: " + displayScore;
        layout.setText(font, scoreText);
        font.draw(batch, layout, GameConfig.HUD_WIDTH - layout.width - PADDING, GameConfig.HUD_HEIGHT - layout.height);

        batch.end();
    }

    private void renderDebug() {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();


        // draw grid
        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
    }



}
