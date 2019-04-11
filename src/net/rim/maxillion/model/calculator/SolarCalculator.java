/*
 * @(#)SolarCalculator.java 1.0 2009-06-22
 * @(#)SolarCalculator.java 1.1 2009-09-15
 * @(#)SolarCalculator.java 1.2 2010-04-27
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
 * Tel: 4813770 Fax: 4813764 
 * version: opn1.2
 */
package net.rim.maxillion.model.calculator;

import java.util.Calendar;
import net.rim.maxillion.model.calculator.utils.GeoParameters;
import net.rim.maxillion.model.calculator.utils.SalatConstants;
import net.rim.maxillion.model.calculator.utils.time.TimeFormatter;


/**
 * Performs all the solar calculations including any math needed to do with the position of
 * the sun.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 Removed some unnecessary constants. Updated to deal with non-static design
 * changes to the TimeFormatter class.
 * @version 1.20 2010-04-27 CalendarConversion access is now static so no instance reference needed.
 * SolarCalculatorFormulae access is once again static. Updated to comply with the SalatConstants enum.
 * This class now has package visiblity.
 * @since MaxillionPrayers 1.0
 */
class SolarCalculator
{ 
    /** The maximum CH value. */
    public static final double MAX_CH_VALUE = 1;

    /** The calculated equatorial coordinates. */
    private double[] equatorialCoordinates;

    /** Performs height correction for problematic locations. */
    private HeightCorrector heightC;

    /** The latitude of the region to calculate the solar position data for. */
    private double latitude;

    /** The maximum latitude to use to calculate the solar position data for. */
    private double maxLatitude;

    /** The calculated noon time. */
    private double noonTime;

    /** The calculated time of the sun rise. */
    private double sunrise;

    /** The calculated time of the sun set. */
    private double sunset;


    /**
     * Allows solar calculations to be done.
     */
    public SolarCalculator()
    {
        this.equatorialCoordinates = new double[2];
        this.heightC = new HeightCorrector();
    }


    /**
     * Performs the solar calculations for the geographical region specified given the
     * specified parameters. If recalculation is needed, it is performed.
     * @param gc The date and time to perform the solar calculations for.
     * @param g The geographical data on the location to perform the solar calculations for.
     * @param dstOffset The necessary daylight savings time offset adjustment.
     * @return true If recalculation was needed due to a problematic calculation done.
     */
    public boolean calculate(Calendar gc, GeoParameters g, double dstOffset)
    {
        boolean success = performCalculation( gc, g.getTimeZone(), g.getLongitude(), g.getLatitude(), dstOffset );
        boolean correctionNeeded = HeightCorrector.correctionNeeded( success, g.getLatitude(), SalatConstants.HEIGHT_DIFFERENCE_WEST, SalatConstants.HEIGHT_DIFFERENCE_EAST );

        if (correctionNeeded)
            heightC.correct( getSinDeclination(), getCosDeclination(), SalatConstants.HEIGHT_DIFFERENCE_WEST, SalatConstants.HEIGHT_DIFFERENCE_EAST );

        double solarDifference = Math.abs( sunset-sunrise );
        boolean recalculationNeeded = !success || (solarDifference <= 1) || (solarDifference >= 23);

        if (recalculationNeeded)
        {
            this.maxLatitude = getMaxLatitude(g);
            performCalculation(gc, g.getTimeZone(), g.getLongitude(), maxLatitude, dstOffset);
        }

        if (noonTime < 0)
            noonTime += TimeFormatter.TOTAL_HOURS_IN_A_DAY;

        return recalculationNeeded;
    }


    /**
     * Gets the cos value of the equatorial declination value performed on the previously
     * calculated latitude.
     * @return The cos value of the equatorial declination value performed on the previously
     * calculated latitude.
     */
    public double getCosDeclination()
    {
        return Math.cos( equatorialCoordinates[0] ) * Math.cos(latitude);
    }


    /**
     * Gets the cos value of the equatorial declination value performed on the specified latitude.
     * @param latitude The latitude in radians.
     * @return The cos value of the equatorial declination value performed on the specified latitude.
     */
    public double getCosDeclination(double latitude)
    {
        return Math.cos( equatorialCoordinates[0] ) * Math.cos(latitude);
    }


    /**
     * Gets the equatorial coordinates as calculated. The Equatorial Coordinate System is a
     * popular method of mapping celestial objects. It functions by projecting the Earth's
     * geographic poles, equator, and ecliptic onto the celestial sphere. This allows stars
     * to be catalogued by objective locations (as opposed to the altitude-azimuth system, in
     * which stars' coordinates are dependent on the observer's location on Earth). The
     * projection of the Earth's equator onto the celestial sphere is called the celestial
     * equator. Similarly, the projections of the Earth's North and South geographic poles
     * become the North and South celestial poles, respectively.<br><br>
     *
     * There are two systems to specify the longitudinal (longitude-like) coordinate:<br>
     * -The hour angle system is fixed to the Earth like the geographic coordinate system
     * -The right ascension system is fixed to the stars, thus, during a night or a few
     *  nights, it appears to move across the sky as the Earth spins and orbits under the
     *  fixed stars. Over long periods of time, precession and nutation effects alter the
     *  earth's orbit and thus the apparent location of the stars. When considering
     *  observations separated by long intervals, it is necessary to specify an epoch
     *  (frequently J2000.0, for older data B1950.0) when specifying coordinates of planets,
     *  stars, galaxies, etc.<br><br>
     *
     * The latitudinal (latitude-like) angle of the equatorial system is called declination
     * (Dec for short). It measures the angle of an object above or below the celestial
     * equator. The longitudinal angle is called the right ascension (RA for short). It
     * measures the angle of an object east of the vernal equinox point. Unlike longitude,
     * right ascension is usually measured in hours instead of degrees, because the apparent
     * rotation of the equatorial coordinate system is closely related to sidereal time and
     * hour angle. Since a full rotation of the sky takes 24 hours of sidereal time to
     * complete, there are (360 degrees / 24 hours) = 15 degrees in one hour of right
     * ascension. [1]<br><br>
     *
     * [1] Wikipedia, (2009). Equatorial coordinate system. [Online]. Available:
     * http://en.wikipedia.org/wiki/Equatorial_coordinate_system [June 20, 2009]
     *
     * @return The equatorial coordinates of the given region as calculated.
     */
    public double[] getEquatorialCoordinates()
    {
        return equatorialCoordinates;
    }


    /**
     * Gets the height corrector used for problematic locations.
     * @return The object that performs height correction for problematic locations.
     */
    public HeightCorrector getHeightC()
    {
        return heightC;
    }


    /**
     * Gets the maximum latitude to use to calculate the solar position data for.
     * @return The maximum latitude used to calculate the solar position data for.
     */
    public double getMaxLatitude()
    {
        return this.maxLatitude;
    }


    /**
     * Gets the total length of a night for the region.
     * @return The total night length for the region.
     */
    public double getNightLength()
    {
        return TimeFormatter.TOTAL_HOURS_IN_A_DAY-(sunset-sunrise);
    }


    /**
     * Gets the noon time for the region.
     * @return The time of noon for the region.
     */
    public double getNoonTime()
    {
        return this.noonTime;
    }


    /**
     * Gets the sin value of the equatorial declination value performed on the previously
     * calculated latitude.
     * @return The sin value of the equatorial declination value performed on the previously
     * calculated latitude.
     */
    public double getSinDeclination()
    {
        return Math.sin( equatorialCoordinates[0] ) * Math.sin(latitude);
    }


    /**
     * Gets the sin value of the equatorial declination value performed on the specified latitude.
     * @param latitude The latitude in radians.
     * @return The sin value of the equatorial declination value performed on the specified latitude.
     */
    public double getSinDeclination(double latitude)
    {
        return Math.sin( equatorialCoordinates[0] ) * Math.sin(latitude);
    }


    /**
     * Gets the time of the sunrise for the region.
     * @return The time of the sun rises in that region.
     */
    public double getSunrise()
    {
        return this.sunrise;
    }


    /**
     * Gets the time of the sun sets in the region.
     * @return The time of the sun sets in that region.
     */
    public double getSunset()
    {
        return this.sunset;
    }


    /**
     * Performs the solar calculations for the geographical region specified given the
     * specified parameters.
     * @param gc The date and time to perform the solar calculations for.
     * @param timeZone The time zone to perform the solar calculations for.
     * @param longitude The longitude of the region (in radians).
     * @param latitude The latitude of the region (in radians).
     * @param dstOffset The necessary daylight savings time offset adjustment.
     * @return true If recalculation will be needed due to a problematic region.
     */
    public boolean performCalculation(Calendar gc, double timeZone, double longitude, double latitude, double dstOffset)
    {
        this.latitude = latitude; // 0.791916
        double tz = -(timeZone+dstOffset); // 4.0
        double julianDate = CalendarConversion.calculateJulianEpoch(gc); // 2455304
        double T = SolarCalculatorFormulae.calculateCenturiesSince2000(julianDate, tz); // 0.102934
        double L = SolarCalculatorFormulae.calculateSunMeanLongitude(T); // 0.4433
        double M = SolarCalculatorFormulae.calculateSunMeanAnomaly(T); // 1.8152
        double earthEccentricity = SolarCalculatorFormulae.calculateEarthEccentricity(T); // e: 0.016746
        double obliq = SolarCalculatorFormulae.calculateEclipticObliquity(T); // Ec: 0.409296
        double Y = SolarCalculatorFormulae.calculateY(obliq); // 0.043078

        double eot = SolarCalculatorFormulae.calculateEquationOfTime(Y, L, M, earthEccentricity); // EOT: 0.001912
        double hours = SolarCalculatorFormulae.calculateHours(eot); // UT: 0.007306
        double eValue = SolarCalculatorFormulae.calculateEuler(M, earthEccentricity); // 1.831
        double v = SolarCalculatorFormulae.calculateV(earthEccentricity, eValue); // 1.8475
        double tht = SolarCalculatorFormulae.calculateTheta(L, v, M); // 0.4756

        this.equatorialCoordinates = SolarCalculatorFormulae.getEquatorialCoordinates( new double[]{0, tht} ); // where DECL = delta = x: 0.183, alpha = RA = y: 0.441
        this.noonTime = SolarCalculatorFormulae.calculateNoonTime(-longitude, hours, tz);
        double cH = SolarCalculatorFormulae.calculateCH( latitude, equatorialCoordinates[0] );
        boolean successFlag = cH <= MAX_CH_VALUE;

        if (!successFlag)
            cH = MAX_CH_VALUE; // At this day and place the sun does not rise or set

        /* The sunrise occurs when the upper limb of the Sun disc is visible at the horizon,
         * towards east, at a location whose elevation is reduced to the sea level - while
         * the sunset occurs in the same circumstances, but in the opposite direction,
         * towards the western horizon.
         * 
         * [2] Tomezzoli, Vanni, (2001). How to find the times of sunrise and sunset. [Online]. Available:
         * http://xoomer.virgilio.it/vtomezzo/sunriset/formulas/algorythms.html [June 21, 2009]
         */
        double H = SolarCalculatorFormulae.calculateH(cH);
        this.sunrise = noonTime-H; 
        this.sunset = noonTime+H;

        return successFlag;
    }


    /**
     * Gets the maximum latitude to use for the specified geographical region.
     * @param g The geographical region to get the maximum latitude value for.
     * @return The maximum latitude to use for the specified geographical region (in radians).
     */
    public static final double getMaxLatitude(GeoParameters g)
    {
        double maxLatitude = Math.abs( SalatConstants.RABITA_REFERENCE_ANGLE ); // There are problems in computing sun(rise,set)

        if ( g.getLatitude() < 0 ) // This is because of places above -+65.5 at some days of the year
            maxLatitude = -maxLatitude;

        return maxLatitude;
    }
}