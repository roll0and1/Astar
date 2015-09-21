package com.ccnu.astar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lowagie.text.Element;
import com.lowagie.text.Font;

class MyCanvas extends JPanel implements MouseListener, MouseMotionListener,
		Runnable {
	private int buttonCommand; // 鼠标点击按钮标志
	Point startPoint = null; // 传入到算法中的起点坐标
	Point terminatePoint = null;// 传入到算法中的终点坐标
	Set<Point> barrierPointSet = new HashSet<Point>(); // 传入到算法中的障碍物坐标集合
	List<Point> pathPointList = new ArrayList<Point>(); // 成功找出的路径的集合
	List<Point> tempPointList = null;// 临时路径集合

	private String positionString; // 鼠标在canvas上点击的坐标
	private String currentPathString;// 当前路径坐标
	private String sPointString;// 获取的起点坐标
	private String tPointString;// 获取的终点坐标
	private Set<String> bPointSet = new HashSet<String>(); // 获取的障碍物坐标集合
	private List<String> rPointList = new ArrayList<String>();// 得到的路径坐标集合
	private long time;// 寻找路径所需的时间
	JLabel startLabel = MyGUI.startLabel; // 显示起点坐标的标签
	JLabel terminLabel = MyGUI.terminLabel; // 显示终点坐标的标签
	JLabel pathLabel = MyGUI.pathLabel; // 显示实时路径坐标的标签

	// 标签内容格式化
	public void formatLable(JLabel label, String text, Color color, int size) {
		label.setText(text);
		label.setForeground(color);
		java.awt.Font font = new java.awt.Font("黑体", java.awt.Font.BOLD, size);
		label.setFont(font);
	}

	// 得到路径和寻径所需的时间
	public void generatePath() {
		// 判断pathPointList是否为空，来判断用户是否在未生成报告钱多次点击“开始寻径”按钮
		if (!this.pathPointList.isEmpty()) {
			removetempPointList();
		}
		Path searchPath = new Path(startPoint, this.terminatePoint,
				this.barrierPointSet);
		this.tempPointList = searchPath.search();

		setTime(searchPath.getSerachTime());
		new Thread(this).start();
	}

	// 生成路径报告
	public void generateReport() {

		if (!resultPath()) { // 路径是否存在
			return;
		}
		StringBuffer bPointStringBuffer = new StringBuffer(); // 以StringBuffer存储的障碍物坐标集合
		StringBuffer rPointStringBuffer = new StringBuffer(); // 以StringBuffer存储的路径坐标集合
		for (String str : bPointSet) {
			bPointStringBuffer.append(str + ";");
		}
		for (String str : rPointList) {
			rPointStringBuffer.append(str + ";");
		}
		SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String timestamp = format1.format(date);
		String dateTime = format2.format(date);
		String fileTime = "(" + format3.format(date) + ")"; // 生成报告的时间

		String dirPath = "D:\\AstarPathResultFile"; // 报告所处文件夹名
		String filePath = "D:\\AstarPathResultFile\\" + dateTime + "_"
				+ timestamp + ".doc"; // 生成报告的路径

		// 生成报告内容
		String Title = "A星寻路算法路径报告"; // 报告标题
		String startContext = "起点坐标：" + getsPointString();// 起点坐标
		String terminateContext = "终点坐标：" + gettPointString();// 终点坐标
		String barrierContext = "障碍物坐标：" + bPointStringBuffer.toString(); // 障碍物坐标
		String resultContext = "路径坐标：" + rPointStringBuffer.toString(); // 路径坐标
		String timeContext = "寻找路径耗时：" + getTime() + "ms"; // 寻找路径耗时

		File dirFile = new File(dirPath);
		FileOutputStream fos = null;
		File resultPathFile = new File(filePath);

		// 创建文件夹
		if (!(dirFile.exists())) {

			dirFile.mkdirs();
		}

		// 创建文件
		if (!(resultPathFile.exists()))
			try {
				resultPathFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
			fos = new FileOutputStream(resultPathFile);
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		// 将内容写入到word文档中
		WordUtils wordUtils = new WordUtils();
		wordUtils.openDocument(fos);
		wordUtils.insertTitle(Title, 30, Font.BOLD, Element.ALIGN_CENTER);
		wordUtils.insertTitle(fileTime, 10, Font.NORMAL, Element.ALIGN_CENTER); // 往word文档中写入标题
		wordUtils.insertContext(startContext, 12, Font.NORMAL,
				Element.ALIGN_LEFT); // 往word文档中写入起点坐标
		wordUtils.insertContext(terminateContext, 12, Font.NORMAL,
				Element.ALIGN_LEFT); // 往word文档中写入终点坐标
		wordUtils.insertContext(barrierContext, 12, Font.NORMAL,
				Element.ALIGN_LEFT); // 往word文档中写入障碍物坐标
		wordUtils.insertContext(resultContext, 12, Font.NORMAL,
				Element.ALIGN_LEFT); // 往word文档中写入路径坐标
		wordUtils.insertContext(timeContext, 12, Font.NORMAL,
				Element.ALIGN_LEFT); // 往word文档中搜寻所花费时间

		wordUtils.closeDocument(); // 关闭文档

		// 生成文件完毕后清空集合类中的对象，防止对下一次的寻路造成影响
		tempPointList = null;
		rPointList.clear();

		// 通知文档已生成
		JOptionPane.showMessageDialog(null, "恭喜，文件已生成！", "通知",
				JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	// 鼠标在面板上点击时的一系列处理
	public void mouseClicked(MouseEvent e) {
		// 将像素点坐标转换成栅格矩阵坐标
		int clickX = e.getY() / 12;
		int clickY = e.getX() / 12;

		// 对超出矩阵范围的坐标的处理
		if (clickX > 49) {
			clickX = 49;
		}
		if (clickY > 49) {
			clickY = 49;
		}
		if (clickX < 0) {
			clickX = 0;
		}
		if (clickY < 0) {
			clickY = 0;
		}

		String mousePoint = "(" + clickX + "," + clickY + ")"; // 将面板上点击坐标转换成字符串方便写入文档
		setPositionString(mousePoint); // 将点坐标存入positionString方便后面标签显示处理
		Point p = new Point(clickX, clickY);

		// 对鼠标点击面板时设置的是起点坐标还是终点坐标或障碍物坐标进行判断
		switch (buttonCommand) {

		// "设置起点"按钮被点击时，设置的是起点坐标
		case 1:
			startPoint = p;
			setsPointString(mousePoint); // 设置起点坐标方便写入文档
			startLabel.setText(getPositionString()); // 设置面板显示的起点坐标
			this.removeMouseMotionListener(this); // 点击完关闭面板的鼠标监听
			break;

		// "设置终点"按钮被点击，设置的是终点坐标
		case 2:
			terminatePoint = p;
			settPointString(mousePoint); // 设置终点坐标方便写入文档
			terminLabel.setText(getPositionString()); // 设置面板显示的终点点坐标
			this.removeMouseMotionListener(this); // 点击完关闭面板的鼠标监听
			break;

		// 表示"设置障碍物"按钮被点击，设置的是障碍物坐标
		case 3:
			barrierPointSet.add(p); // 往集合中加入障碍物坐标
			bPointSet.add(mousePoint);
			break;
		}
		this.repaint(); // 进行重画
	}

	// 设置障碍物坐标时鼠标在面板上拖曳时的一系列处理
	public void mouseDragged(MouseEvent e) {
		Point p = null;
		if (buttonCommand == 3) {
			// 将像素点坐标转换成栅格矩阵坐标
			int dragX = e.getY() / 12;
			int dragY = e.getX() / 12;

			// 对超出矩阵范围的坐标的处理
			if (dragX > 49) {
				dragX = 49;
			}
			if (dragY > 49) {
				dragY = 49;
			}
			if (dragX < 0) {
				dragX = 0;
			}
			if (dragY < 0) {
				dragY = 0;
			}

			String mousePoint = "(" + dragX + "," + dragY + ")";
			bPointSet.add(mousePoint);
			p = new Point(dragX, dragY);

			// 使用Set，避免在鼠标拖拽时相同点的重复添加
			barrierPointSet.add(p);
			this.repaint();
		}

	}

	// 鼠标在面板上移动时的一系列处理，方便引导用户设置自己想设置的坐标
	public void mouseMoved(MouseEvent e) {
		// 将像素点坐标转换成栅格矩阵坐标
		int moveX = e.getY() / 12;
		int moveY = e.getX() / 12;

		// 对超出矩阵范围的坐标的处理
		if (moveX < 0) {
			moveX = 0;
		}
		if (moveX > 49) {
			moveX = 49;
		}
		if (moveY > 49) {
			moveY = 49;
		}
		if (moveY < 0) {
			moveY = 0;
		}
		setPositionString("(" + moveX + "," + moveY + ")");

		// 设置起点时面板标签显示内容格式化
		if (buttonCommand == 1) {
			formatLable(startLabel, getPositionString(), Color.GREEN, 30);
		}

		// 设置终点时面板标签显示内容格式化
		if (buttonCommand == 2) {
			formatLable(terminLabel, getPositionString(), Color.BLUE, 30);
		}
	}

	// 绘制代表起点、终点、障碍物和路径的小方格
	public void paint(Graphics g) {
		int x, y;
		g.setColor(new Color(204, 232, 207)); // 设置背景色
		g.fillRect(0, 0, 600, 600);
		g.setColor(Color.BLACK); // 设置栅格线颜色

		// 绘制栅格
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++) {
				g.drawRect(i * 12, j * 12, 12, 12);
			}
		}

		// 绘制起点，用绿色栅格表示
		if (startPoint != null) {
			x = (int) startPoint.getY();
			y = (int) startPoint.getX();
			g.setColor(Color.GREEN);
			g.fillRect(x * 12, y * 12, 12, 12);
		}

		// 绘制终点，用蓝色栅格表示
		if (terminatePoint != null) {
			x = (int) terminatePoint.getY();
			y = (int) terminatePoint.getX();
			g.setColor(Color.BLUE);
			g.fillRect(x * 12, y * 12, 12, 12);
		}

		// 绘制障碍物，用黑色栅格表示
		if (!barrierPointSet.isEmpty()) {
			for (Point p : barrierPointSet) {
				y = (int) p.getX();
				x = (int) p.getY();
				g.setColor(Color.BLACK);
				g.fillRect(x * 12, y * 12, 12, 12);
			}
		}

		// 绘制路径，用红色表示
		if (!pathPointList.isEmpty()) {

			for (Point p : pathPointList) {
				y = (int) p.getX();
				x = (int) p.getY();
				g.setColor(Color.RED);
				g.fillRect(x * 12, y * 12, 12, 12);
			}

		}

	}

	// 移除所有的鼠标监听器
	public void removeAllListeners() {
		int numOfMouseListener = getMouseListeners().length;
		int numOfMouseMotionListener = getMouseMotionListeners().length;
		for (int i = 0; i < numOfMouseListener; i++) {
			removeMouseListener(this);
		}
		for (int j = 0; j < numOfMouseMotionListener; j++) {
			removeMouseMotionListener(this);
		}

	}

	// 清除面板上的所有点
	public void removeAllPoint() {
		// 传入算法的参数的处理
		startPoint = null;
		terminatePoint = null;
		barrierPointSet.clear();
		pathPointList.clear();

		// 写入到文档内容的处理
		sPointString = null;
		tPointString = null;
		bPointSet.clear();
		rPointList.clear();

		// 算法计算的路径设为null
		tempPointList = null;

		// 面板上显示的坐标复原
		formatLable(startLabel, "(0,0)", Color.GREEN, 30);
		formatLable(terminLabel, "(0,0)", Color.BLUE, 30);
		formatLable(pathLabel, "(0,0)", Color.RED, 30);

		this.repaint();
	}

	// 用户未生成报告前多次点击“开始寻径”按钮的处理
	public void removetempPointList() {
		pathPointList.clear(); // pathPoint清空
		tempPointList = null;
		this.repaint(); // 重画
	}

	// 对算法获取的路径的处理
	public boolean resultPath() {
		if (tempPointList == null) {
			JOptionPane.showMessageDialog(null, "请等待寻找路径完毕后生成报告或你的报告已生成！",
					"出错了", JOptionPane.ERROR_MESSAGE);
			return false;
		} else {

			int size = tempPointList.size();
			if (size > 2) {

				Point checkPoint = tempPointList.get(size - 2); // 获取路径倒数第二个坐标
				String tempString = "(" + (int) checkPoint.getX() + ","
						+ (int) checkPoint.getY() + ")";

				// 判断当前线程显示坐标与算法计算的倒数第二个坐标是否相等来判断路径是否传入到rPointList防止线程冲突
				if (!getCurrentPathString().equals(tempString)) {
					JOptionPane.showMessageDialog(null,
							"请等待寻找路径完毕后生成报告或你的报告已生成！", "出错了",
							JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}

		// 将算法计算的路径传入到rPointList
		for (Point p : tempPointList) {
			int runY = (int) p.getX();
			int runX = (int) p.getY();
			String mousePath = "(" + runY + "," + runX + ")";
			rPointList.add(mousePath);
		}
		return true;
	}

	@Override
	// 从tempPointList向pathPointList每隔100ms添加一个坐标
	public void run() {
		if (tempPointList != null && !(tempPointList.isEmpty())) {

			for (Point p : tempPointList) {

				// 将路径坐标中的起点和终点去掉防止在面板上显示时，红色栅格对绿色栅格和蓝色栅格覆盖
				if (!(p.equals(startPoint)) && !(p.equals(terminatePoint))) {
					int runY = (int) p.getX();
					int runX = (int) p.getY();
					pathPointList.add(p);
					String mousePath = "(" + runY + "," + runX + ")";
					setCurrentPathString(mousePath);
					formatLable(pathLabel, getCurrentPathString(), Color.RED,
							30);
				}
				repaint();

				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	// 属性值的setter、getter方法
	public void setButtonCommand(int buttonCommand) {
		this.buttonCommand = buttonCommand;
	}

	public void setCurrentPathString(String currentPathString) {
		this.currentPathString = currentPathString;
	}

	public void setPositionString(String positionString) {
		this.positionString = positionString;
	}

	public void setrPointList(List<String> rPointList) {
		this.rPointList = rPointList;
	}

	public void setsPointString(String sPointString) {
		this.sPointString = sPointString;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void settPointString(String tPointString) {
		this.tPointString = tPointString;
	}

	public int getButtonCommand() {
		return buttonCommand;
	}

	public String getCurrentPathString() {
		return currentPathString;
	}

	public String getPositionString() {
		return positionString;
	}

	public List<String> getrPointList() {
		return rPointList;
	}

	public String getsPointString() {
		return sPointString;
	}

	public long getTime() {
		return time;
	}

	public String gettPointString() {
		return tPointString;
	}
}
