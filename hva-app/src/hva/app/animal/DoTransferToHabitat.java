package hva.app.animal;

import hva.Hotel;
import hva.app.exceptions.UnknownAnimalKeyException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.exceptions.UnknownAnimalException;
import hva.exceptions.UnknownHabitatException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoTransferToHabitat extends Command<Hotel> {

    DoTransferToHabitat(Hotel hotel) {
        super(Label.TRANSFER_ANIMAL_TO_HABITAT, hotel);
        addStringField("key", Prompt.animalKey());
        addStringField("habitat", hva.app.habitat.Prompt.habitatKey());
    }

    @Override
    protected final void execute() throws CommandException {

        try {
            _receiver.existsAnimal(stringField("key"));
            _receiver.existsHabitat(stringField("habitat"));
            _receiver.transferAnimalHabitats(stringField("key"), 
                                        stringField("habitat"));

        } catch (UnknownAnimalException e) {
            throw new UnknownAnimalKeyException(stringField("key"));
        } catch (UnknownHabitatException e) {
            throw new UnknownHabitatKeyException(stringField("habitat"));
        }
    }

}
