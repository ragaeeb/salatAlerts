/*
 * @(#)TimeCriticalEvent.java	1.0	2010-04-22
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


/**
 * Any Islamic event that is time-critical. Examples of these events are salat-times
 * as well as the sunrise, and the half-night.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2010-04-22 Initial submission.
 * @since MaxillionPrayers 3.0
 */
public interface TimeCriticalEvent
{
    public static final byte Fajr = 0;

    public static final byte Dhuhr = 1;

    public static final byte Asr = 2;

    public static final byte Maghrib = 3;

    public static final byte Isha = 4;

    /** The time the sun rises for the day. This is significant to mark the end of the Fajr
     * prayer. */
    public static final byte Sunrise = 5;

    /** The time achieved when the night is divided in half (midpoint between sunset and
     * >subh-saadiq. This is significant to mark the recommended end time of the Isha prayer [1].<br?
     * <br>
     * [1] Wikipedia, (2009). Fajr. [Online]. Available:
     * http://iskandrani.wordpress.com/2008/06/29/time-limit-for-the-isha-prayer [May 15, 2010]
     * */
    public static final byte HalfNight = 6;
}