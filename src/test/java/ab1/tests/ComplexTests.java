package ab1.tests;

import ab1.NFA;
import ab1.NFAFactory;
import ab1.NFAProvider;
import ab1.Transition;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComplexTests {
    private final NFAFactory factory = NFAProvider.provideFactory();

    @Test
    public void concat1Test() {
        var nfaA = buildCharLanguage('a');

        var testInstance = nfaA.concatenation(nfaA);

        assertFalse(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("aa"));
        assertFalse(testInstance.acceptsWord("aaa"));
    }

    @Test
    public void union1Test() {
        var nfaA = buildCharLanguage('a');
        var nfaB = buildCharLanguage('b');

        var testInstance = nfaA.union(nfaB);

        assertTrue(testInstance.acceptsWord("a"));
        assertTrue(testInstance.acceptsWord("b"));
        assertFalse(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("ba"));
    }

    @Test
    public void intersection1Test() {
        var nfaA = buildCharLanguage('a');
        var nfaB = buildCharLanguage('b');

        var testInstance = nfaA.intersection(nfaB);

        assertFalse(testInstance.acceptsWord("a"));
        assertFalse(testInstance.acceptsWord("b"));
        assertFalse(testInstance.acceptsWord("ab"));
        assertFalse(testInstance.acceptsWord("ba"));
    }

    @Test
    public void intersection2Test() {
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

    @Test
    public void star1Test() {
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
    public void plus1Test() {
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
    public void plus2Test() {
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
    public void complement1Test() {
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

    @Test
    public void finite1Test() {
        var nfaA = buildCharLanguage('a');

        assertTrue(nfaA.isFinite());
    }

    @Test
    public void finite2Test() {
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
