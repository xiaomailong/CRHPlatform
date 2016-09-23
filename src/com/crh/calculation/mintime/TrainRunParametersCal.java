package com.crh.calculation.mintime;

import com.crh.calculation.mintime.brakepoint.SnBpMinTime;
import com.crh.calculation.mintime.brakepoint.VnBpMinTime;
import com.crh.service.MinTimeSimulationService;
import com.crh.service.TrainEditPanelService;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.javabean.TrainTractionConf;

/**
 * �������в���ָ��
 * @author huhui
 */
public class TrainRunParametersCal {

    private int trainCategoryId;

    /**
     * ���캯��
     * @param trainCategoryId
     */
    public TrainRunParametersCal(int trainCategoryId) {
        this.trainCategoryId = trainCategoryId;
        TrainAttribute.CRH_MAX_SPEED = TrainEditPanelService.getTrainInfoByCategoryId(trainCategoryId).getMaxV();
        initFormulas();
    }

    /**
     * ��ǣ�����������������ƶ�����ʽ��ֵ
     */
    public void initFormulas() {
        this.initTractionForce();
        this.initAirFriction();
        this.initBrakeForce();
    }

    /**
     * ����ǣ����
     */
    public void initTractionForce() {
        TrainTractionConf trainTractionConf = MinTimeSimulationService.getTrainTractionConf(trainCategoryId);
        TractionForceMinTime.Fst = trainTractionConf.getFst();
        TractionForceMinTime.F1 = trainTractionConf.getF1();
        TractionForceMinTime.F2 = trainTractionConf.getF2();
        TractionForceMinTime.v1 = trainTractionConf.getV1();
        TractionForceMinTime.v2 = trainTractionConf.getV2();
        TractionForceMinTime.P1 = trainTractionConf.getP1();
        TractionForceMinTime.mode = 1;
    }

    /**
     * ���ÿ�������
     */
    public void initAirFriction() {
        double[] abc = MinTimeSimulationService.getAirFrictionParameters(trainCategoryId);
        AirFrictionMinTime.a = abc[0];
        AirFrictionMinTime.b = abc[1];
        AirFrictionMinTime.c = abc[2];
        AirFrictionMinTime.m = abc[4];
    }

    /**
     * �����ƶ���
     */
    public void initBrakeForce() {
        BrakeForceMinTime.setBrakeList(MinTimeSimulationService.getTrainBrakeFactor(trainCategoryId));//�ƶ���ϵ��
        TrainElectricBrake trainElectricBrake = MinTimeSimulationService.getTrainElectricBrake(trainCategoryId);
        double[] para = MinTimeSimulationService.getSixParameters(trainCategoryId);//k1,k2,D,N,T,F2
        double k1 = para[0];
        double k2 = para[1];
        double D = para[2];
        double N = para[3];
        double T = para[4];
        BrakeForceMinTime.v1 = trainElectricBrake.getV1();
        BrakeForceMinTime.v2 = trainElectricBrake.getV2();
        BrakeForceMinTime.F2 = (N * trainElectricBrake.getP00() * 3.6) / BrakeForceMinTime.v2;
        BrakeForceMinTime.Fst = ((2 * N * k1 * k2 * T) / 1000) / D;
    }

    /**
     * �������ָ��
     **/
    public double[] forwardParameters(double speedLimit) {
        int i = 0;//ʱ��
        double currentSpeed = 0;// �൱��Vi+1
        double lastSpeed = 0;// �൱��Vi
        double Si = 0, S = 0;// ���
        double power = 0;//����
        double avgAcc = 0;//ƽ�����ٶ�
        double aveRAcc = 0;//ʣ����ٶ�
        double[] parameters = new double[5];
        CpMinTime.hasElectricity = true;
        while (true) {
            currentSpeed = VnMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, 0);//�ٶ�
            Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);//���
            power += TractionForceMinTime.getTractionForce(currentSpeed) * (Si - S);//��������
            i++;
            avgAcc = currentSpeed / (3.6 * i);
            lastSpeed = currentSpeed;
            S = Si;
            if (currentSpeed >= speedLimit) {
                aveRAcc = (TractionForceMinTime.getTractionForce(speedLimit) - AirFrictionMinTime.getAirFriction(speedLimit)) / (AirFrictionMinTime.m * 1000);
                parameters[0] = i;
                parameters[1] = Si;
                parameters[2] = power;
                parameters[3] = avgAcc;
                parameters[4] = aveRAcc;
                break;
            }
        }
        return parameters;
    }

    /**
     * �������ָ��
     **/
    public double[] reverseParameters(double speedLimit) {
        int i = 0;
        double currentSpeed = 0;// �൱��Vi+1
        double lastSpeed = 0;// �൱��Vi
        double Si = 0, S = 0;// �൱��Si+1
        double power = 0;//��������
        double avgAcc = 0;//ƽ�����ٶ�
        double[] parameters = new double[4];
        while (true) {
            currentSpeed = VnBpMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, 0);
            Si = SnBpMinTime.calShift(currentSpeed, lastSpeed, Si);
            i++;
            avgAcc = currentSpeed / (3.6 * i);
            power += BrakeForceMinTime.getComBrakingForce(currentSpeed) * (Si - S);//������������
            lastSpeed = currentSpeed;
            S = Si;
            if (currentSpeed >= speedLimit) {
                parameters[0] = i;
                parameters[1] = Si;
                parameters[2] = Double.isNaN(power) ? 0 : power;
                parameters[3] = avgAcc;
                break;
            }
        }
        return parameters;
    }

}
