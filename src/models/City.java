package models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "city")
public class City {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "coverImage")
    private String coverImage;
    private Country country;
    private CostOfLiving costOfLiving;
    private double safetyIndex;
    private int costOfLivingRanking;
    private int safetyRanking;
    private int healthCareRanking;
    private int population;
    private int rankingScore;
    private List<String> languages = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    public City() {
    }

    public City(String name, Country country, CostOfLiving costOfLiving) {
        this.name = name;
        this.country = country;
        this.costOfLiving = costOfLiving;
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(int id, String name, String coverImage) {
        this.id = id;
        this.name = name;
        this.coverImage = coverImage;
    }

    public City(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CostOfLiving getCostOfLiving() {
        return costOfLiving;
    }

    public void setCostOfLiving(CostOfLiving costOfLiving) {
        this.costOfLiving = costOfLiving;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getCostOfLivingRanking() {
        return costOfLivingRanking;
    }

    public void setCostOfLivingRanking(int costOfLivingRanking) {
        this.costOfLivingRanking = costOfLivingRanking;
    }

    public int getSafetyRanking() {
        return safetyRanking;
    }

    public void setSafetyRanking(int safetyRanking) {
        this.safetyRanking = safetyRanking;
    }

    public int getHealthCareRanking() {
        return healthCareRanking;
    }

    public void setHealthCareRanking(int healthCareRanking) {
        this.healthCareRanking = healthCareRanking;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public double getSafetyIndex() {
        return safetyIndex;
    }

    public void setSafetyIndex(double safetyIndex) {
        this.safetyIndex = safetyIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getRankingScore() {
        return rankingScore;
    }

    public void setRankingScore(int rankingScore) {
        this.rankingScore = rankingScore;
    }
}
