package models;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Item;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cost-of-living")
public class CostOfLiving {
    @XmlElementWrapper
    @XmlElement(name = "category", required = true)
    private Map<String, ItemList> categories = new HashMap<>();

    public CostOfLiving() {
    }

    public Map<String, ItemList> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, ItemList> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CostOfLiving{" +
                "categories=" + categories +
                '}';
    }
}
