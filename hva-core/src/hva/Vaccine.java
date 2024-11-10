package hva;

import java.util.Map;
import java.util.TreeMap;
import java.io.Serializable;

public class Vaccine implements Serializable{
    private String _id;
    private String _name;
    private Map<String,Species> _speciesAllowed = new TreeMap<>();  
    private int _numApplications = 0;  

    public Vaccine(String id, String name){
        _id = id;
        _name = name;
    }

    public Vaccine(String id, String name,Map<String,Species> species){
        _id = id;
        _name = name;
        _speciesAllowed = species;
    }

    public String getId(){
        return _id;
    }

    public void updateApplications(){
        _numApplications++;
    }

    public boolean canVaccinateSpecies(String idSpecies){
        return _speciesAllowed.containsKey(idSpecies);
    }

    public int findMaxStringSize(){
        int max = 0;
        for(Species s : _speciesAllowed.values()){
            if (s.getName().length() > max)
                max = s.getName().length();
        }
        return max;
    }

    public int findCommonCaracters(String s1, String s2){
        int count = 0;
        /* Convert the second string to a mutable StringBuilder 
        to allow character removal*/
        StringBuilder sb2 = new StringBuilder(s2);

        for (char c : s1.toCharArray()) {
            int index = sb2.indexOf(String.valueOf(c));
            if (index != -1) {
                count++;
                /* Remove the character so it's not counted again */
                sb2.deleteCharAt(index); 
            }
        }

        return count;
    }

    public Damage determineDamage(Species animalSpecies){
        int damage = 0;
        for(Species s : _speciesAllowed.values()){
            if(s.getId().equals(animalSpecies.getId())){
                return Damage.NORMAL;
            }

            int aux = (Math.max(animalSpecies.getName().length(),
                    s.getName().length())) - 
                    this.findCommonCaracters(animalSpecies.getName(),
                                            s.getName());
            if (aux > damage){
                damage = aux;
            }
        }
        
        if (damage == 0){
            return Damage.CONFUSÃO;
        }
        else if (damage >= 1 && damage <= 4)
            return Damage.ACIDENTE;
        else
            return Damage.ERRO;
    }

    public String showSpecies(){
        StringBuilder sb = new StringBuilder();
        int size = _speciesAllowed.size(); 
        int count = 0;  

        for(Map.Entry<String,Species>entry:_speciesAllowed.entrySet()){
            sb.append(entry.getValue().getId());
            
            count++;  
            if (count < size) {
                sb.append(",");  
            }
        }

        return sb.toString();
    }

    @Override
    public String toString(){
        if (this.showSpecies() == null || 
            this.showSpecies().equals(""))
            return "VACINA|" + _id + "|" + _name + 
                    "|" + _numApplications;
        else 
            return "VACINA|" + _id + "|" + _name + "|" 
                    + _numApplications + "|" + this.showSpecies();
    }

}
