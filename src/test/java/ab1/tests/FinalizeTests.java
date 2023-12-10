package ab1.tests;

import ab1.FinalizedStateException;
import ab1.NFAFactory;
import ab1.NFAProvider;
import ab1.Transition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FinalizeTests {

    private final NFAFactory factory = NFAProvider.provideFactory();

    @Test
    public void finalize1Test() {
        var instance = factory.buildNFA("START");

        assertFalse(instance.isFinalized());

        instance.finalizeAutomaton();

        assertTrue(instance.isFinalized());
    }

    @Test
    public void finalize2Test() {
        var instance = factory.buildNFA("START");
        instance.finalizeAutomaton();

        assertThrows(
                FinalizedStateException.class,
                () -> instance.addTransition(
                        Transition.builder()
                                .fromState("START")
                                .toState("ACCEPT")
                                .readSymbol('a')
                                .build()
                )
        );
    }

    @Test
    public void finalize3Test() {
        var instance = factory.buildNFA("START");

        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .toState("ACCEPT")
                        .readSymbol('a')
                        .build()
        );
        instance.finalizeAutomaton();
    }

    @Test
    public void finalize4Test() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.kleeneStar()
        );
    }

    @Test
    public void finalize5Test() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.plusOperator()
        );
    }

    @Test
    public void finalize6Test() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.concatenation(instance)
        );
    }

    @Test
    public void finalize7Test() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.intersection(instance)
        );
    }

    @Test
    public void finalize8Test() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.complement()
        );
    }

    @Test
    public void finalize9Test() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.union(instance)
        );
    }


}
