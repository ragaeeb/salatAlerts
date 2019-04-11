/*
 * @(#)HeightCorrector.java 1.0 2009-06-22
 * @(#)HeightCorrector.java 1.1 2010-04-27
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

import net.rim.device.api.util.MathUtilities;


/**
 * Helps recompute height related values in problematic places.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2010-04-27 This class now has package visibility.
 * @since MaxillionPrayers 1.0
 */
class HeightCorrector
{
    /** The ratio to the height. */
    public static final double HEIGHT_RATIO = 12/Math.PI;

    /** The equatorial radius of the Earth in meters. */
    private static final double EARTH_EQUATORIAL_RADIUS_METERS = 6378.1370*1000; // 6378.137 km

    /** The corrected height for the eastern region. */
    private double correctedEasternHeight;

    /** The corrected height for the western region. */
    private double correctedWesternHeight;


    /**
     * Creates an instance of this class which allows height correction to occur for
     * problematic places.
     */
    public HeightCorrector()
    {
        super();
        this.correctedEasternHeight = 0;
        this.correctedWesternHeight = 0;
    }


    /**
     * Corrects the specified sun declination angles given the region height differences.
     * @param sinDeclination The sin value of the latitudinal angle.
     * @param cosDeclination The cos value of the latitudinal angle.
     * @param westernHeightDifference The place western horizon height difference in meters.
     * @param easternHeightDifference The place eastern horizon height difference in meters.
     */
    public void correct(double sinDeclination, double cosDeclination, double westernHeightDifference, double easternHeightDifference)
    {
        double initialHeight = getCH(SolarCalculatorFormulae.SUNRISE_ARC_ANGLE, sinDeclination, cosDeclination);

        double heightCorrectionWest = getCH( getAngle(westernHeightDifference), sinDeclination, cosDeclination);
        correctedWesternHeight = adjustCorrection(initialHeight, heightCorrectionWest);

        double heightCorrectionEast = getCH( getAngle(easternHeightDifference), sinDeclination, cosDeclination);
        correctedEasternHeight = adjustCorrection(initialHeight, heightCorrectionEast);
    }


    /**
     * Gets the eastern corrected height value.
     * @return The corrected eastern height value.
     */
    public double getCorrectedEasternHeight()
    {
        return correctedEasternHeight;
    }


    /**
     * Gets the western corrected height value.
     * @return The corrected western height value.
     */
    public double getCorrectedWesternHeight()
    {
        return this.correctedWesternHeight;
    }


    /**
     * Calculates the corrected height value given the specified parameters.
     * @param angle The angle value (in radians).
     * @param sinDeclination The sin value of the equatorial latitudinal angle.
     * @param cosDeclination The cos value of the equatorial latitudinal angle.
     * @return The corrected height value given the angle, the sin and cos values of the
     * latitudinal angle.
     */
    public static final double calculateCH(double angle, double sinDeclination, double cosDeclination)
    {
        return ( Math.sin(angle)-sinDeclination ) / cosDeclination;
    }


    /**
     * Determines if height correction is needed given the specified parameters.
     * @param solarCalculationOK Was the solar-calculation non-problematic?
     * @param latitude The latitude of the region.
     * @param westHeightDifference The place western horizon height difference in meters.
     * @param eastHeightDifference The place eastern horizon height difference in meters.
     * @return true if height correction is needed, false otherwise. Height correction is not
     * used for problematic places above 45 degrees.
     */
    public static final boolean correctionNeeded(boolean solarCalculationOK, double latitude, double westHeightDifference, double eastHeightDifference)
    {
        boolean latitudeOK = Math.abs(latitude) < Math.toRadians(45);
        boolean nonZeroHeightDifference = (westHeightDifference != 0) || (eastHeightDifference != 0);

        return solarCalculationOK && latitudeOK && nonZeroHeightDifference;
    }


    /**
     * Adjusts the height value given the specified parameters.
     * @param H0 The initial height.
     * @param regionValue The height difference in meters of the region (eastern or western).
     * @return The corrected height value.
     */
    private static final double adjustCorrection(double H0, double regionValue)
    {
        return (H0-regionValue) * HEIGHT_RATIO;
    }


    /**
     * Gets the angle associated with the specified region.
     * @param region The height difference in meters of the region (eastern or western).
     * @return The angle associated with the specified region (in radians).
     */
    private static final double getAngle(double region)
    {
        double radialValue = MathUtilities.asin( EARTH_EQUATORIAL_RADIUS_METERS / (EARTH_EQUATORIAL_RADIUS_METERS+region) );

        return SolarCalculatorFormulae.SUNRISE_ARC_ANGLE + (0.5*Math.PI - radialValue);
    }


    /**
     * Calculates the corrected height value given the specified parameters.
     * @param angle The angle value (in radians).
     * @param sinDeclination The sin value of the equatorial latitudinal angle.
     * @param cosDeclination The cos value of the equatorial latitudinal angle.
     * @return The corrected height value given the angle, the sin and cos values of the
     * latitudinal angle.
     */
    private static final double getCH(double angle, double sinDeclination, double cosDeclination)
    {
        double result = calculateCH(angle, sinDeclination, cosDeclination);

        return MathUtilities.acos(result);
    }
}