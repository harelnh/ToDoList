package harel.com.ToDoList;

import java.util.Date;

/**
 * Created by harel on 10/12/2016.
 */
public class Note {

    private int _id;
    private String _title;
    private String _textItself;
    private Date   _date;


    // creating an empty constructor

    public Note (){}

    public Note(String _title, String _textItself, Date _date) {
        this._title = _title;
        this._textItself = _textItself;
        this._date = _date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_textItself() {
        return _textItself;
    }

    public void set_textItself(String _textItself) {
        this._textItself = _textItself;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }
}
