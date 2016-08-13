package com.crh.calculation.mintime.brakepoint;

import java.util.List;

import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Slope;

/**
 * 2014.10.23 ���յ�վ������������㸽������
 * @author huhui
 */
public class ReverseOtherResistance {
	/**
	 * ����µ�����
	 */
	private static List<Slope> slopeList = null;
	/**
	 * ����������
	 */
	private static List<Curve> curveList = null;

	/**
	 * ���캯������ʼ��List
	 * @param slopeList
	 * @param curveList
	 */
	public ReverseOtherResistance(List<Slope> slopeList,List<Curve> curveList) {
		// ����ǣ��ʱ������
		ReverseOtherResistance.slopeList = slopeList;
		ReverseOtherResistance.curveList = curveList;
	}
	
	/**
	 * �����������Cp
	 * @param v ��ǰ�ٶ�
	 * @param s ��ǰ���
	 * @return ���� array[0]->�ٶ�, array[1]->����������� 
	 */
	public static double [] byCurve(double v, double s) {
		double location = TrainAttribute.CRH_DESTINATION_LOCATION - s;
		double [] paras = new double[2];
		double curveCp = 0;
		int size = curveList.size();
		for (int i = size - 1; i >= 0; i--) {
			Curve bean = curveList.get(i);
			double curveStart = bean.getStart(); //��������
			double curveEnd = bean.getStart() + bean.getLength() / 1000.0; //����յ�
			if(location < curveStart){
				continue;
			}else{
				double speedLimit = bean.getSpeedLimit();
				if(v > speedLimit){
					v = speedLimit;
				}
				if(location >= curveEnd && location <= curveStart){
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
	 *  �µ��޸�Cp
	 * @param s
	 * @return
	 */
	public static double bySlope(double s) {
		double location = TrainAttribute.CRH_DESTINATION_LOCATION - s;
		double slopeCp = 0;
		int size = slopeList.size();
		for (int i = size - 1; i >= 0; i--) {
			Slope bean = slopeList.get(i);
			double slopeStart = bean.getEnd() - bean.getLength() / 1000.0;
			if(location < slopeStart){
				continue;
			}else{
				slopeCp = bean.getSlope();
				break;
			}
		}
		return slopeCp;
	}

}
