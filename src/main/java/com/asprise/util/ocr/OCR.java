package com.asprise.util.ocr;

import java.awt.image.RenderedImage;
import java.util.Vector;

public class OCR {
	private static final int MODE_CHARACTERS = 1;
	private static final int MODE_BARCODES = 2;
	private static final int MODE_ALL = 3;
	private static final String TAG_UNKNOWN = "#&*!@";
	private static final String TAG_BARCODE_START = "[BARCODESTART";
	private static final String TAG_BARCODE_END = "[BARCODEEND";
	private static final String TAG_PICTURE = "[PIC]";

	public OCR() {
		loadLibrary();
	}

	public String recognizeEverything(RenderedImage image) {
		return (String) processString(image, 3, "[", "]");
	}

	public String recognizeEverything(RenderedImage image,
			String barcodePrefix, String barcodeSuffix) {
		return (String) processString(image, 3, barcodePrefix, barcodeSuffix);
	}

	public String recognizeCharacters(RenderedImage image) {
		return (String) processString(image, 1, null, null);
	}

	@SuppressWarnings("unchecked")
	public String recognizeBarcode(RenderedImage image) {
		Vector<String> v = (Vector<String>) processString(image, 2, null, null);
		if ((v == null) || (v.size() == 0)) {
			return null;
		}
		return v.elementAt(0);
	}

	@SuppressWarnings("unchecked")
	public Vector<String> recognizeBarcodes(RenderedImage image) {
		return (Vector<String>) processString(image, 2, null, null);
	}

	private Object processString(RenderedImage image, int mode, String prefix,
			String suffix) {
		if ((image == null) || (image.getHeight() <= 0)
				|| (image.getWidth() <= 0)) {
			return null;
		}
		String s = performOCR(image, mode);
		if (s == null) {
			return s;
		}
		StringBuffer buf = new StringBuffer();

		int cursor = 0;
		while (cursor < s.length()) {
			int pos = s.indexOf("#&*!@", cursor);
			if (pos < 0) {
				buf.append(s.substring(cursor));
				break;
			}
			buf.append(s.substring(cursor, pos));
			buf.append(' ');
			cursor = pos + "#&*!@".length();
		}
		s = buf.toString();

		cursor = 0;
		int posStart = -1;
		int posEnd = -1;

		StringBuffer sb = new StringBuffer();
		String barcode = null;
		Vector<String> barcodes = new Vector<String>();
		while (cursor < s.length()) {
			posStart = s.indexOf("[BARCODESTART", cursor);
			if ((posStart == -1) && (cursor == 0)) {
				switch (mode) {
				case 1:
				case 3:
					return s;
				case 2:
					return barcodes;
				}
			}
			if (posStart == -1) {
				switch (mode) {
				case 1:
				case 3:
					sb.append(s.substring(cursor));
					return sb.toString();
				case 2:
					return barcodes;
				}
			}
			sb.append(s.substring(cursor, posStart));
			cursor = posStart + "[BARCODESTART".length();

			posEnd = s.indexOf("[BARCODEEND", cursor);
			if (posEnd == -1) {
				throw new RuntimeException(
						"Please contact Asprise support: support@asprise.com. Errro code: NO CLOSING BARCODE END TAG!");
			}
			barcode = s.substring(posStart + "[BARCODESTART".length(), posEnd);
			switch (mode) {
			case 3:
				sb.append(prefix + barcode + suffix);
				break;
			case 2:
				barcodes.add(barcode);
			}
			cursor = posEnd + "[BARCODEEND".length();
		}
		throw new RuntimeException(
				"Please contact Asprise support: support@asprise.com. SHOULD NOT REACH HERE!");
	}

	private static boolean libraryLoaded = false;
	private static String libPath = null;

	private native String performOCR(RenderedImage paramRenderedImage,
			int paramInt);

	private static void loadLibrary() {
		if (libraryLoaded) {
			return;
		}
		if (libPath != null) {
			try {
				System.load(libPath);
				libraryLoaded = true;
				return;
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		System.loadLibrary("AspriseOCR");
		libraryLoaded = true;
	}

	public static void setLibraryPath(String libraryPath) {
		libPath = libraryPath;
	}

	public static String getLibraryPath() {
		return libPath;
	}
}
