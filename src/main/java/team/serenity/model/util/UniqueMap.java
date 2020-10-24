package team.serenity.model.util;

import java.util.Map;

import team.serenity.model.group.exceptions.DuplicateException;

public interface UniqueMap<K, V> extends Map<K, V> {

    public Map<K, V> getMap();

    public void setElementsWithUniqueMap(UniqueMap<K, V> replacement);

    public void setElementsWithMap(Map<K, V> replacement) throws DuplicateException;

    public Map<K, V> asUnmodifiableMap();

    public boolean elementsAreUnique(Map<K, V> elements);

}
