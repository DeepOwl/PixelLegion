package com.legion.samplegame;


import com.legion.framework.Game;
import com.legion.framework.Graphics;
import com.legion.framework.Image;
import com.legion.framework.Screen;
import com.legion.framework.Graphics.ImageFormat;


public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }


    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.menu = g.newImage("menu.jpg", ImageFormat.RGB565);
        Assets.click = game.getAudio().createSound("explode.ogg");

        Assets.background = g.newImage("background2.png", ImageFormat.RGB565);
    	Assets.tiledirt = g.newImage("tiledirt.png", ImageFormat.RGB565);
    	Assets.tileocean = g.newImage("tileocean.png", ImageFormat.RGB565);
		Assets.character1 = g.newImage("character1.png", ImageFormat.RGB565);
		Assets.haloGreen = g.newImage("halo_green.png", ImageFormat.RGB565);
		Assets.pauseLogo = g.newImage("pauselogo.png", ImageFormat.RGB565);
		Assets.playLogo = g.newImage("playlogo.png", ImageFormat.RGB565);
		Assets.dirt10 = g.newImage("dirt10.png", ImageFormat.RGB565);
		Assets.tile_grass = g.newImage("grasstile.png", ImageFormat.RGB565);
		Assets.tile_river_NS = g.newImage("river_ns.png", ImageFormat.RGB565);
		Assets.tile_river_EW = g.newImage("river_ew.png", ImageFormat.RGB565);
		Assets.tile_river_NE = g.newImage("river_ne.png", ImageFormat.RGB565);
		Assets.tile_river_SE = g.newImage("river_se.png", ImageFormat.RGB565);
		Assets.tile_river_WN = g.newImage("river_wn.png", ImageFormat.RGB565);
		Assets.tile_river_WS = g.newImage("river_ws.png", ImageFormat.RGB565);
		
		Assets.border = g.newImage("blue_border.png", ImageFormat.RGB565);
        game.setScreen(new MainMenuScreen(game));


    }


    @Override
    public void paint(float deltaTime) {


    }


    @Override
    public void pause() {


    }


    @Override
    public void resume() {


    }


    @Override
    public void dispose() {


    }


    @Override
    public void backButton() {


    }
}