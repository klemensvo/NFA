package ab1;

import lombok.Builder;

/**
 * Describes a transition of one character
 *
 * @param fromState  - state, the automata must have for the transition to take effect
 * @param readSymbol - next symbol in the word. Can be null for ε.
 * @param toState    - state, the automata has after the transition
 */
@Builder
public record Transition(String fromState, Character readSymbol, String toState) {
}

/*
usage of Transition:
Transition t = Transition.builder()
                         .fromState("q0")
                         .readSymbol('a')
                         .toState("q1")
                         .build();

System.out.println("Transition from: " + t.fromState());
// Will print: "Transition from: q0"
 */