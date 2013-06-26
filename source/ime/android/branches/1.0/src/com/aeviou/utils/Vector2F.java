package com.aeviou.utils;

public class Vector2F {
	public float x;
	public float y;
	
	public Vector2F(){
		x=y=0;
	}
	
	public Vector2F(float x,float y){
		this.x=x;
		this.y=y;
	}
	
	public void set(float x,float y){
		this.x=x;
		this.y=y;
	}
	
	public void add(float x,float y){
		this.x+=x;
		this.y+=y;
	}
	 
	public void sub(float x,float y){
		this.x-=x;
		this.y-=y;
	}
	
	public boolean isZero(){
		return x==0&&y==0;
	}
	
	public void normalized(){
		float squareRoot=(float) Math.sqrt(x*x+y*y);
		if(squareRoot!=0){
			this.x/=squareRoot;
			this.y/=squareRoot;
		}
	}
}
