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
		Assets.haloYellow = g.newImage("halo_yellow.png", ImageFormat.RGB565);
		Assets.pauseLogo = g.newImage("pauselogo.png", ImageFormat.RGB565);
		Assets.playLogo = g.newImage("playlogo.png", ImageFormat.RGB565);
		Assets.attackLogo = g.newImage("attacklogo.png", ImageFormat.RGB565);
		Assets.dirt10 = g.newImage("dirt10.png", ImageFormat.RGB565);
		Assets.tile_grass = g.newImage("grasstile.png", ImageFormat.RGB565);
		Assets.tile_river_NS = g.newImage("river_ns.png", ImageFormat.RGB565);
		Assets.tile_river_EW = g.newImage("river_ew.png", ImageFormat.RGB565);
		Assets.tile_river_NE = g.newImage("river_ne.png", ImageFormat.RGB565);
		Assets.tile_river_SE = g.newImage("river_se.png", ImageFormat.RGB565);
		Assets.tile_river_WN = g.newImage("river_wn.png", ImageFormat.RGB565);
		Assets.tile_river_WS = g.newImage("river_ws.png", ImageFormat.RGB565);
		Assets.tile_path_NS = g.newImage("path_ns.png", ImageFormat.RGB565);
		Assets.tile_path_EW = g.newImage("path_ew.png", ImageFormat.RGB565);
		Assets.tile_path_NE = g.newImage("path_ne.png", ImageFormat.RGB565);
		Assets.tile_path_SE = g.newImage("path_se.png", ImageFormat.RGB565);
		Assets.tile_path_WN = g.newImage("path_wn.png", ImageFormat.RGB565);
		Assets.tile_path_WS = g.newImage("path_ws.png", ImageFormat.RGB565);
		Assets.tile_path_NSW = g.newImage("path_nsw.png", ImageFormat.RGB565);
		Assets.tile_path_NSE = g.newImage("path_nse.png", ImageFormat.RGB565);
		Assets.tile_path_ESW = g.newImage("path_esw.png", ImageFormat.RGB565);
		Assets.tile_path_NWE = g.newImage("path_nwe.png", ImageFormat.RGB565);
		Assets.tile_path_NESW = g.newImage("path_nesw.png", ImageFormat.RGB565);
		Assets.tile_bridge_NS = g.newImage("bridge_ns.png", ImageFormat.RGB565);
		Assets.tile_bridge_EW = g.newImage("bridge_ew.png", ImageFormat.RGB565);
		
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