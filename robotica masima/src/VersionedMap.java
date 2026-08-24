import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class VersionedMap<K, V> {
    private final Map<K, V> internalMap = new HashMap<>();
    private final Deque<Change<K, V>> undoStack = new ArrayDeque<>();
    private final Deque<Change<K, V>> redoStack = new ArrayDeque<>();

    public V get(K key) {
        if (!internalMap.containsKey(key)) {
            throw new MissingKeyException("Key not found: " + key);
        }
        return internalMap.get(key);
    }

    public boolean containsKey(K key) {
        return internalMap.containsKey(key);
    }

    public int size() {
        return internalMap.size();
    }

    public Change<K, V> execute(MapCommand<K, V> command) {
        Change<K, V> change = command.execute(internalMap);
        undoStack.push(change);
        redoStack.clear();
        return change;
    }

    public Change<K, V> put(K key, V value) {
        return execute(new PutCommand<>(key, value));
    }

    public Change<K, V> remove(K key) {
        return execute(new RemoveCommand<>(key));
    }

    public Change<K, V> transform(K key, UnaryOperator<V> transformer) {
        return execute(new TransformCommand<>(key, transformer));
    }

    public Change<K, V> undo() {
        if (undoStack.isEmpty()) {
            throw new NothingToUndoException("No actions to undo.");
        }
        Change<K, V> change = undoStack.pop();
        applyState(change.key(), change.before());
        redoStack.push(change);
        return change;
    }

    public Change<K, V> redo() {
        if (redoStack.isEmpty()) {
            throw new NothingToRedoException("No actions to redo.");
        }
        Change<K, V> change = redoStack.pop();
        applyState(change.key(), change.after());
        undoStack.push(change);
        return change;
    }

    public void executeAll(
            Collection<? extends MapCommand<K, V>> commands,
            Consumer<? super Change<K, V>> successHandler,
            Consumer<? super MapException> errorHandler
    ) {
        for (MapCommand<K, V> command : commands) {
            try {
                Change<K, V> change = execute(command);
                if (successHandler != null) successHandler.accept(change);
            } catch (MapException e) {
                if (errorHandler != null) errorHandler.accept(e);
            }
        }
    }

    private void applyState(K key, Errors<V> state) {
        if (state.exists()) {
            internalMap.put(key, state.value());
        } else {
            internalMap.remove(key);
        }
    }
}