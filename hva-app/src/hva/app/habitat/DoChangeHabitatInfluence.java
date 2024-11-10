package hva.app.habitat;

import hva.Hotel;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.app.exceptions.UnknownSpeciesKeyException;
import hva.exceptions.UnknownHabitatException;
import hva.exceptions.UnknownSpeciesException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

class DoChangeHabitatInfluence extends Command<Hotel> {

    DoChangeHabitatInfluence(Hotel receiver) {
        super(Label.CHANGE_HABITAT_INFLUENCE, receiver);
        addStringField("key", Prompt.habitatKey());
        addStringField("keySpecies", hva.app.animal.Prompt.speciesKey());
        addOptionField("influence", Prompt.habitatInfluence(), "POS","NEG","NEU");
    }

    @Override
    protected void execute() throws CommandException {

        try {
            _receiver.existsHabitat(stringField("key"));

            _receiver.existSpecies(stringField("keySpecies"));

            _receiver.changeInfluenceHabitat(stringField("key"),
                                            stringField("keySpecies"),
                                            stringField("influence"));
            
        } catch (UnknownHabitatException e) {
            throw new UnknownHabitatKeyException(stringField("key"));
        } catch (UnknownSpeciesException e) {
            throw new UnknownSpeciesKeyException(stringField("keySpecies"));
        }
    }

}
