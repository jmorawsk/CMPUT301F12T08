package tasktracker.model.elements;

/**
 * TaskTracker
 * 
 * Copyright 2012 Jeanine Bonot, Michael Dardis, Katherine Jasniewski,
 * Jason Morawski
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class for handling the members of a task
 * @author Jason
 *
 */
public class TaskMembers
{

    private List<String> _otherMembersList;

    /**
     * Gets the members from TaskMembers
     * @return the list of members
     */
    public List<String> get_otherMembersList()
    {
        return _otherMembersList;
    }

    /**
     * Sets the members for TaskMembers
     * 
     * @param _otherMembersList 
     */
    public void set_otherMembersList(List<String> _otherMembersList)
    {
        this._otherMembersList = new ArrayList<String>();
        this._otherMembersList.addAll(_otherMembersList);
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