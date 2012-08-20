package net.imglib2.algorithm.region.localneighborhood;

import java.util.Iterator;

import net.imglib2.AbstractEuclideanSpace;
import net.imglib2.AbstractLocalizable;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.IterableRealInterval;
import net.imglib2.Positionable;
import net.imglib2.RandomAccess;
import net.imglib2.RealPositionable;

/**
 * INCOMPLETE !!!
 *
 * TODO
 *
 * @author Tobias Pietzsch <tobias.pietzsch@gmail.com>
 * @author Stephan Preibisch <preibisch@mpi-cbg.de>
 * @author Stephan Saalfeld <saalfeld@mpi-cbg.de>
 */
public class HyperSphereNeighborhood< T > extends AbstractLocalizable implements Neighborhood< T >
{
	public static < T > HyperSphereNeighborhoodFactory< T > factory()
	{
		return new HyperSphereNeighborhoodFactory< T >() {
			@Override
			public Neighborhood< T > create( final long[] position, final long radius, final RandomAccess< T > sourceRandomAccess )
			{
				return new HyperSphereNeighborhood< T >( position, radius, sourceRandomAccess );
			}
		};
	}

	private final RandomAccess< T > sourceRandomAccess;

	private final long radius;

	private final int maxDim;

	HyperSphereNeighborhood( final long[] position, final long radius, final RandomAccess< T > sourceRandomAccess )
	{
		super( position );
		this.sourceRandomAccess = sourceRandomAccess;
		this.radius = radius;
		maxDim = n - 1;
	}
	public final class LocalCursor extends AbstractEuclideanSpace implements Cursor< T >
	{
		private final RandomAccess< T > source;

		// the current radius in each dimension we are at
		private final long[] r;

		// the remaining number of steps in each dimension we still have to go
		private final long[] s;

		public LocalCursor( final RandomAccess< T > source )
		{
			super( source.numDimensions() );
			this.source = source;
			r = new long[ n ];
			s = new long[ n ];
			reset();
		}

		protected LocalCursor( final LocalCursor c )
		{
			super( c.numDimensions() );
			source = c.source.copyRandomAccess();
			r = c.r.clone();
			s = c.s.clone();
		}

		@Override
		public T get()
		{
			return source.get();
		}

		@Override
		public void fwd()
		{
			int d;
			for ( d = 0; d < n; ++d )
			{
				if ( --s[ d ] >= 0 )
				{
					source.fwd( d );
					break;
				}
				else
				{
					s[ d ] = r[ d ] = 0;
					source.setPosition( position[ d ], d );
				}
			}

			if ( d > 0 )
			{
				final int e = d - 1;
				final long rd = r[ d ];
				final long pd = rd - s[ d ];

				final long rad = (long)( Math.sqrt( rd * rd - pd * pd ) );
				s[ e ] = 2 * rad;
				r[ e ] = rad;

				source.setPosition( position[ e ] - rad, e );
			}
		}

		@Override
		public void jumpFwd( final long steps )
		{
			for ( long i = 0; i < steps; ++i )
				fwd();
		}

		@Override
		public T next()
		{
			fwd();
			return get();
		}

		@Override
		public void remove()
		{
			// NB: no action.
		}

		@Override
		public void reset()
		{
			for ( int d = 0; d < maxDim; ++d )
			{
				r[ d ] = s[ d ] = 0;
				source.setPosition( position[ d ], d );
			}

			source.setPosition( position[ maxDim ] - radius - 1, maxDim  );

			r[ maxDim ] = radius;
			s[ maxDim ] = 1 + 2 * radius;
		}

		@Override
		public boolean hasNext()
		{
			return s[ maxDim ] > 0;
		}

		@Override
		public float getFloatPosition( final int d )
		{
			return source.getFloatPosition( d );
		}

		@Override
		public double getDoublePosition( final int d )
		{
			return source.getDoublePosition( d );
		}

		@Override
		public int getIntPosition( final int d )
		{
			return source.getIntPosition( d );
		}

		@Override
		public long getLongPosition( final int d )
		{
			return source.getLongPosition( d );
		}

		@Override
		public void localize( final long[] position )
		{
			source.localize( position );
		}

		@Override
		public void localize( final float[] position )
		{
			source.localize( position );
		}

		@Override
		public void localize( final double[] position )
		{
			source.localize( position );
		}

		@Override
		public void localize( final int[] position )
		{
			source.localize( position );
		}

		@Override
		public LocalCursor copy()
		{
			return new LocalCursor( this );
		}

		@Override
		public LocalCursor copyCursor()
		{
			return copy();
		}
	}

	@Override
	public Interval getStructuringElementBoundingBox()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T firstElement()
	{
		return cursor().next();
	}

	@Override
	public Object iterationOrder()
	{
		return this; // iteration order is only compatible with ourselves
	}

	@Override
	public boolean equalIterationOrder( final IterableRealInterval< ? > f )
	{
		return iterationOrder().equals( f.iterationOrder() );
	}

	@Override
	public double realMin( final int d )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void realMin( final double[] min )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void realMin( final RealPositionable min )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public double realMax( final int d )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void realMax( final double[] max )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void realMax( final RealPositionable max )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator< T > iterator()
	{
		return cursor();
	}

	@Override
	public long min( final int d )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void min( final long[] min )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void min( final Positionable min )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public long max( final int d )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void max( final long[] max )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void max( final Positionable max )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dimensions( final long[] dimensions )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public long dimension( final int d )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LocalCursor cursor()
	{
		return new LocalCursor( sourceRandomAccess.copyRandomAccess() );
	}

	@Override
	public LocalCursor localizingCursor()
	{
		return cursor();
	}
}