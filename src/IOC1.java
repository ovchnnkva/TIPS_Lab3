import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class IOC1 {
    /**
     * Количество сообщений
     */
    private int countMsg;

    /**
     * Количество Вып
     */
    private int countVip;

    /**
     * Количество ошибок
     */
    private int countOsh;

    /**
     * Количество Вбо
     */
    private int countVbo;

    /**
     * Количество Вст
     */
    private int countVst;

    /**
     * Служебный сигнао
     */
    private String serviceSignal;
    /**
     * Значение информационной комбинации источника
     * с минимальной длиной сигнала
     */
    private String infCombWithMinLengthSignal = "";
    private String infCombWithMinLengthSignal2 = "";

    private ProbabilityCalculator probabilityCalculator ;

    private StringBuilder table = new StringBuilder();


    public void start(ProbabilityCalculator probabilityCalculator) {
        this.probabilityCalculator = probabilityCalculator;

        table.append(String.format("%10s%20s%20s%20s%20s%20s%20s%20s",
            "№ С", "ИКИ", "П из Н", "ИКвН","КвОК", "СС", "ПСС", "ИП")).append("\n");

        Scanner in = new Scanner(System.in);
        System.out.println("Введите количество сообщений: ");
        countMsg = in.nextInt();
        for (int i = 0; i < countMsg; i++) {
            System.out.println("Введите значение информационной комбинации источника с минимальной длинной сигнала " + probabilityCalculator.getMinLengthSignal());
            infCombWithMinLengthSignal = in.next();
            while ((infCombWithMinLengthSignal.length() + infCombWithMinLengthSignal2.length()) < probabilityCalculator.getMinLengthSignal()) {
                infCombWithMinLengthSignal2 += "0";
            }
            infCombWithMinLengthSignal += infCombWithMinLengthSignal2;

            System.out.println("Введите значение служебного сигнала: ");
            serviceSignal = in.next();

            calc();
        }
        System.out.println(table);
    }
    public void randomGeneration(ProbabilityCalculator probabilityCalculator) {
        this.probabilityCalculator = probabilityCalculator;
        table.append(String.format("%10s%20s%20s%20s%20s%20s%20s%20s",
                "№ С", "ИКИ", "П из Н", "ИКвН","КвОК", "СС", "ПСС", "ИП")).append("\n");

        Scanner in = new Scanner(System.in);
        System.out.println("Введите количество сообщений: ");
        countMsg = in.nextInt();

        for (int i = 0; i < countMsg; i++) {
            infCombWithMinLengthSignal = "";
            for (int j = 0; j < 5; j++) {
                infCombWithMinLengthSignal += String.valueOf(ThreadLocalRandom.current().nextInt(0, 2));
            }
            while (infCombWithMinLengthSignal.length() < probabilityCalculator.getMinLengthSignal()) {
                infCombWithMinLengthSignal += "1";
            }
            serviceSignal = "";

            for (int j = 0; j < 3; j++) {
                serviceSignal += String.valueOf(ThreadLocalRandom.current().nextInt(0, 2));
            }

            calc();
        }

        System.out.println(table);
    }

    private void calc() {
        String pIsN = "-";
        String ikVn = infCombWithMinLengthSignal;
        String kVok = ikVn;
        String serviceSignalV = serviceSignal;
        String ip = "Вбо";
        int iter = 0;

        double distortionProb = ThreadLocalRandom.current().nextDouble(0.004, 0.08);
        StringBuilder distortionIkVn;
        if(distortionProb <= probabilityCalculator.getProbabilityReceptionWithErrors()) {
            distortionIkVn = new StringBuilder();

            for(Character bit : ikVn.toCharArray()) {
                if(bit.equals('1')) distortionIkVn.append("0");
                else distortionIkVn.append("1");

                ikVn = distortionIkVn.toString();
            }
        }

        distortionProb = ThreadLocalRandom.current().nextDouble(0.004, 0.08);
        if(distortionProb <= probabilityCalculator.getProbabilityReturnMsgWithError()) {
            StringBuilder distortionIkVok = new StringBuilder();
            for(Character bit : kVok.toCharArray()) {
                if(bit.equals('1')) distortionIkVok.append("0");
                else distortionIkVok.append("1");
            }

            kVok = distortionIkVok.toString();
        }

        if(distortionProb <= probabilityCalculator.getProbabilityIncorrectReceptionServiceSignal()) {
            StringBuilder distortedSlSig = new StringBuilder();
            countOsh += 1;

            for (Character bit : serviceSignal.toCharArray()) {
                if(bit.equals('1')) distortedSlSig.append("0");
                else distortedSlSig.append("1");
            }

            serviceSignalV = distortedSlSig.toString();
            ip = "ВсО и ПП";
            String ikis = infCombWithMinLengthSignal;
            iter += 1;
            table.append(print(String.valueOf(iter), infCombWithMinLengthSignal, pIsN, ikVn, kVok, serviceSignal, serviceSignalV, ip)).append("\n");

            while(distortionProb <= probabilityCalculator.getProbabilityReturnMsgWithError()) {
                distortionProb = ThreadLocalRandom.current().nextDouble(0.004, 0.08);
                pIsN = ikis;
                infCombWithMinLengthSignal = "-";
                ip = "ВсО и ПП";
                table.append(print("-", infCombWithMinLengthSignal, pIsN, ikVn, kVok, serviceSignal, serviceSignalV, ip)).append("\n");
            }

            ip = "ВбО";
            pIsN = ikis;
            infCombWithMinLengthSignal = "-";
            table.append(print("-", infCombWithMinLengthSignal, pIsN, ikVn, kVok, serviceSignal, serviceSignalV, ip)).append("\n");

        } else if (distortionProb <= (probabilityCalculator.getProbabilityReturnMsgWithError()
                                      + probabilityCalculator.getProbabilityLostMsg())) {
            StringBuilder distortedSlSig = new StringBuilder();

            distortedSlSig.append("1".repeat(serviceSignal.length()));
            serviceSignalV = distortedSlSig.toString();
            countVip += 1;
            ip = "Вып";
            iter += 1;
            table.append(print(String.valueOf(iter), infCombWithMinLengthSignal, pIsN, ikVn, kVok, serviceSignal, serviceSignalV, ip)).append("\n");

        } else if(distortionProb <= (probabilityCalculator.getProbabilityReturnMsgWithError()
                + probabilityCalculator.getProbabilityLostMsg()
                + probabilityCalculator.getProbabilityInsertingMsg())) {

            StringBuilder distortedSlSig = new StringBuilder();
            distortedSlSig.append("1".repeat(serviceSignal.length()));

            serviceSignalV = distortedSlSig.toString();
            countVip += 1;
            ip = "Вст";
            iter += 1;
            table.append(print(String.valueOf(iter), infCombWithMinLengthSignal, pIsN, ikVn, kVok, serviceSignal, serviceSignalV, ip)).append("\n");

        } else if(distortionProb >= (probabilityCalculator.getProbabilityReturnMsgWithError()
                + probabilityCalculator.getProbabilityLostMsg()
                + probabilityCalculator.getProbabilityInsertingMsg())) {
            countVbo += 1;
            serviceSignalV = serviceSignal;
            ip = "ВбО";
            iter += 1;
            table.append(print(String.valueOf(iter), infCombWithMinLengthSignal, pIsN, ikVn, kVok, serviceSignal, serviceSignalV, ip)).append("\n");
        }
    }
    private String print(String i, String iki, String pIsN, String ikVn, String kVok,
                         String serviceSignal, String serviceSignalV, String ip) {
        return String.format("%10s%20s%20s%20s%20s%20s%20s%20s",
                i, iki, pIsN, ikVn, kVok, serviceSignal, serviceSignalV, ip);
    }
}
