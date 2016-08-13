package com.crh.view.dialog;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.crh.service.RouteDataManagementDialogService;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Section;
import com.crh2.javabean.Slope;
import com.crh2.javabean.SpecialSpeedLimitPoint;
import com.crh2.javabean.StationInfo;
import com.crh2.service.DataService;
import com.crh2.util.MyUtillity;

/**
 * 列车线路数据编辑对话框
 * @author huhui
 *
 */
public class RouteDataEditDialog extends JDialog {
	/**
	 * 定义JTabbedPane中的tab页
	 */
	private JTabbedPane tabbedPane;
	/**
	 * tab页的数量
	 */
	private int SIZE = 5;
	private JPanel [] panelTab;
	private JTable [] routeDataTable;
	private JScrollPane [] routeDataTableScrollPane;
	/**
	 * routeDataTable的列名
	 */
	private Vector<Vector<String>> columnNamesVector;
	/**
	 * 获取列车数据
	 */
	private DataService dataService = new DataService();
	
	public RouteDataEditDialog(Dialog owner, String routeName) {
		super(owner, true);
		setBounds(100, 100, 771, 521);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, routeName, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(0, 0, 755, 444);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 24, 735, 410);
		panel.add(tabbedPane);
		
		initColumnName();
		final int routeId = RouteDataManagementDialogService.getRouteIdByName(routeName);
		generatePanelTab(routeId);
		
		JButton btnNewButton = new JButton("添加");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				appendOneLine();
			}
		});
		btnNewButton.setBounds(194, 450, 93, 23);
		getContentPane().add(btnNewButton);
		
		JButton button = new JButton("删除");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeOneLine();
			}
		});
		button.setBounds(339, 450, 93, 23);
		getContentPane().add(button);
		
		JButton button_1 = new JButton("保存");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveRouteData(routeId);
			}
		});
		button_1.setBounds(474, 450, 93, 23);
		getContentPane().add(button_1);
		
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * 生成tab页面
	 * @param routeId
	 */
	public void generatePanelTab(int routeId){
		tabbedPane.removeAll();
		panelTab = new JPanel[SIZE];
		routeDataTable = new JTable[SIZE];
		routeDataTableScrollPane = new JScrollPane[SIZE];
		for(int i=0;i<SIZE;i++){
			panelTab[i] = new JPanel(new GridLayout(1, 1));
			tabbedPane.addTab(getRouteDataTypeByIndex(i), panelTab[i]);
			routeDataTable[i] = createRouteDataTable(i, routeId);
			routeDataTableScrollPane[i] = new JScrollPane(routeDataTable[i]);
			panelTab[i].add(routeDataTableScrollPane[i]);
		}
	}
	
	/**
	 * 保存各个tab页的列车线路数据
	 * @param routeId
	 */
	public void saveRouteData(int routeId){
		ArrayList<String> sqlList = new ArrayList<String>();
		for(int i=0;i<routeDataTable.length;i++){
			JTable table = routeDataTable[i];
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			for(int j=0;j<tableModel.getRowCount();j++){
				Vector line = (Vector) tableModel.getDataVector().elementAt(j);
				int dataId = Integer.parseInt(line.get(0).toString());
				if(i == 0){
					String name = (String)line.get(1);
					if(name == null || name.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"车站名称\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String locationStr = (String)line.get(2);
					if(locationStr == null || locationStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"中心里程\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String sql = "";
					try {
						sql = "insert into train_route_station values(null," + dataId + ",'"
								+ name + "'," + Double.parseDouble(locationStr) + "," + routeId + ")";
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "请输入数值！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					sqlList.add(sql);
				}else if(i == 1){
					String startStr = (String)line.get(1);
					if(startStr == null || startStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"起始里程\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String endStr = (String)line.get(2);
					if(endStr == null || endStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"末端里程\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String electricStr = (String)line.get(3);
					if(electricStr == null || electricStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"是否有电\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String sql="";
					try {
						sql = "insert into train_route_section values(null," + dataId + ","
								+ Double.parseDouble(startStr) + "," + Double.parseDouble(endStr) + "," + Double.parseDouble(electricStr) + ","
								+ routeId + ")";
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "请输入数值！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					sqlList.add(sql);
				}else if(i == 2){
					String curveStr = (String)line.get(1);
					if(curveStr == null || curveStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"坡度\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String lengthStr = (String)line.get(2);
					if(lengthStr == null || lengthStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"坡长\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String endStr = (String)line.get(3);
					if(endStr == null || endStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"末端里程\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String heightStr = (String)line.get(4);
					if(heightStr == null || heightStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"标高\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String sql = "";
					try {
						sql = "insert into train_route_slope values(null," + dataId + "," + Double.parseDouble(curveStr)
								+ "," + Double.parseDouble(lengthStr) + "," + Double.parseDouble(endStr) + "," +Double.parseDouble(heightStr) + "," + routeId + ")";
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "请输入数值！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					sqlList.add(sql);
				}else if(i == 3){
					String radiusStr = (String)line.get(1);
					if(radiusStr == null || radiusStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"半径\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String startStr = (String)line.get(2);
					if(startStr == null || startStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"起始里程\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String lengthStr = (String)line.get(3);
					if(lengthStr == null || lengthStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"长度\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String speedStr = (String)line.get(4);
					if(speedStr == null || speedStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"限速\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String sql = "";
					try {
						sql = "insert into train_route_curve values(null," + dataId + "," + Double.parseDouble(radiusStr)
								+ "," + Double.parseDouble(startStr) + "," + Double.parseDouble(lengthStr) + "," + Double.parseDouble(speedStr) + "," + routeId + ")";
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "请输入数值！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					sqlList.add(sql);
				}else if(i == 4){
					String startStr = (String)line.get(1);
					if(startStr == null || startStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"起始里程\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String endStr = (String)line.get(2);
					if(endStr == null || endStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"末端里程\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String speedStr = (String)line.get(3);
					if(speedStr == null || speedStr.equals("")){
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "\"限速\"不能为空！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					String sql = "";
					try {
						sql = "insert into train_route_speed_limit values(null," + dataId
								+ "," + Double.parseDouble(startStr) + "," + Double.parseDouble(endStr) + "," + Double.parseDouble(speedStr) + "," + routeId + ")";
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(RouteDataEditDialog.this, "请输入数值！");
						tabbedPane.setSelectedIndex(i);
						table.setRowSelectionInterval(j, j);
						return;
					}
					sqlList.add(sql);
				}
			}
		}
		//批量插入数据
		RouteDataManagementDialogService.saveRouteData(routeId, sqlList);
		JOptionPane.showMessageDialog(RouteDataEditDialog.this, "保存成功！");
	}
	
	/**
	 * 初始化各个tab页中列车线路数据
	 * @param index 标识显示的表
	 * @param routeId 线路id
	 * @return
	 */
	public JTable createRouteDataTable(int index, int routeId){
		final DefaultTableModel routeDataTableModel;
		JTable routeDataTable = null;
		//数据List
		List<StationInfo> stationList = null;
		List<Section> sectionList = null;
		List<Slope> slopeList = null;
		List<Curve> curveList = null;
		List<SpecialSpeedLimitPoint> pointList = null;
		if(index == 0){
			stationList = dataService.getStationInfoData(routeId);
			routeDataTableModel = new DefaultTableModel(columnNamesVector.get(index), 0){
				public boolean isCellEditable(int row, int column){
					if(column == 0){
						return false;
					}
					return true;
				}
			};
			routeDataTable = new JTable(routeDataTableModel);
			routeDataTable.putClientProperty("terminateEditOnFocusLost", true);
			for(StationInfo station : stationList){
				Vector<String> line = new Vector<String>();
				line.add(station.getStationId()+"");
				line.add(station.getStationName());
				line.add(station.getLocation()+"");
				routeDataTableModel.addRow(line);
			}
		}else if(index == 1){
			sectionList = dataService.getSectionData(routeId);
			routeDataTableModel = new DefaultTableModel(columnNamesVector.get(index), 0){
				public boolean isCellEditable(int row, int column){
					if(column == 0){
						return false;
					}
					return true;
				}
			};
			routeDataTable = new JTable(routeDataTableModel);
			routeDataTable.putClientProperty("terminateEditOnFocusLost", true);
			for(Section section : sectionList){
				Vector<String> line = new Vector<String>();
				line.add(section.getSection_id()+"");
				line.add(section.getStart()+"");
				line.add(section.getEnd()+"");
				line.add(section.getElectricity()+"");
				routeDataTableModel.addRow(line);
			}
		}else if(index == 2){
			slopeList = dataService.getSlopeData(routeId);
			routeDataTableModel = new DefaultTableModel(columnNamesVector.get(index), 0){
				public boolean isCellEditable(int row, int column){
					if(column == 0){
						return false;
					}
					return true;
				}
			};
			routeDataTable = new JTable(routeDataTableModel);
			routeDataTable.putClientProperty("terminateEditOnFocusLost", true);
			for(Slope slope : slopeList){
				Vector<String> line = new Vector<String>();
				line.add(slope.getSlopeId()+"");
				line.add(slope.getSlope()+"");
				line.add(slope.getLength()+"");
				line.add(slope.getEnd()+"");
				line.add(slope.getHeight()+"");
				routeDataTableModel.addRow(line);
			}
		}else if(index == 3){
			curveList = dataService.getCurveData(routeId);
			routeDataTableModel = new DefaultTableModel(columnNamesVector.get(index), 0){
				public boolean isCellEditable(int row, int column){
					if(column == 0){
						return false;
					}
					return true;
				}
			};
			routeDataTable = new JTable(routeDataTableModel);
			routeDataTable.putClientProperty("terminateEditOnFocusLost", true);
			for(Curve curve : curveList){
				Vector<String> line = new Vector<String>();
				line.add(curve.getCurveId()+"");
				line.add(curve.getRadius()+"");
				line.add(curve.getStart()+"");
				line.add(curve.getLength()+"");
				line.add(curve.getSpeedLimit()+"");
				routeDataTableModel.addRow(line);
			}
		}else if(index == 4){
			pointList = dataService.getSpeedLimitData(routeId);
			routeDataTableModel = new DefaultTableModel(columnNamesVector.get(index), 0){
				public boolean isCellEditable(int row, int column){
					if(column == 0){
						return false;
					}
					return true;
				}
			};
			routeDataTable = new JTable(routeDataTableModel);
			routeDataTable.putClientProperty("terminateEditOnFocusLost", true);
			for(SpecialSpeedLimitPoint point : pointList){
				Vector<String> line = new Vector<String>();
				line.add(point.getPointId()+"");
				line.add(point.getStart()+"");
				line.add(point.getEnd()+"");
				line.add(point.getSpeedLimit()+"");
				routeDataTableModel.addRow(line);
			}
		}
		
		return routeDataTable;
	}
	
	/**
	 * 初始化LoadTable的列名
	 */
	public void initColumnName(){
		columnNamesVector = new Vector<Vector<String>>();
		
		for(int i=0;i<SIZE;i++){
			Vector<String> vector = new Vector<String>();
			if(i == 0){
				//0.车站
				vector.add("序号");
				vector.add("车站名称");
				vector.add("中心里程(km)");
			}else if(i == 1){
				//1.分相
				vector.add("序号");
				vector.add("起始里程(m)");
				vector.add("末端里程(m)");
				vector.add("是否有电");
			}else if(i == 2){
				//2.坡道
				vector.add("序号");
				vector.add("坡度(‰)");
				vector.add("坡长(m)");
				vector.add("末端里程(km)");
				vector.add("标高(m)");
			}else if(i == 3){
				//3.曲线
				vector.add("序号");
				vector.add("半径(m)左拐为负数");
				vector.add("起始里程(km)");
				vector.add("长度(m)");
				vector.add("限速(km/h)");
			}else if(i == 4){
				//4.特殊限速
				vector.add("序号");
				vector.add("起始里程(km)");
				vector.add("末端里程(km)");
				vector.add("限速(km/h)");
			}
			columnNamesVector.add(vector);
		}
	}
	
	/**
	 * 添加一行
	 */
	public void appendOneLine(){
		int tabIndex = tabbedPane.getSelectedIndex();
		if(tabIndex == -1){
			return;
		}
		JTable table = routeDataTable[tabIndex];
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.insertRow(tableModel.getRowCount(), new Vector<String>());
		int nextId = Integer.parseInt((String) tableModel.getValueAt(tableModel.getRowCount()-2, 0)) + 1;
		tableModel.setValueAt(nextId, tableModel.getRowCount()-1, 0);
	}
	
	/**
	 * 删除一行
	 */
	public void removeOneLine(){
		int tabIndex = tabbedPane.getSelectedIndex();
		if(tabIndex == -1){
			return;
		}
		JTable table = routeDataTable[tabIndex];
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		int selectedIndex = table.getSelectedRow();
		if(selectedIndex == -1){
			JOptionPane.showMessageDialog(RouteDataEditDialog.this, "请选择一行再删除！");
			return;
		}
		int option = JOptionPane.showConfirmDialog(RouteDataEditDialog.this, "确认删除？");
		if(option == 0){
			tableModel.removeRow(selectedIndex);
		}
	}
	
	/**
	 *  通过index获取要显示的线路表的名称
	 * @param index
	 * @return
	 */
	public String getRouteDataTypeByIndex(int index){
		String type = "";
		if(index == 0){
			type = "车站";
		}else if(index == 1){
			type = "分相";
		}else if(index == 2){
			type = "坡道";
		}else if(index == 3){
			type = "曲线";
		}else if(index == 4){
			type = "特殊限速";
		}
		return type;
	}
	
}
