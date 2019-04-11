/*
 * @(#)CalendarConversion.java  1.0 2009-06-22
 * @(#)CalendarConversion.java  1.1 2009-09-15
 * @(#)CalendarConversion.java  1.2 2010-04-27
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


/**
 * Deals with all necessary calendar conversions.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 Methods are no longer static.
 * @version 1.20 2010-04-27 This class now has package visibility and methods are once again static.
 * @since MaxillionPrayers 1.0
 */
class CalendarConversion
{
    /**
     * Calculates the Julian Epoch value of the specified Gregorian calendar. This does not take
     * care of 1582 correction, assumes that the correct Gregorian calendar from the past is being
     * specified.<br><br>
     *
     * In astronomy, an epoch is a specific moment in time for which celestial coordinates or
     * orbital elements are specified, and from which other orbital parametrics are thereafter
     * calculated in order to predict future position. The applied tools of the mathematics
     * disciplines of Celestial mechanics or its subfield Orbital mechanics (both predict
     * orbital paths and positions) about a center of gravity are used to generate an
     * ephemeris (plural: ephemerides; from the Greek word ephemeros = daily) which is a
     * table of values that gives the positions of astronomical objects in the sky at a given
     * time or times, or a formula to calculate such given the proper time offset from the
     * epoch. Such calculations generally result in an elliptical path on a plane defined by
     * some point on the orbit, and the two focii of the ellipse. Viewing from another
     * orbiting body, following its own trace and orbit, creates shifts in three dimensions
     * in the spherical trigonometry used to calculate relative positions. Over time,
     * inexactitudes and other errors accumulate, creating more and greater errors of
     * prediction, so ephemeris factors need recalculated from time to time, and that
     * requires a new epoch to be defined. Different astronomers or groups of astronomers
     * used to define epochs to suit themselves, but these days of speedy communications, the
     * epochs are generally defined in an international agreement, so astronomers world wide
     * can collaborate more effectively. It was inefficient and error prone to translate data
     * observed by one group so other groups could compare information. An example of how
     * this works: if a star's position is measured by someone today, he/she then obtains the
     * change that occurred in the reference frame position since J2000 and corrects the
     * star's position appropriately, yielding the position of the star relative to the
     * reference frame of J2000. It is this J2000 position which is shared with others.<br><br>
     *
     * Therefore, the current epoch, defined by international agreement, is called J2000.0
     * and is precisely defined to be:<br>
     * 1. The Julian date 2451545.0 TT (Terrestrial Time), or January 1, 2000, noon TT.<br>
     * 2. This is equivalent to January 1, 2000, 11:59:27.816 TAI (International Atomic Time) or<br>
     * 3. January 1, 2000, 11:58:55.816 UTC (Coordinated Universal Time).<br><br>
     *
     * A Julian year, named after Julius Caesar (100 BCE — 44 BCE), is a year of exactly
     * 365.25 days. Julian year 2000 began on 2000 January 1 at exactly 12:00 TT. The
     * beginning of Julian years are indicated with prefix 'J' and suffix '.0', for example
     * 'J2000.0' for the beginning of Julian year 2000. Because Julian years have a fixed
     * length, their beginning is far easier to calculate than that of Besselian years.<br><br>
     *
     * The IAU decided at their General Assembly of 1976 that the new standard equinox of
     * J2000.0 should be used starting in 1984. (Before that, the equinox of B1950.0 seems to
     * have been the standard.) If the past is a good guide, then we may expect to switch to
     * J2050.0 in the mid-2030s. Julian epochs are calculated according to:<br>
     * J = 2000.0 + (Julian date - 2451545.0) / 365.25 [1]<br><br>
     *
     * [1] Wikipedia, (2009). Epoch (astronomy). [Online]. Available:
     * http://en.wikipedia.org/wiki/Epoch_(astronomy) [June 20, 2009]
     *
     * @param gc The Gregorian calendar to calculate the Julian epoch value for.
     * @return THe Julian Epoch value of the specified Gregorian Calendar.
     */
    public static double calculateJulianEpoch(Calendar gc)
    {
        int yy = gc.get(Calendar.YEAR);
        int mm = gc.get(Calendar.MONTH)+1;
        int dd = gc.get(Calendar.DAY_OF_MONTH);
        int A, B, m, y;
        double T1,T2,Tr;

        if (mm > 2)
        {
            y = yy;
            m = mm;
        }

        else
        {
            y = yy-1;
            m = mm+12;
        }

        A = y/100;
        B = 2 - A + A/4;
        T1 = ip( 365.25*(y+4716) );
        T2 = ip( 30.6001*(m+1) );
        Tr = T1+T2+dd+B-1524.5;

        return Tr;
    }


    /**
     * Returns the integral portion of the specified floating-point value.
     * @param x The floating-point value to extract the integral portion of.
     * @return The integral portion of the specified floating-point value (ie: If x = 5.2,
     * 5 is returned).
     */
    private static double ip(double x)
    {
        return (int)x;
    }
}