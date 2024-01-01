package ab1.tests;

import ab1.FinalizedStateException;
import ab1.NFAFactory;
import ab1.NFAProvider;
import ab1.Transition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CustomTests {

    private final NFAFactory factory = NFAProvider.provideFactory();

    @Test
    public void acceptingStatesTest1() {
        var instance = factory.buildNFA("START");
        instance.addAcceptingState("ACCEPT");

        assertEquals(1, instance.getAcceptingStates().size());
        assertTrue(instance.getAcceptingStates().contains("ACCEPT"));
        System.out.println(instance.getAcceptingStates());
    }

    @Test
    public void acceptingStatesTest2() {
        var instance = factory.buildNFA("START");
        instance.addAcceptingState("ACCEPT");
        instance.addAcceptingState("ACCEPT");
        instance.addAcceptingState("ACCEPT");

        assertEquals(1, instance.getAcceptingStates().size());
        assertTrue(instance.getAcceptingStates().contains("ACCEPT"));
    }

    @Test
    public void acceptingStatesTest3() {
        var instance = factory.buildNFA("START");
        instance.addAcceptingState("ACCEPT");
        instance.addAcceptingState("OTHER_ACCEPT");

        assertEquals(2, instance.getAcceptingStates().size());
        assertTrue(instance.getAcceptingStates().contains("ACCEPT"));
        assertTrue(instance.getAcceptingStates().contains("OTHER_ACCEPT"));
        System.out.println(instance.getAcceptingStates());
    }

    @Test
    public void acceptingStatesTest4() {
        var instance = factory.buildNFA("START");
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );
        instance.addAcceptingState("ACCEPT");

        assertEquals(1, instance.getAcceptingStates().size());
        assertTrue(instance.getAcceptingStates().contains("ACCEPT"));
    }

    @Test
    public void transitionsTest1() {
        var instance = factory.buildNFA("START");
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );

        assertEquals(2, instance.getStates().size());
    }

    @Test
    public void initialStateTest() {
        var instance = factory.buildNFA("START");

        assertEquals("START", instance.getInitialState());
    }

    @Test
    public void finalizeTest1() {
        var instance = factory.buildNFA("START");

        assertFalse(instance.isFinalized());

        instance.finalizeAutomaton();

        assertTrue(instance.isFinalized());
    }

    @Test
    public void finalizeTest2() {
        var instance = factory.buildNFA("START");
        instance.finalizeAutomaton();

        assertThrows(
                FinalizedStateException.class,
                () -> instance.addTransition(
                        Transition.builder()
                                .fromState("START")
                                .readSymbol('a')
                                .toState("ACCEPT")
                                .build()
                )
        );
    }

    @Test
    public void languageTest1() {
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
    public void languageTest2() {
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
    public void languageTest3() {
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
        assertFalse(instance.acceptsWord("aa"));
        assertFalse(instance.acceptsWord("ba"));
        assertFalse(instance.acceptsWord("xyaz"));
        assertFalse(instance.acceptsWord("ETI is fun!"));
    }

    /*
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
     */

}
