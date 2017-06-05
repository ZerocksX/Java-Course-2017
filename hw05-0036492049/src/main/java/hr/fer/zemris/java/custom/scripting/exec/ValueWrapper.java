package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Wrapper for value<br/>
 * Can store null, Integer, Double or String
 *
 * @author Pavao Jerebić
 */
public class ValueWrapper {
    /**
     * Value
     */
    private Object value;

    /**
     * Constructor that sets value
     *
     * @param value value
     * @throws RuntimeException if value is not an instance of Integer,Double,String or is not null
     */
    public ValueWrapper(Object value) {
        this.value = value;
    }

    /**
     * Helping method to process value
     *
     * @param value value
     * @return value as Integer, Double or String
     * @throws RuntimeException if value is not an instance of Integer,Double,String or is not null
     */
    private Object processValue(Object value) {
        if (value == null) {
            return 0;
        } else if (value instanceof Integer) {
            return value;
        } else if (value instanceof Double) {
            return value;
        } else if (value instanceof String) {
            try {
                return Integer.valueOf((String) value);
            } catch (Exception ex1) {
                try {
                    return Double.valueOf((String) value);
                } catch (Exception ex2) {
                    throw new RuntimeException(ex2.getMessage());
                }
            }
        } else {
            throw new RuntimeException("Value's type is not appropriate");
        }
    }


    /**
     * Process value as double
     *
     * @param value value
     * @return value as double
     * @throws RuntimeException if value is not an instance of Integer or Double
     */
    private Double processValueAsDouble(Object value) {
        value = processValue(value);
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return Double.valueOf((Integer) value);
        } else {
            throw new RuntimeException("Value's type is not appropriate");
        }
    }

    /**
     * Getter for value
     *
     * @return value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Setter for value
     *
     * @param value value
     * @throws RuntimeException if value is not an instance of Integer or Double
     */
    public void setValue(Object value) {
        if (value == null) {

            this.value = null;

        } else {
            try {

                this.value = processValue(value);

            } catch (RuntimeException ex) {

                if (value instanceof String) {
                    this.value = value;
                } else {
                    throw new RuntimeException("Value's type is not appropriate");
                }
            }
        }

    }

    /**
     * Adds incValue to value
     *
     * @param incValue increment value
     * @throws RuntimeException if value is not an instance of Integer or Double
     */
    public void add(Object incValue) {
        Object o = processValue(incValue);
        Object value = processValue(this.value);
        if (value instanceof Double || o instanceof Double) {
            Double result = processValueAsDouble(value);
            result = result + processValueAsDouble(o);
            this.value = result;
        } else if (o instanceof Integer && (value == null || value instanceof Integer)) {
            Integer result = (Integer) processValue(value);
            result = result + (Integer) o;
            this.value = result;
        } else {
            throw new RuntimeException("Value's type is not appropriate");
        }
    }


    /**
     * Subtracts decValue from value
     *
     * @param decValue decrement value
     * @throws RuntimeException if value is not an instance of Integer or Double
     */
    public void subtract(Object decValue) {
        Object o = processValue(decValue);
        Object value = processValue(this.value);
        if (value instanceof Double || o instanceof Double) {
            Double result = processValueAsDouble(value);
            result = result - processValueAsDouble(o);
            this.value = result;
        } else if (o instanceof Integer && (value == null || value instanceof Integer)) {
            Integer result = (Integer) processValue(value);
            result = result - (Integer) o;
            this.value = result;
        } else {
            throw new RuntimeException("Value's type is not appropriate");
        }
    }

    /**
     * Multiplies value with multiplication value
     *
     * @param mulValue multiplication value
     * @throws RuntimeException if value is not an instance of Integer or Double
     */
    public void multiply(Object mulValue) {
        Object o = processValue(mulValue);
        Object value = processValue(this.value);
        if (value instanceof Double || o instanceof Double) {
            Double result = processValueAsDouble(value);
            result = result * processValueAsDouble(o);
            this.value = result;
        } else if (o instanceof Integer && (value == null || value instanceof Integer)) {
            Integer result = (Integer) processValue(value);
            result = result * (Integer) o;
            this.value = result;
        } else {
            throw new RuntimeException("Value's type is not appropriate");
        }
    }

    /**
     * Divides value with division value
     *
     * @param divValue division value
     * @throws RuntimeException if value is not an instance of Integer or Double
     */
    public void divide(Object divValue) {
        Object o = processValue(divValue);
        Object value = processValue(this.value);
        if (value instanceof Double || o instanceof Double) {
            Double result = processValueAsDouble(value);
            if (processValueAsDouble(o) == 0) {
                throw new RuntimeException("Can not divide with 0");
            }
            result = result / processValueAsDouble(o);
            this.value = result;
        } else if (o instanceof Integer && (value == null || value instanceof Integer)) {
            Integer result = (Integer) processValue(value);
            if (processValueAsDouble(o) == 0) {
                throw new RuntimeException("Can not divide with 0");
            }
            result = result / (Integer) o;
            this.value = result;
        } else {
            throw new RuntimeException("Value's type is not appropriate");
        }
    }

    /**
     * Compares value with given value<br/>
     * null is compared as {@link Integer#valueOf(int 0)}
     *
     * @param withValue given value
     * @return less than 0 if this value is smaller than given value, more than 0 if it is greater, 0 if it is equal
     * @throws RuntimeException if value is not an instance of Integer or Double
     */
    public int numCompare(Object withValue) {
        Object tempValue = value;
        Object value = processValue(this.value);
        if (value == null || withValue == null) {
            if (value == null && withValue == null) {
                return 0;
            } else {
                tempValue = processValue(this.value);
            }
        }
        Object o = processValue(withValue);
        ValueWrapper tempWrapper = new ValueWrapper(tempValue);
        tempWrapper.subtract(o);
        int result;
        if (tempWrapper.getValue() instanceof Double) {
            result = Math.abs((Double) tempWrapper.getValue()) > 1
                    ? ((Double) tempWrapper.getValue()).intValue()
                    : ((Double) tempWrapper.getValue() == 0
                        ? 0
                        : ((Double) tempWrapper.getValue() < 0
                            ? -1
                            : 1));
        } else if (tempWrapper.getValue() instanceof Integer) {
            result = (Integer) tempWrapper.getValue();
        } else {
            throw new RuntimeException("Value's type is not appropriate");
        }
        return result;
    }

}
