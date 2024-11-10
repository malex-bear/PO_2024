package hva.app.vaccine;

import hva.Hotel;
import hva.app.exceptions.UnknownVaccineKeyException;
import hva.app.exceptions.UnknownVeterinarianKeyException;
import hva.app.exceptions.VeterinarianNotAuthorizedException;
import hva.exceptions.UnknownAnimalException;
import hva.exceptions.UnknownVaccineException;
import hva.exceptions.UnknownVeterinarianException;
import hva.exceptions.VetNotAuthorizedException;
import hva.app.exceptions.UnknownAnimalKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoVaccinateAnimal extends Command<Hotel> {

    DoVaccinateAnimal(Hotel receiver) {
        super(Label.VACCINATE_ANIMAL, receiver);
        addStringField("keyV", Prompt.vaccineKey());
        addStringField("vet", Prompt.veterinarianKey());
        addStringField("animal", hva.app.animal.Prompt.animalKey());

    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.existsVaccine(stringField("keyV"));
            _receiver.existsVeterinarian(stringField("vet"));
            _receiver.existsAnimal(stringField("animal"));

            _receiver.canVaccinate(stringField("vet"), 
                                    stringField("animal"));

            if(!_receiver.canVaccinateWithoutDamage(stringField("keyV"),
                                            stringField("animal"))){
                _display.popup(Message.wrongVaccine(stringField("keyV"), 
                                            stringField("animal")));
            }

            _receiver.vaccinateAnimal(stringField("keyV"), 
                            stringField("vet"), stringField("animal"));

        } catch (UnknownVaccineException e) {
            throw new UnknownVaccineKeyException(stringField("keyV"));
        } catch (UnknownVeterinarianException e){
            throw new UnknownVeterinarianKeyException(stringField("vet"));
        } catch (UnknownAnimalException e){
            throw new UnknownAnimalKeyException(stringField("animal"));
        } catch (VetNotAuthorizedException e){
            throw new VeterinarianNotAuthorizedException(stringField("vet"),
                                                        e.getId());
        }

    }

}
