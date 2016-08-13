package com.crh.calculation.mintime;

import java.util.List;

import com.crh2.javabean.Curve;
import com.crh2.javabean.Slope;

/**
 * ͨ����ȡ���ݿ����µ�����������ݣ��޸�Cp����ֵ������������
 * @author huhui
 * @date 2014.10.23
 */
public class OtherResistance {
	/**
	 *  ����µ�����
	 */
	private static List<Slope> slopeList = null;
	/**
	 * ����������
	 */
	private static List<Curve> curveList = null;

	/**
	 *  ���캯������ʼ��List
	 * @param slopeList
	 * @param curveList
	 */
	public OtherResistance(List<Slope> slopeList,List<Curve> curveList) {
		OtherResistance.slopeList = slopeList;
		OtherResistance.curveList = curveList;
	}

	/**
	 * �������s���õ������������Cp
	 * @param v ��ǰ�ٶ�
	 * @param s ��ǰ���
	 * @return ���� array[0]->�ٶ�, array[1]->����������� 
	 */
	public static double [] byCurve(double v, double s) {
		double [] paras = new double[2];
		double curveCp = 0;
		for (Curve bean : curveList) {
			double curveStart = bean.getStart(); //��������
			double curveEnd = curveStart + bean.getLength() / 1000.0; //����յ�
			if(s > curveEnd){
				continue; //Ѱ���г���Ҫ��������
			}else{
				double speedLimit = bean.getSpeedLimit(); //�������
				if(v > speedLimit){
					v = speedLimit;
				}
				if(s >= curveStart && s <= curveEnd){
					curveCp = 600 / Math.abs(bean.getRadius());
				}
				break;
			}
		}
		paras[0] = v;
		paras[1] = curveCp;
		return paras;
	}
	
	/**
	 * �������s���õ��µ���������Cp
	 * @param s ��ǰ���
	 * @return �µ��������� 
	 */
	public static double bySlope(double s) {
		double slopeCp = 0;
		for (Slope bean : slopeList) {
			double slopeEnd = bean.getEnd();
			if(s > slopeEnd){
				continue;
			}else{
				slopeCp = bean.getSlope();
				break;
			}
		}
		return slopeCp;
	}

}
