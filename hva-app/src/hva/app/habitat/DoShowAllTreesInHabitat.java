package hva.app.habitat;

import hva.Hotel;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.exceptions.UnknownHabitatException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowAllTreesInHabitat extends Command<Hotel> {

    DoShowAllTreesInHabitat(Hotel receiver) {
        super(Label.SHOW_TREES_IN_HABITAT, receiver);
        addStringField("keyHabitat", Prompt.habitatKey());
    }

    @Override
    protected void execute() throws CommandException {

        try {
            _receiver.existsHabitat(stringField("keyHabitat"));
            _display.popup(_receiver.viewTreesHabitat(stringField("keyHabitat")));
        } catch (UnknownHabitatException e) {
            throw new UnknownHabitatKeyException(stringField("keyHabitat"));
        }
    }

}
