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
 * �û���ȡExcel�е�����
 * ����½���·�еġ���ʼ���㡱��ť���������ķ�����ȡexcel������
 * ����ཫ�����ȱ�����list�У������ṩgetter��������excelֵ���������࣬���ڼ���rundata
 * @author huhui
 *
 */
public class ExcelAction {
	// ÿһ��list����һ��excel�������
	private ArrayList<StationInfo> stationInfoList = new ArrayList<StationInfo>();
	private ArrayList<Section> sectionList = new ArrayList<Section>();
	private ArrayList<Slope> slopeList = new ArrayList<Slope>();
	private ArrayList<Curve> curveList = new ArrayList<Curve>();
	private ArrayList<SpecialSpeedLimitPoint> specialSpeedLimitPointList = new ArrayList<SpecialSpeedLimitPoint>();
	private int routeId;
	private String excelPath;
	private SQLHelper sqlHelper;

	// ���췽��
	public ExcelAction(int routeId, String excelPath) {
		this.routeId = routeId;
		this.excelPath = excelPath;
		sqlHelper = new SQLHelper();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExcelAction excelAction = new ExcelAction(1, "c:\\�����������.xls");
		excelAction.readDataFromExcel();
		System.out.println("���н���");
	}

	/**
	 *  �õ�Excel���е�����
	 * @return
	 */
	public boolean readDataFromExcel() {
		FileInputStream fileInputStream = null;
		Workbook workbook = null;
		try {
			fileInputStream = new FileInputStream(excelPath);
			// ����Wookbook��������������
			workbook = Workbook.getWorkbook(fileInputStream);
			// ��ά���飬���ڱ���excel�е�����
			String[][] excelData = null;
			// ����վsheet
			Sheet stationSheet = workbook.getSheet("��վ");
			excelData = this.readSheetData(stationSheet);
			this.stationInfoAction(excelData, routeId);
			// �������sheet
			Sheet sectionSheet = workbook.getSheet("����");
			excelData = this.readSheetData(sectionSheet);
			this.sectionAction(excelData, routeId);
			// �����µ�sheet
			Sheet slopeSheet = workbook.getSheet("�µ�");
			excelData = this.readSheetData(slopeSheet);
			this.slopeAction(excelData, routeId);
			// ��������sheet
			Sheet curveSheet = workbook.getSheet("����");
			excelData = this.readSheetData(curveSheet);
			this.curveAction(excelData, routeId);
			// ������������sheet
			Sheet specialSpeedLimitPointSheet = workbook.getSheet("��������");
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
	 * ��ȡexcel��sheet������
	 * @param sheet
	 * @return
	 */
	public String[][] readSheetData(Sheet sheet) {
		String[][] sheetData = new String[sheet.getRows() - 2][sheet
				.getColumns()];// ��ȥ����
		// �ӵڶ��п�ʼ��ȡ����
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
	 * ����specialSpeedLimitPoint
	 * @param excelData
	 * @param routeId
	 */
	public void specialSpeedLimitPointAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// ������ά����
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
		// ��������
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * ����curve
	 * @param excelData
	 * @param routeId
	 */
	public void curveAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// ������ά����
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
		// ��������
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * ����slope
	 * @param excelData
	 * @param routeId
	 */
	public void slopeAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// ������ά����
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
		// ��������
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * ����Section
	 * @param excelData
	 * @param routeId
	 */
	public void sectionAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// ������ά����
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
		// ��������
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * ����Station������excelData[][]���飬�ȳ�ʼ��list,Ȼ��д�����ݿ�
	 * @param excelData
	 * @param routeId
	 */
	public void stationInfoAction(String[][] excelData, int routeId) {
		ArrayList<String> sqlList = new ArrayList<String>();
		// ������ά����
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
		// ��������
		sqlHelper.batchInsert(sqlList);
	}

	/**
	 * �ṩlist��getter����
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
