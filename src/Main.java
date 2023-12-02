public class Main {
    private static final ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator();
    private static final IOC1 ioc1 = new IOC1();
    private static final IOC2 ioc2 = new IOC2();
    public static void main(String[] args) {
        probabilityCalculator.start();
        ioc1.randomGeneration(probabilityCalculator);
        ioc2.randomGeneration(probabilityCalculator);
    }
}