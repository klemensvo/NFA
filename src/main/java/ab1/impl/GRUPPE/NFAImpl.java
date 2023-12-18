package ab1.impl.GRUPPE;

import ab1.FinalizedStateException;
import ab1.NFA;
import ab1.Transition;

import java.util.Collection;
import java.util.Set;

public class NFAImpl implements NFA {
    @Override
    public Set<String> getStates() {
        return null;
    }

    @Override
    public Collection<Transition> getTransitions() {
        return null;
    }

    @Override
    public Set<String> getAcceptingStates() {
        return null;
    }

    @Override
    public String getInitialState() {
        return null;
    }

    @Override
    public void addTransition(Transition transition) throws FinalizedStateException {

    }

    @Override
    public void addAcceptingState(String state) throws FinalizedStateException {

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
        return false;
    }

    @Override
    public void finalizeAutomaton() {

    }

    @Override
    public boolean isFinite() {
        return false;
    }

    @Override
    public boolean acceptsWord(String word) {
        return false;
    }
}
