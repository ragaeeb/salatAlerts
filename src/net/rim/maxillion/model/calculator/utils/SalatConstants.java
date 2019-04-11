/*
 * @(#)SalatConstants.java  1.0 2009-06-22
 * @(#)SalatConstants.java  1.1 2010-04-27
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
 * Constants that are used during the prayer time calculation.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-06-22 Initial submission.
 * @version 1.10 2010-04-27 This is now an enum class.
 * @since MaxillionPrayers 1.0
 *
 */
public interface SalatConstants
{
    /** The eastern height difference. */
    public static final byte HEIGHT_DIFFERENCE_EAST = 0;

    /** The western height difference. */
    public static final byte HEIGHT_DIFFERENCE_WEST = 0;

    /** The reference angle as suggested by Rabita. */
    public static final double RABITA_REFERENCE_ANGLE = Math.toRadians(45);

    /** Safety time used to give some room for error handling. */
    public static final double SAFETY_TIME = 0.016389; //(59/TimeFormatter.TOTAL_SECONDS_IN_MINUTE)/TimeFormatter.TOTAL_MINUTES_IN_HOUR; // but times are off with IslamicFinder

    /** The ratio of the length of the object to its shadow at noon. The standard convention for the Asr prayer time calculation used by Imamas Shafii,
     * Hanbali and Maliki. */
    public static final byte SHAFII_ASR_JURISTIC_SHADOW_RATIO = 1;


    /**
     * Represents the Islamic Society of North America methodology of calculating prayer times.
     * The Islamic Society of North America (ISNA), based in Plainfield, Indiana, USA, is a
     * Muslim umbrella group that describes itself as the largest Muslim organization in North
     * America.<br><br>
     *
     * The vision of ISNA is "to be an exemplary and unifying Islamic organization in North
     * America that contributes to the betterment of the Muslim community and society at large."
     * ISNA is an association of Muslim organizations and individuals that provides a common
     * platform for presenting Islam, supporting Muslim communities, developing educational,
     * social and outreach programs and fostering good relations with other religious communities,
     * and civic and service organizations. ISNA's annual convention is generally the largest
     * gathering of American Muslims in the United States. Islamic Horizons is the bi-monthly
     * publication of ISNA. [1]<br>
     *<br>
     * [1] Wikipedia, (2009). Islamic Society of North America. [Online]. Available:
     * http://en.wikipedia.org/wiki/Islamic_Society_of_North_America [June 20, 2009]
     */
    public static final AngleParameters ISNA_ANGLES = new AngleParameters(15,15);

    public static final IntervalParameters ISNA_INTERVALS = new IntervalParameters(0);
}