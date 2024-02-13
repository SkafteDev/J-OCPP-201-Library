package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation;

import com.decouplink.Context;
import com.decouplink.Link;
import com.decouplink.Utilities;

import java.util.*;

public class ChargingStationImpl {

    private Context ctx;
    private Map<Integer, Link<?>> links; // <Component HashCode, Link<?>>

    public ChargingStationImpl() {
        ctx = Utilities.context(this);
        links = new HashMap<>();
    }

    public <T> void addComponent(Class<T> type, T component) {
        Link<T> dynamicLink = ctx.add(type, component);
        links.put(component.hashCode(), dynamicLink);
    }

    public <T> boolean removeComponent(T component) {
        Link<?> link = links.get(component.hashCode());

        if (link != null) {
            link.dispose();
            return true;
        }

        return false;
    }

    public <T> boolean replaceComponent(Class<T> type, T oldComponent, T newComponent) {
        boolean wasRemoved = removeComponent(oldComponent);

        if (wasRemoved) {
            addComponent(type, newComponent);
            return true;
        }

        return false;
    }

    public <T> T getComponent(Class<T> type) {
        return ctx.one(type);
    }

    public <T> Collection<? extends T> getComponents(Class<T> type) {
        return ctx.all(type);
    }
}
