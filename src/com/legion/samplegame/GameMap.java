package com.legion.samplegame;

import java.util.ArrayList;

import android.util.Log;



public class GameMap {
	public static enum dir {
		NORTH, SOUTH, EAST, WEST
	}
	public static final int SPRITE_WIDTH = 40;
	public static final int SPRITE_HEIGHT = 40;
	public static final int SPRITE_VERT_OFFSET = 10;
	char[][] map = {	{ 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'p', 'g'},
						{ 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'p', 'g'},
						{ 'g', 'g', 'g', 'g', 'g', 'g', 'r', 'g', 'p', 'g'},
						{ 'g', 'g', 'g', 'r', 'r', 'r', 'r', 'g', 'p', 'g'},
						{ 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'p', 'p'},
						{ 'g', 'g', 'g', 'r', 'g', 'g', 'p', 'p', 'p', 'p'},
						{ 'p', 'p', 'p', 'b', 'p', 'p', 'p', 'p', 'p', 'g'},
						{ 'g', 'g', 'g', 'r', 'g', 'g', 'p', 'p', 'p', 'p'},
						{ 'g', 'g', 'g', 'r', 'r', 'g', 'g', 'g', 'g', 'g'},
						{ 'g', 'g', 'g', 'g', 'r', 'g', 'g', 'g', 'g', 'g'}, };
	private int width;
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	private int height;
	GameMap(){
		width = map[0].length;
		height = map.length;
	}
	public boolean isTraversible(Position p){
		if(!inMap((int)(p.getX()), (int)(p.getY()))){
			return false;
		} else if (map[(int)(p.getY())][(int)(p.getX())] == 'r'){
			return false;
		} else return true;
	}
	public String getTileContext(int x, int y){
		String ret = "";
		if(inMap(x,y))ret+=map[y][x];
		else ret+='0';
		ret+=tileTo(dir.NORTH, x, y);
		ret+=tileTo(dir.EAST, x, y);
		ret+=tileTo(dir.SOUTH, x, y);
		ret+=tileTo(dir.WEST, x, y);
		return ret;
	}
	boolean isRiver(int x, int y){
		if(map[y][x] == 'r')
			return true;
		else return false;
	}
	boolean isPath(int x, int y){
		if(map[y][x] == 'p')
			return true;
		else return false;
	}
	boolean isGrass(int x, int y){
		if(map[y][x] == 'g')
			return true;
		else return false;
	}
	boolean inMap(int x, int y){
		return (x>=0 && x<width && y>=0 && y<height);
	}
	char tileTo(dir d, int x, int y){
		char ret = '0';
		switch (d){
		case WEST:
			if(inMap(x-1,y)) ret = map[y][x-1];
			break;
		case EAST:
			if(inMap(x+1,y)) ret = map[y][x+1];
			break;
		case NORTH:
			if(inMap(x,y-1)) ret = map[y-1][x];
			break;
		case SOUTH:
			if(inMap(x,y+1)) ret = map[y+1][x];
			break;
		default:
			break;
		}
		return ret;
	}
	boolean sameToNorth(int x, int y, boolean trueIfEdge){
		if(y == 0 ) return trueIfEdge;
		if(map[y-1][x] == map[y][x])
			return true;
		else return false;
	}
	boolean sameToEast(int x, int y, boolean trueIfEdge){
		if(x == width-1 ) return trueIfEdge;
		if(map[y][x+1] == map[y][x])
			return true;
		else return false;
	}
	boolean sameToSouth(int x, int y, boolean trueIfEdge){
		if(y ==  height-1 ) return trueIfEdge;
		if(map[y+1][x] == map[y][x])
			return true;
		else return false;
	}
	boolean sameToWest(int x, int y, boolean trueIfEdge){
		if(x == 0 ) return trueIfEdge;
		if(map[y][x-1] == map[y][x])
			return true;
		else return false;
	}
	boolean isAdjacent(float fx, float fy, float fx2, float fy2){
		int x = (int) fx; 
		int y = (int) fy; 
		int x2 = (int) fx2; 
		int y2 = (int) fy2;
		//System.out.println("adjacency: " + x + " "+ y + " "+ x2 + " "+ y2 + " ");
		if(x==x2 && Math.abs(y-y2) == 1 ){
			return true;
		}
		if(y==y2 && Math.abs(x-x2) == 1 ){
			return true;
		}
		return false;
	}
	
	ArrayList<Position> pathFind(int x1, int y1, int x2, int y2){
		ArrayList<Position> path = new ArrayList<Position>();
		Position goal = new Position(x2, y2);
		Position head = new Position(x1, y1);
		Position curr = head;
		path.add(head);
		while(!path.get(path.size()-1).equals(goal)){
			ArrayList<Position> neighbors = getNeighbors(curr);
			float minDist = 9999999;
			Position bestPos = curr;
			for(int i=0; i<neighbors.size(); i++){
				float dist = neighbors.get(i).distanceTo(goal);
				
				if(dist<minDist){
					
					minDist = dist;
					bestPos = neighbors.get(i);
				}
			}
			path.add(bestPos);
			curr = bestPos;
			//System.out.println(path);
		}
		return path;
	}
	
	public ArrayList<Position> getNeighbors(Position pos){
		int x = (int)pos.getX();
		int y = (int)pos.getY();
		ArrayList<Position> neighbors = new ArrayList<Position>();
		if(x > 0) neighbors.add(new Position(x-1, y));
		if(y > 0) neighbors.add(new Position(x, y-1));
		if(x < width-1) neighbors.add(new Position(x+1, y));
		if(y < height-1) neighbors.add(new Position(x, y+1));

		return neighbors;
	}
	
	Position getSpritePos(int x, int y){
		Position ret = new Position(0,0);
		ret.setX(x*SPRITE_WIDTH);
		ret.setY(y*SPRITE_HEIGHT + SPRITE_VERT_OFFSET);
		return ret;
	}
	
	
	


	
}
