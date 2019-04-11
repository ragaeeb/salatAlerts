/*
 * @(#)AngleParameters.java 1.0 2009-06-22
 * @(#)AngleParameters.java 1.1 2009-09-15
 * @(#)AngleParameters.java 1.2 2010-04-27
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

import net.rim.maxillion.model.calculator.utils.time.TimeFormatter;


/**
 * Maintains the angle information necessary to perform the prayer time calculations.
 * Earth's atmosphere scatters sun light. In the absence of atmosphere on earth there would be
 * no sunlight after sunset and before sunrise. So, Twilight causes gradual decrease of
 * sunlight. Twilight angle is the angle between line reaching sun's center and the horizon.
 * Sun's center must be below it. Greater twilight angle leads to earlier Fajr and later Isha.<br><br>
 *
 * To imagine twilight go to seaside at sunset and observe the sun gradually disappears, when
 * you see the center of the sun at the horizon level , then the twilight angle is just near 0.
 * Now the sun disappears and still sunlight is observable, that's twilight. Twilight ends
 * when no sunlight is seen. The same can be done at sunrise. [1]<br><br>
 *
 * Civil twilight: roughly equivalent to lighting up time. The brightest stars are visible
 * and at sea the horizon is clearly visible.<br><br>
 *
 * Nautical twilight - the horizon at sea ceases to be clearly visible and it is impossible
 * to determine altitudes with reference to the horizon.<br><br>
 *
 * Astronomical twilight- when it is truly dark and no perceptible twilight remains. [2]<br><br> 
 *
 * [1] Mohab, Mohamed, (2005). Calculating Muslims' Prayer Times. [Online]. Available:
 * http://mohamed-mohab.blogspot.com/2008/01/calculating-muslims-prayers-times.html [June 21, 2009]<br>
 * [2] Ahmed, Monzur, (1997). The Determination of Salat Times. [Online]. Available:
 * http://www.ummah.net/astronomy/saltime/ [June 21, 2009]<br>
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 Updated to deal with design changes in TimeFormatter class.
 * @version 1.20 2010-04-27 Updated to comply with design changes to TimeFormatter class.
 * @since MaxillionPrayers 1.0
 */
public class AngleParameters
{
    /** The Fajr twilight angle. Fajr starts with the morning twilight (dawn). */
    private double fajrTwilightAngle;

    /** The Isha twilight angle. Isha starts at the end of the evening twilight (dusk). */
    private double ishaTwilightAngle;


    /**
     * Creates an instance of this class so that twilight angles may be recorded for prayer
     * time calculation.
     * @param fajrTwilightAngle The Fajr prayer twilight angle in degrees.
     * @param ishaTwilightAngle The Isha prayer twilight angle.
     */
    public AngleParameters(double fajrTwilightAngle, double ishaTwilightAngle)
    {
        super();
        this.fajrTwilightAngle = Math.toRadians(fajrTwilightAngle);
        this.ishaTwilightAngle = Math.toRadians(ishaTwilightAngle);
    }


    /**
     * Creates an instance of this class so that twilight angles may be recorded for prayer
     * time calculation.
     * @param fajrTwilightAngle The Fajr prayer twilight angle in degrees.
     * @param ip The interval parameters that need to be modified of its interval.
     */
    public AngleParameters(double fajrTwilightAngle, IntervalParameters ip)
    {
        this(fajrTwilightAngle, 0);
        ip.setIshaInterval( ip.getIshaInterval()/TimeFormatter.TOTAL_MINUTES_IN_AN_HOUR ); // convert minute to hours
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        boolean result = false;

        try {
            AngleParameters gp = (AngleParameters)obj;
            result = (fajrTwilightAngle == gp.fajrTwilightAngle) && (ishaTwilightAngle == gp.ishaTwilightAngle);
        }

        catch (ClassCastException ex)
        {
        }

        return result;
    }


    /**
     * Gets the Fajr twilight angle value.
     * @return The Fajr twilight angle value in radians.
     */
    public double getFajrTwilightAngle()
    {
        return this.fajrTwilightAngle;
    }


    /**
     * Gets the Isha twilight angle value.
     * @return The Isha twilight angle value in radians.
     */
    public double getIshaTwilightAngle()
    {
        return this.ishaTwilightAngle;
    }
}