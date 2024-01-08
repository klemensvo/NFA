package ab1;

import ab1.impl.gruppe14_samer_schachner_vospernik.NFAFactoryImpl;

public class NFAProvider {
    public static NFAFactory provideFactory() {
        return new NFAFactoryImpl();
    }
}
