package com.legion.samplegame;
import java.util.ArrayList;

import com.legion.framework.Image;
import com.legion.samplegame.Position;

public class Agent {



	private int maxHealth, currentHealth, power;
	float speed;
	float direction;
	boolean selected;
	public Image image;
	private ArrayList<Position> path;
	private boolean doneFlag;


	Position position;
	Position destination;
	//private Background bg = StartingClass.getBg1();

	public Agent ()
	{
		position = new Position(0 , 0);
		destination = new Position (0, 0);
		path = new ArrayList<Position>();
	
	}
	
	public void setPath(ArrayList<Position> p){
		path.clear();
		for(int i=0;i<p.size();i++){
			path.add(new Position(p.get(i)));
		}
		if(path.size()>0){
			this.destination = path.get(0);
		}
			
	}
	
	// Behavioral Methods
	public Position getDestination() {
		return destination;
	}

	public void setDestination(Position destination) {
		this.destination = destination;
	}
	public void setDestination(int x, int y) {
		this.destination.setX(x);
		this.destination.setY(y);
	}

	public void update(float time) {
		direction = (float) position.directionTo(destination);
		
		position.update(time, direction, 0.02f);
		//speedX = bg.getSpeedX();

		if(position.distanceTo(destination) < 0.1f)
		{
			position.setX(destination.getX());
			position.setY(destination.getY());
			if(path.size()>0){
				path.remove(0);
				if(path.size()>0)
					destination = path.get(0);
				else{ //done
					doneFlag = true;
					System.out.println("DONE? " + doneFlag);
				}
			}
		}
	}


	public boolean isDoneFlag() {
		return doneFlag;
	}

	public void setDoneFlag(boolean doneFlag) {
		this.doneFlag = doneFlag;
	}

	public void die() {

	}

	public void attack() {

	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getPower() {
		return power;
	}

	public double getSpeed() {
		return speed;
	}


	//public Background getBg() {
	//	return bg;
	//}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setCenterX(int centerX) {
		this.position.setX(centerX);
	}

	public void setCenterY(int centerY) {
		this.position.setY(centerY);
	}

	public Position getCenterPosition() {
		// TODO Auto-generated method stub
		return new Position(position);
	}
	public Position getGridPosition() {
		// TODO Auto-generated method stub
		return new Position(position.getX(), position.getY());
	}
	public float getDirection() {
		return direction;
	}
	public void setDirection(float direction) {
		this.direction = direction;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public void toggleSelected() {
		this.selected = !this.selected;
	}
	//public void setBg(Background bg) {
	//	this.bg = bg;
	//}

	public void setGridPosition(Position position2) {
		// TODO Auto-generated method stub
		position = new Position(position2);
	}

}
