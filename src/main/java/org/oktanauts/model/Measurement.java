package org.oktanauts.model;


/**
 * This class is the measurement model class
 */
public class Measurement {
    private String code;
    private String type;
    private double value;
    private String unit;

    /**
     * Measurement Constructor
     *
     * @param code the LOINC code of the measurement
     * @param type the name of the measurement
     * @param value the double value of the measurement
     * @param unit the unit that the measurement value is expressed in
     */
    public Measurement(String code, String type, double value, String unit) {
        System.out.println(code + ", " + type + ", " + value + ", " + unit);
        this.code = code;
        this.type = type;
        this.value = value;
        this.unit = unit;
    }

    /**
     * Gets the LOINC code of the measurement
     *
     * @return the LOINC code of the measurement
     */
    public String getCode() { return this.code; }

    /**
     * Gets the value of the measurement
     *
     * @return the float value of the measurement
     */
    public double getValue() { return this.value; }

    /**
     * Gets a formatted string version of the measurement
     *
     * @return the concatenated string of the value and unit
     */
    public String toString() { return this.value + " " + this.unit; }

    public String getType(){
        return type;
    }
}
