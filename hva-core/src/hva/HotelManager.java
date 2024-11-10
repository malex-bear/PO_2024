package hva;

import java.io.*;
import hva.exceptions.*;

/**
 * Class that represents the hotel application.
 */
public class HotelManager {

    /** This is the current hotel. */
    private Hotel _hotel = new Hotel();

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (!changed())
            return;
        if (_hotel.getFilename() == null || _hotel.getFilename().equals(""))
            throw new MissingFileAssociationException();
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                                    new FileOutputStream(_hotel.getFilename())))) {
            oos.writeObject(_hotel);
            _hotel.setChanged(false);
        }
    }

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _hotel.setFilename(filename);
        save();
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream ois = new ObjectInputStream(
            new BufferedInputStream(new FileInputStream(filename)))) {
            
            _hotel = (Hotel) ois.readObject();
            _hotel.setFilename(filename);
            _hotel.setChanged(false);
        }
        catch (FileNotFoundException | ClassNotFoundException e) {
            throw new UnavailableFileException(filename);
        }
        catch (IOException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file.
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException{
        _hotel.importFile(filename);
    }

    public void createHotel(){
        _hotel = new Hotel();
    }

    public Hotel getHotel(){
        return _hotel;
    }

    public String calculateSatisfactionHotel(){
        return "" + Math.round(_hotel.calculateTotalSat());
    }

    public boolean changed() {
        if (_hotel != null)
            return _hotel.hasChanged();
        return false;
    }

    public String advanceSeason(){
        return _hotel.advanceSeason();
    }

}
