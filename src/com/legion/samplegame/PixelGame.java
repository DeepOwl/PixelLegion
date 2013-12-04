package com.legion.samplegame;

import java.util.ArrayList;

public class PixelGame {
	private ArrayList<Agent> agentList;
	private ArrayList<Agent> agentOrderList;
	private GameMap map;
	private GameScreen screen;
    public static enum state {
    	WAITING, SELECTING_PATH, MOVING, SELECTING_ACTION, SELECTING_TARGET
    }
    public state st;
	public PixelGame(){
		loadGameMap();
		agentList = new ArrayList<Agent>();
		agentOrderList = new ArrayList<Agent>();
		st = state.WAITING;
	}
	
	//returns null if no one
	public Agent agentOnGrid(int x, int y){
		if(x<0 || x>=map.getWidth() || y<0 || y>=map.getHeight())
			return null;
		for(int i=0;i<agentList.size();i++){
			if(agentList.get(i).getGridPosition().equals(new Position(x, y))){
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

	public void updateAgents(float deltaTime) {
		boolean someoneDone = false;
		for(int i=0;i<agentList.size();i++){
			agentList.get(i).update(deltaTime);
			if(agentList.get(i).isDoneFlag()){
				someoneDone = true;
				agentList.get(i).setDoneFlag(false);
			}
		}
		if (someoneDone){
			//nextAgentTurn();
			//screen.doneMoving();
			st = state.SELECTING_ACTION;
		}
	}
	private void nextAgentTurn() {
		// TODO Auto-generated method stub
		agentOrderList.add(agentOrderList.remove(0));
		System.out.println(agentOrderList);
	}

	public int numAgents(){
		return agentList.size();
	}
	public Agent getAgent(int i){
		if(i < 0 || i >= numAgents()){
			return null;
		} else return agentList.get(i);
	}

	public ArrayList<Position> getCurrentAgentTargets() {
		//assume melee range only
		return map.getNeighbors(getCurrentAgent().getGridPosition());
		
	}

}
