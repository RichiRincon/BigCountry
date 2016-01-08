package com.rrinconapps.bigcountry;

/**
 * Created by Ricardo on 19/12/2015.
 */
public class Country {
    private String name;
    private int flag_id;
    private double size;
    private double population;

    public Country(String name, int flag_id, double size, double population) {
        this.name = name;
        this.flag_id = flag_id;
        this.size = size;
        this.population = population;
    }

    /**
     * Indicates if the country is bigger than another one
     * @param otherCountry to compare with
     * @return true if it is bigger or false in other case
     */
    public boolean isBigger(Country otherCountry) {
        return this.getSize() > otherCountry.getSize();
    }

    public String getName() {
        return name;
    }

    public int getFlag_id() {
        return flag_id;
    }

    public double getSize() {
        return size;
    }

    public double getPopulation() {
        return population;
    }
}
