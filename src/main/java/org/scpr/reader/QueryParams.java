package org.scpr.reader;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

public class QueryParams extends HashMap<String, String>
{

    // This method is here because we're leaning on HashMap's implementation
    // of Serializable, which turns it back into a HashMap.
    // Another option would be to implement Serializable ourselves,
    // but, you know...
    public static QueryParams buildFromHashMap(HashMap<String, String> hashMap)
    {
        QueryParams params = new QueryParams();
        params.merge(hashMap);
        return params;

    }


    public QueryParams merge(HashMap<String, String>... updates)
    {
        for (HashMap<String, String> update : updates)
        {
            if (update == null) continue;

            for (Map.Entry<String, String> entry : update.entrySet())
            {
                this.put(entry.getKey(), entry.getValue());
            }
        }

        return this;
    }


    public RequestParams toParams()
    {
        RequestParams params = new RequestParams();

        for (Map.Entry<String, String> entry : this.entrySet())
        {
            params.put(entry.getKey(), entry.getValue());
        }

        return params;
    }


    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : this.entrySet())
        {
            hashMap.put(entry.getKey(), entry.getValue());
        }

        return hashMap;
    }

}
