package hva.app.habitat;

import hva.Hotel;
import hva.app.exceptions.DuplicateTreeKeyException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.exceptions.DupTreeException;
import hva.exceptions.UnknownHabitatException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

class DoAddTreeToHabitat extends Command<Hotel> {

    DoAddTreeToHabitat(Hotel receiver) {
        super(Label.ADD_TREE_TO_HABITAT, receiver);
        addStringField("key",Prompt.habitatKey());
        addStringField("treeKey", Prompt.treeKey());
        addStringField("treeName", Prompt.treeName());
        addIntegerField("treeAge", Prompt.treeAge());
        addIntegerField("treeDif", Prompt.treeDifficulty());
        addOptionField("treeType", Prompt.treeType(),"CADUCA","PERENE");
    }

    @Override
    protected void execute() throws CommandException {

        try {
            _receiver.existsHabitat(stringField("key"));    
            _display.popup("" + _receiver.addTreeToHabitat(stringField("key"),
                        stringField("treeKey"), stringField("treeName"), 
                        integerField("treeAge"), integerField("treeDif"), 
                        stringField("treeType")));

        } catch (UnknownHabitatException e) {
            throw new UnknownHabitatKeyException(stringField("key"));
        } catch (DupTreeException e){
            throw new DuplicateTreeKeyException(stringField("treeKey"));
        }

    }

}
