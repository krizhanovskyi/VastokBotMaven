package vastokbot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PhotoRepository {
    public final Map<Integer, String> photos = new ConcurrentHashMap<>();

}
