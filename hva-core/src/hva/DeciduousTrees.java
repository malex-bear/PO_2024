package hva;
import java.lang.Math;
/** Class reprsenting a tree of type "Caduca"*/
public class DeciduousTrees extends Tree{
    private String _type = "CADUCA";
    private int _cleaningDifficulty;
    private int _seasonEffort; 

    public DeciduousTrees(String id, String name, int age, 
                            Seasons initial, int dif){
        super(id, name, age, initial);
        _cleaningDifficulty = dif;
    }

    @Override
    public double cleaningEffort(){
        return  _cleaningDifficulty * _seasonEffort * 
                Math.log(super.getAge() + 1);
    }

    @Override
    public void setSeasonEffort(Seasons season){
        if (season == Seasons.INVERNO)
            _seasonEffort = 0;
        else if (season == Seasons.PRIMAVERA)
            _seasonEffort = 1;
        else if (season == Seasons.VERAO)
            _seasonEffort = 2;
        else 
            _seasonEffort = 5;
    }

    @Override
    public String setBiologicalCycle(){

        Seasons season = super.getSeason();

        switch (season.name()) {
            case "INVERNO":
                return "SEMFOLHAS";
            case "PRIMAVERA":
                return "GERARFOLHAS";
            case "VERAO":
                return "COMFOLHAS";
            default:
                return "LARGARFOLHAS";
        }

    }

    @Override
    public String toString(){
        return super.toString() + "|" +_cleaningDifficulty + 
                "|" + _type + "|" + this.setBiologicalCycle();
    }
}