package scraper;

import analyzer.NumbeoCostOfLivingProcessor;
import analyzer.NumbeoSafetyProcessor;
import models.*;
import services.*;

import java.util.*;
import java.util.stream.Collectors;

public class MainScraper {
    public void startCrawling() {
        NumbeoCostOfLivingScraper scraper = new NumbeoCostOfLivingScraper();
        NumbeoSafetyScraper numbeoSafetyScraper = new NumbeoSafetyScraper();
        PexelsImageScraper pexelsImageScraper = new PexelsImageScraper();
        CountryLanguageScraper countryLanguageScraper = new CountryLanguageScraper();
        NumbeoCostOfLivingProcessor numbeoCostOfLivingProcessor = new NumbeoCostOfLivingProcessor();
        NumbeoSafetyProcessor numbeoSafetyProcessor = new NumbeoSafetyProcessor();
        WorldPopulationScraper worldPopulationScraper = new WorldPopulationScraper();
        CountryService countryService = new CountryService();
        CityService cityService = new CityService();
        CategoryService categoryService = new CategoryService();
        CostOfLivingService costOfLivingService = new CostOfLivingService();
        LanguageService languageService = new LanguageService();
        MinimumWageScraper minimumWageScraper = new MinimumWageScraper();

        List<Category> categoryWeights = new ArrayList<>();
        try {
            categoryWeights = categoryService.getCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<Category> unknownCategories = new HashSet<>();
        List<Country> countries = new ArrayList<>();
        Map<String, Double> minimumWage = minimumWageScraper.getMinimumWage();
        List<City> citiesWithCostOfLiving = scraper.getCitiesWithCostOfLiving(categoryWeights, unknownCategories, countries);
        citiesWithCostOfLiving = numbeoCostOfLivingProcessor
                .processScrapedData(citiesWithCostOfLiving, categoryWeights, unknownCategories, minimumWage);
        countries.forEach(c -> {
            c.setMinimumWage(minimumWage.getOrDefault(c.getName(), 0.0));
        });
        Set<String> groups = citiesWithCostOfLiving.stream()
                .flatMap(city -> city.getCostOfLiving().getCategories().keySet().stream())
                .collect(Collectors.toSet());

        citiesWithCostOfLiving.forEach(city -> {
            List<String> cityImages = pexelsImageScraper.getCityImages(city.getName(), 5);
            city.setImages(cityImages);
        });
        List<City> citiesWithSafetyIndexRanking = numbeoSafetyProcessor.getCitiesWithSafetyRanking(
                numbeoSafetyScraper.getCitiesWithSafetyRanking(),
                numbeoSafetyScraper.getCountriesWithSafetyIndex(),
                citiesWithCostOfLiving);
        List<City> cityPopulations = worldPopulationScraper.getCityPopulations(citiesWithSafetyIndexRanking);

        List<Country> countriesWithLanguages = countryLanguageScraper.getCountryLanguageMapping(countries);
        Set<Language> languages = languageService.getLanguages(countries);

        try {
            Map<String, Integer> allGroups = categoryService.insertGroups(groups);
            List<Category> newCategories = categoryService.insertCategories(unknownCategories, allGroups);
            categoryWeights.addAll(newCategories);
            countryService.insertCountries(countries);
            cityService.insertCities(cityPopulations);
            costOfLivingService.insertCostOfLiving(cityPopulations, categoryWeights);
            languageService.insertLanguages(languages);
            countryService.insertCountryLanguages(countriesWithLanguages);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(() -> {
//
//        },
//                0,
//                TimeUnit.HOURS.toSeconds(12),
//                TimeUnit.SECONDS);
//        scheduler.shutdown();
    }
}
