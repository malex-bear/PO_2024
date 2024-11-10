package hva;

import java.util.Map;
import java.util.TreeMap;
import java.io.Serializable;

/**Class representing a species. */
public class Species implements Serializable{
    private String _id;
    private String _name;
    private Map<String,Animal> _animals;
    private Map<String,Veterinarian> _vetsResponsability;

    public Species(String id, String name){
        _id = id;
        _name = name;
        _animals = new TreeMap<>();
        _vetsResponsability = new TreeMap<>();
    }

    public String getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    public Map<String,Animal> getListAnimals(){
        return _animals;
    }

    public int hasSameResponsability(){
        return _vetsResponsability.size();
    }

    public void addResponsability(Veterinarian v){
        _vetsResponsability.put(v.getId(), v);
    }

    public void removeResponsability(Veterinarian v){
        _vetsResponsability.remove(v.getId(), v);
    }

    public int getPopulation(){
        return _animals.size();
    }

    public void addAnimal(Animal a){
        _animals.put(a.getId(), a);
    }

    public Animal getAnimal(String id){
        return _animals.get(id);
    }

}
