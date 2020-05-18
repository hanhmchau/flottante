package analyzer;

import models.City;
import models.Country;

import java.util.*;

public class NumbeoSafetyProcessor {

    public List<City> getCitiesWithSafetyRanking(Map<String, Double> citiesWithSafetyIndex,
                              Map<String, Double> countriesWithSafetyIndex,
                              List<City> cities) {
        Map<City, Double> fullMapWithSafetyIndex = new HashMap<>();
        double averageForAllCities = citiesWithSafetyIndex.values().stream().mapToDouble(a -> a).average().orElse(100.0/2);
        cities.forEach(city -> {
            String cityName = city.getName();
            if (citiesWithSafetyIndex.containsKey(cityName)) {
                fullMapWithSafetyIndex.put(city, citiesWithSafetyIndex.get(cityName));
            } else
                fullMapWithSafetyIndex.put(city, countriesWithSafetyIndex.getOrDefault(city.getCountry().getName(),
                        averageForAllCities));
        });
        return createMapWithRanking(fullMapWithSafetyIndex);
    }

    private List<City> createMapWithRanking(Map<City, Double> mapWithIndex) {
        List<Double> list = new ArrayList<>(mapWithIndex.values());
        list.sort((a, b) -> Double.compare(b, a));
        mapWithIndex.forEach((city, safetyIndex) -> {
            city.setSafetyRanking(list.indexOf(safetyIndex) + 1);
            city.setSafetyIndex(safetyIndex);
        });
        return new ArrayList<>(mapWithIndex.keySet());
    }
}
