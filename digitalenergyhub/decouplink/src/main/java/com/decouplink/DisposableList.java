package com.decouplink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author mrj
 */
public class DisposableList implements Disposable {

    private final List<Disposable> items = new ArrayList<>();

    public DisposableList() {
    }

    public DisposableList(Disposable... list) {
        Collection<Disposable> c = Arrays.asList(list);

        if(c.contains(null)) {
            throw new IllegalArgumentException();
        }
        
        items.addAll(c);
    }

    public void add(Disposable d) {
        if(d == null) {
            throw new IllegalArgumentException();
        }

        items.add(d);
    }
    
    public void remove(Disposable d) {
        items.remove(d);
    }

    @Override
    public void dispose() {
        for(Disposable d : items) {
            d.dispose();
        }
        items.clear();
    }
}
