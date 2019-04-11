/*
 * @(#)SolarCalculatorFormulae.java 1.0 2009-06-22
 * @(#)SolarCalculatorFormulae.java 1.1 2009-09-15
 * @(#)SolarCalculatorFormulae.java 1.2 2010-04-27
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
 * Responsible for all the mathematical operations and formulae done to calculate solar time
 * and position-related data. Nearly all the formulae used in this class is from the the
 * "Illustrating Shadows" document. [4]<br><br>
 *
 * [4] Smith, Simon W, (2005). Illustrating Shadows. [Online]. Available:
 * http://www.illustratingshadows.com/www-formulae-collection.pdf [June 21, 2009]<br>
 * [12] Mohab, Mohamed, (2005). Calculating Muslims' Prayer Times. [Online]. Available:
 * http://mohamed-mohab.blogspot.com/2008/01/calculating-muslims-prayers-times.html [June 21, 2009]
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 Enabled constructor and turned all methods to non-static.
 * @version 1.20 2010-04-27 All operations are once again all static. This class now has package visibility.
 * @since MaxillionPrayers 1.0
 */
class SolarCalculatorFormulae
{
    /** The solar elevation angle. */
    public static final double SUNRISE_ARC_ANGLE = Math.toRadians(-5.0/6.0);
    
    private static final double MAX_DEGREES = 360;


    /**
     * Calculates the number of centuries since the year 2000. [3]<br><br>
     *
     * [3] Astronomical Applications Department, (2009). Approximate Sidereal Time. [Online].
     * Available: http://aa.usno.navy.mil/faq/docs/GAST.php) [June 21, 2009]<br>
     *
     * @param julianDate The Julian date of the previous midnight (Universal Time) (the value will
     * end in 0.5 exactly).
     * @param timeZone Hours of UT elapsed since the given Julian date.
     * @return The number of centuries that have occurred since the year 2000.
     */
    public static final double calculateCenturiesSince2000(double julianDate, double timeZone)
    {
        double jdTimeOfInterest = julianDate + timeZone/24.0;

        return (jdTimeOfInterest - 2451545.0) / 36525.0; // form time in Julian centuries from 1900.0
    }


    /**
     * Calculates the ratio used to calculate the height above sea level. The used formula is
     * defined in the "Easy PC Astronomy" book (p.38). [2][12]
     * @param latitude The latitude of the region (in radians).
     * @param decl The declination value of the equatorial coordinates.
     * @return The ratio given the specified latitude and declination value.
     */
    public static final double calculateCH(double latitude, double decl)
    {
        double T1 = ( Math.sin(SUNRISE_ARC_ANGLE) - Math.sin(decl)*Math.sin(latitude) ); // divisor
        double T2 = ( Math.cos(decl)*Math.cos(latitude) ); // divisor: Hour angle for the Sun

        return T1/T2;
    }


    /**
     * Calculates the Earth eccentricity given the number of centuries since 2000.<br><br>
     *
     * In astrodynamics, under standard assumptions, any orbit must be of conic section shape.
     * The eccentricity of this conic section, the orbit's eccentricity, is an important
     * parameter of the orbit that defines its absolute shape. Eccentricity may be interpreted
     * as a measure of how much this shape deviates from a circle.<br><br>
     *
     * The eccentricity of the Earth's orbit is currently about 0.0167. Over thousands of
     * years, the eccentricity of the Earth's orbit varies from nearly 0.0034 to almost 0.058
     * as a result of gravitational attractions among the planets.<br><br>
     *
     * Orbital mechanics require that the duration of the seasons be proportional to the area
     * of the Earth's orbit swept between the solstices and equinoxes, so when the orbital
     * eccentricity is extreme, the seasons that occur on the far side of the orbit (aphelion)
     * can be substantially longer in duration. Today, northern hemisphere fall and winter
     * occur at closest approach (perihelion), when the earth is moving at its maximum
     * velocity. As a result, in the northern hemisphere, fall and winter are slightly
     * shorter than spring and summer. In 2006, summer was 4.66 days longer than winter and
     * spring is 2.9 days longer than fall. Axial precession slowly changes
     * the place in the Earth's orbit where the solstices and equinoxes occur. Over the next
     * 10,000 years, northern hemisphere winters will become gradually longer and summers will
     * become shorter. Any cooling effect, however, will be counteracted by the fact that the
     * eccentricity of Earth's orbit will be almost halved, reducing the mean orbital radius
     * and raising temperatures in both hemispheres closer to the mid-interglacial peak. [5]<br><br>
     *
     * [5] Wikipedia, (2009). Orbital eccentricity. [Online]. Available:
     * http://en.wikipedia.org/wiki/Orbital_eccentricity [June 21, 2009]
     *
     * @param T The number of centuries since January 1, 2000 at 12 UT.
     * @return The Earth eccentricity.
     */
    public static final double calculateEarthEccentricity(double T)
    {
        return 0.01675104 - 418E-7*T - 126E-9*( MathUtilities.pow(T,2) );
    }


    /**
     * Calculates the obliquity of the ecliptic with the expression of Newcomb.<br><br>
     *
     * In astronomy, axial tilt is the inclination angle of a planet's rotational axis in
     * relation to its orbital plane. It is also called axial inclination or obliquity. The
     * axial tilt is expressed as the angle made by the planet's axis and a line drawn through
     * the planet's center perpendicular to the orbital plane.
     *
     * The Earth currently has an axial tilt of about 23.37°. The axis remains tilted in the
     * same direction throughout a year; however, as the Earth orbits the Sun, the hemisphere
     * (half part of earth) tilted away from the Sun will gradually become tilted towards the
     * Sun, and vice versa. This effect is the main cause of the seasons (see effect of sun
     * angle on climate). Whichever hemisphere is currently tilted toward the Sun experiences
     * more hours of sunlight each day, and the sunlight at midday also strikes the ground at
     * an angle nearer the vertical and thus delivers more energy per unit surface area. [6]<br>,<br>
     *
     * [6] Wikipedia, (2009). Axial tilt. [Online]. Available:
     * http://en.wikipedia.org/wiki/Obliquity_of_the_ecliptic [June 21, 2009]
     *
     * @param T The number of centuries since January 1, 2000 at 12 UT.
     * @return The ecliptic obliquity value.
     */
    public static final double calculateEclipticObliquity(double T)
    {
        double ec = 23.452294 - 0.0130125*T - 164E-8*( MathUtilities.pow(T,2) ) + 503E-9*( MathUtilities.pow(T,3) );

        return Math.toRadians(ec);
    }


    /**
     * Calculates the equation of time given the specified parameters.<br><br>
     *
     * The equation of time is the difference between apparent solar time and mean solar time,
     * both taken at a given place (or at another place with the same geographical longitude)
     * at the same real instant of time.<br><br>
     *
     * Apparent (or true) solar time can be obtained for example by measurement of the current
     * position (hour angle) of the Sun, or indicated (with limited accuracy) by a sundial.
     * Mean solar time, for the same place, would be the time indicated by a steady clock set
     * so that its differences over the year from apparent solar time average to zero (with
     * zero net gain or loss over the year).<br><br>
     *
     * The equation of time varies over the course of a year, in way that is almost exactly
     * reproduced from one year to the next. Apparent time, and the sundial, can be ahead
     * (fast) by as much as 16 min 33 s (around November 3), or behind (slow) by as much as
     * 14 min 6 s (around February 12).<br><br>
     *
     * The equation of time results from two different superposed astronomical causes, each
     * causing a different non-uniformity in the apparent daily motion of the Sun relative to
     * the stars, and contributing a part of the effect:<br>
     * -the obliquity of the ecliptic (the plane of the Earth's annual orbital motion around
     *  the Sun), which is inclined by about 23.44 degrees relative to the plane of the Earth's
     *  equator; and<br>
     * -the eccentricity and elliptical form of the Earth's orbit around the Sun.<br><br>
     *
     * The equation of time is also the east or west component of the analemma, a curve
     * representing the angular offset of the Sun from its mean position on the celestial
     * sphere as viewed from Earth.<br><br>
     *
     * The equation of time was used historically to set clocks. Between the invention of
     * accurate clocks in 1656 and the advent of commercial time distribution services around
     * 1900, one of two common land-based ways to set clocks was by observing the passage of
     * the sun across the local meridian at noon. The moment the sun passed overhead, the
     * clock was set to noon, offset by the number of minutes given by the equation of time
     * for that date. (The second method did not use the equation of time, it used stellar
     * observations to give sidereal time, in combination with the relation between sidereal
     * time and solar time.) The equation of time values for each day of the year,
     * compiled by astronomical observatories, were widely listed in almanacs and ephemerides. [7]<br><br>
     *
     * [7] Wikipedia, (2009). Equation of time. [Online]. Available:
     * http://en.wikipedia.org/wiki/Equation_of_time [June 21, 2009] 
     *
     * @param y The Y value.
     * @param L The geometric mean longitude of the sun.
     * @param M The sun mean anomaly.
     * @param earthEccentricity The Earth eccentricity value. 
     * @return The equation of time.
     */
    public static final double calculateEquationOfTime(double y, double L, double M, double earthEccentricity)
    {
        return y*Math.sin(2*L) - 2*earthEccentricity*Math.sin(M) + 4*earthEccentricity*y*Math.sin(M)*Math.cos(2*L) - 0.5*y*y*Math.sin(4*L) - 5*0.25*earthEccentricity*earthEccentricity*Math.sin(2*M);
    }


    /**
     * Calculates Euler's constant given the specified parameters. The mathematical constant e
     * is the unique real number such that the area above the x-axis and below the curve y=1/x
     * for 1 <= x <= e is exactly 1. It turns out that, consequently, the area for 1 <= x <= et is
     * t. Also, the function ex has the same value as the slope of the tangent line, for all
     * values of x. More generally, the only functions equal to their own derivatives are
     * of the form Cex, where C is a constant. The function ex so defined is called the
     * exponential function, and its inverse is the natural logarithm, or logarithm to base e.
     * The number e is also commonly defined as the base of the natural logarithm (using an
     * integral to define the latter), as the limit of a certain sequence, or as the sum of a
     * certain series (see alternative characterizations below).<br><br>
     *
     * The number e is one of the most important numbers in mathematics, alongside the
     * additive and multiplicative identities 0 and 1, the constant pi, and the imaginary unit
     * i. (All five of these constants together comprise Euler's identity.) The number e is
     * sometimes called Euler's number after the Swiss mathematician Leonhard Euler. The
     * number e is irrational; it is not a ratio of integers (root of a linear polynomial).
     * Furthermore, it is transcendental; it is not a root of any polynomial with integer
     * coefficients. [1]<br><br>
     *
     * The used formula is defined in the "Easy PC Astronomy" book (p.91). [2]<br><br>
     *
     * [1] Wikipedia, (2009). e (mathematical constant). [Online]. Available:
     * http://en.wikipedia.org/wiki/E_(mathematical_constant) [June 21, 2009]<br>
     * [2] Smith, Peter D. (1996). Easy PC Astronomy. Cambridge: Press Syndicate of the
     * University of Cambridge.
     *
     * @param M The sun mean anomaly.
     * @param earthEccentricity The Earth eccentricity.
     * @return The calculated Euler constant given the specified sun mean anomaly and
     * Earth eccentricity.
     */
    public static final double calculateEuler(double M, double earthEccentricity)
    {
        double dt = 1;
        double euler = M;

        while( Math.abs(dt) > 1e-9 )
        {
            dt = euler - earthEccentricity*Math.sin(euler) - M;
            double dE = dt /( 1 - earthEccentricity*Math.cos(euler) );
            euler = euler-dE;
        }

        return euler;
    }


    /**
     * Calculates the height value given the specified parameters. [12]
     * @param cH The cH value to base the height calculation on.
     * @return The height above sea leavel in meters.
     */
    public static final double calculateH(double cH)
    {
        return MathUtilities.acos(cH)*(12/Math.PI);
    }


    /**
     * Determines the number of hours that have passed given the specified equation of time.
     * @param eot The equation of time as calculated.
     * @return The sidereal time at Greenwich measured in hours.
     */
    public static final double calculateHours(double eot)
    {
        eot /= 15;

        return Math.toDegrees(eot);
    }


    /**
     * Calculates the noon time (which is when the sun reaches its highest point: the zenith).
     * This is the point in the sun's path at which it is on the local meridian. [8]
     *
     * [8] Farlex Inc., (2009). noon. [Online]. Available:
     * http://www.thefreedictionary.com/noon [June 21, 2009]
     *
     * @param obsLongitude The observer's longitude value (in radians).
     * @param UT The universal time (in hours).
     * @param tz The timezone of the region.
     * @return The noon-time for the region given the specified parameters.
     */
    public static final double calculateNoonTime(double obsLongitude, double UT, double tz)
    {
        return 12 - UT - tz + obsLongitude*(12.0/Math.PI);
    }


    /**
     * Calculates the sun mean anomaly given the specified centuries passed. The mean anomaly
     * is the angle between lines drawn from the Sun to the perihelion B and to a point
     * (not shown) moving in the orbit at a uniform rate corresponding to the period of
     * revolution of the planet. [9] The anomaly is the angular difference between a mean
     * circular orbit and the true elliptic orbit. [10]<br><br>
     *
     * [9] Encyclopedia Britannica Inc., (2009). anomaly. [Online]. Available:
     * http://www.britannica.com/EBchecked/topic/26578/anomaly#ref105659 [June 21, 2009]
     * [10] Tomezzoli, Vanni, (2001). How to find the times of sunrise and sunset. [Online]. Available:
     * http://xoomer.virgilio.it/vtomezzo/sunriset/formulas/algorythms.html [June 21, 2009] 
     *
     * @param T The number of centuries since January 1, 2000 at 12 UT.
     * @return The sun mean anomaly in radians.
     */
    public static final double calculateSunMeanAnomaly(double T)
    {
        double M = 358.47583 + 35999.04975*T - 15E-5*( MathUtilities.pow(T,2) ) - 33E-7*( MathUtilities.pow(T,3) );

        return convertToCorrectRadians(M);
    }


    /**
     * Calculates the Geometric mean longitude of the sun measured from the vernal equinox.<br><br>
     *
     * The direction to the equinox at a particular epoch, with the effect of nutation
     * subtracted. The mean equinox therefore moves smoothly across the sky due to precession
     * alone, without short-term oscillations due to nutation. [11]<br><br>
     *
     * In astrodynamics or celestial dynamics mean longitude is the longitude at which an
     * orbiting body could be found if its orbit were circular and its inclination were zero.
     * The mean longitude changes at a constant rate over time. The only times when it is
     * equal to the true longitude are at periapsis and apoapsis. [12]<br><br>
     *
     * [11] HighBeam Research, Inc., (2009). mean equinox. [Online]. Available:
     * http://www.encyclopedia.com/doc/1O80-meanequinox.html [June 21, 2009]<br>
     * [12] Wikipedia, (2009). Mean longitude. [Online]. Available:
     * http://en.wikipedia.org/wiki/Mean_longitude [June 21, 2009] 
     *
     * @param T The number of centuries since January 1, 2000 at 12 UT.
     * @return The geometric mean longitude of the sun measured from the vernal equinox.
     */
    public static final double calculateSunMeanLongitude(double T)
    {
        double L = 279.6966778 + (36000.76892*T) + ( 0.0003025*( MathUtilities.pow(T,2) ) ); // the periodic oscillation observed in the precession of the earth's axis and the precession of the equinoxes. The following is also used: L0 = 280.46646 + 36000.76983 T + 0.0003032 T2

        return convertToCorrectRadians(L);
    }


    /**
     * Calculates the theta value given the specified parameters.
     * @param L The sun mean longitude.
     * @param v The v value as calculated.
     * @param M The sun mean anomaly.
     * @return The theta value as calculated.
     */
    public static final double calculateTheta(double L, double v, double M)
    {
        return L+v-M;
    }


    /**
     * Calculates the V value given the specified parameters.
     * @param earthEccentricity The Earth eccentricity.
     * @param eclipticObliquity The ecliptic obliquity.
     * @return The V value.
     */
    public static final double calculateV(double earthEccentricity, double eclipticObliquity)
    {
        double x = Math.sqrt( (1+earthEccentricity) / (1-earthEccentricity) );
        double tnv = x * Math.tan(0.5*eclipticObliquity);

        return 2*MathUtilities.atan(tnv);
    }


    /**
     * Calculates the Y value given the ecliptic obliquity.
     * @param obliq The ecliptic obliquity.
     * @return The Y value associated with the ecliptic obliquity.
     */
    public static final double calculateY(double obliq)
    {
        double y = Math.tan(obliq*0.5);

        return MathUtilities.pow(y, 2);
    }


    /**
     * Calculates the equatorial coordinates given the ecliptic coordinates.
     * @param c The ecliptic coordinates in in radians (where the x-coordinate is beta and the y-coordinate is lamda).
     * @return The equatorial coordinates of the given ecliptic coordinates (where the x-coordinate of
     * the result is the declination, and the y-coordinate is the right-ascension).
     */
    public static final double[] getEquatorialCoordinates(double[] c)
    {
        double epsilonRadians = Math.toRadians(23.439281); // is the Earth's axial tilt in radians

        double beta = c[0]; // The latitudinal angle is called the ecliptic latitude or celestial latitude (denoted beta) measured positive towards the north.
        double lamda = c[1]; // The longitudinal angle is called the ecliptic longitude or celestial longitiude (denoted lamda), measured eastwards from 0° to 360°

        double sinDelta = Math.sin(beta)*Math.cos(epsilonRadians) + Math.cos(beta)*Math.sin(epsilonRadians)*Math.sin(lamda);
        double deltaR = MathUtilities.asin(sinDelta);

        double y = Math.sin(lamda)*Math.cos(epsilonRadians) - Math.tan(beta)*Math.sin(epsilonRadians);
        double x = Math.cos(lamda);
        double alpha = atanxy(x, y);

        c[0] = deltaR;
        c[1] = alpha;

        return c;
    }

    /**
     * Gets the angle between the two points.
     * @param x The first point.
     * @param y The second point.
     * @return The atanxy angle value between the two points.
     */
    private static final double atanxy(double x, double y)
    {
        double argm;

        if (x == 0)
            argm = 0.5*Math.PI;

        else
            argm = MathUtilities.atan(y/x);

        if ( (x > 0) && (y < 0) )
            argm = 2.0*Math.PI + argm;

        if (x < 0)
            argm = Math.PI + argm;

        return argm;
    }


    /**
     * Converts the specified degrees value to the correct radians. This means that if a value
     * larger than 360 degrees is given, it is scaled until it is less than or equal to 360
     * degrees and then converted to radians.
     * @param L The value in degrees to convert to the correct radians.
     * @return The corrected radians value of the specified degrees.
     */
    private static double convertToCorrectRadians(double L)
    {
        L -= (int)(L/MAX_DEGREES) * MAX_DEGREES;

        if (L < 0)
            L += MAX_DEGREES;

        return Math.toRadians(L);
    }
}