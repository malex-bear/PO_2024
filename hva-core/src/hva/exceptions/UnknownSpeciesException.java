package hva.exceptions;

import java.io.Serial;

public class UnknownSpeciesException extends Exception{
    
    @Serial
    private static final long serialVersionUID = 202407081733L;

    private String _idSpecies;

    public UnknownSpeciesException(){
        super();
    }

    public UnknownSpeciesException(String id){
        _idSpecies = id;
    }

    public String getSpeciesId(){
        return _idSpecies;
    }
}
