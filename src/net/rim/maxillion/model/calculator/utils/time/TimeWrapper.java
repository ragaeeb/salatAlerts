/*
 * @(#)TimeWrapper.java 1.0 2010-04-27
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
package net.rim.maxillion.model.calculator.utils.time;

import java.util.Calendar;
import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;


/**
 * Used to wrap a time object so that it is easier to work with and is more friendly to read.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2010-04-27 Initial submission.
 * @since MaxillionPrayers 3.0
 */
public class TimeWrapper
{
    /** Used to display the raw time value in a user-friendly format. */
    private static final DateFormat _timeFormat = new SimpleDateFormat("h:mm a");
    
    /** Used to display the raw time value in a user-friendly format. */
    private static final DateFormat _dateFormat = new SimpleDateFormat("MMM d, yyyy");

    /** The raw time value. */
    private long _time;


    /**
     * Creates an instance of this class to store the specified time.
     * @param hour The hour value.
     * @param minute The minute value.
     * @param second The second value.
     */
    public TimeWrapper(int hour, int minute, int second)
    {
        this( generateLong(hour, minute, second) );
    }

    private static final long generateLong(int hour, int minute, int second)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, minute);

        return c.getTime().getTime();
    }


    /**
     * Creates an instance of this class to hold the specified raw time value.
     * @param time
     */
    public TimeWrapper(long time)
    {
        _time = time;
    }


    /**
     * Gets the raw time value of this object.
     * @return The raw time value stored.
     */
    public long getTime()
    {
        return _time;
    }
    
    
    public String getDate()
    {
        return _dateFormat.formatLocal(_time);
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return _timeFormat.formatLocal(_time);
    }
}