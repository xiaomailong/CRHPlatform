package com.crh2.calculate;

import com.crh2.util.MyTools;

/**
 * TractionConfPanelҳ���Ӧ�����߹�ʽ
 */
public class TractionFormulas {
	
	public static double dv;
	/**
	 * �г���������λ ��t
	 */
	public static double m;
	public static double maxV;
	
	//1.������������
	public static double a;
	public static double b;
	public static double c;
	/**
	 * ���������������
	 * @param v
	 * @return
	 */
	public static double airFriction(double v){//�ٶ�v��km/h
		double W0 = 0;//��λKN
		W0 = a + b * (v + dv) + c * Math.pow(v + dv, 2);
		return W0;
	}
	
	//2.��������
	public static double vi1,I1;
	public static double vi2,I2;
	public static double vi3,I3;
	/**
	 * �������
	 * @param v �ٶ�v��km/h
	 * @return
	 */
	public static double electricity(double v){
		double I = 0;//��λA
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
	
	//3.��ѹ����
	public static double vu1,u1;
	public static double vu2,u2;
	public static double vu3,u3;
	/**
	 * �����ѹ
	 * @param v �ٶ�v��km/h
	 * @return
	 */
	public static double voltage(double v){
		double u = 0;//��λ��V
		if(v<27.6){
			u = 0;
		}else if(v>=27.6 && v<112){
			u = 100 + 22 * (v - 27.6);
		}else if(v>=112 && v<207){
			u = 1950 + 8.947 * (v - 112);
		}else if(v>=207){
			u = 2800;
		}
		return u/10;//�����С10��
	}
	
	//4.ǣ��ճ�����Թ�ʽ
	public static double g = 9.8;
	/**
	 * ����ǣ��ճ�����ԣ��ɹ�
	 * @param v
	 * @return
	 */
	public static double dryRail(double v){//�ٶ�v��km/h
		double result = 0;
		if(v<100){
			result = 0.25;
		}else if(v>=100){
			result = 0.325 - (0.15 * v)/200;
		}
		return result * m * g;
	}
	/**
	 * ����ǣ��ճ�����ԣ�ʪ��
	 * @param v
	 * @return
	 */
	public static double wetRail(double v){//�ٶ�v��km/h
		double result = 0;
		if(v<100){
			result = 0.22;
		}else if(v>=100){
			result = 0.295 - (0.15 * v)/200;
		}
		return result * m * g;
	}
	
	//5.�µ�����
	/**
	 * �µ�����
	 * @param i
	 * @param v
	 * @return
	 */
	public static double slope(double i, double v){//�ٶ�v��km/h
		double result = 0;
		result = airFriction(v) + (i * m * g)/1000;
		return result;
	}
	
	
	//�����ǡ������������漰�Ĺ�ʽ���㡱���漰�Ĺ�ʽ
	
	//1.���ٶȹ�ʽ a=(F-f)/m
	/**
	 * ������ٶȣ���ʽ a=(F-f)/m
	 * @param v
	 * @return
	 */
	public static double acceleration(double v){//�ٶ�v��km/h
		double traction = TractionForce.getTractionForce(v/3.6);//��λ��N
		double friction = airFriction(v) * 1000;
		double a = (traction - friction)/(m*1000);
		return a;//��λ m/s^2
	}
	
	//2.�ٶȹ�ʽ Vt=V0 + at
	/**
	 * �����ٶȣ���ʽ Vt=V0 + at
	 * @param V0
	 * @param a
	 * @return
	 */
	public static double Vt(double V0, double a){//V0��λ��km/h
		double Vt = 0;
		if(a!=0){
			Vt = (V0/3.6 + a * 1)*3.6;
		}else{
			Vt = V0;
		}
		if(MyTools.numFormat2(Vt) == MyTools.numFormat2(V0)){
			Vt = -1;//�˳������־
		}
		return Vt;//��λ km/h
	}
	
	//3.λ�ƹ�ʽ
	/**
	 * ����λ�ƣ�Vt��V0����km/h  S0Ϊkm
	 * @param S0
	 * @param Vt
	 * @param V0
	 * @param a
	 * @return
	 */
	public static double St(double S0, double Vt, double V0, double a){//Vt��V0����km/h  S0Ϊkm
		double St = 0;
		if(a != 0){
			St = (S0*1000 + (Math.pow(Vt/3.6, 2) - Math.pow(V0/3.6, 2))/(2 * a))/1000;
		}else{
			St = (S0*1000 + Vt/3.6 * 1)/1000;
		}
		return St;//��λkm
	}
	
}
