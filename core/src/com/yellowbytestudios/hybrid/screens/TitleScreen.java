package com.yellowbytestudios.hybrid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.yellowbytestudios.hybrid.media.Assets;
import com.yellowbytestudios.hybrid.screens.game.GameScreen;

import static com.yellowbytestudios.hybrid.MainGame.HEIGHT;
import static com.yellowbytestudios.hybrid.MainGame.WIDTH;

public class TitleScreen extends Screen {

    private Skin skin;
    private Stage stage;

    @Override
    public void create() {
        super.create();
        skin = Assets.manager.get(Assets.SKIN, Skin.class);

        stage = new Stage(getViewport());

        Table table = new Table();
        table.setDebug(false);
        table.align(Align.bottomLeft);
        table.pad(80);


        table.add(createButton("Start", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(null);
                ScreenManager.setScreen(new GameScreen());
            }
        })).row();
        table.add(createButton("Options", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        })).row();
        table.add(createButton("Exit", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(null);
                Gdx.app.exit();
            }
        })).row();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private Container createButton(String label, ClickListener clickListener) {
        TextButton button = new TextButton(label, skin, "default");
        button.setColor(Color.WHITE);
        button.getLabel().setAlignment(Align.left);
        button.getLabel().setFontScale(0.6f);
        button.addListener(clickListener);

        Container container = new Container(button);
        container.width(250f);
        container.height(50f);
        container.padTop(20);
        container.padBottom(20);
        return container;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.NAVY);
        sr.rect(0, 0, WIDTH, HEIGHT);
        sr.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        super.resize(w, h);
        //stage.getViewport().update(w, h, true);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void goBack() {

    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
