package hva;

import java.util.Map;
import java.util.ArrayList;
import java.util.TreeMap;

public class Veterinarian extends Worker{
    private Map<String,Species> _speciesToTreat = new TreeMap<>();
    private ArrayList<Vaccination> _vaccinations = new ArrayList<>();

    public Veterinarian(String id, String name){
        super(id,name);
    }

    public Veterinarian(String id, String name, 
                    Map<String,Species> species){
        super(id,name);
        _speciesToTreat = species;
    }

    public void addSpecies(Species s){
        _speciesToTreat.put(s.getId(), s);
    }

    public void removeSpecies(Species s){
        _speciesToTreat.remove(s.getId(), s);
    }

    public boolean hasResponsability(String idResponsability){
        return _speciesToTreat.containsKey(idResponsability);
    }

    public Map<String,Species> getResponsabilities(){
        return _speciesToTreat;
    }

    public void addVaccination(Vaccination v){
        _vaccinations.add(v);
    }

    public String showResponsabilities(){
        StringBuilder sb = new StringBuilder();
        int size = _speciesToTreat.size(); 
        int count = 0;  

        for (Map.Entry<String,Species> entry:_speciesToTreat.entrySet()){
            sb.append(entry.getKey()); 

            count++;  
            if (count < size) {
                sb.append(",");  
            }
        }

        return sb.toString();
    }

    public String showVaccinations(){
        StringBuilder sb = new StringBuilder();
        int size = _vaccinations.size(); 
        int count = 0;  

        for (Vaccination v : _vaccinations) {
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
        if (showResponsabilities() == null || 
            showResponsabilities().equals(""))
            return "VET|" + super.toString();
        else
            return "VET|" + super.toString() + "|" + showResponsabilities();
    }
}
