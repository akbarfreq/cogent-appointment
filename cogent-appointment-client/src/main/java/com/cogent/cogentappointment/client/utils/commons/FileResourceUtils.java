package com.cogent.cogentappointment.client.utils.commons;

import java.io.File;

/**
 * @author Rupak
 */
public class FileResourceUtils {

    public File convertResourcesFileIntoFile(String fileLocation){
        File file = new File(getClass().getClassLoader().getResource(fileLocation).getFile());
        return file;

    }
}
