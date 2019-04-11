/*
 * DailyNotifier.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2010-2010
 */
package net.rim.maxillion.controller;

import net.rim.device.api.util.DateTimeUtilities;
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
class DailyNotifier extends Notifier
{
    private static final long ID = 0x2a454ba0e7810b64L;

    /**
     * @param id
     */
    protected DailyNotifier()
    {
        super(ID);
    }

    /* (non-Javadoc)
     * @see net.rim.maxillion.controller.Notifier#notifyDailySubscribers(long, byte, net.rim.maxillion.model.calculator.utils.time.TimeWrapper[])
     */
    public void notifySubscribers(long dateMS, byte nextPrayerIndex, TimeWrapper[] times)
    {
        StringBuffer sb = new StringBuffer();
        long firstTime = times[nextPrayerIndex].getTime();

        for (; nextPrayerIndex < times.length; nextPrayerIndex++)
        {
            sb.append( NAMES.getName(nextPrayerIndex) );
            sb.append(": ");
            sb.append( times[nextPrayerIndex].toString() );

            if (nextPrayerIndex < times.length-1)
                sb.append("\n");
        }

        if ( !DateTimeUtilities.isSameDate(firstTime, _lastCommit) )
            createEmailTask( dateMS, sb.toString(), calculateScheduleDifference(firstTime, dateMS), DatabaseContainer.TABLE_DAILY );
    }
}