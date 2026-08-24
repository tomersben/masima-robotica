    public enum Operation {
        PUT(false),
        REMOVE(true),
        TRANSFORM(true);

        private final boolean requiresExistingKey;

        Operation(boolean requiresExistingKey) {
            this.requiresExistingKey = requiresExistingKey;
        }

        public boolean requiresExistingKey() {
            return requiresExistingKey;
        }
    }

