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
        char[] symbols = word.toCharArray();

        // variable currentStates for all possible states of the
        // non-deterministic automaton
        Set<String> currentStates = new HashSet<>();
        currentStates.add(initialState);
        // epsilon transitions from initial state:
        currentStates = followEpsilonTransitions(currentStates);

        //
        for (char symbol : symbols) {
            Set<String> nextStates = new HashSet<>();

            // there is one currentState for each of the non-deterministic paths:
            for (String currentState : currentStates) {
                Set<Transition> stateTransitions =
                        transitions.getOrDefault(currentState, Collections.emptySet());
                        // instead of null, Collections.emptySet() is returned if no
                        // transitions are defined for the currentState
                for (Transition stateTransition : stateTransitions) {
                    // Only add the stateTransition if it's a valid transition for the symbol
                    if (stateTransition.readSymbol() != null &&
                            stateTransition.readSymbol() == symbol) {
                        nextStates.add(stateTransition.toState());
                    }
                }
            }
            // follow newly found epsilon transitions
            nextStates = followEpsilonTransitions((nextStates));
            currentStates = nextStates;
        }

        // accepts if any current state accepts:
        return currentStates.stream().anyMatch(acceptingStates::contains);
    }

    private Set<String> followEpsilonTransitions(Set<String> states) {
        Set<String> reachableStates = new HashSet<>(states);
        Set<String> newStates = new HashSet<>(states);

        while (!newStates.isEmpty()) {
            Set<String> temporaryStates = new HashSet<>();
            for (String state : newStates) {
                Set<Transition> stateTransitions = transitions.getOrDefault(state, Collections.emptySet());
                for (Transition transition : stateTransitions) {
                    if (transition.readSymbol() == null) {
                        if (reachableStates.add(transition.toState())) {
                            temporaryStates.add(transition.toState());
                        }
                    }
                }
            }
            newStates = temporaryStates;
        }

        return reachableStates;
    }
    /*
    @Override
public boolean acceptsWord(String word) {
    char[] symbols = word.toCharArray();

    Set<String> currentStates = new HashSet<>();
    currentStates.add(initialState);
    currentStates = followEpsilonTransitions(currentStates);

    for (char symbol : symbols) {
        Set<String> nextStates = new HashSet<>();

        for (String state : currentStates) {
            Set<Transition> stateTransitions = transitions.getOrDefault(state, Collections.emptySet());
            for (Transition transition : stateTransitions) {
                if (transition.readSymbol() != null && transition.readSymbol() == symbol) {
                    // Only add the next state if it's a valid transition for the symbol
                    nextStates.add(transition.toState());
                }
            }
        }
        // Follow epsilon transitions from the next states after reading the symbol
        nextStates = followEpsilonTransitions(nextStates);
        currentStates = nextStates;
    }

    // Check if any of the current states are accepting
    return currentStates.stream().anyMatch(acceptingStates::contains);
}

     */
}
