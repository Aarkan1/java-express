package express.database;

/**
 * @author Johan Wirén
 *
 * Handler to be used when registering a watcher to a collection
 */
public interface WatchHandler {
    void handler(WatchData watchData);
}
