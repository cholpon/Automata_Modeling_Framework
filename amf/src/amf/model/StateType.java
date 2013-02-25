package amf.model;

public class StateType
{
	String Name;
	public StateType(String name)
	{
		Name = name;		
	}
	public String getName() {
		return Name;
	}
	
	// use this constants to set state type
	static final StateType BASIC_STATE = new StateType("basic state");
	static final StateType START_STATE = new StateType("start state");
	static final StateType END_STATE = new StateType("end state");
}
