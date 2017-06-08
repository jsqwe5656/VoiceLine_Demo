package zbf.voiceline_demo;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 文件过滤类
 * Created by zbf on 2017/6/7.
 */

class FileNameSelector implements FilenameFilter
{
    String extension = ".";

    public FileNameSelector(String fileExtensionNoDot)
    {
        extension += fileExtensionNoDot;
    }

    @Override
    public boolean accept(File dir, String name)
    {
        return name.endsWith(extension);
    }
}
