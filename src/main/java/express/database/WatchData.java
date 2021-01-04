package express.database;

import java.util.List;

/**
 * @author Johan Wirén
 *
 * Wrapper object to handle collection events
 */
public class WatchData {
    private String model;
    private String event;
    private List data;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
