package com.abhpan91.emergencyhelper;

public class person {
	 //private variables
    int _id;
    String _name;
    String _phone_number;
     
    // Empty constructor
    public person() {
		// TODO Auto-generated constructor stub
	}
    // constructor
    public person(int id, String name, String _phone_number){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
    }
     
    // constructor
    public person(String name, String _phone_number){
        this._name = name;
        this._phone_number = _phone_number;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
     
    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }
     
    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }

}
