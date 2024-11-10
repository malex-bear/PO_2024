package hva;

import java.io.Serial;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import hva.exceptions.DupTreeException;
import hva.exceptions.DuplicateAnimalException;
import hva.exceptions.DuplicateHabitatException;
import hva.exceptions.DuplicateNameSpeciesException;
import hva.exceptions.DuplicateVaccineException;
import hva.exceptions.DuplicateWorkerException;
import hva.exceptions.ImportFileException;
import hva.exceptions.UnknownAnimalException;
import hva.exceptions.UnknownHabitatException;
import hva.exceptions.UnknownSpeciesException;
import hva.exceptions.UnknownVaccineException;
import hva.exceptions.UnknownVeterinarianException;
import hva.exceptions.UnknownWorkerException;
import hva.exceptions.UnrecognizedEntryException;
import hva.exceptions.VetNotAuthorizedException;
import hva.exceptions.NotAppropriateException;

public class Hotel implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private Map<String,Veterinarian> _veterinarians;
    private Map<String,Caretaker> _caretakers;
    private Map<String,Habitat> _habitats;
    private Map<String,Vaccine> _vaccines;
    private Map<String,Worker> _employees;
    private Map<String,Species> _species;
    private List<Vaccination> _vaccinations;
    private Seasons _season = Seasons.PRIMAVERA;
    private boolean _changed = false;
    private String _filename;
    private SatisfactionCalculator _satisfactionStrategy;

    public Hotel(){
        _veterinarians = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _caretakers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _habitats = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _vaccines = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _employees = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _species = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _vaccinations = new ArrayList<>();
    }

    /**
     * Advances the hotel's season.
     * @return 1 if summer, 2 if fall, 3 if winter, 0 if spring
     */
    public String advanceSeason(){
        String s;
        if (_season == Seasons.PRIMAVERA){
            _season = Seasons.VERAO;
            s = "1";
        }
        else if (_season == Seasons.VERAO){
            _season  = Seasons.OUTONO;
            s = "2";
        }
        else if (_season == Seasons.OUTONO){
            _season = Seasons.INVERNO;
            s = "3";
        }
        else {
            _season = Seasons.PRIMAVERA;
            s = "0";
        }
        
        for (Habitat h : _habitats.values()){
            for(Tree t : h.getTrees().values()){
                t.updateSeason(_season);
                t.setSeasonEffort(_season);
            }
        }

        this.changed();
        return s;

    }

    /**
     * Register an employee to this hotel.
     * 
     * @param id worker's key
     * @param name worker's name
     * @param type VET or TRT
     */
    public void addWorker(String id, String name, String type){
        if(type.equals("VET")){
            Veterinarian vet = new Veterinarian(id, name);
            _veterinarians.put(id, vet);
            _employees.put(id, vet);
        }
        else{
            Caretaker trt = new Caretaker(id, name);
            _caretakers.put(id, trt);
            _employees.put(id, trt);
        }
        this.changed();;
    }

    /**
     * Register an animal to this hotel by associating him to a 
     * habitat and a species.
     * 
     * @param id animals'key
     * @param name animal's name
     * @param idSpecies species in which animal is a part of
     * @param idHabitat habitat in which animal is a part of 
     */
    public void addAnimal(String id, String name, String idSpecies,
                            String idHabitat){
        Species s = _species.get(idSpecies);
        Habitat h = _habitats.get(idHabitat);
        Animal a = new Animal(id, name, s, h);
        
        s.addAnimal(a);
        h.addAnimal(a);
        h.setInfluence(idSpecies, "NEU");
        _species.replace(idSpecies, s);
        _habitats.replace(idHabitat, h);
        this.changed();

    }

    /**
     * Register a habitat to this hotel.
     * 
     * @param id habitat's key
     * @param name habitat's name
     * @param dimension habitat's dimension
     */
    public void addHabitat(String id, String name, int dimension){
        Habitat h = new Habitat(id, name, dimension);
        _habitats.put(id, h);
        this.changed();
    }

    /**
     * Register a vaccine to this hotel.
     * 
     * @param id vaccine's key
     * @param name vaccine's name
     * @param speciesKeys array of species for which this vaccine can be
     *               applied
     */
    public void addVaccine(String id, String name, String[] speciesKeys) 
                            throws UnknownSpeciesException{
        Map<String,Species> speciesMap = new TreeMap<>();
        for (String speciesKey : speciesKeys) {
                speciesKey = speciesKey.trim();  
                Species species = this.getSpecies(speciesKey);  

                if (species == null) { 
                    throw new UnknownSpeciesException(speciesKey);
                }

                speciesMap.put(speciesKey, species);
            }

        Vaccine v = new Vaccine(id, name, speciesMap);
        _vaccines.put(id, v);
        this.changed();
    }

    /**
     * Register a species to this hotel.
     * 
     * @param s species
     */
    public void addSpecies(Species s){
        _species.put(s.getId(), s);
        this.changed();

    }

    /**
     * Sets the strategy pattern satisfaction.
     * @param sat satisfaction class
     */
    public void setSatisfactionStrategy(SatisfactionCalculator sat){
        _satisfactionStrategy = sat;
    }

    /**
     * Get the species associated to the key given.
     * 
     * @param id species' key
     * @return species 
     */
    public Species getSpecies(String id){
        return _species.get(id);
    }

    /**
     * Set habitat's dimension.
     * 
     * @param id habitat's key
     * @param dimension habitat's dimension
     */
    public void setAreaHabitat(String id, int dimension){
        Habitat h = _habitats.get(id);
        h.setDimension(dimension);
        this.changed();
    }

    /**
     * Changes a habitat's influence on a species
     * @param idHabitat habitat's key
     * @param idS species' key
     * @param influence POS, NEG, NEU
     */
    public void changeInfluenceHabitat(String idHabitat, String idS,
                                        String influence){
        Habitat h = this.getHabitat(idHabitat);
        h.setInfluence(idS, influence);
        this.changed();
    }

    /**
     * Register a tree in the given habitat.
     * 
     * @param hID habitat's key
     * @param id tree's key
     * @param name tree's name
     * @param age tree's age
     * @param difficulty tree's cleaning difficulty
     * @param type CADUCA or PERENE
     * @return tree added to habitat
     */
    public Tree addTreeToHabitat(String hID, String id, String name, 
                                int age, int difficulty, String type)
                throws DupTreeException{
        if(this.existsTree(id))
            throw new DupTreeException();
        Habitat h = _habitats.get(hID);
        if(type.equals("CADUCA")){
            DeciduousTrees dTree = new DeciduousTrees(id, name, age,
                                    _season, difficulty);
            dTree.setSeasonEffort(_season);
            h.addTree(dTree);
            this.changed();
            return dTree;
        }
        else{
            PerennialTrees pTree = new PerennialTrees(id, name, age,
                                    _season, difficulty);
            pTree.setSeasonEffort(_season);
            h.addTree(pTree);
            this.changed();
            return pTree;
        }
        
    }

    /**
     * Get animal based on the key given.
     * 
     * @param animalId animal's key
     * @return animal found
     */
    public Animal findAnimal(String animalId){
        for(Map.Entry<String,Species> species : _species.entrySet()){
            Map<String,Animal> a = species.getValue().getListAnimals();
            for (String key : a.keySet()) {
                if (key.equalsIgnoreCase(animalId)) {
                    return a.get(key);
                }
            }
        }
        return null;
    }

    /**
     * Removes animal from habitat and transfers to a new one.
     * 
     * @param animalId animal's key
     * @param h2 key of new habitat
     */
    public void transferAnimalHabitats(String animalId, String h2){
        Animal animal = this.findAnimal(animalId);
        Habitat hNew = _habitats.get(h2);
        Habitat hOld = _habitats.get(animal.getHabitat().getId());
        hOld.removeAnimal(animal);
        animal.setHabitat(hNew);
        hNew.addAnimal(animal);

        this.changed();
    }


    /**
     * Change hotel's state to indicate that changes have been made.
     */
    public void changed() {
        this.setChanged(true);
    }

    /**
     * Verify if hotel has been changed.
     * @return true if it has changed and false otherwise
     */
    public boolean hasChanged() {
        return _changed;
    }

    /**
     * Set hotel's state to indicate if it has changed or not.
     * @param changed true if changed and false otherwise
     */
    public void setChanged(boolean changed) {
        _changed = changed;
    }

    /**
     * Get hotel's filename.
     * @return filename associated to hotel
     */
    public String getFilename(){
        return _filename;
    }

    /**
     * Set filename for this hotel.
     * @param filename name to associate this hotel to a file
     */
    public void setFilename(String filename){
        _filename = filename;
    }

    /**
     * Get veterinarian with key given.
     * @param id veterinarian's key
     * @return veterinarian found
     */
    public Veterinarian getVeterinarian(String id){
        return _veterinarians.get(id);
    }

    /**
     * Get habitat with key given.
     * @param id habitat's key
     * @return habitat found
     */
    public Habitat getHabitat(String id){
        return _habitats.get(id);
    }

    /**
     * Check if given animal is registered in this hotel, based on the 
     * exception we want to check (if it's duplicate or unknown)
     * @param id animal's key
     * @param type indicates if we want to know if it already exists
     *              or not (for duplicate and unknown exceptions)
     * @return true if animal exists and false otherwise
     * @throws UnknownAnimalException
     */
    public boolean existsAnimal(String id) throws UnknownAnimalException{
        for(Map.Entry<String,Species> i : _species.entrySet()){
            Species species = i.getValue();
            Map<String,Animal> a = species.getListAnimals();
            for (String key : a.keySet()) {
                if (key.equalsIgnoreCase(id)) {
                    return true;
                }
            }
        }
        throw new UnknownAnimalException();
    }

    /**
     * Checks if given animal already exists
     * @param id animal key
     * @return true if it already exists
     * @throws DuplicateAnimalException
     */
    public boolean existedAnimalAlready(String id) throws DuplicateAnimalException{
        for(Map.Entry<String,Species> i : _species.entrySet()){
            Species species = i.getValue();
            Map<String,Animal> a = species.getListAnimals();
            for (String key : a.keySet()) {
                if (key.equalsIgnoreCase(id)) {
                    throw new DuplicateAnimalException();
                }
            }
        }
        return false;
    }

    /**
     * Check if given species is registered in this hotel.
     * @param id species' key
     * @return true if species exist and false otherwise
     * @throws UnknownSpeciesException
     */
    public boolean existSpecies(String id) throws UnknownSpeciesException{
        if(!_species.containsKey(id))
            throw new UnknownSpeciesException();
        return true;
    }

    /**
     * Check if already exists a species by that name and if there isn't
     * it registers a new species.
     * @param id species' key
     * @param name species' name
     * @throws DuplicateNameSpeciesException
     */
    public void existsNameSpecies(String id,String name) throws DuplicateNameSpeciesException{
        for(Species species : _species.values()){
            if (species.getName().equals(name))
                throw new DuplicateNameSpeciesException();
        }
        this.addSpecies(new Species(id, name));
    }

    /**
     * Check if given worker is registered in this hotel.
     * @param id worker's key
     * @return true if exists and false otherwise
     * @throws UnknownWorkerException
     */
    public boolean existsWorker(String id) throws UnknownWorkerException{
        if(!_employees.containsKey(id))
            throw new UnknownWorkerException();
        return true;
    }

    /**
     * Checks if employee already exists.
     * @param id worker key
     * @return true if it already exists
     * @throws DuplicateWorkerException
     */
    public boolean existedWorkerAlready(String id) throws DuplicateWorkerException{
        if(_employees.containsKey(id))
            throw new DuplicateWorkerException();
        return false;
    }

    /**
     * Check if given habitat is registered in this hotel, based on the 
     * exception we want to check (if it's duplicate or unknown)
     * @param id habitat's key
     * @param type indicates if we want to know if it already exists
     *              or not (for duplicate and unknown exceptions)
     * @return true if exists and false otherwise
     * @throws UnknownHabitatException
     */
    public boolean existsHabitat(String id) throws UnknownHabitatException{
        if(!_habitats.containsKey(id))
            throw new UnknownHabitatException();
        
        return true;
    }

    /**
     * Checks if habitat exists already.
     * @param id habitat key
     * @return true if it already exists
     * @throws DuplicateHabitatException
     */
    public boolean existedHabitatAlready(String id) throws DuplicateHabitatException {
        if(_habitats.containsKey(id))
            throw new DuplicateHabitatException();
        return false;
    }

    /**
     * Check if given vaccine is registered in this hotel.
     * @param id vaccine's key
     * @return true if exists and false otherwise
     * @throws UnknwonVaccineEception
     */
    public boolean existsVaccine(String id) throws UnknownVaccineException{
        if(!_vaccines.containsKey(id))
            throw new UnknownVaccineException();
        return true;
    }

    /**
     * Checks if vaccine exists already.
     * @param id vaccine key
     * @return true if it already exists
     * @throws DuplicateVaccineException
     */
    public boolean existedVaccineAlready(String id) throws DuplicateVaccineException{
        if(_vaccines.containsKey(id))
            throw new DuplicateVaccineException();
        return false;
    }

    /**
     * Check if given veterinarian is registered in this hotel.
     * @param id veterinarian's key
     * @return true if exists and false otherwise
     * @throws UnknownVeterinarianException
     */
    public boolean existsVeterinarian(String id) throws UnknownVeterinarianException{
        if(!_veterinarians.containsKey(id))
            throw new UnknownVeterinarianException();
        return true;
    }

    /**
     * Check if given tree is registered in this hotel.
     * @param id tree's key
     * @return true if exists and false otherwise
     */
    public boolean existsTree(String id){
        for(Map.Entry<String,Habitat> i : _habitats.entrySet()){
            Habitat habitat = i.getValue();
            Map<String,Tree> t = habitat.getTrees();
            for (String key : t.keySet()) {
                if (key.equalsIgnoreCase(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Based on the type given, add a responsability to the given 
     * worker, depending on whether it's a veterinarian or caretaker,
     * or removes a responsability from the worker.
     * 
     * @param id worker's key
     * @param responsability species or habitat's key
     * @param type ADD or REMOVE
     */
    public void treatResponsability(String id,String responsability,
                                    String type){
        if(_veterinarians.containsKey(id)){
            Veterinarian v = _veterinarians.get(id);
            Species s = _species.get(responsability);
            if (type.equals("ADD")){
                v.addSpecies(s);
                s.addResponsability(v);
            }
            else if (type.equals("REMOVE")){
                v.removeSpecies(s);
                s.removeResponsability(v);
            }
            _employees.replace(id, v);
            _species.replace(responsability, s);
        } else {
            Caretaker c = _caretakers.get(id);
            Habitat h = _habitats.get(responsability);
            if (type.equals("ADD")){
                c.updateHabitats(h);
                h.addResponsability(c);
            }
                
            else if (type.equals("REMOVE")){
                c.removeHabitats(h);
                h.removeResponsability(c);
            }
            
            _employees.replace(id, c);
            _habitats.replace(responsability, h);
        }
        this.changed();
        
    }

    public boolean isAppropriate(String id, String res, String preference) throws NotAppropriateException{
        Habitat h = _habitats.get(res);
        Species s = _species.get(res);
        if (_veterinarians.containsKey(id) && (h != null) &&
            (s == null)){
            throw new NotAppropriateException();
        }
        else if(_caretakers.containsKey(id) && (s != null) && 
            (h == null)){
            throw new NotAppropriateException();
        }
        if (preference.equals("REM")){
            if (_veterinarians.containsKey(id)){
                if(!_veterinarians.get(id).hasResponsability(res))
                    throw new NotAppropriateException();
            }
            else if (_caretakers.containsKey(id)){
                if (!_caretakers.get(id).hasResponsability(res))
                    throw new NotAppropriateException();
            }
        }
        return true;
    }

    /**
     * Check if given worker has given responsability registered.
     * 
     * @param id worker's key
     * @param res species or habitat's key (responsability)
     * @return true if has that responsability or false othewise
     */
    public boolean hasResponsabilities(String id,String res){
        try {
            if(this.existsVeterinarian(id)){
                Veterinarian v = _veterinarians.get(id);
                return v.hasResponsability(res);
            }
            else{
                Caretaker c = _caretakers.get(id);
                return c.hasResponsability(res);
            }
        } catch (UnknownVeterinarianException e) {
            return false;
        }
        
    }

    /**
     * Check if veterinarian can vaccinate specified species.
     * 
     * @param idVet veterinarian's key
     * @param idSpecies species to vaccinate
     * @return true if it is possible and false otherwise
     */
    public boolean canVaccinate(String idVet, String idAnimal)
                            throws VetNotAuthorizedException{
        Veterinarian v = _veterinarians.get(idVet);
        String idSpecies = this.findAnimal(idAnimal).getSpecies().getId();
        if(!v.hasResponsability(idSpecies))
            throw new VetNotAuthorizedException(idSpecies);
        return true;
    }

    /**
     * Check if it is possible to vaccinate withou damage.
     * 
     * @param idVaccine vaccine's key
     * @param idSpecies species to vaccinate
     * @return true if it is possible and false otherwise
     */
    public boolean canVaccinateWithoutDamage(String idV,String idAnimal){
        Vaccine v = _vaccines.get(idV);
        String idSpecies = this.findAnimal(idAnimal).getSpecies().getId();
        return v.canVaccinateSpecies(idSpecies);
    }

    /**
     * Vaccinates an animal, and creates an object type Vaccination.
     * @param idV vaccine key
     * @param idVet veterinarian key
     * @param idAnimal animal key
     */
    public void vaccinateAnimal(String idV, String idVet, String idAnimal){
        Vaccine v = _vaccines.get(idV);
        Veterinarian vet = _veterinarians.get(idVet);
        Animal a = this.findAnimal(idAnimal);
        Vaccination vaccination = new Vaccination(v, vet, a);
        vaccination.calculateDamage();
        _vaccinations.add(vaccination);
        v.updateApplications();
        vet.addVaccination(vaccination);
        this.changed();
    }

    /**
     * Calculates satisfaction of animal.
     * @param idA animal key
     * @return satisfaction
     */
    public float calculateSatisfactionAnimal(String idA){
        Animal a = this.findAnimal(idA);
        return this.calculateSatisfactionAnimal(a);
    }

    /**
     * Calculates satisfaction of animal.
     * @param a animal (necessary for global satisfaction)
     * @return satisfaction
     */
    public float calculateSatisfactionAnimal(Animal a){
        Habitat h = a.getHabitat();
        float equalSpecies = h.calculateEqualSpecies(a);
        float satisfaction = 20 + (3 * equalSpecies) -
                            (2 * (h.getNumAnimals() - equalSpecies - 1)) +
                            ((float) h.getDimension()/h.getNumAnimals()) +
                            h.calculateCorrespondence(a);
        return satisfaction;
    }

    /**
     * Calculates overall satisfaction of an employee and calls its
     * concrete satisfaction method based on the employee's type and
     * using a strategy design pattern.
     * @param idW worker key
     * @return satisfaction
     */

    public float calculateSatisfactionWorker(String idW){
        Worker w = _employees.get(idW);
        if (_veterinarians.containsKey(idW))
            this.setSatisfactionStrategy(new SatisfactionVeterinarian());
        else
            this.setSatisfactionStrategy(new SatisfactionCaretaker());
        return _satisfactionStrategy.calculateSatisfaction(w);
    }

    /**
     * Formats animal's satisfaction
     * @param idA animal key
     * @return string converted satisfaction
     */
    public String showSatisfactionAnimal(String idA){
        return "" + Math.round(this.calculateSatisfactionAnimal(idA));
    }

    /**
     * Formats worker's satisfaction
     * @param idW worker key
     * @return string converted satisfaction
     */
    public String showSatisfactionWorker(String idW){
        return "" + Math.round(this.calculateSatisfactionWorker(idW));
    }

    /**
     * Add to species the veterinarian that is responsible for it
     * @param s map of all species the vet is responsible for
     * @param v veterinarian
     */
    public void addToResponsability(Map<String,Species> s, Veterinarian v){
        for(Map.Entry<String,Species> species : s.entrySet()){
            species.getValue().addResponsability(v);
        }
    }

    /**
     * Add to habitats the caretaker that is responsible for it
     * @param h map of all habitats the caretaker is responsible for
     * @param ct caretaker
     */
    public void addToResponsability(Map<String,Habitat> h, Caretaker ct){
        for(Map.Entry<String,Habitat> habitat : h.entrySet()){
            habitat.getValue().addResponsability(ct);
        }
    }
    
    /**
     * Read text input file and create domain entities.
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    void importFile(String filename) throws ImportFileException{
        try (BufferedReader reader = new BufferedReader(new 
                                        FileReader(filename))) {
            String line;
            Map<String,Tree> nTrees = new TreeMap<String,Tree>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();  /* Remove white spaces */
                if (line.isEmpty()) {
                    continue;  
                }

                String[] parts = line.split("\\|");  
                String entityType = parts[0]; 
                /* For each entry, registers a new class object. */
                switch (entityType) {
                    case "ESPÉCIE":
                        Species newVarS = new Species(parts[1], parts[2]);
                        _species.put(parts[1], newVarS);

                        break;
                    case "ÁRVORE":
                        int age = Integer.parseInt(parts[3]);
                        int difficulty = Integer.parseInt(parts[4]);
                        if (parts[5].equals("PERENE")){
                            PerennialTrees treeVar = new 
                                    PerennialTrees(parts[1], parts[2], 
                                    age, Seasons.PRIMAVERA, difficulty);
                            treeVar.setSeasonEffort(_season);
                            nTrees.put(parts[1], treeVar);
                        }
                        else if (parts[5].equals("CADUCA")){
                            DeciduousTrees treeVar = new 
                                    DeciduousTrees(parts[1], parts[2], 
                                    age, Seasons.PRIMAVERA, difficulty);
                            treeVar.setSeasonEffort(_season);
                            nTrees.put(parts[1], treeVar);
                        }
                        break;

                    case "HABITAT":
                        int dimension = Integer.parseInt(parts[3]);
                        Habitat newVarH;
                        if (parts.length == 4){ /* no trees associated */
                            newVarH = new Habitat(parts[1], parts[2], 
                                                    dimension);
                        }
                        else{
                            Map <String,Tree> t = new TreeMap<>();
                            String[] partsHabitat = parts[4].split("\\,");
                            for (String id : partsHabitat) {
                                if (nTrees.containsKey(id)) {
                                    Tree treeNew = nTrees.get(id);
                                    t.put(id, treeNew);
                                }
                            }
                            newVarH = new Habitat(parts[1], parts[2], 
                                                    dimension, t);
                        }
                        _habitats.put(parts[1], newVarH);
                        break;

                    case "ANIMAL":
                        this.addAnimal(parts[1], parts[2], parts[3], 
                                        parts[4]);
                        break;

                    case "TRATADOR":
                        Caretaker newVarC;
                        if (parts.length == 3){ /* no responsabilities */
                            newVarC = new Caretaker(parts[1],parts[2]);
                        }
                        else{
                            Map <String,Habitat> h1 = new TreeMap<>();
                            String[] partsHabitat = parts[3].split("\\,");
                            for (String id : partsHabitat) {
                                if (_habitats.containsKey(id)) {
                                    Habitat hNew = _habitats.get(id);
                                    h1.put(id, hNew);
                                }
                            }
                            newVarC = new Caretaker(parts[1], 
                                                parts[2], h1);
                            this.addToResponsability(h1, newVarC);
                        }
                        _caretakers.put(parts[1], newVarC);
                        _employees.put(parts[1], newVarC);
                        break;

                    case "VETERINÁRIO":
                        Veterinarian newVarV;
                        if (parts.length == 3){ /* no responsabilities */
                            newVarV = new Veterinarian(parts[1],
                                                        parts[2]);
                        }
                        else{
                            Map <String,Species> s1 = new TreeMap<>();
                            String[] partsSpecies = parts[3].split("\\,");
                            for (String id : partsSpecies) {
                                if (_species.containsKey(id)) {
                                    Species sNew = _species.get(id);
                                    s1.put(id, sNew);
                                }
                            }
                            newVarV = new Veterinarian(parts[1], 
                                                    parts[2], s1);
                            this.addToResponsability(s1,newVarV);
                        }
                        _veterinarians.put(parts[1], newVarV);
                        _employees.put(parts[1], newVarV);
                        
                        break;
                    case "VACINA":
                        Vaccine newVarVac;
                        if (parts.length == 3){
                            newVarVac = new Vaccine(parts[1],
                                                parts[2]);
                        }
                        else{
                            Map <String,Species> s2=new TreeMap<>();
                            String[] partsSpecies = parts[3].split("\\,");
                            for (String id : partsSpecies) {
                                if (_species.containsKey(id)) {
                                    Species sNew = _species.get(id);
                                    s2.put(id, sNew);
                                }
                            }
                            newVarVac = new Vaccine(parts[1], 
                                                    parts[2], s2);
                        }
                        _vaccines.put(parts[1], newVarVac);
                        break;

                    default:
                        throw new UnrecognizedEntryException(filename);
                }
            }

            this.setChanged(true);
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(filename, e);
        }
    }

    /**
     * Show all habitats in the necessary format.
     * 
     * @return all habitats registered in this hotel (formatted)
     */
    public String showHabitats(){
        StringBuilder sb = new StringBuilder();
        int size = _habitats.size(); 
        int count = 0;  

        for(Map.Entry<String, Habitat> entry : _habitats.entrySet()){
            sb.append(entry.getValue());
            
            count++;  
            if (count < size) { /* new line until the last Habitat */
                sb.append("\n");  
            }
        }

        return sb.toString();
    }

    /**
     * Show all workers in the necessary format.
     * 
     * @return all workers registered in this hotel (formatted)
     */
    public String showWorkers(){
        StringBuilder sb = new StringBuilder();
        int size = _employees.size(); 
        int count = 0;  

        for(Map.Entry<String, Worker> entry : _employees.entrySet()){
            sb.append(entry.getValue());
            
            count++;  
            if (count < size) { /* new line until the last Worker */
                sb.append("\n");  
            }
        }

        return sb.toString();
    }

    /**
     * Retrieve all animals in this hotel and show in the necessary
     * format.
     * 
     * @return all animals registered in this hotel (formatted)
     */
    public String showAnimals() {
        StringBuilder sb = new StringBuilder(); 
        Map<String,Animal> sortedAnimals = new TreeMap<>();

        /* get all animals present in the hotel */
        for (Map.Entry<String, Species> entry : _species.entrySet()){
            Species species = entry.getValue();
    
            sortedAnimals.putAll(species.getListAnimals());
        }

        int size = sortedAnimals.size(); 
        int count = 0;  
        for(Map.Entry<String,Animal> animal:sortedAnimals.entrySet()){
            sb.append(animal.getValue());

            count++;  
            if (count < size) { /* new line until the last Animal */
                sb.append("\n");  
            }
        }
    
        return sb.toString();
    }
    
    /**
     * Show all vaccines in the necessary format.
     * 
     * @return all vaccines registered in this hotel (formatted)
     */
    public String showVaccines(){
        StringBuilder sb = new StringBuilder();
        int size = _vaccines.size(); 
        int count = 0;  

        for(Map.Entry<String, Vaccine> entry : _vaccines.entrySet()){
            sb.append(entry.getValue());
            
            count++;  
            if (count < size) { /* new line until the last Vaccine */
                sb.append("\n");  
            }
        }

        return sb.toString();
    }

    /**
     * Show all vaccinations in the necessary format.
     * 
     * @return all vaccinations registered in this hotel (formatted)
     */
    public String showVaccinations(){
        StringBuilder sb = new StringBuilder();
        int size = _vaccinations.size(); 
        int count = 0; 
        
        for(Vaccination v : _vaccinations){
            sb.append(v);
            
            count++;  
            if (count < size) { /* new line until the last Vaccination */
                sb.append("\n");  
            }
        }

        return sb.toString();
    }

    /**
     * Show vaccinations for a specific veterinarian
     * @param idVet veterinarian key
     * @return medical acts by veterinarian
     */
    public String showVaccinations(String idVet){
        Veterinarian vet = _veterinarians.get(idVet);
        return vet.showVaccinations();
    }

    /**
     * Calculate global satisfaction of hotel
     * @return satisfaction
     */
    public float calculateTotalSat(){
        float total = 0;
        for (Map.Entry<String,Species> species : _species.entrySet()){
            Map<String,Animal> animals = species.getValue().getListAnimals();
            for(Map.Entry<String,Animal> a : animals.entrySet()){
                total += this.calculateSatisfactionAnimal(a.getValue());
            }
        }

        for (Map.Entry<String,Worker> worker : _employees.entrySet()){
            total += this.calculateSatisfactionWorker(worker.getKey());
        }

        return total;
    }

    /**
     * Shows all medical acts done to an animal
     * @param idAnimal animal key
     * @return health record
     */
    public String showMedicalActsAnimal(String idAnimal){
        Animal animal = this.findAnimal(idAnimal);
        return animal.showVaccinations();
    }

    /**
     * Shows all vaccinations that have caused damage
     * @return damaged vaccinations
     */
    public String showWrongVaccinations(){
        StringBuilder sb = new StringBuilder();
        int count = 0; 
        int sizeDamaged = 0;
        for(Vaccination v : _vaccinations){
            if(v.getDamage() != Damage.NORMAL){
                sizeDamaged++;
            }
        }

        for(Vaccination v : _vaccinations){
            if(v.getDamage() != Damage.NORMAL){
                sb.append(v);
                count++;

                if (count < sizeDamaged) {
                    sb.append("\n");  
                }
            }                
        }

        return sb.toString();
    }

    /**
     * Shows all trees planted in habitat
     * @param idHabitat habitat key
     * @return habitat's trees
     */
    public String viewTreesHabitat(String idHabitat){
        return this.getHabitat(idHabitat).viewTrees();
    }

    /**
     * Shows all animals planted in habitat
     * @param idHabitat habitat key
     * @return habitat's animals
     */
    public String viewAnimalsHabitat(String idHabitat){
        return this.getHabitat(idHabitat).viewAnimals();
    }

}
