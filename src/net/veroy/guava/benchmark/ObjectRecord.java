package net.veroy.guava.benchmark;

public class ObjectRecord {
    private int _myId;
    private int _objId;
    private int _age;
    private int _allocTime;
    private int _deathTime;
    private String _myType;

    public ObjectRecord( int myId,
                         int objId,
                         int age,
                         int allocTime,
                         int deathTime,
                         String myType) {
        super();
        this._myId = myId;
        this._objId = objId;
        this._age = age;
        this._allocTime = allocTime;
        this._deathTime = deathTime;
        this._myType = myType;
    }

    public ObjectRecord() {
        super();
        this._myId = 0;
        this._objId = 0;
        this._age = 0;
        this._allocTime = 0;
        this._deathTime = 0;
        this._myType = "None";
    }

    public int get_myId() {
        return _myId;
    }
    public void set_myId(int _myId) {
        this._myId = _myId;
    }
    public int get_age() {
        return _age;
    }
    public void set_age(int _age) {
        this._age = _age;
    }
    public int get_objId() {
        return _objId;
    }
    public void set_objId(int _objId) {
        this._objId = _objId;
    }
    public int get_allocTime() {
        return _allocTime;
    }
    public void set_allocTime(int _allocTime) {
        this._allocTime = _allocTime;
    }
    public int get_deathTime() {
        return _deathTime;
    }
    public void set_deathTime(int _deathTime) {
        this._deathTime = _deathTime;
    }
    public String get_myType() {
        return _myType;
    }
    public void set_myType(String _myType) {
        this._myType = _myType;
    }


}
//    public ObjectRecord( int myId,
//                         int objId,
//                         int age,
//                         int allocTime,
//                         int deathTime,
//                         String myType ) {
//
//        super();
//        this.
//    }
