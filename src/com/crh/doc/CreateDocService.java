package com.crh.doc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Encoder;

import com.crh.calculation.mintime.TrainRunParametersCal;
import com.crh.service.AuxiliaryPowerSupplyPanelService;
import com.crh.service.RouteDataManagementDialogService;
import com.crh.service.TopTargetService;
import com.crh.service.TractionConfPanelService;
import com.crh.service.TrainEditPanelService;
import com.crh2.javabean.DocumentBean;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.StationStoptimePair;
import com.crh2.javabean.TrainInfo;
import com.crh2.javabean.TrainTopTarget;
import com.crh2.javabean.TrainTractionConf;
import com.crh2.util.MyTools;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 2014.11.27 功能：根据doc模板生成分析报告
 * @author huhui
 *
 */
public class CreateDocService {

    private String             routeName;
    private String             trainNum;
    private int                trainCategoryId;
    private String             trainCategory;
    private ArrayList<Rundata> rundataList;
    /**
     * 设置模本
     */
    private Configuration      configuration = null;
    private DocumentBean       docBean       = null;

    /**
     * 输出文档路径及名称
     */
    private String             savePath      = "";

    public static void main(String[] args) {
        CreateDocService createDocService = new CreateDocService("京津线（下行）", "G99", 57, "CRH3", null, "D:/train.doc");
        createDocService.createDoc();
    }

    public CreateDocService(String routeName, String trainNum, int trainCategoryId, String trainCategory, ArrayList<Rundata> rundataList, String savePath) {
        this.routeName = routeName;
        this.trainNum = trainNum;
        this.trainCategoryId = trainCategoryId;
        this.trainCategory = trainCategory;
        this.rundataList = rundataList;
        this.savePath = savePath;
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        docBean = new DocumentBean();
    }

    /**
     * 创建Doc文件
     */
    public void createDoc() {
        // 要填入模本的数据文件
        Map<String, Object> dataMap = new HashMap<String, Object>();
        fillInData(dataMap);
        // 设置模本装置方法和路径
        configuration.setClassForTemplateLoading(this.getClass(), "/template");
        Template t = null;
        try {
            // doc.ftl为要装载的模板
            t = configuration.getTemplate("doc.ftl");
            // 输出文档路径及名称
            File outFile = new File(savePath);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            t.process(dataMap, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 填充数据。注意dataMap里存放的数据Key值要与模板中的参数相对应
     * @param dataMap
     */
    public void fillInData(Map<String, Object> dataMap) {
        getDataFromDatabase();
        //1.内容部分
        dataMap.put("lcbz0", docBean.getLcbz0());
        dataMap.put("lcbz1", docBean.getLcbz1());
        dataMap.put("lcbz2", docBean.getLcbz2());
        dataMap.put("sd0", docBean.getSd0());
        dataMap.put("sd1", docBean.getSd1());
        dataMap.put("sd2", docBean.getSd2());
        dataMap.put("sd3", docBean.getSd3());
        dataMap.put("sd4", docBean.getSd4());
        dataMap.put("ssd0", docBean.getSsd0());
        dataMap.put("ssd1", docBean.getSsd1());
        dataMap.put("ssd2", docBean.getSsd2());
        dataMap.put("ssd3", docBean.getSsd3());
        dataMap.put("ssd4", docBean.getSsd4());
        dataMap.put("ssd5", docBean.getSsd5());
        dataMap.put("aq0", docBean.getAq0());
        dataMap.put("aq1", docBean.getAq1());
        dataMap.put("aq2", docBean.getAq2());
        dataMap.put("aq3", docBean.getAq3());
        dataMap.put("aq4", docBean.getAq4());
        dataMap.put("zh0", docBean.getZh0());
        dataMap.put("zh1", docBean.getZh1());
        dataMap.put("zh2", docBean.getZh2());
        dataMap.put("zh3", docBean.getZh3());
        dataMap.put("lj0", docBean.getLj0());
        dataMap.put("lj1", docBean.getLj1());
        dataMap.put("lj2", docBean.getLj2());
        dataMap.put("clx0", docBean.getClx0());
        dataMap.put("clx1", docBean.getClx1());
        dataMap.put("zl0", docBean.getZl0());
        dataMap.put("zl1", docBean.getZl1());
        dataMap.put("nztx0", docBean.getNztx0());
        dataMap.put("nztx1", docBean.getNztx1());
        dataMap.put("nztx2", docBean.getNztx2());
        dataMap.put("nztx3", docBean.getNztx3());
        dataMap.put("djcs0", docBean.getDjcs0());
        dataMap.put("djcs1", docBean.getDjcs1());
        dataMap.put("djcs2", docBean.getDjcs2());
        dataMap.put("djcs3", docBean.getDjcs3());
        dataMap.put("djcs4", docBean.getDjcs4());
        dataMap.put("jlxj0", docBean.getJlxj0());
        dataMap.put("jlxj1", docBean.getJlxj1());
        dataMap.put("jlxj2", docBean.getJlxj2());
        dataMap.put("jldj0", docBean.getJldj0());
        dataMap.put("jldj1", docBean.getJldj1());
        dataMap.put("jldj2", docBean.getJldj2());
        dataMap.put("jldj3", docBean.getJldj3());
        dataMap.put("jldj4", docBean.getJldj4());
        dataMap.put("jldj5", docBean.getJldj5());
        dataMap.put("zlxj0", docBean.getZlxj0());
        dataMap.put("zlxj1", docBean.getZlxj1());
        dataMap.put("zlxj2", docBean.getZlxj2());
        dataMap.put("zldj0", docBean.getZldj0());
        dataMap.put("zldj1", docBean.getZldj1());
        dataMap.put("zldj2", docBean.getZldj2());
        dataMap.put("zlqt0", docBean.getZlqt0());
        dataMap.put("zlqt1", docBean.getZlqt1());
        dataMap.put("zlqt2", docBean.getZlqt2());
        dataMap.put("zlqt3", docBean.getZlqt3());
        dataMap.put("zlqt4", docBean.getZlqt4());
        dataMap.put("zlqt5", docBean.getZlqt5());
        dataMap.put("zlqt6", docBean.getZlqt6());
        dataMap.put("xltx0", docBean.getXltx0());
        dataMap.put("xltx1", docBean.getXltx1());
        dataMap.put("xltx2", docBean.getXltx2());
        dataMap.put("xltx3", docBean.getXltx3());
        dataMap.put("xltx4", docBean.getXltx4());
        dataMap.put("xltx5", docBean.getXltx5());
        dataMap.put("xltx6", docBean.getXltx6());
        dataMap.put("xltx7", docBean.getXltx7());
        dataMap.put("xltx8", docBean.getXltx8());
        dataMap.put("xltx9", docBean.getXltx9());
        dataMap.put("xltx10", docBean.getXltx10());
        dataMap.put("xltx11", docBean.getXltx11());
        dataMap.put("xltx12", docBean.getXltx12());
        dataMap.put("xltx13", docBean.getXltx13());
        dataMap.put("xltx14", docBean.getXltx14());
        dataMap.put("xltx15", docBean.getXltx15());
        dataMap.put("xltx16", docBean.getXltx16());
        dataMap.put("xltx17", docBean.getXltx17());
        dataMap.put("xltx18", docBean.getXltx18());
        dataMap.put("xltx19", docBean.getXltx19());
        dataMap.put("xltx20", docBean.getXltx20());
        dataMap.put("xltx21", docBean.getXltx21());
        dataMap.put("xltx22", docBean.getXltx22());
        dataMap.put("xltx23", docBean.getXltx23());
        dataMap.put("xltx24", docBean.getXltx24());

        dataMap.put("qytx0", docBean.getQytx0());
        dataMap.put("qytx1", docBean.getQytx1());
        dataMap.put("qytx2", docBean.getQytx2());
        dataMap.put("qytx3", docBean.getQytx3());
        dataMap.put("qytx4", docBean.getQytx4());
        dataMap.put("qytx5", docBean.getQytx5());
        dataMap.put("qytx6", docBean.getQytx6());
        dataMap.put("qytx7", docBean.getQytx7());
        dataMap.put("qytx8", docBean.getQytx8());
        dataMap.put("qytx9", docBean.getQytx9());
        dataMap.put("qytx10", docBean.getQytx10());
        dataMap.put("qytx11", docBean.getQytx11());
        dataMap.put("zdtx0", docBean.getZdtx0());
        dataMap.put("zdtx1", docBean.getZdtx1());
        dataMap.put("zdtx2", docBean.getZdtx2());
        dataMap.put("zdtx3", docBean.getZdtx3());
        dataMap.put("zdtx4", docBean.getZdtx4());
        dataMap.put("zdtx5", docBean.getZdtx5());
        dataMap.put("zdtx6", docBean.getZdtx6());
        dataMap.put("zdtx7", docBean.getZdtx7());
        dataMap.put("zdtx8", docBean.getZdtx8());
        dataMap.put("zdtx9", docBean.getZdtx9());

        //2.图片部分
        CreateChartService createChartService = new CreateChartService(routeName, trainNum, trainCategoryId, trainCategory, rundataList);
        createChartService.createCharts();
        String path = CreateChartService.PATH + "//";
        dataMap.put(CreateChartService.chart_1_10, getImageStr(path + CreateChartService.chart_1_10 + ".jpg"));
        dataMap.put(CreateChartService.chart_1_11_1, getImageStr(path + CreateChartService.chart_1_11_1 + ".jpg"));
        dataMap.put(CreateChartService.chart_1_11_2, getImageStr(path + CreateChartService.chart_1_11_2 + ".jpg"));
        dataMap.put(CreateChartService.chart_2_1_2, getImageStr(path + CreateChartService.chart_2_1_2 + ".jpg"));
        dataMap.put(CreateChartService.chart_2_2_2, getImageStr(path + CreateChartService.chart_2_2_2 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_1, getImageStr(path + CreateChartService.chart_3_1 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_2, getImageStr(path + CreateChartService.chart_3_2 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_3, getImageStr(path + CreateChartService.chart_3_3 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_4, getImageStr(path + CreateChartService.chart_3_4 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_5, getImageStr(path + CreateChartService.chart_3_5 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_6, getImageStr(path + CreateChartService.chart_3_6 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_7, getImageStr(path + CreateChartService.chart_3_7 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_8, getImageStr(path + CreateChartService.chart_3_8 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_9, getImageStr(path + CreateChartService.chart_3_9 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_10, getImageStr(path + CreateChartService.chart_3_10 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_11, getImageStr(path + CreateChartService.chart_3_11 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_12, getImageStr(path + CreateChartService.chart_3_12 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_13, getImageStr(path + CreateChartService.chart_3_13 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_14_1, getImageStr(path + CreateChartService.chart_3_14_1 + ".jpg"));
        //		dataMap.put(CreateChartService.chart_3_14_2, getImageStr(path + CreateChartService.chart_3_14_2 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_15_1, getImageStr(path + CreateChartService.chart_3_15_1 + ".jpg"));
        //		dataMap.put(CreateChartService.chart_3_15_2, getImageStr(path + CreateChartService.chart_3_15_2 + ".jpg"));
        dataMap.put(CreateChartService.chart_3_16, getImageStr(path + CreateChartService.chart_3_16 + ".jpg"));

    }

    /**
     * 从数据库中获取数据填充DocumentBean
     */
    public void getDataFromDatabase() {
        TrainInfo trainInfoBean = TrainEditPanelService.getTrainInfoByCategoryId(trainCategoryId);//列车编辑
        double[] trainInfoArray = TrainEditPanelService.getWeightInfo(trainCategoryId);//列车质量等
        TrainTopTarget trainTopTargetBean = TopTargetService.getTrainTopTarget(trainCategoryId);//顶层目标
        TrainTractionConf trainTractionConfBean = TractionConfPanelService.getTrainTractionConf(trainCategoryId);//牵引配置

        //1.1	列车编组
        docBean.setLcbz0(trainCategory);
        docBean.setLcbz1(trainInfoBean.getPowerConf());
        docBean.setLcbz2(trainInfoArray[0]);

        //1.2	顶层指标
        docBean.setSd0(trainTopTargetBean.getSpeed0());
        docBean.setSd1(trainTopTargetBean.getSpeed1());
        docBean.setSd2(trainTopTargetBean.getSpeed2());
        docBean.setSd3(trainTopTargetBean.getSpeed3());
        docBean.setSd4(trainTopTargetBean.getSpeed4());

        docBean.setSsd0(trainTopTargetBean.getComfort0());
        docBean.setSsd1(trainTopTargetBean.getComfort1());
        docBean.setSsd2(trainTopTargetBean.getComfort2());
        docBean.setSsd3(trainTopTargetBean.getComfort3());
        docBean.setSsd4(trainTopTargetBean.getComfort4());
        docBean.setSsd5(trainTopTargetBean.getComfort5());

        docBean.setAq0(trainTopTargetBean.getSafty0());
        docBean.setAq1(trainTopTargetBean.getSafty1());
        docBean.setAq2(trainTopTargetBean.getSafty2());
        docBean.setAq3(trainTopTargetBean.getSafty3());
        docBean.setAq4(trainTopTargetBean.getSafty4());

        //1.3	载荷与定员
        docBean.setZh0(trainInfoArray[1]);
        docBean.setZh1(trainInfoArray[2]);
        docBean.setZh2(trainInfoArray[3]);
        docBean.setZh3(trainInfoBean.getJ());

        //1.4	轮径
        docBean.setLj0(trainTractionConfBean.getD0());
        docBean.setLj1(trainTractionConfBean.getD());
        docBean.setLj2(trainTractionConfBean.getD1());

        //1.5	齿轮箱
        docBean.setClx0(trainTractionConfBean.getK2());
        docBean.setClx1(trainTractionConfBean.getK1());

        //1.6	阻力
        docBean.setZl0(trainInfoBean.getLaunchf());
        docBean.setZl1("W0 = " + trainInfoBean.getA() + " + " + trainInfoBean.getB() + "*V + " + trainInfoBean.getC() + "*V^2");

        //1.7	黏着特性
        docBean.setNztx0(trainInfoBean.getTu1().replaceAll(">", " gt ").replaceAll("<", " lt "));
        docBean.setNztx1(trainInfoBean.getTu2().replaceAll(">", " gt ").replaceAll("<", " lt "));
        docBean.setNztx2(trainInfoBean.getBu1().replaceAll(">", " gt ").replaceAll("<", " lt "));
        docBean.setNztx3(trainInfoBean.getBu2().replaceAll(">", " gt ").replaceAll("<", " lt "));

        //1.8	电机参数
        docBean.setDjcs0(trainTractionConfBean.getN());
        docBean.setDjcs1(trainTractionConfBean.getT());
        docBean.setDjcs2(trainTractionConfBean.getP0());
        docBean.setDjcs3(trainTractionConfBean.getV1());
        docBean.setDjcs4(trainTractionConfBean.getV2());

        //1.9	辅助功率
        double[] paras = AuxiliaryPowerSupplyPanelService.getDocPara(trainCategoryId);//交流
        docBean.setJlxj0(paras[0]);
        docBean.setJlxj1(paras[1]);
        docBean.setJlxj2(paras[2]);

        docBean.setJldj0(paras[3]);
        docBean.setJldj1(paras[4]);
        docBean.setJldj2(paras[5]);
        docBean.setJldj3("优先推荐安装在拖车上");
        docBean.setJldj4(paras[6]);
        docBean.setJldj5(paras[7]);

        double[] parasDc = AuxiliaryPowerSupplyPanelService.getDocParaDC(trainCategoryId);//直流
        docBean.setZlxj0(parasDc[0]);
        docBean.setZlxj1(parasDc[1]);
        docBean.setZlxj2(parasDc[2]);

        docBean.setZldj0(parasDc[3]);
        docBean.setZldj1(parasDc[4]);
        docBean.setZldj2(parasDc[5]);

        docBean.setZlqt0("优先推荐安装在没有变压器的拖车上");
        docBean.setZlqt1(parasDc[6]);
        docBean.setZlqt2(parasDc[7]);
        docBean.setZlqt3(parasDc[8]);
        docBean.setZlqt4("优先推荐安装在没有变压器的拖车上");
        docBean.setZlqt5(parasDc[9]);
        docBean.setZlqt6(parasDc[10]);

        //1.12	线路特性（车次设置中的相关信息）
        double[] parasRoute = RouteDataManagementDialogService.getRuntime(routeName, trainNum);
        docBean.setXltx0(routeName);
        docBean.setXltx1(trainNum);
        docBean.setXltx2(parasRoute[0]);

        StationStoptimePair[] ssPairArray = RouteDataManagementDialogService.getStationStoptimePairArray(routeName, trainNum);
        docBean.setXltx3(ssPairArray[0].getStationName());
        docBean.setXltx4(ssPairArray[0].getStoptime());
        docBean.setXltx5(ssPairArray[1].getStationName());
        docBean.setXltx6(ssPairArray[1].getStoptime());
        docBean.setXltx7(ssPairArray[2].getStationName());
        docBean.setXltx8(ssPairArray[2].getStoptime());
        docBean.setXltx9(ssPairArray[3].getStationName());
        docBean.setXltx10(ssPairArray[3].getStoptime());
        docBean.setXltx11(ssPairArray[4].getStationName());
        docBean.setXltx12(ssPairArray[4].getStoptime());
        docBean.setXltx13(ssPairArray[5].getStationName());
        docBean.setXltx14(ssPairArray[5].getStoptime());
        docBean.setXltx15(ssPairArray[6].getStationName());
        docBean.setXltx16(ssPairArray[6].getStoptime());
        docBean.setXltx17(ssPairArray[7].getStationName());
        docBean.setXltx18(ssPairArray[7].getStoptime());
        docBean.setXltx19(ssPairArray[8].getStationName());
        docBean.setXltx20(ssPairArray[8].getStoptime());
        docBean.setXltx21(ssPairArray[9].getStationName());
        docBean.setXltx22(ssPairArray[9].getStoptime());
        docBean.setXltx23(ssPairArray[10].getStationName());
        docBean.setXltx24(ssPairArray[10].getStoptime());

        double maxV = trainInfoBean.getMaxV();
        //2.1	牵引特性计算结果
        TrainRunParametersCal trainParametersCal = new TrainRunParametersCal(trainCategoryId);
        double fwSpeed200 = TractionConfPanelService.getSpeed200(1, trainCategoryId);
        double[] fwPara200 = trainParametersCal.forwardParameters(fwSpeed200);
        docBean.setQytx0(MyTools.numFormat2(fwPara200[0]));
        docBean.setQytx1(MyTools.numFormat2(fwPara200[1]));
        docBean.setQytx2(MyTools.numFormat2(fwPara200[2]));
        docBean.setQytx3(MyTools.numFormat2(fwPara200[3]));
        docBean.setQytx4(fwSpeed200);
        docBean.setQytx5(MyTools.numFormat2(fwPara200[4]));

        double[] fwPara300 = trainParametersCal.forwardParameters(maxV);
        docBean.setQytx6(maxV);
        docBean.setQytx7(MyTools.numFormat2(fwPara300[0]));
        docBean.setQytx8(MyTools.numFormat2(fwPara300[1]));
        docBean.setQytx9(MyTools.numFormat2(fwPara300[2]));
        docBean.setQytx10(MyTools.numFormat2(fwPara300[3]));
        docBean.setQytx11(MyTools.numFormat2(fwPara300[4]));

        //2.2	制动特性计算结果
        double reSpeed200 = TractionConfPanelService.getSpeed200(0, trainCategoryId);
        double[] rePara200 = trainParametersCal.reverseParameters(reSpeed200);
        docBean.setZdtx0(MyTools.numFormat2(rePara200[0]));
        docBean.setZdtx1(MyTools.numFormat2(rePara200[1]));
        docBean.setZdtx2(MyTools.numFormat2(rePara200[2]));
        docBean.setZdtx3(MyTools.numFormat2(rePara200[3]));
        docBean.setZdtx4(reSpeed200);
        double[] rePara300 = trainParametersCal.reverseParameters(maxV);
        docBean.setZdtx5(maxV);
        docBean.setZdtx6(MyTools.numFormat2(rePara300[0]));
        docBean.setZdtx7(MyTools.numFormat2(rePara300[1]));
        docBean.setZdtx8(MyTools.numFormat2(rePara300[2]));
        docBean.setZdtx9(MyTools.numFormat2(rePara300[3]));

    }

    /**
     * 根据图片路径获取图片
     * @param imagePath
     * @return
     */
    public String getImageStr(String imagePath) {
        String imgFile = imagePath;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

}
