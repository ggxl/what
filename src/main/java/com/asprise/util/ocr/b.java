package com.asprise.util.ocr;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;

import sun.security.action.GetPropertyAction;

@SuppressWarnings({ "unused", "restriction" })
class b extends a {

	private static final int PBM_ASCII = 49;
	private static final int PGM_ASCII = 50;
	private static final int PPM_ASCII = 51;
	private static final int PBM_RAW = 52;
	private static final int PGM_RAW = 53;
	private static final int PPM_RAW = 54;
	private static final int SPACE = 32;
	private static final String COMMENT = "#ASPRISE OCR";
	private byte[] lineSeparator;
	private int variant;
	private int maxValue;

	public b(OutputStream outputstream) {
		super(outputstream);
	}

	private static BufferedImage forceBanding(BufferedImage image) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		BufferedImage bufferedImage = new BufferedImage(imageWidth,
				imageHeight, 1);

		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawRenderedImage(image, new AffineTransform());

		return bufferedImage;
	}

	public void encode(RenderedImage renderedimage) throws IOException {
		int i = renderedimage.getMinX();
		int j = renderedimage.getMinY();
		int k = renderedimage.getWidth();
		int l = renderedimage.getHeight();
		int i1 = renderedimage.getTileHeight();
		SampleModel samplemodel = renderedimage.getSampleModel();
		ColorModel colormodel = renderedimage.getColorModel();
		String s = (String) AccessController
				.doPrivileged(new GetPropertyAction("line.separator"));
		this.lineSeparator = s.getBytes();
		int j1 = samplemodel.getTransferType();
		if ((j1 == 4) || (j1 == 5)) {
			encode(forceBanding((BufferedImage) renderedimage));
			return;
		}
		int[] ai = samplemodel.getSampleSize();
		int k1 = samplemodel.getNumBands();
		byte[] abyte0 = (byte[]) null;
		byte[] abyte1 = (byte[]) null;
		byte[] abyte2 = (byte[]) null;
		boolean flag = false;
		if (k1 == 1) {
			if ((colormodel instanceof IndexColorModel)) {
				IndexColorModel indexcolormodel = (IndexColorModel) colormodel;
				int l1 = indexcolormodel.getMapSize();
				if (l1 < 1 << ai[0]) {
					encode(forceBanding((BufferedImage) renderedimage));
					return;
				}
				if (ai[0] == 1) {
					this.variant = 52;
					flag = indexcolormodel.getRed(1)
							+ indexcolormodel.getGreen(1)
							+ indexcolormodel.getBlue(1) > indexcolormodel
							.getRed(0)
							+ indexcolormodel.getGreen(0)
							+ indexcolormodel.getBlue(0);
				} else {
					this.variant = 54;
					abyte0 = new byte[l1];
					abyte1 = new byte[l1];
					abyte2 = new byte[l1];
					indexcolormodel.getReds(abyte0);
					indexcolormodel.getGreens(abyte1);
					indexcolormodel.getBlues(abyte2);
				}
			} else if (ai[0] == 1) {
				this.variant = 52;
			} else if (ai[0] <= 8) {
				this.variant = 53;
			} else {
				this.variant = 50;
			}
		} else if (k1 == 3) {
			if ((ai[0] <= 8) && (ai[1] <= 8) && (ai[2] <= 8)) {
				this.variant = 54;
			} else {
				this.variant = 51;
			}
		} else {
			encode(forceBanding((BufferedImage) renderedimage));
			return;
		}
		if (!isRaw(this.variant)) {
			boolean flag1 = true;
			for (int i2 = 0; i2 < ai.length; i2++) {
				if (ai[i2] > 8) {
					flag1 = false;
					break;
				}
			}
			if (flag1) {
				this.variant += 3;
			}
		}
		this.maxValue = ((1 << ai[0]) - 1);
		this.output.write(80);
		this.output.write(this.variant);
		this.output.write(this.lineSeparator);
		this.output.write("# ASPRISE OCR".getBytes());
		this.output.write(this.lineSeparator);
		writeInteger(this.output, k);
		this.output.write(32);
		writeInteger(this.output, l);
		if ((this.variant != 52) && (this.variant != 49)) {
			this.output.write(this.lineSeparator);
			writeInteger(this.output, this.maxValue);
		}
		if ((this.variant == 52) || (this.variant == 53)
				|| (this.variant == 54)) {
			this.output.write(10);
		}
		boolean flag2 = false;
		if ((this.variant == 52) && (samplemodel.getTransferType() == 0)
				&& ((samplemodel instanceof MultiPixelPackedSampleModel))) {
			MultiPixelPackedSampleModel multipixelpackedsamplemodel = (MultiPixelPackedSampleModel) samplemodel;
			if ((multipixelpackedsamplemodel.getDataBitOffset() == 0)
					&& (multipixelpackedsamplemodel.getPixelBitStride() == 1)) {
				flag2 = true;
			}
		} else if (((this.variant == 53) || (this.variant == 54))
				&& ((samplemodel instanceof ComponentSampleModel))
				&& (!(colormodel instanceof IndexColorModel))) {
			ComponentSampleModel componentsamplemodel = (ComponentSampleModel) samplemodel;
			if (componentsamplemodel.getPixelStride() == k1) {
				flag2 = true;
				if (this.variant == 54) {
					int[] ai2 = componentsamplemodel.getBandOffsets();
					for (int l2 = 0; l2 < k1; l2++) {
						if (ai2[l2] != l2) {
							flag2 = false;
							break;
						}
					}
				}
			}
		}
		if (flag2) {
			int j2 = this.variant != 52 ? k * samplemodel.getNumBands()
					: (k + 7) / 8;
			int k2 = renderedimage.getNumYTiles();
			Rectangle rectangle = new Rectangle(renderedimage.getMinX(),
					renderedimage.getMinY(), renderedimage.getWidth(),
					renderedimage.getHeight());
			Rectangle rectangle1 = new Rectangle(renderedimage.getMinX(),
					renderedimage.getMinTileY() * renderedimage.getTileHeight()
							+ renderedimage.getTileGridYOffset(),
					renderedimage.getWidth(), renderedimage.getTileHeight());
			byte[] abyte4 = (byte[]) null;
			if (flag) {
				abyte4 = new byte[j2];
			}
			for (int l3 = 0; l3 < k2; l3++) {
				if (l3 == k2 - 1) {
					rectangle1.height = (renderedimage.getHeight() - rectangle1.y);
				}
				Rectangle rectangle2 = rectangle1.intersection(rectangle);
				Raster raster = renderedimage.getData(rectangle2);
				byte[] abyte5 = ((DataBufferByte) raster.getDataBuffer())
						.getData();
				int i5 = this.variant != 52 ? ((ComponentSampleModel) raster
						.getSampleModel()).getScanlineStride()
						: ((MultiPixelPackedSampleModel) raster
								.getSampleModel()).getScanlineStride();
				if ((i5 == j2) && (!flag)) {
					this.output.write(abyte5, 0, abyte5.length);
				} else {
					int i6 = 0;
					for (int k6 = 0; k6 < rectangle2.height; k6++) {
						if (flag) {
							for (int i7 = 0; i7 < j2; i7++) {
								abyte4[i7] = ((byte) (abyte5[(i6 + i7)] & 0xFF ^ 0xFFFFFFFF));
							}
							this.output.write(abyte4, 0, j2);
						} else {
							this.output.write(abyte5, i6, j2);
						}
						i6 += i5;
					}
				}
				rectangle1.y += i1;
			}
			this.output.flush();
			return;
		}
		int[] ai1 = new int[8 * k * k1];
		byte[] abyte3 = abyte0 != null ? new byte[8 * k * 3] : new byte[8 * k
				* k1];
		int i3 = 0;
		int j3 = j + l;
		for (int k3 = j; k3 < j3; k3 += 8) {
			int i4 = Math.min(8, j3 - k3);
			int j4 = i4 * k * k1;
			Raster raster1 = renderedimage.getData(new Rectangle(i, k3, k, i4));
			raster1.getPixels(i, k3, k, i4, ai1);
			if (flag) {
				for (int k4 = 0; k4 < j4; k4++) {
					ai1[k4] ^= 0x1;
				}
			}
			switch (this.variant) {
			default:
				break;
			case 49:
			case 50:
				for (int l4 = 0; l4 < j4; l4++) {
					if (i3++ % 16 == 0) {
						this.output.write(this.lineSeparator);
					} else {
						this.output.write(32);
					}
					writeInteger(this.output, ai1[l4]);
				}
				this.output.write(this.lineSeparator);
				break;
			case 51:
				if (abyte0 == null) {
					for (int j5 = 0; j5 < j4; j5++) {
						if (i3++ % 16 == 0) {
							this.output.write(this.lineSeparator);
						} else {
							this.output.write(32);
						}
						writeInteger(this.output, ai1[j5]);
					}
				} else {
					for (int k5 = 0; k5 < j4; k5++) {
						if (i3++ % 16 == 0) {
							this.output.write(this.lineSeparator);
						} else {
							this.output.write(32);
						}
						writeInteger(this.output, abyte0[ai1[k5]] & 0xFF);
						this.output.write(32);
						writeInteger(this.output, abyte1[ai1[k5]] & 0xFF);
						this.output.write(32);
						writeInteger(this.output, abyte2[ai1[k5]] & 0xFF);
					}
				}
				this.output.write(this.lineSeparator);
				break;
			case 52:
				int l5 = 0;
				int j6 = 0;
				for (int l6 = 0; l6 < j4 / 8; l6++) {
					int j7 = ai1[(j6++)] << 7 | ai1[(j6++)] << 6
							| ai1[(j6++)] << 5 | ai1[(j6++)] << 4
							| ai1[(j6++)] << 3 | ai1[(j6++)] << 2
							| ai1[(j6++)] << 1 | ai1[(j6++)];
					abyte3[(l5++)] = ((byte) j7);
				}
				if (j4 % 8 > 0) {
					int k7 = 0;
					for (int i8 = 0; i8 < j4 % 8; i8++) {
						k7 |= ai1[(j4 + i8)] << 7 - i8;
					}
					abyte3[(l5++)] = ((byte) k7);
				}
				this.output.write(abyte3, 0, (j4 + 7) / 8);
				break;
			case 53:
				for (int l7 = 0; l7 < j4; l7++) {
					abyte3[l7] = ((byte) ai1[l7]);
				}
				this.output.write(abyte3, 0, j4);
				break;
			case 54:
				if (abyte0 == null) {
					for (int j8 = 0; j8 < j4; j8++) {
						abyte3[j8] = ((byte) (ai1[j8] & 0xFF));
					}
				} else {
					int k8 = 0;
					int l8 = 0;
					for (; k8 < j4; k8++) {
						abyte3[(l8++)] = abyte0[ai1[k8]];
						abyte3[(l8++)] = abyte1[ai1[k8]];
						abyte3[(l8++)] = abyte2[ai1[k8]];
					}
				}
				this.output.write(abyte3, 0, abyte3.length);
			}
		}
		this.output.flush();
	}

	private void writeInteger(OutputStream outputstream, int i)
			throws IOException {
		outputstream.write(Integer.toString(i).getBytes());
	}

	private void writeByte(OutputStream outputstream, byte byte0)
			throws IOException {
		outputstream.write(Byte.toString(byte0).getBytes());
	}

	private boolean isRaw(int i) {
		return i >= 52;
	}

	public static void main(String[] args) {
	}
}
