package hva.app.animal;

import hva.Hotel;
import hva.Species;
import hva.app.exceptions.DuplicateAnimalKeyException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.exceptions.DuplicateAnimalException;
import hva.exceptions.DuplicateNameSpeciesException;
import hva.exceptions.UnknownAnimalException;
import hva.exceptions.UnknownHabitatException;
import hva.exceptions.UnknownSpeciesException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoRegisterAnimal extends Command<Hotel> {

    DoRegisterAnimal(Hotel receiver) {
        super(Label.REGISTER_ANIMAL, receiver);
        addStringField("key", Prompt.animalKey());
        addStringField("name", Prompt.animalName());
        addStringField("speciesKey", Prompt.speciesKey());
        addStringField("habitat", hva.app.habitat.Prompt.habitatKey());
    }

    @Override
    protected final void execute() throws CommandException {

        try {
            _receiver.existedAnimalAlready(stringField("key"));

            _receiver.existSpecies(stringField("speciesKey"));

            _receiver.existsHabitat(stringField("habitat"));

            _receiver.addAnimal(stringField("key"),stringField("name"),
                    stringField("speciesKey"),stringField("habitat"));
            
        } catch (DuplicateAnimalException e) {
            throw new DuplicateAnimalKeyException(stringField("key"));
        } catch (UnknownHabitatException e) {
            throw new UnknownHabitatKeyException(stringField("habitat"));
        } catch (UnknownSpeciesException e) {
            Form form = new Form();
            form.addStringField("sName",Prompt.speciesName());
            form.parse();
            try {
                _receiver.existsNameSpecies(stringField("speciesKey"),form.stringField("sName"));
                _receiver.addAnimal(stringField("key"),stringField("name"),
                    stringField("speciesKey"),stringField("habitat"));
            } catch (DuplicateNameSpeciesException dupE) {
                dupE.printStackTrace();
            }
        }

    }

}
