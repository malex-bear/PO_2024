package hva;
import java.util.Map;

public class SatisfactionVeterinarian implements SatisfactionCalculator{

    @Override
    public float calculateSatisfaction(Worker w){
        Veterinarian vet = (Veterinarian) w;

        Map<String, Species> res = vet.getResponsabilities();
        float work = 0;
        for (Species species : res.values()) {
            work += (float) species.getPopulation() / 
                    species.hasSameResponsability();
        }
        return 20 - work;
    }
}
