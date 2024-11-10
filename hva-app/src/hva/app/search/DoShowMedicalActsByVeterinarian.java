package hva.app.search;

import hva.Hotel;
import hva.app.exceptions.UnknownVeterinarianKeyException;
import hva.exceptions.UnknownVeterinarianException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowMedicalActsByVeterinarian extends Command<Hotel> {

    DoShowMedicalActsByVeterinarian(Hotel receiver) {
        super(Label.MEDICAL_ACTS_BY_VET, receiver);
        addStringField("key",hva.app.employee.Prompt.employeeKey());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.existsVeterinarian(stringField("key"));
            _display.popup(_receiver.showVaccinations(stringField("key")));
        } catch (UnknownVeterinarianException e) {
            throw new UnknownVeterinarianKeyException(stringField("key"));
        }
    }

}
