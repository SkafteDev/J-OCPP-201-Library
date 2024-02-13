package com.decouplink;

/**
 * Abstract class for implementing link-sharing with late initialization and
 * eager disposal.
 *
 * @author mrj
 */
public abstract class AbstractLinkShare<T> {

    private Link<T> link = null;
    private int count = 0;

    public synchronized Link<T> getLink() {
        increase();
        return new ClientSpecificLink<>(link.getDestination());
    }

    private synchronized void increase() {
        if(count == 0) {
            assert(link == null);
            link = createObject();
        }

        count++;
    }

    private synchronized void decrease() {
        count--;
        
        if(count == 0) {
            link.dispose();
            link = null;
        }
    }

    protected abstract Link<T> createObject();

    /**
     * Client-specific link.
     */
    private class ClientSpecificLink<T> extends AbstractLink<T> {

        private boolean disposed = false;

        public ClientSpecificLink(T t) {
            super(t);
        }

        @Override
        public void dispose() {
            if(!disposed) {
                disposed = true;
                decrease();
            }
        }
    }
}
