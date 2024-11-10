package hva;
import java.io.Serializable;

/**abstract class representing an employee. */
public abstract class  Worker implements Serializable {
    private String _id;
    private String _name;

    public Worker(String id, String name){
        _id = id;
        _name = name;
    }

    public String getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    @Override
    public String toString(){
        return _id + "|" + _name;
    }

}
