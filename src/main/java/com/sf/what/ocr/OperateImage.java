package com.sf.what.ocr;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class OperateImage {
	private String srcpath;
	private String subpath;
	private int x;
	private int y;
	private int width;
	private int height;

	public void setSrcpath(String srcpath) {
		this.srcpath = srcpath;
	}

	public void setSubpath(String subpath) {
		this.subpath = subpath;
	}

	public OperateImage() {
	}

	public OperateImage(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void cut() throws IOException {
		FileInputStream is = null;
		ImageInputStream iis = null;
		try {
			is = new FileInputStream(this.srcpath);

			Iterator<ImageReader> it = ImageIO
					.getImageReadersByFormatName("jpg");

			ImageReader reader = (ImageReader) it.next();

			iis = ImageIO.createImageInputStream(is);

			reader.setInput(iis, true);

			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(this.x, this.y, this.width,
					this.height);

			param.setSourceRegion(rect);
			
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "png", new File(this.subpath));
		} finally {
			if (is != null) {
				is.close();
			}
			if (iis != null) {
				iis.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String name = "E:\\imgcode.jpg";
		OperateImage o = new OperateImage(2, 2, 92, 26);
		o.setSrcpath(name);
		o.setSubpath("E:\\imgcode22.jpg");
		o.cut();
	}
}
