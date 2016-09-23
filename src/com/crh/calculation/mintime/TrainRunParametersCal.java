package com.crh.calculation.mintime;

import com.crh.calculation.mintime.brakepoint.SnBpMinTime;
import com.crh.calculation.mintime.brakepoint.VnBpMinTime;
import com.crh.service.MinTimeSimulationService;
import com.crh.service.TrainEditPanelService;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.javabean.TrainTractionConf;

/**
 * 计算运行参数指标
 * @author huhui
 */
public class TrainRunParametersCal {

    private int trainCategoryId;

    /**
     * 构造函数
     * @param trainCategoryId
     */
    public TrainRunParametersCal(int trainCategoryId) {
        this.trainCategoryId = trainCategoryId;
        TrainAttribute.CRH_MAX_SPEED = TrainEditPanelService.getTrainInfoByCategoryId(trainCategoryId).getMaxV();
        initFormulas();
    }

    /**
     * 给牵引力、空气阻力、制动力公式赋值
     */
    public void initFormulas() {
        this.initTractionForce();
        this.initAirFriction();
        this.initBrakeForce();
    }

    /**
     * 设置牵引力
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
     * 设置空气阻力
     */
    public void initAirFriction() {
        double[] abc = MinTimeSimulationService.getAirFrictionParameters(trainCategoryId);
        AirFrictionMinTime.a = abc[0];
        AirFrictionMinTime.b = abc[1];
        AirFrictionMinTime.c = abc[2];
        AirFrictionMinTime.m = abc[4];
    }

    /**
     * 设置制动力
     */
    public void initBrakeForce() {
        BrakeForceMinTime.setBrakeList(MinTimeSimulationService.getTrainBrakeFactor(trainCategoryId));//制动力系数
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
     * 正向计算指标
     **/
    public double[] forwardParameters(double speedLimit) {
        int i = 0;//时间
        double currentSpeed = 0;// 相当于Vi+1
        double lastSpeed = 0;// 相当于Vi
        double Si = 0, S = 0;// 里程
        double power = 0;//能量
        double avgAcc = 0;//平均加速度
        double aveRAcc = 0;//剩余加速度
        double[] parameters = new double[5];
        CpMinTime.hasElectricity = true;
        while (true) {
            currentSpeed = VnMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, 0);//速度
            Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);//里程
            power += TractionForceMinTime.getTractionForce(currentSpeed) * (Si - S);//计算做功
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
     * 反向计算指标
     **/
    public double[] reverseParameters(double speedLimit) {
        int i = 0;
        double currentSpeed = 0;// 相当于Vi+1
        double lastSpeed = 0;// 相当于Vi
        double Si = 0, S = 0;// 相当于Si+1
        double power = 0;//阻力做功
        double avgAcc = 0;//平均加速度
        double[] parameters = new double[4];
        while (true) {
            currentSpeed = VnBpMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, 0);
            Si = SnBpMinTime.calShift(currentSpeed, lastSpeed, Si);
            i++;
            avgAcc = currentSpeed / (3.6 * i);
            power += BrakeForceMinTime.getComBrakingForce(currentSpeed) * (Si - S);//计算阻力做功
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
