package com.sdll18.data;

public class MyPoint {

	public double x;
	public double y;

	public MyPoint() {
		x = 0.0;
		y = 0.0;
	}

	public MyPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}

	@Override
	public String toString() {
		return "MyPoint [x=" + x + ", y=" + y + "]";
	}
	

}
