package hva;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**Class that represents an animal. */
public class Animal implements Serializable{
    private String _id;
    private String _name;
    private Species _species;
    private List<Vaccination> _healthRecords = new ArrayList<>();
    private Habitat _habitat;


    public Animal(String id,String name,Species species,
                    Habitat habitat){
        _id = id;
        _name = name;
        _species = species;
        _habitat = habitat;
    }

    public String getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    public Species getSpecies(){
        return _species;
    }

    public Habitat getHabitat(){
        return _habitat;
    }

    public void setHabitat(Habitat h){
        _habitat = h;
    }

    public void updateHealthRecords(Vaccination v){
        _healthRecords.add(v);
    }

    public String showHealthRecords(){
        if (_healthRecords == null || _healthRecords.size() == 0){
            return "VOID";
        }
        else{
            StringBuilder health = new StringBuilder();
            int count = 0;
            for (Vaccination v : _healthRecords){
                health.append(v.getDamage().name());

                count++;  
                if (count < _healthRecords.size()) {
                    health.append(",");  
                }
            }

            return health.toString();
        }
    }

    public String showVaccinations(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int size = _healthRecords.size();
        for (Vaccination v : _healthRecords){
            sb.append(v);

            count++;  
            if (count < size) {
                sb.append("\n");  
            }
        }

        return sb.toString();
    }

    @Override
    public String toString(){
        return "ANIMAL|" + _id + "|" + _name + "|" + 
                _species.getId() + "|" + this.showHealthRecords() + 
                "|" + _habitat.getId();
    }
}
