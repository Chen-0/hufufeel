package app;


import junit.framework.TestCase;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.service.DocumentService;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;

import java.io.File;

public class AppTest extends TestCase {

    public void test1 () {
        DocumentService.Action action = new DocumentService.Action(new File("D:\\uploads\\2222.jpg"));

        action.resize(200, 200).save();
    }
}
