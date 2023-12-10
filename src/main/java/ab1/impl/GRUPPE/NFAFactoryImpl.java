package ab1.impl.GRUPPE;

import ab1.NFA;
import ab1.NFAFactory;

public class NFAFactoryImpl implements NFAFactory {
    @Override
    public NFA buildNFA(String startState) {
        return new NFAImpl();
    }
}
