package ab1;

import ab1.impl.GRUPPE.NFAFactoryImpl;

public class NFAProvider {
    public static NFAFactory provideFactory() {
        return new NFAFactoryImpl();
    }
}
