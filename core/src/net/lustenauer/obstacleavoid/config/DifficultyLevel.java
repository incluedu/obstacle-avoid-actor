package net.lustenauer.obstacleavoid.config;

/**
 * Created by Patric Hollenstein on 07.01.18.
 *
 * @author Patric Hollenstein
 */
public enum DifficultyLevel {
    EASY(net.lustenauer.obstacleavoid.config.GameConfig.EASY_OBSTACLE_SPEED),
    MEDIUM(net.lustenauer.obstacleavoid.config.GameConfig.MEDIUM_OBSTACLE_SPEED),
    HARD(net.lustenauer.obstacleavoid.config.GameConfig.HARD_OBSTACLE_SPEED);

    private final float obstacleSpeed;

    DifficultyLevel(float obstacleSpeed) {
        this.obstacleSpeed = obstacleSpeed;
    }

    public float getObstacleSpeed() {
        return obstacleSpeed;
    }

    public boolean isEasy() {
        return this == EASY;
    }

    public boolean isMedium() {
        return this == MEDIUM;
    }

    public boolean isHard() {
        return this == HARD;
    }


}
