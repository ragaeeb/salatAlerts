/*
 * @(#)IshaTimeCalculator.java  1.0 2009-06-22
 * @(#)IshaTimeCalculator.java  1.1 2010-04-27
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
import net.rim.maxillion.model.calculator.utils.SalatConstants;


/**
 * Calculates the Isha prayer time at a referenced latitude (45 degrees as suggested by Rabita)
 * to the night length.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2010-04-27 This class now has package visibility.
 * @since MaxillionPrayers 1.0
 */
class IshaTimeCalculator
{
    /** The linear ratio needed. */
    public static final double LINEAR_RATIO = 0.45;

    /** The multiplier value. */
    public static final double MULTIPLIER = 1.3369;

    /** The maximum Isha angle. */
    private static final double MAXIMUM_ISHA_ANGLE = Math.toRadians(48);

    /** The day of the month when the northern and southern hemispheres are inclined toward the sun in specific months. */
    private static final int SOLSTICES_DAY_OF_MONTH = 21;

    /** The calculated Isha time. */
    private double ishaTime;

    /** The calculated Isha time according to Rabita. */
    private double rabitaIsha;


    /**
     * Allows the Isha prayer start time to be calculated.
     * @param ishaTwilight The Isha twilight angle (in radians).
     * @param sc Reference to the solar information calculator.
     * @param latitude The latitude of the region to calculate the Isha time for.
     * @param ratioCalc Gives accurate calculations of the Fajr and Isha prayer start times.
     * @param ishaInterval The Isha interval time from the Maghrib prayer.
     * @param maghribTime The time of the Maghrib prayer.
     * @param noon The calculated noon-time for the region.
     * @param night The calculated night length for the region.
     * @param set The calculated sunset time for the region.
     * @param sinDec The sin value of the equatorial latitudinal angle.
     * @param cosDec The cos value of the equatorial latitudinal angle.
     */
    public IshaTimeCalculator(double ishaTwilight, SolarCalculator sc, double latitude, FajrIshaRatioCalculator ratioCalc, double ishaInterval, double maghribTime, double noon, double night, double set, double sinDec, double cosDec)
    {
        if (ishaTwilight != 0)  /* if Ish angle  not equal zero*/
        {
            double angle = -ishaTwilight;
            double cH = HeightCorrector.calculateCH( angle, sinDec, cosDec );
            double absCH = Math.abs(cH);

            if ( Math.abs(latitude) < MAXIMUM_ISHA_ANGLE ) // If the latitude is less than 48 degrees, there is no problem
            {
                cH = HeightCorrector.calculateCH( angle, sc.getSinDeclination(), sc.getCosDeclination() );
                absCH = Math.abs(cH);

                double H = IslamicEventAdjustedTimes.computeH(cH);
                ishaTime = sc.getNoonTime() + ( H+sc.getHeightC().getCorrectedWesternHeight()+SalatConstants.SAFETY_TIME );    /* Isha time, instead of  Sunset+1.5h */
                rabitaIsha = ishaTime;
            }

            else
            {
                // ---------------------------------------------------------------------
                if ( absCH > (LINEAR_RATIO + MULTIPLIER*ishaTwilight) ) // A linear equation I have introduced: The problem occurs for places above -+48 in the summer
                {
                    /* The cause of the seasons is that the Earth's axis of rotation is not
                     * perpendicular to its orbital plane (the flat plane made through the
                     * center of mass (barycenter) of the solar system (near or within the
                     * Sun) and the successive locations of Earth during the year), but
                     * currently makes an angle of about 23.44° (called the "obliquity of the
                     * ecliptic"), and that the axis keeps its orientation with respect to
                     * inertial space. As a consequence, for half the year (from around 20
                     * March to 22 September) the northern hemisphere is inclined toward the
                     * Sun, with the maximum around 21 June, while for the other half year
                     * the southern hemisphere has this distinction, with the maximum around
                     * 21 December. The two moments when the inclination of Earth's rotational
                     * axis has maximum effect are the solstices. [1]
                     *
                     * [1] Wikipedia, (2009). Solstice. [Online]. Available:
                     * http://en.wikipedia.org/wiki/Solstice [June 21, 2009]
                     */
                    Calendar gc = Calendar.getInstance();
                    gc.setTime( ratioCalc.getCalendar().getTime() );
                    gc.set(Calendar.DAY_OF_MONTH, SOLSTICES_DAY_OF_MONTH);

                    if (latitude < 0)
                        gc.set(Calendar.MONTH, Calendar.DECEMBER);

                    else
                        gc.set(Calendar.MONTH, Calendar.JUNE);

                    ratioCalc.setGc(gc);

                    ishaTime = set + night*ratioCalc.getIshaStartTime(); // According to the Rabita method
                }

                else // no problem
                {
                    double H = IslamicEventAdjustedTimes.computeH(cH);
                    ishaTime = noon + ( H + sc.getHeightC().getCorrectedWesternHeight() + SalatConstants.SAFETY_TIME ); // Isha time, instead of Sunset+1.5h
                }

                // ---------------------------------------------------------------------
                if (absCH > 1.0) // The problem occurs for places above -+48 in the summer
                {
                    double ishaStart = ratioCalc.getIshaStartTime();
                    rabitaIsha = set + night*ishaStart; // According to the general ratio rule
                }

                else
                {
                    double H = IslamicEventAdjustedTimes.computeH(cH);
                    rabitaIsha = noon + (H + sc.getHeightC().getCorrectedWesternHeight() + SalatConstants.SAFETY_TIME ); // Isha time, instead of Sunset+1.5h
                }
            }
        }

        else
        {
            ishaTime = maghribTime+ishaInterval; // Isha time OmAlqrah standard Sunset + fixed time (1.5 hours or 2 hours in Ramadan)
            rabitaIsha = ishaTime;
        }
    }


    /**
     * Gets the calculated Isha prayer time.
     * @return The Isha prayer start time.
     */
    public double getIshaTime()
    {
        return ishaTime;
    }


    /**
     * Gets the calculated Isha prayer time as suggested by Rabita.
     * @return The Rabita suggested Isha prayer start time.
     */
    public double getRabitaIsha()
    {
        return rabitaIsha;
    }
}