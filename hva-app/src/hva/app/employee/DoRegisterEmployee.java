package hva.app.employee;

import hva.Hotel;
import hva.app.exceptions.DuplicateEmployeeKeyException;
import hva.exceptions.DuplicateWorkerException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

class DoRegisterEmployee extends Command<Hotel> {

    DoRegisterEmployee(Hotel receiver) {
        super(Label.REGISTER_EMPLOYEE, receiver);
        addStringField("key", Prompt.employeeKey());
        addStringField("name", Prompt.employeeName());
        addOptionField("type", Prompt.employeeType(), "VET","TRT");
    }

    @Override
    protected void execute() throws CommandException {

        try {
            _receiver.existedWorkerAlready(stringField("key"));
            _receiver.addWorker(stringField("key"),stringField("name"),
                                stringField("type"));
            
        } catch (DuplicateWorkerException e) {
            throw new DuplicateEmployeeKeyException(stringField("key"));
        }
    }

}
