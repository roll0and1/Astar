package com.ccnu.astar;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

class WordUtils {
	private Document document; // 文档对象
	private BaseFont bfChinese; // 中文字体

	// 构造方法
	public WordUtils() {
		this.document = new Document(PageSize.A4);// 设置纸张大小

	}

	// 打开文档
	public void openDocument(FileOutputStream fos) {
		// 建立一个书写器，使用它将文档写入到磁盘中
		RtfWriter2.getInstance(this.document, fos);
		this.document.open();
		// 设置中文字体
		try {
			this.bfChinese = BaseFont.createFont("STSongStd-Light",
					"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertTitle(String titleStr, int fontsize, int fontStyle,
			int elementAlign) {
		Font titleFont = new Font(this.bfChinese, fontsize, fontStyle);

		Paragraph title = new Paragraph(titleStr);

		// 设置标题格式对齐方式
		title.setAlignment(elementAlign);
		title.setFont(titleFont);

		try {
			this.document.add(title);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 将内容以一定文字格式写入到文档中
	public void insertContext(String contextStr, int fontsize, int fontStyle,
			int elementAlign) {

		Font contextFont = new Font(bfChinese, fontsize, fontStyle);// 正文字体风格
		Paragraph context = new Paragraph(contextStr);

		context.setLeading(3f);// 设置行距

		context.setAlignment(elementAlign); // 正文格式左对齐
		context.setFont(contextFont);

		context.setSpacingBefore(2); // 离上一段落（标题）空的行数

		context.setFirstLineIndent(20); // 设置第一行空的列数
		try {
			document.add(context);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 关闭文档
	public void closeDocument() {
		this.document.close();
	}

	// 属性值的getter、setter方法
	public BaseFont getBfChinese() {
		return bfChinese;
	}

	public void setBfChinese(BaseFont bfChinese) {
		this.bfChinese = bfChinese;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
