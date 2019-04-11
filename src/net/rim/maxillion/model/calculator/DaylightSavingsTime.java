/*
 * @(#)DaylightSavingsTime.java	1.0	2009-06-22
 * @(#)DaylightSavingsTime.java	1.1	2009-09-15
 * @(#)DaylightSavingsTime.java	1.2	2010-04-27
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


/**
 * Deals with time adjustments due to daylight savings time.<br><br>
 *
 * Daylight saving time is the convention of advancing clocks so that afternoons have more
 * daylight and mornings have less. Typically clocks are adjusted forward one hour near the
 * start of spring and are adjusted backward in autumn. Modern DST was first proposed in 1895
 * by George Vernon Hudson, a New Zealand entomologist. Many countries have used it since
 * then; details vary by location and change occasionally.<br><br>
 *
 * In a typical case where a one-hour shift occurs at 02:00 local time, in spring the clock
 * jumps forward from 02:00 standard time to 03:00 DST and the day has 23 hours, whereas in
 * autumn the clock jumps backward from 02:00 DST to 01:00 standard time, repeating that hour,
 * and the day has 25 hours. A digital display of local time does not read 02:00 exactly, but
 * instead jumps from 01:59:59.9 either forward to 03:00:00.0 or backward to 01:00:00.0. In
 * this example, a location observing UTC+10 during standard time is at UTC+11 during DST;
 * conversely, a location at UTC-10 during standard time is at UTC-9 during DST.<br><br>
 *
 * Clock shifts are usually scheduled near a weekend midnight to lessen disruption to weekday
 * schedules. A one-hour shift is customary, but Australia's Lord Howe Island uses a half-hour
 * shift. Twenty-minute and two-hour shifts have been used in the past.<br><br>
 *
 * Coordination strategies differ when adjacent time zones shift clocks. The European Union
 * shifts all at once, at 01:00 UTC; for example, Eastern European Time is always one hour
 * ahead of Central European Time. Most of North America shifts at 02:00 local time, so
 * its zones do not shift at the same time; for example, Mountain Time can be temporarily
 * either zero or two hours ahead of Pacific Time. Australian districts go even further and
 * do not always agree on start and end dates; for example, in 2008 most DST-observing areas
 * shifted clocks forward on October 5 but Western Australia shifted on October 26.
 *
 * Start and end dates vary with location and year. Since 1996 European Summer Time has been
 * observed from the last Sunday in March to the last Sunday in October; previously the rules
 * were not uniform across the European Union. Starting in 2007, most of the United States
 * and Canada observe DST from the second Sunday in March to the first Sunday in November,
 * almost two-thirds of the year. The 2007 U.S. change was part of the Energy Policy Act
 * of 2005; previously, from 1987 through 2006, the start and end dates were the first Sunday
 * in April and the last Sunday in October, and Congress retains the right to go back to the
 * previous dates now that an energy-consumption study has been done.
 *<br><br>
 * Beginning and ending dates are the reverse in the southern hemisphere. For example,
 * mainland Chile observes DST from the second Saturday in October to the second Saturday in
 * March, with transitions at 24:00 local time. The time difference between the United
 * Kingdom and mainland Chile may therefore be three, four, or five hours, depending on the
 * time of year.<br><br>
 *
 * Western China, Iceland, and other areas skew time zones westward, in effect observing DST
 * year-round without complications from clock shifts. For example, Saskatoon, Saskatchewan,
 * is at 106° 39' W longitude, slightly west of center of the idealized Mountain Time Zone
 * (105° W), but the time in Saskatchewan is Central Standard Time (90° W) year-round, so
 * Saskatoon is always about 67 minutes ahead of mean solar time. Conversely, northeast
 * India and a few other areas skew time zones eastward, in effect observing negative DST.
 * The United Kingdom and Ireland experimented with year-round DST from 1968 to 1971 but
 * abandoned it because of its unpopularity, particularly in northern regions.<br><br>
 *
 * Western France, Spain, and other areas skew time zones and shift clocks, in effect
 * observing DST in winter with an extra hour in summer. For example, Nome, Alaska, is at
 * 165° 24' W longitude, which is just west of center of the idealized Samoa Time Zone
 * (165° W), but Nome observes Alaska Time (135° W) with DST, so it is slightly more than two
 * hours ahead of the sun in winter and three in summer.<br><br>
 *
 * DST is generally not observed near the equator, where sunrise times do not vary enough to
 * justify it. Some countries observe it only in some regions; for example, southern Brazil
 * observes it while equatorial Brazil does not. Only a minority of the world's population
 * uses DST because Asia and Africa generally do not observe it. [1]<br><br>
 *
 * [1] Wikipedia, (2009). Daylight saving time. [Online]. Available:
 * http://en.wikipedia.org/wiki/Daylight_saving_time [June 20, 2009]
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 Unnecessary constants removed. Non-static references removed.
 * @version 1.20 2010-04-27 Static-references added. This class now has package visibility. Removed
 * constructor.
 * @since MaxillionPrayers 1.0
 */
class DaylightSavingsTime
{
    /** Amount of hours to offset for daylight savings. */
    public static final int OFFSET = 1;


    /**
     * Gets the amount to adjust the specified time by.
     * @param d The time to potentially adjust.
     * @param timeZone The time zone that we are dealing with (ie: -5.0).
     * @return The amount to adjust the specified time by, if it falls under the daylight saving
     * time criteria, then the offset is returned, otherwise 0 is returned.
     */
    public static int getDaylightSavingsAdjustmentValue(Date time)
    {
        int value = 0;

        if ( isWithinDST(time) )
            value = OFFSET; // adjust 1 hour

        return value;
    }


    /**
     * 
     */
    private static final long getSpecificSunday(long currentTime, int dstMonth, byte sundayNumber)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date(currentTime) );
        calendar.set(Calendar.MONTH, dstMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        byte sundaysEncountered = 0;
        int current = 1; // current day of month

        while (true)
        {
            calendar.set(Calendar.DAY_OF_MONTH, current);

            if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) // this is a sunday
            {
                sundaysEncountered++;

                if (sundaysEncountered == sundayNumber) // this is the second sunday we encountered
                    break;
            }

            current++;
        }

        return calendar.getTime().getTime();
    }


    /**
     * Determines whether the specified date and timezone falls under the daylight savings time. 
     * @param d The time to potentially adjust.
     * @param timeZone The time zone that we are dealing with (ie: -5.0).
     * @return true If the specified time and timezone is affected by daylight savings time,
     * false otherwise.
     */
    private static boolean isWithinDST(Date currentTime)
    {
        long time = currentTime.getTime();
        long secondSunInMarch = getSpecificSunday( time, Calendar.MARCH, (byte)2 );
        long firstSunInNov = getSpecificSunday( time, Calendar.NOVEMBER, (byte)1 );

        return (time >= secondSunInMarch) && (time < firstSunInNov);
    }
}