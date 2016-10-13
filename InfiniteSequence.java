import java.math.BigInteger;
import java.util.Scanner;

public class InfiniteSequence {
    public static void main(String[] argvs) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter parameters in a proper format:");

        String input = scan.nextLine().trim();
        int aSequenceLen = input.length();

        if (aSequenceLen < 1 || aSequenceLen > 50) {
            System.err.println("Error: sequence length should be in range [1,50]");
            System.exit(1);
        }

        BigInteger aSequence = new BigInteger(input);
        BigInteger sequenceLastNumber = BigInteger.ONE;
        BigInteger shift = BigInteger.ZERO;

        String infiniteSequence = sequenceLastNumber.toString();

        while (true) {
            BigInteger index = BigInteger.valueOf(infiniteSequence.indexOf(aSequence.toString()) + 1);

            if (index != BigInteger.ZERO) {
                System.out.println(shift.add(index).toString());
                break;
            }

            int infiniteSequenceLen = infiniteSequence.length();
            if (infiniteSequenceLen > aSequenceLen) {
                int diff = infiniteSequenceLen - aSequenceLen;
                infiniteSequence = infiniteSequence.substring(diff);
                shift = shift.add(BigInteger.valueOf(diff));
            }

            sequenceLastNumber = sequenceLastNumber.add(BigInteger.ONE);

            infiniteSequence += sequenceLastNumber.toString();
        }
    }
}
