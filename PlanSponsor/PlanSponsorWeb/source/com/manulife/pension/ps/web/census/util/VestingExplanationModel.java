package com.manulife.pension.ps.web.census.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.manulife.pension.ps.web.census.util.VestingExplanationRetriever.VestingExplanationParameters;
import com.manulife.pension.util.Pair;

class VestingExplanationModel {
    
    //*********************************************************
    // interface - consisting of build and evaluate operations
    //*********************************************************
    
    VestingExplanationModel() { builder = new ModelBuilder(); }
    
    
    // build hierarchy
    
    ModelBuilder getBuilder() { return builder; }
    
    enum EvaluationType {
        SHORT_CIRCUIT,
        COMPLETE;
    }
    
    static interface EvaluationStrategy {
        boolean evaluate(VestingExplanationParameters parms);
    }
    
    private static final Node NODE_PLACEHOLDER = new Node(
            new EvaluationStrategy() {
                public boolean evaluate(VestingExplanationParameters parms) { return false; }
            },
            0);
    class ModelBuilder {
        private Node root;
        private ModelBuilder() { this.root = NODE_PLACEHOLDER; }
        void setLevel(EvaluationType type, int... nodePath) {
            Node node = new Node(type);
            setNode(node, nodePath);
        }
        void setCondition(EvaluationStrategy condition, int cmaId, int... nodePath) {
            Node node = new Node(condition, cmaId);
            setNode(node, nodePath);
        }
        void releaseObject() {
            checkModel();
            VestingExplanationModel.this.evaluator = new ModelEvaluator(root);
            root = null;
            VestingExplanationModel.this.builder = null;
        }
        private void setNode(Node node, int... nodePath) {
            
            if (nodePath.length == 0) {
                
                // set the root node
                if (root == NODE_PLACEHOLDER) {
                    root = node;
                } else {
                    throw new IllegalArgumentException("Root is being reset");
                }
                
            } else {
                
                // locate the parent node
                Node visitor = root;
                for (int i = 0; i < nodePath.length - 1; i ++) {
                    visitor = visitor.children.get(nodePath[i] - 1); // nodePath values are indexed from 1
                }
                
                ArrayList<Node> children = visitor.children;
                int index = nodePath[nodePath.length - 1] - 1; // nodePath values are indexed from 1
                
                // fill in placeholders
                for (int i = children.size(); i < index; i ++) {
                    children.add(NODE_PLACEHOLDER);
                }
                
                if (index < children.size()) {
                    // replace placeholder
                    Node existingNode = children.set(index, node);
                    if (existingNode != NODE_PLACEHOLDER) {
                        throw new IllegalArgumentException("Node is being reset");
                    }
                } else {
                    // add to end of children list
                    children.add(node);
                }
                
            }
        }
        private void checkModel() {
            
            // check no childless nodes
            
            ArrayList<Integer[]> errorList = new ArrayList<Integer[]>();
            
            if (! root.isLeaf()) {
                
                if (root.children.size() <= 0) {
                    errorList.add(new Integer[] { 0 });
                } else {
                    
                    Stack<Pair<Node, Integer>> state = new Stack<Pair<Node, Integer>>();
                    state.push(new Pair<Node, Integer>(root, 0));
                    
                    do {
                        
                        Pair<Node, Integer> stateIndex = state.peek();
                        Node visitor = stateIndex.getFirst().children.get(stateIndex.getSecond());
                        while (! visitor.isLeaf()) {
                            if (visitor.children.isEmpty()) {
                                ArrayList<Integer> path = new ArrayList<Integer>();
                                for (Pair<Node, Integer> index : state) {
                                    path.add(index.getSecond() + 1); // path indexed from 1
                                }
                                errorList.add(path.toArray(new Integer[0]));
                                break;
                            } else {
                                stateIndex = new Pair<Node, Integer>(visitor, 0);
                                state.push(stateIndex);
                                visitor = stateIndex.getFirst().children.get(stateIndex.getSecond());
                            }
                        }
                        
                        while (stateIndex.getSecond() + 1 >= stateIndex.getFirst().children.size()) {
                            
                            state.pop();
                            if (! state.isEmpty()) {
                                stateIndex = state.peek();
                            } else {
                                stateIndex = null;
                                break;
                            }
                            
                        }
                        
                        if (stateIndex != null) {
                            stateIndex = new Pair<Node, Integer>(stateIndex.getFirst(), stateIndex.getSecond() + 1);
                            state.pop();
                            state.push(stateIndex);
                        }
                        
                    } while (! state.isEmpty());
                    
                }
            }
            
            if (! errorList.isEmpty()) {
                
                StringBuilder errorListSb = new StringBuilder();
                for (Integer[] path : errorList) {
                    errorListSb.append("[");
                    for (Integer index : path) {
                        errorListSb.append(index);
                        errorListSb.append(",");
                    }
                    errorListSb.replace(errorListSb.length() - 1, errorListSb.length(), "]");
                }
                throw new IllegalStateException("Illegal message hierarchy: childless nodes at the following paths" + errorListSb);
                
            }
            
        }
    }
    
    
    // evaluate
    
    List<Integer> getCmaList(VestingExplanationParameters parms) {
        if (evaluator == null) {
            throw new IllegalStateException("Model has not yet been released from builder");
        }
        return evaluator.evaluate(parms);
    }
    
    
    //**************************************************************
    // internals - consisting of structural and evaluation elements
    //**************************************************************
    
    private ModelBuilder builder;
    private ModelEvaluator evaluator;
    
    private static class NodeEvaluation {
        private EvaluationType type;
        private Iterator<Node> childIterator;
        private boolean aggregateEvaluation;
        private NodeEvaluation(EvaluationType type, Iterator<Node> childIterator, boolean aggregateEvaluation) {
            this.type = type;
            this.childIterator = childIterator;
            this.aggregateEvaluation = aggregateEvaluation;
        }
    }
    
    private static class Node {
        private final EvaluationType type;
        private final EvaluationStrategy condition;
        private final Integer cmaId;
        private final ArrayList<Node> children;
        private Node(EvaluationType type) {
            this.type = type;
            this.condition = null;
            this.cmaId = null;
            children = new ArrayList<Node>();
        }
        private Node(EvaluationStrategy condition, int cmaId) {
            this.type = null;
            this.condition = condition;
            this.cmaId = cmaId;
            children = null;
        }
        private boolean isLeaf() { return children == null; }
    }
    
    private static class ModelEvaluator {
        private final Node root;
        private ModelEvaluator(Node root) { this.root = root; }
        private List<Integer> evaluate(VestingExplanationParameters parms) {
            
            List<Integer> cmaList = new ArrayList<Integer>();
            
            Stack<NodeEvaluation> state = new Stack<NodeEvaluation>();
            if (root.isLeaf()) {
                
                tabulateNode(cmaList, root, parms);
                
            } else {
                
                state.push(new NodeEvaluation(root.type, root.children.iterator(), false));
                
                do {
                    
                    // evaluate
                    NodeEvaluation stateIndex = state.peek();
                    Node nodeVisitor = stateIndex.childIterator.next();
                    while (! nodeVisitor.isLeaf()) {
                        stateIndex = state.push(new NodeEvaluation(nodeVisitor.type, nodeVisitor.children.iterator(), false));
                        nodeVisitor = stateIndex.childIterator.next();
                    }
                    stateIndex.aggregateEvaluation |= tabulateNode(cmaList, nodeVisitor, parms);
                    
                    // aggregate
                    while (! stateIndex.childIterator.hasNext()
                            || stateIndex.type == EvaluationType.SHORT_CIRCUIT && stateIndex.aggregateEvaluation) {
                        boolean aggregand = state.pop().aggregateEvaluation;
                        if (state.isEmpty()) {
                            break;
                        }
                        stateIndex = state.peek();
                        stateIndex.aggregateEvaluation |= aggregand;
                    }
                    
                } while (! state.isEmpty());
                
            }
            
            return cmaList;
            
        }
        private boolean tabulateNode(List<Integer> cmaList, Node node, VestingExplanationParameters parms) {
            boolean value = node.condition.evaluate(parms);
            if (value) {
                cmaList.add(node.cmaId);
            }
            return value;
        }
    }
    
}
