package com.legion.samplegame;

import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;



import android.util.Log;
import android.view.Display;

import com.legion.framework.Game;
import com.legion.framework.Graphics;
import com.legion.framework.Image;
import com.legion.framework.Screen;
import com.legion.framework.Input.TouchEvent;
import com.legion.samplegame.*;

public class GameScreen extends Screen {
    enum GameState {
        Ready, Running, Paused, GameOver
    }
    enum PlayState {
    	AwaitingSelection, SelectingPath, PerformingAction
    }

    GameState state = GameState.Ready;
    PlayState playstate = PlayState.AwaitingSelection;
    // Variable Setup
    // You would create game objects here.
    PixelGame pgame;
    //ArrayList<Agent> characters;
    //characters.add(character1);
    int livesLeft = 1;
    Paint paint;
    GUIObject pause;
    GUIObject play;
    int viewX, viewY;
    float zoom;
    int MAX_X = 1280;
    int MAX_Y = 800;
    int selectedIndex = -1;
    //GameMap map;
    private String msg = "";
    private ArrayList<Position> selpath;
    Position lastGridDrag;
    Position lastScreenDrag;

    public GameScreen(Game game) {
        super(game);
        viewX = 0;
        viewY = 0;
        pgame = new PixelGame();
        //map = new GameMap();
        selpath = new ArrayList<Position>();
        lastGridDrag = null;
        lastScreenDrag = null;
        // Initialize game objects here
        Agent character1 = new Agent();
        Agent character2 = new Agent();
        character1.setGridPosition(new Position(3, 4));
    	character1.setDestination(new Position(3, 4));
    	character1.image = Assets.character1;
    	
        character2.setGridPosition(new Position(4, 6));
    	character2.setDestination(new Position(4, 6));
    	character2.image = Assets.character1;
    	//characters = new ArrayList<Agent>();
    	//characters.add(character1);
    	//characters.add(character2);
    	pgame.addAgent(character1);
    	pgame.addAgent(character2);
        // Defining a paint object
        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        zoom=3.0f;
        
        pause = new GUIObject(1100, 50, Assets.pauseLogo);
        play = new GUIObject(1100, 50, Assets.playLogo);
    }

    public Position worldToScreen(int x, int y)
    {
    	return new Position(x - viewX, y - viewY);
    	
    }
    //gives the screen coordinates from the world coordinates
    public int w2sx(int x)
    {
    	return (int) ((x*zoom - viewX));
    }
    public int w2sy(int y)
    {
    	return (int) ((y*zoom - viewY));
    }
    public int s2wx(int x)
    {
    	return (int) ((x + viewX)/zoom);
    }
    public int s2wy(int y)
    {
    	return (int) ((y + viewY)/zoom);
    }
    
    
    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.
        // Refer to Unit 3's code. We did a similar thing without separating the
        // update methods.
        GameState nowState = state;
        if (nowState == GameState.Ready){
            updateReady(touchEvents);
            return;
        }
        else if (nowState == GameState.Running){
            updateRunning(touchEvents, deltaTime);
            return;
        }
        else if (nowState == GameState.Paused){
            updatePaused(touchEvents);
            return;
        }
        else if (nowState == GameState.GameOver){
            updateGameOver(touchEvents);
            return;
        }
    }

    private void updateReady(List<TouchEvent> touchEvents) {
        
        // This example starts with a "Ready" screen.
        // When the user touches the screen, the game begins. 
        // state now becomes GameState.Running.
        // Now the updateRunning() method will be called!
        
        if (touchEvents.size() > 0)
            state = GameState.Running;
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        
        //This is identical to the update() method from our Unit 2/3 game.
        
        
        // 1. All touch input is handled here:
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            //get event position in world terms
            Position eventPositionOnScreen = new Position(event.x, event.y);
            Position eventPositionInWorld = new Position(s2wx(event.x), s2wy(event.y));
            Position eventPositionInGrid = new Position((int)(eventPositionInWorld.getX()/40), (int)(eventPositionInWorld.getY()/40));
            boolean wasSomethingSelected = false;
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (playstate == PlayState.AwaitingSelection) {
                	//check if the player is starting to draw a path
	                if(pgame.agentOnGrid(eventPositionInGrid) == pgame.getCurrentAgent()){
	                	playstate = PlayState.SelectingPath;
	                }
                }
                
            }

            if (event.type == TouchEvent.TOUCH_UP) {            
                if(pause.isHitBy(eventPositionOnScreen))
                {
                	state = GameState.Paused;
                	wasSomethingSelected = true;
                }        

                if(playstate == PlayState.SelectingPath){
	                playstate = PlayState.AwaitingSelection;
	                pgame.getCurrentAgent().setPath(selpath);
	                selpath.clear();

                }
                lastScreenDrag = null;
            }
            if (event.type == TouchEvent.TOUCH_DRAGGED) {
            	if (playstate == PlayState.SelectingPath){
	
	            	
	            	if(lastGridDrag == null || selpath.size() == 0){
	            		lastGridDrag = eventPositionInGrid;
	            		selpath.add(eventPositionInGrid);
	            	}
	            	if(!eventPositionInGrid.equals(lastGridDrag)){//new grid position
	            		if(selpath.size()>1 && selpath.get(selpath.size()-2).equals(eventPositionInGrid)){//just went here
	            			selpath.remove(selpath.size()-1);
	            		}
	            		else if(selpath.size()>0 && pgame.getMap().isAdjacent(eventPositionInGrid.getX(), eventPositionInGrid.getY(), selpath.get(selpath.size()-1).getX(), selpath.get(selpath.size()-1).getY())){
	            			selpath.add(eventPositionInGrid);
	            		}
	            		lastGridDrag = eventPositionInGrid;
	            	}
            	}
            	else {
            		if(lastScreenDrag == null){
            			lastScreenDrag = eventPositionOnScreen;
            		} else {
            			viewX -= eventPositionOnScreen.getX() - lastScreenDrag.getX();
            			viewY -= eventPositionOnScreen.getY() - lastScreenDrag.getY();
            			if (viewX > Assets.background.getWidth()*zoom - MAX_X) viewX = (int) (Assets.background.getWidth()*zoom - MAX_X);
            			if (viewY > Assets.background.getHeight()*zoom - MAX_Y) viewY = (int) (Assets.background.getHeight()*zoom - MAX_Y);
            			if (viewX < 0) viewX = 0;
            			if (viewY < 0) viewY = 0;
            			lastScreenDrag = eventPositionOnScreen;
            		}
            	}
            	
            }
        }

        // 2. Check miscellaneous events like death:
        
        if (livesLeft == 0) {
            state = GameState.GameOver;
        }
        
        
        // 3. Call individual update() methods here.
        // This is where all the game updates happen.
        // For example, robot.update();
        pgame.updateAgents(deltaTime);
   
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            Position eventPositionOnScreen = new Position(event.x, event.y);
            if (event.type == TouchEvent.TOUCH_UP) {
            	if(pause.isHitBy(eventPositionOnScreen))
                {
                	state = GameState.Running;
                }
        		
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 300 && event.x < 980 && event.y > 100
                        && event.y < 500) {
                    nullify();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }

    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        // First draw the game elements.


        
        // Example:
        g.drawScaledImage(Assets.background, viewX * -1, viewY * -1, zoom);
        this.drawTerrain();
        //g.drawScaledImage(Assets.character1, 900, 200, (float)4.0);
        for(int i=0; i<selpath.size();i++){
        	Position p = selpath.get(i);
        	g.drawScaledImage(Assets.border, w2sx((int)(p.getX()*GameMap.SPRITE_WIDTH)), w2sy((int)(p.getY()*GameMap.SPRITE_HEIGHT)), zoom);
        }
        for(int i=0; i<pgame.numAgents(); i++)
    	{
    		//if (i == selectedIndex)
    		//{
    		//	g.drawScaledImage(Assets.haloGreen, 
    		//			(int)(w2sx((int)(characters.get(i).getGridPosition().getX()*40))), 
    		//			(int)(w2sy((int)(characters.get(i).getGridPosition().getY()*40))-20*zoom), zoom);
    		//}
    					
    		g.drawScaledImage(pgame.getAgent(i).image,  
    				(int)(w2sx((int)(pgame.getAgent(i).getGridPosition().getX()*40))), 
    				(int)(w2sy((int)(pgame.getAgent(i).getGridPosition().getY()*40))-GameMap.SPRITE_VERT_OFFSET*zoom), zoom);
    	}
        	
        

       /* g.drawString(""
        		//+"(" + character1.getCenterPosition().getX() + " " + character1.getCenterPosition().getY() +")("
        		+ "("+ character1.getDestination().getX()+ " " + character1.getDestination().getY() + ")" 
        		//+ "D" + character1.getDirection() + "\t" 
        		//+ character1.getCenterPosition().distanceTo(character1.getDestination())
                ,640, 300, paint);*/
        g.drawString(msg,640, 300, paint);
        
        // Secondly, draw the UI above the game elements.
        GameState nowState = state;
        if (nowState == GameState.Ready){        
            drawReadyUI();
            return;
        }
        else if (nowState == GameState.Running){
            drawRunningUI();
	        return;
	    }
        else if (nowState == GameState.Paused){
            drawPausedUI();
        	return;
		}
        else if (nowState == GameState.GameOver){
            drawGameOverUI();
        	return;
    	}

    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;

        // Call garbage collector to clean up memory.
        System.gc();
    }
    
    private void drawTerrain()
    {
    	Graphics g = game.getGraphics();
    	for(int y = 0; y<pgame.getMap().getHeight(); y++){
    		for(int x=0; x<pgame.getMap().getWidth(); x++){
    			if(pgame.getMap().isRiver(x,y)){
    				if(pgame.getMap().sameToNorth(x, y, true)){//NORTH RIVER
    					if(pgame.getMap().sameToSouth(x, y, true)){//NORTH and SOUTH RIVER
    						g.drawScaledImage(Assets.tile_river_NS, w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);
    					}
    					else if(pgame.getMap().sameToWest(x, y, true)){//NORTH and WEST RIVER
    						g.drawScaledImage(Assets.tile_river_WN, w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);
    					}
    					else if(pgame.getMap().sameToEast(x, y, true)){//NORTH and EAST RIVER
    						g.drawScaledImage(Assets.tile_river_NE, w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);
    					}
    				}
    				else if(pgame.getMap().sameToSouth(x, y, true)){//SOUTH RIVER
    					if(pgame.getMap().sameToWest(x, y, true)){//SOUTH and WEST RIVER
    						g.drawScaledImage(Assets.tile_river_WS, w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);
    					}
    					else if(pgame.getMap().sameToEast(x, y, true)){//SOUTH and EAST RIVER
    						g.drawScaledImage(Assets.tile_river_SE,  w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);
    					}
    				} 
    				else if(pgame.getMap().sameToWest(x, y, true)){//WEST RIVER
    					if(pgame.getMap().sameToEast(x, y, true)){//SOUTH and EAST RIVER
    						g.drawScaledImage(Assets.tile_river_EW, w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);
    					}
    				} 
    			} else {//NO RIVER
					g.drawScaledImage(Assets.tile_grass, w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);
    			}
    			//g.drawScaledImage(Assets.border, w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);
    		}
    	}
    		
    	
    
    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap each side of the screen to move in that direction.",
                640, 300, paint);

    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        //g.drawImage(Assets.menu, x, y)
        g.drawImage(pause.getImage(), pause.getULX(), pause.getULY());
    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);
        g.drawImage(play.getImage(), play.getULX(), play.getULY());
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("GAME OVER.", 640, 300, paint);

    }

    @Override
    public void pause() {
        if (state == GameState.Running)
            state = GameState.Paused;

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        pause();
    }

    
}