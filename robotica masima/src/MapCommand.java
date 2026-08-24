import java.util.Map;

public abstract class MapCommand<K, V> {
    private final K key;

    protected MapCommand(K key) {
        this.key = key;
    }

    public final K getKey() {
        return key;
    }

    public abstract Change<K, V> execute(Map<K, V> currentValues);
}