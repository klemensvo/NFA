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
        Set<Transition> resultTransitions = new HashSet<>();
        for (Set<Transition> transitionSet : transitions.values()) {
            resultTransitions.addAll(transitionSet);
        }
        return resultTransitions;
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
        if (this.isFinalized() || other.isFinalized()) {
            throw new FinalizedStateException();
        }
        NFAImpl concatenatedNFA = new NFAImpl(this.initialState);

        // add all states and translations from first NFA
        concatenatedNFA.states.addAll(this.states); // addAll for Set
        concatenatedNFA.transitions.putAll(this.transitions); // putAll for Map

        // accepting states of first NFA are connected to initial state
        // of the second NFA via an epsilon transition (i.e., null)
        for (String acceptingState : this.acceptingStates) {
            concatenatedNFA.transitions.computeIfAbsent(acceptingState,
                            currentState -> new HashSet<>())
                    .add(new Transition(acceptingState, null,
                            other.getInitialState()));
        }

        // add states and transitions of second NFA, for uniqueness add a prefix first
        for (String state : other.getStates()) {
            String uniqueState = "second_" + state;
            concatenatedNFA.states.add(uniqueState);

            // todo: change transition names to prefix "second_" + transition name
            // Set<Transition> transitionsOfSecondNFA = other.

            //for (Transition transition : other. */
        }

        return null;
    }

    /* todo delete later

Set<Transition> transitionsForState = ((NFAImpl)other).transitions.getOrDefault(state, Collections.emptySet());


    // Paso 4: Copiar los estados y transiciones del segundo NFA
    for (String state : ((NFAImpl)other).getStates()) {
        String newState = "second_" + state; // Asegurar nombres únicos
        concatenatedNFA.states.add(newState);

        for (Transition transition : ((NFAImpl)other).transitions.getOrDefault(state, Collections.emptySet())) {
            String newToState = "second_" + transition.toState();
            concatenatedNFA.addTransition(new Transition(newState, transition.readSymbol(), newToState));
        }
    }
    // Iterar sobre cada transición y añadir una transición correspondiente en el nuevo NFA concatenado
for (Transition transition : transitionsForState) {
    String newToState = "second_" + transition.toState();
    concatenatedNFA.addTransition(new Transition(newState, transition.readSymbol(), newToState));
}

    // Paso 5: Manejar los estados de aceptación
    concatenatedNFA.acceptingStates.clear(); // Limpiar los antiguos estados de aceptación
    for (String acceptingState : ((NFAImpl)other).getAcceptingStates()) {
        concatenatedNFA.acceptingStates.add("second_" + acceptingState);
    }

    // Paso 6: Finalizar el nuevo NFA
    concatenatedNFA.finalizeAutomaton();

    return concatenatedNFA;
}

     */

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
    public boolean isFinite() {
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
                        this.transitions.getOrDefault(currentState, Collections.emptySet());
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
        Set<String> epsilonStates = new HashSet<>(states);

        while (!epsilonStates.isEmpty()) {
            Set<String> temporaryStates = new HashSet<>();
            for (String epsilonState : epsilonStates) {
                Set<Transition> stateTransitions =
                        this.transitions.getOrDefault(epsilonState, Collections.emptySet());
                for (Transition transition : stateTransitions) {
                    if (transition.readSymbol() == null) {
                        if (reachableStates.add(transition.toState())) {
                            temporaryStates.add(transition.toState());
                        }
                    }
                }
            }
            epsilonStates = temporaryStates;
        }

        return reachableStates;
    }

}
