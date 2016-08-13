/**
 * �޸��û�����Ľ���
 */
package com.crh2.frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.crh2.dao.SQLHelper;
import com.crh2.util.MyUtillity;

public class ModifyPasswordFrame extends JFrame implements ActionListener {

	/**
	 * @param args
	 */
	// ����label
	private JLabel passwordLabel, passwordConfirmLabel;
	private JPasswordField passwordText, passwordConfirmText;
	private JButton confirmButton, cancelButton;

	// ���캯������ʼ�������ؼ�������
	public ModifyPasswordFrame() {
		// ��ʼ��label
		passwordLabel = new JLabel("�� �� ��");
		passwordConfirmLabel = new JLabel("����ȷ��");
		// ��ʼ��textfield
		passwordText = new JPasswordField();
		passwordConfirmText = new JPasswordField();
		// ��ʼ��button
		confirmButton = new JButton("ȷ��");
		cancelButton = new JButton("ȡ��");
		// Ϊ��ť����¼���Ӧ
		confirmButton.addActionListener(this);
		confirmButton.setActionCommand("confirm");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");

		// ���ò���
		this.setLayout(new GridLayout(3, 2));
		// ����frameΪtop
		this.setAlwaysOnTop(true);
		// ���ؼ����뵽frame��
		this.add(passwordLabel);
		this.add(passwordText);
		this.add(passwordConfirmLabel);
		this.add(passwordConfirmText);
		this.add(confirmButton);
		this.add(cancelButton);
		this.setSize(240, 120);
		this.setTitle("�޸�����");
		// ����frame����
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModifyPasswordFrame modifyPasswordFrame = new ModifyPasswordFrame();
	}

	// �޸����ݿ��е�admin����
	public boolean modifyPassword(String newPassword) {
		SQLHelper sqlHelper = new SQLHelper();
		// ��֯sql���
		String sql = "UPDATE users SET PASSWORD = '" + newPassword
				+ "' WHERE NAME = 'admin';";
		return sqlHelper.update(sql, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("confirm")) {
			String passwordStr = new String(passwordText.getPassword());
			String passwordConfirmStr = new String(passwordConfirmText
					.getPassword());
			// ���������ͬ���޸�
			if (passwordStr.equals(passwordConfirmStr)) {
				if (this.modifyPassword(passwordStr)) {
					JOptionPane.showMessageDialog(this, "�����޸ĳɹ�");
					this.dispose();
				}
			} else {
				JOptionPane.showMessageDialog(this, "�������벻һ�£�����������");
				// ��������
				passwordText.setText("");
				passwordConfirmText.setText("");
			}
		} else if (e.getActionCommand().equals("cancel")) {
			this.dispose();
		}
	}

}
