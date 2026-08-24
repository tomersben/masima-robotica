import java.util.Map;
    public class RemoveCommand<K, V> extends MapCommand<K, V> {
        public RemoveCommand(K key) {
            super(key);
        }

        @Override
        public Change<K, V> execute(Map<K, V> currentValues) {
            K key = getKey();
            if (!currentValues.containsKey(key)) {
                throw new MissingKeyException("Key not found: " + key);
            }

            V oldValue = currentValues.remove(key);
            Errors<V> before = new Errors<>(true, oldValue);
            Errors<V> after = new Errors<>(false, null);

            return new Change<>(key, before, after, Operation.REMOVE);
        }
    }

