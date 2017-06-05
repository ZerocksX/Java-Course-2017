package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;

/**
 * Engine that executes given SmartScript document with the given request context
 *
 * @author Pavao Jerebić
 */
public class SmartScriptEngine {

    /**
     * Document
     */
    private DocumentNode documentNode;
    /**
     * Context
     */
    private RequestContext requestContext;
    /**
     * Variable stack
     */
    private ObjectMultistack multistack = new ObjectMultistack();
    /**
     * Operations
     */
    private HashMap<String, OperationFunction> operations;

    /**
     * Visitor
     */
    private INodeVisitor visitor = new INodeVisitor() {
        @Override
        public void visitTextNode(TextNode node) {
            try {
                requestContext.write(node.getText());
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            ElementVariable variable = node.getVariable();
            ValueWrapper current = new ValueWrapper(processNumberElement(node.getStartExpression()));
            ValueWrapper stop = new ValueWrapper(processNumberElement(node.getEndExpression()));
            ValueWrapper step = node.getStepExpression() == null ? null : new ValueWrapper(processNumberElement(node.getStepExpression()));
            multistack.push(variable.getName(), current);
            while (current.numCompare(stop.getValue()) < 0) {
                for (int i = 0; i < node.numberOfChildren(); i++) {
                    node.getChild(i).accept(this);
                }
                multistack.pop(variable.getName());
                if (step != null) {
                    current.add(step.getValue());
                    multistack.push(variable.getName(), current);
                }
            }
            multistack.pop(variable.getName());
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            Stack<Object> stack = new Stack<>();
            for (Element element : node.getElements()) {
                if (element instanceof ElementConstantInteger) {
                    stack.push((double) ((ElementConstantInteger) element).getValue());
                } else if (element instanceof ElementConstantDouble) {
                    stack.push(((ElementConstantDouble) element).getValue());
                } else if (element instanceof ElementString) {
                    stack.push(removeQuotes(((ElementString) element).getValue()));
                } else if (element instanceof ElementVariable) {
                    stack.push(multistack.peek(((ElementVariable) element).getName()).getValue());
                } else if (element instanceof ElementOperator) {
                    processOperation(stack, ((ElementOperator) element).getSymbol());
                } else if (element instanceof ElementFunction) {

                    switch (((ElementFunction) element).getName()) {
                        case "@dup":
                            stack.push(stack.peek());
                            break;
                        case "@swap":
                            Object x = stack.pop();
                            Object y = stack.pop();
                            stack.push(x);
                            stack.push(y);
                            break;
                        default:
                            processOperation(stack, ((ElementFunction) element).getName());
                            break;
                    }
                } else {
                    throw new RuntimeException("Wrong element");
                }
            }
            List<String> data = new LinkedList<>();
            while (!stack.empty()) {
                Object value = stack.pop();
                if (value instanceof Double) {
                    if ((double) value % 1 == 0) {
                        value = ((Double) value).intValue();
                    }
                }
                data.add(0, value.toString());
            }
            try {
                for (String st : data) {
                    requestContext.write(st);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not write");
            }
        }

        /**
         * Helping method that processes the given operation
         * @param stack current stack
         * @param name name of the operation
         */
        private void processOperation(Stack<Object> stack, String name) {
            OperationFunction f = operations.get(name);
            Object param1 = null, param2 = null;
            if (f.nOfVariables == 1) {
                param1 = stack.pop();
            } else if (f.nOfVariables == 2) {
                param2 = stack.pop();
                param1 = stack.pop();
            }
            Object result = f.function.apply(param1, param2);
            if (result != null) {
                stack.push(result);
            }
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for (int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(this);
            }
        }
    };

    /**
     * Helping method that processes number from given Element
     *
     * @param element element
     * @return number value from element
     * @throws RuntimeException if element's type is not valid
     */
    private Double processNumberElement(Element element) {
        if (element instanceof ElementConstantDouble) {
            return ((ElementConstantDouble) element).getValue();
        } else if (element instanceof ElementConstantInteger) {
            return (double) ((ElementConstantInteger) element).getValue();
        } else if (element instanceof ElementString) {
            return Double.parseDouble(((ElementString) element).getValue());
        } else {
            throw new RuntimeException("Not a valid type");
        }
    }

    /**
     * Constructor that sets document and context
     *
     * @param documentNode   document
     * @param requestContext context
     * @throws IllegalArgumentException if any parameter is null
     */
    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        if (documentNode == null
                || requestContext == null) {
            throw new IllegalArgumentException("All parameters must not be null");
        }
        this.documentNode = documentNode;
        this.requestContext = requestContext;
        operations = new HashMap<>();
        operations.put(
                "@sin",
                new OperationFunction(
                        1,
                        (param1, param2) -> {
                            if (!(param1 instanceof Double)) {
                                throw new RuntimeException("Parameter of invalid type");
                            }
                            return Math.sin((double) param1);
                        }
                )
        );
        operations.put(
                "@decfmt",
                new OperationFunction(
                        2,
                        (param1, param2) -> {
                            if ((!(param1 instanceof Double) && !(param1 instanceof String)) || !(param2 instanceof String)) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            String format = (String) param2;
                            NumberFormat formatter = new DecimalFormat(format);
                            return formatter.format(Double.valueOf(param1.toString()));
                        }
                )
        );
        operations.put(
                "@setMimeType",
                new OperationFunction(
                        1,
                        (param1, param2) -> {
                            if (!(param1 instanceof String)) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            String mime = (String) param1;
                            requestContext.setMimeType(mime);
                            return null;
                        }
                )
        );
        operations.put(
                "@paramGet",
                new OperationFunction(
                        2,
                        (param1, param2) -> {
                            if (!(param1 instanceof String) || (!(param2 instanceof Double) && !(param2 instanceof String))) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            return Double.valueOf(requestContext.getParameters().getOrDefault(param1, param2.toString()));
                        }
                )
        );
        operations.put(
                "@pparamGet",
                new OperationFunction(
                        2,
                        (param1, param2) -> {
                            if (!(param1 instanceof String) || (!(param2 instanceof Double) && !(param2 instanceof String))) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            return Double.valueOf(requestContext.getPersistentParameters().getOrDefault(param1, param2.toString()));
                        }
                )
        );
        operations.put(
                "@tparamGet",
                new OperationFunction(
                        2,
                        (param1, param2) -> {
                            if (!(param1 instanceof String) || (!(param2 instanceof Double) && !(param2 instanceof String))) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            return Double.valueOf(requestContext.getTemporaryParameters().getOrDefault(param1, param2.toString()));
                        }
                )
        );
        operations.put(
                "@pparamSet",
                new OperationFunction(
                        2,
                        (param2, param1) -> {
                            if (!(param1 instanceof String) || (!(param2 instanceof Double) && !(param2 instanceof String))) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            requestContext.setPersistentParameter(((String) param1), param2.toString());
                            return null;
                        }
                )
        );
        operations.put(
                "@tparamSet",
                new OperationFunction(
                        2,
                        (param2, param1) -> {
                            if (!(param1 instanceof String) || (!(param2 instanceof Double) && !(param2 instanceof String))) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            requestContext.setTemporaryParameter(((String) param1), param2.toString());
                            return null;
                        }
                )
        );
        operations.put(
                "@pparamDel",
                new OperationFunction(
                        1,
                        (param1, param2) -> {
                            if (!(param1 instanceof String)) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            requestContext.removePersistentParameter(((String) param1));
                            return null;
                        }
                )
        );
        operations.put(
                "@tparamDel",
                new OperationFunction(
                        1,
                        (param1, param2) -> {
                            if (!(param1 instanceof String)) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            requestContext.removeTemporaryParameter(((String) param1));
                            return null;
                        }
                )
        );

        operations.put(
                "*",
                new OperationFunction(
                        2,
                        (param1, param2) -> {
                            if (!(param1 instanceof Double) || !(param2 instanceof Double)) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            return Double.valueOf(param1.toString()) * Double.valueOf(param2.toString());
                        }
                )
        );
        operations.put(
                "+",
                new OperationFunction(
                        2,
                        (param1, param2) -> {
                            if (!(param1 instanceof Double) || !(param2 instanceof Double)) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            return (double) param1 + (double) param2;
                        }
                )
        );
        operations.put(
                "-",
                new OperationFunction(
                        2,
                        (param1, param2) -> {
                            if (!(param1 instanceof Double) || !(param2 instanceof Double)) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            return (double) param1 - (double) param2;
                        }
                )
        );
        operations.put(
                "/",
                new OperationFunction(
                        2,
                        (param1, param2) -> {
                            if (!(param1 instanceof Double) || !(param2 instanceof Double)) {
                                throw new RuntimeException("Parameters of invalid type");
                            }
                            return (double) param1 / (double) param2;
                        }
                )
        );
    }

    /**
     * Method that executes given document
     */
    public void execute() {
        documentNode.accept(visitor);
    }

    /**
     * Helping method that removes quotes, does not check for quotes, can throw exception
     *
     * @param string string
     * @return string without quotes
     * @throws IndexOutOfBoundsException if string length is 1 or less
     */
    private String removeQuotes(String string) {
        string = string.substring(1, string.length() - 1);
        return string;
    }

    /**
     * Helping class that describes the operation
     */
    private static class OperationFunction {
        /**
         * Operation
         */
        BiFunction<Object, Object, Object> function;
        /**
         * Number of needed variables to pop from stack
         */
        Integer nOfVariables;

        /**
         * Basic constructor
         *
         * @param nOfVariables number of variables
         * @param function     operation
         * @throws IllegalArgumentException if any parameter is null
         */
        public OperationFunction(Integer nOfVariables, BiFunction<Object, Object, Object> function) {
            if (nOfVariables == null || function == null) {
                throw new IllegalArgumentException("All parameters must not be null");
            }
            this.function = function;
            this.nOfVariables = nOfVariables;
        }
    }

}
