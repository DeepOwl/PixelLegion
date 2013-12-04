package com.legion.samplegame;
import com.legion.framework.Audio;
import com.legion.framework.FileIO;
import com.legion.framework.Game;
import com.legion.framework.Graphics;
import com.legion.framework.Input;
import com.legion.framework.Screen;
//import java.awt.Graphics;


public class DesktopGame implements Game{

	public void start(){
		new LoadingScreen(this);
	}

	@Override
	public Audio getAudio() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Input getInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileIO getFileIO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graphics getGraphics() {
		// TODO Auto-generated method stub
		//Graphics t;
		return null;
	}

	@Override
	public void setScreen(Screen screen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Screen getCurrentScreen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Screen getInitScreen() {
		// TODO Auto-generated method stub
		return null;
	}
}
