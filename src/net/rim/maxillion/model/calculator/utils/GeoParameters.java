/*
 * @(#)GeoParameters.java	1.0	2009-06-22
 * @(#)GeoParameters.java	1.1	2009-09-15
 * @(#)GeoParameters.java	1.2	2010-04-27
 *
 * Copyright 2009 Exes Technologies. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Exes Technologies nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.rim.maxillion.model.calculator.utils;



/**
 * Stores the coordinates of a location including its latitude, longitude and time zone. This
 * information can be used for prayer time calculation.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 Constructor was replaced with empty constructor and set() methods. toString
 * method no longer depends on unnecessary static methods.
 * @version 1.20 2010-04-27 Removed setter methods and replaced them in constructor.
 * @since MaxillionPrayers 1.0
 */
public class GeoParameters
{
	/** The latitude in radians. */
	private double latitude;

	/** The longitude in radians. */
	private double longitude;

	/** The time zone associated with the longitude and latitude. */
	private double timeZone;


	/**
	 * Creates an instance of this class so that geographical parameters can be stored.
	 * @param latitude The latitude in degrees.
	 * @param longitude The longitude in degrees.
	 * @param timeZone The time zone associated with the location.
	 * @since MaxillionPrayers 3.0
	 */
	public GeoParameters(double latitude, double longitude, double timeZone)
	{
		this.latitude = Math.toRadians(latitude);
		this.longitude = Math.toRadians(longitude);
		this.timeZone = timeZone;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		boolean result = false;

		try {
			GeoParameters gp = (GeoParameters)obj;
			result = (latitude == gp.latitude) && (longitude == gp.longitude) && (timeZone == gp.timeZone);
		}

		catch (ClassCastException ex)
		{
		}

		return result;
	}


	/**
	 * Gets the latitude stored. Latitude, usually denoted by the Greek letter gives the
	 * location of a place on Earth (or other planetary body) north or south of the equator.
	 * Lines of Latitude are the horizontal lines shown running east-to-west on maps
	 * (particularly so in the Mercator projection). Technically, latitude is an angular
	 * measurement in degrees (marked with °) ranging from 0° at the equator (low latitude)
	 * to 90 degrees at the poles (+90 for the North pole, -90 for the South Pole). The
	 * complementary angle of a latitude is called the colatitude and this is approximately
	 * the angle between straight up at the surface (the zenith) and the sun at the Spring
	 * and Fall equinox. [1]<br><br>
	 *
	 * [1] Wikipedia, (2009). Latitude. [Online]. Available:
	 * http://en.wikipedia.org/wiki/Latitude [June 20, 2009]
	 *
	 * @return The latitude in radians.
	 */
	public double getLatitude()
	{
		return this.latitude;
	}


	/**
	 * Gets the longitude stored. Longitude is the geographic coordinate most commonly used in
	 * cartography and global navigation for east-west measurement. A line of longitude is a
	 * north-south meridian and half of a great circle. [2]<br><br>
	 *
	 * [2] Wikipedia, (2009). Longitude. [Online]. Available:
	 * http://en.wikipedia.org/wiki/Longitude [June 20, 2009]
	 *
	 * @return The longitude in radians.
	 */
	public double getLongitude()
	{
		return this.longitude;
	}


	/**
	 * Gets the time zone stored in terms of Greenwich Mean Time.
	 * @return The time zone (ie: -5.0 means GMT-5.0, 4 means GMT+4.0).
	 */
	public double getTimeZone()
	{
		return this.timeZone;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Latitude: "+latitude+"\nLongitude: "+longitude+"\nTimezone: "+timeZone;
	}
}