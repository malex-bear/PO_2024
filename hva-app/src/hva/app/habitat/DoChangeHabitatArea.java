package hva.app.habitat;

import hva.Hotel;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.exceptions.UnknownHabitatException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoChangeHabitatArea extends Command<Hotel> {

    DoChangeHabitatArea(Hotel receiver) {
        super(Label.CHANGE_HABITAT_AREA, receiver);
        addStringField("key", Prompt.habitatKey());
        addIntegerField("area",Prompt.habitatArea());
    }

    @Override
    protected void execute() throws CommandException {

        try {
            _receiver.existsHabitat(stringField("key"));
            if(integerField("area") > 0){
                _receiver.setAreaHabitat(stringField("key"),
                                    integerField("area"));
            } else {
                _display.popup("Área tem de ser maior que 0. ");
            }
            
        } catch (UnknownHabitatException e) {
            throw new UnknownHabitatKeyException(stringField("key"));
        }

    }

}
