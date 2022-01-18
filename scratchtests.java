class scrathtests{
    public static void main(String[] args) {


        ALU alu = new ALU();
        LongWord op1 = new LongWord();
        LongWord op2 = new LongWord();

        //op1.set(-2147483647);
        op1.set(0);
        op2.set(32);

        LongWord op3 = alu.operate(7, op1, op2);
        System.out.println(op3.getSigned());
        System.out.println("ZF: "+alu.getZF()+" -NF: "+alu.getNF()+" -CF: "+alu.getCF()+" -OF: "+alu.getOF());
        
    }
}