package com.crh.calculation.mintime.brakepoint;

import java.util.ArrayList;
import java.util.List;

import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Curveback;
import com.crh2.javabean.Slope;
import com.crh2.javabean.Slopeback;
import com.crh2.service.DataService;
import com.crh2.util.Arith;

/**
 * �������У����ڼ������һ���ƶ���
 * ͨ����ȡ���ݿ����µ�����������ݣ��޸�Cp����ֵ
 * @author huhui
 *
 */
public class UpdateCpReverseMinTime {
	/**
	 * ����µ�����
	 */
	private List<Slopeback> slopeListReverse = null;
	/**
	 * ����������
	 */
	private List<Curveback> curveListReverse = null;
	/**
	 *  ��¼�µ��±�
	 */
	private int slopeFlag = 0;
	/**
	 *  ��¼����±�
	 */
	private int curveFlag = 0;
	/**
	 * ��¼�¶�ֵ
	 */
	private double slope = 0;
	/**
	 * ��¼���ֵ
	 */
	private double curve = 0;

	/**
	 *  ���캯������ʼ��List
	 * @param slopeList
	 * @param curveList
	 */
	public UpdateCpReverseMinTime(List<Slope> slopeList,List<Curve> curveList) {
		DataService ds = new DataService();
		// ����ǣ��ʱ������
		this.slopeListReverse = this.slopeListConvertor(slopeList);
		this.curveListReverse = this.curveListConvertor(curveList);
	}
	
	/**
	 * ���ڽ�slopeListת��ΪslopeListReverse������ת����
	 * @param slopeList
	 * @return
	 */
	public List<Slopeback> slopeListConvertor(List<Slope> slopeList){
		List<Slopeback> slopeListReverse = new ArrayList<Slopeback>();
		//�ȸ���0����¼��ֵ
		int t = 0;
		Slopeback slopeback = new Slopeback();
		slopeback.setId(0);
		slopeback.setSlope(0);
		slopeback.setStart(0);
		slopeListReverse.add(slopeback);
		
		double slopeValue = 0;
		double sumLength = 0;
		for(int i=slopeList.size()-1;i>0;i--){
			t++;
			slopeback = new Slopeback();
			Slope slope = slopeList.get(i);
			sumLength += slope.getLength();
			slopeValue = slopeList.get(i-1).getSlope();
			slopeback.setId(t);
			slopeback.setSlope(Arith.negate(slopeValue));
			slopeback.setStart(sumLength/1000);
			slopeListReverse.add(slopeback);
		}
		return slopeListReverse;
	}
	
	/**
	 * ���ڽ�curveListת��ΪcurveListReverse
	 * @param curveList
	 * @return
	 */
	public List<Curveback> curveListConvertor(List<Curve> curveList){
		List<Curveback> curveListReverse = new ArrayList<Curveback>();
		int t = 0;
		for(int i=curveList.size()-1;i>=0;i--){
			Curveback curveback = new Curveback();
			Curve curve = curveList.get(i);
			curveback.setId(t);
			curveback.setRadius(curve.getRadius());
			curveback.setLength(curve.getLength());
			curveback.setSpeedLimit(curve.getSpeedLimit());
			curveback.setStart(TrainAttribute.CRH_DESTINATION_LOCATION - curve.getStart() - curve.getLength()/1000);
			curveListReverse.add(curveback);
			t++;
		}
		return curveListReverse;
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
		for (int i = index; i < curveListReverse.size(); i++) {
			curveStart = curveListReverse.get(i).getStart();
			if (Sn >= curveStart) {// �������
				curveFlag = i;
				curve = 600 / Math.abs(curveListReverse.get(i).getRadius());
				newCp = newCp - curve;
//				break;
			}
		}
		return newCp;
	}

	/**
	 * �ж��Ƿ񵽴���һ������������������byCurve
	 * @param Sn
	 * @param index
	 * @return
	 */
	public boolean isGetNextCurve(double Sn, int index) {
		if (index >= curveListReverse.size()) {
			return false;
		}
		if (Sn >= curveListReverse.get(index).getStart()) {
//			System.out.println("����� "+index+" �������Sn= "+Sn);
			return true;// ����
		} else {
			return false;// δ����
		}
	}
	
	/**
	 * �ж��Ƿ񳬹���һ��������ٶȣ�������������Ѹ��������ٶ����У����򣬼�������
	 */
	public double overSpeedNextCurve(double speed, int index) {
		if (index >= curveListReverse.size()) {
			return speed;
		}
		double speedLimit = curveListReverse.get(index).getSpeedLimit();
		if (speed >= speedLimit) {
			return speedLimit;// ����
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
		if(index == curveListReverse.size()){
			--index;
		}
		if (Sn > curveListReverse.get(index).getStart() + curveListReverse.get(index).getLength() / 1000) {
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
		for (int i = index; i < slopeListReverse.size(); i++) {
			slopeStart = slopeListReverse.get(i).getStart();
			if (Sn >= slopeStart) {
				slopeFlag = i;
				slope = slopeListReverse.get(i).getSlope();
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
		if (index >= slopeListReverse.size()) {
			return false;
		}
		double startPoint = slopeListReverse.get(index).getStart();
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
