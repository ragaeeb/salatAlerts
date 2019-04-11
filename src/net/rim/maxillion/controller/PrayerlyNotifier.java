/*
 * PrayerlyNotifier.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2010-2010
 */
package net.rim.maxillion.controller;

import net.rim.maxillion.model.calculator.utils.TimeCriticalEvent;
import net.rim.maxillion.model.calculator.utils.time.TimeWrapper;
import net.rim.maxillion.subscription.controller.DatabaseContainer;

/**
 * 
 *
 * @author Ragaeeb Haq
 * @version 1.00 Sep 2, 2010 Initial submission.
 * @since salatAlerts 
 *
 */
class PrayerlyNotifier extends Notifier
{
    private static final long ID = 0x81ae718ff9e5f074L;

    /**
     * @param id
     */
    protected PrayerlyNotifier()
    {
        super(ID);
    }

    /* (non-Javadoc)
     * @see net.rim.maxillion.controller.Notifier#notifyDailySubscribers(long, byte, net.rim.maxillion.model.calculator.utils.time.TimeWrapper[])
     */
    public void notifySubscribers(long dateMS, byte nextPrayerIndex, TimeWrapper[] times)
    {
        for (int i = nextPrayerIndex; i < times.length; i++)
        {
            if ( (i != TimeCriticalEvent.Sunrise) && (i != TimeCriticalEvent.HalfNight) )
            {
                TimeWrapper current = times[i];
                String prayer = NAMES.getName(i);
                String message = prayer+" at: "+current.toString();
                long salatTime = current.getTime();

                if ( (salatTime-_lastCommit) > ADVANCE_TIME )
                    createEmailTask( dateMS, message, calculateScheduleDifference(salatTime, dateMS), DatabaseContainer.TABLE_PRAYERLY );
            }
        }
    }
}