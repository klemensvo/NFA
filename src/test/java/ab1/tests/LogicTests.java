package ab1.tests;

import ab1.NFAFactory;
import ab1.NFAProvider;
import ab1.Transition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogicTests {
    private final NFAFactory factory = NFAProvider.provideFactory();

    @Test
    public void language1Test() {
        var instance = factory.buildNFA("START");
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );
        instance.addAcceptingState("ACCEPT");
        instance.finalizeAutomaton();

        assertTrue(instance.acceptsWord("a"));
        assertFalse(instance.acceptsWord("aa"));
        assertFalse(instance.acceptsWord("ba"));
        assertFalse(instance.acceptsWord("xyaz"));
        assertFalse(instance.acceptsWord("ETI is fun!"));
    }

    @Test
    public void language2Test() {
        var instance = factory.buildNFA("START");
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );
        instance.addTransition(
                Transition.builder()
                        .fromState("ACCEPT")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );
        instance.addAcceptingState("ACCEPT");
        instance.finalizeAutomaton();

        assertTrue(instance.acceptsWord("a"));
        assertTrue(instance.acceptsWord("aa"));
        assertTrue(instance.acceptsWord("aaa"));
        assertTrue(instance.acceptsWord("aaaa"));
        assertTrue(instance.acceptsWord("aaaaaaaaaaaaaaaaaaaaaa"));
        assertFalse(instance.acceptsWord("ba"));
        assertFalse(instance.acceptsWord("xyaz"));
        assertFalse(instance.acceptsWord("ETI is fun!"));
    }

    @Test
    public void language3Test() {
        var instance = factory.buildNFA("START");
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol(null)
                        .toState("S1")
                        .build()
        );
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol(null)
                        .toState("S2")
                        .build()
        );
        instance.addTransition(
                Transition.builder()
                        .fromState("S1")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );
        instance.addTransition(
                Transition.builder()
                        .fromState("S2")
                        .readSymbol('b')
                        .toState("ACCEPT")
                        .build()
        );
        instance.addAcceptingState("ACCEPT");
        instance.finalizeAutomaton();

        assertTrue(instance.acceptsWord("a"));
        assertTrue(instance.acceptsWord("b"));
        assertFalse(instance.acceptsWord("ba"));
        assertFalse(instance.acceptsWord("xyaz"));
        assertFalse(instance.acceptsWord("ETI is fun!"));
    }

    @Test
    public void language4Test() {
        var instance = factory.buildNFA("START");
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );
        instance.addTransition(
                Transition.builder()
                        .fromState("ACCEPT")
                        .readSymbol(null)
                        .toState("START")
                        .build()
        );
        instance.addAcceptingState("ACCEPT");
        instance.finalizeAutomaton();

        assertTrue(instance.acceptsWord("a"));
        assertTrue(instance.acceptsWord("aa"));
        assertTrue(instance.acceptsWord("aaa"));
        assertTrue(instance.acceptsWord("aaaa"));
        assertTrue(instance.acceptsWord("aaaaaaaaaaaaaaaaaaaaaa"));
        assertFalse(instance.acceptsWord("ba"));
        assertFalse(instance.acceptsWord("xyaz"));
        assertFalse(instance.acceptsWord("ETI is fun!"));
    }

}
