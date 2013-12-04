package com.legion.samplegame;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;


import com.legion.framework.Game;
import com.legion.framework.Graphics;
import com.legion.framework.Image;
import com.legion.framework.Screen;
import com.legion.framework.Input.TouchEvent;

public class GameScreen extends Screen {
    enum GameState {
        Ready, Running, Paused, GameOver
    }


    GameState state = GameState.Ready;
    //PlayState playstate = PlayState.AwaitingSelection;
    // Variable Setup
    // You would create game objects here.
    PixelGame pgame;
    int livesLeft = 1;
    Paint paint;
    GUIObject pause;
    GUIObject play;
    GUIObject attack;
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
    Image[][] imageMap;

    public GameScreen(Game game) {
        super(game);
        viewX = 0;
        viewY = 0;
        pgame = new PixelGame();
        selpath = new ArrayList<Position>();
        determineTiles();
        lastGridDrag = null;
        lastScreenDrag = null;
        // Initialize game objects here
        Agent character1 = new Agent();
        Agent character2 = new Agent();
        character1.setGridPosition(new Position(1, 1));
    	character1.setDestination(new Position(1, 1));
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
        attack = new GUIObject(900, 100, Assets.attackLogo);
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
                if (pgame.st == PixelGame.state.WAITING) {
                	//check if the player is starting to draw a path
	                if(pgame.agentOnGrid(eventPositionInGrid) == pgame.getCurrentAgent()){
	                	pgame.st = PixelGame.state.SELECTING_PATH;
	                }
                }
                
            }

            if (event.type == TouchEvent.TOUCH_UP) {            
                if(pause.isHitBy(eventPositionOnScreen))
                {
                	state = GameState.Paused;
                	wasSomethingSelected = true;
                }        

                if(pgame.st == PixelGame.state.SELECTING_PATH){
                	if(selpath.size() > 1 ){
    	                pgame.getCurrentAgent().setPath(selpath);
                	}
                	pgame.st = PixelGame.state.WAITING;
	                selpath.clear();

                }
                if(pgame.st == PixelGame.state.SELECTING_ACTION){
                	if(attack.isHitBy(eventPositionOnScreen)){
                		pgame.st = PixelGame.state.SELECTING_TARGET;
                	}
                }
                lastScreenDrag = null;
            }
            if (event.type == TouchEvent.TOUCH_DRAGGED) {
            	if (pgame.st == PixelGame.state.SELECTING_PATH){
	
	            	
	            	if(lastGridDrag == null || selpath.size() == 0){
	            		lastGridDrag = eventPositionInGrid;
	            		selpath.add(eventPositionInGrid);
	            	}
	            	if(!eventPositionInGrid.equals(lastGridDrag)){//new grid position
	            		if(selpath.size()>1 && selpath.get(selpath.size()-2).equals(eventPositionInGrid)){//just went here
	            			selpath.remove(selpath.size()-1);
	            		}
	            		else if(selpath.size()>0 && pgame.getMap().isAdjacent(eventPositionInGrid.getX(), eventPositionInGrid.getY(), selpath.get(selpath.size()-1).getX(), selpath.get(selpath.size()-1).getY())
	            				&& !selpath.contains(eventPositionInGrid)
	            				&& pgame.getMap().isTraversible(eventPositionInGrid)){
	            			if(selpath.size() < 1 || pgame.agentOnGrid((int)(eventPositionInGrid.getX()), (int)(eventPositionInGrid.getY())) == null){
	            				selpath.add(eventPositionInGrid);
	            			}else{
	            				
	            			}
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
        //DRAW TERRAIN OVERLAYS
        if(pgame.st == PixelGame.state.SELECTING_PATH){
        	for(int i=0; i<selpath.size();i++){
        	Position p = selpath.get(i);
        	g.drawScaledImage(Assets.border, w2sx((int)(p.getX()*GameMap.SPRITE_WIDTH)), w2sy((int)(p.getY()*GameMap.SPRITE_HEIGHT)), zoom);
        	}
        } else if(pgame.st == PixelGame.state.SELECTING_TARGET){
        	ArrayList<Position> targets = pgame.getCurrentAgentTargets();
        	for (int i=0;i<targets.size();i++){
        		g.drawScaledImage(Assets.border, w2sx((int)(targets.get(i).getX()*GameMap.SPRITE_WIDTH)), w2sy((int)(targets.get(i).getY()*GameMap.SPRITE_HEIGHT)), zoom);
        	}
        }
        for(int i=0; i<pgame.numAgents(); i++)
    	{
    		if(pgame.getCurrentAgent() == pgame.getAgent(i))		{
        		g.drawScaledImage(Assets.haloYellow,  
        				(int)(w2sx((int)(pgame.getAgent(i).getGridPosition().getX()*40))), 
        				(int)(w2sy((int)(pgame.getAgent(i).getGridPosition().getY()*40))-GameMap.SPRITE_VERT_OFFSET*zoom), zoom);
    		}
    		g.drawScaledImage(pgame.getAgent(i).image,  
    				(int)(w2sx((int)(pgame.getAgent(i).getGridPosition().getX()*40))), 
    				(int)(w2sy((int)(pgame.getAgent(i).getGridPosition().getY()*40))-GameMap.SPRITE_VERT_OFFSET*zoom), zoom);
    	}
        	

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
    
    private void determineTiles() {
    	imageMap = new Image[pgame.getMap().getWidth()][pgame.getMap().getHeight()];
    	for(int y = 0; y<pgame.getMap().getHeight(); y++){
    		for(int x=0; x<pgame.getMap().getWidth(); x++){
    			String cxt = pgame.getMap().getTileContext(x, y);
    			//if(x==4 && y==3)System.out.println(cxt);
    			if(cxt.matches("r[rb0][^r][rb0][^r]")){
    				imageMap[x][y] = Assets.tile_river_NS;
    			} else if(cxt.matches("r[^r][rb0][^r][rb0]")){
    				imageMap[x][y] = Assets.tile_river_EW;
    			} else if(cxt.matches("rr[^r][^r]r")){
    				imageMap[x][y] = Assets.tile_river_WN;
    			} else if(cxt.matches("rrr[^r][^r]")){
    				imageMap[x][y] = Assets.tile_river_NE;
    			} else if(cxt.matches("r[^r][^r]rr")){
    				imageMap[x][y] = Assets.tile_river_WS;
    			} else if(cxt.matches("r[^r]rr[^r]")){
    				imageMap[x][y] = Assets.tile_river_SE;
    			} else if(cxt.matches("p[pb0][^p][pb0][^p]")){
    				imageMap[x][y] = Assets.tile_path_NS;
    			} else if(cxt.matches("p[^p][pb0][^rp][pb0]")){
    				imageMap[x][y] = Assets.tile_path_EW;
    			} else if(cxt.matches("pp[^p][^p]p")){
    				imageMap[x][y] = Assets.tile_path_WN;
    			} else if(cxt.matches("ppp[^p][^p]")){
    				imageMap[x][y] = Assets.tile_path_NE;
    			} else if(cxt.matches("p[^p][^p]pp")){
    				imageMap[x][y] = Assets.tile_path_WS;
    			} else if(cxt.matches("p[^p]pp[^p]")){
    				imageMap[x][y] = Assets.tile_path_SE;
    			} else if(cxt.matches("brprp")){
    				imageMap[x][y] = Assets.tile_bridge_EW;
    			} else if(cxt.matches("bprpr")){
    				imageMap[x][y] = Assets.tile_bridge_NS;
    			} else if(cxt.matches("p[^p][p0][p0][p0]")){
    				imageMap[x][y] = Assets.tile_path_ESW;
    			} else if(cxt.matches("p[p0][^p][p0][p0]")){
    				imageMap[x][y] = Assets.tile_path_NSW;
    			} else if(cxt.matches("p[p0][p0][^p][p0]")){
    				imageMap[x][y] = Assets.tile_path_NWE;
    			} else if(cxt.matches("p[p0][p0][p0][^p]")){
    				imageMap[x][y] = Assets.tile_path_NSE;
    			} else if(cxt.matches("p[p0][p0][p0][p0]")){
    				imageMap[x][y] = Assets.tile_path_NESW;
    			} else{
    				imageMap[x][y] = Assets.tile_grass;
    			}
    		}
    	}
	}
    
    private void drawTerrain()
    {
    	Graphics g = game.getGraphics();
    	for(int y = 0; y<pgame.getMap().getHeight(); y++){
    		for(int x=0; x<pgame.getMap().getWidth(); x++){
    			g.drawScaledImage(imageMap[x][y], w2sx(x*GameMap.SPRITE_WIDTH), w2sy(y*GameMap.SPRITE_HEIGHT), zoom);}
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
        if(pgame.st == PixelGame.state.SELECTING_ACTION){
        	g.drawImage(attack.getImage(), attack.getULX(), attack.getULY());
        }
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