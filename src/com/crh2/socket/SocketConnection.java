package com.crh2.socket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.crh2.service.DataService;

/**
 * UDP连接服务器
 * @author huhui
 *
 */
public class SocketConnection implements Runnable {	
	
	private DatagramSocket ds = null;
	private DatagramPacket dp = null;
	byte[] buffer = new byte[1024];

	private int PORT = 0;
	
	private boolean isRun = true;
	
	/**
	 * 从数据库中拿到端口信息
	 */
	private DataService dataService = new DataService();
	
	public static void main(String [] args){
		SocketConnection sc = new SocketConnection();
		Thread thread = new Thread(sc);
		thread.start();
	}

	public SocketConnection() {
		try {
			//从数据库中拿到ip和端口信息
			PORT = Integer.parseInt(dataService.getServerinfo().getPort());
			ds = new DatagramSocket(PORT);
			dp = new DatagramPacket(buffer, buffer.length);
		} catch (Exception e) {
			e.printStackTrace();
			isRun = false;
		}
	}

	public void connectServer() {
		try {
			String info = "";
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(buffer));
			ds.receive(dp);
			info = bytes2HexString(dp.getData());
			System.out.println(info);
			UDPDataParser.dataParser(info);
			in.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
			isRun = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 转换16进制数 
	 */
	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret.substring(0, 76);
	}

	@Override
	public void run() {
		while(isRun){
			this.connectServer();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}