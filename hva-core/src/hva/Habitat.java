package hva;
import java.util.Map;
import java.util.TreeMap;
import java.io.Serializable;

/**Class representing a habitat. */
public class Habitat implements Serializable{
    private String _id;
    private String _name;
    private int _dimension;
    private Map<String,Tree> _trees = new TreeMap<>();
    private Map<String,Animal> _animals = new TreeMap<>();
    private Map<String,String> _influence = new TreeMap<>();
    private Map<String,Caretaker> _ctResponsability = new TreeMap<>();

    public Habitat(String id, String name, int dimension){
        _id = id;
        _name = name;
        _dimension = dimension;
    }

    public Habitat(String id, String name, int dimension, 
                    Map<String,Tree> trees){
        _id = id;
        _name = name;
        _dimension = dimension;
        _trees = trees;
    }

    public String getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    public int getDimension(){
        return _dimension;
    }

    public int getPopulation(){
        return _trees.size();
    }

    public int getNumAnimals(){
        return _animals.size();
    }

    public Map<String,Tree> getTrees(){
        return _trees;
    }

    public Map<String,Animal> getAnimals(){
        return _animals;
    }

    public int hasSameResponsability(){
        return _ctResponsability.size();
    }

    public void addResponsability(Caretaker ct){
        _ctResponsability.put(ct.getId(), ct);
    }

    public void removeResponsability(Caretaker ct){
        _ctResponsability.remove(ct.getId(), ct);
    }

    public int calculateCorrespondence(Animal animal){
        String idSpecies = animal.getSpecies().getId();
        String inf = _influence.get(idSpecies);
        switch (inf) {
            case "POS":
                return 20;
            case "NEG":
                return -20;
            default:
                return 0;
        }
    }

    public int calculateEqualSpecies(Animal animal){
        int count = 0;
        Species s = animal.getSpecies();
        for(Animal a : _animals.values()){
            if (a.getSpecies().getId().equals(s.getId()) &&
                !a.getId().equals(animal.getId()))
                count++;
        }

        return count;
    }

    public float calculateWorkloadInHabitat(){
        float effort = 0;
        for (Tree tree : _trees.values()){
            effort += tree.cleaningEffort();
        }
        return (float) _dimension + (3 * this.getNumAnimals()) + effort;
    }

    public void addTree(Tree tree){
        _trees.put(tree.getId(), tree);
    }

    public void setDimension(int newD){
        _dimension = newD;
    }

    public String viewTrees(){
        StringBuilder sb = new StringBuilder();
        int size = _trees.size(); 
        int count = 0;  

        for (Map.Entry<String, Tree> entry : _trees.entrySet()) {
            sb.append(entry.getValue());
            
            count++;  
            if (count < size) {
                sb.append("\n");  
            }
        }

        return sb.toString();
    }

    public String viewAnimals(){
        StringBuilder sb = new StringBuilder();
        int size = _animals.size(); 
        int count = 0;  

        for (Map.Entry<String, Animal> entry : _animals.entrySet()) {
            sb.append(entry.getValue());
            
            count++;  
            if (count < size) {
                sb.append("\n");  
            }
        }

        return sb.toString();
    }

    public void addAnimal(Animal a){
        _animals.put(a.getId(), a);
        if(this.getInfluence(a.getSpecies().getId()) == null)
            this.setInfluence(a.getSpecies().getId(), "NEU");
    }

    public void removeAnimal(Animal a){
        _animals.remove(a.getId(),a);
    }

    public String getInfluence(String species){
        return _influence.get(species);
    }

    public void setInfluence(String s, String influence){
        _influence.put(s, influence);
    }

    public void addInfluence(String s, String influence){
        _influence.replace(s, influence);
    }

    @Override
    public String toString(){
        String hStr = "HABITAT|" + _id + "|" + _name + "|" +
                    _dimension + "|" + this.getPopulation();
        String tStr = this.viewTrees();
        if (tStr.equals(""))
            return hStr;
        else
            return hStr + "\n" + tStr;
    }
}
