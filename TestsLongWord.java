
class TestsLongWord{

    public static void runTests(){
        System.out.println("Running some short and simple visual tests...");
        visualTests();

        System.out.println("\nRunning some more rigorous tests without visual feedback...\n");
        System.out.println("Setting, clearing, and toggling every bit in bitset in turn...");
        System.out.println( setClearToggleWorking() ? "Succesful":"Unsucessful");
        System.out.println("Generating 1000 random LongWords and verifying that Fundamental properties of and, or, not, and xor are working...");
        System.out.println( andNotOrXorWorks() ? "Succesful":"Unsucessful");
        System.out.println("Generating 1000 random LongWords and verifying that shiftRightArithmetic works as intended...");
        System.out.println( ShiftArithmeticWorks() ? "Succesful":"Unsucessful");
        System.out.println("Generating 1000 random LongWords and verifying that ShiftRightLogical and ShiftLeftLogical work as intended...");
        System.out.println( ShiftLeftRightLogicalWorks() ? "Succesful":"Unsucessful");
        System.out.println("Generating 1000 random LongWords and verifying that copy works as intended...");
        System.out.println( copyWorks() ? "Succesful":"Unsucessful");
        System.out.println("Generating 1000 random LongWords and verifying that set and getSigned work as intended...");
        System.out.println( setGetSignedWorks() ? "Succesful":"Unsucessful");
        System.out.println("Setting two random bits 1000 times and verifying getUnsigned returns a value equal to the sum of the powers of two of their indexes...");
        System.out.println( longWorks() ? "Succesful":"Unsucessful\n");

    }
    


    public static void visualTests(){
        //c+g
        LongWord longWord = new LongWord();
        System.out.println("\nlongWord at initialization: \n"+longWord.toString());
        System.out.println("longWord.isZero() = "+longWord.isZero());
        longWord.setBit(0);
        longWord.setBit(30);
        longWord.setBit(31);
        longWord.setBit(31);
        System.out.println("longWord after calling setBit on bits 0, 30 and 31 twice: \n"+longWord.toString());
        System.out.println("longWord.isZero() = "+longWord.isZero());
        longWord.clearBit(0);
        longWord.clearBit(30);
        longWord.clearBit(7);
        System.out.println("longWord after calling clearBit on bits 0, 30 and 7: \n"+longWord.toString());
        System.out.println("longWord's bits printed individually using getBit: ");
        for(int i = 0; i < 32; i++){
            System.out.print(( (longWord.getBit(31-i)) ? 1:0 ));
        }
        System.out.println(" ");
        for(int i = 0; i < 32; i++){
            longWord.toggleBit(i);
        }
        System.out.println("longWord after calling toggle bit on all bits: \n"+longWord.toString()+"\n");
        //d
        longWord.set(-1);
        System.out.println("longWord after calling set(-1) \n"+longWord.toString());
        System.out.println("longWord.getSigned() = "+longWord.getSigned());
        System.out.println("longWord.getUnsigned() = "+longWord.getUnsigned());
        LongWord other = new LongWord();
        other.copy(longWord);
        System.out.println("new LongWord other, after other.copy(longword): \n"+other.toString());
        //e
        longWord = longWord.shiftRightLogical(5);
        System.out.println("longWord after being set to shiftRightLogical(5): \n"+longWord.toString());
        longWord = longWord.shiftLeftLogical(20);
        System.out.println("longWord after being set to longWord.shiftLeftLogical(20): \n"+longWord.toString());
        longWord = longWord.shiftRightArithmetic(5);
        System.out.println("longWord after being set to shiftRightArithmetic(5): \n"+longWord.toString());
        longWord = longWord.shiftRightLogical(1);
        System.out.println("longWord after being set to shiftRightLogical(1): \n"+longWord.toString());
        longWord = longWord.shiftRightArithmetic(5);
        System.out.println("longWord after being set to shiftRightArithmetic(5): \n"+longWord.toString()+"\n");
        //f
        longWord = longWord.not();
        System.out.println("longWord after being set to longWord.not(): \n"+longWord.toString());
        System.out.println("other, as a reminder: \n"+other.toString());
        System.out.println("longWord.and(other) = "+longWord.and(other).toString());
        System.out.println("longWord.or(other) = "+longWord.or(other).toString());
        System.out.println("longWord.xor(other) = "+longWord.xor(other).toString()+"\n");
    }

    public static Boolean andNotOrXorWorks(){
        LongWord longWord = new LongWord();
        LongWord other = new LongWord();

        //running test 1000 times
        for(int i = 0; i < 1000; i ++){
            //randomly generates any valid 32-bit int value to make sure test is rigorous
            int rand = (int)(Math.random()*(Math.pow(2, 31)-1));
            if (Math.round(Math.random()) != 1){
                rand *= -1;
            }
            longWord.set(rand);
            //if other is longword.not(), other AND longword should generate a bitset of all zeros
            other = longWord.not();
            LongWord nother = longWord.and(other);
            if(!allzeros(nother)){
                System.out.println("not() and/or and() failed to process "+rand);
                return false;
            }
            //if other is longword.not(), other XOR longword should generate a bitset of all ones
            nother = longWord.xor(other);
            if(!allones(nother)){
                System.out.println("not() and/or xor() failed to process "+rand);
                return false;
            }
            //any longword XOR itself should generate a bitset of all zeros
            nother = longWord.xor(longWord);
            if(!allzeros(nother)){
                System.out.println("not() and/or xor() failed to process "+rand);
                return false;
            }
            //longword OR longwords.not() should generate a bitset of all ones
            if(!allones(longWord.not().or(longWord))){
                System.out.println("not() and/or or() failed to process "+rand);
                return false;
            }
        }
        return true;
    }

    public static Boolean ShiftArithmeticWorks(){

        LongWord longWord = new LongWord();
        int rand = 0;
        //running test 1000 times
        for (int i = 0; i < 1000; i++){
            //randomly generates any valid 32-bit int value to make sure test is rigorous
            rand = (int)(Math.random()*(Math.pow(2, 31)-1));
            if (Math.round(Math.random()) != 1){
                rand *= -1;
            }
            longWord.set(rand);

            //if rand is positive, preform the same test used in shiftLeftRightLogicalWorks
            if (rand > -1){
                rand = (int) Math.round(Math.random()*31);
                LongWord other = longWord.shiftLeftLogical(rand);
                other = other.shiftRightLogical(rand);
                LongWord otherSum = longWord.shiftRightLogical(32-rand);
                otherSum = otherSum.shiftLeftLogical(32-rand);
                
                if (other.getUnsigned() + otherSum.getUnsigned() != longWord.getUnsigned()){
                    System.out.println("Failed to process: being shifted "+rand+" bits");
                    return false;
                }
            } else {
                rand = (int) Math.round(Math.random()*31);
                LongWord arithmeticShiftWord = longWord.shiftRightArithmetic(rand);

                //set first bit to 1 and arithmetically shift rand-1 to build bitset of all 1's 
                //from index bitsetsize-1 to bitsetsize-rand-1, representing 1's generated from bitshift
                LongWord leadingOnes = new LongWord();
                //special case must be made for shifting 0 bits, which will result in a leadingOnes bitset of all 0's. 
                if (rand!=0){
                    leadingOnes.setBit(31);
                    leadingOnes = leadingOnes.shiftRightArithmetic(rand-1);
                }

                //shift Logically to acquire all numbers not included in leadingOnes
                LongWord otherNums = longWord.shiftRightLogical(rand);

                if (arithmeticShiftWord.getUnsigned() != leadingOnes.getUnsigned()+otherNums.getUnsigned()){
                    System.out.println("Failed to process "+longWord.getSigned()+" being shifted "+rand+ " bits");
                    return false;
                }
            }
        }
        return true;
    }

    public static Boolean ShiftLeftRightLogicalWorks() {
        LongWord longWord = new LongWord();
        int rand = 0;
        //running test 1000 times
        for (int i = 0; i < 1000; i++){
            //randomly generates any valid 32-bit int value to make sure test is rigorous
            rand = (int)(Math.random()*(Math.pow(2, 31)-1));
            if (Math.round(Math.random()) != 1){
                rand *= -1;
            }
            longWord.set(rand);

            //selects a random # bewteen 0 & 31, to be the # of bits shifted and lost 
            //on both sides of the number. If shift works correctly, shifting n bits 
            //off of one side and shifting bitsize-n bits off the other side and adding
            //the two resultsant bit vectors together will reslut in our original vector 
            rand = (int) Math.round(Math.random()*31);
            LongWord other = longWord.shiftLeftLogical(rand);
            other = other.shiftRightLogical(rand);
            LongWord otherSum = longWord.shiftRightLogical(32-rand);
            otherSum = otherSum.shiftLeftLogical(32-rand);
            
            if (other.getUnsigned() + otherSum.getUnsigned() != longWord.getUnsigned()){
                System.out.println("Failed to process: being shifted "+rand+" bits");
                return false;
            }
        }
        return true;
    }


    private static Boolean copyWorks (){
        LongWord longWord = new LongWord();
        LongWord other = new LongWord();
        int rand = 0;
        //running test 1000 times
        for (int i = 0; i < 1000; i++){
            //randomly generates any valid 32-bit int value to make sure test is rigorous
            rand = (int)Math.round((Math.random()*(Math.pow(2, 31)-1)));
            if (Math.round(Math.random()) != 1){
                rand *= -1;
            }
            longWord.set(rand);
            other.copy(longWord);
            if(longWord.getSigned()!=other.getSigned()){
                System.out.println("Failed to process: "+rand);
                return false;
            }
        }
        return true;
    }

    private static Boolean setGetSignedWorks (){
        LongWord longWord = new LongWord();
        int rand = 0;
        //running test 1000 times
        for (int i = 0;i < 1000; i++){
            //randomly generates any valid 32-bit int value to make sure test is rigorous
            rand = (int)Math.round((Math.random()*(Math.pow(2, 31)-1)));
            if (Math.round(Math.random()) != 1){
                rand *= -1;
            }
            longWord.set(rand);
            if(longWord.getSigned()!=rand){
                System.out.println("Failed to process: "+rand);
                return false;
            }
        }
        return true;
    }

    private static Boolean longWorks(){
        LongWord longWord = new LongWord();

        for (int i = 0; i < 1000; i++){
            int rand = (int) Math.round(Math.random()*31);
            int rand2 = (int) Math.round(Math.random()*31);
            while (rand == rand2){
                rand2 = (int) Math.round(Math.random()*31);
            }
            longWord.setBit(rand);
            longWord.setBit(rand2);

            if (longWord.getUnsigned()!=Math.pow(2,rand)+Math.pow(2, rand2)){
                return false;
            } 
            longWord.clearBit(rand);
            longWord.clearBit(rand2);
        }
        return true;
    }
    

    private static Boolean setClearToggleWorking(){

        LongWord longWord = new LongWord();
        for(int i = 0; i < 32; i++){
            //set all bits to 1
            longWord.setBit(i);
        }
        //fail if they aren't all 1
        if (!allones(longWord)){
            return false;
        }
        for(int i = 0; i < 32; i++){
            //set all bits to 0
            longWord.clearBit(i);
        }
        //fail if they aren't all 0
        if (!allzeros(longWord)){
            return false;
        }
        for(int i = 0; i < 32; i++){
            //reset bits to 1
            longWord.toggleBit(i);
        }
        //fail if they aren't all 1
        if (!allones(longWord)){
            return false;
        }
        //succeed by not failing
        return true;
    }
    private static Boolean allzeros(LongWord longWord){
        if (longWord.toString().equals("0000 0000 0000 0000 0000 0000 0000 0000 \t0x00000000")){
            return true;
        } else {
            return false;
        }
    }

    private static Boolean allones(LongWord longWord){
        if (longWord.toString().equals("1111 1111 1111 1111 1111 1111 1111 1111 \t0xffffffff")){
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        runTests();
    }
}