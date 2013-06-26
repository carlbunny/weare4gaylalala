package com.aeviou.utils;

public class Vector2i {
	public int x;
	public int y;
	
	public Vector2i(){
		x=y=0;
	}
	
	public Vector2i(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public void set(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public void add(int x,int y){
		this.x+=x;
		this.y+=y;
	}
	 
	public void sub(int x,int y){
		this.x-=x;
		this.y-=y;
	}
	
	public boolean isZero(){
		return x==0&&y==0;
	}
	
	
}
