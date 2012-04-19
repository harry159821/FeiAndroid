package com.njnu.kai.fordevd;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class MainFrm {

	private JFrame frmFordevd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//		formparams.add(new BasicNameValuePair("param1", "value1"));
//		formparams.add(new BasicNameValuePair("param2", "value2"));
//		try {
//			System.out.println(URLEncodedUtils.format(formparams, "UTF-8"));
////			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
//			StringEntity entity = new StringEntity("param1=value1&param2=value2", "UTF-8");
//			entity.setContentType(URLEncodedUtils.CONTENT_TYPE + HTTP.CHARSET_PARAM  + "UTF-8");
//			System.out.println(entity.getContentLength());
//			System.out.println(entity.getContentType());
//			System.out.println(entity.getContentEncoding());
//			System.out.println(entity.getClass());
//			byte[] datahah = new byte[32];
//			System.out.println(datahah);
//			InputStream ins = entity.getContent();
//			System.out.println(ins.available());
////			System.out.println(ins.read());
////			System.out.println(ins.read());
////			System.out.println(ins.read());
////			System.out.println(ins.read());
//			ins.read(datahah);
//			System.out.println(new String(datahah));
//			System.out.println(entity.getContent());
//
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrm window = new MainFrm();
					window.frmFordevd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFordevd = new JFrame();
		frmFordevd.setResizable(false);
		frmFordevd.setTitle("ForDevD");
		frmFordevd.setBounds(100, 100, 800, 600);
		frmFordevd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblStatusbar = new JLabel("  Kai Java Gui Application");
		frmFordevd.getContentPane().add(lblStatusbar, BorderLayout.SOUTH);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFordevd.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Friend", null, panel, null);
		panel.setLayout(null);

		btnFriendStart = new JButton("Start");
		btnFriendStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnFriendStart.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						threadDoAddFriendTask();
					}
				}).start();
			}
		});
		btnFriendStart.setBounds(656, 13, 117, 29);
		panel.add(btnFriendStart);

		txtFriendResult = new JTextArea();
		panel.add(txtFriendResult);
		txtFriendResult.setText("welcome.\r\n");
		txtFriendResult.setColumns(64);
		txtFriendResult.setRows(10);
		txtFriendResult.setBounds(2, 2, 425, 309);

		JScrollPane scrollPane = new JScrollPane(txtFriendResult);
		scrollPane.setBounds(0, 54, 773, 434);
		panel.add(scrollPane);

		tabbedPane.addTab("Tab 2", new JLabel("Tab 2 Content"));
		tabbedPane.addTab("Tab 3", new JLabel("Tab 3 Content"));

		JMenuBar menuBar = new JMenuBar();
		frmFordevd.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmItem = new JMenuItem("Item1");
		mnFile.add(mntmItem);
	}

	private void threadDoAddFriendTask() {
		AddLineToResult("Start......");
		boolean operateSucess = true;
		if (!IsValidDevdCookie()) {
			AddLineToResult("Cookie无效了，重新登录......");
			if (LoginDevdBBs()) {
				AddLineToResult("登录成功......");
			} else {
				AddLineToResult("登录失败，Over！");
				operateSucess = false;
			}
		} else {
			AddLineToResult("Cookie有效，无需重新登录!");
		}

		if (operateSucess) {

		}

		EnableButton(true);
	}

	private boolean LoginDevdBBs() {
		_lastContent = HttpUtility
				.GetUseAutoEncoding("http://www.devdiv.com/member.php?mod=logging&action=login");
		if (_lastContent.indexOf("登录   DEVDIV.COM") < 0) {
			return false;
		}
		// AddLineToResult(_lastContent);
		Pattern pattern = Pattern
				.compile("<input type=\"hidden\" name=\"formhash\" value=\"(\\w+?)\" />");
		Matcher match = pattern.matcher(_lastContent);
		match.find();
		String formhash = match.group(1);

		pattern = Pattern.compile("action=\"(member.php\\?mod=logging.+?)\">");
		match = pattern.matcher(_lastContent);
		match.find();
		String posturl = match.group(1).replace("&amp;", "&");

		pattern = Pattern.compile("value=\"(\\d+?)\"  />自动登录</label>");
		match = pattern.matcher(_lastContent);
		match.find();
		String cookietime = match.group(1);

		String md5pw = HttpUtility.getMD5(devUserPW.getBytes());
//		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//		formparams.add(new BasicNameValuePair("formhash", formhash));
//		formparams.add(new BasicNameValuePair("referer", ""));
//		formparams.add(new BasicNameValuePair("username", devUserAccount));
//		formparams.add(new BasicNameValuePair("password", md5pw));
//		formparams.add(new BasicNameValuePair("questionid", "0"));
//		formparams.add(new BasicNameValuePair("answer", ""));
//		formparams.add(new BasicNameValuePair("cookietime", cookietime));
		String postdata = String.format("formhash=%s&referer=&username=%s&password=%s&questionid=0&answer=&cookietime=%s", formhash, devUserAccount, md5pw, cookietime);
//		AddLineToResult(posturl + " " + postdata);
		_lastContent = HttpUtility.PostUseAutoEncoding("http://www.devdiv.com/" + posturl, postdata, HTTP.UTF_8);
//		AddLineToResult(_lastContent);
		return _lastContent.indexOf("欢迎您回来") >= 0;
	}

	private boolean IsValidDevdCookie() {
		String needStr = String
				.format(".com/home.php?mod=space&amp;uid=%s\" target=\"_blank\" title=\"访问我的空间\">%s</a></strong>",
						devUid, devUserAccount);
		_lastContent = HttpUtility
				.GetUseAutoEncoding("http://www.devdiv.com/forum-154-1.html");
		return _lastContent.indexOf(needStr) >= 0;
		// System.out.println("\r\n\r\n------------\r\n\r\n" + _lastContent);
		// txtFriendResult.setText(String.format("indexofs=%d", indexOfStr));
	}

	private void AddLineToResult(String lineText) {
		uiOperator.lineText = lineText;
		try {
			SwingUtilities.invokeAndWait(uiOperator);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void EnableButton(boolean enable) {
		uiOperator.btnEnable = enable;
		try {
			SwingUtilities.invokeAndWait(uiOperator);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private UIOperator uiOperator = new UIOperator();

	private String _lastContent = null;
	private JTextArea txtFriendResult = null;
	private JButton btnFriendStart = null;

	private final static String devUserAccount = "waring1983";
	private final static String devUserPW = "qwertasdf";
	private final static String devUid = "215055";

	private class UIOperator implements Runnable {
		@Override
		public void run() {
			if (lineText != null) {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]: ");
			String oriText = txtFriendResult.getText();
			txtFriendResult.setText(oriText + dateFormat.format(date) + lineText + "\r\n");
			lineText = null;
			} else {
				btnFriendStart.setEnabled(btnEnable);
			}
		}
		public String lineText = null;
		public boolean btnEnable;
	}
}