/**
 * Copyright (c) 2009--2012, ImgLib2 developers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the Fiji project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Tobias Pietzsch
 */
package net.imglib2.view;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.img.cell.CellImg;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.cell.DefaultCell;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.IntervalIndexer;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Tobias Pietzsch <tobias.pietzsch@gmail.com>
 *
 */
public class RandomAccessibleIntervalCursorTest
{
	long[] dimensions;

	int numValues;

	int[] intData;

	ArrayImg< IntType, IntArray > array1;

	ArrayImg< IntType, IntArray > array2;

	CellImg< IntType, IntArray, DefaultCell< IntArray > > cell;

	@SuppressWarnings( "unchecked" )
	@Before
	public void setUp()
	{
		dimensions = new long[] {207, 103};
		array1 = ( ArrayImg< IntType, IntArray > ) new ArrayImgFactory< IntType >().create( dimensions, new IntType() );
		array2 = ( ArrayImg< IntType, IntArray > ) new ArrayImgFactory< IntType >().create( dimensions, new IntType() );
		cell = ( CellImg< IntType, IntArray, DefaultCell< IntArray > > ) new CellImgFactory< IntType >().create( dimensions, new IntType() );

		// fill intData with random values
		numValues = 1;
		for ( int d = 0; d < dimensions.length; ++d )
			numValues *= dimensions[ d ];
		intData = new int[ numValues ];
		final Random random = new Random( 0 );
		for ( int i = 0; i < numValues; ++i )
			intData[ i ] = random.nextInt();

		// copy intData to array1
		final long[] pos = new long[ dimensions.length ];
		final RandomAccess< IntType > a = array1.randomAccess();
		for ( int i = 0; i < numValues; ++i )
		{
			IntervalIndexer.indexToPosition( i, dimensions, pos );
			a.setPosition( pos );
			a.get().set( intData[ i ] );
		}
	}

	public void copy( final Cursor< IntType > src, final Cursor< IntType > dst )
	{
		while( src.hasNext() )
			dst.next().set( src.next().get() );
	}

	int[] getImgAsInts( final Img< IntType > img )
	{
		final RandomAccess< IntType > a = img.randomAccess();
		final int N = ( int ) img.size();
		final int[] data = new int[ N ];
		final long[] dim = new long[ img.numDimensions() ];
		final long[] pos = new long[ img.numDimensions() ];
		img.dimensions( dim );
		for ( int i = 0; i < N; ++i ) {
			IntervalIndexer.indexToPosition( i, dim, pos );
			a.setPosition( pos );
			data[ i ] = a.get().get();
		}
		return data;
	}

	@Test
	public void testJumpFwd()
	{
		final Cursor< IntType > c1 = array1.cursor();
		final Cursor< IntType > c2 = new RandomAccessibleIntervalCursor< IntType >( array1 );

		c1.fwd();
		c2.fwd();
		assertEquals( c1.get().get(), c2.get().get() );

		for ( int i=0; i<10; ++i )
			c1.fwd();
		c2.jumpFwd( 10 );
		assertEquals( c1.get().get(), c2.get().get() );

		c1.jumpFwd( 713 );
		c2.jumpFwd( 713 );
		assertEquals( c1.get().get(), c2.get().get() );
	}

	@Test
	public void testArrayCopy()
	{
		copy( array1.cursor(), new RandomAccessibleIntervalCursor< IntType >( array2 ) );
		assertArrayEquals( intData, getImgAsInts( array2 ) );
	}

	@Test
	public void testArrayCopy2()
	{
		copy( new RandomAccessibleIntervalCursor< IntType >( array1 ), array2.cursor() );
		assertArrayEquals( intData, getImgAsInts( array2 ) );
	}

	@Test
	public void testCellCopy()
	{
		copy( array1.cursor(), new RandomAccessibleIntervalCursor< IntType >( cell ) );
		assertArrayEquals( intData, getImgAsInts( cell ) );
	}
}