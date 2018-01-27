package net.lustenauer.obstacleavoid.screen.game._old;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import net.lustenauer.obstacleavoid.common.GameManager;
import net.lustenauer.obstacleavoid.entity._old.Background;
import net.lustenauer.obstacleavoid.entity._old.Obstacle;
import net.lustenauer.obstacleavoid.entity._old.Player;

/**
 * Created by Patric Hollenstein on 07.01.18.
 *
 * @author Patric Hollenstein
 */
@Deprecated
public class GameControllerOld {
    // == constants ==
    private static final Logger log = new Logger(GameControllerOld.class.getName(), Logger.DEBUG);

    // == attributes ==
    private Player player;
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Background background;
    private float obstacleTimer;
    private float scoreTimer;
    private int lives = net.lustenauer.obstacleavoid.config.GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private Pool<Obstacle> obstaclePool;
    private Sound hitSound;

    private final float startPlayerX = (net.lustenauer.obstacleavoid.config.GameConfig.WORLD_WIDTH - net.lustenauer.obstacleavoid.config.GameConfig.PLAYER_SIZE) / 2f;
    private final float startPlayerY = 1 - net.lustenauer.obstacleavoid.config.GameConfig.PLAYER_SIZE / 2f;
    private final net.lustenauer.obstacleavoid.ObstacleAvoidGame game;
    private final AssetManager assetManager;

    // == constructors ==


    public GameControllerOld(net.lustenauer.obstacleavoid.ObstacleAvoidGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        init();
    }

    // == init ==
    private void init() {
        // create Player
        player = new Player();
        player.setPosition(startPlayerX, startPlayerY);

        // create obstacle pool
        obstaclePool = Pools.get(Obstacle.class, 40);

        // create background
        background = new Background();
        background.setPosition(0f, 0f);
        background.setSize(net.lustenauer.obstacleavoid.config.GameConfig.WORLD_WIDTH, net.lustenauer.obstacleavoid.config.GameConfig.WORLD_HEIGHT);

        hitSound = assetManager.get(net.lustenauer.obstacleavoid.assets.AssetDescriptors.HIT_SOUND);
    }

    // == public methodes ==
    public void update(float delta) {
        if (isGameOver()) {
            return;
        }

        updatePlayer(delta);
        updateObstacles(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if (isPlayerCollidingWithObstacle()) {
            log.debug("Collision detected");
            lives--;

            if (isGameOver()) {
                log.debug("Game Over!!!");
                GameManager.INSTANCE.updateHighScore(score);
            } else {
                restart();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public Background getBackground() {
        return background;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    // == private methodes ==
    private void restart() {
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX, startPlayerY);

    }

    private boolean isPlayerCollidingWithObstacle() {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isNotHit() && obstacle.isPlayerColliding(player)) {
                hitSound.play();
                return true;
            }
        }
        return false;
    }

    private void updateScore(float delta) {
        scoreTimer += delta;

        if (scoreTimer >= net.lustenauer.obstacleavoid.config.GameConfig.SCORE_MAX_TIME) {
            score += MathUtils.random(1, 5);
            scoreTimer = 0f;
        }
    }

    private void updateDisplayScore(float delta) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int) (60 * delta));
        }
    }

    private void updatePlayer(float delta) {
        float xSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = net.lustenauer.obstacleavoid.config.GameConfig.MAX_PLAYER_X_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -net.lustenauer.obstacleavoid.config.GameConfig.MAX_PLAYER_X_SPEED;
        }

        player.setX(player.getX() + xSpeed);

        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(player.getX(), 0, net.lustenauer.obstacleavoid.config.GameConfig.WORLD_WIDTH - player.getWidth());
        player.setPosition(playerX, player.getY());
    }

    private void updateObstacles(float delta) {

        for (Obstacle obstacle : obstacles) {
            obstacle.update();
        }

        createNewObstacle(delta);
        removePassedObstacles();
    }


    private void createNewObstacle(float delta) {
        obstacleTimer += delta;

        if (obstacleTimer > net.lustenauer.obstacleavoid.config.GameConfig.OBSTACLE_SPAWN_TIME) {
            float min = 0;
            float max = net.lustenauer.obstacleavoid.config.GameConfig.WORLD_WIDTH - net.lustenauer.obstacleavoid.config.GameConfig.OBSTACLE_SIZE;

            float obstacleX = MathUtils.random(min, max);
            float obstacleY = net.lustenauer.obstacleavoid.config.GameConfig.WORLD_HEIGHT;

            Obstacle obstacle = obstaclePool.obtain();
            net.lustenauer.obstacleavoid.config.DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            obstacles.add(obstacle);
            obstacleTimer = 0f;
        }
    }

    private void removePassedObstacles() {
        if (obstacles.size > 0) {
            Obstacle firstObstacle = obstacles.first();

            float minObstacleY = -net.lustenauer.obstacleavoid.config.GameConfig.OBSTACLE_SIZE;

            if (firstObstacle.getY() < minObstacleY) {
                obstacles.removeValue(firstObstacle, true);
                obstaclePool.free(firstObstacle);
            }
        }
    }


}
