package hva.app.search;

import hva.Hotel;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.exceptions.UnknownHabitatException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowAnimalsInHabitat extends Command<Hotel> {

    DoShowAnimalsInHabitat(Hotel receiver) {
        super(Label.ANIMALS_IN_HABITAT, receiver);
        addStringField("key",hva.app.habitat.Prompt.habitatKey());
    }

    @Override
    protected void execute() throws CommandException {

        try {
            _receiver.existsHabitat(stringField("key"));
            _display.popup(_receiver.viewAnimalsHabitat(stringField("key")));

        } catch (UnknownHabitatException e) {
            throw new UnknownHabitatKeyException(stringField("key"));
        }
    }

}
