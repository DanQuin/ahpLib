package sty;

import clc.DecisionElement;

// TODO documentation

public interface ComputeListener {
    void methodHasBeenComputed(
            String method1, String method2, DecisionElement decisionElement, int current, int total);
}
