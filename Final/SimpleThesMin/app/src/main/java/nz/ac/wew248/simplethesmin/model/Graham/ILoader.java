package nz.ac.wew248.simplethesmin.model.Graham;

import android.content.Context;
import java.util.Dictionary;

public interface ILoader {
    Dictionary<String, String> loadLineFromFile(Context context, String theFilename);
}
