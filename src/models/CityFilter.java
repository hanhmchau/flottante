package models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "city-filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class CityFilter {
    @XmlElement(name = "page")
    private int page;
    @XmlElement(name = "col")
    private int costOfLivingIndexPriority;
    @XmlElement(name = "safety")
    private int safetyIndexPriority;
    @XmlElement(name = "population-min")
    private int populationMin;
    @XmlElement(name = "population-max")
    private int populationMax;
    @XmlElement(name = "languages")
    private List<String> languages;

    public CityFilter() {
    }

    public CityFilter(int page, int costOfLivingIndexPriority, int safetyIndexPriority, int populationMin, int populationMax, List<String> languages) {
        this.page = page;
        this.costOfLivingIndexPriority = costOfLivingIndexPriority;
        this.safetyIndexPriority = safetyIndexPriority;
        this.populationMin = populationMin;
        this.populationMax = populationMax;
        this.languages = languages;
    }

    public int getCostOfLivingIndexPriority() {
        return costOfLivingIndexPriority;
    }

    public void setCostOfLivingIndexPriority(int costOfLivingIndexPriority) {
        this.costOfLivingIndexPriority = costOfLivingIndexPriority;
    }

    public int getSafetyIndexPriority() {
        return safetyIndexPriority;
    }

    public void setSafetyIndexPriority(int safetyIndexPriority) {
        this.safetyIndexPriority = safetyIndexPriority;
    }

    public int getPopulationMin() {
        return populationMin;
    }

    public void setPopulationMin(int populationMin) {
        this.populationMin = populationMin;
    }

    public int getPopulationMax() {
        return populationMax;
    }

    public void setPopulationMax(int populationMax) {
        this.populationMax = populationMax;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
