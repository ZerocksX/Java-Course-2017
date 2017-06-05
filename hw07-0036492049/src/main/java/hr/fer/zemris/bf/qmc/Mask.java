package hr.fer.zemris.bf.qmc;

import hr.fer.zemris.bf.utils.Util;

import java.util.*;

/**
 * Representation of a mask used in QMC minimisation algorithm<br/>
 * It stores values of a given implicant. 0 stands for negated, 1 for regular and 2 for both
 * This class also stores indexes represented by this implicant
 *
 * @author Pavao Jerebić
 */
public class Mask {

    /**
     * Values
     */
    private byte[] values;
    /**
     * Indexes
     */
    private Set<Integer> indexes;
    /**
     * Don't care flag
     */
    private boolean dontCare;
    /**
     * Hash code
     */
    private int hashCode;
    /**
     * Is combined flag
     */
    private boolean combined;

    /**
     * Constructor for only one index representation
     *
     * @param index             index
     * @param numberOfVariables number of variables
     * @param dontCare          don't care flag
     * @throws IllegalArgumentException if number of variables is < 1
     */
    public Mask(int index, int numberOfVariables, boolean dontCare) {
        if (numberOfVariables < 1) {
            throw new IllegalArgumentException("Number of variables must be greater than 0");
        }
        values = Util.indexToByteArray(index, numberOfVariables);
        this.dontCare = dontCare;
        indexes = new LinkedHashSet<>(numberOfVariables);
        indexes.add(index);
        this.hashCode = Arrays.hashCode(values);
    }

    /**
     * Constructor that creates an implicant representation of multiple indexes
     *
     * @param values   values
     * @param indexes  indexes
     * @param dontCare dont care
     * @throws IllegalArgumentException if values are invalid
     */
    public Mask(byte[] values, Set<Integer> indexes, boolean dontCare) {
        if (values == null
                || indexes == null
                || indexes.size() < 1
                || values.length < 1) {
            throw new IllegalArgumentException("Values are invalid");
        }
        this.values = values;
        this.indexes = indexes;
        this.dontCare = dontCare;
        hashCode = Arrays.hashCode(values);
    }

    /**
     * Getter for values
     *
     * @return values
     */
    public byte[] getValues() {
        return values;
    }

    /**
     * Getter for dont care
     *
     * @return dont care
     */
    public boolean isDontCare() {
        return dontCare;
    }

    /**
     * Getter for combined
     *
     * @return combined
     */
    public boolean isCombined() {
        return combined;
    }

    /**
     * Setter for combined
     *
     * @param combined combined
     */
    public void setCombined(boolean combined) {
        this.combined = combined;
    }

    /**
     * Getter for indexes
     *
     * @return indexes
     */
    public Set<Integer> getIndexes() {
        return Collections.unmodifiableSortedSet(new TreeSet<>(indexes));
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mask)) {
            return false;
        }
        Mask otherMask = (Mask) o;
        return Arrays.equals(values, otherMask.values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (byte b : values) {
            if (b == 2) {
                sb.append("-");
            } else {
                sb.append(b);
            }
        }

        sb.append(" ").append(dontCare ? ('D') : ('.'))
                .append(" ").append(combined ? ('*') : (' '))
                .append(" ").append(getIndexes().toString());

        return sb.toString();

    }

    /**
     * Getter for values size
     *
     * @return values size
     */
    public int size() {
        return values.length;
    }

    /**
     * Getter for value at given position
     *
     * @param position position
     * @return value at position
     * @throws IllegalArgumentException if position is invalid
     */
    public byte getValueAt(int position) {
        if (position < 0 || position > size() - 1) {
            throw new IllegalArgumentException("Invalid position");
        }
        return values[position];
    }

    /**
     * Returns count of ones in values
     *
     * @return count of ones
     */
    public int countOfOnes() {
        int ones = 0;
        for (byte b : values) {
            if (b == 1) {
                ones++;
            }
        }
        return ones;
    }


    /**
     * Combines this mask with other if possible
     *
     * @param other other mask
     * @return a new mask, combination of this and other
     * @throws IllegalArgumentException if masks size don't match or if other is null
     */
    public Optional<Mask> combineWith(Mask other) {
        if (other == null
                || other.size() != this.size()) {
            throw new IllegalArgumentException("Invalid argument");
        }
        int differencePosition = -1;
        for (int i = 0, n = size(); i < n; ++i) {
            if (this.values[i] != other.values[i]) {
                if (differencePosition != -1) {
                    return Optional.empty();
                } else {
                    differencePosition = i;
                }
            }
        }

        byte[] newValues = Arrays.copyOf(values, size());
        newValues[differencePosition] = 2;
        HashSet<Integer> newIndexes = new HashSet<>(this.indexes);
        newIndexes.addAll(other.getIndexes());
        return Optional.of(new Mask(newValues, newIndexes, this.dontCare && other.dontCare));

    }
}
