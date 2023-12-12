package ab1;

import java.util.Collection;

/**
 * The {@code NFA} interface represents a Non-deterministic Finite Automaton (NFA) and provides methods to manipulate
 * NFAs as well as run words against the NFA to check if they are in the language or not. The NFA has two states:
 * edit and accept. While in edit-state, new transitions and accepting states can be added until
 * {@link #finalizeAutomaton()} is called. If a method is called in the wrong state, a {@link FinalizedStateException}
 * should be thrown.
 */
public interface NFA {

    /**
     * @return all states present in the automata
     */
    Collection<String> getStates();

    /**
     * @return all states present in the automata, marked as accepting state
     */
    Collection<String> getAcceptingStates();

    /**
     * @return initial state of the automata. This cannot change over the lifetime of the object
     */
    String getInitialState();

    /**
     * Add a new transition to the automata.
     *
     * @param transition information about the new transition. If the automaton already has a transition for the
     *                   given fromState and readSymbol, it should be replaced by the provided one.
     * @throws FinalizedStateException if {@link #finalizeAutomaton()} was already called
     */
    void addTransition(Transition transition) throws FinalizedStateException;

    /**
     * Flag a state to be an accepting state. If the state was accepting before, nothing changes.
     *
     * @param state - label of the new accepting state
     * @throws FinalizedStateException if {@link #finalizeAutomaton()} was already called
     */
    void addAcceptingState(String state) throws FinalizedStateException;

    /**
     * @param other any NFA
     * @return a new NFA instance that accepts the language L(return) = L(this) ∪ L(other).
     * other. Both, other and this NFA must not change during this operation.
     * @throws FinalizedStateException if {@link #finalizeAutomaton()} was not already called
     */
    NFA union(NFA other) throws FinalizedStateException;

    /**
     * @param other - any NFA
     * @return a new NFA instance that accepts the language L(return) = L(this) ∩ L(other).
     * Both, other and this NFA must not change during this operation.
     * @throws FinalizedStateException if {@link #finalizeAutomaton()} was not already called
     */
    NFA intersection(NFA other) throws FinalizedStateException;

    /**
     * @param other - any NFA
     * @return a new NFA instance that accepts the language L(return) = L(this) + L(other).
     * Both, other and this NFA must not change during this operation.
     * @throws FinalizedStateException if {@link #finalizeAutomaton()} was not already called
     */
    NFA concatenation(NFA other) throws FinalizedStateException;

    /**
     * @return a new NFA instance that accepts the language L(this)*.
     * This NFA must not change during the operation.
     * @throws FinalizedStateException if {@link #finalizeAutomaton()} was not already called
     */
    NFA kleeneStar() throws FinalizedStateException;

    /**
     * @return a new NFA instance that accepts the language L(this)⁺.
     * This NFA must not change during the operation.
     * @throws FinalizedStateException if {@link #finalizeAutomaton()} was not already called
     */
    NFA plusOperator() throws FinalizedStateException;

    /**
     * @return a new NFA instance that accepts the complement of L(this).
     * This NFA must not change during the operation.
     * @throws FinalizedStateException if {@link #finalizeAutomaton()} was not already called
     */
    NFA complement() throws FinalizedStateException;

    /**
     * @return if the automaton is editable or not
     */
    boolean isFinalized();

    /**
     * Marks the automaton as finalized. Only now words can be tested.
     */
    void finalizeAutomaton();

    /**
     * @return whether the language of this automaton is finite or infinite.
     */
    boolean isFinite();

    /**
     * @param word - word to check, can be empty
     * @return true, iff the word lies in the language of the automaton
     */
    boolean acceptsWord(String word);
}
