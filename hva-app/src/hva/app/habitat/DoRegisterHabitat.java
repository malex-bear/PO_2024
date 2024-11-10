package hva.app.habitat;

import hva.Hotel;
import hva.app.exceptions.DuplicateHabitatKeyException;
import hva.exceptions.DuplicateHabitatException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoRegisterHabitat extends Command<Hotel> {

    DoRegisterHabitat(Hotel receiver) {
        super(Label.REGISTER_HABITAT, receiver);
        addStringField("key",Prompt.habitatKey());
        addStringField("name", Prompt.habitatName());
        addIntegerField("area", Prompt.habitatArea());
    }

    @Override
    protected void execute() throws CommandException {

        try {
            _receiver.existedHabitatAlready(stringField("key"));
            if (integerField("area") > 0)
                _receiver.addHabitat(stringField("key"),
                                    stringField("name"), 
                                    integerField("area"));
            else
                _display.popup("Área do habitat tem de ser maior que 0.");
        } catch (DuplicateHabitatException e) {
            throw new DuplicateHabitatKeyException(stringField("key"));
        }
    }


}
