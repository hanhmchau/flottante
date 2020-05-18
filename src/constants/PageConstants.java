package constants;

public class PageConstants {
    public static String USD = "USD";

    public static class NUMBEO {
        private static final String DOMAIN = "https://www.numbeo.com";
        public static final String COST_OF_LIVING_PAGE = DOMAIN + "/cost-of-living";
        public static final String SAFETY_PAGE = DOMAIN + "/crime/rankings_current.jsp";
        public static final String SAFETY_PAGE_BY_COUNTRIES = DOMAIN + "/crime/rankings_by_country.jsp";

        public static String getCountryCostOfLivingPage(String countrySuffix) {
            return String.format("%s/%s", COST_OF_LIVING_PAGE, countrySuffix);
        }

        public static String getCityCostOfLivingPage(String citySuffix) {
            return String.format("%s/%s", COST_OF_LIVING_PAGE, citySuffix);
        }

        public static String getCityCostOfLivingPage(String citySuffix, String currency) {
            return String.format("%s/%s?displayCurrency=%s", COST_OF_LIVING_PAGE, citySuffix, currency);
        }

        public static String appendCurrency(String url, String currency) {
            return String.format("%s?displayCurrency=%s", url, currency);
        }
    }

    public static class PEXELS {
        private static final String DOMAIN = "https://www.pexels.com";

        public static String getCityImagesPage(String cityName) {
            return String.format("%s/search/%s", DOMAIN, cityName);
        }
    }

    public static class WORLD_POPULATION {
        public static final String SITE = "http://worldpopulationreview.com/world-cities/";
    }

    public static class LANGUAGE_CODE {
        public static final String SITE = "https://www.science.co.il/language/Codes.php";
    }

    public static class COUNTRY_LANGUAGE {
        public static final String SITE = "https://wiki.openstreetmap.org/wiki/Nominatim/Country_Codes";
    }

    public static class MINIMUM_WAGE {
        public static final String SITE = "https://countryeconomy.com/national-minimum-wage";
    }
}
