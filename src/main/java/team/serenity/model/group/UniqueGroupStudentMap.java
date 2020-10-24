package team.serenity.model.group;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import team.serenity.model.group.exceptions.DuplicateException;
import team.serenity.model.util.UniqueList;
import team.serenity.model.util.UniqueMap;

public class UniqueGroupStudentMap implements UniqueMap<Group, UniqueList<Student>> {
    private final Map<Group, UniqueList<Student>> internalMap = Collections.emptyMap();
    private final Map<Group, UniqueList<Student>> internalUnmodifiableMap = Collections.unmodifiableMap(this.internalMap);

    @Override
    public Map<Group, UniqueList<Student>> getMap() {
        return this.internalMap;
    }

    @Override
    public void setElementsWithUniqueMap(UniqueMap<Group, UniqueList<Student>> replacement) {
        requireNonNull(replacement);
        clear();
        putAll(replacement);
    }

    @Override
    public void setElementsWithMap(Map<Group, UniqueList<Student>> map) throws DuplicateException {
        requireNonNull(map);
        if (!elementsAreUnique(map)) {
            throw new DuplicateException();
        }
        clear();
        putAll(map);
    }

    @Override
    public Map<Group, UniqueList<Student>> asUnmodifiableMap() {
        return this.internalUnmodifiableMap;
    }

    @Override
    public boolean elementsAreUnique(Map<Group, UniqueList<Student>> map) {
        requireNonNull(map);
        for (Group grp : map.keySet()) {
            for (Group grp2 : map.keySet()) {
                if (map.get(grp).equals(map.get(grp2))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int size() {
        return this.internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        requireNonNull(key);
        return this.internalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        requireNonNull(value);
        return this.internalMap.containsValue(value);
    }

    @Override
    public UniqueList<Student> get(Object key) {
        requireNonNull(key);
        return this.internalMap.get(key);
    }

    @Override
    public UniqueList<Student> put(Group key, UniqueList<Student> value) {
        requireNonNull(key);
        requireNonNull(value);
        return this.internalMap.put(key, value);
    }

    @Override
    public UniqueList<Student> remove(Object key) {
        requireNonNull(key);
        return this.internalMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends Group, ? extends UniqueList<Student>> map) {
        requireNonNull(map);
        this.internalMap.putAll(map);
    }

    @Override
    public void clear() {
        this.internalMap.clear();
    }

    @Override
    public Set<Group> keySet() {
        return this.internalMap.keySet();
    }

    @Override
    public Collection<UniqueList<Student>> values() {
        return this.internalMap.values();
    }

    @Override
    public Set<Entry<Group, UniqueList<Student>>> entrySet() {
        return this.internalMap.entrySet();
    }

    @Override
    public int hashCode() {
        return this.internalMap.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueGroupStudentMap)) {
            return false;
        }

        UniqueGroupStudentMap otherMap = (UniqueGroupStudentMap) other;

        for (Group grp : otherMap.keySet()) {
            Optional<UniqueList<Student>> list = Optional.ofNullable(internalMap.get(grp));
            if (list.isEmpty()) {
                return false;
            }

            if (!list.get().equals(otherMap.get(grp))) {
                return false;
            }
        }

        return true;
    }
}
