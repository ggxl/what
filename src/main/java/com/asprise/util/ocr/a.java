package com.asprise.util.ocr;

import java.awt.image.RenderedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

abstract class a {
	protected OutputStream output;

	public a(OutputStream outputstream) {
		this.output = outputstream;
	}

	public OutputStream getOutputStream() {
		return this.output;
	}

	public abstract void encode(RenderedImage paramRenderedImage)
			throws IOException;

	static boolean a(RenderedImage image, String path) {
		try {
			OutputStream out = new FileOutputStream(path);
			a encoder = new b(out);
			encoder.encode(image);
			out.close();
			return true;
		} catch (IOException e) {
		}
		return false;
	}
}
