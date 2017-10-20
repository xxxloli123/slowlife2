package com;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgrape on 2017/6/21.
 * e-mail: sgrape1153@gmail.com
 */

public class Observable<T> {
    private boolean changed;
    private List<Observer<T>> observers;
    private static final String SAVE_OBSERVABLE_STATE = "save_observable_state";

    public Observable() {
        observers = new ArrayList<>();
    }

    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(T t) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Observer[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary Observables while holding its own Monitor.
             * The code where we extract each Observable from
             * the ArrayList and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             *
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!hasChanged())
                return;

            arrLocal = observers.toArray(new Observer[observers.size()]);
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--)
            arrLocal[i].update(this, t);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        observers.clear();
    }

    public synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the <tt>hasChanged</tt> method will now return <tt>false</tt>.
     * This method is called automatically by the
     * <code>notifyObservers</code> methods.
     *
     * @see java.util.Observable#notifyObservers()
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return <code>true</code> if and only if the <code>setChanged</code>
     * method has been called more recently than the
     * <code>clearChanged</code> method on this object;
     * <code>false</code> otherwise.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers of this <tt>Observable</tt> object.
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return observers.size();
    }

    public synchronized void deleteObserver(Observer o) {
        observers.remove(o);
    }

}
