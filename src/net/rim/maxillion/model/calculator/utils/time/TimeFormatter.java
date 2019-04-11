/*
 * @(#)TimeFormatter.java   1.0 2009-06-22
 * @(#)TimeFormatter.java   1.1 2009-09-15
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
package net.rim.maxillion.model.calculator.utils.time;




/**
 * Formats a raw prayer time value to a user-friendly one.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2009-09-15 Seconds are now incorporated into the result. Singleton pattern
 * added with non-static changes throughout class. String and DateFormat was added
 * here due to relevancy.
 * @version 1.20 2010-04-27 getTime() now expects a long. Human-friendly getTime(): String
 * method removed.
 * @since MaxillionPrayers 1.0
 */
public class TimeFormatter
{
    /** The maximum integer value that the hour value can take. */
    private static final byte MAX_HOUR_VALUE = 23;
    
       /** The maximum integer value that the minute field can be. */
    private static final byte MAX_MINUTE_VALUE = 59;
    
    /** The total number of hours in a day. */
    public static final byte TOTAL_HOURS_IN_A_DAY = MAX_HOUR_VALUE+1;

    /** The total number of hours in a day. */
    public static final byte TOTAL_MINUTES_IN_AN_HOUR = MAX_MINUTE_VALUE+1;
    
    /** The total number of hours in a day. */
    private static final byte TOTAL_SECONDS_IN_A_MINUTE = TOTAL_MINUTES_IN_AN_HOUR;


    /**
     * Gets the time object associated with the raw time data specified.
     * @param time The raw prayer time value to create a user friendly version of. For
     * example 4:30.00 AM would be 4.5, and 11:30.00 PM would be 23.5.
     * @param intervalAddition The extra amount to add to the minutes.
     * @return The time object associated with the raw time data specified.
     * 
     */
    public static TimeWrapper getTime(double time, int intervalAddition)
    {
        int hour = (int)time;
        int min = (int)( 60*(time-hour) );
        int sec = (int)( 3600.0*( time - hour - min/60.0 ) );

        if ( sec > TOTAL_SECONDS_IN_A_MINUTE/2 )
            min = min + 1; // go to next minute if seconds are more than 30.

        if ( sec == TOTAL_SECONDS_IN_A_MINUTE )
        {
            min++;
            sec = 0;
        }

        sec = Math.abs(sec);
        min = Math.abs(min);
        hour = Math.abs(hour);

        min += intervalAddition;

        while ( min > MAX_MINUTE_VALUE ) // Adjust the minutes. Minutes must be less than 60.
        {
            min = min - TOTAL_MINUTES_IN_AN_HOUR;
            hour++;
        }

        while ( hour > MAX_HOUR_VALUE ) // Adjust the hours. Hours must be less than 24.
            hour = hour - TOTAL_HOURS_IN_A_DAY;

        return new TimeWrapper(hour, min, sec);
    }
}