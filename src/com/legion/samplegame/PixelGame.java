package com.legion.samplegame;

import java.util.ArrayList;

public class PixelGame {
	private ArrayList<Agent> agentList;
	private ArrayList<Agent> agentOrderList;
	private GameMap map;
	
	public PixelGame(){
		loadGameMap();
		agentList = new ArrayList<Agent>();
		agentOrderList = new ArrayList<Agent>();
		
	}
	
	//returns null if no one
	public Agent agentOnGrid(int x, int y){
		if(x<0 || x>=map.getWidth() || y<0 || y>=map.getHeight())
			return null;
		for(int i=0;i<agentList.size();i++){
			if(agentList.get(i).getGridPosition() == new Position(x, y)){
				return agentList.get(i);
			}
		}
		return null;
	}
	
	public void loadGameMap(){
		map = new GameMap();
	}
	
	public Agent getCurrentAgent(){
		return agentOrderList.get(0);
	}
	public GameMap getMap(){
		return map;
	}
	public void addAgent(Agent ag){
		agentList.add(ag);
		agentOrderList.add(ag);
	}

	public Agent agentOnGrid(Position p) {
		return agentOnGrid((int)p.getX(), (int)p.getY());
	}

}
