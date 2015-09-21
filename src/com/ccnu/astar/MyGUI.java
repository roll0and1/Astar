package com.ccnu.astar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MyGUI extends JFrame {
	static JLabel startLabel = new JLabel();

	static JLabel terminLabel = new JLabel();

	static JLabel pathLabel = new JLabel();

	public static void main(String[] args) {

		MyGUI myGUI = new MyGUI();
		myGUI.launchFram();

	}

	// 关闭窗口
	public void doWindowClosing(WindowEvent e) {

		int i = JOptionPane.showConfirmDialog(null, "提醒", "确认关闭？",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (i == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else {
			return;
		}
	}

	public void launchFram() {

		// 实例化展示区
		MyCanvas canvas = new MyCanvas();
		canvas.setLayout(null);

		// 实例化按钮标签区
		ButtonsPanel buttonsPanel = new ButtonsPanel(canvas);
		this.setResizable(false);

		// 进行布局
		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);
		borderLayout.setHgap(10);

		// 实例化展示区和按钮标签区
		JPanel tablePanel = new JPanel();
		JPanel jpRight = new JPanel();

		// 实例化标签
		JLabel realTimeLabel = new JLabel("实时路径坐标", SwingConstants.CENTER);

		// 实例化右侧面板中的子面板
		JPanel jps = new JPanel();
		JPanel jpt = new JPanel();
		JPanel jpf = new JPanel();
		JPanel jpd = new JPanel();
		JPanel jpb = new JPanel();
		JPanel jpm = new JPanel();

		// tablePanel不进行布局
		tablePanel.setLayout(null);

		// 实例化按钮
		JButton start = new JButton("设置起点");
		JButton terminate = new JButton("设置终点");
		JButton barrier = new JButton("设置障碍");
		JButton reset = new JButton("清空面板");
		JButton search = new JButton("开始寻径");
		JButton generateFile = new JButton("生成路径报告");

		// 将按钮添加到子面板中
		jps.add(start);
		jpt.add(terminate);
		jpf.add(search);
		jpd.add(generateFile);
		jpb.add(barrier);
		jpm.add(reset);

		// 设置障碍物和清除面板两个按钮处于同一行
		JPanel littlePanel = new JPanel();
		littlePanel.setLayout(new GridLayout(0, 2));
		littlePanel.add(jpb);
		littlePanel.add(jpm);

		// 对三个标签中文字内容进行格式化
		formatLable(startLabel, Color.GREEN, 30);
		formatLable(terminLabel, Color.BLUE, 30);
		formatLable(pathLabel, Color.RED, 30);

		// 对右侧面板进行布局
		buttonsPanel.setLayout(new GridLayout(0, 1, 0, 30));

		// 将组件添加到右侧的buttonsPanel子面板中
		buttonsPanel.add(startLabel);
		buttonsPanel.add(jps);
		buttonsPanel.add(terminLabel);
		buttonsPanel.add(jpt);
		buttonsPanel.add(littlePanel);
		buttonsPanel.add(pathLabel);
		buttonsPanel.add(realTimeLabel);
		buttonsPanel.add(jpf);
		buttonsPanel.add(jpd);
		tablePanel.add(canvas);

		// 将buttonsPanel添加到右侧面板中
		jpRight.add(buttonsPanel);

		// 设置窗口和面板的位置和大小
		this.setSize(830, 650);
		tablePanel.setSize(620, 620);
		canvas.setBounds(10, 10, 601, 601);

		// 让窗口处于显示器的中心位置
		this.setLocation(
				(Toolkit.getDefaultToolkit().getScreenSize().width - this
						.getWidth()) / 2, (Toolkit.getDefaultToolkit()
						.getScreenSize().height - this.getHeight()) / 2);

		// 将两个子面板添加到窗口中
		this.add(tablePanel, BorderLayout.CENTER);
		this.add(jpRight, BorderLayout.EAST);
		this.setTitle("基于栅格地图的A星寻路算法演示软件 V1.0");
		this.setVisible(true);

		// 为按钮设置监听
		start.addActionListener(buttonsPanel);
		terminate.addActionListener(buttonsPanel);
		barrier.addActionListener(buttonsPanel);
		reset.addActionListener(buttonsPanel);
		search.addActionListener(buttonsPanel);
		generateFile.addActionListener(buttonsPanel);

		// 添加窗口监听器
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				doWindowClosing(e);
			}

		});
		// 设置窗口关闭键不可用，以达到关闭窗口时弹出对话框
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	}

	// 程序启动时面板标签内容的格式化
	public void formatLable(JLabel label, Color color, int size) {
		label.setText("(0,0)"); // 设置标签文字内容
		label.setForeground(color);// 设置标签文字的颜色
		java.awt.Font font = new java.awt.Font("黑体", java.awt.Font.BOLD, size); // 标签字体
		label.setFont(font);
		label.setHorizontalAlignment(SwingConstants.CENTER); // 设置标签对齐方式
	}

}
