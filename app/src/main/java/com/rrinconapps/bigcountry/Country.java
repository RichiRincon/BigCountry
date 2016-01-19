package com.rrinconapps.bigcountry;

/**
 * Models a country with its main information.
 *
 * @author  Ricardo Rincon
 * @since 2015-12-19
 */
public class Country {
    private String name;
    private String capital;
    private String continent;
    private int flag_id;
    private double size;
    private double population;

    /**
     * Constructs a country object initializing its information.
     *
     * @param name Name of the country
     * @param capital Capital of the country
     * @param continent Continent where the country is placed
     * @param flag_id Id of the icon with the country flag
     * @param size Area of the country
     * @param population Population of the country
     */
    public Country(String name, String capital, String continent, int flag_id, double size,
                   double population) {
        this.name = name;
        this.capital = capital;
        this.continent = continent;
        this.flag_id = flag_id;
        this.size = size;
        this.population = population;
    }

    /**
     * Indicates if the country is bigger than another one
     *
     * @param otherCountry to compare with
     * @return true if it is bigger or false in other case
     */
    public boolean isBigger(Country otherCountry) {
        return this.getSize() > otherCountry.getSize();
    }

    /**
     * Indicates if the country is more populated than another one
     *
     * @param otherCountry to compare with
     * @return true if it is more populated or false in other case
     */
    public boolean isMorePopulated(Country otherCountry) {
        return this.getPopulation() > otherCountry.getPopulation();
    }

    /**
     * Gets the country's name.
     * @return the name of the country
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the country's capital.
     * @return the capital of the country
     */
    public String getCapital() {
        return capital;
    }

    /**
     * Gets the country's continent.
     * @return the continent of the country
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Gets the id of country's flag.
     * @return the id of the flag of the country
     */
    public int getFlag_id() {
        return flag_id;
    }

    /**
     * Gets the country's area.
     * @return the area of the country
     */
    public double getSize() {
        return size;
    }

    /**
     * Gets the country's population.
     * @return the population of the country
     */
    public double getPopulation() {
        return population;
    }
}
