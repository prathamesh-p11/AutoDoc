package cecs575_gof_state_structure;
public class State2 extends State {

    @Override
    public void op1(Context context) {
       System.out.println("I am State2.op1()");
    }
    @Override
    public void op2(Context context) {
       System.out.println("I am State2.op2() changing to State1");
       context.setState(new State1());
    }   
}
