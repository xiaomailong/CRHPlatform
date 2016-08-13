package com.crh.view.dialog;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.crh.service.RouteDataManagementDialogService;
import com.crh2.util.MyUtillity;

/**
 * ��������·���ơ��Ի���
 * @author huhui
 *
 */
public class RouteDataManagementInputRouteNameDialog extends JDialog {
	/**
	 * ����·���ơ����ı���
	 */
	private JTextField textField;

	public RouteDataManagementInputRouteNameDialog(Dialog owner) {
		super(owner,true);
		setSize(450, 300);
		MyUtillity.setFrameOnCenter(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "������·", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("��·����");
		label.setBounds(67, 79, 69, 15);
		panel.add(label);
		
		textField = new JTextField();
		textField.setBounds(145, 76, 205, 21);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("ȷ��");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isBlank()){//����Ϊ��
					JOptionPane.showMessageDialog(RouteDataManagementInputRouteNameDialog.this, "·��������Ϊ�գ�");
				}else{
					String newRouteName = textField.getText().trim();
					//�������ݿ�
					try {
						if(RouteDataManagementDialogService.saveRouteName(newRouteName)){
							//����JList
							RouteNameListPanel.addListElement(newRouteName);
							JOptionPane.showMessageDialog(RouteDataManagementInputRouteNameDialog.this, "�����·�ɹ���");
						}else{
							JOptionPane.showMessageDialog(RouteDataManagementInputRouteNameDialog.this, "�����·ʧ�ܣ�");
						}
						//�ر�Dialog
						dispose();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(RouteDataManagementInputRouteNameDialog.this, e.getMessage());
					}
				}
			}
		});
		button.setBounds(82, 147, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("ȡ��");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_1.setBounds(243, 147, 93, 23);
		panel.add(button_1);
		setVisible(true);
	}
	
	/**
	 * �ж���·�����Ƿ�Ϊ��
	 * @return
	 */
	public boolean isBlank(){
		if(textField.getText().trim().equals("")){
			return true;//����Ϊ��
		}else{
			return false;//���ݲ�Ϊ��
		}
	}
}
