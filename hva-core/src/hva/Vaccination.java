package hva;
import java.io.Serializable;
/**Class representing a vaccination. */
public class Vaccination implements Serializable{
    private Veterinarian _vet;
    private Animal _animal;
    private Vaccine _vaccine;
    private Damage _damage = Damage.NORMAL;

    public Vaccination(Vaccine vaccine, Veterinarian vet, Animal animal){
        _vaccine = vaccine;
        _vet = vet;
        _animal = animal;
    }

    public Damage getDamage(){
        return _damage;
    }

    public void calculateDamage(){
        _damage = _vaccine.determineDamage(_animal.getSpecies());
        _animal.updateHealthRecords(this);
    }

    @Override
    public String toString(){
        return "REGISTO-VACINA|" + _vaccine.getId() + "|" + 
                _vet.getId() + "|" + _animal.getSpecies().getId();
    }


}
