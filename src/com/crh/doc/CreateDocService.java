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

        dataMap.put("tz0", docBean.getTz0());
        dataMap.put("tz1", docBean.getTz1());
        dataMap.put("tz2", docBean.getTz2());
        dataMap.put("tz3", docBean.getTz3());
        dataMap.put("tz4", docBean.getTz4());
        dataMap.put("tz5", docBean.getTz5());
        dataMap.put("tz6", docBean.getTz6());
        dataMap.put("tz7", docBean.getTz7());
        dataMap.put("tz8", docBean.getTz8());
        dataMap.put("tz9", docBean.getTz9());

        dataMap.put("tz10", docBean.getTz10());
        dataMap.put("tz11", docBean.getTz11());
        dataMap.put("tz12", docBean.getTz12());
        dataMap.put("tz13", docBean.getTz13());
        dataMap.put("tz14", docBean.getTz14());
        dataMap.put("tz15", docBean.getTz15());
        dataMap.put("tz16", docBean.getTz16());
        dataMap.put("tz17", docBean.getTz17());
        dataMap.put("tz18", docBean.getTz18());
        dataMap.put("tz19", docBean.getTz19());

        dataMap.put("tz20", docBean.getTz20());
        dataMap.put("tz21", docBean.getTz21());
        dataMap.put("tz22", docBean.getTz22());
        dataMap.put("tz23", docBean.getTz23());
        dataMap.put("tz24", docBean.getTz24());
        dataMap.put("tz25", docBean.getTz25());
        dataMap.put("tz26", docBean.getTz26());
        dataMap.put("tz27", docBean.getTz27());
        dataMap.put("tz28", docBean.getTz28());
        dataMap.put("tz29", docBean.getTz29());

        dataMap.put("tz30", docBean.getTz30());
        dataMap.put("tz31", docBean.getTz31());
        dataMap.put("tz32", docBean.getTz32());
        dataMap.put("tz33", docBean.getTz33());
        dataMap.put("tz34", docBean.getTz34());
        dataMap.put("tz35", docBean.getTz35());
        dataMap.put("tz36", docBean.getTz36());
        dataMap.put("tz37", docBean.getTz37());
        dataMap.put("tz38", docBean.getTz38());
        dataMap.put("tz39", docBean.getTz39());

        dataMap.put("tz40", docBean.getTz40());
        dataMap.put("tz41", docBean.getTz41());
        dataMap.put("tz42", docBean.getTz42());
        dataMap.put("tz43", docBean.getTz43());
        dataMap.put("tz44", docBean.getTz44());
        dataMap.put("tz45", docBean.getTz45());
        dataMap.put("tz46", docBean.getTz46());
        dataMap.put("tz47", docBean.getTz47());
        dataMap.put("tz48", docBean.getTz48());
        dataMap.put("tz49", docBean.getTz49());

        dataMap.put("tz50", docBean.getTz50());
        dataMap.put("tz51", docBean.getTz51());
        dataMap.put("tz52", docBean.getTz52());
        dataMap.put("tz53", docBean.getTz53());
        dataMap.put("tz54", docBean.getTz54());
        dataMap.put("tz55", docBean.getTz55());
        dataMap.put("tz56", docBean.getTz56());
        dataMap.put("tz57", docBean.getTz57());
        dataMap.put("tz58", docBean.getTz58());
        dataMap.put("tz59", docBean.getTz59());

        dataMap.put("tz60", docBean.getTz60());
        dataMap.put("tz61", docBean.getTz61());
        dataMap.put("tz62", docBean.getTz62());
        dataMap.put("tz63", docBean.getTz63());
        dataMap.put("tz64", docBean.getTz64());
        dataMap.put("tz65", docBean.getTz65());
        dataMap.put("tz66", docBean.getTz66());
        dataMap.put("tz67", docBean.getTz67());
        dataMap.put("tz68", docBean.getTz68());
        dataMap.put("tz69", docBean.getTz69());

        dataMap.put("tz70", docBean.getTz70());
        dataMap.put("tz71", docBean.getTz71());
        dataMap.put("tz72", docBean.getTz72());
        dataMap.put("tz73", docBean.getTz73());
        dataMap.put("tz74", docBean.getTz74());
        dataMap.put("tz75", docBean.getTz75());
        dataMap.put("tz76", docBean.getTz76());
        dataMap.put("tz77", docBean.getTz77());
        dataMap.put("tz78", docBean.getTz78());
        dataMap.put("tz79", docBean.getTz79());

        dataMap.put("tz80", docBean.getTz80());
        dataMap.put("tz81", docBean.getTz81());
        dataMap.put("tz82", docBean.getTz82());
        dataMap.put("tz83", docBean.getTz83());
        dataMap.put("tz84", docBean.getTz84());
        dataMap.put("tz85", docBean.getTz85());
        dataMap.put("tz86", docBean.getTz86());
        dataMap.put("tz87", docBean.getTz87());
        dataMap.put("tz88", docBean.getTz88());
        dataMap.put("tz89", docBean.getTz89());

        dataMap.put("tz90", docBean.getTz90());
        dataMap.put("tz91", docBean.getTz91());
        dataMap.put("tz92", docBean.getTz92());
        dataMap.put("tz93", docBean.getTz93());
        dataMap.put("tz94", docBean.getTz94());
        dataMap.put("tz95", docBean.getTz95());
        dataMap.put("tz96", docBean.getTz96());
        dataMap.put("tz97", docBean.getTz97());
        dataMap.put("tz98", docBean.getTz98());
        dataMap.put("tz99", docBean.getTz99());

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
        docBean.setTz0(ssPairArray[0].getStationName());
        docBean.setTz1(ssPairArray[0].getStoptime());
        docBean.setTz2(ssPairArray[1].getStationName());
        docBean.setTz3(ssPairArray[1].getStoptime());
        docBean.setTz4(ssPairArray[2].getStationName());
        docBean.setTz5(ssPairArray[2].getStoptime());
        docBean.setTz6(ssPairArray[3].getStationName());
        docBean.setTz7(ssPairArray[3].getStoptime());
        docBean.setTz8(ssPairArray[4].getStationName());
        docBean.setTz9(ssPairArray[4].getStoptime());

        docBean.setTz10(ssPairArray[5].getStationName());
        docBean.setTz11(ssPairArray[5].getStoptime());
        docBean.setTz12(ssPairArray[6].getStationName());
        docBean.setTz13(ssPairArray[6].getStoptime());
        docBean.setTz14(ssPairArray[7].getStationName());
        docBean.setTz15(ssPairArray[7].getStoptime());
        docBean.setTz16(ssPairArray[8].getStationName());
        docBean.setTz17(ssPairArray[8].getStoptime());
        docBean.setTz18(ssPairArray[9].getStationName());
        docBean.setTz19(ssPairArray[9].getStoptime());

        docBean.setTz20(ssPairArray[10].getStationName());
        docBean.setTz21(ssPairArray[10].getStoptime());
        docBean.setTz22(ssPairArray[11].getStationName());
        docBean.setTz23(ssPairArray[11].getStoptime());
        docBean.setTz24(ssPairArray[12].getStationName());
        docBean.setTz25(ssPairArray[12].getStoptime());
        docBean.setTz26(ssPairArray[13].getStationName());
        docBean.setTz27(ssPairArray[13].getStoptime());
        docBean.setTz28(ssPairArray[14].getStationName());
        docBean.setTz29(ssPairArray[14].getStoptime());

        docBean.setTz30(ssPairArray[15].getStationName());
        docBean.setTz31(ssPairArray[15].getStoptime());
        docBean.setTz32(ssPairArray[16].getStationName());
        docBean.setTz33(ssPairArray[16].getStoptime());
        docBean.setTz34(ssPairArray[17].getStationName());
        docBean.setTz35(ssPairArray[17].getStoptime());
        docBean.setTz36(ssPairArray[18].getStationName());
        docBean.setTz37(ssPairArray[18].getStoptime());
        docBean.setTz38(ssPairArray[19].getStationName());
        docBean.setTz39(ssPairArray[19].getStoptime());

        docBean.setTz40(ssPairArray[20].getStationName());
        docBean.setTz41(ssPairArray[20].getStoptime());
        docBean.setTz42(ssPairArray[21].getStationName());
        docBean.setTz43(ssPairArray[21].getStoptime());
        docBean.setTz44(ssPairArray[22].getStationName());
        docBean.setTz45(ssPairArray[22].getStoptime());
        docBean.setTz46(ssPairArray[23].getStationName());
        docBean.setTz47(ssPairArray[23].getStoptime());
        docBean.setTz48(ssPairArray[24].getStationName());
        docBean.setTz49(ssPairArray[24].getStoptime());

        docBean.setTz50(ssPairArray[25].getStationName());
        docBean.setTz51(ssPairArray[25].getStoptime());
        docBean.setTz52(ssPairArray[26].getStationName());
        docBean.setTz53(ssPairArray[26].getStoptime());
        docBean.setTz54(ssPairArray[27].getStationName());
        docBean.setTz55(ssPairArray[27].getStoptime());
        docBean.setTz56(ssPairArray[28].getStationName());
        docBean.setTz57(ssPairArray[28].getStoptime());
        docBean.setTz58(ssPairArray[29].getStationName());
        docBean.setTz59(ssPairArray[29].getStoptime());

        docBean.setTz60(ssPairArray[30].getStationName());
        docBean.setTz61(ssPairArray[30].getStoptime());
        docBean.setTz62(ssPairArray[31].getStationName());
        docBean.setTz63(ssPairArray[31].getStoptime());
        docBean.setTz64(ssPairArray[32].getStationName());
        docBean.setTz65(ssPairArray[32].getStoptime());
        docBean.setTz66(ssPairArray[33].getStationName());
        docBean.setTz67(ssPairArray[33].getStoptime());
        docBean.setTz68(ssPairArray[34].getStationName());
        docBean.setTz69(ssPairArray[34].getStoptime());

        docBean.setTz70(ssPairArray[35].getStationName());
        docBean.setTz71(ssPairArray[35].getStoptime());
        docBean.setTz72(ssPairArray[36].getStationName());
        docBean.setTz73(ssPairArray[36].getStoptime());
        docBean.setTz74(ssPairArray[37].getStationName());
        docBean.setTz75(ssPairArray[37].getStoptime());
        docBean.setTz76(ssPairArray[38].getStationName());
        docBean.setTz77(ssPairArray[38].getStoptime());
        docBean.setTz78(ssPairArray[39].getStationName());
        docBean.setTz79(ssPairArray[39].getStoptime());

        docBean.setTz80(ssPairArray[40].getStationName());
        docBean.setTz81(ssPairArray[40].getStoptime());
        docBean.setTz82(ssPairArray[41].getStationName());
        docBean.setTz83(ssPairArray[41].getStoptime());
        docBean.setTz84(ssPairArray[42].getStationName());
        docBean.setTz85(ssPairArray[42].getStoptime());
        docBean.setTz86(ssPairArray[43].getStationName());
        docBean.setTz87(ssPairArray[43].getStoptime());
        docBean.setTz88(ssPairArray[44].getStationName());
        docBean.setTz89(ssPairArray[44].getStoptime());

        docBean.setTz90(ssPairArray[45].getStationName());
        docBean.setTz91(ssPairArray[45].getStoptime());
        docBean.setTz92(ssPairArray[46].getStationName());
        docBean.setTz93(ssPairArray[46].getStoptime());
        docBean.setTz94(ssPairArray[47].getStationName());
        docBean.setTz95(ssPairArray[47].getStoptime());
        docBean.setTz96(ssPairArray[48].getStationName());
        docBean.setTz97(ssPairArray[48].getStoptime());
        docBean.setTz98(ssPairArray[49].getStationName());
        docBean.setTz99(ssPairArray[49].getStoptime());

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
