public record Errors<V>(boolean exists, V value) {
        public Errors {
            if (!exists && value != null) {
                throw new IllegalArgumentException("Value must be null when exists is false.");
            }
            if (exists && value == null) {
                throw new IllegalArgumentException("Value cannot be null when exists is true.");
            }
        }
    }

