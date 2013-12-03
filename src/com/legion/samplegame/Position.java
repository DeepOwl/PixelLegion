package com.legion.samplegame;
import java.math.*;

public class Position {
	private float x;
	private float y;
	public Position(float px, float py)
	{
		this.x = px;
		this.y = py;
	}
	public Position(Position p2) {
		// TODO Auto-generated constructor stub
		this.x = p2.getX();
		this.y = p2.getY();
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
    public float distanceTo(Position p2)
    {
    	return (float) Math.sqrt((Math.pow((double)(p2.getX() - this.getX()), 2) + Math.pow((double)(p2.getY() - this.getY()) ,2)));
    }
    public float directionTo(Position p2)
    {
		double deltaY = p2.getY() - this.y;
		double deltaX = p2.getX() - this.x;
    	double angleInRadians = Math.atan2(deltaY , deltaX);
    	return (float) angleInRadians;
    }
    public void update(float deltaTime, float direction, float speed)
    {
    	float distance = deltaTime * speed;
    	this.y += distance * Math.sin(direction);
    	this.x += distance * Math.cos(direction);
    }
    public boolean equals(Object obj){
	    if (obj == null)
	        return false;
	    if (obj == this)
	        return true;
	    if (!(obj instanceof Position))
	        return false;
	
	    Position rhs = (Position) obj;
	    if(this.x == rhs.x && this.y == rhs.y){
	    	return true;
	    }
	    return false;    	
    }
    public String toString(){
    	return "("+x+", "+y+")";
    }

}
