package com.crh.calculation.mintime;

/**
 * ����ǣ����
 * @author huhui
 *
 */
public class TractionForceMinTime {
	/**
	 * ǣ�������㹫ʽ�еĲ���
	 */
	public static double Fst = 0;
	public static double F1 = 0;
	public static double F2 = 0;
	public static double v1 = 0;
	public static double v2 = 0;
	public static double P1 = 0;
	public static double mode = 0;
	
	/**
	 * 2015.1.18���ӣ�0Ϊ�޼�(Ĭ��)��1Ϊ�м�
	 */
	public static int tractionType = 0;
	/**
	 * ǣ������
	 */
	public static int level = 0;

	/**
	 *  ����ǣ������ǣ��״̬�£�
	 * @param speed ��λkm/h
	 * @return ǣ��������λ��N
	 */
	public static double getTractionForce(double speed) {//speed��λkm/h
		double F = 0.0;
		double v = speed;
		if (tractionType == 0) {//�޼�(Ĭ��)
			if (v >= 0 && v <= v1) {
				F = 1000 * (Fst - (((Fst - F1) / v1) * v));
			} else if (v > v1 && v <= v2) {
				F = 1000 * ((3.6 * P1) / v);
			} else if (v > v2) {
				F = 1000 * ((F2 * v2 * v2) / (v * v));
			}
			return F * mode;// ��λ��N
		}else if(tractionType == 1){//�м�
			F = TractionLevelForceMinTime.getTractionLevelForce(level, v) * 1000;// ��λ��N
			return F;
		}else{
			return 0;
		}
	}
}
