package com.crh2.calculate.excel;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Section;
import com.crh2.javabean.Slope;
import com.crh2.javabean.SpecialSpeedLimitPoint;
import com.crh2.javabean.StationInfo;

/**
 * 用户读取Excel中的数据
 * 点击新建线路中的“开始计算”按钮调用这个类的方法读取excel表并计算
 * 这个类将数据先保存在list中，并且提供getter方法，将excel值传给其它类，用于计算rundata
 * @author huhui
 *
 */
public class ExcelAction {
	// 每一个list保存一张excel表的数据
	private ArrayList<StationInfo> stationInfoList = new ArrayList<StationInfo>();
	private ArrayList<Section> sectionList = new ArrayList<Section>();
	private ArrayList<Slope> slopeList = new ArrayList<Slope>();
	private ArrayList<Curve> curveList = new ArrayList<Curve>();
	private ArrayList<SpecialSpeedLimitPoint> specialSpeedLimitPointList = new ArrayList<SpecialSpeedLimitPoint>();
	private int routeId;
	private String excelPath;
	private SQLHelper sqlHelper;

	// 构造方法
	public ExcelAction(int routeId, String excelPath) {
		this.routeId = routeId;
		this.excelPath = excelPath;
		sqlHelper = new SQLHelper();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExcelAction excelAction = new ExcelAction(1, "c:\\导入计算数据.xls");
		excelAction.readDataFromExcel();
		System.out.println("运行结束");
	}

	/**
	 *  得到Excel表中的内容
	 * @return
	 */
	public boolean readDataFromExcel() {
		FileInputStream fileInputStream = null;
		Workbook workbook = null;
		try {
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
			// TODO: handle exception
			e.printStackTrace();
			return false;
		} finally {
			try {
				workbook.close();
				fileInputStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
		String[][] sheetData = new String[sheet.getRows() - 2][sheet
				.getColumns()];// 减去两行
		// 从第二行开始读取数据
		for (int i = 2; i < sheet.getRows(); i++) {
			for (int j = 0; j < sheet.getColumns(); j++) {
				Cell cell = sheet.getCell(j, i);
				sheetData[i - 2][j] = cell.getContents();
				// System.out.println("cell.getContents()="+cell.getContents());
			}
		}
		return sheetData;
	}

	/**
	 * 处理specialSpeedLimitPoint
	 * @param excelData
	 * @param routeId
	 */
	public void specialSpeedLimitPointAction(String[][] excelData, int routeId) {
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
			sslp.setStart(Double.parseDouble(array[0]));
			sslp.setEnd(Double.parseDouble(array[1]));
			sslp.setSpeedLimit(Double.parseDouble(array[2]));
			sslp.setRouteId(routeId);
			specialSpeedLimitPointList.add(sslp);
			sql = "insert into specialspeedlimitpoint values(null," + array[0]
					+ "," + array[1] + "," + array[2] + "," + routeId + ")";
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
			sql = "insert into curve values(null," + array[0] + "," + array[1]
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
			sql = "insert into slope values(null," + array[0] + "," + array[1]
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
			sql = "insert into section values(null," + array[0] + ","
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
			sql = "insert into stationinfo values(null," + array[0] + ",'"
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
