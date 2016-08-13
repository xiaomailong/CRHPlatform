package com.crh.view.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.crh2.javabean.Serverinfo;
import com.crh2.service.DataService;
import com.crh2.util.MyUtillity;

/**
 * 常态运行中的网络设置
 * @author huhui
 *
 */
public class NetworkConfDialog extends JDialog {
	private JTextField portTF;
	private JTextField index1TF;
	private JTextField index2TF;
	private JTextField index3TF;
	private JTextField index4TF;
	private JTextField index5TF;
	private JTextField index6TF;
	private JTextField index7TF;
	private JTextField index8TF;
	
	private DataService dataService;
	private JTextField prefix1TF;
	private JTextField prefix2TF;
	private JTextField prefix3TF;
	private JTextField prefix4TF;
	private JTextField prefix5TF;
	private JTextField prefix6TF;
	private JTextField prefix7TF;
	private JTextField prefix8TF;

	public NetworkConfDialog() {
		setTitle("\u7F51\u7EDC\u8BBE\u7F6E");
		setBounds(100, 100, 685, 431);
		getContentPane().setLayout(null);
		
		dataService = new DataService();
		Serverinfo bean = dataService.getServerinfo();
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u63A5\u6536\u7AEF\u53E3\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 649, 68);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		portTF = new JTextField();
		portTF.setBounds(120, 26, 207, 21);
		panel.add(portTF);
		portTF.setColumns(10);
		portTF.setText(bean.getPort());
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "\u901A\u4FE1\u534F\u8BAE\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 88, 649, 257);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u4FE1\u53F7\u540D\u79F0");
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 12));
		lblNewLabel.setBounds(48, 27, 54, 15);
		panel_1.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("1. \u84C4\u7535\u6C60\u5F00\u5173");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(21, 52, 93, 15);
		panel_1.add(lblNewLabel_1);
		
		JLabel label = new JLabel("2. \u94A5\u5319\u5360\u7528");
		label.setFont(new Font("宋体", Font.PLAIN, 14));
		label.setBounds(21, 77, 93, 15);
		panel_1.add(label);
		
		JLabel label_1 = new JLabel("3. \u65B9\u5411\u5F00\u5173");
		label_1.setFont(new Font("宋体", Font.PLAIN, 14));
		label_1.setBounds(21, 102, 93, 15);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("--------------------------------------------------------------------------------------------------");
		label_2.setBounds(31, 62, 608, 15);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("--------------------------------------------------------------------------------------------------");
		label_3.setBounds(31, 89, 608, 15);
		panel_1.add(label_3);
		
		JLabel label_4 = new JLabel("4. \u53D7\u7535\u5F13");
		label_4.setFont(new Font("宋体", Font.PLAIN, 14));
		label_4.setBounds(21, 127, 93, 15);
		panel_1.add(label_4);
		
		JLabel label_5 = new JLabel("5. \u4E3B\u65AD\u8DEF\u5668");
		label_5.setFont(new Font("宋体", Font.PLAIN, 14));
		label_5.setBounds(21, 152, 93, 15);
		panel_1.add(label_5);
		
		JLabel label_6 = new JLabel("6. \u7275\u5F15\u529B\u8BBE\u5B9A\u767E\u5206\u6BD4(%)");
		label_6.setFont(new Font("宋体", Font.PLAIN, 14));
		label_6.setBounds(21, 177, 166, 15);
		panel_1.add(label_6);
		
		JLabel label_7 = new JLabel("7. \u5236\u52A8\u624B\u67C4\u7EA7\u4F4D");
		label_7.setFont(new Font("宋体", Font.PLAIN, 14));
		label_7.setBounds(21, 202, 124, 15);
		panel_1.add(label_7);
		
		JLabel lblkmh = new JLabel("8. \u901F\u5EA6\u624B\u67C4\u8BBE\u7F6E\u7684\u6700\u9AD8\u901F\u5EA6(km/h)");
		lblkmh.setFont(new Font("宋体", Font.PLAIN, 14));
		lblkmh.setBounds(21, 227, 223, 15);
		panel_1.add(lblkmh);
		
		JLabel label_8 = new JLabel("--------------------------------------------------------------------------------------------------");
		label_8.setBounds(31, 114, 608, 15);
		panel_1.add(label_8);
		
		JLabel label_9 = new JLabel("--------------------------------------------------------------------------------------------------");
		label_9.setBounds(31, 139, 608, 15);
		panel_1.add(label_9);
		
		JLabel label_10 = new JLabel("--------------------------------------------------------------------------------------------------");
		label_10.setBounds(31, 164, 608, 15);
		panel_1.add(label_10);
		
		JLabel label_11 = new JLabel("--------------------------------------------------------------------------------------------------");
		label_11.setBounds(31, 191, 608, 15);
		panel_1.add(label_11);
		
		JLabel label_12 = new JLabel("--------------------------------------------------------------------------------------------------");
		label_12.setBounds(31, 216, 608, 15);
		panel_1.add(label_12);
		
		JLabel label_13 = new JLabel("\u504F\u79FB\u91CF");
		label_13.setFont(new Font("宋体", Font.BOLD, 12));
		label_13.setBounds(402, 27, 54, 15);
		panel_1.add(label_13);
		
		index1TF = new JTextField();
		index1TF.setBounds(390, 47, 66, 21);
		panel_1.add(index1TF);
		index1TF.setColumns(10);
		index1TF.setText(bean.getIndex1());
		
		index2TF = new JTextField();
		index2TF.setColumns(10);
		index2TF.setBounds(390, 74, 66, 21);
		panel_1.add(index2TF);
		index2TF.setText(bean.getIndex2());
		
		index3TF = new JTextField();
		index3TF.setColumns(10);
		index3TF.setBounds(390, 99, 66, 21);
		panel_1.add(index3TF);
		index3TF.setText(bean.getIndex3());
		
		index4TF = new JTextField();
		index4TF.setColumns(10);
		index4TF.setBounds(390, 124, 66, 21);
		panel_1.add(index4TF);
		index4TF.setText(bean.getIndex4());
		
		index5TF = new JTextField();
		index5TF.setColumns(10);
		index5TF.setBounds(390, 149, 66, 21);
		panel_1.add(index5TF);
		index5TF.setText(bean.getIndex5());
		
		index6TF = new JTextField();
		index6TF.setColumns(10);
		index6TF.setBounds(390, 174, 66, 21);
		panel_1.add(index6TF);
		index6TF.setText(bean.getIndex6());
		
		index7TF = new JTextField();
		index7TF.setColumns(10);
		index7TF.setBounds(390, 201, 66, 21);
		panel_1.add(index7TF);
		index7TF.setText(bean.getIndex7());
		
		index8TF = new JTextField();
		index8TF.setColumns(10);
		index8TF.setBounds(390, 226, 66, 21);
		panel_1.add(index8TF);
		index8TF.setText(bean.getIndex8());
		
		JLabel label_14 = new JLabel("\u7AEF\u53E3\u5927\u5C0F");
		label_14.setFont(new Font("宋体", Font.BOLD, 12));
		label_14.setBounds(545, 27, 54, 15);
		panel_1.add(label_14);
		
		JLabel lblNewLabel_2 = new JLabel("1bit");
		lblNewLabel_2.setBounds(555, 52, 54, 15);
		panel_1.add(lblNewLabel_2);
		
		JLabel label_15 = new JLabel("1bit");
		label_15.setBounds(555, 77, 54, 15);
		panel_1.add(label_15);
		
		JLabel label_16 = new JLabel("1bit");
		label_16.setBounds(555, 102, 54, 15);
		panel_1.add(label_16);
		
		JLabel label_17 = new JLabel("1bit");
		label_17.setBounds(555, 127, 54, 15);
		panel_1.add(label_17);
		
		JLabel label_18 = new JLabel("1bit");
		label_18.setBounds(555, 152, 54, 15);
		panel_1.add(label_18);
		
		JLabel lblInteger = new JLabel("INTEGER16");
		lblInteger.setBounds(545, 177, 54, 15);
		panel_1.add(lblInteger);
		
		JLabel lblUnsigned = new JLabel("UNSIGNED8");
		lblUnsigned.setBounds(545, 202, 54, 15);
		panel_1.add(lblUnsigned);
		
		JLabel lblUnsigned_1 = new JLabel("UNSIGNED16");
		lblUnsigned_1.setBounds(545, 227, 60, 15);
		panel_1.add(lblUnsigned_1);
		
		JLabel label_19 = new JLabel("\u6807\u8BC6\u7B26");
		label_19.setFont(new Font("宋体", Font.BOLD, 12));
		label_19.setBounds(270, 27, 54, 15);
		panel_1.add(label_19);
		
		prefix1TF = new JTextField();
		prefix1TF.setText("00200000");
		prefix1TF.setColumns(10);
		prefix1TF.setBounds(258, 47, 66, 21);
		panel_1.add(prefix1TF);
		prefix1TF.setText(bean.getPrefix1());
		
		prefix2TF = new JTextField();
		prefix2TF.setText("00200830");
		prefix2TF.setColumns(10);
		prefix2TF.setBounds(258, 74, 66, 21);
		panel_1.add(prefix2TF);
		prefix2TF.setText(bean.getPrefix2());
		
		prefix3TF = new JTextField();
		prefix3TF.setText("00200000");
		prefix3TF.setColumns(10);
		prefix3TF.setBounds(258, 99, 66, 21);
		panel_1.add(prefix3TF);
		prefix3TF.setText(bean.getPrefix3());
		
		prefix4TF = new JTextField();
		prefix4TF.setText("002000DC");
		prefix4TF.setColumns(10);
		prefix4TF.setBounds(258, 124, 66, 21);
		panel_1.add(prefix4TF);
		prefix4TF.setText(bean.getPrefix4());
		
		prefix5TF = new JTextField();
		prefix5TF.setText("020000DE");
		prefix5TF.setColumns(10);
		prefix5TF.setBounds(258, 149, 66, 21);
		panel_1.add(prefix5TF);
		prefix5TF.setText(bean.getPrefix5());
		
		prefix6TF = new JTextField();
		prefix6TF.setText("002000B9");
		prefix6TF.setColumns(10);
		prefix6TF.setBounds(258, 174, 66, 21);
		panel_1.add(prefix6TF);
		prefix6TF.setText(bean.getPrefix6());
		
		prefix7TF = new JTextField();
		prefix7TF.setText("00200C12");
		prefix7TF.setColumns(10);
		prefix7TF.setBounds(258, 201, 66, 21);
		panel_1.add(prefix7TF);
		prefix7TF.setText(bean.getPrefix7());
		
		prefix8TF = new JTextField();
		prefix8TF.setText("00200015");
		prefix8TF.setColumns(10);
		prefix8TF.setBounds(258, 226, 66, 21);
		panel_1.add(prefix8TF);
		prefix8TF.setText(bean.getPrefix8());
		
		JButton portBtn = new JButton("\u4FEE\u6539");
		portBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkTextField()){ //全为数字
					Serverinfo serverinfo = new Serverinfo();
					serverinfo.setPort(portTF.getText().trim());
					serverinfo.setIndex1(index1TF.getText().trim());
					serverinfo.setIndex2(index2TF.getText().trim());
					serverinfo.setIndex3(index3TF.getText().trim());
					serverinfo.setIndex4(index4TF.getText().trim());
					serverinfo.setIndex5(index5TF.getText().trim());
					serverinfo.setIndex6(index6TF.getText().trim());
					serverinfo.setIndex7(index7TF.getText().trim());
					serverinfo.setIndex8(index8TF.getText().trim());
					serverinfo.setPrefix1(prefix1TF.getText().trim());
					serverinfo.setPrefix2(prefix2TF.getText().trim());
					serverinfo.setPrefix3(prefix3TF.getText().trim());
					serverinfo.setPrefix4(prefix4TF.getText().trim());
					serverinfo.setPrefix5(prefix5TF.getText().trim());
					serverinfo.setPrefix6(prefix6TF.getText().trim());
					serverinfo.setPrefix7(prefix7TF.getText().trim());
					serverinfo.setPrefix8(prefix8TF.getText().trim());
					dataService.saveServerinfo(serverinfo);
					JOptionPane.showMessageDialog(NetworkConfDialog.this, "保存成功！");
				}else{
					JOptionPane.showMessageDialog(NetworkConfDialog.this, "请填入正确数字！");
				}
			}
		});
		portBtn.setBounds(225, 360, 93, 23);
		getContentPane().add(portBtn);
		
		JButton button = new JButton("\u53D6\u6D88");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button.setBounds(367, 360, 93, 23);
		getContentPane().add(button);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		MyUtillity.setFrameOnCenter(this);
		setModal(true);
		setVisible(true);
	}
	
	/**
	 * 校验个文本框内容是否正确
	 */
	public boolean checkTextField(){
		boolean b = false;
		if (isIndexPositiveNumber(portTF.getText().trim())
				&& isIndexPositiveNumber(index1TF.getText().trim())
				&& isIndexPositiveNumber(index2TF.getText().trim())
				&& isIndexPositiveNumber(index3TF.getText().trim())
				&& isIndexPositiveNumber(index4TF.getText().trim())
				&& isIndexPositiveNumber(index5TF.getText().trim())
				&& isIndexPositiveNumber(index6TF.getText().trim())
				&& isIndexPositiveNumber(index7TF.getText().trim())
				&& isIndexPositiveNumber(index8TF.getText().trim())
				&& isPrefixPositiveNumber(prefix1TF.getText().trim())
				&& isPrefixPositiveNumber(prefix2TF.getText().trim())
				&& isPrefixPositiveNumber(prefix3TF.getText().trim())
				&& isPrefixPositiveNumber(prefix4TF.getText().trim())
				&& isPrefixPositiveNumber(prefix5TF.getText().trim())
				&& isPrefixPositiveNumber(prefix6TF.getText().trim())
				&& isPrefixPositiveNumber(prefix7TF.getText().trim())
				&& isPrefixPositiveNumber(prefix8TF.getText().trim())
				) {
			b = true;
		}
		return b;
	}
	
	/**
	 * 校验偏移量是否是正数
	 */
	public boolean isIndexPositiveNumber(String str){
		try {
			if(MyUtillity.isNumber(str) && Integer.parseInt(str)>=0 && !"".equals(str)){
				return true;
			}else{
				return false;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	/**
	 * 校验标识符是否是正数
	 */
	public boolean isPrefixPositiveNumber(String str) {
		if (!"".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
}
