package hva;

/**Using a Strategy pattern to calculate employee satisfaction */
public interface SatisfactionCalculator{
    public float calculateSatisfaction(Worker w);
}