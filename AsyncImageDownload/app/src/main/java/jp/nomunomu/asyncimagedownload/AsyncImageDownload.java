package jp.nomunomu.asyncimagedownload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * 非同期処理を行うクラス AsyncImageDownload extends AsyncTask<Param, Progress, Result>
 *
 * onPreExecute()                   ->  事前準備の処理を記述する
 *
 * Param                            ->  doInBackgroundに渡される引数の型指定。execute()で渡す型と一致しなければならない
 * doInBackground(Params...)        ->	バックグラウンドで行う処理を記述する。この関数はサブスレッドで動作する。
 *
 * Progress                         ->  onProgressUpdateに渡される引数の型指定。
 * onProgressUpdate(Progress...)    ->	進捗状況をUIスレッドで表示する処理を記述する。この関数はメインスレッドで動作する。
 *
 * Result                           ->  onPostExecuteに渡させる引数の型指定。onProgressUpdateでreturnされるObjectの型と一致しなければならない
 * onPostExecute(Result)            ->  バックグラウンド処理が完了し、UIスレッドに反映する処理を記述する。この関数はメインスレッドで動作する。
 *
 * 実行順は以下のようになります。
 * 1. onPreExecute()
 * 2. doInBackground()
 * (3. onProgressUpdate() ※doInBackground()で、publishProgress()が呼ばれた場合に処理されます。)
 * 4. onPostExecute()
 */

public class AsyncImageDownload extends AsyncTask<String, Void, Bitmap> {

    private AsyncImageDownloadDelegate callback = null;

    public AsyncImageDownload(AsyncImageDownloadDelegate callback) { this.callback = callback; }

    public void print(Object object) { Log.d("AsyncImageDownload", object.toString()); }

    public void e_print(Object object) { Log.e("AsyncImageDownload", object.toString()); }

    /*
     * 非同期処理実行前に呼ばれる
     */
    @Override
    protected void onPreExecute() {
        callback.StartDownloadImage();
        super.onPreExecute();
    }

    /*
     * 非同期処理が完了したら呼ばれる
     * つまりdoInBackgroundでreturnが呼ばれたら呼ばれる
     */
    @Override
    protected void onPostExecute(Bitmap result) {
        callback.SucceedDownloadImage(result);
    }

    /*
     * 非同期処理そのもの
     * execute()で指定した引数が順番にparamsに入る
     * new AsyncImageDownload(this).execute("a", "b");
     * params[0] -> "a", params[1] -> "b"
     * 引数の型はextendsのAsyncTaskの第一引数で決めます
     * ex.) extends AsyncTask<Void, Void, Void> -> doInBackground(Void... params) {}
     */
    @Override
    protected Bitmap doInBackground(String... params) {

        print("download_url >> " + params[0]);

        Bitmap bmp = null;

        try {
            URL url = new URL(params[0]);

            // HttpURLConnection インスタンス生成
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // タイムアウト設定
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);

            // リクエストメソッド
            urlConnection.setRequestMethod("GET");

            // リダイレクトを自動で許可しない設定
            urlConnection.setInstanceFollowRedirects(false);

            // ヘッダーの設定(複数設定可能)
            urlConnection.setRequestProperty("Accept-Language", "jp");

            // 接続
            urlConnection.connect();

            int resp = urlConnection.getResponseCode();

            switch (resp) {

                case HttpURLConnection.HTTP_OK:
                    InputStream is = urlConnection.getInputStream();
                    bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    e_print("switch >> HttpURLConnection.HTTP_OK");
                    break;

                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    e_print("switch >> HttpURLConnection.HTTP_UNAUTHORIZED");
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e_print("downloadImage error");
            e.printStackTrace();
        }

        return bmp;
    }
}
