/*
 * @(#)IslamicEventAdjustedTimes.java   1.0 2009-06-22
 * @(#)IslamicEventAdjustedTimes.java   1.1 2009-09-15
 * @(#)IslamicEventAdjustedTimes.java   1.2 2010-04-27
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
import net.rim.device.api.util.MathUtilities;
import net.rim.maxillion.model.calculator.utils.GeoParameters;
import net.rim.maxillion.model.calculator.utils.SalatConstants;
import net.rim.maxillion.model.calculator.utils.TimeCriticalEvent;
import net.rim.maxillion.model.calculator.utils.time.TimeFormatter;
import net.rim.maxillion.model.calculator.utils.time.TimeWrapper;


/**
 * Computes the prayer times, and other times such as sunrise and sunset with the corrected
 * values.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 Updated to comply with non-static design changes to the TimeFormatter
 * class.
 * @version 1.20 2010-04-27 Removed eid prayer time calculation. Changed HourMinuteTime wrapper to
 * TimeWrapper object. Updated to comply with the SalatConstants enum. This class now has package
 * visibility.
 * @since MaxillionPrayers 1.0
 */
class IslamicEventAdjustedTimes
{
    /** The maximum radians value used to calculate the Fajr TimeCriticalEvent. */
    private static final double FAJR_MAX_RADIANS = Math.toRadians(48);

    /** The cos value of the equatorial latitudinal angle. */
    private double cosDec;

    /** The night length. */
    private double night;

    /** The noon-time. */
    private double noon;

    /** The correct prayer time as calculated. */
    private TimeWrapper[] prayerData;

    /** Allows accurate calculations of the Fajr and Isha prayer start times. */
    private FajrIshaRatioCalculator ratioCalc;

    /** The time of the sunrise. */
    private double rise;

    /** Reference to the solar information calculator. */
    private SolarCalculator sc;

    /** The sunset time. */
    private double set;

    /** The sin value of the equatorial latitudinal angle. */
    private double sinDec;


    /**
     * Allows prayer time and other key event times to be computed with the correct values.
     * @param problematic Was the solar calculation problematic?
     * @param sc Reference to the solar information calculator.
     * @param p The parameters being used for the calculation.
     * @param gc The calendar that will be used to perform calculations.
     */
    public IslamicEventAdjustedTimes(boolean problematic, SolarCalculator sc, GeoParameters p, Calendar gc)
    {
        this.sc = sc;
        this.prayerData = new TimeWrapper[7];
        this.ratioCalc = new FajrIshaRatioCalculator(sc, gc, p);

        computeSunrise();
        computeDhuhrTime( SalatConstants.ISNA_INTERVALS.getDhuhrInterval() );
        double maghrib = computeMaghribTime( SalatConstants.ISNA_INTERVALS.getMaghribInterval() );
        computeAsrTime( problematic, SalatConstants.SHAFII_ASR_JURISTIC_SHADOW_RATIO, p.getLatitude() );
        computeFajrTime( SalatConstants.ISNA_ANGLES.getFajrTwilightAngle(), p.getLatitude() );
        computeIshaTime( p.getLatitude(), maghrib );
    }


    /**
     * Gets the calculated prayer time information.
     * @return The final prayer time information as calculated.
     */
    public TimeWrapper[] getPrayerData()
    {
        return prayerData;
    }


    /**
     * Computes the Asr prayer time.
     * @param problematic Was the solar calculation problematic?
     * @param angleRatio The angle ratio.
     * @param latitude The latitude to use when calculating the Asr prayer start time.
     */
    private void computeAsrTime(boolean problematic, int angleRatio, double latitude)
    {
        double difference = sc.getEquatorialCoordinates()[0];

        if (problematic) // for places above 65 degrees
            difference -= sc.getMaxLatitude();

        else // no problem
            difference -= latitude; // In the standard equations abs() is not used, but it is required for -ve latitude

        double act = getActValue(angleRatio, difference);
        double angle = MathUtilities.atan(1.0/act);
        double cH = HeightCorrector.calculateCH( angle, sc.getSinDeclination(), sc.getCosDeclination() );
        double H = getAsrH(cH);

        double asrTime = sc.getNoonTime()+H+SalatConstants.SAFETY_TIME; // Asr time
        prayerData[TimeCriticalEvent.Asr] = TimeFormatter.getTime(asrTime, 0);
    }


    /**
     * Computes the time for the Dhuhr TimeCriticalEvent.
     * @param dhuhrInterval The interval for the Dhuhr TimeCriticalEvent.
     */
    private void computeDhuhrTime(double dhuhrInterval)
    {
        double dhuhrTime = sc.getNoonTime() + SalatConstants.SAFETY_TIME; /* Zohar time+extra time to make sure that the sun has moved from zaowal */
        TimeWrapper t = TimeFormatter.getTime(dhuhrTime, (int)dhuhrInterval );
        prayerData[TimeCriticalEvent.Dhuhr] = t;
    }



    /**
     * Computes the Fajr prayer time.
     * @param fajrTwilight The Fajr twilight angle.
     * @param latitude The latitude to use when calculating the Asr prayer start time.
     */
    private void computeFajrTime(double fajrTwilight, double latitude)
    {
        double angle = -fajrTwilight; // The value -19deg is used by OmAlqrah for Fajr, but it is not correct. Astronomical twilight and Rabita use -18deg
        //double rabitaFajr;
        double fajrTime;
        double cH = HeightCorrector.calculateCH( angle, sc.getSinDeclination(), sc.getCosDeclination() );
        double H;

        if( Math.abs(latitude) < FAJR_MAX_RADIANS ) // If latitude is < 48 degrees: no problem
        {
            H = computeH(cH);
            fajrTime = sc.getNoonTime() - ( H+sc.getHeightC().getCorrectedEasternHeight() ) + SalatConstants.SAFETY_TIME;
            prayerData[TimeCriticalEvent.Fajr] = TimeFormatter.getTime(fajrTime, 0);
        }

        else // Get fixed ratio, data depends on latitude sign
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
            gc.set(Calendar.DAY_OF_MONTH, 21);

            if (latitude < 0)
                gc.set(Calendar.MONTH, Calendar.DECEMBER);

            else
                gc.set(Calendar.MONTH, Calendar.JUNE);


            ratioCalc.setGc(gc);
            rise = sc.getSunrise();
            noon = sc.getNoonTime();
            night = sc.getNightLength();
            set = sc.getSunset();
            sinDec = sc.getSinDeclination();
            cosDec = sc.getCosDeclination();
            double fajrStart = ratioCalc.getFajrStartTime(latitude);
            double absCH = Math.abs(cH);

            if( absCH > ( IshaTimeCalculator.LINEAR_RATIO + IshaTimeCalculator.MULTIPLIER*fajrTwilight ) ) // linear equation: The problem occurs for places above -+48 in the summer
            {
                fajrTime = rise - night*fajrStart; // According to the general ratio rule
                prayerData[TimeCriticalEvent.Fajr] = TimeFormatter.getTime(fajrTime, 0);
            }

            else // no problem
            {
                H = computeH(cH);
                fajrTime = noon - ( H+sc.getHeightC().getCorrectedEasternHeight() ) + SalatConstants.SAFETY_TIME;
                prayerData[TimeCriticalEvent.Fajr] = TimeFormatter.getTime(fajrTime, 0);
            }
        }
    }


    /**
     * Computes the Isha prayer time.
     * @param p The parameters being used for the calculation.
     * @param maghribTime The time of the Maghrib TimeCriticalEvent.
     */
    private void computeIshaTime(double latitude, double maghribTime)
    {
        IshaTimeCalculator itc = new IshaTimeCalculator( SalatConstants.ISNA_ANGLES.getIshaTwilightAngle(), sc, latitude, ratioCalc, SalatConstants.ISNA_INTERVALS.getIshaInterval(), maghribTime, noon, night, set, sinDec, cosDec  );
        TimeWrapper t = TimeFormatter.getTime( itc.getIshaTime(), (int)SalatConstants.ISNA_INTERVALS.getIshaInterval() );
        prayerData[TimeCriticalEvent.Isha] = t;
    }


    /**
     * Computes the Maghrib prayer time.
     * @param maghribInterval The Maghrib prayer interval.
     * @return The Maghrib prayer time.
     */
    private double computeMaghribTime(double maghribInterval)
    {
        double maghribTime = sc.getSunset() + sc.getHeightC().getCorrectedWesternHeight() + SalatConstants.SAFETY_TIME; // Magrib= SunSet + Height correction + Safety Time
        TimeWrapper t = TimeFormatter.getTime(maghribTime, (int)maghribInterval );
        prayerData[TimeCriticalEvent.Maghrib] = t;

        return maghribTime;
    }


    /**
     * Computes the time of the sunrise.
     * @return The time of the sunrise.
     */
    private void computeSunrise()
    {
        double sunrise = sc.getSunrise() - sc.getHeightC().getCorrectedEasternHeight(); /* Sunrise - Height correction */
        prayerData[TimeCriticalEvent.Sunrise] = TimeFormatter.getTime(sunrise, 0);
    }


    /**
     * Computes the height value given the ratio.
     * @param cH The ratio used to calculate the correct height value.
     * @return The correct height value.
     */
    public static final double computeH(double cH)
    {
        return MathUtilities.acos(cH) * HeightCorrector.HEIGHT_RATIO;
    }


    /**
     * Gets the act value.
     * @param angleRatio The angle ratio.
     * @param difference The latitude difference.
     * @return The act value.
     */
    private static final double getActValue(double angleRatio, double difference)
    {
        return angleRatio + Math.tan( Math.abs(difference) );
    }


    /**
     * Gets the Asr height value.
     * @param cH The ratio used to calculate the correct height value.
     * @return The correct height value.
     */
    private static final double getAsrH(double cH)
    {
        double H = 3.5;

        if( Math.abs(cH) <= 1.0 )
            H = computeH(cH);

        return H;
    }
}