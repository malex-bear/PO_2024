package hva;
import java.lang.Math;
/**Class representing a tree of type "Perene" */
public class PerennialTrees extends Tree{
    private String _type = "PERENE";
    private int _cleaningDifficulty;
    private int _seasonEffort;

    public PerennialTrees(String id, String name, int age, 
                            Seasons initial, int dif){
        super(id, name, age, initial);
        _cleaningDifficulty = dif;
    }

    @Override
    public double cleaningEffort(){
        return (double) _cleaningDifficulty * _seasonEffort * 
                Math.log(super.getAge() + 1);
    }

    @Override
    public void setSeasonEffort(Seasons season){
        if (season == Seasons.INVERNO)
            _seasonEffort = 2;
        else
            _seasonEffort = 1;
    }

    @Override
    public String setBiologicalCycle(){

        Seasons season = super.getSeason();

        switch (season.name()) {
            case "INVERNO":
                return "LARGARFOLHAS";
            case "PRIMAVERA":
                return "GERARFOLHAS";
            default:
                return "COMFOLHAS";
        }
    }

    @Override
    public String toString(){
        return super.toString() + "|" + _cleaningDifficulty + 
                "|" + _type + "|" + this.setBiologicalCycle();
    }
}
