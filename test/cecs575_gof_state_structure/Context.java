package cecs575_gof_state_structure;
class Context {
    private State state;//current state
    public Context(){
        state = new State1();
    }
    public void op1(){
        state.op1(this);
    }
    public void op2(){
        state.op2(this);
    }
    //
    public void setState(State s){
        state = s;
    }
}
