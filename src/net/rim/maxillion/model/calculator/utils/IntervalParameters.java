/*
 * @(#)IntervalParameters.java	1.0	2009-06-22
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
 * Time intervals that are used for the calculation of certain prayers. Duration in hours (ie:
 * a value of 1 is any field is 1 hour, 1.5 is 90 minutes, etc.). For example, a time interval
 * is subtracted from sunrise to obtain the Fajr prayer time while the interval is added to
 * the sunset to obtain the Isha time.<br><br>
 *
 * At extreme latitudes the twilight may persist between sunset and the next sunrise for
 * certain months of the year. In these months the sun does not go below the horizon by a
 * sufficient amount to abolish twilight. Hence there is no true night. Under these
 * circumstances, Fajr and Isha times may be calculated using one of four agreed principles:<br>
 * 1. Nearest latitude (Aqrab Al-Balad) - add the interval between sunset and Isha for a
 * location on latitude 48 degrees to the local sunset time to obtain time for local Isha.
 * Similarly the interval between fajr and sunrise for a location on latitude 48 degrees is
 * subtracted from local sunrise to obtain local Fajr time.<br>
 * 2. Nearest day (Aqrab Al-Ayyam) - use Fajr and Isha times from the last day when it was
 * possible to calculate these times in the normal way for that location.<br>
 * 3. Middle of night (Nisf Al-Lail) - split interval between sunrise and sunset into two
 * halves. Isha is offered before the midpoint (e.g. 15 minutes before) and Fajr is offered
 * after the midpoint.
 * 4. One seventh of night (Sube Al-Lail) - split interval between sunset and sunrise into
 * seven segments. Isha is offered after the first segment and fajr is offered after the
 * sixth segment. [1]<br><br>
 *
 * [1] Ahmed, Monzur, (1997). The Determination of Salat Times. [Online]. Available:
 * http://www.ummah.net/astronomy/saltime/ [June 21, 2009]<br>
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @since MaxillionPrayers 1.0
 */
public class IntervalParameters
{
	/** The number of minutes to add in Zawal time for the Dhuhr prayer time. */
	private double dhuhrInterval;

	/** The difference of Isha time from Maghrib. */
	private double ishaInterval;

	/** The number of minutes to add to the sunset time for the Maghrib prayer time. */
	private double maghribInterval;


	/**
	 * Creates an instance of this class with the default Dhuhr and Maghrib intervals and a
	 * 0 Isha interval.
	 */
	public IntervalParameters()
	{
		this(0);
	}


	/**
	 * Creates an instance of this class with the specified Isha interval and a default interval
	 * of 1 for both the Dhuhr and Maghrib intervals.
	 * @param ishaInterval The Isha interval to use.
	 */
	public IntervalParameters(double ishaInterval)
	{
		super();
		this.ishaInterval = ishaInterval;
		this.dhuhrInterval = 1;
		this.maghribInterval = 1;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		boolean result = false;

		try {
			IntervalParameters gp = (IntervalParameters)obj;
			result = (dhuhrInterval == gp.dhuhrInterval) && (ishaInterval == gp.ishaInterval) && (maghribInterval == gp.maghribInterval);
		}

		catch (ClassCastException ex)
		{
		}

		return result;
	}

	/**
	 * Gets the Dhuhr interval.
	 * @return The interval for the Dhuhr prayer.
	 */
	public double getDhuhrInterval()
	{
		return this.dhuhrInterval;
	}


	/**
	 * Gets the Isha prayer interval time as stored.
	 * @return The difference of Isha time from Maghrib.
	 */
	public double getIshaInterval()
	{
		return this.ishaInterval;
	}


	/**
	 * Gets the Maghrib prayer interval value.
	 * @return The number of minutes to add to the sunset time for the Maghrib prayer time.
	 */
	public double getMaghribInterval()
	{
		return this.maghribInterval;
	}


	/**
	 * Sets the Dhuhr interval to the specified value.
	 * @param dhuhrInterval The new Dhuhr prayer interval.
	 */
	public void setDhuhrInterval(double dhuhrInterval)
	{
		this.dhuhrInterval = dhuhrInterval;
	}


	/**
	 * Sets the Isha interval to the specified value.
	 * @param ishaInterval The new Isha prayer interval.
	 */
	public void setIshaInterval(double ishaInterval)
	{
		this.ishaInterval = ishaInterval;
	}


	/**
	 * Sets the Maghrib interval to the specified value.
	 * @param maghribInterval The new Maghrib prayer interval.
	 */
	public void setMaghribInterval(double maghribInterval)
	{
		this.maghribInterval = maghribInterval;
	}
}