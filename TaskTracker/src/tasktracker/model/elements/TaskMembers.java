package tasktracker.model.elements;


import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TaskMembers
{

    private List<String> _otherMembersList;

    public List<String> get_otherMembersList()
    {

        return _otherMembersList;
    }

    public void set_otherMembersList(List<String> _otherMembersList)
    {

        this._otherMembersList = _otherMembersList;
    }

    /**
     * Gets the string of members, separates them with comma delimiters, then adds each member to the members list.
     * @param value The string of members.
     */
    public void setOtherMembers(String value)
    {

        _otherMembersList = new ArrayList<String>();
        if (value.matches(""))
            return;
        String[] others = value.split("(\\s+)?,(\\s+)?");
        for (String member : others)
        {
            _otherMembersList.add(member);
        }
        Collections.sort(_otherMembersList);
    }
}