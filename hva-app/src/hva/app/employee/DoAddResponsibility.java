package hva.app.employee;

import hva.Hotel;
import hva.app.exceptions.NoResponsibilityException;
import hva.app.exceptions.UnknownEmployeeKeyException;
import hva.exceptions.UnknownHabitatException;
import hva.exceptions.NotAppropriateException;
import hva.exceptions.UnknownSpeciesException;
import hva.exceptions.UnknownWorkerException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoAddResponsibility extends Command<Hotel> {

    DoAddResponsibility(Hotel receiver) {
        super(Label.ADD_RESPONSABILITY, receiver);
        addStringField("key", Prompt.employeeKey());
        addStringField("responsability", Prompt.responsibilityKey());
    }

    @Override
    protected void execute() throws CommandException {

        boolean habitatException = false;
        boolean speciesException = false;

        try {

            _receiver.existsWorker(stringField("key"));

            
            try {
                _receiver.existsHabitat(stringField("responsability"));
            } catch (UnknownHabitatException e) {
                habitatException = true;
            }
        
            try {
                _receiver.existSpecies(stringField("responsability"));
            } catch (UnknownSpeciesException e) {
                speciesException = true;
            }
        
            
            if (habitatException && speciesException) {
                throw new NoResponsibilityException(stringField("key"), 
                                        stringField("responsability"));
            }

            _receiver.isAppropriate(stringField("key"),stringField("responsability"), "ADD");
            _receiver.treatResponsability(stringField("key"), 
                                stringField("responsability"), "ADD");

        } catch (UnknownWorkerException e) {
            throw new UnknownEmployeeKeyException(stringField("key"));
        } catch (NotAppropriateException e){
            throw new NoResponsibilityException(stringField("key"), 
                                        stringField("responsability"));
        }
    }

}