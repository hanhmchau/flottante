package analyzer;

import models.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class NumbeoCostOfLivingProcessor {
    public List<City> processScrapedData(List<City> cities, List<Category> categoryWeights, Set<Category> unknownCategories, Map<String, Double> minimumWage) {
        NumbeoCostOfLivingProcessor processor = new NumbeoCostOfLivingProcessor();
        Map<City, Double> costOfLivingIndexMap = processor.getCostOfLivingIndexMap(cities, categoryWeights, unknownCategories, minimumWage);
        return processor.getCostOfLivingRankingList(costOfLivingIndexMap);
    }

    private Map<City, Double> getCostOfLivingIndexMap(List<City> cities, List<Category> categories, Set<Category> unknownCategories, Map<String, Double> minimumWage) {
        Map<City, Double> indexes = new HashMap<>();
        cities.forEach(city -> indexes.put(city, getCostOfLivingIndex(city.getCountry().getName(), city.getCostOfLiving(), categories, unknownCategories, minimumWage)));
        return indexes;
    }

    private List<City> getCostOfLivingRankingList(Map<City, Double> indexMap) {
        List<Double> values = new ArrayList<>(indexMap.values());
        Collections.sort(values);
        indexMap.forEach((city, value) -> {
            city.setCostOfLivingRanking(values.indexOf(value) + 1);
        });
        return new ArrayList<>(indexMap.keySet());
    }

    private double getCostOfLivingIndex(String countryName, CostOfLiving costOfLiving, List<Category> categories, Set<Category> unknownCategories, Map<String, Double> minimumWage) {
        double index = 0;
        Map<String, Double> map = new HashMap<>();
        categories.forEach(c -> map.put(c.getName(), c.getWeight()));
        double averageMinimumWage = minimumWage.values().stream().mapToDouble(c -> c).average().orElse(1);
        double minWage = minimumWage.getOrDefault(countryName, averageMinimumWage);

        for (Map.Entry<String, ItemList> entry : costOfLiving.getCategories().entrySet()) {
            for (Item item : entry.getValue().getItems()) {
                if (map.containsKey(item.getTitle())) {
                    double categoryAverage = map.get(item.getTitle());
                    if (categoryAverage > 0) {
                        index += categoryAverage * item.getValue();
                    }
                    if (categoryAverage == -1) {
                        index += item.getValue();
                    }
                } else {
                    Category newCat = new Category();
                    newCat.setName(item.getTitle());
                    newCat.setGroupName(entry.getKey());
                    unknownCategories.add(newCat);
                    index += item.getValue() * 1.0;
                }
            }
        }
        if (minWage != 0) {
            return index / minWage;
        }
        return index;
    }
}
