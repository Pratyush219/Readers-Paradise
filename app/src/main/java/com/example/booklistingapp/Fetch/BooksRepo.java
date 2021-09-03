package com.example.booklistingapp.Fetch;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.booklistingapp.ListDefaults;
import com.example.booklistingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BooksRepo {
    private final ArrayList<BookInfo> bookInfos = new ArrayList<>();
    private final Application mApplication;
    private static BooksRepo mInstance;

    private BooksRepo(Application mApplication) {
        this.mApplication = mApplication;
    }
    public static BooksRepo getInstance(Application application){
        if(mInstance == null) mInstance = new BooksRepo(application);
        return mInstance;
    }

    public void fetchBooks(String search, List<String> filters, BooksUpdater updater){
        if(search == null || search.equals("")) return;
        bookInfos.clear();
        int c = 0;
        boolean hasEnglishFilter = true;
        for(int i = 0; i < filters.size(); i++){
            if(ListDefaults.languages.containsKey(filters.get(i))){
                c++;
            }
        }
        if(c > 0 && !filters.contains("English")) hasEnglishFilter = false;
        Log.i("LanguageFIlters", hasEnglishFilter + "");
        String refactoredSearchString = search.toLowerCase().replace(" ", "+");
        String initialURI = "https://www.googleapis.com/books/v1/volumes?q=";
        String fields = "&fields=items(volumeInfo/title,volumeInfo/authors,volumeInfo/description,volumeInfo/imageLinks,volumeInfo/infoLink, volumeInfo/language)";
        String maxResults = "&maxResults=30";
        String subjectHeader = "&subject:";
        String languageHeader = "&langRestrict=";
        String searchQuery;
        String addFilter;
        StringBuilder searchAppendedString = new StringBuilder(initialURI);
        RequestQueue queue = VolleySingleton.getInstance(mApplication).getRequestQueue();
        searchAppendedString.append(refactoredSearchString);
        for(int i = -1; i < filters.size(); i++){
            String key = mApplication.getString(R.string.api_key);
            if(i == -1){
                searchQuery = searchAppendedString + fields + key + maxResults;
            }
            else if(ListDefaults.categories.contains(filters.get(i))){
                addFilter = filters.get(i).toLowerCase().replace(" ", "+");
                searchQuery = searchAppendedString + subjectHeader + addFilter + fields + key + maxResults;
            }
            else{
                addFilter = Objects.requireNonNull(ListDefaults.languages.get(filters.get(i))).toLowerCase();
                searchQuery = searchAppendedString + languageHeader + addFilter + fields + key + maxResults;
            }

            boolean finalHasEnglishFilter = hasEnglishFilter;
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    searchQuery,
                    response -> {
                        try {
                            JSONArray booksArray = (new JSONObject(response)).getJSONArray("items");
                            JSONObject item1;
                            int len = booksArray.length();
                            int j = 0;
                            while (j < len) {
                                String title = "", language = "", thumbnailUrl = "", bookUrl = "";
                                StringBuilder authorNames = new StringBuilder();
                                try {
                                    item1 = booksArray.getJSONObject(j);
                                    title = item1.getJSONObject("volumeInfo").getString("title");
                                    JSONArray array = item1.getJSONObject("volumeInfo").getJSONArray("authors");
                                    language = item1.getJSONObject("volumeInfo").getString("language");
                                    for (int k = 0; k < array.length() - 1; k++) {
                                        authorNames.append(array.getString(k)).append(", ");
                                    }
                                    authorNames.append(array.getString(array.length() - 1));
                                    bookUrl = item1.getJSONObject("volumeInfo").getString("infoLink");
                                    thumbnailUrl = item1.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                finally {
                                    String alt = title.toLowerCase();
                                    String[] searchLowerCase = search.toLowerCase().split(" ");
                                    int index = 0;
                                    boolean contains = false;
                                    while(!contains && index < searchLowerCase.length){
                                        if(alt.contains(searchLowerCase[index])) {
                                            contains = true;
                                            BookInfo info = new BookInfo(title, bookUrl, thumbnailUrl, language, authorNames);
                                            if (!hasEntry(info)) bookInfos.add(info);
                                        }
                                        index++;
                                    }
                                    j++;
                                }
                            }
                            if(!finalHasEnglishFilter) onLanguageFirstSelection();
                            Collections.shuffle(bookInfos);
                            updater.updateBooks(bookInfos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(mApplication, "Something went wrong!", Toast.LENGTH_SHORT).show());
            queue.add(request);
        }
    }
    private boolean hasEntry(BookInfo info) {
        String tgt = info.getName();
        for(BookInfo obj1: bookInfos){
            String src = obj1.getName();
            if(src.equals(tgt))
                return true;
        }
        return false;
    }

    private void onLanguageFirstSelection(){
        int i = 0;
        while(i < bookInfos.size()){
            if(bookInfos.get(i).getLanguageCode().equals("en")){
                bookInfos.remove(i);
            }
            else{
                i++;
            }
        }
    }
}
