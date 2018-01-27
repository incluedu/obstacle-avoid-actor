package net.lustenauer.obstacleavoid.screen.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import net.lustenauer.obstacleavoid.common.GameManager;

/**
 * Created by Patric Hollenstein on 14.01.18.
 *
 * @author Patric Hollenstein
 */
public class HeightScoreScreen extends MenuScreenBase {
    private static final Logger log = new Logger(HeightScoreScreen.class.getName(), Logger.DEBUG);


    /*
     * CONSTRUCTORS
     */
    public HeightScoreScreen(net.lustenauer.obstacleavoid.ObstacleAvoidGame game) {
        super(game);
    }

    /*
     * PRIVATE METHODES
     */
    protected Actor createUi() {
        Table table = new Table();

        TextureAtlas gamePlayAtlas = assetManager.get(net.lustenauer.obstacleavoid.assets.AssetDescriptors.GAME_PLAY);
        Skin uiskin = assetManager.get(net.lustenauer.obstacleavoid.assets.AssetDescriptors.UI_SKIN);
        TextureRegion backgroundRegion = gamePlayAtlas.findRegion(net.lustenauer.obstacleavoid.assets.RegionNames.BACKGROUND);

        // background
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        // highscore text
        Label highScoreText = new Label("HIGHSCORE", uiskin);

        // highscore label
        String highScoreString = GameManager.INSTANCE.getHighScoreString();
        Label highScoreLabel = new Label(highScoreString, uiskin);

        // back button
        TextButton backButton = new TextButton("BACK",uiskin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                back();
            }
        });

        // setup tables
        Table contentTable = new Table(uiskin);
        contentTable.defaults().pad(20f);
        contentTable.setBackground(net.lustenauer.obstacleavoid.assets.RegionNames.PANEL);

        contentTable.add(highScoreText).row();
        contentTable.add(highScoreLabel).row();
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

}
