package org.scpr.reader;

import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HtmlParser extends AsyncTask<String, Integer, Spanned>
{

    private TextView mTextView;
    private Article mArticle;
    private ProgressBar mProgressBar;


    public HtmlParser(TextView textView, Article article, ProgressBar progressBar)
    {
        mTextView = textView;
        mArticle = article;
        mProgressBar = progressBar;
    }


    @Override
    protected void onPreExecute()
    {
        if (mArticle.getParsedBody() != null)
        {
            cancel(true);
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected Spanned doInBackground(String... strings)
    {
        return Html.fromHtml(strings[0]);
    }


    @Override
    protected void onPostExecute(Spanned result)
    {
        mProgressBar.setVisibility(View.GONE);
        mArticle.setParsedBody(result);
        mTextView.setText(result);
    }

}
