import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static VersionedMap<String, Integer> map;

    public static void main(String[] args) {
        setup(); test1();
        setup(); test2();
        setup(); test3();
        setup(); test4();
        setup(); test5();
        setup(); test6();
        setup(); test7();
        setup(); test8();
        setup(); test9();
        setup(); test10();
        setup(); test11();
        setup(); test12();
        setup(); test13();
        setup(); test14();
        setup(); test15();
        setup(); test16();
        setup(); test17();

        System.out.println("All tests passed!");
    }

    static void setup() {
        map = new VersionedMap<>();
    }

    static void test1() {
        map.put("name", 10);
        if (map.containsKey("name") && map.get("name") == 10) {
            System.out.println("Test 1 passed");
        }
    }

    static void test2() {
        map.put("name", 100);
        map.put("name", 1);
        if (map.get("name") == 1) {
            System.out.println("Test 2 passed");
        }
    }

    static void test3() {
        map.put("name", 1);
        map.remove("name");
        if (!map.containsKey("name")) {
            System.out.println("Test 3 passed");
        }
    }

    static void test4() {
        map.put("name", 20);
        map.transform("name", v -> v * 2);
        if (map.get("name") == 40) {
            System.out.println("Test 4 passed");
        }
    }

    static void test5() {
        map.put("name", 5);
        map.undo();
        if (!map.containsKey("name")) {
            System.out.println("Test 5 passed");
        }
    }

    static void test6() {
        map.put("name", 5);
        map.put("name", 10);
        map.undo();
        if (map.containsKey("name") && map.get("name") == 5) {
            System.out.println("Test 6 passed");
        }
    }

    static void test7() {
        map.put("name", 5);
        map.remove("name");
        map.undo();
        if (map.containsKey("name")) {
            System.out.println("Test 7 passed");
        }
    }

    static void test8() {
        map.put("name", 5);
        map.put("bob", 10);
        map.undo();
        map.undo();
        if (!map.containsKey("name") && !map.containsKey("bob")) {
            System.out.println("Test 8 passed");
        }
    }

    static void test9() {
        map.put("name", 5);
        map.put("bob", 10);
        map.undo();
        map.undo();
        map.redo();
        map.redo();
        if (map.containsKey("name") && map.containsKey("bob")) {
            System.out.println("Test 9 passed");
        }
    }

    static void test10() {
        map.put("a", 1);
        map.undo();
        map.put("b", 2);
        try {
            map.redo();
            throw new RuntimeException("Expected NothingToRedoException");
        } catch (NothingToRedoException e) {
            System.out.println("Test 10 passed");
        }
    }

    static void test11() {
        try {
            map.undo();
            throw new RuntimeException("Expected NothingToUndoException");
        } catch (NothingToUndoException e) {
            System.out.println("Test 11 passed");
        }
    }

    static void test12() {
        try {
            map.redo();
            throw new RuntimeException("Expected NothingToRedoException");
        } catch (NothingToRedoException e) {
            System.out.println("Test 12 passed");
        }
    }

    static void test13() {
        try {
            map.remove("name");
            throw new RuntimeException("Expected MissingKeyException");
        } catch (MissingKeyException e) {
            System.out.println("Test 13 passed");
        }
    }

    static void test14() {
        try {
            map.transform("name", v -> v * 2);
            throw new RuntimeException("Expected MissingKeyException");
        } catch (MissingKeyException e) {
            System.out.println("Test 14 passed");
        }
    }

    static void test15() {
        try {
            map.put("name", 5);
            map.transform("name", v -> null);
            throw new RuntimeException("Expected InvalidValueException");
        } catch (InvalidValueException e) {
            System.out.println("Test 15 passed");
        }
    }

    static void test16() {
        map.put("random", 10);
        AtomicInteger counter = new AtomicInteger(5);
        map.transform("random", val -> val + counter.getAndIncrement()); // 15
        map.undo(); // 10
        map.redo(); // Must be 15 without incrementing counter to 6
        if (map.get("random") == 15) {
            System.out.println("Test 16 passed");
        }
    }

    static void test17() {
        map.put("alice", 10);
        List<MapCommand<String, Integer>> commands = List.of(
                new PutCommand<>("bob", 20),
                new RemoveCommand<>("nonexistent"),
                new TransformCommand<>("alice", v -> v + 5)
        );
        AtomicInteger successes = new AtomicInteger();
        AtomicInteger failures = new AtomicInteger();

        map.executeAll(commands, c -> successes.incrementAndGet(), e -> failures.incrementAndGet());

        if (successes.get() == 2 && failures.get() == 1) {
            System.out.println("Test 17 passed");
        }
    }
}