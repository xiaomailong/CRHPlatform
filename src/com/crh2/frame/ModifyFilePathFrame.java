/**
 * 修改txt保存路径的txt
 */
package com.crh2.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.crh2.dao.SQLHelper;
import com.crh2.service.PathFileService;
import com.crh2.util.MyUtillity;

public class ModifyFilePathFrame extends JFrame implements ActionListener {

	/**
	 * @param args
	 */
	// 定义label
	private JLabel pathLabel;
	private JTextField pathText;
	private JButton fileChooseButton,confirmButton, cancelButton;
	//定义文件选择器
	private JFileChooser fileChooser;
	//定义文件过滤器
	private FileNameExtensionFilter fileFilter;
	//定义panel
	private JPanel contentPanel,buttonPanel;
	//新的路径path
	private String newPath = "";

	// 构造函数，初始化各个控件及窗口
	public ModifyFilePathFrame() {
		contentPanel = new JPanel();
		// 初始化label
		pathLabel = new JLabel("保存路径：");
		// 初始化textfield
		pathText = new JTextField(41);
		pathText.setText(PathFileService.getDefaultPath());
		// 初始化button
		fileChooseButton = new JButton("路径修改");
		contentPanel.add(pathLabel,BorderLayout.WEST);
		contentPanel.add(pathText,BorderLayout.CENTER);
		contentPanel.add(fileChooseButton,BorderLayout.SOUTH);
		contentPanel.setBorder(BorderFactory.createTitledBorder("路径选择"));
		
		buttonPanel = new JPanel();
		confirmButton = new JButton("保存");
		cancelButton = new JButton("取消");
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		
		// 为按钮添加事件响应
		fileChooseButton.addActionListener(this);
		fileChooseButton.setActionCommand("chooseFile");
		confirmButton.addActionListener(this);
		confirmButton.setActionCommand("confirm");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");

		// 设置frame为top
		this.setAlwaysOnTop(true);
		// 将控件加入到frame中
		this.add(contentPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		
		this.setSize(360, 240);
		this.setTitle("\"车辆设置\"文件保存路径");
		// 设置frame居中
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	// 点击“文件选择”按钮
	public void fileChooseButtonClicked() {
		// 初始化JFileChooser
		fileChooser = new JFileChooser();
		fileFilter = new FileNameExtensionFilter(".txt", ".txt");
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setVisible(true);// 显示
		int retval = fileChooser.showOpenDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION) {
			// 得到用户选择文件的绝对路径
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			// 给文件路径文本框赋值
			pathText.setText(filePath.trim()+".txt");
			newPath = filePath.trim()+".txt";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		ModifyFilePathFrame modifyFilePathFrame = new ModifyFilePathFrame();
	}

	// 修改数据库中path
	public boolean modifyPath(String newPath) {
		newPath = newPath.replaceAll("\\\\", "\\\\\\\\");
		SQLHelper sqlHelper = new SQLHelper();
		// 组织sql语句
		String sql = "UPDATE traintypesavepath SET path='"+newPath+"'";
		return sqlHelper.update(sql, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("confirm")) {
			if(this.modifyPath(newPath)){
				JOptionPane.showMessageDialog(this, "路径修改成功！");
			}else{
				JOptionPane.showMessageDialog(this, "路径修改失败！");
			}
		} else if (e.getActionCommand().equals("cancel")) {
			this.dispose();
		}else if(e.getActionCommand().equals("chooseFile")){
			this.fileChooseButtonClicked();
		}
	}

}
