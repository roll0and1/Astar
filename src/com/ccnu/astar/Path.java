package com.ccnu.astar;

import java.awt.Point;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

public class Path {
	private int flag;
	Point startPoint;
	Point terminatePoint;
	Set<Point> barrierPointSet;

	// 构造方法
	public Path(Point startPoint, Point terminatePoint,
			Set<Point> barrierPointSet) {
		this.startPoint = startPoint;
		this.barrierPointSet = barrierPointSet;
		this.terminatePoint = terminatePoint;

	}

	public List<Point> search() {
		long startTime = System.currentTimeMillis(); // 开始时间
		List<Point> pathPointList = null;
		int[][] map = new int[50][50]; // 栅格矩阵

		// 生成栅格矩阵
		for (int i = 0; i < 50; i++)
			for (int j = 0; j < 50; j++) {
				map[i][j] = 1; // “1”表示可以通过
			}
		for (Point p : barrierPointSet) {
			map[(int) p.getX()][(int) p.getY()] = 0; // “0”表示是障碍物，不能通过
		}
		AStar aStar = new AStar(map, 50, 50);

		// 起点，终点，障碍物设置判断
		if (startPoint == null && terminatePoint != null) {
			setFlag(2);
		} else if (terminatePoint == null && startPoint != null) {
			setFlag(3);
		} else if (startPoint == null && terminatePoint == null) {
			setFlag(4);

		} else {
			int num = aStar.searchFlag((int) startPoint.getX(),
					(int) startPoint.getY(), (int) terminatePoint.getX(),
					(int) terminatePoint.getY());
			setFlag(num);

		}
		// 结果判断，弹出对话框处理
		if (getFlag() == -1) {
			JOptionPane.showMessageDialog(null, "障碍物、起点或终点设置有误，请重新设置！", "警告",
					JOptionPane.WARNING_MESSAGE);
		} else if (getFlag() == 0) {
			JOptionPane.showMessageDialog(null, "sorry,无法找到合适路径！", "出错了",
					JOptionPane.ERROR_MESSAGE);
		} else if (getFlag() == 2) {
			JOptionPane.showMessageDialog(null, "请设置起点！", "警告",
					JOptionPane.WARNING_MESSAGE);
		} else if (getFlag() == 3) {
			JOptionPane.showMessageDialog(null, "请设置终点", "警告",
					JOptionPane.WARNING_MESSAGE);
		} else if (getFlag() == 4) {
			JOptionPane.showMessageDialog(null, "请设置起点和终点！", "警告",
					JOptionPane.WARNING_MESSAGE);
			return null;
		} else {
			pathPointList = aStar.getSearchPath();
		}
		long endTime = System.currentTimeMillis(); // 结束时间
		setSerachTime(endTime - startTime);
		return pathPointList;

	}

	// 属性值的setter、getter方法
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	private long serachTime;

	public long getSerachTime() {
		return serachTime;
	}

	public void setSerachTime(long serachTime) {
		this.serachTime = serachTime;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getTerminatePoint() {
		return terminatePoint;
	}

	public void setTerminatePoint(Point terminatePoint) {
		this.terminatePoint = terminatePoint;
	}

	public Set<Point> getBarrierPointSet() {
		return barrierPointSet;
	}

	public void setBarrierPointSet(Set<Point> barrierPointSet) {
		this.barrierPointSet = barrierPointSet;
	}
}
