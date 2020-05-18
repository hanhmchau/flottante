package models;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="item", propOrder = {
        "title",
        "value"
})
public class Item {
    @XmlElement(required = true)
    private String title;
    @XmlElement(required = true)
    @XmlSchemaType(name = "decimal")
    private double value;

    public Item() {
    }

    public Item(String title, double value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", value=" + value +
                '}';
    }
}
