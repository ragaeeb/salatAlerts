/*
 * @(#)FajrIshaRatioCalculator.java 1.0 2009-06-22
 * @(#)FajrIshaRatioCalculator.java 1.1 2010-04-27
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
 *
 * The codebase for this class is based on the code of:
 * Fayez Alhargan, 2001
 * King Abdulaziz City for Science and Technology
 * Computer and Electronics Research Institute
 * Riyadh, Saudi Arabia
 * alhargan@kacst.edu.sa
 * Tel:4813770 Fax:4813764 
 * version: opn1.2
 */
package net.rim.maxillion.model.calculator;

import java.util.Calendar;
import net.rim.maxillion.model.calculator.utils.GeoParameters;
import net.rim.maxillion.model.calculator.utils.SalatConstants;



/**
 * Helper class allowing accurate calculations of the Fajr and Isha prayer start times.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2010-04-27 Updated to comply with the SalatConstants enum.
 * @since MaxillionPrayers 1.0
 */
class FajrIshaRatioCalculator
{
    /** The calendar that will be used to perform calculations. */
    private Calendar calendar;

    /** The start time for the Isha prayer. */
    private double ishastart;

    /** The maximum latitude value to be used. */
    private double maxLatitude;

    /** Deals with calculations relating to sun position. */
    private SolarCalculator sc;

    /** Geographical location information to calculate the times for. */
    private GeoParameters gp;


    /**
     * Creates an instance of this class and allows for precise calculations of the Fajr and Isha
     * prayer start times. 
     * @param sc Deals with calculations relating to sun position.
     * @param gc The calendar that will be used to perform calculations.
     * @param p The parameters being used for the calculation.
     */
    public FajrIshaRatioCalculator(SolarCalculator sc, Calendar gc, GeoParameters gp)
    {
        this.sc = sc;
        this.calendar = gc;
        this.gp = gp;
        this.maxLatitude = SolarCalculator.getMaxLatitude(gp);
    }


    /**
     * Gets the calendar that is used to perform calculations.
     * @return The calendar necessary to perform calculations with.
     */
    public Calendar getCalendar()
    {
        return calendar;
    }


    /**
     * Calculates the start time for the Fajr prayer given the specified latitude. Also calculates
     * the value of the Isha prayer start time but does not return it.
     * @param latitude The latitude to use when calculating the Fajr prayer start time.
     * @return The start time of the Fajr prayer.
     */
    public double getFajrStartTime(double latitude)
    {
        sc.performCalculation(calendar, gp.getTimeZone(), gp.getLongitude(), maxLatitude, DaylightSavingsTime.OFFSET );

        double night = sc.getNightLength(); // Night length

        // Fajr
        double angle = -SalatConstants.ISNA_ANGLES.getFajrTwilightAngle();
        double cH = HeightCorrector.calculateCH( angle, sc.getSinDeclination(maxLatitude), sc.getCosDeclination(maxLatitude) );
        double H = IslamicEventAdjustedTimes.computeH(cH);
        double fajrReference = sc.getNoonTime() - H - SalatConstants.SAFETY_TIME;
        double ishaTwilight = SalatConstants.ISNA_ANGLES.getIshaTwilightAngle();
        double ishaReference = sc.getSunset() + SalatConstants.ISNA_INTERVALS.getIshaInterval();

        if (ishaTwilight != 0)
        {
            angle = -ishaTwilight;
            cH = HeightCorrector.calculateCH( angle, sc.getSinDeclination(maxLatitude), sc.getCosDeclination(maxLatitude) );
            H = IslamicEventAdjustedTimes.computeH(cH);
            ishaReference = sc.getNoonTime() + H + SalatConstants.SAFETY_TIME;
        }

        ishastart = ( ishaReference - sc.getSunset() )/night;

        return ( sc.getSunrise()-fajrReference )/night;
    }


    /**
     * Gets the calculated start time of the Isha prayer.
     * @return The start time of the Isha prayer.
     */
    public double getIshaStartTime()
    {
        return ishastart;
    }


    /**
     * Sets the calendar to use to the specified one.
     * @param gc The calendar that should be used when performing the calculations.
     */
    public void setGc(Calendar gc)
    {
        this.calendar = gc;
    }
}