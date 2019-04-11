/*
 * @(#)Calculator.java  1.0 2010-04-27
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

import java.util.Date;
import net.rim.maxillion.model.calculator.utils.GeoParameters;
import net.rim.maxillion.model.calculator.utils.time.TimeWrapper;


/**
 * A method of calculating salat-times. This interface was introduced so that there may
 * be a choice of how the times are calculated (through the system or simply retrieved
 * from a third-party website).
 *
 * @author Ragaeeb Haq
 * @version 1.00 2010-04-27 Initial submission.
 * @since MaxillionPrayers 3.0
 */
public abstract class Calculator
{
    private static Calculator instance;

    /**
     * Calculates the prayer times for the specified geographical parameters and date.
     * @param g The geographical coordinates of the location to calculate the prayer times for.
     * @param requestedDate The specific date to calculate the prayer times for.
     * @return The prayer times for the requested geographical coordinates and specified date.
     */
    public abstract TimeWrapper[] calculate(GeoParameters g, Date requestedDate);


    public static final Calculator getInstance()
    {
        if (instance == null)
            instance = new CalculatorImpl();

        return instance;
    }
}