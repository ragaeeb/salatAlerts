/*
 * @(#)AlertsManager.java   1.0 2009-09-15
 * @(#)AlertsManager.java   1.1 2009-09-20
 * @(#)AlertsManager.java   1.2 2009-09-23
 * @(#)AlertsManager.java   1.3 2009-09-24
 * @(#)AlertsManager.java   1.4 2009-10-03
 * @(#)AlertsManager.java   1.5 2010-02-27
 * @(#)AlertsManager.java   1.6 2010-04-27
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
package net.rim.maxillion.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.GlobalEventListener;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.util.DateTimeUtilities;
import net.rim.maxillion.model.calculator.Calculator;
import net.rim.maxillion.model.calculator.utils.GeoParameters;
import net.rim.maxillion.model.calculator.utils.TimeCriticalEvent;
import net.rim.maxillion.model.calculator.utils.time.TimeWrapper;
import net.rim.maxillion.subscription.controller.DatabaseContainer;


/**
 * Calculates the next upcoming prayer schedule and informs the subscribers of it
 * appropriately. This notification can either happen once a day or can happen on a per
 * event basis.
 *
 * @author Ragaeeb Haq
 * @version 1.00 2009-09-15 Initial Submission.
 * @version 1.10 2009-09-20 Error-logging is now system-dependent.
 * @version 1.20 2009-09-23 Prayer names are now included in the result. Sunrise is discarded.
 * @version 1.30 2009-09-24 Active variable added to close thread when Servlet exits.
 * @version 1.40 2009-10-03 Safe-exception handling by only catching InterruptedException.
 * @version 1.50 2010-02-27 This is now a Thread subclass rather than Runnable.
 * @version 1.60 2010-04-27 Removed all logging. Initialization and set-up is now removed from this class.
 * TimeFormatter reference removed.
 * @since MaxillionPrayers 2.0
 */
class AlertsManager extends Application implements Runnable, GlobalEventListener
{
    /** The geographic location to calculate the prayer times for. */
    private static final GeoParameters geoParams = new GeoParameters(45.3560, -75.7579, -5);

    /**
     * 
     */
    private static final int NO_MORE_TIMES = -1;

    /** Is this thread active? */
    private boolean _active;

    private Thread _current;

    private Notifier _daily;

    private Notifier _prayerly;


    /**
     * Creates an instance of this class so that the alerts manager can be initialized,
     * along with its dependent notification systems.
     */
    private AlertsManager()
    {
        _active = false;

        DatabaseContainer app = (DatabaseContainer)RuntimeStore.getRuntimeStore().get(DatabaseContainer.ID);
        Timer timer = new Timer();

        _daily = new DailyNotifier();
        _prayerly = new PrayerlyNotifier();

        _daily.setReferences(app, timer);
        _prayerly.setReferences(app, timer);

        addGlobalEventListener(this);

        _current = new Thread(this);
        _current.start();
    }



    /* (non-Javadoc)
     * @see net.rim.device.api.system.GlobalEventListener#eventOccurred(long, int, int, java.lang.Object, java.lang.Object)
     */
    public void eventOccurred(long guid, int data0, int data1, Object object0, Object object1)
    {
        if ( (guid == DatabaseContainer.ID) && _active && (data0 == DatabaseContainer.ALERTS_CLOSE_SIGNAL) ) // close signal
        {
            synchronized (this)
            {
                _active = false;
                _prayerly.close();
                _daily.close();
                _current.interrupt();

                removeGlobalEventListener(this);
                requestClose();
            }

            System.exit(0);
        }
    }


    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public final void run()
    {
        _active = true;

        while (_active)
        {
            try {
                Date date = new Date();
                long dateMS = date.getTime();

                TimeWrapper[] times = Calculator.getInstance().calculate(geoParams, date); // the salat timings for today
                byte nextPrayerIndex = nextPrayerIndex(times, dateMS);

                if (nextPrayerIndex == NO_MORE_TIMES) // no more prayer times left for today so just sleep til the next one
                {
                    Calendar midnight = DateTimeUtilities.getDate(DateTimeUtilities.ONEDAY);
                    dateMS = midnight.getTime().getTime()-dateMS; // sleep until that time when you can wake up and recalculate for that day 
                }

                else
                {
                    _daily.notifySubscribers(dateMS, nextPrayerIndex, times);
                    _prayerly.notifySubscribers(dateMS, nextPrayerIndex, times);
                    dateMS = times[times.length-1].getTime()-dateMS;
                }

                Thread.sleep(dateMS);
            }

            catch (IllegalStateException ex)
            {
            }

            catch (InterruptedException ex)
            {
            }
        }
    }



    public static void libMain(String[] args)
    {
        Application am = new AlertsManager();
        am.enterEventDispatcher();
    }


    /**
     * Gets the times for the prayers/day characteristics that have not yet happened.
     * @param results Contains the actual prayer time data.
     * @param dateMS The time to compare the the calculated prayer times with
     * to determine whether they have passed or not.
     * @return The times for the prayers that have not yet passed.
     */
    private static final byte nextPrayerIndex(TimeWrapper[] results, long dateMS)
    {
        byte nextPrayer = NO_MORE_TIMES;

        for (byte prayer = TimeCriticalEvent.Fajr; prayer <= TimeCriticalEvent.HalfNight; prayer++)
        {
            if ( results[prayer].getTime() >= dateMS )
            {
                nextPrayer = prayer;
                break;
            }
        }

        return nextPrayer;
    }
}