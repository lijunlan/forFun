package com.sdll18.tool;

public class MyTools {

	public static int getQuadrant(int originX,int originY,int x,int y){
		if(x>originX&&y>=originY)return 1;
		if(y>originY&&x<=originX)return 2;
		if(x<originX&&y<=originY)return 3;
		if(x>=originX&&y<originY)return 4;
		return 0;
	}
	
	public static double[] rotate(double coss,double sinn,double[] coordi){
		double[][] changeCoordi = new double[3][3];
		double[] answerCoordi = new double[3];
		changeCoordi[0][0] = coss;
		changeCoordi[1][0] = -sinn;
		changeCoordi[2][0] = 0.0;
		changeCoordi[0][1] = sinn;
		changeCoordi[1][1] = coss;
		changeCoordi[2][1] = 0.0;
		changeCoordi[0][2] = 0.0;
		changeCoordi[1][2] = 0.0;
		changeCoordi[2][2] = 1.0;
		for(int i = 1;i<=3;i++){
			double sum = 0.0;
			for(int j = 1;j<=3;j++){
				sum += changeCoordi[j-1][i-1] * coordi[j-1];
			}
			answerCoordi[i-1] = sum;
		}
		return answerCoordi;
	}
}
