/*
 * BungeeTabListPlus - a BungeeCord plugin to customize the tablist
 *
 * Copyright (C) 2014 - 2015 Florian Stober
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package codecrafter47.bungeetablistplus.data;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class DataCache {
    private final Map<DataKey<?>, Object> cache = new ConcurrentHashMap<>();
    private final Multimap<DataKey<?>, Consumer<?>> listeners = Multimaps.synchronizedMultimap(MultimapBuilder.hashKeys().hashSetValues().build());

    @SuppressWarnings("unchecked")
    public <T> void updateValue(DataKey<T> dataKey, T object) {
        if (object == null) {
            cache.remove(dataKey);
        } else {
            cache.put(dataKey, object);
        }
        listeners.get(dataKey).forEach(consumer -> ((Consumer<T>) consumer).accept(object));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getValue(DataKey<T> dataKey) {
        return Optional.ofNullable((T) cache.get(dataKey));
    }

    @SuppressWarnings("unchecked")
    public <T> T getRawValue(DataKey<T> dataKey) {
        return (T) cache.get(dataKey);
    }

    public void clear() {
        cache.keySet().forEach(key -> listeners.get(key).forEach(consumer -> consumer.accept(null)));
        cache.clear();
    }

    public <T> void registerValueChangeListener(DataKey<T> key, Consumer<T> listener) {
        listeners.put(key, listener);
    }

    public <T> void unregisterValueChangeListener(DataKey<T> key, Consumer<T> listener) {
        listeners.remove(key, listener);
    }

    public Map<DataKey<?>, Object> getMap() {
        return cache;
    }
}
