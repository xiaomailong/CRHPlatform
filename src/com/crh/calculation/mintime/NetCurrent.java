package com.crh.calculation.mintime;

import com.crh.service.AuxiliaryPowerSupplyPanelService;
import com.crh.service.TrainEditPanelService;

/**
 * 2014.12.1 ��������
 * @author huhui
 */
public class NetCurrent {
	
	/**
	 * ��ѹ
	 */
	private double u = 0;
	/**
	 * �ļ���������
	 */
	private double summerPower = 0;
	/**
	 * ������������
	 */
	private double winterPower = 0;
	
	public NetCurrent(int trainCategoryId){
		u = TrainEditPanelService.getTrainInfoByCategoryId(trainCategoryId).getA200() * 1000;//��ȡ��ѹ����27.5kv
		double [] para = AuxiliaryPowerSupplyPanelService.getDocPara(trainCategoryId);//�õ��ǽ�������
		summerPower = para[0] * 1000;
		winterPower = para[3] * 1000;
	}
	
	/**
	 * ����ǣ��״̬�µ�����
	 * @param tractionForce
	 * @param v �ٶ�
	 * @return
	 */
	public double getTractionNetCurrent(double tractionForce, double v){
		tractionForce = tractionForce * 1000;// N
		v = v * 3.6;// m/s
		return (tractionForce * v + summerPower)/u;
	}
	
	/**
	 * �����ƶ�״̬�µ�����(��ֵ)
	 * @param comBrakeForce �ȽϺ���ƶ���
	 * @param v
	 * @return
	 */
	public double getComBrakeForceNetCurrent(double comBrakeForce, double v){
		comBrakeForce = comBrakeForce * 1000;// N
		v = v * 3.6;// m/s
		return -((comBrakeForce * v + winterPower)/u);
	}

}
