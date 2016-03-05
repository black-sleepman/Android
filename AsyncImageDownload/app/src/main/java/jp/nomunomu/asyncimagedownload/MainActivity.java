package jp.nomunomu.asyncimagedownload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AsyncImageDownloadDelegate {

    // 画像が置かれているURLを入力するTextView
    static EditText URL;

    // 非同期処理によってダウンロードされた画像を表示するImageView
    static ImageView GetImagePreview;

    // ダウンロードを開始するためのButton
    static Button GetImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL = (EditText) findViewById(R.id.UrlText);
        GetImagePreview = (ImageView) findViewById(R.id.GetImageView);
        GetImageButton = (Button) findViewById(R.id.GetButton);

        GetImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 * 非同期処理クラス AsyncImageDownloadを呼び出す
                 * new AsyncImageDownload(1).execute(2);
                 * 1: AsyncImageDownloadDelegateがimplementsされているクラスを指定（クラス名.this）
                 * 2: doInBackgroundで利用するObjectを指定。「,」で区切ることで複数指定することができる。
                 */
                new AsyncImageDownload(MainActivity.this)
                        .execute(URL.getText().toString());
            }
        });
    }

    /*
     * AsyncImageDownloadDelegateで定義されたコールバック関数
     * onPostExecuteで呼び出されている
     * つまりdoInBackgroundが終了したら呼び出される
     */
    @Override
    public void SucceedDownloadImage(Bitmap result) {
        GetImagePreview.setImageBitmap(result);
    }

    /*
     * AsyncImageDownloadDelegateで定義されたコールバック関数
     * onPreExecuteで呼び出されている
     * つまりAsyncImageDownloadがexecuteされたら呼び出される
     */
    @Override
    public void StartDownloadImage() {
        Toast.makeText(getApplicationContext(), "DownloadStart", Toast.LENGTH_SHORT).show();
    }
}
