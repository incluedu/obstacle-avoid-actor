package net.lustenauer.obstacleavoid.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by Patric Hollenstein on 14.01.18.
 *
 * @author Patric Hollenstein
 */
public class GameManager {

    public static final GameManager INSTANCE = new GameManager();

    private static final String HIGH_SCORE_KEY = "highscore";
    private static final String DIFFICULTY_KEY = "difficulty";

    private Preferences PREFS;

    private int highscore;
    private net.lustenauer.obstacleavoid.config.DifficultyLevel difficultyLevel = net.lustenauer.obstacleavoid.config.DifficultyLevel.MEDIUM;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(net.lustenauer.obstacleavoid.ObstacleAvoidGame.class.getSimpleName());
        highscore = PREFS.getInteger(HIGH_SCORE_KEY, 0);
        String difficultyName = PREFS.getString(DIFFICULTY_KEY, net.lustenauer.obstacleavoid.config.DifficultyLevel.MEDIUM.name());
        difficultyLevel = net.lustenauer.obstacleavoid.config.DifficultyLevel.valueOf(difficultyName);
    }

    public void updateHighScore(int score) {
        if (score < highscore) {
            return;
        }
        highscore = score;
        PREFS.putInteger(HIGH_SCORE_KEY, highscore);
        PREFS.flush();
    }

    public String getHighScoreString() {
        return String.valueOf(highscore);
    }

    public void updateDifficultyLevel(net.lustenauer.obstacleavoid.config.DifficultyLevel newDifficultyLevel) {
        if (difficultyLevel == newDifficultyLevel) {
            return;
        }
        difficultyLevel = newDifficultyLevel;
        PREFS.putString(DIFFICULTY_KEY, difficultyLevel.name());
        PREFS.flush();
    }

    public net.lustenauer.obstacleavoid.config.DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
}
