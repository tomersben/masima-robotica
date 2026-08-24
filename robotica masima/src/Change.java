    public record Change<K, V>(
            K key,
            Errors<V> before,
            Errors<V> after,
            Operation operationType
    ) {}

