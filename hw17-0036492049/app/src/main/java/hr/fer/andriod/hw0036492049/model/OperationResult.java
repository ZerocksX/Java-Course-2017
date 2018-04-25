package hr.fer.andriod.hw0036492049.model;

import java.io.Serializable;

/**
 * Representation of a operation with its parameters and the result<br/>
 * Also stores the name of the operation
 * Created by pavao on 28.06.17..
 */

public class OperationResult implements Serializable {
    /**
     * First parameter
     */
    private int first;
    /**
     * Second parameter
     */
    private int second;
    /**
     * Result
     */
    private double result;
    /**
     * Operation name
     */
    private String operation;

    /**
     * Basic constructor
     *
     * @param first     first parameter
     * @param second    second parameter
     * @param result    result
     * @param operation operation name
     */
    public OperationResult(int first, int second, double result, String operation) {
        this.first = first;
        this.second = second;
        this.result = result;
        this.operation = operation;
    }

    /**
     * Getter for first
     *
     * @return first
     */
    public int getFirst() {
        return first;
    }

    /**
     * Setter for first
     *
     * @param first first
     */
    public void setFirst(int first) {
        this.first = first;
    }

    /**
     * Getter for second
     *
     * @return second
     */
    public int getSecond() {
        return second;
    }

    /**
     * Setter for second
     *
     * @param second second
     */
    public void setSecond(int second) {
        this.second = second;
    }

    /**
     * Getter for result
     *
     * @return result
     */
    public double getResult() {
        return result;
    }

    /**
     * Setter for result
     *
     * @param result result
     */
    public void setResult(double result) {
        this.result = result;
    }

    /**
     * Getter for operation
     *
     * @return operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Setter for operation
     *
     * @param operation operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return String.format("Rezultat operacije %s je %s.", operation, result);
    }
}
