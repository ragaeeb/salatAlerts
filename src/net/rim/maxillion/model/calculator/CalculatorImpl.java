/*
 * @(#)CalculatorImpl.java  1.0 2009-06-22
 * @(#)CalculatorImpl.java  1.1 2009-09-15
 * @(#)CalculatorImpl.java  1.2 2010-04-27
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
package net.rim.maxillion.model.calculator;
import java.util.Calendar;
import java.util.Date;
import net.rim.device.api.util.DateTimeUtilities;
import net.rim.maxillion.model.calculator.utils.GeoParameters;
import net.rim.maxillion.model.calculator.utils.TimeCriticalEvent;
import net.rim.maxillion.model.calculator.utils.time.TimeWrapper;


/**
 * Calculates the prayer times for a given geographic location and date. Salat times refers to
 * times when Muslims perform prayers (salat). The term is primarily used for the five daily
 * prayers plus the Friday TimeCriticalEvent. According to Muslim beliefs, the salat times were taught
 * by Allah through Gabriel to Muhammad.<br><br>
 *
 * Prayer times are standard for Muslims in the world, especially the fard prayer times. It
 * used by the condition of Sun and geography. There are varying opinions regarding the exact
 * salat times, the schools of Islamic thought differing in minor details. All schools agree
 * that any given prayer cannot be performed before its stipulated time.<br><br>
 *
 * The Fajr prayer starts with the rise of "white light" (fajar sadiq) in the east, and lasts
 * until sunrise. The Dhuhr prayer starts after the sun passes its zenith, and lasts until Asr.
 * The Asr prayer starts when the shadow of an object is the same length as the object itself
 * (or, according to Hanafi fiqh, twice its length) and lasts till sunset. Asr can be split
 * into two sections; the preferred time is before the sun starts to turn orange, while the
 * time of necessity (dharoori waqt) is from when the sun turns orange until sunset. The
 * Maghrib prayer begins when the sun sets, and lasts till the red light has left the sky in
 * the west. The Isha'a prayer starts when the red light is gone from the western sky, and
 * lasts until the rise of the "white light" (fajar sadiq) in the east. The preferred time
 * for Isha'a is before midnight, meaning halfway between sunset and sunrise. [1]<br><br>
 *
 * [1] Wikipedia, (2009). Salat Times. [Online]. Available:
 * http://en.wikipedia.org/wiki/Salat_times [June 20, 2009]
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 No longer dependent on the calculation methods to perform the calculation.
 * Removed adjustable minutes. Added default conventions and static modifiers for them.
 * @version 1.20 2010-04-27 Added Isha end-calculation feature. Static-references added. This class now deals with
 * time-critical event objects instead of Prayer objects. Minute adjuster is now a static instance variable.
 * @since MaxillionPrayers 1.0
 */
class CalculatorImpl extends Calculator
{
    /* (non-Javadoc)
     * @see model.calculator.Calculator#calculate(model.GeoParameters, java.util.Date)
     */
    public TimeWrapper[] calculate(GeoParameters g, Date requestedDate)
    {
        int dstAdjust = DaylightSavingsTime.getDaylightSavingsAdjustmentValue(requestedDate);
        Calendar gc = Calendar.getInstance();
        gc.setTime(requestedDate);

        TimeWrapper[] today = performCalculation(g, dstAdjust, gc);
        calculateIshaEnd(g, gc, today, dstAdjust);

        return today;
    }


    /**
     * Calculates the half-night value that marks the recommended end-time of the Isha TimeCriticalEvent. [2]
     * [2] Albalagh, (2004). When Does Isha Time End? [Online]. Available:
     * http://www.albalagh.net/qa/0056.shtml [April 26, 2010]
     * @since MaxillionPrayers 3.0
     */
    private void calculateIshaEnd(GeoParameters g, Calendar gc, TimeWrapper[] today, int dstAdjust)
    {
        gc = DateTimeUtilities.getNextDate(DateTimeUtilities.ONEDAY); // find out the prayer times for tomorrow to calculate tomorrow's Fajr time
        TimeWrapper[] tomorrow = performCalculation(g, dstAdjust, gc);

        // maghrib
        Calendar maghrib = Calendar.getInstance();
        maghrib.setTime( new Date( today[TimeCriticalEvent.Maghrib].getTime() ) );

        // fajr
        gc.setTime( new Date( tomorrow[TimeCriticalEvent.Fajr].getTime() ) );

        // now do fajr-maghrib / 2
        int sum = maghrib.get(Calendar.HOUR_OF_DAY)+gc.get(Calendar.HOUR_OF_DAY); // 8+4
        maghrib.set(Calendar.HOUR_OF_DAY, sum);

        sum = maghrib.get(Calendar.MINUTE)+gc.get(Calendar.MINUTE);
        maghrib.set(Calendar.MINUTE, sum);

        sum = maghrib.get(Calendar.SECOND)+gc.get(Calendar.SECOND);
        maghrib.set(Calendar.SECOND, sum);

        today[TimeCriticalEvent.HalfNight] = new TimeWrapper( maghrib.getTime().getTime() );
    }


    /**
     * Calculates the prayer times for the specified geographical parameters and date.
     * @param g The geographical coordinates of the location to calculate the prayer times for.
     * @param gc The specific date to calculate the prayer times for.
     * @param dstAdjust The hour(s) to adjust the hour-value by due to daylight savings.
     * @return The prayer times for the requested geographical coordinates and specified date.
     */
    private TimeWrapper[] performCalculation(GeoParameters g, int dstAdjust, Calendar gc)
    {
        SolarCalculator sc = new SolarCalculator(); // Performs all the solar calculations including any math needed to do with the position of the sun.
        boolean problematic = sc.calculate(gc, g, dstAdjust); // perform initial calculation

        return new IslamicEventAdjustedTimes(problematic, sc, g, gc).getPrayerData();
    }
}