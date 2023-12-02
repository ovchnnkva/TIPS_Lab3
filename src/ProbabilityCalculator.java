import java.util.Scanner;

public class ProbabilityCalculator {

    /**
     * Число разрядов в информационной комбинации
     */
    private int countDigitsInformationCombination;

    /**
     * Прямой канал
     */
    private double directChannel;

    /**
     * Обратный канал
     */
    private double reverseChannel;

    /**
     * Гамма
     */
    private double gamma;

    /**
     * Достоверность
     */
    private double reliability;

    /**
     * Вероятность необнаружения ошибки
     */
    private double probabilityNotFoundError;

    /**
     * Вероятность ошибочного приема одного двоичного символа
     */
    private double probabilityErrorReceptionSingleChar;

    /**
     * Вероятность правильного приема в ПК
     */
    private double probabilityCorrectReceptionPK;

    /**
     * Вероятность обнаружения ошибки в ПК
     */
    private double probabilityFindErrorPK;

    /**
     * Вероятность правильного приёма m-элементной информационной комбинации
     * в канале с биноминальной моделью ошибок
     */
    private double probabilityCorrectReceptionMultiCharBinominalModelError;

    /**
     * Вероятность приема с ошибками
     */
    private double probabilityReceptionWithErrors;

    /**
     * Вероятность выдачи получателю сообщение с ошибкой
     */
    private double probabilityReturnMsgWithError;

    /**
     * Вероятность неправильного приема служебного сигнала
     */
    private double probabilityIncorrectReceptionServiceSignal;

    /**
     * Минимальное значение длины сигнала
     */
    private int minLengthSignal;

    /**
     * Вероятность правильного приема служебного сигнала "стирание"
     */
    private double probabilityCorrectReceptionServiceSignalClear;

    /**
     * Вероятность неправильного приема служебного сигнала "стирание"
     */
    private double probabilityIncorrectReceptionServiceSignalClear;

    /**
     * Допустимое значение вероятность неправильного приема служебного сигнала
     */
    private double acceptProbabilityIncorrectReceptionServiceSignal;
    /**
     * Вероятность потери сообщения за счет искажения служебного сигнала "разрешение"
     */
    private double probabilityLostMsg;

    /**
     * Вероятность вставки сообщения
     */
    private double probabilityInsertingMsg;
    public void start() {
        Scanner in = new Scanner(System.in);
        System.out.println("Число разрядов в информационной комбинации: ");
        countDigitsInformationCombination = in.nextInt();
        System.out.println("Прямой канал: ");
        directChannel = in.nextDouble();
        System.out.println("Обратный канал: ");
        reverseChannel = in.nextDouble();
        System.out.println("Гамма: ");
        gamma = in.nextDouble();
        System.out.println("Достоверность: ");
        reliability = in.nextDouble();

        calculateProbabilities();
        print();
    }
    private void calculateProbabilities() {
        calcProbNotFoundError();
        calcErrorReceptionSingleChar();
        calcCorrectReceptionPK();
        calcProbFindErrorPK();
        calcProbCorrectReceptionMultiCharBinominalModelError();
        calcProbReceptioWithErrors();
        calcReturnMsgWithErrors();
        calcProbIncorrectAcceptServiceSignal();
        calcAcceptProbabilityIncorrectReceptionServiceSignal();
        calcProbabilityIncorrectReceptionServiceSignalClearAndMinLengthSignal();
        calcProbCorrectReceptionServiceSignalClear();
        calcProbIncorrectReceptionServiceSignalClear();
        calcProbLostMsg();
        calcProbInsertingMsg();
    }

    private void calcProbNotFoundError() {
        probabilityNotFoundError = 0.0;
        double c = 0.0;
        for(int i = 1; i < countDigitsInformationCombination + 1; i++) {
            c += factorial(countDigitsInformationCombination) / (factorial(i) * factorial(countDigitsInformationCombination - i));
            probabilityNotFoundError += c * Math.pow(directChannel, i) * Math.pow(1 - directChannel, countDigitsInformationCombination - i)
                    * Math.pow(reverseChannel, i) * Math.pow(1 - reverseChannel, countDigitsInformationCombination - i);
        }
    }

    private void calcErrorReceptionSingleChar() {
        double c = 0.0;
        for(int i = 0; i < countDigitsInformationCombination + 1; i++) {
            c += factorial(countDigitsInformationCombination) / (factorial(i) * factorial(countDigitsInformationCombination - i));
        }

        probabilityErrorReceptionSingleChar = Math.pow(probabilityNotFoundError / c, 0.5);
    }

    private void calcCorrectReceptionPK() {
        probabilityCorrectReceptionPK = Math.pow(1 - directChannel, countDigitsInformationCombination)
                * Math.pow(1 - reverseChannel, countDigitsInformationCombination);
    }

    private void calcProbFindErrorPK() {
        probabilityFindErrorPK = 1 - probabilityCorrectReceptionPK - probabilityNotFoundError;
    }

    private void calcProbCorrectReceptionMultiCharBinominalModelError() {
        probabilityCorrectReceptionMultiCharBinominalModelError = Math.pow(1 - probabilityErrorReceptionSingleChar, countDigitsInformationCombination);
    }

    private void calcProbReceptioWithErrors() {
        probabilityReceptionWithErrors = 0.0;
        double c;
        for(int i = 1; i < countDigitsInformationCombination + 1; i++) {
            c = factorial(countDigitsInformationCombination) / (factorial(i) * factorial(countDigitsInformationCombination - i));
            probabilityReceptionWithErrors += c * Math.pow(probabilityErrorReceptionSingleChar, i)
                    * Math.pow(1 - probabilityErrorReceptionSingleChar, countDigitsInformationCombination - i);
        }
    }

    private void calcReturnMsgWithErrors() {
        probabilityReturnMsgWithError = probabilityNotFoundError / (1 - probabilityFindErrorPK);
    }

    private void calcProbIncorrectAcceptServiceSignal() {
        probabilityIncorrectReceptionServiceSignal = (gamma * probabilityReturnMsgWithError
                * (1 - probabilityFindErrorPK)) /  (1 - 2 * gamma * probabilityReturnMsgWithError * probabilityFindErrorPK);
    }
    private void calcAcceptProbabilityIncorrectReceptionServiceSignal() {
        acceptProbabilityIncorrectReceptionServiceSignal = (gamma * probabilityReturnMsgWithError * (1 - probabilityFindErrorPK)) /
                (1 - 2 * gamma * probabilityReturnMsgWithError * probabilityFindErrorPK);
    }

    private void calcProbabilityIncorrectReceptionServiceSignalClearAndMinLengthSignal() {
        minLengthSignal = 1;
        int minLengthI = (minLengthSignal + 1) / 2;
        double c = factorial(minLengthSignal) / (factorial(minLengthI) * factorial(minLengthSignal - minLengthI));
        probabilityIncorrectReceptionServiceSignal = c * Math.pow(directChannel, minLengthI)
                * Math.pow(1 - Math.pow(directChannel, minLengthI), minLengthSignal - minLengthI);

        while(probabilityIncorrectReceptionServiceSignal > acceptProbabilityIncorrectReceptionServiceSignal) {
            probabilityIncorrectReceptionServiceSignal = 0.0;
            minLengthSignal += 2;
            minLengthI = (minLengthSignal + 1) / 2;

            for(int i = minLengthI; i < minLengthSignal + 1; i++) {
                c = factorial(minLengthSignal) / (factorial(i) * factorial(minLengthSignal - i));
                probabilityIncorrectReceptionServiceSignal += c * Math.pow(directChannel, i)
                        * Math.pow(1 - Math.pow(directChannel, i), minLengthSignal - i);
            }

            if(probabilityIncorrectReceptionServiceSignal <= acceptProbabilityIncorrectReceptionServiceSignal) {
                probabilityIncorrectReceptionServiceSignal = 0.0;
                minLengthI = (minLengthSignal + 1) / 2;

                for(int i = minLengthI; i < minLengthSignal + 1; i++) {
                    c = factorial(minLengthSignal) / (factorial(i) * factorial(minLengthSignal - i));
                    probabilityIncorrectReceptionServiceSignal += c * Math.pow(directChannel, i)
                            * Math.pow(1 - Math.pow(directChannel, i), minLengthSignal - i);
                }
            }
        }
    }

    private void calcProbCorrectReceptionServiceSignalClear() {
        probabilityCorrectReceptionServiceSignalClear = 0.0;
        int l = (minLengthSignal - 1) / 2;
        double c = 0.0;

        for (int i = 0; i < l + 1;i++) {
            c = factorial(l) / (factorial(i) * factorial(l - i));
            probabilityCorrectReceptionServiceSignalClear += c * Math.pow(directChannel, i)
                    * Math.pow(1 - directChannel, minLengthSignal - i);
        }
    }

    public void calcProbIncorrectReceptionServiceSignalClear() {
        probabilityIncorrectReceptionServiceSignalClear = 0.0;
        int l = (minLengthSignal + 1) / 2;
        double c = 0.0;

        for(int i = l; i < minLengthSignal + 1; i++) {
            c = factorial(minLengthSignal) / (factorial(i) * factorial(minLengthSignal - i));
            probabilityIncorrectReceptionServiceSignalClear += c * Math.pow(directChannel, i)
                    * Math.pow(1 - Math.pow(directChannel, i), l - i);
        }
    }

    public void calcProbLostMsg() {
        probabilityLostMsg = ((1 - probabilityFindErrorPK) * probabilityIncorrectReceptionServiceSignalClear) /
                (1 - probabilityFindErrorPK * probabilityCorrectReceptionServiceSignalClear);
    }

    private void calcProbInsertingMsg() {
        probabilityInsertingMsg = (probabilityFindErrorPK * (1 - probabilityFindErrorPK)
                * probabilityCorrectReceptionServiceSignalClear * probabilityIncorrectReceptionServiceSignalClear) /
                Math.pow(1 - probabilityFindErrorPK * probabilityCorrectReceptionServiceSignalClear, 2);
    }
    private void print() {
        System.out.println("Вероятность необнаружения ошибки = " + probabilityNotFoundError);
        System.out.println("Вероятность ошибочного приема одного двоичного символа = " + probabilityErrorReceptionSingleChar);
        System.out.println("Вероятность правильного решения в ПК = " + probabilityCorrectReceptionPK);
        System.out.println("Вероятность обнаружения ошибки в ПК = " + probabilityFindErrorPK);
        System.out.println("Вероятность правильного приема m-элементной инофрмационной комбинации в канале с биноминальной моделью ошибок = " + probabilityCorrectReceptionMultiCharBinominalModelError);
        System.out.println("Вероятность приема с ошибками = " + probabilityReceptionWithErrors);
        System.out.println("Вероятность выдачи получателю сообщение с ошибкой = " + probabilityReturnMsgWithError);
        System.out.println("Допустимое значение вероятность неправильного приема служебного сигнала = " + acceptProbabilityIncorrectReceptionServiceSignal);
        System.out.println("Минимальное значение длины сигнала = " + minLengthSignal);
        System.out.println("Вероятность неправильного приема служебного сигнала = " + probabilityIncorrectReceptionServiceSignal);
        System.out.println("Вероятность правильного приема служебного сигнала разрешение (стирание) = " + probabilityCorrectReceptionServiceSignalClear);
        System.out.println("Вероятность неправильного приема служебного сигнала разрешение (стирание) = " + probabilityIncorrectReceptionServiceSignalClear);
        System.out.println("Вероятность потери сообщения за счет искажения служебного сигнала разрешение = " + probabilityLostMsg);
        System.out.println("Вероятность вставки сообщения = " + probabilityInsertingMsg);
    }

    private static double factorial(int n) {
        double result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public int getCountDigitsInformationCombination() {
        return countDigitsInformationCombination;
    }

    public double getDirectChannel() {
        return directChannel;
    }

    public double getReverseChannel() {
        return reverseChannel;
    }

    public double getGamma() {
        return gamma;
    }

    public double getReliability() {
        return reliability;
    }

    public double getProbabilityNotFoundError() {
        return probabilityNotFoundError;
    }

    public double getProbabilityErrorReceptionSingleChar() {
        return probabilityErrorReceptionSingleChar;
    }

    public double getProbabilityCorrectReceptionPK() {
        return probabilityCorrectReceptionPK;
    }

    public double getProbabilityFindErrorPK() {
        return probabilityFindErrorPK;
    }

    public double getProbabilityCorrectReceptionMultiCharBinominalModelError() {
        return probabilityCorrectReceptionMultiCharBinominalModelError;
    }

    public double getProbabilityReceptionWithErrors() {
        return probabilityReceptionWithErrors;
    }

    public double getProbabilityReturnMsgWithError() {
        return probabilityReturnMsgWithError;
    }

    public double getProbabilityIncorrectReceptionServiceSignal() {
        return probabilityIncorrectReceptionServiceSignal;
    }

    public int getMinLengthSignal() {
        return minLengthSignal;
    }

    public double getProbabilityCorrectReceptionServiceSignalClear() {
        return probabilityCorrectReceptionServiceSignalClear;
    }

    public double getProbabilityIncorrectReceptionServiceSignalClear() {
        return probabilityIncorrectReceptionServiceSignalClear;
    }

    public double getAcceptProbabilityIncorrectReceptionServiceSignal() {
        return acceptProbabilityIncorrectReceptionServiceSignal;
    }

    public double getProbabilityLostMsg() {
        return probabilityLostMsg;
    }

    public double getProbabilityInsertingMsg() {
        return probabilityInsertingMsg;
    }
}
