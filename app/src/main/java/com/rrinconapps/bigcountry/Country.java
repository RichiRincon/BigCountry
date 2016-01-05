package com.rrinconapps.bigcountry;

/**
 * Created by Ricardo on 19/12/2015.
 */
public class Country {
    private String _name;
    private int _flag_id;
    private double _size;
    private double _population;

    public Country(String name, int flag_id, double size, double population) {
        _name = name;
        _flag_id = flag_id;
        _size = size;
        _population = population;
    }

    /**
     * Indicates if the country is bigger than another one
     * @param otherCountry to compare with
     * @return true if it is bigger or false in other case
     */
    public boolean isBigger(Country otherCountry) {
        return this.get_size() > otherCountry.get_size();
    }

    public String get_name() {
        return _name;
    }

    public int get_flag_id() {
        return _flag_id;
    }

    public double get_size() {
        return _size;
    }

    public double get_population() {
        return _population;
    }
}
