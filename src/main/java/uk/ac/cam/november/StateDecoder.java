package uk.ac.cam.november;

import uk.ac.cam.november.buttons.ButtonNames;


// Just for tests, not meant to be in the final product.
public class StateDecoder {
    public String getRecent(String buttonType)
    {
        switch(buttonType)
        {
        case ButtonNames.WATER_DEPTH:
            return "5";
        case ButtonNames.WIND_SPEED:
            return "10";
        default:
            return "What was that?";        
        }
    }
}
