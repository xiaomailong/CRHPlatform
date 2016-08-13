package com.crh.calculation.mintime;

import java.util.List;

import com.crh2.javabean.Curve;
import com.crh2.javabean.Slope;

/**
 * ͨ����ȡ���ݿ����µ�����������ݣ��޸�Cp����ֵ
 * @author huhui
 */
public class UpdateCpMinTime {
	/**
	 * ����µ�����
	 */
	private List<Slope> slopeList = null;
	/**
	 * ����������
	 */
	private List<Curve> curveList = null;
	/**
	 * ���ڼ�¼�µ��±�
	 */
	private int slopeFlag = 0;
	/**
	 * ���ڼ�¼����±�
	 */
	private int curveFlag = 0;
	/**
	 * ���ڼ�¼�¶�ֵ
	 */
	private double slope = 0;
	/**
	 * ���ڼ�¼���ֵ
	 */
	private double curve = 0;

	/**
	 *  ���캯������ʼ��List
	 * @param slopeList
	 * @param curveList
	 */
	public UpdateCpMinTime(List<Slope> slopeList,List<Curve> curveList) {
		// ����ǣ��ʱ������
		this.slopeList = slopeList;
		this.curveList = curveList;

	}

	/**
	 *  ����޸�Cp
	 * @param Cp
	 * @param Sn
	 * @param index
	 * @return
	 */
	public double byCurve(double Cp, double Sn, int index) {
		if(index != 0){
			index = index - 1;
		}
		double newCp = Cp;
		double curveStart = 0;// ��¼ÿ����������
		for (int i = index; i < curveList.size(); i++) {
			curveStart = curveList.get(i).getStart();
			if (Sn >= curveStart) {// �������
				curveFlag = i;
				curve = 600 / Math.abs(curveList.get(i).getRadius());
				newCp = newCp - curve;
//				break;
			}
		}
		return newCp;
	}

	/**
	 *  �ж��Ƿ񵽴���һ������������������byCurve
	 * @param Sn
	 * @param index
	 * @return
	 */
	public boolean isGetNextCurve(double Sn, int index) {
		if (index >= curveList.size()) {
			return false;
		}
		if (Sn >= curveList.get(index).getStart()) {
			return true;// ����
		} else {
			return false;// δ����
		}
	}
	
	/**
	 * �ж��Ƿ񳬹���һ��������ٶȣ�������������Ѹ��������ٶ����У����򣬼�������
	 */
	public double overSpeedNextCurve(double speed, int index) {
		if (index >= curveList.size()) {
			return speed;
		}
		double speedLimit = curveList.get(index).getSpeedLimit();
		if (speed >= speedLimit) {
			if(index == 21 || index == 22){//��ֹ�ٶȴ�300->250
				return speed;
			}else{
				return speedLimit;// ����
			}
		} else {
			return speed;// δ����    speed��speedLimit����km/h
		}
	}

	/**
	 *  �ж���������ڻ��������
	 * @param Sn
	 * @param index
	 * @return
	 */
	public boolean isInCurve(double Sn, int index) {
		//�ж��ǲ������һ�����
		if(index == curveList.size()){
			--index;
		}
		if (Sn > curveList.get(index).getStart() + curveList.get(index).getLength() / 1000) {
			return false;// �������
		} else {
			return true;// �������
		}
	}

	/**
	 *  �µ��޸�Cp
	 * @param Cp
	 * @param Sn
	 * @param index
	 * @return
	 */
	public double bySlope(double Cp, double Sn, int index) {
		if(index != 0){
			index = index - 1;
		}
		double newCp = Cp;
		double slopeStart = 0;// ��¼ÿ���µ������
		for (int i = index; i < slopeList.size(); i++) {
			slopeStart = slopeList.get(i).getEnd()- (slopeList.get(i).getLength() / 1000);
			if (Sn >= slopeStart) {
				slopeFlag = i;
				slope = slopeList.get(i).getSlope();
				newCp = newCp - slope;
//				break;
			}
		}
		return newCp;
	}

	/**
	 *  �ж��Ƿ񵽴���һ���µ��������������bySlope
	 * @param Sn
	 * @param index
	 * @return
	 */
	public boolean isGetNextSlope(double Sn, int index) {
		if (index >= slopeList.size()) {
			return false;
		}
		double startPoint = slopeList.get(index).getEnd() - (slopeList.get(index).getLength() / 1000);
		if (Sn >= startPoint) {
//			System.out.println("����� "+index+" ���µ���Sn= "+Sn);
			return true;// �ﵽ
		} else {
			return false;// δ����
		}
	}

	public double getCurve() {
		return curve;
	}

	public double getSlope() {
		return slope;
	}

	public int getSlopeFlag() {
		return slopeFlag;
	}

	public int getCurveFlag() {
		return curveFlag;
	}
}
