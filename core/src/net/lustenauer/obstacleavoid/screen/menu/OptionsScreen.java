package net.lustenauer.obstacleavoid.screen.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import net.lustenauer.obstacleavoid.ObstacleAvoidGame;
import net.lustenauer.obstacleavoid.assets.AssetDescriptors;
import net.lustenauer.obstacleavoid.assets.RegionNames;
import net.lustenauer.obstacleavoid.common.GameManager;
import net.lustenauer.obstacleavoid.config.DifficultyLevel;

/**
 * Created by Patric Hollenstein on 14.01.18.
 *
 * @author Patric Hollenstein
 */
public class OptionsScreen extends MenuScreenBase {
    private static final Logger log = new Logger(OptionsScreen.class.getName(), Logger.DEBUG);

    private ButtonGroup<CheckBox> checkBoxGroup;
    private CheckBox easy;
    private CheckBox medium;
    private CheckBox hard;

    public OptionsScreen(ObstacleAvoidGame game) {
        super(game);
    }

    protected Actor createUi() {
        Table table = new Table();
        table.defaults().pad(15f);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        Skin uiskin = assetManager.get(AssetDescriptors.UI_SKIN);
        DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();

        TextureRegion backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        // label
        Label label = new Label("DIFFICULTY", uiskin);

        // check boxes
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                difficultyChanged();
            }
        };

        easy = checkBox(DifficultyLevel.EASY.name(), uiskin);
        easy.addListener(listener);

        medium = checkBox(DifficultyLevel.MEDIUM.name(), uiskin);
        medium.addListener(listener);

        hard = checkBox(DifficultyLevel.HARD.name(), uiskin);
        hard.addListener(listener);

        checkBoxGroup = new ButtonGroup<CheckBox>(easy, medium, hard);
        checkBoxGroup.setChecked(difficultyLevel.name());

        // back button
        TextButton backButton = new TextButton("BACK", uiskin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                back();
            }
        });


        // setup tables
        Table contentTable = new Table(uiskin);
        contentTable.defaults().pad(10f);
        contentTable.setBackground(RegionNames.PANEL);

        contentTable.add(label).row();
        contentTable.add(easy).row();
        contentTable.add(medium).row();
        contentTable.add(hard).row();
        contentTable.add(backButton);

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }


    private void back() {
        log.debug("back");
        game.setScreen(new MenuScreen(game));
    }

    private void difficultyChanged() {
        log.debug("difficultyChanged");

        CheckBox checked = checkBoxGroup.getChecked();

        if (checked == easy) {
            log.debug("easy");
            GameManager.INSTANCE.updateDifficultyLevel(DifficultyLevel.EASY);
        } else if (checked == hard) {
            log.debug("hard");
            GameManager.INSTANCE.updateDifficultyLevel(DifficultyLevel.HARD);
        } else {
            log.debug("medium");
            GameManager.INSTANCE.updateDifficultyLevel(DifficultyLevel.MEDIUM);
        }
    }

    private static CheckBox checkBox(String text, Skin skin) {
        CheckBox checkBox = new CheckBox(text, skin);
        checkBox.left();
        checkBox.pad(8f);
        checkBox.getLabelCell().pad(8f);
        return checkBox;
    }

}
