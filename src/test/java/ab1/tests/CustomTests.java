package ab1.tests;

import ab1.NFAFactory;
import ab1.NFAProvider;
import ab1.Transition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}
