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

import android.graphics.Bitmap;

/**
 * A class representing a photo requirement. Currently only holds one photo.
 * 
 * @author Jeanine Bonot
 * 
 */
// TODO: Add multiple photo property.
public class PhotoRequirement extends Requirement {

	private static final long serialVersionUID = 1L;

	private Bitmap photo;

	/** Gets the bitmap photo */
	public Bitmap getPhoto() {
		return this.photo;
	}
	
	/** Sets the bitmap photo */
	public void setPhoto(Bitmap photo){
		this.photo = photo;
	}

	/**
	 * Fulfill the requirement by adding a photo.
	 * @return True if the photo 
	 */
	@Override
    public boolean fulfill() {
		// TODO: Prompt user to take a photo and set it here.
		
		// Set photo with a dummy image.
		this.photo = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);
		
		return super.fulfill();
	}
}
