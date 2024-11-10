package hva;
import java.io.Serializable;

/**Class representing a tree. */
public abstract class Tree implements Serializable{
    private String _id;
    private String _name;
    private int _age;
    private Seasons _initialSeason;
    private Seasons _season;

    public Tree(String id, String name, int age, Seasons initial){
        _id = id;
        _name = name;
        _age = age;
        _initialSeason = initial;
        _season = initial;
    }

    public String getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    public int getAge(){
        return _age;
    }

    public Seasons getSeason(){
        return _season;
    }

    public void updateSeason(Seasons season){
        _season = season;
        if (_initialSeason == season){
            _age++;
        }
    }

    public abstract void setSeasonEffort(Seasons season);

    public abstract String setBiologicalCycle();

    public abstract double cleaningEffort();

    @Override
    public String toString(){
        return "ÁRVORE|" + _id + "|" + _name + "|" + _age;
    }
}
