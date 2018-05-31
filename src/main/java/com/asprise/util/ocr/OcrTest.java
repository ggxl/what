package com.asprise.util.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sf.what.ocr.ImgFilter;
import com.sf.what.ocr.OperateImage;


public class OcrTest {
	public static void main(String[] args) throws IOException {
		/*
		 * Ocr.setUp(); //一次设置 Ocr ocr = new Ocr();
		 * ocr.startEngine("eng",Ocr.SPEED_FASTEST); String s = ocr.recognize(
		 * new File [] {new File("E:\\imgcode.jpg")}, Ocr.RECOGNIZE_TYPE_ALL,
		 * Ocr.OUTPUT_FORMAT_PLAINTEXT); System.out.println("resutl:"+s);
		 * ocr.stopEngine();
		 */
		test();
	}

	public static void test() throws IOException {
		// 剪切图片边框
		String imgName = "E:\\imgcode2.jpg";
		OperateImage o = new OperateImage(2, 2, 92, 26);
		o.setSrcpath(imgName);
		o.setSubpath(imgName);
		o.cut();
		// 处理图片 降噪
		FileInputStream fin = new FileInputStream(imgName);
		BufferedImage bi = ImageIO.read(fin);
		ImgFilter flt = new ImgFilter(bi);
		flt.changeGrey();
		flt.getGrey();
		flt.getBrighten();
		bi = flt.getProcessedImg();
		File file = new File(imgName);
		ImageIO.write(bi, "png", file);

		String verifyCode = "";

		/*file = new File("E:\\imgcode.jpg");*/
		BufferedImage image = ImageIO.read(file);
		String s = new OCR().recognizeCharacters(image);
		verifyCode = s.replaceAll("[^a-zA-Z0-9]", "");
		
		System.out.println("verifyCode:" + verifyCode);
	}
}
