package cecs575_gof_state_structure;
public class State1 extends State {

    public void op1(Context context){
        System.out.println("I am State1.op1() changing to State2");
        context.setState(new State2());
    }
    public void op2(Context context){
        System.out.println("I am State1.op2()");
    }  
}
