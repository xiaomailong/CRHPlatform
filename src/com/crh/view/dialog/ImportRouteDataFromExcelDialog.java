package com.crh.view.dialog;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.crh.service.RouteDataManagementDialogService;
import com.crh2.calculate.excel.ImportExcel;
import com.crh2.util.MyUtillity;

/**
 * 从Excel文件中导入列车线路数据
 * @author huhui
 *
 */
public class ImportRouteDataFromExcelDialog extends JDialog {
	private JTextField textField_1;
	/**
	 *  定义文件选择器
	 */
	private JFileChooser excelFileChooser;
	/**
	 *  定义文件过滤器
	 */
	private FileNameExtensionFilter fileFilter;

	public ImportRouteDataFromExcelDialog(final String routeName, Dialog owner) {
		super(owner,true);
		setBounds(100, 100, 517, 288);
		getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		setTitle(routeName);
		MyUtillity.setFrameOnCenter(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u6570\u636E\u5BFC\u5165", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(28, 81, 337, 21);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JButton button = new JButton("文件选择");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excelFileChooser = new JFileChooser();
				fileFilter = new FileNameExtensionFilter(".xls", "xls");
				excelFileChooser.setFileFilter(fileFilter);
				excelFileChooser.setVisible(true);//显示
				int retval = excelFileChooser.showOpenDialog(ImportRouteDataFromExcelDialog.this);
				 if(retval ==JFileChooser.APPROVE_OPTION){
					//得到用户选择文件的绝对路径
					String filePath = excelFileChooser.getSelectedFile().getAbsolutePath();
					//给文件路径文本框赋值
					textField_1.setText(filePath.trim());
				 }
			}
		});
		button.setBounds(380, 80, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("导入");
		button_1.addActionListener(new ActionListener() {
			String name = routeName;
			public void actionPerformed(ActionEvent e) {
				int routeId = RouteDataManagementDialogService.getRouteIdByName(name);
				String excelPath = textField_1.getText().trim();
				ImportExcel importExcel = new ImportExcel(routeId, excelPath);
				if(importExcel.readDataFromExcel()){
					JOptionPane.showMessageDialog(ImportRouteDataFromExcelDialog.this, "数据导入成功！");
				}else{
					JOptionPane.showMessageDialog(ImportRouteDataFromExcelDialog.this, "数据导入失败！");
				}
			}
		});
		button_1.setBounds(126, 161, 93, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("取消");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_2.setBounds(260, 161, 93, 23);
		panel.add(button_2);
		
		setVisible(true);
	}
}
