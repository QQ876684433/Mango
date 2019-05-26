package ui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 参数类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/5/26
 */
public class ParamTuple {
    private StringProperty key;
    private StringProperty value;
    private StringProperty description;


    public ParamTuple(String key, String value, String description) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
        this.description = new SimpleStringProperty(description);
    }

    public ParamTuple() {
        this.key = new SimpleStringProperty(null);
        this.value = new SimpleStringProperty(null);
        this.description = new SimpleStringProperty(null);
    }

    public String getKey() {
        return key.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty keyProperty() {
        return key;
    }

    public StringProperty valueProperty() {
        return value;
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
