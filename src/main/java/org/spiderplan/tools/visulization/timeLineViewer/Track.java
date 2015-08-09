/*******************************************************************************
 * Copyright (c) 2015 Uwe Köckemann <uwe.kockemann@oru.se>
 *  
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package org.spiderplan.tools.visulization.timeLineViewer;

import java.util.ArrayList;

/**
 * Represents a track of events on a time line, which is made up of a series of values with start and end times.
 * 
 * @author Uwe Köckemann
 *
 */
public class Track {
	public String name;
	public ArrayList<Value> values = new ArrayList<Value>();
	
	public Track( String name ) {
		this.name = name;
	}
	
	/**
	 * Update all values with this id
	 * @param id ID to be updated
	 * @param start New start time
	 * @param end New end time
	 */
	public void update( String id, int start, int end ) {
		for ( Value v : values ) {
			if ( v.id.equals(id) ) {
				v.start = start;
				v.end = end;
			}
		}
	}
	
	public Track copy() {
		Track c = new Track(this.name);
		for ( Value v : this.values ) {
			c.values.add(v.copy());
		}
		return c;
	}
}
