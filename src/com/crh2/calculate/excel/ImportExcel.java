package com.crh2.calculate.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Section;
import com.crh2.javabean.Slope;
import com.crh2.javabean.SpecialSpeedLimitPoint;
import com.crh2.javabean.StationInfo;

/**
 * 新版本 2014.6.22
 * 用户读取Excel中的数据
 * @author huhui
 *
 */
public class ImportExcel {

	// 每一个list保存一张excel表的数据
	private ArrayList<StationInfo> stationInfoList = null;
	private ArrayList<Section> sectionList = null;
	private ArrayList<Slope> slopeList = null;
	private ArrayList<Curve> curveList = null;
	private ArrayList<SpecialSpeedLimitPoint> specialSpeedLimitPointList = null;
	private int routeId;
	private int trainCategoryId;
	private String excelPath;
	private SQLHelper sqlHelper = new SQLHelper();
	
	/**
	 *  构造方法 2014.10.18  制动加速度导入
	 * @param excelPath
	 */
	public ImportExcel(String excelPath){
		this.excelPath = excelPath;
	}
	
	/**
	 * 构造方法 2014.10.18  制动加速度导入
	 * @param excelPath
	 * @param trainCategoryId
	 */
	public ImportExcel(String excelPath, int trainCategoryId){
		this.excelPath = excelPath;
		this.trainCategoryId = trainCategoryId;
	}

	/**
	 * 构造方法
	 * @param routeId
	 * @param excelPath
	 */
	public ImportExcel(int routeId, String excelPath) {
		stationInfoList = new ArrayList<StationInfo>();
		sectionList = new ArrayList<Section>();
		slopeList = new ArrayList<Slope>();
		curveList = new ArrayList<Curve>();
		specialSpeedLimitPointList = new ArrayList<SpecialSpeedLimitPoint>();
		this.routeId = routeId;
		this.excelPath = excelPath;
	}

	public static void main(String[] args) {
//		ImportExcel excelAction = new ImportExcel(7, "C:\\Users\\huhui\\Documents\\京津线(下行).xls");
//		excelAction.readDataFromExcel();
//		System.out.println("运行结束");
	}
	
	/**
	 * 读取excel中外部数据
	 */
	public ArrayList<Rundata> readRundata() {
		ArrayList<Rundata> rundataList = new ArrayList<Rundata>();
		InputStream fileInputStream = null;
		Workbook workbook = null;
		try {
			String[][] excelData = null; //存放excel中数据
			fileInputStream = new FileInputStream(excelPath);
			workbook = Workbook.getWorkbook(fileInputStream);
			Sheet factorSheet = workbook.getSheet("运行数据");
			excelData = this.readSheetData(factorSheet);
			for(int i=0;i<excelData.length;i++){
				Rundata bean = new Rundata();
				bean.setRuntime(Double.parseDouble(excelData[i][0]));
				bean.setSpeed(Double.parseDouble(excelData[i][1]));
				bean.setLocation(Double.parseDouble(excelData[i][2]));
				rundataList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				workbook.close();
				fileInputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return rundataList;
	}
	
	/**
	 * 读取制动加速度数据
	 * @return
	 */
	public boolean readBrakeFactor() {
		boolean b = false;
		InputStream fileInputStream = null;
		Workbook workbook = null;
		try {
			String[][] excelData = null; //存放excel中数据
			fileInputStream = new FileInputStream(excelPath);
			workbook = Workbook.getWorkbook(fileInputStream);
			Sheet factorSheet = workbook.getSheet("加速度");
			excelData = this.readSheetData(factorSheet);
			this.brakeFactorAction(excelData, trainCategoryId);
			b = true;
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}finally{
			try {
				workbook.close();
				fileInputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * 得到Excel表中关于线路的内容
	 * @return
	 */
	public boolean readDataFromExcel() {
		InputStream fileInputStream = null;
		Workbook workbook = null;
		try {
//			fileInputStream = this.getClass().getClassLoader().getResourceAsStream(excelPath);
			fileInputStream = new FileInputStream(excelPath);
			// 构建Wookbook（工作簿）对象
			workbook = Workbook.getWorkbook(fileInputStream);
			// 二维数组，用于保存excel中的数据
			String[][] excelData = null;
			// 处理车站sheet
			Sheet stationSheet = workbook.getSheet("车站");
			excelData = this.readSheetData(stationSheet);
			this.stationInfoAction(excelData, routeId);
			// 处理分相sheet
			Sheet sectionSheet = workbook.getSheet("分相");
			excelData = this.readSheetData(sectionSheet);
			this.sectionAction(excelData, routeId);
			// 处理坡道sheet
			Sheet slopeSheet = workbook.getSheet("坡道");
			excelData = this.readSheetData(slopeSheet);
			this.slopeAction(excelData, routeId);
			// 处理曲线sheet
			Sheet curveSheet = workbook.getSheet("曲线");
			excelData = this.readSheetData(curveSheet);
			this.curveAction(excelData, routeId);
			// 处理特殊限速sheet
			Sheet specialSpeedLimitPointSheet = workbook.getSheet("特殊限速");
			excelData = this.readSheetData(specialSpeedLimitPointSheet);
			this.specialSpeedLimitPointAction(excelData, routeId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				workbook.close();
				fileInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 读取excel中sheet的数据
	 * @param sheet
	 * @return
	 */
	public String[][] readSheetData(Sheet sheet) {
		String[][] sheetData = new String[sheet.getRows() - 2][sheet.getColumns()];// 减去两行
		// 从第二行开始读取数据
		for (int i = 2; i < sheet.getRows(); i++) {
			for (int j = 0; j < sheet.getColumns(); j++) {
				Cell cell = sheet.getCell(j, i);
				String cellContent = cell.getContents();
				sheetData[i - 2][j] = (cellContent == null || cellContent.equals("")) ? "0" : cellContent;
			}
		}
		return sheetData;
	}
	
	/**
	 * 读取完Excel中的制动因子数据再保存
	 * @param excelData
	 * @param trainCategoryId
	 */
	public void brakeFactorAction(String [][] excelData, int trainCategoryId){
		// 1.删除旧有数据
		String delSQL = "DELETE FROM train_brake_factor WHERE train_category_id = " + trainCategoryId;
		sqlHelper.update(delSQL, null);
		// 2.插入新数据
		ArrayList<String> sqlList = new ArrayList<String>();
		for(int i = 0; i < excelData.length; i++) {
			String sql = "";
			String array [] = new String [excelData[i].length];
			for(int j = 0; j < excelData[i].length; j++){
				array[j] = excelData[i][j];
			}
			sql = "insert into train_brake_factor values(null," + array[0]
					+ "," + array[1] + "," + array[2] + "," + array[3] + ","
					+ array[4] + "," + array[5] + "," + array[6] + ","
					+ array[7] + "," + array[8] + "," + array[9] + ","
					+ array[10] + "," + array[11] + ","+ array[12] + ","
					+ array[13] + ","+ array[14] + ","+ array[15] + ","+ +trainCategoryId + ")";
			sqlList.add(sql);
		}
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * 处理specialSpeedLimitPoint
	 * @param excelData
	 * @param routeId
	 */
	public void specialSpeedLimitPointAction(String [][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// 遍历二维数组
		for (int i = 0; i < excelData.length; i++) {
			String sql = "";
			String array[] = new String[excelData[i].length];
			for (int j = 0; j < excelData[i].length; j++) {
				array[j] = excelData[i][j];
			}
			SpecialSpeedLimitPoint sslp = new SpecialSpeedLimitPoint();
			sslp.setId(i);
			sslp.setPointId(Integer.parseInt(array[0]));
			sslp.setStart(Double.parseDouble(array[1]));
			sslp.setEnd(Double.parseDouble(array[2]));
			sslp.setSpeedLimit(Double.parseDouble(array[3]));
			sslp.setRouteId(routeId);
			specialSpeedLimitPointList.add(sslp);
			sql = "insert into train_route_speed_limit values(null," + array[0]
					+ "," + array[1] + "," + array[2] + "," + array[3] + "," + routeId + ")";
			sqlList.add(sql);
		}
		// 批量插入
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * 处理curve
	 * @param excelData
	 * @param routeId
	 */
	public void curveAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// 遍历二维数组
		for (int i = 0; i < excelData.length; i++) {
			String sql = "";
			String array[] = new String[excelData[i].length];
			for (int j = 0; j < excelData[i].length; j++) {
				array[j] = excelData[i][j];
			}
			Curve curve = new Curve();
			curve.setId(i);
			curve.setCurveId(Integer.parseInt(array[0]));
			curve.setRadius(Double.parseDouble(array[1]));
			curve.setStart(Double.parseDouble(array[2]));
			curve.setLength(Double.parseDouble(array[3]));
			curve.setSpeedLimit(Double.parseDouble(array[4]));
			curve.setRouteId(routeId);
			curveList.add(curve);
			sql = "insert into train_route_curve values(null," + array[0] + "," + array[1]
					+ "," + array[2] + "," + array[3] + "," + array[4] + ","
					+ routeId + ")";
			sqlList.add(sql);
		}
		// 批量插入
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * 处理slope
	 * @param excelData
	 * @param routeId
	 */
	public void slopeAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// 遍历二维数组
		for (int i = 0; i < excelData.length; i++) {
			String sql = "";
			String array[] = new String[excelData[i].length];
			for (int j = 0; j < excelData[i].length; j++) {
				array[j] = excelData[i][j];
			}
			Slope slope = new Slope();
			slope.setId(i);
			slope.setSlopeId(Integer.parseInt(array[0]));
			slope.setSlope(Double.parseDouble(array[1]));
			slope.setLength(Double.parseDouble(array[2]));
			slope.setEnd(Double.parseDouble(array[3]));
			slope.setHeight(Double.parseDouble(array[4]));
			slope.setRouteId(routeId);
			slopeList.add(slope);
			sql = "insert into train_route_slope values(null," + array[0] + "," + array[1]
					+ "," + array[2] + "," + array[3] + "," +array[4] + "," + routeId + ")";
			sqlList.add(sql);
		}
		// 批量插入
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * 处理Section
	 * @param excelData
	 * @param routeId
	 */
	public void sectionAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// 遍历二维数组
		for (int i = 0; i < excelData.length; i++) {
			String sql = "";
			String array[] = new String[excelData[i].length];
			for (int j = 0; j < excelData[i].length; j++) {
				array[j] = excelData[i][j];
			}
			Section section = new Section();
			section.setId(i);
			section.setSection_id(Integer.parseInt(array[0]));
			section.setStart(Double.parseDouble(array[1]));
			section.setEnd(Double.parseDouble(array[2]));
			section.setElectricity(Integer.parseInt(array[3]));
			section.setRouteId(routeId);
			sectionList.add(section);
			sql = "insert into train_route_section values(null," + array[0] + ","
					+ array[1] + "," + array[2] + "," + array[3] + ","
					+ routeId + ")";
			sqlList.add(sql);
		}
		// 批量插入
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * 处理Station，传入excelData[][]数组，先初始化list,然后写入数据库
	 * @param excelData
	 * @param routeId
	 */
	public void stationInfoAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// 遍历二维数组
		for (int i = 0; i < excelData.length; i++) {
			String sql = "";
			String array[] = new String[excelData[i].length];
			for (int j = 0; j < excelData[i].length; j++) {
				array[j] = excelData[i][j];
			}
			StationInfo si = new StationInfo();
			si.setId(i);
			si.setStationId(Integer.parseInt(array[0]));
			si.setStationName(array[1]);
			si.setLocation(Double.parseDouble(array[2]));
			si.setRouteId(routeId);
			stationInfoList.add(si);
			sql = "insert into train_route_station values(null," + array[0] + ",'"
					+ array[1] + "'," + array[2] + "," + routeId + ")";
			sqlList.add(sql);
		}
		// 批量插入
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * 提供list的getter方法
	 * @return
	 */
	public ArrayList<StationInfo> getStationInfoList() {
		return stationInfoList;
	}

	public ArrayList<Section> getSectionList() {
		return sectionList;
	}

	public ArrayList<Slope> getSlopeList() {
		return slopeList;
	}

	public ArrayList<Curve> getCurveList() {
		return curveList;
	}

	public ArrayList<SpecialSpeedLimitPoint> getSpecialSpeedLimitPointList() {
		return specialSpeedLimitPointList;
	}

}
