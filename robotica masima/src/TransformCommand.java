import java.util.Map;
import java.util.function.UnaryOperator;

public class TransformCommand<K, V> extends MapCommand<K, V> {
    private final UnaryOperator<V> transformer;

    public TransformCommand(K key, UnaryOperator<V> transformer) {
        super(key);
        this.transformer = transformer;
    }

    @Override
    public Change<K, V> execute(Map<K, V> currentValues) {
        K key = getKey();
        if (!currentValues.containsKey(key)) {
            throw new MissingKeyException("Key not found: " + key);
        }

        V oldValue = currentValues.get(key);
        V newValue = transformer.apply(oldValue);

        if (newValue == null) {
            throw new InvalidValueException("Transformer returned null.");
        }

        currentValues.put(key, newValue);
        Errors<V> before = new Errors<>(true, oldValue);
        Errors<V> after = new Errors<>(true, newValue);

        return new Change<>(key, before, after, Operation.TRANSFORM);
    }
}