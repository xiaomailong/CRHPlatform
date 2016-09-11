package com.crh.service;

import java.util.ArrayList;
import java.util.List;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.TrainInfo;
import com.crh2.javabean.TrainTractionConf;
import com.crh2.javabean.TrainTractionConfType;
import com.crh2.javabean.TrainTractionLevelConf;

/**
 * 为TractionConfPanel提供数据的增、删、改、查
 * @author huhui
 *
 */
public class TractionConfPanelService {

    private static SQLHelper sqlHelper = new SQLHelper();

    /**
     * 保存train_traction_conf
     * @param trainTractionConf
     * @param trainCategoryId
     * @param tractionId
     * @param actionType 0表示修改现有方案，1表示增加方案
     * @return
     */
    public static boolean saveTrainTractionConf(TrainTractionConf trainTractionConf, int trainCategoryId, int tractionId, int actionType, int type) {
        int newType = 0;
        if (actionType == 0) {
            newType = type;
            String deleteSQL = "DELETE FROM train_traction_conf WHERE train_category_id = " + trainCategoryId + " AND id = " + tractionId;
            sqlHelper.update(deleteSQL, null);
        } else if (actionType == 1) {
            newType = getNextTractionType(trainCategoryId);
        }
        boolean b = false;
        if (newType < 6) {
            String insertSQL = "insert into train_traction_conf values(null," + trainTractionConf.getK1() + "," + trainTractionConf.getK2() + ","
                               + trainTractionConf.getD() + "," + trainTractionConf.getN0() + "," + trainTractionConf.getN2() + "," + trainTractionConf.getN()
                               + "," + trainTractionConf.getT() + "," + trainTractionConf.getP0() + "," + trainTractionConf.getP1() + ","
                               + trainTractionConf.getV1() + "," + trainTractionConf.getV2() + "," + trainTractionConf.getF1() + ","
                               + trainTractionConf.getF2() + "," + trainTractionConf.getFst() + "," + trainTractionConf.getVi1() + ","
                               + trainTractionConf.getI1() + "," + trainTractionConf.getVi2() + "," + trainTractionConf.getI2() + ","
                               + trainTractionConf.getVi3() + "," + trainTractionConf.getI3() + "," + trainTractionConf.getVu1() + ","
                               + trainTractionConf.getU1() + "," + trainTractionConf.getVu2() + "," + trainTractionConf.getU2() + ","
                               + trainTractionConf.getVu3() + "," + trainTractionConf.getU3() + "," + trainCategoryId + "," + trainTractionConf.getD0() + ","
                               + trainTractionConf.getD1() + ")";
            int newTractionId = sqlHelper.insertAndGetId(insertSQL);
            String sqlStr = "insert into train_traction_conf_type values(null," + newType + "," + newTractionId + "," + trainCategoryId + ",0)";
            sqlHelper.update(sqlStr, null);
            b = true;
        }
        return b;
    }

    /**
     * 根据trainCategoryId获取train_traction_conf_type中所对应的数据
     * @param trainCategoryId
     * @return
     */
    public static ArrayList<TrainTractionConfType> getTrainTractionConfType(int trainCategoryId) {
        ArrayList<TrainTractionConfType> typeList = new ArrayList<TrainTractionConfType>();
        String sql = "SELECT * FROM train_traction_conf_type WHERE train_category_id = " + trainCategoryId + " ORDER BY TYPE";
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                TrainTractionConfType bean = new TrainTractionConfType();
                bean.setId(Integer.parseInt(obj[0].toString()));
                bean.setType(Integer.parseInt(obj[1].toString()));
                bean.setTractionId(Integer.parseInt(obj[2].toString()));
                bean.setTrainCategoryId(Integer.parseInt(obj[3].toString()));
                typeList.add(bean);
            }
        }
        return typeList;
    }

    /**
     * 将train_traction_conf_type的select字段更新为1
     * @param type
     * @param trainCategoryId
     */
    public static void updateTrainTractionConfType(int type, int trainCategoryId) {
        String sql = "UPDATE train_traction_conf_type t SET t.select = 0 WHERE t.train_category_id = " + trainCategoryId; //先复位
        sqlHelper.update(sql, null);
        sql = "UPDATE train_traction_conf_type t SET t.select = 1 WHERE t.type = " + type + " AND t.train_category_id = " + trainCategoryId;
        sqlHelper.update(sql, null);
    }

    /**
     * 根据trainCategoryId获取train_traction_conf_type中type字段最大值，返回下一个type值
     * @param trainCategoryId
     * @return
     */
    public static int getNextTractionType(int trainCategoryId) {
        String sql = "SELECT MAX(TYPE) FROM train_traction_conf_type WHERE train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        int nextType = 1;
        if (((Object[]) list.get(0))[0] != null) {
            nextType = Integer.parseInt(((Object[]) list.get(0))[0].toString()) + 1;
        }
        return nextType;
    }

    /**
     * 根据trainCategoryId获取被选中的牵引配置参数
     * @time 20160910
     */
    public static int getSelectedTractionConf(int trainCategoryId) {
        String sql = "SELECT traction_id FROM train_traction_conf_type WHERE train_category_id=" + trainCategoryId + " AND `select` = 1";
        List list = sqlHelper.query(sql, null);
        int tractionId = 1;
        if (((Object[]) list.get(0))[0] != null) {
            tractionId = Integer.parseInt(((Object[]) list.get(0))[0].toString());
        }
        return tractionId;
    }

    /**
     * 通过列车种类获取牵引配置数据train_traction_conf，如果无牵引配置信息，则返回系统默认的数值
     * @param trainCategoryId
     * @return
     */
    public static TrainTractionConf getTrainTractionConf(int trainCategoryId) {

        String idSQL = "SELECT traction_id FROM train_traction_conf_type t WHERE t.select = 1 AND t.train_category_id = " + trainCategoryId;
        List idList = sqlHelper.query(idSQL, null);
        String sql = "";
        if (idList.size() == 0) {
            sql = "SELECT * FROM train_traction_conf WHERE train_category_id = " + trainCategoryId;
        } else {
            int tractionId = Integer.parseInt(((Object[]) idList.get(0))[0].toString());
            sql = "SELECT * FROM train_traction_conf WHERE train_category_id = " + trainCategoryId + " AND id = " + tractionId;
        }
        List list = sqlHelper.query(sql, null);
        TrainTractionConf trainTractionConf = new TrainTractionConf();
        if (list.size() != 0) {
            trainTractionConf.setId(Integer.parseInt(((Object[]) list.get(0))[0].toString()));
            trainTractionConf.setK1(Double.parseDouble(((Object[]) list.get(0))[1].toString()));
            trainTractionConf.setK2(Double.parseDouble(((Object[]) list.get(0))[2].toString()));
            trainTractionConf.setD(Double.parseDouble(((Object[]) list.get(0))[3].toString()));
            trainTractionConf.setN0(Integer.parseInt(((Object[]) list.get(0))[4].toString()));
            trainTractionConf.setN2(Integer.parseInt(((Object[]) list.get(0))[5].toString()));
            trainTractionConf.setN(Integer.parseInt(((Object[]) list.get(0))[6].toString()));
            trainTractionConf.setT(Double.parseDouble(((Object[]) list.get(0))[7].toString()));
            trainTractionConf.setP0(Double.parseDouble(((Object[]) list.get(0))[8].toString()));
            trainTractionConf.setP1(Double.parseDouble(((Object[]) list.get(0))[9].toString()));
            trainTractionConf.setV1(Double.parseDouble(((Object[]) list.get(0))[10].toString()));
            trainTractionConf.setV2(Double.parseDouble(((Object[]) list.get(0))[11].toString()));
            trainTractionConf.setF1(Double.parseDouble(((Object[]) list.get(0))[12].toString()));
            trainTractionConf.setF2(Double.parseDouble(((Object[]) list.get(0))[13].toString()));
            trainTractionConf.setFst(Double.parseDouble(((Object[]) list.get(0))[14].toString()));
            trainTractionConf.setVi1(Double.parseDouble(((Object[]) list.get(0))[15].toString()));
            trainTractionConf.setI1(Double.parseDouble(((Object[]) list.get(0))[16].toString()));
            trainTractionConf.setVi2(Double.parseDouble(((Object[]) list.get(0))[17].toString()));
            trainTractionConf.setI2(Double.parseDouble(((Object[]) list.get(0))[18].toString()));
            trainTractionConf.setVi3(Double.parseDouble(((Object[]) list.get(0))[19].toString()));
            trainTractionConf.setI3(Double.parseDouble(((Object[]) list.get(0))[20].toString()));
            trainTractionConf.setVu1(Double.parseDouble(((Object[]) list.get(0))[21].toString()));
            trainTractionConf.setU1(Double.parseDouble(((Object[]) list.get(0))[22].toString()));
            trainTractionConf.setVu2(Double.parseDouble(((Object[]) list.get(0))[23].toString()));
            trainTractionConf.setU2(Double.parseDouble(((Object[]) list.get(0))[24].toString()));
            trainTractionConf.setVu3(Double.parseDouble(((Object[]) list.get(0))[25].toString()));
            trainTractionConf.setU3(Double.parseDouble(((Object[]) list.get(0))[26].toString()));
            trainTractionConf.setTrainCategoryId(Integer.parseInt(((Object[]) list.get(0))[27].toString()));
            trainTractionConf.setD0(Double.parseDouble(((Object[]) list.get(0))[28].toString()));
            trainTractionConf.setD1(Double.parseDouble(((Object[]) list.get(0))[29].toString()));
        }

        return trainTractionConf;
    }

    /**
     * 获取train_traction_conf
     * @param tractionId
     * @param trainCategoryId
     * @return
     */
    public static TrainTractionConf getTrainTractionConf(int tractionId, int trainCategoryId) {
        String sql = "SELECT * FROM train_traction_conf WHERE id= " + tractionId + " and train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        TrainTractionConf trainTractionConf = new TrainTractionConf();
        if (list.size() != 0) {
            trainTractionConf.setId(Integer.parseInt(((Object[]) list.get(0))[0].toString()));
            trainTractionConf.setK1(Double.parseDouble(((Object[]) list.get(0))[1].toString()));
            trainTractionConf.setK2(Double.parseDouble(((Object[]) list.get(0))[2].toString()));
            trainTractionConf.setD(Double.parseDouble(((Object[]) list.get(0))[3].toString()));
            trainTractionConf.setN0(Integer.parseInt(((Object[]) list.get(0))[4].toString()));
            trainTractionConf.setN2(Integer.parseInt(((Object[]) list.get(0))[5].toString()));
            trainTractionConf.setN(Integer.parseInt(((Object[]) list.get(0))[6].toString()));
            trainTractionConf.setT(Double.parseDouble(((Object[]) list.get(0))[7].toString()));
            trainTractionConf.setP0(Double.parseDouble(((Object[]) list.get(0))[8].toString()));
            trainTractionConf.setP1(Double.parseDouble(((Object[]) list.get(0))[9].toString()));
            trainTractionConf.setV1(Double.parseDouble(((Object[]) list.get(0))[10].toString()));
            trainTractionConf.setV2(Double.parseDouble(((Object[]) list.get(0))[11].toString()));
            trainTractionConf.setF1(Double.parseDouble(((Object[]) list.get(0))[12].toString()));
            trainTractionConf.setF2(Double.parseDouble(((Object[]) list.get(0))[13].toString()));
            trainTractionConf.setFst(Double.parseDouble(((Object[]) list.get(0))[14].toString()));
            trainTractionConf.setVi1(Double.parseDouble(((Object[]) list.get(0))[15].toString()));
            trainTractionConf.setI1(Double.parseDouble(((Object[]) list.get(0))[16].toString()));
            trainTractionConf.setVi2(Double.parseDouble(((Object[]) list.get(0))[17].toString()));
            trainTractionConf.setI2(Double.parseDouble(((Object[]) list.get(0))[18].toString()));
            trainTractionConf.setVi3(Double.parseDouble(((Object[]) list.get(0))[19].toString()));
            trainTractionConf.setI3(Double.parseDouble(((Object[]) list.get(0))[20].toString()));
            trainTractionConf.setVu1(Double.parseDouble(((Object[]) list.get(0))[21].toString()));
            trainTractionConf.setU1(Double.parseDouble(((Object[]) list.get(0))[22].toString()));
            trainTractionConf.setVu2(Double.parseDouble(((Object[]) list.get(0))[23].toString()));
            trainTractionConf.setU2(Double.parseDouble(((Object[]) list.get(0))[24].toString()));
            trainTractionConf.setVu3(Double.parseDouble(((Object[]) list.get(0))[25].toString()));
            trainTractionConf.setU3(Double.parseDouble(((Object[]) list.get(0))[26].toString()));
            trainTractionConf.setTrainCategoryId(Integer.parseInt(((Object[]) list.get(0))[27].toString()));
            trainTractionConf.setD0(Double.parseDouble(((Object[]) list.get(0))[28].toString()));
            trainTractionConf.setD1(Double.parseDouble(((Object[]) list.get(0))[29].toString()));
        }

        return trainTractionConf;
    }

    /**
     * 计算空气阻力时，需要a、b、c、dv、m、maxV这几个系数
     * @param trainCategoryId
     * @return
     */
    public static double[] getSomeParametersFromTrainEdit(int trainCategoryId) {
        double[] parameters = new double[6];
        TrainInfo trainInfo = TrainEditPanelService.getTrainInfoByCategoryId(trainCategoryId);
        parameters[0] = trainInfo.getA();
        parameters[1] = trainInfo.getB();
        parameters[2] = trainInfo.getC();
        parameters[3] = trainInfo.getDv();
        parameters[4] = trainInfo.getM();
        parameters[5] = trainInfo.getMaxV();
        return parameters;
    }

    //获取动车数目
    public static int getDynamicTrainNum(int trainCategoryId) {
        int num = 0;
        TrainInfo trainInfo = TrainEditPanelService.getTrainInfoByCategoryId(trainCategoryId);
        num = Integer.parseInt(trainInfo.getPowerConf().substring(0, 1));
        return num;
    }

    /**
     * 保存train_traction_speed200
     * @param speed
     * @param type 1代表牵引，0代表制动
     * @param trainCategoryId
     */
    public static void saveSpeed200(double speed, int type, int trainCategoryId) {
        String delSQL = "DELETE FROM train_traction_speed200 WHERE TYPE = " + type + " AND train_category_id = " + trainCategoryId;
        sqlHelper.update(delSQL, null);
        String sql = "INSERT INTO train_traction_speed200 VALUES(NULL, " + speed + ", " + type + ", " + trainCategoryId + ")";
        sqlHelper.update(sql, null);
    }

    /**
     * 获取speed200， 如果有值则返回，没有则返回默认的200
     * @param type
     * @param trainCategoryId
     * @return
     */
    public static double getSpeed200(int type, int trainCategoryId) {
        double speed = 200;
        String sql = "SELECT speed200 FROM train_traction_speed200 WHERE TYPE = " + type + " AND train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            speed = Double.parseDouble(((Object[]) list.get(0))[0].toString());
        }
        return speed;
    }

    /**
     * 获取整车电机数目
     * @param trainCategoryId
     * @return 整车电机数目
     */
    public static int getSumDynamicAxleNum(int trainCategoryId) {
        int num = 0;
        String sql = "SELECT sumDynamicAxle FROM train_formation WHERE train_category_id = " + trainCategoryId + " LIMIT 1";
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            num = Integer.parseInt(((Object[]) list.get(0))[0].toString());
        }
        return num;
    }

    /**
     * 获取车牵引配置的第一个id号，如果没有则返回1
     * @param trainCategoryId
     * @return
     */
    public static int getFirstTractionId(int trainCategoryId) {
        int firstId = 1;
        String sql = "SELECT id FROM train_traction_conf WHERE train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            firstId = Integer.parseInt(((Object[]) list.get(0))[0].toString());
        }
        return firstId;
    }

    /****************以下是TractionLevelConfPanel的service********************/

    /**
     * 保存牵引级位数据TrainTractionLevelConf
     * @param trainCategoryId
     */
    public static boolean saveTrainTractionLevelConf(int trainCategoryId, TrainTractionLevelConf bean) {
        String delSQL = "DELETE FROM train_traction_level_conf WHERE train_category_id = " + trainCategoryId;
        sqlHelper.update(delSQL, null);
        String sql = "INSERT INTO train_traction_level_conf VALUES(NULL," + bean.get_1_0() + "," + bean.get_1_1() + "," + bean.get_1_2() + "," + bean.get_1_3()
                     + "," + bean.get_2_0() + "," + bean.get_2_1() + "," + bean.get_2_2() + "," + bean.get_2_3() + "," + bean.get_3_0() + "," + bean.get_3_1()
                     + "," + bean.get_3_2() + "," + bean.get_3_3() + "," + bean.get_4_0() + "," + bean.get_4_1() + "," + bean.get_4_2() + "," + bean.get_4_3()
                     + "," + bean.get_4_4() + "," + bean.get_5_0() + "," + bean.get_5_1() + "," + bean.get_5_2() + "," + bean.get_5_3() + "," + bean.get_5_4()
                     + "," + bean.get_6_0() + "," + bean.get_6_1() + "," + bean.get_6_2() + "," + bean.get_6_3() + "," + bean.get_6_4() + "," + bean.get_7_0()
                     + "," + bean.get_7_1() + "," + bean.get_7_2() + "," + bean.get_7_3() + "," + bean.get_7_4() + "," + bean.get_8_0() + "," + bean.get_8_1()
                     + "," + bean.get_8_2() + "," + bean.get_8_3() + "," + bean.get_8_4() + "," + bean.get_9_0() + "," + bean.get_9_1() + "," + bean.get_9_2()
                     + "," + bean.get_9_3() + "," + bean.get_9_4() + "," + bean.get_10_0() + "," + bean.get_10_1() + "," + bean.get_10_2() + ","
                     + bean.get_10_3() + "," + bean.get_10_4() + "," + bean.get_10_5() + "," + bean.get_10_6() + "," + bean.get_11_0() + "," + bean.get_11_1()
                     + "," + bean.get_11_2() + "," + bean.get_11_3() + "," + bean.get_11_4() + "," + bean.get_11_5() + "," + bean.get_11_6() + ","
                     + bean.get_12_0() + "," + bean.get_12_1() + "," + bean.get_12_2() + "," + bean.get_12_3() + "," + bean.get_12_4() + "," + bean.get_12_5()
                     + "," + bean.get_12_6() + "," + bean.get_13_0() + "," + bean.get_13_1() + "," + bean.get_13_2() + "," + bean.get_13_3() + ","
                     + bean.get_13_4() + "," + bean.get_13_5() + "," + bean.get_13_6() + "," + bean.get_14_0() + "," + bean.get_14_1() + "," + bean.get_14_2()
                     + "," + bean.get_14_3() + "," + bean.get_14_4() + "," + bean.get_14_5() + "," + bean.get_14_6() + "," + bean.get_15_0() + ","
                     + bean.get_15_1() + "," + bean.get_15_2() + "," + bean.get_15_3() + "," + bean.get_15_4() + "," + trainCategoryId + ")";
        sqlHelper.update(sql, null);
        return true;
    }

    /**
     * 根据列车种类trainCategoryId获取该车牵引级位数据TrainTractionLevelConf
     * @param trainCategoryId
     * @return
     */
    public static TrainTractionLevelConf getTrainTractionLevelConf(int trainCategoryId) {
        TrainTractionLevelConf bean = new TrainTractionLevelConf();
        String sql = "SELECT * FROM train_traction_level_conf WHERE train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            Object[] obj = (Object[]) list.get(0);
            bean.set_1_0(Double.parseDouble(obj[1].toString()));
            bean.set_1_1(Double.parseDouble(obj[2].toString()));
            bean.set_1_2(Double.parseDouble(obj[3].toString()));
            bean.set_1_3(Double.parseDouble(obj[4].toString()));

            bean.set_2_0(Double.parseDouble(obj[5].toString()));
            bean.set_2_1(Double.parseDouble(obj[6].toString()));
            bean.set_2_2(Double.parseDouble(obj[7].toString()));
            bean.set_2_3(Double.parseDouble(obj[8].toString()));

            bean.set_3_0(Double.parseDouble(obj[9].toString()));
            bean.set_3_1(Double.parseDouble(obj[10].toString()));
            bean.set_3_2(Double.parseDouble(obj[11].toString()));
            bean.set_3_3(Double.parseDouble(obj[12].toString()));

            bean.set_4_0(Double.parseDouble(obj[13].toString()));
            bean.set_4_1(Double.parseDouble(obj[14].toString()));
            bean.set_4_2(Double.parseDouble(obj[15].toString()));
            bean.set_4_3(Double.parseDouble(obj[16].toString()));
            bean.set_4_4(Double.parseDouble(obj[17].toString()));

            bean.set_5_0(Double.parseDouble(obj[18].toString()));
            bean.set_5_1(Double.parseDouble(obj[19].toString()));
            bean.set_5_2(Double.parseDouble(obj[20].toString()));
            bean.set_5_3(Double.parseDouble(obj[21].toString()));
            bean.set_5_4(Double.parseDouble(obj[22].toString()));

            bean.set_6_0(Double.parseDouble(obj[23].toString()));
            bean.set_6_1(Double.parseDouble(obj[24].toString()));
            bean.set_6_2(Double.parseDouble(obj[25].toString()));
            bean.set_6_3(Double.parseDouble(obj[26].toString()));
            bean.set_6_4(Double.parseDouble(obj[27].toString()));

            bean.set_7_0(Double.parseDouble(obj[28].toString()));
            bean.set_7_1(Double.parseDouble(obj[29].toString()));
            bean.set_7_2(Double.parseDouble(obj[30].toString()));
            bean.set_7_3(Double.parseDouble(obj[31].toString()));
            bean.set_7_4(Double.parseDouble(obj[32].toString()));

            bean.set_8_0(Double.parseDouble(obj[33].toString()));
            bean.set_8_1(Double.parseDouble(obj[34].toString()));
            bean.set_8_2(Double.parseDouble(obj[35].toString()));
            bean.set_8_3(Double.parseDouble(obj[36].toString()));
            bean.set_8_4(Double.parseDouble(obj[37].toString()));

            bean.set_9_0(Double.parseDouble(obj[38].toString()));
            bean.set_9_1(Double.parseDouble(obj[39].toString()));
            bean.set_9_2(Double.parseDouble(obj[40].toString()));
            bean.set_9_3(Double.parseDouble(obj[41].toString()));
            bean.set_9_4(Double.parseDouble(obj[42].toString()));

            bean.set_10_0(Double.parseDouble(obj[43].toString()));
            bean.set_10_1(Double.parseDouble(obj[44].toString()));
            bean.set_10_2(Double.parseDouble(obj[45].toString()));
            bean.set_10_3(Double.parseDouble(obj[46].toString()));
            bean.set_10_4(Double.parseDouble(obj[47].toString()));
            bean.set_10_5(Double.parseDouble(obj[48].toString()));
            bean.set_10_6(Double.parseDouble(obj[49].toString()));

            bean.set_11_0(Double.parseDouble(obj[50].toString()));
            bean.set_11_1(Double.parseDouble(obj[51].toString()));
            bean.set_11_2(Double.parseDouble(obj[52].toString()));
            bean.set_11_3(Double.parseDouble(obj[53].toString()));
            bean.set_11_4(Double.parseDouble(obj[54].toString()));
            bean.set_11_5(Double.parseDouble(obj[55].toString()));
            bean.set_11_6(Double.parseDouble(obj[56].toString()));

            bean.set_12_0(Double.parseDouble(obj[57].toString()));
            bean.set_12_1(Double.parseDouble(obj[58].toString()));
            bean.set_12_2(Double.parseDouble(obj[59].toString()));
            bean.set_12_3(Double.parseDouble(obj[60].toString()));
            bean.set_12_4(Double.parseDouble(obj[61].toString()));
            bean.set_12_5(Double.parseDouble(obj[62].toString()));
            bean.set_12_6(Double.parseDouble(obj[63].toString()));

            bean.set_13_0(Double.parseDouble(obj[64].toString()));
            bean.set_13_1(Double.parseDouble(obj[65].toString()));
            bean.set_13_2(Double.parseDouble(obj[66].toString()));
            bean.set_13_3(Double.parseDouble(obj[67].toString()));
            bean.set_13_4(Double.parseDouble(obj[68].toString()));
            bean.set_13_5(Double.parseDouble(obj[69].toString()));
            bean.set_13_6(Double.parseDouble(obj[70].toString()));

            bean.set_14_0(Double.parseDouble(obj[71].toString()));
            bean.set_14_1(Double.parseDouble(obj[72].toString()));
            bean.set_14_2(Double.parseDouble(obj[73].toString()));
            bean.set_14_3(Double.parseDouble(obj[74].toString()));
            bean.set_14_4(Double.parseDouble(obj[75].toString()));
            bean.set_14_5(Double.parseDouble(obj[76].toString()));
            bean.set_14_6(Double.parseDouble(obj[77].toString()));

            bean.set_15_0(Double.parseDouble(obj[78].toString()));
            bean.set_15_1(Double.parseDouble(obj[79].toString()));
            bean.set_15_2(Double.parseDouble(obj[80].toString()));
            bean.set_15_3(Double.parseDouble(obj[81].toString()));
            bean.set_15_4(Double.parseDouble(obj[82].toString()));

        }
        return bean;
    }

}
