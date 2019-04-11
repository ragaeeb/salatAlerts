/*
 * Notifier.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2010-2010
 */
package net.rim.maxillion.controller;

import java.util.Timer;
import java.util.TimerTask;
import net.rim.device.api.collection.util.UnsortedReadableList;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.maxillion.model.calculator.utils.TimeCriticalEventNames;
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
abstract class Notifier
{
    /**
     * 
     */
    protected static final int ADVANCE_TIME = 10*60*1000;

    protected static final TimeCriticalEventNames NAMES = TimeCriticalEventNames.getInstance();

    protected DatabaseContainer _app;
    protected long _lastCommit;

    protected PersistentObject _po;

    protected Timer _timer;

    private long _id;


    protected Notifier(long id)
    {
        _po = PersistentStore.getPersistentObject(id);

        Long object = ( (Long)_po.getContents() );
        _lastCommit = object == null ? 0 : object.longValue();

        _id = id;
    }


    public void close()
    {
        _timer.cancel();
        PersistentStore.destroyPersistentObject(_id);
    }


    public void setReferences(DatabaseContainer app, Timer timer)
    {
        _timer = timer;
        _app = app;
    }


    protected void createEmailTask(final long date, final String content, final long sendTime, final String table)
    {
        TimerTask t = new TimerTask() {
            public void run()
            {
                UnsortedReadableList all = _app.getTable(table).getAll();

                if ( all.size() > 0 ) // protect against duplicate sending in case device rebooted
                {
                    _app.getEmailBoundary().sendMessage( all, new TimeWrapper(date).getDate(), content );
                    _lastCommit = date;

                    _po.setContents( new Long(_lastCommit) );
                    _po.commit();
                }
            }
        };

        synchronized (this)
        {
            _timer.schedule(t, sendTime);
        }
    }

    protected abstract void notifySubscribers(long dateMS, byte nextPrayerIndex, TimeWrapper[] times);

    /**
     * Calculates the delay to use before notifying the subscribers. If the event is to
     * occur within 10 minutes of the current time, subscribers should be immediately
     * notified, otherwise they should be notified 10 minutes before the scheduled time.
     * @param time The time of the scheduled event.
     * @param currentTime The current raw time.
     * @return 0 If the difference between the scheduled event time and the current time
     * is less than 10 minutes, otherwise a time value of 10 minutes before the scheduled
     * event.
     */
    protected static final long calculateScheduleDifference(long time, long currentTime)
    {
        long difference = time-currentTime;

        if (difference > ADVANCE_TIME) // more than 10 minutes
            difference -= ADVANCE_TIME; // schedule it for 10 minutes before the event

        else // less than 10 minutes left, let them know instantly
            difference = 0;

        return difference;
    }
}