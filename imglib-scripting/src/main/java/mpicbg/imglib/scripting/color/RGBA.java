package mpicbg.imglib.scripting.color;

import java.util.Collection;

import mpicbg.imglib.cursor.Cursor;
import mpicbg.imglib.image.Image;
import mpicbg.imglib.scripting.color.fn.ColorFunction;
import mpicbg.imglib.scripting.math.fn.IFunction;
import mpicbg.imglib.type.numeric.RGBALegacyType;
import mpicbg.imglib.type.numeric.RealType;

/** Given up to 4 channels--each represented by an {@link IFunction},
 *  this class composes them into an {@link RGBALegacyType} {@link Image}.
 *  
 *  Channel order: A=4, R=3, G=2, B=1.
 *  
 *  Expects each channel in floats or doubles, in the range [0, 255].
 *  */
public final class RGBA extends ColorFunction {

	private final IFunction red, green, blue, alpha;

	public RGBA(final IFunction red, final IFunction green, final IFunction blue, final IFunction alpha) {
		this.red = null == red ? empty : red;
		this.green = null == green ? empty : green;
		this.blue = null == blue ? empty : blue;
		this.alpha = null == alpha ? empty : alpha;
	}

	/** Interpret the @param img as an ARGB image. */
	public RGBA(final Image<? extends RealType<?>> img) {
		this(new Channel(img, 3), new Channel(img, 2), new Channel(img, 1), new Channel(img, 4));
	}

	/** Accepts only {@link Image}, {@link Number}, {@link IFunction} instances or null as arguments. */
	public RGBA(final Object red, final Object green, final Object blue, final Object alpha) throws Exception {
		this(wrap(red), wrap(green), wrap(blue), wrap(alpha));
	}

	/** Accepts only {@link Image}, {@link Number}, {@link IFunction} instances or null as arguments. */
	public RGBA(final Object red, final Object green, final Object blue) throws Exception {
		this(wrap(red), wrap(green), wrap(blue), empty);
	}

	/** Accepts only {@link Image}, {@link Number}, {@link IFunction} instances or null as arguments. */
	public RGBA(final Object red, final Object green) throws Exception {
		this(wrap(red), wrap(green), empty, empty);
	}

	/** Accepts only {@link Image}, {@link Number}, {@link IFunction} instances or null as arguments. */
	public RGBA(final Object red) throws Exception {
		this(wrap(red), empty, empty, empty);
	}

	/** Creates an RGBA with only the given channel filled, from 1 to 4,
	 *  where RGBA is really ARGB and thus A=4, R=3, G=2, B=1.
	 *  
	 *  @throws Exception If the channel < 1 or > 4. */
	public RGBA(final IFunction fn, final int channel) throws IllegalArgumentException {
		this(3 == channel ? fn : empty, 2 == channel ? fn : empty,
			 1 == channel ? fn : empty, 4 == channel ? fn : empty);
		if (channel < 1 || channel > 4) throw new IllegalArgumentException("RGB: channel must be >= 1 and <= 4");
	}

	/** Creates an RGBA with only the given channel filled, from 1 to 4,
	 *  where RGBA is really ARGB and thus A=4, R=3, G=2, B=1.
	 *  
	 *  @param ob can be an instance of {@link Image}, {@link Number}, {@link IFunction}, or null.
	 *  
	 *  @throws Exception If the channel < 1 or > 4. */
	public RGBA(final Object ob, final int channel) throws Exception, IllegalArgumentException {
		this(wrap(ob), channel);
		if (channel < 1 || channel > 4) throw new IllegalArgumentException("RGB: channel must be >= 1 and <= 4");
	}

	@Override
	public final IFunction duplicate() throws Exception {
		return new RGBA(red.duplicate(), green.duplicate(), blue.duplicate(), alpha.duplicate());
	}

	/** Returns each ARGB value packed in an {@code int} that is casted to {@code double}. */
	@Override
	public final double eval() {
		return (((int)alpha.eval()) << 24) | (((int)red.eval()) << 16) | (((int)green.eval()) << 8) | ((int)blue.eval());
	}

	@Override
	public final void findCursors(final Collection<Cursor<?>> cursors) {
		alpha.findCursors(cursors);
		red.findCursors(cursors);
		green.findCursors(cursors);
		blue.findCursors(cursors);
	}
}