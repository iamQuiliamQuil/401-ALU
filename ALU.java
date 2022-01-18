class ALU {
    private Boolean zeroFlag; //ZF
    private Boolean negativeFlag; //NF
    private Boolean carryOutFlag; //CF
    private Boolean overflowFlag; //OF

    public LongWord operate(int code, LongWord op1, LongWord op2){
        LongWord result; 
        
        switch (code) {
            case(0): //AND
                result = AND(op1, op2);
                break;
            case(1): //OR
                result = OR(op1, op2);
                break;
            case(2)://XOR
                result = XOR(op1, op2);
                break;
            case(3)://ADD
                result = ADD(op1,op2);
                break;
            case(4)://SUN
                result = SUB(op1, op2);
                break;
            case(5)://SLL
                result = SLL(op1, op2);
                break;
            case(6)://SRL
                result = SRL(op1, op2); 
                break;
            case(7)://SRA
                result = SRA(op1,op2);
                break;
            default:
            throw new ArgumentIsBadException("Code must be between 0 and 8");
        }
        return result;
    }

    //allows us to return two boolean variable in half and full adder
    private static class AdderResult{
        public Boolean sum;
        public Boolean cout;

        AdderResult(){
            sum = false;
            cout = false;
        }
    }

    //emulates Half Adder circuit
    private AdderResult halfAdder(Boolean op1, Boolean op2){
        AdderResult result = new AdderResult();
        result.cout = (op1&&op2); 
        result.sum = (op1^op2);
        return result;
    }

    //emulates Full Adder circuit
    private AdderResult fullAdder(Boolean op1, Boolean op2, Boolean cin){
        AdderResult result = new AdderResult();
        AdderResult halfAdd = halfAdder(op1, op2);
        AdderResult threeQuarterAdd = halfAdder(halfAdd.sum, cin);

        result.sum = threeQuarterAdd.sum;
        result.cout = (halfAdd.cout || threeQuarterAdd.cout);

        return result;
    }

    //emulates Ripple Carry Adder circuit - also sets flags for ADD and SUB
    private LongWord rippleCarryAdder(LongWord op1, LongWord op2, Boolean cin){
        LongWord result = new LongWord();
        AdderResult currentResult = new AdderResult();
        currentResult.cout = cin;

        for(int i = 0; i < op1.getBitSetSize(); i++){
            currentResult = fullAdder(op1.getBit(i), op2.getBit(i), currentResult.cout);
            if(currentResult.sum==true){
                result.setBit(i);
            }
        }
        
        //setting flags feels out of place in this method, but the alternative was 
        //retunring cout alongside result in a wrapper class, so I did this 
        //for simplicity's sake 
        carryOutFlag = currentResult.cout;
        overflowFlag = carryOutFlag; 

        return result;
    }

    //uses ripple carry adder
    private LongWord ADD(LongWord op1, LongWord op2){
        clearFlags();
        
        //cin = false for addition - if cin = true, we're adding 1 to the final sum
        LongWord result = rippleCarryAdder(op1, op2, false);

        checkNegativeFlag(result);
        checkZeroFlag(result);

        return result;
    }

    //uses ripple carry adder
    private LongWord SUB(LongWord op1, LongWord op2){
        clearFlags();
        
        //we negate a number by flipping bits and adding 1, creating subtraction 
        // by a-b = a+(-b), we add 1 through cin = true
        LongWord result = rippleCarryAdder(op1, op2.not(), true);

        checkNegativeFlag(result);
        checkZeroFlag(result);

        return result;
    }

    //relies on longword implementation
    private LongWord AND(LongWord op1, LongWord op2){
        LongWord result =  op1.and(op2);

        clearFlags();
        checkNegativeFlag(result);
        checkZeroFlag(result);

        return result;
    }

    //relies on longword implementation
    private LongWord OR(LongWord op1, LongWord op2){
        LongWord result =  op1.or(op2);

        clearFlags();
        checkNegativeFlag(result);
        checkZeroFlag(result);

        return result;
    }

    //relies on longword implementation
    private LongWord XOR(LongWord op1, LongWord op2){
        LongWord result =  op1.xor(op2);

        clearFlags();
        checkNegativeFlag(result);
        checkZeroFlag(result);

        return result;
    }

    //relies on longword implementation
    private LongWord SLL(LongWord operand, LongWord amount){ //shift left logically
        if (amount.getSigned() < -1){
            throw new ArgumentIsBadException("You cannot shift negatively");
        }
        clearFlags();

        //We set overflow flag to true if we are shifting by 1 and a sign flip occurs
        //a sign flip occurs if the most significant bit and second most significant bit are not equal 
        if (amount.getSigned() == 1 && (operand.getBit(operand.getBitSetSize()-1) != operand.getBit(operand.getBitSetSize()-2) )){
            overflowFlag = true; 
        }
        
        LongWord result =  operand.shiftLeftLogical(amount.getSigned());

        
        checkNegativeFlag(result);
        checkZeroFlag(result);

        return result;
    }

    //relies on longword implementation
    private LongWord SRL(LongWord operand, LongWord amount){ //shift right logically
        if (amount.getSigned() < -1){
            throw new ArgumentIsBadException("You cannot shift negatively");
        }
        clearFlags();
        LongWord result =  operand.shiftRightLogical(amount.getSigned());

        //negative check not required by assignment, but necessary if you allow for shifting by 0
        checkNegativeFlag(result);
        checkZeroFlag(result);

        return result;
    }

    //relies on longword implementation
    private LongWord SRA(LongWord operand, LongWord amount){ // shift right arithmetically
        if (amount.getSigned() < -1){
            throw new ArgumentIsBadException("You cannot shift negatively");
        }
        clearFlags();
        LongWord result =  operand.shiftRightArithmetic(amount.getSigned());

        checkNegativeFlag(result);
        checkZeroFlag(result);

        return result;
    }
    

    public Boolean getZF(){
        return zeroFlag;
    }

    public Boolean getNF(){
        return negativeFlag;
    }

    public Boolean getCF(){
        return carryOutFlag;
    }

    public Boolean getOF(){
        return overflowFlag;
    }

    private void checkZeroFlag(LongWord result){
        if (result.isZero()){
            zeroFlag = true;
        }
    }

    private void checkNegativeFlag(LongWord result){
        //check if the most significant bit is one, if it is, the result is negative
        if (result.getBit(result.getBitSetSize()-1)==true){
            negativeFlag = true;
        }
    }

    private void clearFlags(){
        zeroFlag = false;
        negativeFlag = false; 
        carryOutFlag = false;
        overflowFlag = false;
    }


    ALU(){
        //flags are uninitialized, clearFlags will set them to false
        clearFlags();
    }
}