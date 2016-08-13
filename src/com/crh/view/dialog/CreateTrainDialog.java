package com.crh.view.dialog;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.crh.service.TrainEditPanelService;
import com.crh.view.panel.TrainEditPanel;
import com.crh2.javabean.TrainCategory;
import com.crh2.util.MyUtillity;

/**
 * “创建列车”的对话框
 * @author huhui
 *
 */
public class CreateTrainDialog extends JDialog {
	/**
	 * 填写列车名称的文本框
	 */
	private JTextField trainNameField;
	/**
	 * 创建列车后列车信息在数据库中的id
	 */
	private int generatedId;
	/**
	 * 列车名称文本框
	 */
	private String trainNameText;

	public CreateTrainDialog(JPanel owner) {
		super((Frame)SwingUtilities.windowForComponent(owner));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("\u65B0\u5EFA\u5217\u8F66");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label.setBounds(187, 27, 65, 16);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u5217\u8F66\u540D\u79F0\uFF1A");
		label_1.setBounds(50, 98, 65, 15);
		getContentPane().add(label_1);
		
		trainNameField = new JTextField();
		trainNameField.setBounds(124, 95, 247, 21);
		getContentPane().add(trainNameField);
		trainNameField.setColumns(10);
		
		JButton button = new JButton("\u786E\u5B9A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trainNameText = trainNameField.getText().trim();
				if(trainNameText.equals("")){
					JOptionPane.showMessageDialog(CreateTrainDialog.this, "列车名称不能为空！");
				}else{
					generatedId = TrainEditPanelService.saveTrainCategory(trainNameText);
					if(generatedId == -1){//列车名已存在
						JOptionPane.showMessageDialog(CreateTrainDialog.this, "列车名称已存在，请重新输入！");
						return;
					}
					JOptionPane.showMessageDialog(CreateTrainDialog.this, "保存成功！");
					TrainEditPanel.createTrainCategory(generateTrainCategory(generatedId, trainNameText));
					dispose();
				}
			}
		});
		button.setBounds(92, 177, 93, 23);
		getContentPane().add(button);
		
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_1.setBounds(252, 177, 93, 23);
		getContentPane().add(button_1);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		MyUtillity.setFrameOnCenter(this);
		setVisible(true);
	}
	
	/**
	 * 生成列车种类TrainCategory
	 * @param id
	 * @param name
	 * @return
	 */
	public TrainCategory generateTrainCategory(int id, String name){
		TrainCategory trainCategory = new TrainCategory();
		trainCategory.setId(id);
		trainCategory.setName(name);
		return trainCategory;
	}

}
