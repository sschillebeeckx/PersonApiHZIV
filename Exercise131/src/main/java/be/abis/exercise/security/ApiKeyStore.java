package be.abis.exercise.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApiKeyStore {

    private static final Map<Integer, String> store = new ConcurrentHashMap<>();

    public static void store(int personId, String apiKey) {
        store.put(personId,apiKey);
    }

    public static boolean isValid(int personId, String apiKey) {
        String storedKey = store.get(personId);
        return storedKey != null && storedKey.equals(apiKey);
    }

}


