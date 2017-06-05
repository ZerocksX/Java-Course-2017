package hr.fer.zemris.bf.qmc;

import hr.fer.zemris.bf.model.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Minimizer using Quine–McCluskey algorithm<br/>
 * <a>https://en.wikipedia.org/wiki/Quine%E2%80%93McCluskey_algorithm</a>
 *
 * @author Pavao Jerebić
 */
public class Minimizer {
    /**
     * Minterm set
     */
    private Set<Integer> mintermSet;
    /**
     * Don't care set
     */
    private Set<Integer> dontCareSet;
    /**
     * Variables
     */
    private List<String> variables;

    /**
     * Minimal forms
     */
    public List<Set<Mask>> minimalForms;

    /**
     * Number of variables
     */
    private int nOfVariables;
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger("hr.fer.zemris.bf.qmc");

    /**
     * Constructor that takes minterm set, dontcareset, variables and produces a minimal form
     *
     * @param mintermSet  minterm set
     * @param dontCareSet don't care set
     * @param variables   variables
     * @throws IllegalArgumentException if arguments are invalid
     */
    public Minimizer(Set<Integer> mintermSet, Set<Integer> dontCareSet, List<String> variables) {
        if (mintermSet == null
                || dontCareSet == null
                || variables == null
                || mintermSet.size() > 1 << variables.size()
                || dontCareSet.size() > 1 << variables.size()) {
            throw new IllegalArgumentException("Invalid arguments");
        }


        checkNonOverlapping(mintermSet, dontCareSet);

        this.mintermSet = mintermSet;
        this.dontCareSet = dontCareSet;
        this.variables = variables;
        nOfVariables = variables.size();

        Set<Mask> primCover = findPrimaryImplicants();
        if (!primCover.isEmpty()) {
            this.minimalForms = chooseMinimalCover(primCover);
        }
    }

    /**
     * Getter for minimal forms
     *
     * @return minimal forms
     */
    public List<Set<Mask>> getMinimalForms() {
        return minimalForms;
    }

    /**
     * Helping method that finds primary implicants
     *
     * @return primary implicants
     */
    private Set<Mask> findPrimaryImplicants() {
        Set<Mask> primeImplicants = new LinkedHashSet<>();
        List<Set<Mask>> currentCollumn = createFirstColumn();
        logColumn(currentCollumn);
        while (currentCollumn.size() - 1 > 0) {
            List<Set<Mask>> nextCollumn = new ArrayList<>();
            for (int i = 0, n = currentCollumn.size() - 1; i < n; i++) {
                Set<Mask> lGroup = currentCollumn.get(i);
                Set<Mask> rGroup = currentCollumn.get(i + 1);
                Set<Mask> newGroup = new LinkedHashSet<>();
                for (Mask lMask : lGroup) {
                    for (Mask rMask : rGroup) {
                        Optional<Mask> result = lMask.combineWith(rMask);
                        if (result.isPresent()) {
                            lMask.setCombined(true);
                            rMask.setCombined(true);
                            newGroup.add(result.get());
                        }
                    }
                }
                lGroup.stream()
                        .filter(mask -> !mask.isCombined() && !mask.isDontCare())
                        .forEach(mask -> {
                            if (LOG.isLoggable(Level.FINEST)) {
                                LOG.finest(String.format("Pronašao primarni implikant: %s", mask.toString()));
                                LOG.finest("");
                            }
                            primeImplicants.add(mask);
                        });
                if (!newGroup.isEmpty()) nextCollumn.add(newGroup);
            }
            currentCollumn = nextCollumn;
            logColumn(currentCollumn);
        }
        if (currentCollumn.size() == 1) {
            currentCollumn.get(0).stream()
                    .filter(mask -> !mask.isCombined() && !mask.isDontCare())
                    .forEach(mask -> {
                        if (LOG.isLoggable(Level.FINEST)) {
                            LOG.finest(String.format("Pronašao primarni implikant: %s", mask.toString()));
                            LOG.finest("");
                        }
                        primeImplicants.add(mask);
                    });
        }

        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Svi primarni implikanti:");
            primeImplicants.forEach(mask -> LOG.fine(mask.toString()));
            LOG.fine("");
        }

        return primeImplicants;
    }

    /**
     * Logs current column
     *
     * @param column given column
     */
    private void logColumn(List<Set<Mask>> column) {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.finer("Stupac tablice:");
            LOG.finer("=================================");
            for (int i = 0, n = column.size(); i < n; i++) {
                Set<Mask> set = column.get(i);
                set.forEach(mask -> LOG.finer(mask.toString()));
                if (i < n - 1) {
                    LOG.finer("-------------------------------");
                }
            }
            LOG.finer("");
        }
    }

    /**
     * Helping method that creates the first column
     *
     * @return the first column
     */
    private List<Set<Mask>> createFirstColumn() {
        List<Set<Mask>> groups = new ArrayList<>();
        for (int i = 0; i <= nOfVariables; i++) {
            groups.add(new LinkedHashSet<>());
        }
        for (Integer minterm : mintermSet) {
            Mask mask = new Mask(minterm, nOfVariables, false);
            groups.get(mask.countOfOnes()).add(mask);
        }
        for (Integer dontCare : dontCareSet) {
            Mask mask = new Mask(dontCare, nOfVariables, true);
            groups.get(mask.countOfOnes()).add(mask);
        }
        return groups;
    }

    /**
     * Helping method that checks if sets overlap
     *
     * @param mintermSet  minterm set
     * @param dontCareSet don't care set
     * @throws IllegalArgumentException it they do
     */
    private void checkNonOverlapping(Set<Integer> mintermSet, Set<Integer> dontCareSet) {
        for (Integer i : mintermSet) {
            if (dontCareSet.contains(i)) {
                throw new IllegalArgumentException("Minterms and dontCares overlap");
            }
        }
    }

    /**
     * Helping method to create minimal cover
     *
     * @param primCover primary implicants
     * @return minimal cover usping primary implicants
     */
    private List<Set<Mask>> chooseMinimalCover(Set<Mask> primCover) {
        Mask[] implicants = primCover.toArray(new Mask[primCover.size()]);
        Integer[] minterms = mintermSet.toArray(new Integer[mintermSet.size()]);

        Map<Integer, Integer> mintermToColumnMap = new HashMap<>();
        for (int i = 0; i < minterms.length; i++) {
            Integer index = minterms[i];
            mintermToColumnMap.put(index, i);
        }

        boolean[][] table = buildCoverTable(implicants, minterms, mintermToColumnMap);


        boolean[] coveredMinterms = new boolean[minterms.length];


        Set<Mask> importantSet = selectImportantPrimaryImplicants(
                implicants, mintermToColumnMap, table, coveredMinterms);


        List<Set<BitSet>> pFunction = buildPFunction(table, coveredMinterms);

        if (emptyListSet(pFunction)) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Minimalni oblici funkcije su:");
                LOG.fine(importantSet.toString());
                LOG.fine("");
            }
            return new ArrayList<>(Collections.singletonList(importantSet));
        }


        Set<BitSet> minset = findMinimalSet(pFunction);


        List<Set<Mask>> minimalForms = new ArrayList<>();
        for (BitSet bs : minset) {
            Set<Mask> set = new LinkedHashSet<>(importantSet);
            bs.stream().forEach(i -> set.add(implicants[i]));
            minimalForms.add(set);
        }

        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Minimalni oblici funkcije su:");
            for (Set<Mask> form : minimalForms) {
                LOG.fine(form.toString());
            }
            LOG.fine("");
        }

        return minimalForms;
    }

    /**
     * Checks if given primary implicant function is empty
     *
     * @param pFunction primary implicant function
     * @return true if pFunction is empty
     */
    private boolean emptyListSet(List<Set<BitSet>> pFunction) {
        if (pFunction.isEmpty()) {
            return true;
        } else {
            for (Set<BitSet> set : pFunction) {
                if (!set.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Helping method that finds the minimal set
     *
     * @param pFunction primary implicant function
     * @return minimal set of primary implicants
     */
    private Set<BitSet> findMinimalSet(List<Set<BitSet>> pFunction) {
        Set<BitSet> products = new LinkedHashSet<>(pFunction.get(0));
        for (int i = 1, n = pFunction.size(); i < n; i++) {
            Set<BitSet> newProducts = new LinkedHashSet<>();
            for (BitSet product : products) {
                for (BitSet otherBitSet : pFunction.get(i)) {
                    BitSet result = BitSet.valueOf(product.toByteArray());
                    result.or(otherBitSet);
                    newProducts.add(result);
                }
            }
            products.clear();
            products = newProducts;
        }
        if (LOG.isLoggable(Level.FINER)) {
            LOG.finer("Nakon prevorbe p-funkcije u sumu produkata:");
            LOG.finer(products.toString());
            LOG.finer("");
        }

        Set<BitSet> minimalSet = new LinkedHashSet<>();
        int minimumLength = Integer.MAX_VALUE;
        for (BitSet product : products) {
            if (product.cardinality() < minimumLength) {
                minimumLength = product.cardinality();
                minimalSet.clear();
                minimalSet.add(product);
            } else if (product.cardinality() == minimumLength) {
                minimalSet.add(product);
            }
        }

        if (LOG.isLoggable(Level.FINER)) {
            LOG.finer("Minimalna pokrivanja još trebaju:");
            LOG.finer(minimalSet.toString());
            LOG.finer("");
        }


        return minimalSet;
    }

    /**
     * Helping method that build primary implicant function
     *
     * @param table           table that connects implicants and minterms
     * @param coveredMinterms covered minterms
     * @return primary implicant function
     */
    private List<Set<BitSet>> buildPFunction(boolean[][] table, boolean[] coveredMinterms) {
        List<Set<BitSet>> pFunction = new ArrayList<>();
        int nOfRows = table.length;
        int nOfCollumns = table[0].length;
        for (int j = 0; j < nOfCollumns; j++) {
            if (coveredMinterms[j]) continue;
            LinkedHashSet<BitSet> temp = new LinkedHashSet<>();
            for (int i = 0; i < nOfRows; i++) {
                if (table[i][j]) {
                    BitSet bitSet = new BitSet(nOfRows);
                    bitSet.set(i);
                    temp.add(bitSet);
                }
            }
            if (!temp.isEmpty()) {
                pFunction.add(temp);
            }
        }

        if (LOG.isLoggable(Level.FINER)) {
            LOG.finer("p funkcija je: ");
            LOG.finer(pFunction.toString());
            LOG.finer("");
        }

        return pFunction;
    }

    /**
     * Helping method that selects important primary implicants
     *
     * @param implicants         all implicants
     * @param mintermToColumnMap minterm to column map
     * @param table              implicant minterm relation
     * @param coveredMinterms    covered minterms
     * @return important primary implicants
     */
    private Set<Mask> selectImportantPrimaryImplicants(Mask[] implicants, Map<Integer, Integer> mintermToColumnMap, boolean[][] table, boolean[] coveredMinterms) {
        Set<Mask> importantPrimeImplicants = new LinkedHashSet<>();
        int nOfRows = table.length;
        int nOfCollumns = table[0].length;
        for (int j = 0; j < nOfCollumns; j++) {
            if (coveredMinterms[j]) continue;
            int position = -1;
            for (int i = 0; i < nOfRows; i++) {
                if (table[i][j]) {
                    if (position != -1) {
                        position = -1;
                        break;
                    } else {
                        position = i;
                    }
                }
            }
            if (position != -1) {
                importantPrimeImplicants.add(implicants[position]);
                implicants[position].getIndexes().forEach(integer -> {
                    Integer index = mintermToColumnMap.get(integer);
                    if (index != null) {
                        coveredMinterms[index] = true;
                    }
                });
            }
        }
        return importantPrimeImplicants;
    }

    /**
     * Helping method that builds cover table
     *
     * @param implicants         implicants
     * @param minterms           minterms
     * @param mintermToColumnMap minterm to column map
     * @return cover table
     */
    private boolean[][] buildCoverTable(Mask[] implicants, Integer[] minterms, Map<Integer, Integer> mintermToColumnMap) {
        int nOfImplicants = implicants.length, nOfMinterms = minterms.length;
        boolean[][] coverTable = new boolean[nOfImplicants][nOfMinterms];
        for (int i = 0; i < nOfImplicants; ++i) {
            Mask implicant = implicants[i];
            int finalI = i;
            implicant.getIndexes().forEach(integer -> {
                Integer position = mintermToColumnMap.get(integer);
                if (position != null) {
                    coverTable[finalI][position] = true;
                }
            });
        }
        return coverTable;
    }


    /**
     * Returns minimal forms as expression list
     *
     * @return Expressions list
     */
    public List<Node> getMinimalFormsAsExpressions() {
        if (minimalForms == null) {
            return Collections.singletonList(new ConstantNode(false));
        }
        if (minimalForms.size() == 1) {
            Set<Mask> form = minimalForms.get(0);
            if (form.size() == 1) {
                for (Mask mask : form) {
                    if (mask.getIndexes().size() == 1 << nOfVariables) {
                        return Collections.singletonList(new ConstantNode(true));
                    }
                }
            }
        }

        List<Node> expressions = new ArrayList<>();

        for (Set<Mask> form : minimalForms) {
            if (form.size() == 1) {
                for (Mask mask : form) {
                    addToListAsNode(expressions, form);
                }
            } else {
                List<Node> ands = new ArrayList<>();
                addToListAsNode(ands, form);
                expressions.add(new BinaryOperatorNode("or", ands, (a, b) -> a | b));
            }
        }
        return expressions;

    }

    /**
     * Helping method that creates a node from a mask for every element of the given form and adds it to a given list
     *
     * @param list list to add to
     * @param form form
     */
    private void addToListAsNode(List<Node> list, Set<Mask> form) {
        for (Mask mask : form) {
            List<Node> variables = getVariablesFromMask(mask);
            if (variables.size() == 1) {
                list.add(variables.get(0));
            } else {
                list.add(new BinaryOperatorNode("and", variables, (a, b) -> a & b));
            }
        }
    }


    /**
     * Returns variables from mask as list of nodes
     *
     * @param mask mask
     * @return variables
     */
    private List<Node> getVariablesFromMask(Mask mask) {
        List<Node> variables = new ArrayList<>();
        byte[] values = mask.getValues();
        for (int i = 0; i < values.length; i++) {
            switch (values[i]) {
                case 1:
                    variables.add(new VariableNode(this.variables.get(i)));
                    break;
                case 0:
                    variables.add(new UnaryOperatorNode("not", new VariableNode(this.variables.get(i)), a -> !a));
                    break;
            }
        }
        return variables;
    }

    /**
     * Returns minimal forms as a string representation of a boolean function
     *
     * @return minimal forms as string
     */
    public List<String> getMinimalFormsAsString() {
        List<Node> expressions = getMinimalFormsAsExpressions();
        List<String> expressionsAsString = new ArrayList<>();
        for (Node node : expressions) {
            ExpressionToStringVisitor visitor = new ExpressionToStringVisitor();
            node.accept(visitor);
            expressionsAsString.add(visitor.getValue());
        }
        return expressionsAsString;
    }

    /**
     * Helping class to create string representation of the given expression
     */
    private static class ExpressionToStringVisitor implements NodeVisitor {

        /**
         * String builder
         */
        StringBuilder sb = new StringBuilder();

        @Override
        public void visit(ConstantNode node) {
            sb.append(node.getValue());
        }

        @Override
        public void visit(VariableNode node) {
            sb.append(node.getName());
        }

        @Override
        public void visit(UnaryOperatorNode node) {
            sb.append(node.getName()).append(" ");
            node.getChild().accept(this);
        }

        @Override
        public void visit(BinaryOperatorNode node) {
            List<Node> children = node.getChildren();
            children.get(0).accept(this);
            for (int i = 1, n = children.size(); i < n; i++) {
                sb.append(" ").append(node.getName()).append(" ");
                children.get(i).accept(this);
            }
        }

        /**
         * String value of the given expression
         *
         * @return expression as string
         */
        private String getValue() {
            return sb.toString().toUpperCase();
        }
    }

}
