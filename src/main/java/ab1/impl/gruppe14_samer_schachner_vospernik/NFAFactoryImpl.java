package ab1.impl.gruppe14_samer_schachner_vospernik;

import ab1.NFA;
import ab1.NFAFactory;

public class NFAFactoryImpl implements NFAFactory {
    @Override
    public NFA buildNFA(String startState) {
        return new NFAImpl(startState);
    }
}
