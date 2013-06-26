package com.aeviou.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.aeviou.key.AbstractKey;

public class HashGrid2 {

	private float overdx;
	HashMap<V2i,ArrayList<Integer>> grid=new HashMap<V2i,ArrayList<Integer>>();
	class V2i{
		public int arr[]=new int[2];
		
		public V2i(int x,int y){
			set(x,y);
		}
		
		V2i set(int x,int y){
			arr[0]=x;
			arr[1]=y;
			return this;
		}
		
		//default JVM use address, so same obj has same hashcode
		public int hashCode(){
			return Arrays.hashCode(arr);
		}
		
		public boolean equals(Object other){
			return Arrays.equals(arr, ((V2i)other).arr);
		}
	};

	
	public HashGrid2(float dx){
		overdx=1/dx;
	}
	
	public void add_box(AbstractKey key,int index){
		int min_x=(int) (key.x*overdx),min_y=(int) (key.y*overdx);
		int max_x=(int) ((key.x+key.width)*overdx),max_y=(int) ((key.y+key.height)*overdx);
		
		for(int i=min_x;i<=max_x;i++){
			for(int j=min_y;j<=max_y;j++){
				V2i v2i=new V2i(i,j);
				ArrayList<Integer> list=grid.get(v2i);
				if(list==null){
					list=new ArrayList<Integer>();
					grid.put(v2i, list);
				}
				list.add(index);
			}
		}
	}
	
	 V2i v2i=new V2i(0,0);
	  public void find_point(int x,int y, ArrayList<Integer> result) 
	   {
		  v2i.set((int) (x*overdx), (int) (y*overdx));
		  //V2i v2i=new V2i((int) (x*overdx), (int) (y*overdx));
		  ArrayList<Integer> list=grid.get(v2i);
		  if(list!=null)result.addAll(list);
	   }
}
