package hva;

import java.util.Map;
import java.util.TreeMap;

/**Class represents worker who is a caretaker. */
public class Caretaker extends Worker {
    private Map<String,Habitat> _habitats = new TreeMap<>();

    public Caretaker(String id, String name){
        super(id,name);
    }

    public Caretaker(String id,String name,
                    Map<String,Habitat>habitats){
        super(id, name);
        _habitats = habitats;
    }


    public void updateHabitats(Habitat h){
        _habitats.put(h.getId(), h);
    }

    public void removeHabitats(Habitat h){
        _habitats.remove(h.getId(), h);
    }

    public boolean hasResponsability(String idResponsability){
        return _habitats.containsKey(idResponsability);
    }

    public Map<String,Habitat> getResponsabilities(){
        return _habitats;
    }

    public void updateAnimals(Animal a){
        
    }

    public String showResponsabilities(){
        StringBuilder sb = new StringBuilder();
        int size = _habitats.size(); 
        int count = 0; 

        for(Map.Entry<String, Habitat> entry : _habitats.entrySet()){
            sb.append(entry.getKey()); 

            count++;  
            if (count < size) {
                sb.append(",");  
            }
        }

        return sb.toString();
    }

    @Override
    public String toString(){
        if (this.showResponsabilities() == null || 
            this.showResponsabilities().equals(""))
            return "TRT|" + super.toString(); 
        else
            return "TRT|" + super.toString() + "|" + 
            this.showResponsabilities();
    }
}
