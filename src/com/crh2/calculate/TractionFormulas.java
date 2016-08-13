package com.crh2.calculate;

import com.crh2.util.MyTools;

/**
 * TractionConfPanel页面对应的曲线公式
 */
public class TractionFormulas {
	
	public static double dv;
	/**
	 * 列车质量，单位 吨t
	 */
	public static double m;
	public static double maxV;
	
	//1.基本空气阻力
	public static double a;
	public static double b;
	public static double c;
	/**
	 * 计算基本空气阻力
	 * @param v
	 * @return
	 */
	public static double airFriction(double v){//速度v是km/h
		double W0 = 0;//单位KN
		W0 = a + b * (v + dv) + c * Math.pow(v + dv, 2);
		return W0;
	}
	
	//2.电流曲线
	public static double vi1,I1;
	public static double vi2,I2;
	public static double vi3,I3;
	/**
	 * 计算电流
	 * @param v 速度v是km/h
	 * @return
	 */
	public static double electricity(double v){
		double I = 0;//单位A
		if(v<11.8){
			I = 0;
		}else if(v>=11.8 && v<118){
			I = 216 - 0.17 * (v - 11.8);
		}else if(v>=118 && v<212){
			I = 198 - 0.66 * (v - 118);
		}else if(v>=212){
			I = 198;
		}
		return I;
	}
	
	//3.电压曲线
	public static double vu1,u1;
	public static double vu2,u2;
	public static double vu3,u3;
	/**
	 * 计算电压
	 * @param v 速度v是km/h
	 * @return
	 */
	public static double voltage(double v){
		double u = 0;//单位是V
		if(v<27.6){
			u = 0;
		}else if(v>=27.6 && v<112){
			u = 100 + 22 * (v - 27.6);
		}else if(v>=112 && v<207){
			u = 1950 + 8.947 * (v - 112);
		}else if(v>=207){
			u = 2800;
		}
		return u/10;//结果缩小10倍
	}
	
	//4.牵引粘着特性公式
	public static double g = 9.8;
	/**
	 * 计算牵引粘着特性：干轨
	 * @param v
	 * @return
	 */
	public static double dryRail(double v){//速度v是km/h
		double result = 0;
		if(v<100){
			result = 0.25;
		}else if(v>=100){
			result = 0.325 - (0.15 * v)/200;
		}
		return result * m * g;
	}
	/**
	 * 计算牵引粘着特性：湿轨
	 * @param v
	 * @return
	 */
	public static double wetRail(double v){//速度v是km/h
		double result = 0;
		if(v<100){
			result = 0.22;
		}else if(v>=100){
			result = 0.295 - (0.15 * v)/200;
		}
		return result * m * g;
	}
	
	//5.坡道曲线
	/**
	 * 坡道计算
	 * @param i
	 * @param v
	 * @return
	 */
	public static double slope(double i, double v){//速度v是km/h
		double result = 0;
		result = airFriction(v) + (i * m * g)/1000;
		return result;
	}
	
	
	//以下是“启动性能所涉及的公式计算”所涉及的公式
	
	//1.加速度公式 a=(F-f)/m
	/**
	 * 计算加速度，公式 a=(F-f)/m
	 * @param v
	 * @return
	 */
	public static double acceleration(double v){//速度v是km/h
		double traction = TractionForce.getTractionForce(v/3.6);//单位是N
		double friction = airFriction(v) * 1000;
		double a = (traction - friction)/(m*1000);
		return a;//单位 m/s^2
	}
	
	//2.速度公式 Vt=V0 + at
	/**
	 * 计算速度，公式 Vt=V0 + at
	 * @param V0
	 * @param a
	 * @return
	 */
	public static double Vt(double V0, double a){//V0单位是km/h
		double Vt = 0;
		if(a!=0){
			Vt = (V0/3.6 + a * 1)*3.6;
		}else{
			Vt = V0;
		}
		if(MyTools.numFormat2(Vt) == MyTools.numFormat2(V0)){
			Vt = -1;//退出计算标志
		}
		return Vt;//单位 km/h
	}
	
	//3.位移公式
	/**
	 * 计算位移，Vt和V0都是km/h  S0为km
	 * @param S0
	 * @param Vt
	 * @param V0
	 * @param a
	 * @return
	 */
	public static double St(double S0, double Vt, double V0, double a){//Vt和V0都是km/h  S0为km
		double St = 0;
		if(a != 0){
			St = (S0*1000 + (Math.pow(Vt/3.6, 2) - Math.pow(V0/3.6, 2))/(2 * a))/1000;
		}else{
			St = (S0*1000 + Vt/3.6 * 1)/1000;
		}
		return St;//单位km
	}
	
}
