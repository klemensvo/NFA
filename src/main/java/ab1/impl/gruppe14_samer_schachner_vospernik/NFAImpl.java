package ab1.impl.gruppe14_samer_schachner_vospernik;

import ab1.FinalizedStateException;
import ab1.NFA;
import ab1.Transition;

import java.util.*;
import java.util.stream.Collectors;

public class NFAImpl implements NFA {
    final private String initialState;
    final private Set<String> states = new HashSet<>();
    final private Set<String> acceptingStates = new HashSet<>();
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
        if (!isFinalized() || !other.isFinalized()) {
            throw new FinalizedStateException();
        }

        //creating new NFA
        NFAImpl unionNFA = new NFAImpl("q0_union_start");

        //adding states from this
        unionNFA.states.addAll(this.states);
        unionNFA.acceptingStates.addAll(this.acceptingStates);
        unionNFA.transitions.putAll(this.transitions);

        //adding states from other
        for (String state : other.getStates()) {
            //changing state name to avoid conflicts
            String newState = "q_union_" + state;

            //adding new state to union
            unionNFA.states.add(newState);

            //creating new set for transitions from other
            Set<Transition> transitionsFromOther = new HashSet<>();
            for (Transition transition : other.getTransitions()) {
                if (transition.fromState().equals(state)) {
                    Transition newTransition = new Transition(newState, transition.readSymbol(), transition.toState());
                    transitionsFromOther.add(newTransition);
                }
            }

            //adding states from other to union
            unionNFA.transitions.put(newState, transitionsFromOther);

            //marking new state as accepting if it is accepting in other
            if (other.getAcceptingStates().contains(state)) {
                unionNFA.acceptingStates.add(newState);
            }
        }

        //adding new start state and epsilon transition to start states of this and other
        unionNFA.states.add("q0_union_start");
        unionNFA.addTransition(new Transition("q0_union_start", null, this.initialState));
        unionNFA.addTransition(new Transition("q0_union_start", null, "q_union_" + other.getInitialState()));

        unionNFA.finalizeAutomaton();

        return unionNFA;
    }

    @Override
    public NFA intersection(NFA other) throws FinalizedStateException {
        if (!isFinalized() || !other.isFinalized()) {
            throw new FinalizedStateException();
        }

        NFA intersection = this.complement().union(other.complement()).complement();
        intersection.finalizeAutomaton();

        return intersection;
    }




    @Override
    public NFA concatenation(NFA otherNFA) throws FinalizedStateException {
        if (!this.isFinalized() || !otherNFA.isFinalized()) {
            throw new FinalizedStateException("One or both of the original" +
                    "two NFAs are not finalized yet");
        }

        // initialise concatenatedNFA and add states and transitions of first NFA
        NFAImpl concatenatedNFA = new NFAImpl(this.initialState);
        concatenatedNFA.states.addAll(this.states); // addAll for Sets
        concatenatedNFA.transitions.putAll(this.transitions); // putAll for Maps

        // rename initial state of second NFA, then connect
        // accepting states of first NFA to initial state
        // of the second NFA via an epsilon transition (i.e., null)
        String initialStateOfOtherNFA = "second_" + otherNFA.getInitialState();
        for (String acceptingState : this.acceptingStates) {
            concatenatedNFA.transitions.computeIfAbsent(acceptingState,
                            currentState -> new HashSet<>())
                    .add(new Transition(acceptingState, null,
                            initialStateOfOtherNFA));
        }

        // for uniqueness, add the prefix "second_" to all states and
        // transitions of second NFA
        for (String otherNfaState : otherNFA.getStates()) {
            String uniqueFromState = "second_" + otherNfaState;
            concatenatedNFA.states.add(uniqueFromState);

            // find all transitions from 'state':
            Set<Transition> otherTransitions = new HashSet<>(otherNFA.getTransitions());
            Set<Transition> otherStateTransitions = otherTransitions.stream()
                    .filter(transition -> transition.fromState().equals(otherNfaState))
                    .collect(Collectors.toSet());

            // iterate through all transitions from 'state':
            for (Transition transition : otherStateTransitions) {
                String uniqueToState = "second_" + transition.toState();
                concatenatedNFA.addTransition(new Transition(uniqueFromState,
                        transition.readSymbol(), uniqueToState));
            }
        }

        // clear all acceptingStates and only accept
        // accepting states of the second NFA
        concatenatedNFA.acceptingStates.clear();
        for (String acceptingState : otherNFA.getAcceptingStates()) {
            concatenatedNFA.acceptingStates.add("second_" + acceptingState);
        }
        // finalize
        concatenatedNFA.finalizeAutomaton();

        if (!isFinalized) {
            throw new FinalizedStateException("The concatenated NFA is not finalized");
        }

        return concatenatedNFA;
    }


    @Override
    public NFA kleeneStar() throws FinalizedStateException {
        if (!this.isFinalized()) {
            throw new FinalizedStateException("NFA must be finalized!");
        }
        //Create new NFA with new start point and copy settings
        NFAImpl kleeneStarNFA = new NFAImpl("NewStart");
        kleeneStarNFA.states.addAll(this.states);
        kleeneStarNFA.transitions.putAll(this.transitions);
        kleeneStarNFA.acceptingStates.addAll(this.acceptingStates);

        //New start point properties
        kleeneStarNFA.addTransition(new Transition("NewStart",null,this.initialState));
        kleeneStarNFA.addAcceptingState("NewStart");

        //Add null transitions from every old accepting to new start state
        for (String state : this.acceptingStates){
            kleeneStarNFA.addTransition(new Transition(state,null, kleeneStarNFA.initialState));
        }
        kleeneStarNFA.finalizeAutomaton();

        return kleeneStarNFA;
    }

    @Override
    public NFA plusOperator() throws FinalizedStateException {
        if (!this.isFinalized()) {
            throw new FinalizedStateException("NFA must be finalized!");
        }
        //Create kleene Star NFA
        NFA kleene = kleeneStar();
        //Concatenate current NFA with kleene NFA
        return concatenation(kleene);
    }

    @Override
    public NFA complement() throws FinalizedStateException {
        if (!this.isFinalized()) {
            throw new FinalizedStateException("NFA must be finalized!");
        }
        NFAImpl copy = new NFAImpl(initialState);
        copy.acceptingStates.addAll(acceptingStates);
        copy.states.addAll(states);
        copy.transitions.putAll(transitions);

        String complementNFAInitialState;
        String complementNFAAcceptingSate;

        //Add new Accepting State -> Convert multiple accepting states to one
        if(copy.acceptingStates.size()>1){
            for(String state : copy.acceptingStates){
                copy.addTransition(new Transition(state,null,"NewStart"));
            }
            copy.acceptingStates.clear();
            copy.addAcceptingState("NewStart");
            complementNFAInitialState = "NewStart"; //complementNFA needs this as start point
            complementNFAAcceptingSate = copy.initialState; // Old start point -> new accepting state
        } else {
            complementNFAInitialState = copy.acceptingStates.iterator().next(); // Old accepting state is new start point
            complementNFAAcceptingSate = copy.initialState;
        }

        NFAImpl complementNFA = new NFAImpl(complementNFAInitialState);
        complementNFA.states.addAll(copy.states);
        complementNFA.addAcceptingState(complementNFAAcceptingSate);

        //Reverse Tansition connection
        Map<String, Set<Transition>> newTransitions = getNewTransitions(copy);
        complementNFA.transitions.putAll(newTransitions);


        complementNFA.finalizeAutomaton();
/*
        NFAImpl complementNFA = new NFAImpl(this.getInitialState());
        //Copy transitions and states
        complementNFA.transitions.putAll(this.transitions);
        complementNFA.states.addAll(this.states);
        //Remove accepting states from set of all states
        Set<String> newAcceptingStates = this.states;
        newAcceptingStates.removeIf(acceptingStates::contains);
        //Add complement accepting states to new NFA
        complementNFA.acceptingStates.addAll(newAcceptingStates);
*/

        return complementNFA;
    }

    private static Map<String, Set<Transition>> getNewTransitions(NFAImpl copy) {
        Map<String, Set<Transition>> newTransitions = new HashMap<>();

        //Reverse Transition direction
        for(Map.Entry<String, Set<Transition>> transition : copy.transitions.entrySet()){
                Set<Transition> values = transition.getValue();
                Set<Transition> temp = new HashSet<>();
                for(Transition a : values){
                    String from = a.fromState();
                    String to = a.toState();
                    if(!from.equals(to)){
                        Transition newTransition = new Transition(to, a.readSymbol(), from);
                        temp.add(newTransition);
                    }
                }
                newTransitions.put(transition.getKey(),temp);
        }
        return newTransitions;
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

        Set<String> visitedStates = new HashSet<>();
        Set<String> stack = new HashSet<>();
        stack.add(initialState);

        while (!stack.isEmpty()) {
            String currentState = stack.iterator().next();
            stack.remove(currentState);

            visitedStates.add(currentState);

            //check transitions for current state
            Set<Transition> stateTransitions = transitions.getOrDefault(currentState, Collections.emptySet());
            for (Transition transition : stateTransitions) {

                //if toState has not been visited, add to stack
                String toState = transition.toState();
                if (!visitedStates.contains(toState)) {
                    stack.add(toState);
                }
            }
        }

        //check if an accepting state is reached
        for (String state : visitedStates) {
            if (acceptingStates.contains(state)) {
                return true; //finite
            }
        }

        return false; //infinite
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
        Set<String> newStates = new HashSet<>(states);

        // check for visited states to avoid circles caused by circling epsilon transitions
        Set<String> visitedStates = new HashSet<>(states);

        while (!newStates.isEmpty()) {
            Set<String> temporaryStates = new HashSet<>();
            for (String epsilonState : newStates) {
                Set<Transition> stateTransitions = transitions.getOrDefault(epsilonState, Collections.emptySet());
                for (Transition transition : stateTransitions) {
                    if (transition.readSymbol() == null) {
                        // only follow transitions that haven't been visited before
                        if (!visitedStates.contains(transition.toState())) {
                            if (reachableStates.add(transition.toState())) {
                                temporaryStates.add(transition.toState());
                                visitedStates.add(transition.toState()); // mark as visited
                            }
                        }
                    }
                }
            }
            newStates = temporaryStates;
        }

        return reachableStates;
    }
}
