package hva;
import java.util.Map;;

public class SatisfactionCaretaker implements SatisfactionCalculator{

    @Override
    public float calculateSatisfaction(Worker w){
        Caretaker caretaker = (Caretaker) w;
        
        Map<String, Habitat> res = caretaker.getResponsabilities();
        float work = 0;
        for (Habitat habitat : res.values()) {
            work += (float) habitat.calculateWorkloadInHabitat() / 
                    habitat.hasSameResponsability();
        }
        return 300 - work;
    }
}
