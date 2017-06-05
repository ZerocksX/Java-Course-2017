package hr.fer.zemris.java.gui.layouts;

/**
 * Position used in {@link CalcLayout}
 *
 * @author Pavao Jerebić
 */
public class RCPosition {
    /**
     * Row
     */
    private int row;
    /**
     * Column
     */
    private int column;

    /**
     * Basic constructor
     *
     * @param row    row
     * @param column column
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Getter for row
     *
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for column
     *
     * @return column
     */
    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RCPosition)) return false;

        RCPosition that = (RCPosition) o;

        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    /**
     * Helping method that parses given string into {@link RCPosition} in format '%d,%d'
     *
     * @param st string
     * @return RCPosition representation of the given string
     */
    public static RCPosition parse(String st) {
        if (st == null) {
            throw new IllegalArgumentException("Given string can not be null");
        }

        String[] data = st.split(",");

        if (data.length != 2) {
            throw new IllegalArgumentException("Invalid input. Format is '%d,%d'");
        }

        try {
            int r = Integer.parseInt(data[0].trim());
            int c = Integer.parseInt(data[1].trim());
            return new RCPosition(r, c);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid input. Format is '%d,%d'");
        }
    }
}
