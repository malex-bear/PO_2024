package hva.exceptions;
import java.io.Serial;

public class VetNotAuthorizedException extends Exception{
    
    @Serial
    private static final long serialVersionUID = 202407081733L;

    private String _idSpecies;

    public VetNotAuthorizedException(String id){
        _idSpecies = id;
    }

    public String getId(){
        return _idSpecies;
    }
}
