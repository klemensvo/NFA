package ab1.impl.GRUPPE;

import ab1.FinalizedStateException;
import ab1.NFA;
import ab1.Transition;

import java.util.*;

public class NFAImpl implements NFA {
    String initialState;
    Set<String> states = new HashSet<>();
    Set<String> acceptingStates = new HashSet<>();
    final private Map<String, Set<Transition>> transitions = new HashMap<>();

    boolean isFinalized = false;

    public NFAImpl(String initialState) {
        this.initialState = initialState;
    }

    @Override
    public Set<String> getStates() {
        return states;
    }

    @Override
    public Collection<Transition> getTransitions() {
        Set<Transition> allTransitions = new HashSet<>();
        for (Set<Transition> transitionSet : transitions.values()) {
            allTransitions.addAll(transitionSet);
        }
        return allTransitions;
    }

    @Override
    public Set<String> getAcceptingStates() {
        return acceptingStates;
    }

    @Override
    public String getInitialState() {
        return initialState;
    }

    @Override
    public void addTransition(Transition transition) throws FinalizedStateException {
        if (isFinalized) {
            throw new FinalizedStateException();
        }
        // add any state appearing in fromState or toState to states
        states.add(transition.fromState());
        states.add(transition.toState());
        // add transitions from states
        Set<Transition> stateTransitions =
                transitions.computeIfAbsent(transition.fromState(),
                        currentState -> new HashSet<>());
        stateTransitions.add(transition);
    }

    @Override
    public void addAcceptingState(String state) throws FinalizedStateException {
        acceptingStates.add(state);
        states.add(state); // add acceptingStates to states as well
    }

    @Override
    public NFA union(NFA other) throws FinalizedStateException {
        return null;
    }

    @Override
    public NFA intersection(NFA other) throws FinalizedStateException {
        return null;
    }

    @Override
    public NFA concatenation(NFA other) throws FinalizedStateException {
        return null;
    }

    @Override
    public NFA kleeneStar() throws FinalizedStateException {
        return null;
    }

    @Override
    public NFA plusOperator() throws FinalizedStateException {
        return null;
    }

    @Override
    public NFA complement() throws FinalizedStateException {
        return null;
    }

    @Override
    public boolean isFinalized() {
        return isFinalized;
    }

    @Override
    public void finalizeAutomaton() {
        isFinalized = true;
    }

    @Override
    public boolean isFinite() { // todo
        return false;
    }

    @Override
    public boolean acceptsWord(String word) {
        boolean isAccepted = false;
        char[] symbols = word.toCharArray();

        // variable currentStates for all possible states of the
        // non-deterministic automaton
        Set<String> currentStates = new HashSet<>();
        currentStates.add(initialState);

        for (char symbol : symbols) {
            Set<String> nextStates = new HashSet<>();
            for (String state : currentStates) {
                Set<Transition> stateTransitions =
                        transitions.getOrDefault(state, Collections.emptySet());
                for (Transition transition : stateTransitions) {
                    if (transition.readSymbol() != null &&
                            transition.readSymbol() == symbol) {
                        nextStates.add(transition.toState());
                    }
                }
            }
            currentStates = nextStates;
        }
        for (String state : currentStates) {
            if (acceptingStates.contains(state)) {
                isAccepted = true;
            }
        }

        return isAccepted;
    }
}
