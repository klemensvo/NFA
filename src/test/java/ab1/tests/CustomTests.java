package ab1.tests;

import ab1.*;
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
    public void finalizeTest3() {
        var instance = factory.buildNFA("START");

        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );
        instance.finalizeAutomaton();
    }

    @Test
    public void finalizeTest4() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                instance::kleeneStar
        );
    }

    @Test
    public void finalizeTest5() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                instance::plusOperator
        );
    }

    @Test
    public void finalizeTest6() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.concatenation(instance)
        );
    }

    @Test
    public void finalizeTest7() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.intersection(instance)
        );
    }

    @Test
    public void finalizeTest8() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                instance::complement
        );
    }

    @Test
    public void finalizeTest9() {
        var instance = factory.buildNFA("START");

        assertThrows(
                FinalizedStateException.class,
                () -> instance.union(instance)
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

    @Test
    public void languageTest4() {
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

    @Test
    public void languageTest5() {
        var instance = factory.buildNFA("START");

        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol(null)
                        .toState("ACCEPT")
                        .build()
        );
        instance.addAcceptingState("ACCEPT");
        instance.finalizeAutomaton();

        assertTrue(instance.acceptsWord(""));
        assertFalse(instance.acceptsWord("a"));
    }

    @Test
    public void languageTest6() {
        var instance = factory.buildNFA("START");

        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol(null)
                        .toState("ACCEPT")
                        .build()
        );
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol('a')
                        .toState("ACCEPT")
                        .build()
        );

        instance.addAcceptingState("ACCEPT");
        instance.finalizeAutomaton();

        assertTrue(instance.acceptsWord(""));
        assertTrue(instance.acceptsWord("a"));
        assertFalse(instance.acceptsWord("ab"));
        assertFalse(instance.acceptsWord("b"));
    }

    @Test
    public void languageTest7() {
        var instance = factory.buildNFA("START");

        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol(null)
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
                        .readSymbol('b')
                        .toState("START")
                        .build()
        );

        instance.addAcceptingState("START");
        instance.finalizeAutomaton();

        assertTrue(instance.acceptsWord(""));
        assertTrue(instance.acceptsWord("a"));
        assertTrue(instance.acceptsWord("ab"));
        assertTrue(instance.acceptsWord("b"));
        assertTrue(instance.acceptsWord("ba"));
        assertFalse(instance.acceptsWord("d"));
    }

    @Test
    public void concatenationTest1() {
        var nfaA = buildCharLanguage('a');

        var testInstance = nfaA.concatenation(nfaA);

        assertFalse(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("aa"));
        assertFalse(testInstance.acceptsWord("aaa"));
    }

    @Test
    public void concatenationTest2() {
        var nfaA = buildCharLanguage('a');
        var nfaB = buildCharLanguage('b');

        var testInstance = nfaA.concatenation(nfaB);

        assertFalse(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("aaa"));
    }

    @Test
    public void unionTest1() {
        var nfaA = buildCharLanguage('a');
        var nfaB = buildCharLanguage('b');

        var testInstance = nfaA.union(nfaB);

        assertTrue(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("b"));
        assertFalse(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("ba"));
    }

    /*
    @Test
    public void intersectionTest1() {
        var nfaA = buildCharLanguage('a');
        var nfaB = buildCharLanguage('b');

        var testInstance = nfaA.intersection(nfaB);

        assertFalse(testInstance.acceptsWord("a"));
        assertFalse(testInstance.acceptsWord("b"));
        assertFalse(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("ba"));
    }


    @Test
    public void intersectionTest2() {
        var nfaA = buildCharLanguage('a');
        var nfaB = buildCharStarLanguage('a');

        var testInstance = nfaA.intersection(nfaB);

        assertTrue(testInstance.acceptsWord("a"));
        assertFalse(testInstance.acceptsWord("aa"));
        assertFalse(testInstance.acceptsWord("aaaa"));
        assertFalse(testInstance.acceptsWord("aaaaaaaaaaa"));
        assertFalse(testInstance.acceptsWord("b"));
        assertFalse(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("ba"));
    }
    */

    @Test
    public void starTest1() {
        var nfaA = buildCharLanguage('a');

        var testInstance = nfaA.kleeneStar();

        assertTrue(testInstance.acceptsWord(""));
        assertTrue(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("aa"));
        assertTrue(testInstance.acceptsWord("aaaa"));
        assertTrue(testInstance.acceptsWord("aaaaaaaaaaa"));
        assertFalse(testInstance.acceptsWord("b"));
        assertFalse(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("ba"));
    }

    @Test
    public void plusTest1() {
        var nfaA = buildCharLanguage('a');

        var testInstance = nfaA.plusOperator();

        assertTrue(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("aa"));
        assertTrue(testInstance.acceptsWord("aaaa"));
        assertTrue(testInstance.acceptsWord("aaaaaaaaaaa"));
        assertFalse(testInstance.acceptsWord(""));
        assertFalse(testInstance.acceptsWord("b"));
        assertFalse(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("ba"));
    }

    @Test
    public void plusTest2() {
        var nfaA = buildCharLanguage('a');

        var testInstance = nfaA.plusOperator();

        assertTrue(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("aa"));
        assertTrue(testInstance.acceptsWord("aaaa"));
        assertTrue(testInstance.acceptsWord("aaaaaaaaaaa"));
        assertFalse(testInstance.acceptsWord(""));
        assertFalse(testInstance.acceptsWord("b"));
        assertFalse(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("ba"));
    }

    /*
    @Test
    public void complementTest1() {
        var nfaA = buildCharLanguage('a');

        var testInstance = nfaA.complement();

        assertFalse(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("aa"));
        assertTrue(testInstance.acceptsWord("aaaa"));
        assertTrue(testInstance.acceptsWord("aaaaaaaaaaa"));
        assertTrue(testInstance.acceptsWord(""));
        assertTrue(testInstance.acceptsWord("b"));
        assertTrue(testInstance.acceptsWord("ab"));
        assertTrue(testInstance.acceptsWord("ba"));
    }
     */

    @Test
    public void finiteTest1() {
        var nfaA = buildCharLanguage('a');

        assertTrue(nfaA.isFinite());
    }

    @Test
    public void finiteTest2() {
        var nfaA = buildCharLanguage('a');

        assertFalse(nfaA.complement().isFinite());
    }


    private NFA buildCharStarLanguage(char c) {
        var instance = factory.buildNFA("START");
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol(c)
                        .toState("START")
                        .build()
        );
        instance.addAcceptingState("START");
        instance.finalizeAutomaton();

        return instance;
    }

    private NFA buildCharLanguage(char c) {
        var instance = factory.buildNFA("START");
        instance.addTransition(
                Transition.builder()
                        .fromState("START")
                        .readSymbol(c)
                        .toState("ACCEPT")
                        .build()
        );
        instance.addAcceptingState("ACCEPT");
        instance.finalizeAutomaton();

        assertTrue(instance.acceptsWord(String.valueOf(c)));

        return instance;
    }
}
