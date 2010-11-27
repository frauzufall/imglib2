package mpicbg.imglib.scripting.rgb;

import mpicbg.imglib.image.Image;
import mpicbg.imglib.scripting.rgb.fn.HSBOp;
import mpicbg.imglib.type.numeric.RGBALegacyType;

/** Extracts the HSB saturation of an RGB pixel. */
public class Hue extends HSBOp {

	public Hue(final Image<? extends RGBALegacyType> img) {
		super(img);
	}

	@Override
	protected final int getIndex() { return 0; }
}