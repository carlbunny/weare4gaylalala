package com.aeviou.key;

import java.util.ArrayList;

import com.aeviou.utils.HashGrid2;

public class KeySet {
	public AbstractKey[] keys=null;
	private HashGrid2 grid=null;
	private ArrayList<Integer> result=new ArrayList<Integer>();
	
	public KeySet(AbstractKey[] keys){
		this.keys=keys;
		float dx=Math.min(keys[0].width, keys[0].height)*0.5f;
		grid=new HashGrid2(dx);
		for(int i=0;i<keys.length;i++){
			grid.add_box(keys[i], i);
		}
	}
	
	public AbstractKey getKeyByPoint(int x,int y){
		result.clear();
		grid.find_point(x, y, result);
	 // Log.v("aeviou", String.valueOf(result.size()));
	  
		for (int i : result){
			if (keys[i].inGeometry(x, y)) {
				return keys[i];
			}
		}
		return null;
	}
}
