package com.ccnu.astar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

class ButtonsPanel extends JPanel implements ActionListener {

	// 响应按钮被点击时设置标志位
	final int ButtonConmand_STRAT = 1; // “设置起点”按钮的标志位
	final int ButtonConmand_TERMINATE = 2; // “设置终点”按钮的标志位
	final int ButtonConmand_BARRIER = 3; // “设置障碍物”按钮的标志位
	private MyCanvas canvas;

	// 构造函数
	public ButtonsPanel(MyCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// “设置起点”按钮被点击
		if ("设置起点".equals(e.getActionCommand())) {
			canvas.setButtonCommand(ButtonConmand_STRAT); // 设置标志位

			// 清除canvas面板上所有鼠标监听器防止干扰，然后再加入鼠标点击监听和鼠标移动监听
			canvas.removeAllListeners();
			canvas.addMouseListener(canvas);
			canvas.addMouseMotionListener(canvas);
		}
		// “设置终点”按钮被点击
		else if ("设置终点".equals(e.getActionCommand())) {
			canvas.setButtonCommand(ButtonConmand_TERMINATE); // 设置标志位

			// 清除canvas面板上所有鼠标监听器防止干扰，然后再加入鼠标点击监听和鼠标移动监听
			canvas.removeAllListeners();
			canvas.addMouseListener(canvas);
			canvas.addMouseMotionListener(canvas);
		}
		// “设置障碍”按钮被点击
		else if ("设置障碍".equals(e.getActionCommand())) {
			canvas.setButtonCommand(ButtonConmand_BARRIER); // 设置标志位

			// 清除canvas面板上所有鼠标监听器防止干扰，然后再加入鼠标点击监听和鼠标移动监听
			canvas.removeAllListeners();
			canvas.addMouseListener(canvas);
			canvas.addMouseMotionListener(canvas);
		}
		// “清理面板”按钮被点击
		else if ("清空面板".equals(e.getActionCommand())) {
			canvas.removeAllListeners(); // 清除canvas面板上所有鼠标监听器防止干扰
			canvas.removeAllPoint(); // 移除所有点让面板复原

		}
		// “生成路径报告”按钮被点击
		else if ("生成路径报告".equals(e.getActionCommand())) {
			canvas.removeAllListeners(); // 清除canvas面板上所有鼠标监听器防止干扰
			canvas.generateReport(); // 生成报告
		}
		// “搜寻路径”按钮被点击
		else {
			canvas.removeAllListeners(); // 清除canvas面板上所有鼠标监听器防止干扰
			canvas.generatePath(); // 生成路径
		}
	}

	// 属性值getter、setter方法
	public MyCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(MyCanvas canvas) {
		this.canvas = canvas;
	}
}
