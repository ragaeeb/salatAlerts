/*
 * TimeCriticalEventNames.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2010-2010
 */
package net.rim.maxillion.model.calculator.utils;

import net.rim.device.api.util.IntHashtable;

/**
 * 
 *
 * @author Ragaeeb Haq
 * @version 1.00 Aug 28, 2010 Initial submission.
 * @since MaxillionPrayersME 
 *
 */
public class TimeCriticalEventNames
{
    private IntHashtable _table;

    private static TimeCriticalEventNames instance;

    /**
     * 
     */
    private TimeCriticalEventNames()
    {
        _table = new IntHashtable();

        _table.put(TimeCriticalEvent.Fajr, "Fajr");
        _table.put(TimeCriticalEvent.Sunrise, "Sunrise");
        _table.put(TimeCriticalEvent.Dhuhr, "Dhuhr");
        _table.put(TimeCriticalEvent.Asr, "Asr");
        _table.put(TimeCriticalEvent.Maghrib, "Maghrib");
        _table.put(TimeCriticalEvent.Isha, "Isha");
        _table.put(TimeCriticalEvent.HalfNight, "HalfNight");
    }


    public String getName(int event)
    {
        return (String)_table.get(event);
    }


    public static TimeCriticalEventNames getInstance()
    {
        if (instance == null)
            instance = new TimeCriticalEventNames();

        return instance;
    }
}