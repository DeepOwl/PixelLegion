package com.legion.samplegame;

import com.legion.framework.Image;

public class GUIObject {
	private Position position;
	private Image image;
	private int width;
	private int height;
	public GUIObject(int x, int y , Image im)
	{
		position = new Position(x, y);
		//position.setX(x);
		//position.setY(y);
		image = im;
		width = im.getWidth();
		width = im.getHeight();
		
	}
	public int getULX()
	{
		return (int) position.getX();
	}
	public int getULY()
	{
		return (int) position.getY();
	}
	public Image getImage(){
		return image;
	}
	public boolean isHitBy(Position p)
	{
		if (p.getX() < position.getX())
			return false;
		if (p.getX() > position.getX()+width)
			return false;
		if (p.getY() < position.getY())
			return false;
		if (p.getY() > position.getY()+width)
			return false;
		return true;	
			
	}
}
