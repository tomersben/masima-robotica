import java.util.Map;

public class PutCommand<K, V> extends MapCommand<K, V> {
    private final V newValue;

    public PutCommand(K key, V newValue) {
        super(key);
        if (newValue == null) {
            throw new InvalidValueException("Value cannot be null.");
        }
        this.newValue = newValue;
    }

    @Override
    public Change<K, V> execute(Map<K, V> currentValues) {
        K key = getKey();
        boolean exists = currentValues.containsKey(key);
        Errors<V> before = exists ? new Errors<>(true, currentValues.get(key)) : new Errors<>(false, null);
        Errors<V> after = new Errors(true, newValue);

        currentValues.put(key, newValue);
        return new Change<>(key, before, after, Operation.PUT);
    }
}