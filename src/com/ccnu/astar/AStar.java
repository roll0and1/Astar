package com.ccnu.astar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//A*算法
public class AStar {
	private static final int COST_STRAIGHT = 10; // 垂直方向或水平方向移动的路径代价
	private static final int COST_DIAGONAL = 14; // 斜方向移动的路径代价
	private int[][] map; // 二维数组（矩阵）表示栅格地图
	private List<Node> openList; // open表
	private List<Node> closeList; // close表
	private int row; // 行
	private int column;// 列
	private List<Point> searchPath = new ArrayList<Point>();// 存放search过程中按顺序找到的路径上的点

	// 构造函数
	public AStar(int[][] map, int row, int column) {
		this.map = map;
		this.row = row;
		this.column = column;
		openList = new ArrayList<Node>();
		closeList = new ArrayList<Node>();
	}

	// 查询此路是否能走通
	private boolean checkPath(int x, int y, Node parentNode, Node eNode,
			int cost) {
		Node node = new Node(x, y, parentNode);

		// 查找地图中是否能通过
		if (map[x][y] == 0) { // 障碍物
			closeList.add(node); // 不能通过的结点加入到closeList中
			return false;
		}

		// 查找坐标为（x，y）的节点是否在关闭列表中
		if (isListContains(closeList, x, y) != -1) {
			return false;
		}

		// 查找坐标为（x，y）的节点是否在openList中
		int index = -1; // 初始设置此结点不在openList中
		if ((index = isListContains(openList, x, y)) != -1) {

			// 若坐标为(x,y)的节点在在openList中，比较G值判断现在的G值是否更小来决定是否更新G，F值
			if ((parentNode.getG() + cost) < openList.get(index).getG()) {
				node.setParentNode(parentNode);
				countG(node, eNode, cost); // 修正G值
				countF(node);
				openList.set(index, node);
			}
		} else {
			// 既不在closeList中也不在openList中则加入到openList中
			node.setParentNode(parentNode); // 设置它的父节点
			count(node, eNode, cost); // 计算 H,F,G值
			openList.add(node); // 添加到openList中
		}
		return true;
	}

	// 计算G,H,F值
	private void count(Node sNode, Node eNode, int cost) {
		countG(sNode, eNode, cost);
		countH(sNode, eNode);
		countF(eNode);
	}

	// 计算F值
	private void countF(Node node) {
		node.setF(node.getG() + node.getH());
	}

	// 计算G值
	private void countG(Node sNode, Node eNode, int cost) {
		if (sNode.getParentNode() == null) {
			sNode.setG(cost);
		} else {
			sNode.setG(sNode.getParentNode().getG() + cost);
		}
	}

	// 计算H值
	private void countH(Node sNode, Node eNode) {
		sNode.setH(Math.abs(sNode.getX() - eNode.getX())
				+ Math.abs(sNode.getY() - eNode.getY())); // 用曼哈顿距离计算
	}

	// 获取的正序的路径
	private void getPath(List<Node> resultList, Node node) {

		// 递归
		if (node.getParentNode() != null) {
			getPath(resultList, node.getParentNode());
		}
		resultList.add(node); // 将从起点到终点正序节点添加到路径列表中
	}

	// 列表中是否包含坐标为(x,y)的节点，若包含返回节点位置否则返回-1
	private int isListContains(List<Node> list, int x, int y) {
		// 遍历查找
		for (int i = 0; i < list.size(); i++) {
			Node node = list.get(i);
			if (node.getX() == x && node.getY() == y) {
				return i;
			}
		}
		return -1;
	}

	public int searchFlag(int x1, int y1, int x2, int y2) {

		// 判断节点坐标是否超出数组范围
		if (x1 < 0 || x1 >= row || x2 < 0 || x2 >= row || y1 < 0
				|| y1 >= column || y2 < 0 || y2 >= column) {

			return -1;
		}

		// 判断起始点是否障碍物节点
		if (map[x1][y1] == 0 || map[x2][y2] == 0) {
			return -1;
		}

		Node sNode = new Node(x1, y1, null); // 起点
		Node eNode = new Node(x2, y2, null); // 终点

		openList.add(sNode); // 将起点加入到openList中
		List<Node> resultList = this.search(sNode, eNode);

		// 路径长度为零则查找失败返回0
		if (resultList.size() == 0) {
			return 0;
		}

		// 若路径长度不为空将路径传给searchPath
		for (Node node : resultList) {
			this.getSearchPath().add(new Point(node.getX(), node.getY()));
		}
		return 1;

	}

	// 查找路径
	private List<Node> search(Node sNode, Node eNode) {
		List<Node> resultList = new ArrayList<Node>();
		boolean isFind = false;
		Node node = null;
		while (openList.size() > 0) {

			// 取出开启列表中最低F值(初始时起点的F值最小，之后由于排序依旧是第一个节点的F值最小)
			node = openList.get(0);

			// 判断取出的节点是否为终点
			if (node.getX() == eNode.getX() && node.getY() == eNode.getY()) {
				isFind = true;
				break;
			}

			// 扩展F值最小节点的子节点
			// 正北方向的子节点
			if ((node.getY() - 1) >= 0) {
				checkPath(node.getX(), node.getY() - 1, node, eNode,
						COST_STRAIGHT);
			}
			// 正南方向的子节点
			if ((node.getY() + 1) < column) {
				checkPath(node.getX(), node.getY() + 1, node, eNode,
						COST_STRAIGHT);
			}
			// 正西方向的子节点
			if ((node.getX() - 1) >= 0) {
				checkPath(node.getX() - 1, node.getY(), node, eNode,
						COST_STRAIGHT);
			}
			// 正东方向的子节点
			if ((node.getX() + 1) < row) {
				checkPath(node.getX() + 1, node.getY(), node, eNode,
						COST_STRAIGHT);
			}
			// 西北方向的子节点
			if ((node.getX() - 1) >= 0 && (node.getY() - 1) >= 0) {
				checkPath(node.getX() - 1, node.getY() - 1, node, eNode,
						COST_DIAGONAL);
			}
			// 西南方向的子节点
			if ((node.getX() - 1) >= 0 && (node.getY() + 1) < column) {
				checkPath(node.getX() - 1, node.getY() + 1, node, eNode,
						COST_DIAGONAL);
			}
			// 东北方向的子节点
			if ((node.getX() + 1) < row && (node.getY() - 1) >= 0) {
				checkPath(node.getX() + 1, node.getY() - 1, node, eNode,
						COST_DIAGONAL);
			}
			// 东南方向的子节点
			if ((node.getX() + 1) < row && (node.getY() + 1) < column) {
				checkPath(node.getX() + 1, node.getY() + 1, node, eNode,
						COST_DIAGONAL);
			}

			// 从开启列表中删除，添加到关闭列表中
			closeList.add(openList.remove(0));

			// 对openListyi进行排序
			Collections.sort(openList, new NodeFComparator());
		}
		if (isFind) {
			getPath(resultList, node);
		}
		return resultList;
	}

	public int getRow() {
		return row;
	}

	public List<Point> getSearchPath() {
		return searchPath;
	}

	public List<Node> getCloseList() {
		return closeList;
	}

	public int getColumn() {
		return column;
	}

	public int[][] getMap() {
		return map;
	}

	public List<Node> getOpenList() {
		return openList;
	}

	public void setCloseList(List<Node> closeList) {
		this.closeList = closeList;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public void setOpenList(List<Node> openList) {
		this.openList = openList;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setSearchPath(List<Point> searchPath) {
		this.searchPath = searchPath;
	}

}

// 节点类
class Node {
	private int x; // X坐标
	private int y; // Y坐标
	private Node parentNode; // 父节点
	private int g; // 当前点到起点的代价
	private int h; // 当前点到终点的代价
	private int f; // f=g+h

	// 构造函数
	public Node(int x, int y, Node parentNode) {
		this.x = x;
		this.y = y;
		this.parentNode = parentNode;
	}

	public int getF() {
		return f;
	}

	public int getG() {
		return g;
	}

	public int getH() {
		return h;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setF(int f) {
		this.f = f;
	}

	public void setG(int g) {
		this.g = g;
	}

	public void setH(int h) {
		this.h = h;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}

// 节点比较器类
class NodeFComparator implements Comparator<Node> {

	@Override
	public int compare(Node o1, Node o2) {
		return o1.getF() - o2.getF();
	}
}
