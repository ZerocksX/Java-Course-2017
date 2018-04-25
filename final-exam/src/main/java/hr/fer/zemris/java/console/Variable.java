package hr.fer.zemris.java.console;

/**
 * @author Pavao Jerebić
 */
public class Variable {
    private String name;
    private Double value;

    public Variable(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
